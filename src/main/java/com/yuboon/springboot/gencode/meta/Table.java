package com.yuboon.springboot.gencode.meta;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

import java.util.List;
import java.util.Map;

/**
 * 表元数据
 *
 * @author yuboon
 * @version v1.0
 * @date 2020/01/04
 */
public class Table {

    // 编码
    private String code;

    // 类名（生成的JAVA类文件名称）(暂时用不到20210106)
    private String className;

    // 备注
    private String comment;

    // 创建时间
    private String createDate = DateUtil.today();

    // 字段列
    private List<TableColumn> columnList;

    //一些公用参数，用于填充前端和后端各个文件中的信息
    private Map gyMap;
    
    public String getClassName() {
        this.className = StrUtil.upperFirst(StrUtil.toCamelCase(code));
        return className;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<TableColumn> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<TableColumn> columnList) {
        this.columnList = columnList;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Map getGyMap() {
        return gyMap;
    }

    public void setGyMap(Map gyMap) {
        this.gyMap = gyMap;
    }
}
