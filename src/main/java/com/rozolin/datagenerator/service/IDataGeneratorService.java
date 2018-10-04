package com.rozolin.datagenerator.service;

import java.sql.SQLException;

public interface IDataGeneratorService {

    void generatorData(String url, String user, String password, String driverClass) throws SQLException, ClassNotFoundException;

}
