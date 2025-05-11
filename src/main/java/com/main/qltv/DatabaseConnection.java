package com.main.qltv;

import com.main.qltv.model.TaiKhoan;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QUANLYTHUVIEN;encrypt=false";
    private static final String USER = "sa"; // Thay bằng user SQL Server
    private static final String PASSWORD = "thanhtrung1912935"; // Thay bằng mật khẩu

    // Kết nối với database
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean KiemTraTK(String tenDangNhap, String matKhau ){
        String sql = "SELECT COUNT(*) FROM TaiKhoan WHERE tenDangNhap = ? AND matKhau = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tenDangNhap);
            pstmt.setString(2, matKhau);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu có ít nhất 1 kết quả, tài khoản hợp lệ
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu có lỗi hoặc không tìm thấy tài khoản
    }

    public static TaiKhoan LayThongTinTK(String tenDangNhap, String matKhau){
        TaiKhoan nguoidung = new TaiKhoan();
        String sql = "SELECT * FROM TaiKhoan WHERE tenDangNhap = ? AND matKhau = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tenDangNhap);
            pstmt.setString(2, matKhau);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Lấy dữ liệu từ database và gán vào đối tượng TaiKhoan
                nguoidung.maTK = rs.getString("maTaiKhoan");
                nguoidung.tenDangNhap = rs.getString("tenDangNhap");
                nguoidung.matKhauTK = rs.getString("matKhau");
                nguoidung.loaiTK = rs.getString("loaiNguoiDung");
                nguoidung.MSSV = rs.getString("maSinhVien");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nguoidung; // Trả về thông tin tài khoản (null nếu không tồn tại)
    }

}
