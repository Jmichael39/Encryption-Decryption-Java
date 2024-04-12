package encryptdecrypt;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            switch (args[i]) {
                case "-mode" -> mode = args[i + 1];
                case "-data" -> data = args[i + 1];
                case "-key" -> key = Integer.parseInt(args[i + 1]);
                case "-in" -> inputFileName = args[i + 1];
                case "-out" -> outputFileName = args[i + 1];
                case "-alg" -> alg = args[i + 1];
            }
        }

        boolean readFromFile = !inputFileName.isEmpty();
        boolean writeToFile = !outputFileName.isEmpty();

        if (!data.isEmpty() && !inputFileName.isEmpty()) {
            readFromFile = false;
        }

        if (readFromFile){
            File file = new File(inputFileName);
            try (Scanner fileScanner = new Scanner(file)) {
                data = fileScanner.nextLine();
            } catch (FileNotFoundException e) {
                System.out.println("Error " + e.getMessage());
            }
        }

        switch (mode) {
            case "enc" -> {
                if ("unicode".equals(alg)) {
                    unicode(data, key, writeToFile);
                } else {
                    shift(data, key, writeToFile);
                }
            }
            case "dec" -> {
                if ("unicode".equals(alg)) {
                    unicodeDecrypt(data, key, writeToFile);
                } else {
                    shiftDecrypt(data, key, writeToFile);
                }
            }
        }
    }

    public static void unicode(String message, int key, boolean writeToFile) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char ch = message.charAt(i);
            int ascii = (int) ch + key;
            sb.append((char) ascii);
        }
        showResults(writeToFile, sb);
    }

    public static void shift(String message, int key, boolean writeToFile) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char ch = message.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                sb.append((char) ((ch - 'A' + key) % 26 + 'A'));
            } else if (ch >= 'a' && ch <= 'z') {
                sb.append((char) ((ch - 'a' + key) % 26 + 'a'));
            } else sb.append(ch);
        }
        showResults(writeToFile, sb);
    }

    public static void unicodeDecrypt(String message, int key, boolean writeToFile) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char ch = message.charAt(i);
            int ascii = (int) ch - key;
            sb.append((char) ascii);
        }
        showResults(writeToFile, sb);
    }

    public static void shiftDecrypt(String message, int key, boolean writeToFile) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char ch = message.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                sb.append((char) (((ch - 'A' - key + 26) % 26) + 'A'));
            } else if (ch >= 'a' && ch <= 'z') {
                sb.append((char) (((ch - 'a' - key + 26) % 26) + 'a'));
            } else sb.append(ch);
        }
        showResults(writeToFile, sb);
    }

    private static void showResults(boolean writeToFile, StringBuilder sb) {
        if (writeToFile) {
            try {
                fw = new FileWriter(outputFileName);
                fw.write(String.valueOf(sb));
            } catch (IOException e) {
                System.out.println("Error " + e.getMessage());
            } finally {
                try {
                    if (fw != null) {
                        fw.close();
                    }
                } catch (IOException e) {
                    System.out.println("Error " + e.getMessage());
                }
            }
        } else {
            System.out.print(sb);
        }
    }

    static FileWriter fw = null;
    static String outputFileName = "";
    static String mode = "enc", data = "", inputFileName = "", alg = "shift";
    static int key = 0;
}
