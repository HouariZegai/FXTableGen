package com.houarizegai.fxtablegen.controllers;

import com.houarizegai.fxtablegen.models.ColumnTable;
import com.houarizegai.fxtablegen.models.TableInfo;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML // Title of Table
    private JFXTextField fieldTitleTable;

    @FXML // This Box contain boxes of column
    private JFXListView columnsBox;

    @FXML // Counter contain number of column
    private Text txtCounterCol;

    @FXML
    private TextArea resultArea;

    private TableInfo tableInfo;
    private ColumnTable[] columnsTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onAddColumn();
    }

    // Get information from views and put it in objects
    private void extractDataOfTable() {

        columnsTable = new ColumnTable[columnsBox.getItems().size()];

        for(int i = 0; i < columnsTable.length; i++) {
            HBox hBox = (HBox) columnsBox.getItems().get(i);
            columnsTable[i] = new ColumnTable();
            columnsTable[i].setTitle(((JFXTextField) hBox.getChildren().get(1)).getText().trim());
            columnsTable[i].setVarName("col" + columnsTable[i].getTitle().substring(0, 1).toUpperCase()
            + columnsTable[i].getTitle().substring(1));
            columnsTable[i].setWidth(((JFXTextField) hBox.getChildren().get(2)).getText().trim());
        }

        tableInfo = new TableInfo();
        tableInfo.setTitle(fieldTitleTable.getText().trim());
        tableInfo.setVarName(("table" + tableInfo.getTitle().substring(0, 1).toUpperCase() + tableInfo.getTitle().substring(1)));
        tableInfo.setClassDataName(tableInfo.getTitle().substring(0, 1).toUpperCase() + tableInfo.getTitle().substring(1) + "Table");
    }

    @FXML // Generate result
    private void onGenerate() {
        extractDataOfTable();

        StringBuilder result = new StringBuilder();

        result.append(genVars())
                .append(genInitTableMethod())
                .append(genClassData());

        resultArea.setText(result.toString());
    }

    /* Start generate result part */

    private String genVars() { // generate table & column variables
        StringBuilder result = new StringBuilder();

        result.append(String.format("@FXML\nprivate JFXTreeTableView %s;", tableInfo.getVarName()));

        result.append(String.format("\nprivate JFXTreeTableColumn<%s, String> ", tableInfo.getClassDataName()));

        StringBuilder cols = new StringBuilder();
        for(int i = 0; i < columnsTable.length; i++) {
            cols.append(columnsTable[i].getVarName()).append(", ");
        }
        result.append(cols.subSequence(0, cols.length() - 2)).append(";\n");

        return result.toString();
    }

    private String genInitTableMethod() {
        StringBuilder result = new StringBuilder();
        result.append("\nprivate void initTable() {");

        for(int i = 0; i < columnsTable.length; i++) {
            // Column new object
            result.append(String.format("\n\t%s = new JFXTreeTableColumn<>(\"%s\");", columnsTable[i].getVarName(), columnsTable[i].getTitle()));

            // Column width
            result.append(String.format("\n\t%s.setPrefWidth(%s);", columnsTable[i].getVarName(), columnsTable[i].getWidth()));

            // Column data
            result.append(String.format("\n\t%s.setCellValueFactory((TreeTableColumn.CellDataFeatures<%s, String> param) -> param.getValue().getValue().%s);",
                    columnsTable[i].getVarName(), tableInfo.getClassDataName(), columnsTable[i].getTitle()));

            result.append("\n");
        }

        double tableWidth = 0d;

        /* Adding column to table */
        result.append(String.format("\n\t%s.getColumns().addAll(", tableInfo.getVarName()));

        StringBuilder cols = new StringBuilder();
        for(ColumnTable col : columnsTable) {
            cols.append(col.getVarName()).append(", ");
            tableWidth += Double.parseDouble(col.getWidth());
        }
        result.append(cols.substring(0, cols.length() - 2)).append(");");

        // table width
        result.append(String.format("\n\t%s.setPrefWidth(%s);", tableInfo.getVarName(), tableWidth));

        // default show root of table
        result.append(String.format("\n\t%s.setShowRoot(false);", tableInfo.getVarName()));

        // close initTable function
        result.append("\n}\n");

        return result.toString();
    }

    private String genClassData() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("\nclass %s extends RecursiveTreeObject<%s> {", tableInfo.getClassDataName(), tableInfo.getClassDataName()));

        for(ColumnTable col : columnsTable) {
            result.append(String.format("\n\tStringProperty %s;", col.getTitle()));
        }

        // constructor method
        result.append(String.format("\n\n\tpublic %s(", tableInfo.getClassDataName()));
        StringBuilder cols = new StringBuilder();
        for(ColumnTable col : columnsTable) {
            cols.append(String.format("String %s, ", col.getTitle()));
        }
        result.append(cols.substring(0, cols.length() - 2))
                .append(") {");

        for(ColumnTable col : columnsTable) {
            result.append(String.format("\n\t\tthis.%s = new SimpleStringProperty(%s);", col.getTitle(), col.getTitle()));
        }

        result.append("\n\t}"); // close constructor
        result.append("\n}"); // close class

        return result.toString();
    }

    /* End generate result part */

    @FXML // Add new column to ListView
    private void onAddColumn() {
        HBox colBox = null;
        try {
            colBox = FXMLLoader.load(getClass().getResource("/fxml/ColumnBox.fxml"));
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        columnsBox.getItems().add(colBox);
        txtCounterCol.setText(String.valueOf(columnsBox.getItems().size()));
    }

    @FXML // Copy the result to the clipboard
    private void onCopy() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(resultArea.getText().trim());
        clipboard.setContent(content);
    }

}