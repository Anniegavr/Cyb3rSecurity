package com.labs.oop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Converter {
    ArrayList<String> items = new ArrayList<String>(); //all texts
    String[] params = new String[]{"description", "info", "reference", "see_also", "collection", "fieldsSelector",
            "query", "expect", "solution", "severity", "system", "type", "file", "regex", "cmd"};
    public static int indexOf(Pattern pattern, String s) {
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
        return new_value;
    }

    public void convert(String file_path) throws IOException {
        ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
        try {
            File audit_file = new File(file_path);
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


        System.out.println("===================");
        for (String item : items) {
            ArrayList<Integer> indexes_begin = new ArrayList<Integer>();
            ArrayList<Integer> indexes_end = new ArrayList<Integer>();
            HashMap<String, String> data = new HashMap<String, String>();
            System.out.println(item);
            for (String one_param: params){
                int idx = indexOf(Pattern.compile(one_param+"[\\s]+:"), item);
                System.out.println("=="+idx+"|"+one_param);
                if (idx>=0) {
                    indexes_begin.add(idx);
                    indexes_end.add(idx+one_param.length());
                }
            }
            Collections.sort(indexes_begin);
            Collections.sort(indexes_end);
            System.out.println(indexes_begin);
            System.out.println(indexes_end);

            System.out.println("finished indexes");
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
            System.out.println("--------------------------");


            result.add(data);
        }
        System.out.println("===================");


        //open file
        String classpath = System.getProperty("java.class.path");
        classpath = classpath + "\\audit_json.json";
        System.out.println(classpath);
        FileWriter fw = new FileWriter(classpath);
        fw.write("[\n");

        Integer arr_count = 0;
        for(HashMap<String, String> a_map: result){
            arr_count++;

            fw.write("\t{\n");
            Integer len = a_map.keySet().size();
            Integer counter = 0;
            for(String k: a_map.keySet()){
                counter++;
                fw.write("\t\t\""+k+"\":");
                String value = a_map.get(k)
                        .replace("\\", "\\\\")
                        .replace("\n","\\n")
                        .replace("\"", "\\\"");

                if (counter == len)
                    fw.write("\t\t\""+value+"\"\n");
                else
                    fw.write("\t\t\""+value+"\",\n");
            }

            if (arr_count == result.size())
                fw.write("\t}\n");
            else
                fw.write("\t},\n");
        }
        fw.write("]");
        fw.close();
    }
}
