package com.yuboon.springboot.gencode.template.deafult;

import com.yuboon.springboot.gencode.template.CodeTemplate;

/**
 * 控制类模板
 *
 * @author yuboon
 * @version v1.0
 * @date 2020/01/04
 */
public class ControllerCodeTemplate extends CodeTemplate {

    public ControllerCodeTemplate() {
        this.tplName = "控制器模板";
        this.tplPath = "templates/default/Controller.tpl";
        this.fileTag = "Controller";
        this.fileSuffix = ".java";
    }

}
