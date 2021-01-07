package com.yuboon.springboot.gencode.template.[(${xmname})];

import com.yuboon.springboot.gencode.template.CodeTemplate;

/**
 * [(${name})]模板
 *
 * @author yyx
 * @version v1.0
 * @date 2021/01/05
 */
public class [(${main})]CodeTemplate extends CodeTemplate {

    public [(${main})]CodeTemplate() {
        this.tplName = "[(${name})]模板";
        this.tplPath = "templates/[(${xmname})]/[(${main})].tpl";
        this.fileTag = "[(${main})]";
        this.fileSuffix = ".java";
    }

}
