package com.yuboon.springboot.gencode.configuration;

import com.yuboon.springboot.gencode.command.Command;
import com.yuboon.springboot.gencode.command.etaxcloudCommand.GenBoCommand;
import com.yuboon.springboot.gencode.command.etaxcloudCommand.GenBoImplCommand;
import com.yuboon.springboot.gencode.command.etaxcloudCommand.GenCtrlCommand;
import com.yuboon.springboot.gencode.command.etaxcloudCommand.GenDaoCommand;
import com.yuboon.springboot.gencode.command.etaxcloudCommand.GenDaoImplCommand;
import com.yuboon.springboot.gencode.command.etaxcloudCommand.GenServiceCommand;
import com.yuboon.springboot.gencode.command.etaxcloudCommand.GenVoCommand;
import com.yuboon.springboot.gencode.command.etaxcloudCommand.GenetaxcloudCommand;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 命令注册类
 *
 * @author yuboon
 * @version v1.0
 * @date 2020/01/04
 */
public class CommandRegister {

    private Map<String, Command> commandMap = new LinkedHashMap<String, Command>();

    public CommandRegister(){
        /* 默认注册的命令
        registerCommand(new GenControllerCommand());
        registerCommand(new GenEntityCommand());
        registerCommand(new GenControllerAndEntityCommand());*/
        //etaxcloud命令
        registerCommand(new GenetaxcloudCommand());
        registerCommand(new GenCtrlCommand());
        registerCommand(new GenServiceCommand());
        registerCommand(new GenBoCommand());
        registerCommand(new GenBoImplCommand());
        registerCommand(new GenDaoCommand());
        registerCommand(new GenDaoImplCommand());
        registerCommand(new GenVoCommand());
    }

    /**
     * 注册功能
     */
    public void registerCommand(Command command){
        commandMap.put(command.getCommandCode(),command);
    }

    /**
     * 根据命令码获取命令
     * @param commandCode
     * @return
     */
    public Command getCommand(String commandCode){
        return commandMap.get(commandCode);
    }

    /**
     * 根据命令码获取命令
     * @param commandClass
     * @return
     */
    public Command getCommand(Class<? extends Command> commandClass){
        Iterator<Command> iterator = commandMap.values().iterator();
        while(iterator.hasNext()){
            Command command = iterator.next();
            if(command.getClass()  ==  commandClass){
                return command;
            }
        }
        return null;
    }

    /**
     * 根据命令码获取命令集合
     * @return
     */
    public Collection<Command> getCommands(){
        return commandMap.values();
    }

}
