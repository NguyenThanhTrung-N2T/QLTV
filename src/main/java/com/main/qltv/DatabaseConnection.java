package com.main.qltv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QUANLYTHUVIEN;encrypt=false";
    private static final String USER = "sa"; // Thay bằng user SQL Server
    private static final String PASSWORD = "thanhtrung1912935"; // Thay bằng mật khẩu

    // Kết nối với database
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}
