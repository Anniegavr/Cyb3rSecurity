package com.example.newall;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Convertor {
    ArrayList<String> items; //all texts
    ArrayList<Map<String, String>> maps; //all texts
    String[] params;
    public Convertor(){
        this.items = new ArrayList<>();
        this.maps = new ArrayList<Map<String, String>>();
        this.params = new String[]{"description", "info", "reference", "see_also", "collection", "fieldsSelector",
                "query", "expect", "solution", "severity"};
    }

    public static int indexOf(Pattern pattern, String s) {
        Matcher matcher = pattern.matcher(s);
        return matcher.find() ? matcher.start() : -1;
    }
    public void convert(File filen) throws IOException {
        try {
            File audit_file = new File(filen.getPath());
//            File audit_file = new File(filen);
            Scanner sc = new Scanner(audit_file);     //file to be scanned
            Map<String, String> map = new HashMap<String, String>();


            String audit = "";
            Boolean start = false;
            while (sc.hasNextLine()) {    //returns true if and only if scanner has another token
                String this_line = sc.nextLine();

                String REGEX = "</custom_item>";
                int idx = indexOf(Pattern.compile(REGEX), this_line);
                if (idx >= 0){
                    start = false;
                    items.add(audit);
                    audit = "";
                }

                if (start){
                    audit += this_line + "\n";
                }

                REGEX = "<custom_item>";
                idx = indexOf(Pattern.compile(REGEX), this_line);
                if (idx >= 0){
                    start = true;
                }

            }


        } catch (FileNotFoundException e) {
            System.out.println("No");
            e.printStackTrace();
        }


        for (String item : items){
            Map<String, String> map = new HashMap<String, String>();
            maps.add(map);
            String first_word_param = "";
            for (String row : item.split("\n")) {
                if (row.length() == 0)
                    continue;

                Boolean contains = false;
                String first_word = "";

                int semicolon_index = row.indexOf(":");
                if (semicolon_index != -1) {
                    first_word = row.substring(0, row.indexOf(":"));
                    first_word = first_word.replaceAll("\\s+", "");



                    for (String param : params) {
                        if (param.compareTo(first_word) == 0) {
                            contains = true;
                        }

                    }
                }

                if (contains){
                    map.put(first_word, row.substring(semicolon_index+1));
                    first_word_param = first_word;
                }
                else{
                    map.put(first_word_param, map.get(first_word_param) + row);
                }

            }

        }
//        cloneItems = maps;
        System.out.println(maps);
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter("N:\\MINE\\CS\\newAll\\src\\main\\java\\com\\example\\newall\\converted.txt"));
        fileWriter.write("{\n");
        for (Map<String, String> m : maps) {
            fileWriter.write("[{\n");
            for (String k : m.keySet()) {
                for (String v : m.values()) {
                    if (v.contains("\"")) {
                        fileWriter.write(String.format("%s: %s\n", k, v));

                        System.out.println(String.format("%s: %s\n", k, v));
                    }else {
                        fileWriter.write(String.format("%s: \"%s\"\n", k, v));
                        System.out.println(String.format("%s: \"%s\"\n", k, v));
                    }
                }
            }
            fileWriter.write("\n}]");
            System.out.println("\n}]");
        }
        fileWriter.write("\n}");
        System.out.println("\n}]");
        fileWriter.close();

    }

}
