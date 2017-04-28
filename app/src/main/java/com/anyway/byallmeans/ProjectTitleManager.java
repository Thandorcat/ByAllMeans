package com.anyway.byallmeans;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
// Pair programming. 'Expert–expert' variation 
public class ProjectTitleManager {
    private String[] titles;
    private Map<String, Integer> instances;

    public ProjectTitleManager() {// Levental
        titles = new String[]{"Call of Duty", "SERZH Inc.", "TRiTPO",
                "MATAN", "Gradle", "Android Studio", "Full-Life", "World of cats",
                "New PHP", "Doing nothing", "Hot Chickens"};
        instances = new HashMap<>();
        for (String title : titles) {
            instances.put(title, 0);
        }
    }

    public String generateRandom() {
        int id = new Random().nextInt(titles.length);
        return generate(titles[id]);
    }

    public String generateSequel(String title) {// Doropey
        String original = findOriginal(title);
        return generate(original);
    }

    private String generate(String title) {
        String result = title;
        int instance = instances.get(title);
        instances.put(title, ++instance);
        if (instance != 1) {
            result += " " + instance;
        }
        return result;
    }

    private String findOriginal(String title) {
        Pattern p = Pattern.compile("(.+) (\\d+)");
        Matcher m = p.matcher(title);
        if (m.find()) {
            return m.group(1);
        } else {
            return title;
        }
    }
}
