package org.test;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseNodeWriter implements NodeWriter {
    @Override
    public void write(Node node) {
        try{
            //получем подключение
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();
            //читаем поля
            int nodeId = node.getNumber();
            int parentId;
            if(node.getParent() == null){
                parentId = -1;
            }
            else {
                parentId = node.getParent().getNumber();
            }
            String nodeName = node.getName();
            String nodeValue = node.getValue();
            //создаем запрос
            String query = "";
            if(parentId != -1){
                query = String.format("insert into tree_table (node_id, parent_id, node_name, node_value) values (\'%d\', \'%d\', \'%s\', \'%s\')",
                        nodeId, parentId, nodeName, nodeValue);
            }
            else {
                query = String.format("insert into tree_table (node_id, node_name, node_value) values (\'%d\', \'%s\', \'%s\')",
                        nodeId, nodeName, nodeValue);
            }
            //выполняем запрос
            statement.executeUpdate(query);
            statement.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
