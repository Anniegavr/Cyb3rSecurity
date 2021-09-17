package com.labs.oop;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*valid_properties = ['description', 'info']


 */
public class Main {

    public static int indexOf(Pattern pattern, String s) {
        Matcher matcher = pattern.matcher(s);
        return matcher.find() ? matcher.start() : -1;
    }

    public static void main(String[] args) throws IOException {
        Gui g = new Gui();
    }
}
