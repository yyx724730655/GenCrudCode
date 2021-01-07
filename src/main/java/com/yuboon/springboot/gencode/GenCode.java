package com.yuboon.springboot.gencode;

import cn.hutool.core.lang.Console;
import com.yuboon.springboot.gencode.command.Command;
import com.yuboon.springboot.gencode.configuration.GenCodeConfiguration;
import com.yuboon.springboot.gencode.meta.MetaData;
import com.yuboon.springboot.gencode.meta.Table;
import com.yuboon.springboot.gencode.meta.TableColumn;
import com.yuboon.springboot.gencode.output.CodeOutput;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.dialect.SpringStandardDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;
import org.yaml.snakeyaml.Yaml;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * 代码生成主类
 *
 * @author yuboon
 * @version v1.0
 * @date 2020/01/04
 */
public class GenCode {

    // 全局配置
    private GenCodeConfiguration configuration;

    // 模板引擎
    protected SpringTemplateEngine springTemplateEngine;

    public GenCode(GenCodeConfiguration configuration){
        this.configuration = configuration;
        this.initTemplateEngine();
    }

    /**
     * 初始化模板
     */
    private void initTemplateEngine(){
        springTemplateEngine = new SpringTemplateEngine();
        IDialect dialect = new SpringStandardDialect();
        springTemplateEngine.setDialect(dialect);
        // 文本解析器
        StringTemplateResolver resolverText = new StringTemplateResolver();
        resolverText.setCacheable(true);
        resolverText.setTemplateMode(TemplateMode.TEXT);
        // 添加解析器
        springTemplateEngine.addTemplateResolver(resolverText);
    }

    /**
     * 命令处理
     * @param tableName
     * @param command
     * @param codeOutput
     */
    public void process(Scanner scanner,String tableName,Command command,CodeOutput codeOutput){
        Table tableInfo = MetaData.getTableInfo(tableName,configuration.getTypeMapping());
        Console.log("\n请输入你创建的存放tpl文件的文件夹名称，统一使用项目名");
        String foldName=scanner.next();
        //装载自定义参数
        this.loadArgProperties(scanner,foldName,tableInfo);
        //装载动态遍历参数
        this.loadDtblProperties(scanner,foldName,tableInfo);
        Console.log("\n");
        Console.log("开始生成....");
        command.execute(tableInfo,springTemplateEngine,codeOutput);
        Console.log("生成完成....");
    }

    /**
     * 命令处理
     * @param voName
     * @param command
     * @param codeOutput
     */
    public void processByVo(Scanner scanner,String voName, Command command, CodeOutput codeOutput){
        Console.log("\n请输入你创建的存放tpl文件的文件夹名称，和Vo对象所在的文件夹名称一致，统一使用项目名");
        String foldName=scanner.next();
        Object voObject= null;
        try {
            voObject = Class.forName("com.yuboon.springboot.gencode.vo."+foldName+"."+voName).getConstructor().newInstance();
        } catch (Exception e) {
            Console.error("没有找到com.yuboon.springboot.gencode.vo."+foldName+"."+voName+"类");
            e.printStackTrace();
        }
        Class voClass= voObject.getClass();
        Table table=new Table();
        List<TableColumn> columnList=new ArrayList<>();
        table.setCode(voClass.getName());
        table.setColumnList(columnList);
        Field[] fields=voClass.getDeclaredFields();
        //根据VO里的实例变量生成TableColumn实例
        for (Field field : fields) {
            TableColumn tableColumn=new TableColumn();
            tableColumn.setCode(field.getName());
            tableColumn.setAttrName(null);
            tableColumn.setType(field.getType().getTypeName());
            tableColumn.setComment(null);
            columnList.add(tableColumn);
        }
        //装载自定义参数
        this.loadArgProperties(scanner,foldName,table);
        //装载动态遍历参数
        this.loadDtblProperties(scanner,foldName,table);
        Console.log("\n");
        Console.log("开始生成....");
        command.execute(table,springTemplateEngine,codeOutput);
        Console.log("生成完成....");
    }

    /**
     * 往table里装载args.yaml配置文件里的自定义参数
     * @param table
     */
    private void loadArgProperties(Scanner scanner,String foldName,Table table){
        //加载配置文件args.yaml里的自定义参数
        Yaml yaml = new Yaml();
        Map<String, Object> ret = (Map<String, Object>) yaml.load(GenCode.class.getClassLoader().getResourceAsStream("templates/"+foldName+"/args.yaml"));
        if (ret!=null){
            LinkedHashMap<String,String > zdyMap=new LinkedHashMap<>();
            Map<String,Object> argMap = (Map<String,Object> )ret.get("args");
            Console.log("开始查找自定义参数");
            //装载自定义参数
            if (argMap!=null&&argMap.size()>0){
                Console.log("找到一些需要填入的参数，用于填充模板，需要的参数有：");
                for(Map.Entry entry:argMap.entrySet()){
                    System.out.print(entry.getKey()+"("+entry.getValue()+"),");
                    zdyMap.put(entry.getKey().toString(),"");
                }
                Console.log("\n请输入各个参数，中间用逗号间隔:");
                String inputArgs=scanner.next();
                String[] args=inputArgs.split(",");
                while (args.length!=zdyMap.size()){
                    Console.log("输入参数个数("+args.length+")与所需个数("+zdyMap.size()+")不同，请重新输入:");
                    args=scanner.next().split(",");
                }
                int index=0;
                for (String key:zdyMap.keySet()) {
                    zdyMap.put(key,args[index++]);
                }
            }else {
                Console.log("未找到自定义参数");
            }
            table.setGyMap(zdyMap);//table里装载自定义参数
        }else {
            Console.log("没有找到自定义参数配置文件（args.yaml）,继续生成");
        }
    }

    /**
     * 往table里装载args.yaml配置文件里的动态遍历参数
     * @param table
     */
    private void loadDtblProperties(Scanner scanner,String foldName,Table table){
        //加载配置文件args.yaml里的动态遍历参数
        Yaml yaml = new Yaml();
        Map<String, Object> ret = (Map<String, Object>) yaml.load(GenCode.class.getClassLoader().getResourceAsStream("templates/"+foldName+"/args.yaml"));
        if (ret!=null){
            Map<String,Object> dtblMap = (Map<String,Object> )ret.get("dtbl");
            Console.log("开始查找动态遍历自定义参数");
            //装载动态遍历参数
            if (dtblMap!=null&&dtblMap.size()>0){
                //循环所有"动态遍历参数"
                for (Map.Entry<String,Object> entry:dtblMap.entrySet()){
                    StringBuffer zblResult=new StringBuffer();//当前子遍历的结果
                    Map<String,Object> zblMap= (Map<String, Object>) entry.getValue();
                    String blContent= (String) zblMap.get("content");//获取到需要遍历的内容模板
                    Map<String,Object> paramMap= (Map<String, Object>) zblMap.get("parameter");//替代模板中{param}的参数
                    if (paramMap.containsKey("bean")){ //如果是使用bean来替代，就把param分别用bean的实例变量来替代
                        String beanPath= (String) paramMap.get("bean");
                        Console.log("找到需要动态遍历的自定义参数"+entry.getKey()+"，使用的是"+beanPath+"中的实例变量");
                        Class beanClass=null;
                        try {
                            beanClass=Class.forName(beanPath);
                        } catch (ClassNotFoundException e) {
                            Console.log("未找到"+beanPath+"路径下的类！");
                            e.printStackTrace();
                        }
                        for (Field declaredField : beanClass.getDeclaredFields()) {
                            zblResult.append(blContent.replace("{param}",declaredField.getName())).append("\n");
                        }
                    }else {//默认是使用args来替代，需要在控制台输入遍历用的参数，用逗号分隔
                        Console.log("没有配置bean，需要手动输入进行遍历的参数，用逗号分隔，请输入:");
                        String paramstr=scanner.next();
                        String[] paramArr=paramstr.split(",");
                        Console.log("你输入的参数是"+paramstr+",动态遍历"+paramArr.length+"次");
                        for (String param : paramArr) {
                            zblResult.append(blContent.replace("{param}",param)).append("\n");
                        }
                    }
                    table.getGyMap().put(entry.getKey(),zblResult.toString());
                }
            }else {
                Console.log("未找到动态遍历自定义参数");
            }
        }else {
            Console.log("没有找到自定义参数配置文件（args.yaml）,继续生成");
        }
    }
}
