package com.yuboon.springboot.gencode.command.[(${xmname})];

import com.yuboon.springboot.gencode.meta.Table;

import java.util.Map;

/**
 * [(${name})]生成命令
 *
 * @author yyx
 * @version v1.0
 * @date 2021/01/05
 */
public class Gen[(${main})]Command extends SingleCommand {

    public Gen[(${main})]Command() {
        this.commandName = "[(${main})]生成命令";
        this.commandCode = "[(${bh})]";
        this.template = new [(${main})]CodeTemplate();
    }

    @Override
    public void process(Table table, Map map) {
       // 留给用户自定义处理过程，比如一些自定义参数的处理加一些控制等等，此处暂不需要
    }
}
