package com.houarizegai.generatetablefx.java.controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private JFXTextField fieldTableName, fieldTitleTable;

    @FXML
    private VBox columnsBox;

    @FXML
    private TextArea resultArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void onGenerate() {
        StringBuilder result = new StringBuilder();

        String tableName = fieldTableName.getText().trim();
        result.append("@FXML\n\tprivate JFXTreeTableView table" +
                tableName.substring(0, 1).toUpperCase() + tableName.substring(1) + ";");

        result.append("\n");
        result.append("@FXML\n\tprivate JFXTreeTableColumn<?, String> ");

        StringBuilder cols = new StringBuilder();
        for(int i = 0; i < columnsBox.getChildren().size(); i++) {
            HBox hBox = (HBox) columnsBox.getChildren().get(i);
            String colTitle = ((JFXTextField) hBox.getChildren().get(1)).getText().trim();
            String ColWidth = ((JFXTextField) hBox.getChildren().get(2)).getText().trim();
            cols.append(colTitle + "Col, ");
        }

        result.append(cols.subSequence(0, cols.length() - 2) + ";");

        resultArea.setText(result.toString());
    }

    @FXML
    private void onCopy() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(resultArea.getText().trim());
        clipboard.setContent(content);
    }

}
