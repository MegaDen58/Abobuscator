package org.example;

import java.io.*;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static String newMainClass;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введить путь к файлу: ");
        String input = scanner.nextLine();
        newMainClass = randomWord(4, 7);
        String newFile = String.format("C:\\Users\\Admin\\IdeaProjects\\Obfuscator\\src\\main\\java\\org\\example\\%s.java", newMainClass);

        String text = fileToText(input);
        text = text.replaceAll("\\/\\/.+", "");
        text = text.replaceAll("/\\*(?s).*?\\*/", "");
        text = functions(text, 3, 10);
        text = variables(text, 7, 15);
        text = classes(text, 3, 5, input);
        text = trash(text);
        System.out.print(text);

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(newFile))){
            bw.write(text);
            bw.flush();
        }
        catch (Exception e){
            System.out.print(e.getMessage());
        }

    }

    public static String classes(String text, int byNum, int toNum, String path){

        String realClass;
        String regForClass = "(\\w+)\\.java";
        Pattern regPattern = Pattern.compile(regForClass);
        Matcher regMatcher = regPattern.matcher(path);

        while(regMatcher.find()){
            realClass = regMatcher.group().split("\\.")[0];
            text = text.replaceAll(realClass, newMainClass);
        }

        String regular = "(\\s*)class(\\s*)(\\w+)";
        Pattern regPattern1 = Pattern.compile(regular);
        Matcher regMatcher1 = regPattern1.matcher(text);

        while(regMatcher1.find() && !regMatcher1.group().contains(newMainClass)){
            text = text.replaceAll(regMatcher1.group(), randomWord(byNum, toNum));
        }
        return text;
    }

    public static String variables(String text, int byNum, int toNum){
        String regular = "(\\s*)(\\w+)(\\s*)=";
        Pattern regPattern = Pattern.compile(regular);
        Matcher regMatcher = regPattern.matcher(text);
        String oneVariab;

        while(regMatcher.find()){
            oneVariab = regMatcher.group().replaceAll("(\\s*)=", "");
            oneVariab = oneVariab.replaceAll("(\\s*)", "");
            text = text.replaceAll(oneVariab, randomWord(byNum, toNum));
        }

        return text;
    }

    public static String functions(String text, int byNum, int toNum){
        String regular = "class(\\s+)(\\w+)";
        Pattern regPattern = Pattern.compile(regular);
        Matcher regMatcher = regPattern.matcher(text);
        String found = "";
        String newParams;

        while(regMatcher.find()){
            String aboba = regMatcher.group();
            found += aboba.split(" ")[1];
        }

        /*
        Замена названия метода
         */

        String regular1 = "(public|private|protected)(\\s*)(static?)(\\s*)(\\w*)(\\s*)(\\w+)\\(.+\\)";
        String regName = "(\\s+)(\\w+)\\(";
        String regParams = "\\(.+\\)";
        Pattern regPattern1 = Pattern.compile(regular1);
        Pattern namePattern  = Pattern.compile(regName);
        Pattern paramsPattern = Pattern.compile(regParams);

        Matcher regMatcher1 = regPattern1.matcher(text);

        while(regMatcher1.find()){
            String finded = regMatcher1.group();

            Matcher nameMatcher = namePattern.matcher(finded);
            Matcher paramsMatcher = paramsPattern.matcher(finded);
            if(!finded.contains("main") || found.contains(finded)){
                while(nameMatcher.find() && paramsMatcher.find()){
                    String stringName = nameMatcher.group();
                    stringName = stringName.replaceAll("\\(", "");
                    text = text.replaceAll(stringName, " " + randomWord(10, 15));


                    String stringParams = paramsMatcher.group();
                    stringParams = stringParams.replaceAll("\\(", "");
                    stringParams = stringParams.replaceAll("\\)", "");
                    String[] arra = stringParams.split(", ");
                    for(String kk : arra){
                        kk = kk.split(" ")[1];
                        newParams = randomWord(byNum, toNum);
                        text = text.replaceAll(kk, newParams);
                    }
                }
            }
        }
        return text;
    }

    public static String randomWord(int fromThat, int toThat){
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        String output = "";
        Random randomNumber = new Random();

        int lengthWord = randomNumber.nextInt(fromThat, toThat);
        char[] array = alphabet.toCharArray();

        for(int counter = 0; counter < lengthWord; counter++){
            output += array[randomNumber.nextInt(0, 25)];
        }

        return output;
    }

    public static String fileToText(String path){
        String text = "";

        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) {
                text += line + "\n";
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return text;
    }

    public static String trash(String text){
        text = text.replaceAll("(\\s*)\\{(\\s*)", "{");
        text = text.replaceAll("(\\s*)\\}(\\s*)", "}");
        text = text.replaceAll("(\\s*)=(\\s*)", "=");
        text = text.replaceAll("(\\s*)!(\\s*)", "!");
        text = text.replaceAll("(\\s*)\\+(\\s*)", "+");
        text = text.replaceAll("(\\s*)-(\\s*)", "-");
        text = text.replaceAll("(\\s*);(\\s*)", ";");

        return text;
    }
}