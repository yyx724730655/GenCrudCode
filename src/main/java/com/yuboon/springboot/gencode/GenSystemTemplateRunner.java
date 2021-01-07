package com.yuboon.springboot.gencode;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Console;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 根据编写好的某项目的template模板生成对应的command类和template类
 *
 * @author yyx
 * @version v1.0
 * @date 2021/01/05
 */
public class GenSystemTemplateRunner {

    public void run(Scanner scanner,String xmname,String[] filenames)throws Exception{
        String dir = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath()+"/"+xmname;
        File dirFile = new File(dir);
        if (!dirFile.exists()){
            dirFile.mkdirs();//创建项目目录
        }
        File commandDir=new File(dirFile+"/"+xmname+"Command");
        commandDir.mkdir();//创建command目录
        File templateDir=new File(dirFile+"/"+xmname+"Template");
        templateDir.mkdir();//创建template目录
        Map<String,Object> variablesMap=new HashMap();
        variablesMap.put("xmname",xmname);
        StringBuffer dtthStr=new StringBuffer();//动态替换
        SpringTemplateEngine springTemplateEngine=new SpringTemplateEngine();
        for (int i = 0; i < filenames.length; i++) {
            String filename=filenames[i].substring(0,filenames[i].indexOf("."));
            variablesMap.put("main",filename);
            variablesMap.put("name",filename);
            variablesMap.put("bh",i+1);
            Context context = new Context();
            context.setVariables(variablesMap);
            
            //生成command类
            InputStream commandIs = this.getClass().getClassLoader().getResourceAsStream("templates/system/command.tpl");
            String commandTpl = IoUtil.read(commandIs, "UTF-8");
            String commandResult = springTemplateEngine.process(commandTpl,context);
            File commandFile = new File(commandDir+"/"+"Gen"+filename+"Command.java");
            FileUtil.writeBytes(commandResult.getBytes(),commandFile);
            Console.log("command文件位置:" + commandFile.getAbsolutePath());

            //生成template类
            InputStream templateIs = this.getClass().getClassLoader().getResourceAsStream("templates/system/template.tpl");
            String templateTpl = IoUtil.read(templateIs, "UTF-8");
            String templateResult = springTemplateEngine.process(templateTpl,context);
            File templateFile = new File(templateDir+"/"+filename+"CodeTemplate.java");
            FileUtil.writeBytes(templateResult.getBytes(),templateFile);
            Console.log("template文件位置:" + templateFile.getAbsolutePath());

            dtthStr.append("singleCommands.add(new Gen"+filename+"Command());\n\t");
        }
        //生成composeCommand类，用于一键生成所有模块文件
        Context context = new Context();
        context.setVariables(variablesMap);
        InputStream composeIs = this.getClass().getClassLoader().getResourceAsStream("templates/system/composeCommand.tpl");
        String composeTpl = IoUtil.read(composeIs, "UTF-8");
        composeTpl=composeTpl.replace("动态替换",dtthStr.toString());
        String composeResult = springTemplateEngine.process(composeTpl,context);
        File composeFile = new File(dirFile+"/"+"Gen"+xmname+"Command.java");
        FileUtil.writeBytes(composeResult.getBytes(),composeFile);
        Console.log("composeCommand文件位置:" + composeFile.getAbsolutePath());
        Console.log("---------------------");
        Console.log("command类和template类生成完成，对应复制到 /command/项目名/ 和 /template/项目名/ 下，再运行GenCodeRunner来生成项目文件吧");
        Console.log("---------------------");
    }
    
    public static void main(String[] args)throws Exception {
        GenSystemTemplateRunner genSystemTemplateRunner=new GenSystemTemplateRunner();
        Scanner scanner=new Scanner(System.in);
        Console.log("系统启动成功，请输入项目名称");
        Console.log("---------------------");
        while (scanner.hasNext()){
            String xmname=scanner.next();
            File dir= new File(genSystemTemplateRunner.getClass().getClassLoader().getResource("templates/" + xmname).getPath());
            String[] files=dir.list();
            if (files==null){
                Console.log("目录不存在，找不到模板");
            }else {
                Console.log("共找到"+files.length+"个文件");
                new GenSystemTemplateRunner().run(scanner,xmname,files);
            }
            Console.log("生成完毕，输入项目名称重新开始");
            Console.log("---------------------");
        }
    }

}
