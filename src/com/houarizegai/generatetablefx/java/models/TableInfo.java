package com.houarizegai.generatetablefx.java.models;

public class TableInfo {
    private String title;
    private String varName;
    private String width;
    private String classDataName;

    public TableInfo() {

    }

    public TableInfo(String title, String varName, String width, String classDataName) {
        this.title = title;
        this.varName = varName;
        this.width = width;
        this.classDataName = classDataName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getClassDataName() {
        return classDataName;
    }

    public void setClassDataName(String tableDataName) {
        this.classDataName = tableDataName;
    }
}
