package com.main.qltv;
public class DBConnect {
    public static void main(String[] args) {
        try {
            DatabaseConnection.connect();
            System.out.println("Database connected successfully!");
        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }
}