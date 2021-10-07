package com.example.newall;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelloController {
//    @FXML
//    Scene scene;
    public ArrayList<Map<String, String>> cloneItems;
    File fl;
    @FXML
    private Button Convert;
    @FXML
    private Text output;
    @FXML
    private Text selectedFile;
    @FXML
    private ArrayList<Map<String, String>> customList;
    @FXML
    private ScrollPane ScrollPane;

    @FXML
    protected void onSelectButtonClick() {
//        SelectButton.setText("hi");
        FileChooser fileChooser = new FileChooser();
        Stage stage = new Stage();
        File file = fileChooser.showOpenDialog(stage);
        fl = file;
        selectedFile.setText(file.getName());
    }

    @FXML
    protected void onConvertButtonClick(){
       File file = fl;
        if (file != null) {
//            selectedFile.setText(file.getName());
            Convertor conv = new Convertor();
            try {
                conv.convert(file);
                cloneItems = conv.maps;
                customList = cloneItems;
                output.setText(customList.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void onCostumizeSelectionButtonClick(){
        File converted = new File("N:\\MINE\\CS\\newAll\\src\\main\\java\\com\\example\\newall\\converted.txt");
        try {
            Scanner sc = new Scanner(converted);
            String fl = "";
            Boolean start = false;
            while(sc.hasNextLine()){
                fl = fl + sc.nextLine();

            }
            output.setText(fl);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static int indexOf(Pattern pattern, String s) {
        Matcher matcher = pattern.matcher(s);
        return matcher.find() ? matcher.start() : -1;
    }


}
