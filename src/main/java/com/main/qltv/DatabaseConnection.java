package com.main.qltv;

import com.main.qltv.model.Sach;
import com.main.qltv.model.SinhVien;
import com.main.qltv.model.TaiKhoan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static boolean kiemTraSinhVienTonTai(String maSinhVien) {
        String sql = "SELECT COUNT(*) FROM SinhVien WHERE maSinhVien = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maSinhVien);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean kiemTraTaiKhoanDaTonTai(String maSinhVien) {
        String sql = "SELECT COUNT(*) FROM TaiKhoan WHERE maSinhVien = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maSinhVien);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean themTaiKhoan(TaiKhoan tk) {
        // Tự tạo maTaiKhoan mới
        String maTKMoi = "TK" + System.currentTimeMillis(); // Ví dụ đơn giản

        String sql = "INSERT INTO TaiKhoan(maTaiKhoan, tenDangNhap, matKhau, loaiNguoiDung, maSinhVien) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maTKMoi);
            pstmt.setString(2, tk.tenDangNhap);
            pstmt.setString(3, tk.matKhauTK);
            pstmt.setString(4, "sinhvien"); // Gán mặc định là "user"
            pstmt.setString(5, tk.MSSV);

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ObservableList<SinhVien> LayDanhSachSinhVien() {
        ObservableList<SinhVien> danhSach = FXCollections.observableArrayList();
        String sql = "SELECT * FROM SinhVien";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                SinhVien sinhVien = new SinhVien();
                sinhVien.MSSV = rs.getString("maSinhVien");
                sinhVien.tenSinhVien = rs.getString("tenSinhVien");
                sinhVien.gioiTinh = rs.getString("gioiTinh");
                sinhVien.ngaySinh = rs.getDate("ngaySinh");
                sinhVien.diaChi = rs.getString("diaChi");
                sinhVien.email = rs.getString("email");
                sinhVien.SDT = rs.getString("soDienThoai");

                danhSach.add(sinhVien);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public static boolean themSinhVien(SinhVien sv) {
        String sql = "INSERT INTO SinhVien(maSinhVien, tenSinhVien, gioiTinh, ngaySinh, diaChi, email, soDienThoai) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, sv.MSSV);
            pstmt.setString(2, sv.tenSinhVien);
            pstmt.setString(3, sv.gioiTinh);
            pstmt.setDate(4, sv.ngaySinh);
            pstmt.setString(5, sv.diaChi);
            pstmt.setString(6, sv.email);
            pstmt.setString(7, sv.SDT);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean capNhatSinhVien(SinhVien sv) {
        String sql = "UPDATE SinhVien SET tenSinhVien = ?, gioiTinh = ?, ngaySinh = ?, diaChi = ?, email = ?, soDienThoai = ? " +
                "WHERE maSinhVien = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, sv.tenSinhVien);
            pstmt.setString(2, sv.gioiTinh);
            pstmt.setDate(3, sv.ngaySinh);
            pstmt.setString(4, sv.diaChi);
            pstmt.setString(5, sv.email);
            pstmt.setString(6, sv.SDT);
            pstmt.setString(7, sv.MSSV);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean xoaSinhVien(String maSinhVien) {
        String sql = "DELETE FROM SinhVien WHERE maSinhVien = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maSinhVien);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean xoaTaiKhoan(String maSinhVien) {
        String sql = "DELETE FROM TaiKhoan WHERE maSinhVien = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maSinhVien);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ObservableList<Sach> LayDanhSachSach() {
        ObservableList<Sach> danhSach = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Sach";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Sach sach = new Sach();
                sach.maSach = rs.getString("maSach");
                sach.tenSach = rs.getString("tenSach");
                sach.maTacGia = rs.getString("maTacGia");
                sach.maTheLoai = rs.getString("maTheLoai");
                sach.maNXB = rs.getString("maNXB");
                sach.soLuong = rs.getInt("soLuong");
                sach.ngayXuatBan = rs.getDate("ngayXuatBan");
                sach.soTrang = rs.getInt("soTrang");
                sach.moTa = rs.getString("moTa");
                sach.anhBia = rs.getString("anhBia");
                danhSach.add(sach);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public static String layTenTacGia(String maTacGia) {
        String sql = "SELECT tenTacGia FROM TacGia WHERE maTacGia = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maTacGia);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("tenTacGia");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String layTenTheLoai(String maTheLoai) {
        String sql = "SELECT tenTheLoai FROM TheLoai WHERE maTheLoai = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maTheLoai);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("tenTheLoai");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String layTenNXB(String maNXB) {
        String sql = "SELECT tenNXB FROM NhaXuatBan WHERE maNXB = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maNXB);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("tenNXB");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> layDanhSachTenTacGia() {
        List<String> ds = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT TenTacGia FROM TacGia");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ds.add(rs.getString("TenTacGia"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public static List<String> layDanhSachTenTheLoai() {
        List<String> ds = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT TenTheLoai FROM TheLoai");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ds.add(rs.getString("TenTheLoai"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public static List<String> layDanhSachTenNXB() {
        List<String> ds = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT TenNXB FROM NhaXuatBan");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ds.add(rs.getString("TenNXB"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

}
