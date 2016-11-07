package com.pg.pgguardkiki.dao;

public class TableField {
    /**
     * 字段名
     */
    private String name;
    /**
     * 类型
     */
    private String type;
    /**
     * 长度
     */
    private int length;
    /**
     * 是否是主键
     */
    private boolean isKey = false;
    /**
     * 是否允许为空
     */
    private boolean enableNull = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isKey() {
        return isKey;
    }

    public void setKey(boolean key) {
        isKey = key;
    }

    public boolean isEnableNull() {
        return enableNull;
    }

    public void setEnableNull(boolean enableNull) {
        this.enableNull = enableNull;
    }

}
