package com.pg.pgguardkiki.dao;


import java.util.ArrayList;
import java.util.List;

public class TableConfig {
    /**
     * table name
     */
    private String name;
    /**
     * table的描述信息
     */
    private String description;
    /**
     * table的属性（TableField）集合
     */
    private List<TableField> fieldList = new ArrayList<>();

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFieldList(List<TableField> value) {
        this.fieldList = value;
    }

    public String getName() {

        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<TableField> getFieldList() {
        return fieldList;
    }

    public void addField(TableField field) {
        fieldList.add(field);
    }
}
