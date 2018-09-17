package com.houarizegai.generatetablefx.java.models;

public class ColumnTable {
    private String title;
    private String varName;
    private String width;

    public ColumnTable() {

    }

    public ColumnTable(String title, String varName, String width) {
        this.title = title;
        this.varName = varName;
        this.width = width;
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
}
