package com.rozolin.datagenerator.service.impl;

import com.rozolin.datagenerator.model.Column;
import com.rozolin.datagenerator.model.DataType;
import com.rozolin.datagenerator.model.Table;
import com.rozolin.datagenerator.service.IDataGeneratorService;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataGeneratorService implements IDataGeneratorService {

    public static final String APOSTROPHE = "'";
    public static final String EMPTY_STRING = "";
    public static final String DATA_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void generatorData(String url, String user, String password, String driverClass) throws SQLException, ClassNotFoundException {
        Class.forName(driverClass);

        Connection conn = null;
        System.out.println("Connecting...");
        if(StringUtils.isNotBlank(user)){
            conn = DriverManager.getConnection(url, user, password);
        }else{
            conn = DriverManager.getConnection(url);
        }
        System.out.println("Connected");

        List<Table> tables = getTables(conn);

        tables.parallelStream().forEach(table -> this.createInserts(table,2500) );
    }

    private void createInserts(Table table, int rows) {
        System.out.println("Creating "+rows+" insert rows for table "+table.getName());

        FileWriter fw = null;
        BufferedWriter bw = null;
        try {

            fw = new FileWriter(table.getName()+".sql");
            bw = new BufferedWriter(fw);

            for(int i = 0; i < rows; i++){
                StringBuilder insert = new StringBuilder("INSERT INTO "+table.getName());

                StringBuilder cols = new StringBuilder();
                StringBuilder values = new StringBuilder();
                for (Column column : table.getColumns()) {
                    cols.append(" "+column.getName()+", ");
                    values.append(" "+getValue(column,i)+", ");
                }
                insert.append(" ("+ StringUtils.removeEnd(cols.toString(), ", ")+")");
                insert.append(" VALUES ");
                insert.append(" (" + StringUtils.removeEnd(values.toString(),", ") + ")");

                bw.write(insert.toString());
                bw.newLine();
            }
        } catch (IOException e) {

            e.printStackTrace();

        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }


    }

    private String getValue(Column column, int position) {

        if (column.getDataType() == DataType.VARCHAR || column.getDataType() == DataType.CHAR) {
            String value = position + " " + column.getName();
            final Integer length = column.getLength();
            if(value.length() > length && length > 0){
                value = value.substring(0, length);
            }
            return APOSTROPHE +value + APOSTROPHE;
        }
        if (column.getDataType() == DataType.INT) {
            return EMPTY_STRING + (position);
        }
        if (column.getDataType() == DataType.DATETIME) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    DATA_FORMAT_YYYY_MM_DD_HH_MM_SS);
            java.util.Date date = new java.util.Date((new Date().getTime() + (1000 * position)));
            return APOSTROPHE + dateFormat.format(date) + APOSTROPHE;
        }

        return null;
    }

    private List<Table> getTables(Connection conn) {
        System.out.println("Finding tables...");
        List<Table> tables = new ArrayList<Table>();
        try {

            Statement statement = conn.createStatement();
            String queryString = "SELECT * FROM Sys.Tables";
            ResultSet rs = statement.executeQuery(queryString);
            while (rs.next()) {
                String tableName = rs.getString(1);
                Table table = new Table();
                table.setName(tableName);
                table.setColumns(getColumnsFromTable(conn,table));
                tables.add(table);
                System.out.println(tableName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tables;
    }

    private List<Column> getColumnsFromTable(Connection conn, Table table) {

        List<Column> columns = new ArrayList<Column>();
        try {

            Statement statement = conn.createStatement();
            String queryString = "SELECT c.Name as columnName "
                    + " , t.Name as dataType "
                    + " , c.length as lengthSize "
                    + " FROM syscolumns c  "
                    + " INNER JOIN sysobjects o ON o.id = c.id "
                    + " LEFT JOIN  systypes t on t.xtype = c.xtype "
                    + " WHERE o.type = 'U' AND o.Name = '"
                    + table.getName()
                    + "' ";

            ResultSet rs = statement.executeQuery(queryString);
            while (rs.next()) {

                String name = rs.getString(1);
                String type = rs.getString(2);
                int lenght = rs.getInt(3);
                Column column = new Column();
                column.setName(name);
                column.setDataType(DataType.getByType(type));
                column.setLength(lenght);
                columns.add(column);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return columns;
    }
}
