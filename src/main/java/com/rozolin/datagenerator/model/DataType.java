package com.rozolin.datagenerator.model;

public enum DataType {
    VARCHAR("varchar"),
    CHAR("char"),
    INT("int"),
    DATETIME("datetime");

    private String type;

    DataType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static DataType getByType(String type){
        for(DataType dataType : values()){
            if(type.equalsIgnoreCase(dataType.getType())){
                return dataType;
            }
        }
        return null;
    }
}
