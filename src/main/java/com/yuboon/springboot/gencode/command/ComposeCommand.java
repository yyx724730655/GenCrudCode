package com.yuboon.springboot.gencode.command;

import cn.hutool.core.collection.CollectionUtil;
import com.yuboon.springboot.gencode.meta.Table;
import com.yuboon.springboot.gencode.output.CodeOutput;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.List;

/**
 * 组合生成命令
 *
 * @author yuboon
 * @version v1.0
 * @date 2020/01/05
 */
public abstract class ComposeCommand extends Command {

    protected List<SingleCommand> singleCommands = CollectionUtil.newArrayList();

    // 执行命令
    @Override
    public void execute(Table table, SpringTemplateEngine springTemplateEngine, CodeOutput codeOutput){
        for(SingleCommand singleCommand : singleCommands){
            singleCommand.execute(table,springTemplateEngine,codeOutput);
        }
    }

}
