package com.houarizegai.generatetablefx.java.controllers;

import com.houarizegai.generatetablefx.java.models.ColumnTable;
import com.houarizegai.generatetablefx.java.models.TableInfo;
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

        result.append(generateVariables());
        result.append(generateInitTableMethod());
        result.append(generateClassData());
        resultArea.setText(result.toString());
    }

    /* Start generate result part */

    private String generateVariables() { // Generate table & column Attribute
        StringBuilder result = new StringBuilder();

        result.append("@FXML\nprivate JFXTreeTableView ")
                .append(tableInfo.getVarName())
                .append(";");

        result.append("\nprivate JFXTreeTableColumn<")
                .append(tableInfo.getClassDataName())
                .append(", String> ");

        StringBuilder cols = new StringBuilder();
        for(int i = 0; i < columnsTable.length; i++) {
            cols.append(columnsTable[i].getVarName() + ", ");
        }

        result.append(cols.subSequence(0, cols.length() - 2))
                .append(";\n");

        return result.toString();
    }

    private String generateInitTableMethod() {
        StringBuilder result = new StringBuilder();
        result.append("\nprivate void initTable() {");

        for(int i = 0; i < columnsTable.length; i++) {
            // Column new object
            result.append("\n\t" + columnsTable[i].getVarName())
                    .append(" = new JFXTreeTableColumn<>(\"")
                    .append(columnsTable[i].getTitle())
                    .append("\");");

            // Column width
            result.append("\n\t" + columnsTable[i].getVarName())
                    .append(".setPrefWidth(")
                    .append(columnsTable[i].getWidth())
                    .append(");");

            // Column data
            result.append("\n\t" + columnsTable[i].getVarName())
                    .append(".setCellValueFactory((TreeTableColumn.CellDataFeatures<")
                    .append(tableInfo.getClassDataName())
                    .append(", String> param) -> param.getValue().getValue().")
                    .append(columnsTable[i].getTitle())
                    .append(");");

            result.append("\n");
        }

        double tableWidth = 0d;

        /* Adding column to table */
        result.append("\n\t")
                .append(tableInfo.getVarName())
                .append(".getColumns().addAll(");

        StringBuilder cols = new StringBuilder();
        for(ColumnTable col : columnsTable) {
            cols.append(col.getVarName()).append(", ");
            tableWidth += Double.parseDouble(col.getWidth());
        }
        result.append(cols.substring(0, cols.length() - 2)).append(");");

        // Table width
        result.append("\n\t" + tableInfo.getVarName())
                .append(".setPrefWidth(")
                .append(tableWidth)
                .append(");");

        // Make default show root of table
        result.append("\n\t").append(tableInfo.getVarName())
                .append(".setShowRoot(false);");

        result.append("\n}\n"); // Close initTable function

        return result.toString();
    }

    private String generateClassData() {
        StringBuilder result = new StringBuilder();
        result.append("\nclass ")
                .append(tableInfo.getClassDataName())
                .append(" extends RecursiveTreeObject<")
                .append(tableInfo.getClassDataName())
                .append("> {");

        for(ColumnTable col : columnsTable) {
            result.append("\n\tStringProperty ")
                    .append(col.getTitle())
                    .append(";");
        }

        result.append("\n\n\tpublic " + tableInfo.getClassDataName() + "(");
        StringBuilder cols = new StringBuilder();
        for(ColumnTable col : columnsTable) {
            cols.append("String ")
                    .append(col.getTitle())
                    .append(", ");
        }
        result.append(cols.substring(0, cols.length() - 2))
                .append(") {");

        for(ColumnTable col : columnsTable) {
            result.append("\n\t\tthis.")
                    .append(col.getTitle())
                    .append(" = new SimpleStringProperty(")
                    .append(col.getTitle())
                    .append(");");
        }
        result.append("\n\t}");
        result.append("\n}"); // close class

        return result.toString();
    }

    /* End generate result part */

    @FXML // Add new column to ListView
    private void onAddColumn() {
        HBox colBox = null;
        try {
            colBox = FXMLLoader.load(getClass().getResource("/com/houarizegai/generatetablefx/resources/views/ColumnBox.fxml"));
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