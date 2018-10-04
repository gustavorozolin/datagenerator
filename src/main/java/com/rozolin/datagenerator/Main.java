package com.rozolin.datagenerator;

import com.rozolin.datagenerator.service.IDataGeneratorService;
import com.rozolin.datagenerator.service.impl.DataGeneratorService;

import java.sql.SQLException;

public class Main {

    private final static String DRIVER_CLASS =  "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private final static int TABLE_ROWS =  1000;

    public static void main(String... strings) throws SQLException,
            ClassNotFoundException {

        String db = null, user = "", password = "";
        int rows = TABLE_ROWS;

        for (String string : strings) {
            String[] keyValue = string.split("=",2);
            if(keyValue[0].equalsIgnoreCase("db")){
                db = keyValue[1];
            }else if(keyValue[0].equalsIgnoreCase("user")){
                user = keyValue[1];
            }else if(keyValue[0].equalsIgnoreCase("password")){
                password = keyValue[1];
            }else if(keyValue[0].equalsIgnoreCase("rows")){
                rows =  Integer.valueOf(keyValue[1]);
            }
        }

        if(db == null){
            throw new IllegalArgumentException("\"db\" is required!!!");
        }

        System.out.println("Database: " + db);
        System.out.println("User: " + user);
        System.out.println("Rows: " + rows);

        IDataGeneratorService dataGeneratorService = new DataGeneratorService();
        dataGeneratorService.generatorData(db, user, password, DRIVER_CLASS);
    }

}
