package com.example.newAll;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
    ArrayList<HashMap<String, String>> finals = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> selectedAudits = new ArrayList<HashMap<String, String>>();
    ArrayList allResponses = new ArrayList<String>();
    private Boolean allSelected;
    private ArrayList<String> selectedItem = new ArrayList<String>();
    @FXML
    private Label status;

    @FXML
    private Button genFile;

    @FXML
    private Text selectedItems;

    @FXML
    private ScrollPane descriPane;

    String forConversion;
    ArrayList descriptions = new ArrayList<>();

    @FXML
    private CheckBox selectAll;

    @FXML
    SearchableComboBox listView;

    @FXML
    private Button removeButton;

    @FXML
    private Text customItems;
    private List<String> its;

    @FXML
    protected void onSelectButtonClick() {
        FileChooser fileChooser = new FileChooser();
        Stage stage = new Stage();
        File file = fileChooser.showOpenDialog(stage);
        status.setText("Converting "+file.getPath());
        forConversion = file.getPath();
    }

    @FXML
    protected void onSelectedButtonClick(){
        if (!selectedItem.contains(listView.getSelectionModel().getSelectedItem().toString())){
            selectedItem.add(listView.getSelectionModel().getSelectedItem().toString());
            selectedItems.setText(String.valueOf(selectedItem));
            for (HashMap<String, String> mp : finals){
                if (selectedItem.contains(mp.get("description"))){
                    selectedAudits.add(mp);
                }
            }
        }

        System.out.println("Array len: "+selectedItem.size());
    }

    @FXML
    public void onSelectAllButtonClick(){
        if (selectAll.isSelected()){
            allSelected = true;
            selectedItems.setText(listView.getItems().toString());
            selectedAudits = finals;
        }else if (!selectAll.isSelected()){
            selectedItems.setText(String.valueOf(selectedItem));
        }
    }

    @FXML
    public void onRemoveButtonClick(){
        if (selectedItem.contains(listView.getSelectionModel().getSelectedItem().toString())){
            selectedItem.remove(listView.getSelectionModel().getSelectedItem().toString());

        }else{
            selectedItem.remove(selectedItem.size()-1);

        }
        selectedItems.setText(String.valueOf(selectedItem));
    }

    @FXML
    protected void onGenFileButtonClick() throws IOException {
//        File audit_json = new File("audit_json.json");
        //open file
//        String classpath = System.getProperty("java.class.path");
        String classpath = "audit_json.json";
        FileWriter fw = new FileWriter(classpath);
        fw.write("[\n");
        Integer arg_count = 0;
        for (HashMap<String, String> selectedmp : selectedAudits){
            arg_count++;
            Integer len = selectedmp.keySet().size();
            Integer counter = 0;
//            if (selectedItem.contains(mp.get("description"))){
//            System.out.println("M: "+mp.get("description"));

            fw.write("\t{\n");
            for (String kk : selectedmp.keySet()){
                counter++;
                fw.write("\t\t\""+kk+"\":");
                String value = selectedmp.get(kk)
                        .replace("\\", "\\\\")
                        .replace("\n","\\n")
                        .replace("\"", "\\\"");
                if (counter == len) {
                    fw.write("\t\t\"" + value + "\"\n");
                }else{
                    fw.write("\t\t\""+value+"\",\n");
                }
            }
            if (arg_count == selectedAudits.size())
                fw.write("\t}\n");
            else
                fw.write("\t},\n");
//            }

        }
        fw.write("]");
        fw.close();

        status.setText("File generated");
    }

    @FXML
    protected void onConvertButtonClick(){
        ArrayList<String> items = new ArrayList<>();
        String[] params = new String[]{"description", "info", "reference", "see_also", "collection", "fieldsSelector",
                "query", "expect", "solution", "severity", "system", "type", "file", "regex", "cmd"};
        ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
        try {
            File audit_file = new File(forConversion);
            Scanner sc = new Scanner(audit_file);     //file to be scanned
            Map<String, String> map = new HashMap<String, String>();

            String audit = "";
            Boolean start = false;
            while (sc.hasNextLine()) {    //returns true if and only if scanner has another token
                String this_line = sc.nextLine();

                String REGEX = "</custom_item>";
                int idx = indexOf(Pattern.compile(REGEX), this_line);
                if (idx >= 0) {
                    start = false;
                    items.add(audit);
                    audit = "";
                }

                if (start) {
                    audit += this_line + "\n";
                }

                REGEX = "<custom_item>";
                idx = indexOf(Pattern.compile(REGEX), this_line);
                if (idx >= 0) {
                    start = true;
                }

            }

        } catch (FileNotFoundException e) {
            System.out.println("No");
            e.printStackTrace();
        }

//        System.out.println("===================");
        for (String item : items) {
            ArrayList<Integer> indexes_begin = new ArrayList<Integer>();
            ArrayList<Integer> indexes_end = new ArrayList<Integer>();
            HashMap<String, String> data = new HashMap<String, String>();
            for (String one_param: params){
                int idx = indexOf(Pattern.compile(one_param+"[\\s]+:"), item);
//                System.out.println("=="+idx+"|"+one_param);
                if (idx>=0) {
                    indexes_begin.add(idx);
                    indexes_end.add(idx+one_param.length());
                }
            }
            Collections.sort(indexes_begin);
            Collections.sort(indexes_end);
            for(Integer i=1; i<indexes_begin.size();i++){
                int x =indexes_end.get(i-1);
                int y =indexes_begin.get(i);
                String processed = process_value(item.subSequence(x, y).toString());
                int data_begin = indexes_begin.get(i - 1);
                int data_end = indexes_end.get(i - 1);
                data.put(item.subSequence(data_begin, data_end).toString(),processed);
            }
            int x =indexes_end.get(indexes_end.size()-1);
            String processed = process_value(item.subSequence(x, item.length()).toString());
            int data_begin = indexes_begin.get(indexes_begin.size()-1);
            int data_end = indexes_end.get(indexes_begin.size()-1);
            data.put(item.subSequence(data_begin, data_end).toString(),processed);
            result.add(data);
        }
        finals = result;


        //open file
//        String classpath = System.getProperty("java.class.path");
//        classpath = classpath + "\\audit_json.json";

        StringBuilder customItemsBuild = new StringBuilder();
//        FileWriter fw = new FileWriter(classpath);
//        fw.write("[\n");

        customItemsBuild.append("[\n");

        Integer arr_count = 0;
        for(HashMap<String, String> a_map: result){
            arr_count++;

            customItemsBuild.append("\t{\n");
//            fw.write("\t{\n");
            Integer len = a_map.keySet().size();
            Integer counter = 0;
            for(String k: a_map.keySet()){
                if (descriptions.contains(a_map.get("description")) == false){
                    descriptions.add(a_map.get("description"));
                }
                counter++;
                customItemsBuild.append("\t\t\""+k+"\":");
//                fw.write("\t\t\""+k+"\":");
                String value = a_map.get(k)
                        .replace("\\", "\\\\")
                        .replace("\n","\\n")
                        .replace("\"", "\\\"");



                if (counter == len) {
//                    fw.write("\t\t\""+value+"\"\n");
                    customItemsBuild.append("\t\t\"" + value + "\"\n");
                }else {
//                    fw.write("\t\t\""+value+"\",\n");
                    customItemsBuild.append("\t\t\"" + value + "\",\n");
                }
            }

            if (arr_count == result.size())
//                fw.write("\t}\n");
                customItemsBuild.append("\t}\n");
            else
                customItemsBuild.append("\t},\n");
//                fw.write("\t},\n");
        }
        customItemsBuild.append("]");
//        fw.write("]");
//        fw.close();
        customItems.setText(String.valueOf(customItemsBuild));

        listView.getItems().addAll(descriptions);
        status.setText("Converted");
    }

    public static int indexOf (Pattern pattern, String s){
        Matcher matcher = pattern.matcher(s);
        return matcher.find() ? matcher.start() : -1;
    }
    public String process_value(String value){
        String new_value = value;
        // find :
        int idx = indexOf(Pattern.compile(":"), value);
        new_value = value.subSequence(idx+1, value.length()).toString();

        // delete spaces
        new_value = new_value.trim();
        status.setText("Success");
        return new_value;
    }


    public void onPerformAuditButtonClick(){
        try
        {
//            String[] commands = new String[]{};
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "start");
            builder.redirectErrorStream(true);
            Process p = builder.start();
            Runtime.getRuntime().exec("cmd start cmd.exe /K");
            for (HashMap<String, String> selmp : selectedAudits) {
                status.setText(selmp.get("cmd"));
                System.out.println(selmp.get("cmd"));
                Runtime.getRuntime().exec(selmp.get("cmd"));
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while (true) {
                    line = r.readLine();
                    if (line == null) {
                        break;
                    }
                    allResponses.add(line);
                }
                System.out.println(allResponses);


            }

        }
        catch (Exception e)
        {
            status.setText("Error");
            e.printStackTrace();
        }
//
//        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "start");
//        builder.append("dd");
//        System.out.println(builder);
//        builder.redirectErrorStream(true);
//        Process p = builder.start();
//        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
//        String line;
//        while (true) {
//            line = r.readLine();
//            if (line == null) { break; }
//            status.setText(line);
//        }
////        for (HashMap<String, String> selmp : selectedAudits){
//
//        }
    }
}
