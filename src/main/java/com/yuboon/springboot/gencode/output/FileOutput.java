package com.yuboon.springboot.gencode.output;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import com.yuboon.springboot.gencode.meta.Table;
import com.yuboon.springboot.gencode.template.CodeTemplate;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件输出
 *
 * @author yuboon
 * @version v1.0
 * @date 2020/01/04
 */
public class FileOutput extends CodeOutput {

    public FileOutput() {
        this.outputCode = "2";
    }

    @Override
    public void out(Table table, String content, CodeTemplate template) {
        String dir = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath()+"/生成测试/";
        //使用模板中的文件名，并修正模板
        Pattern r = Pattern.compile("\\{文件名前缀:.*(?=\\})");
        Matcher m = r.matcher(content);
        String wjName="";
        while (m.find()) {
            // group(0)或group()将会返回整个匹配的字符串（完全匹配）；group(i)则会返回与分组i匹配的字符
            System.out.println(" matches \"" + m.group(0) + " 起始位置是" + m.start() + "和" + m.end());
            wjName=content.substring(m.start(),m.end()).split(":")[1];
            System.out.println("文件名是"+wjName);
            content=content.replace(content.substring(m.start(),m.end()+1),"");//修正模板
        }
        String fileName = dir + wjName;
        File file = new File(fileName);
        FileUtil.writeBytes(content.getBytes(),file);
        Console.log("文件位置:" + file.getAbsolutePath());
    }
}
