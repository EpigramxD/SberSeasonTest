package org.test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {
    private static int currentNodeNumber = 0;
    private static int currentLevel = 0;

    private enum LexemeType {
        VALUE_LIST_END_LIST_BEGINNING,
        NAME
    }

    public static Node parse(Path path) throws ParsingException{
        currentNodeNumber = 0;
        String text = readText(path);
        Node root = new Node(-1, "root");
        return parse(root, text, LexemeType.VALUE_LIST_END_LIST_BEGINNING);
    }

    private static Node parse(Node parent, String string, LexemeType prevLexemeType) throws ParsingException {
        String text = string.trim();
        if(parent == null) {
            throw new ParsingException("Неверный формат данных");
        }
        if(text.length() == 0) {
            if(!parent.getName().equals("root")) {
                throw new ParsingException("Неверный формат данных");
            }
            Node result = parent.getChildren().get(0);
            result.setParent(null);
            return result;
        }
        switch (prevLexemeType) {
            case VALUE_LIST_END_LIST_BEGINNING: {
                if(Character.isLetter(text.toCharArray()[0]) || text.toCharArray()[0]=='_') {
                    Node node = createNode(text);
                    node.setParent(parent);
                    int firstIndexOfEquals = text.indexOf("=");
                    text = text.substring(firstIndexOfEquals+1).trim();
                    return parse(node, text, LexemeType.NAME);
                }
                else if(string.startsWith("}")) {
                    text = text.substring(1, text.length()).trim();
                    return parse(parent.getParent(), text, LexemeType.VALUE_LIST_END_LIST_BEGINNING);
                }
                else {
                    throw new ParsingException("Неверный формат данных");
                }
            }
            case NAME: {
                if(string.startsWith("{")) {
                    text = text.substring(1, text.length()).trim();
                    return parse(parent, text, LexemeType.VALUE_LIST_END_LIST_BEGINNING);
                }
                else if(string.startsWith("\"")) {
                    text = text.substring(1, text.length());
                    parent.setValue(getValue(text));
                    int lastIndexOfQuotes = text.indexOf("\"");
                    text = text.substring(lastIndexOfQuotes+1, text.length()).trim();
                    return parse(parent.getParent(), text, LexemeType.VALUE_LIST_END_LIST_BEGINNING);
                }
                else {
                    throw new ParsingException("Неверный формат данных");
                }
            }
        }
        return null;
    }

    private static Node createNode(String text) throws ParsingException {
        currentNodeNumber++;
        //вырезаем имя
        int firstIndexOfEquals = text.indexOf("=");
        String name = text.substring(0, firstIndexOfEquals).trim();
        //проверяем имя
        if (!validateName(name)) {
            throw new ParsingException("Неверный формат данных");
        }
        //создаем ноду
        return new Node(currentNodeNumber, name);
    }

    public static String getValue(String text)  throws ParsingException{
        int lastIndexOfQuotes = text.indexOf("\"");
        String value = text.substring(0, lastIndexOfQuotes);
        if(!validateValue(value)) {
            throw new ParsingException("Неверный формат данных");
        }
        return value;
    }

    private static boolean validateName(String name) {
        return name.matches("[a-zA-Z_]+\\w*");
    }

    private static boolean validateValue(String value) {
        if(value.matches(".*"))
            return true;
        return false;
    }

    private static String readText(Path path) {
        try {
            String textFomFile = new String(Files.readAllBytes(path));
            return textFomFile;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return "";
        }
    }
}
