package com.yuboon.springboot.gencode.output;


import cn.hutool.core.lang.Console;
import com.yuboon.springboot.gencode.meta.Table;
import com.yuboon.springboot.gencode.template.CodeTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 控制台输出
 *
 * @author yuboon
 * @version v1.0
 * @date 2020/01/04
 */
public class ConsoleOutput extends CodeOutput {

    public ConsoleOutput() {
        this.outputCode = "1";
    }

    @Override
    public void out(Table table, String content, CodeTemplate template) {
        //修正模板，删除配置文件名的那行
        Pattern r = Pattern.compile("\\{文件名前缀:.*}");
        Matcher m = r.matcher(content);
        while (m.find()) {
            // group(0)或group()将会返回整个匹配的字符串（完全匹配）；group(i)则会返回与分组i匹配的字符
            System.out.println(" matches \"" + m.group(0) + " 起始位置是" + m.start() + "和" + m.end());
            String wjName=content.substring(m.start(),m.end()).split(":")[1];
            System.out.println("文件名是"+wjName);
            content=content.replace(content.substring(m.start(),m.end()),"");//修正模板
        }
        Console.log("\n");
        Console.log("-----------------------------");
        Console.log(content);
        Console.log("-----------------------------");
    }
}
