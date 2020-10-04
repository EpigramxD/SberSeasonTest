package org.test;

import java.nio.file.Paths;

public class App {
    static {
        Database.initConnection("D:\\DataBaseConfig.properties");
    }

    public static void main(String[] args) {
        try {
            Node node = Parser.parse(Paths.get("D:\\test.txt"));
            NodeWriter writer = new DatabaseNodeWriter();
            node.write(writer);
        }
        catch (ParsingException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
