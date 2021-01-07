package com.yuboon.gencode.test;

import com.yuboon.springboot.gencode.GenCode;
import com.yuboon.springboot.gencode.configuration.GenCodeConfiguration;
import com.yuboon.springboot.gencode.command.deafult.GenVoCommand;
import com.yuboon.springboot.gencode.output.ConsoleOutput;

import java.util.Scanner;

/**
 * 此处为类介绍
 *
 * @author yuboon
 * @version v1.0
 * @date 2020/01/05
 */
public class GenCodeTest {

    public static void main(String[] args) {
        GenCodeConfiguration genCodeConfiguration = new GenCodeConfiguration();
        GenCode genCode = new GenCode(genCodeConfiguration);
        genCode.process(new Scanner(System.in),"sys_user",new GenVoCommand(),new ConsoleOutput());
    }

}
