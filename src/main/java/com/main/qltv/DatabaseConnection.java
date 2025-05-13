package com.main.qltv;

import com.main.qltv.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

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

    public static String layMaTacGiaTheoTen(String tenTacGia) throws SQLException {
        String sql = "SELECT maTacGia FROM TacGia WHERE tenTacGia = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tenTacGia);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("maTacGia");
            return null;
        }
    }

    public static String layMaTheLoaiTheoTen(String tenTheLoai) throws SQLException {
        String sql = "SELECT maTheLoai FROM TheLoai WHERE tenTheLoai = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tenTheLoai);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("maTheLoai");
            return null;
        }
    }

    public static String layMaNXBTheoTen(String tenNXB) throws SQLException {
        String sql = "SELECT maNXB FROM NhaXuatBan WHERE tenNXB = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tenNXB);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("maNXB");
            return null;
        }
    }

    public static void themTacGia(TacGia tg) throws SQLException {
        String sql = "INSERT INTO TacGia (maTacGia, tenTacGia) VALUES (?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tg.getMaTacGia());
            stmt.setString(2, tg.getTenTacGia());
            stmt.executeUpdate();
        }
    }

    public static void themTheLoai(TheLoai tl) throws SQLException {
        String sql = "INSERT INTO TheLoai (maTheLoai, tenTheLoai) VALUES (?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tl.getMaTheLoai());
            stmt.setString(2, tl.getTenTheLoai());
            stmt.executeUpdate();
        }
    }

    public static void themNXB(NhaXuatBan nxb) throws SQLException {
        String sql = "INSERT INTO NhaXuatBan (maNXB, tenNXB) VALUES (?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nxb.getMaNXB());
            stmt.setString(2, nxb.getTenNXB());
            stmt.executeUpdate();
        }
    }

    public static boolean kiemTraMaSachTonTai(String maSach) {
        try (Connection conn = connect()) {
            String sql = "SELECT 1 FROM Sach WHERE maSach = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, maSach);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Nếu có bản ghi => tồn tại
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean themSach(Sach sach, TacGia tacGia, TheLoai theLoai, NhaXuatBan nxb) {
        try (Connection conn = connect()) {

            // === 1. Kiểm tra & xử lý tác giả ===
            String maTacGia = layMaTacGiaTheoTen(tacGia.getTenTacGia());
            if (maTacGia == null) {
                maTacGia = "TG" + System.currentTimeMillis();
                tacGia.setMaTacGia(maTacGia);
                themTacGia(tacGia);
            }
            sach.setMaTacGia(maTacGia);

            // === 2. Kiểm tra & xử lý thể loại ===
            String maTheLoai = layMaTheLoaiTheoTen(theLoai.getTenTheLoai());
            if (maTheLoai == null) {
                maTheLoai = "TL" + System.currentTimeMillis();
                theLoai.setMaTheLoai(maTheLoai);
                themTheLoai(theLoai);
            }
            sach.setMaTheLoai(maTheLoai);

            // === 3. Kiểm tra & xử lý NXB ===
            String maNXB = layMaNXBTheoTen(nxb.getTenNXB());
            if (maNXB == null) {
                maNXB = "NXB" + System.currentTimeMillis();
                nxb.setMaNXB(maNXB);
                themNXB(nxb);
            }
            sach.setMaNXB(maNXB);

            // === 4. Tạo mã sách nếu chưa có ===
            if (sach.getMaSach() == null || sach.getMaSach().isEmpty()) {
                sach.setMaSach("S" + System.currentTimeMillis());
            }

            // === 5. Thêm sách vào bảng Sach ===
            String sql = "INSERT INTO Sach (maSach, tenSach, maTacGia, maTheLoai, maNXB, soLuong, ngayXuatBan, soTrang, moTa, anhBia) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, sach.getMaSach());
                stmt.setString(2, sach.getTenSach());
                stmt.setString(3, sach.getMaTacGia());
                stmt.setString(4, sach.getMaTheLoai());
                stmt.setString(5, sach.getMaNXB());
                stmt.setInt(6, sach.getSoLuong());
                stmt.setDate(7, sach.getNgayXuatBan());
                stmt.setInt(8, sach.getSoTrang());
                stmt.setString(9, sach.getMoTa());
                stmt.setString(10, sach.getAnhBia());
                return stmt.executeUpdate() > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void xoaTacGiaNeuKhongDung(String maTacGia) {
        String sql = "SELECT COUNT(*) FROM Sach WHERE maTacGia = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maTacGia);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM TacGia WHERE maTacGia = ?")) {
                    deleteStmt.setString(1, maTacGia);
                    deleteStmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa tác giả không dùng: " + e.getMessage());
        }
    }

    public static void xoaTheLoaiNeuKhongDung(String maTheLoai) {
        String sql = "SELECT COUNT(*) FROM Sach WHERE maTheLoai = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maTheLoai);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM TheLoai WHERE maTheLoai = ?")) {
                    deleteStmt.setString(1, maTheLoai);
                    deleteStmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa thể loại không dùng: " + e.getMessage());
        }
    }

    public static void xoaNXBNguKhongDung(String maNXB) {
        String sql = "SELECT COUNT(*) FROM Sach WHERE maNXB = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maNXB);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM NhaXuatBan WHERE maNXB = ?")) {
                    deleteStmt.setString(1, maNXB);
                    deleteStmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa NXB không dùng: " + e.getMessage());
        }
    }

    public static String layMaTacGiaTheoMaSach(String maSach) {
        String sql = "SELECT maTacGia FROM Sach WHERE maSach = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maSach);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("maTacGia");
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy mã tác giả cũ: " + e.getMessage());
        }
        return null;
    }

    public static String layMaTheLoaiTheoMaSach(String maSach) {
        String sql = "SELECT maTheLoai FROM Sach WHERE maSach = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maSach);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("maTheLoai");
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy mã thể loại cũ: " + e.getMessage());
        }
        return null;
    }

    public static String layMaNXBTheoMaSach(String maSach) {
        String sql = "SELECT maNXB FROM Sach WHERE maSach = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maSach);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("maNXB");
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy mã NXB cũ: " + e.getMessage());
        }
        return null;
    }

    public static void themTacGia(Connection conn, TacGia tg) throws SQLException {
        String sql = "INSERT INTO TacGia(maTacGia, tenTacGia) VALUES(?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tg.getMaTacGia());
            stmt.setString(2, tg.getTenTacGia());
            stmt.executeUpdate();
        }
    }

    public static void themTheLoai(Connection conn, TheLoai tl) throws SQLException {
        String sql = "INSERT INTO TheLoai(maTheLoai, tenTheLoai) VALUES(?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tl.getMaTheLoai());
            stmt.setString(2, tl.getTenTheLoai());
            stmt.executeUpdate();
        }
    }

    public static void themNXB(Connection conn, NhaXuatBan nxb) throws SQLException {
        String sql = "INSERT INTO NhaXuatBan(maNXB, tenNXB) VALUES(?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nxb.getMaNXB());
            stmt.setString(2, nxb.getTenNXB());
            stmt.executeUpdate();
        }
    }

    public static boolean capNhatSach(Sach sach, TacGia tacGia, TheLoai theLoai, NhaXuatBan nxb) {
        Connection conn = null;
        try {
            conn = connect();
            conn.setAutoCommit(false); // Bắt đầu transaction
            // === 1. Xử lý Tác Giả ===
            String maTacGiaMoi = layMaTacGiaTheoTen(tacGia.getTenTacGia());
            if (maTacGiaMoi == null) {
                maTacGiaMoi = "TG" + System.currentTimeMillis();
                tacGia.setMaTacGia(maTacGiaMoi);
                themTacGia(conn, tacGia);
            }
            String maTacGiaCu = layMaTacGiaTheoMaSach(sach.getMaSach());
            sach.setMaTacGia(maTacGiaMoi);

            // === 2. Xử lý Thể Loại ===
            String maTheLoaiMoi = layMaTheLoaiTheoTen(theLoai.getTenTheLoai());
            if (maTheLoaiMoi == null) {
                maTheLoaiMoi = "TL" + System.currentTimeMillis();
                theLoai.setMaTheLoai(maTheLoaiMoi);
                themTheLoai(conn, theLoai);
            }
            String maTheLoaiCu = layMaTheLoaiTheoMaSach(sach.getMaSach());
            sach.setMaTheLoai(maTheLoaiMoi);

            // === 3. Xử lý NXB ===
            String maNXBMoi = layMaNXBTheoTen(nxb.getTenNXB());
            if (maNXBMoi == null) {
                maNXBMoi = "NXB" + System.currentTimeMillis();
                nxb.setMaNXB(maNXBMoi);
                themNXB(conn, nxb);
            }

            String maNXBCu = layMaNXBTheoMaSach(sach.getMaSach());
            sach.setMaNXB(maNXBMoi);

            String sql = "UPDATE Sach SET tenSach = ?, maTacGia = ?, maTheLoai = ?, maNXB = ?, soLuong = ?, ngayXuatBan = ?, soTrang = ?, moTa = ?, anhBia = ? WHERE maSach = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, sach.getTenSach());
                stmt.setString(2, sach.getMaTacGia());
                stmt.setString(3, sach.getMaTheLoai());
                stmt.setString(4, sach.getMaNXB());
                stmt.setInt(5, sach.getSoLuong());
                stmt.setDate(6, sach.getNgayXuatBan());
                stmt.setInt(7, sach.getSoTrang());
                stmt.setString(8, sach.getMoTa());
                stmt.setString(9, sach.getAnhBia());
                stmt.setString(10, sach.getMaSach());

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    conn.commit();

                    // Xóa dữ liệu cũ nếu không còn dùng
                    xoaTacGiaNeuKhongDung(maTacGiaCu);
                    xoaTheLoaiNeuKhongDung(maTheLoaiCu);
                    xoaNXBNguKhongDung(maNXBCu);

                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Lỗi khi rollback: " + rollbackEx.getMessage());
            }
            System.err.println("Lỗi khi cập nhật sách: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.err.println("Lỗi khi đóng connection: " + ex.getMessage());
            }
        }
    }







    public static boolean xoaSach(String maSach) {
        String checkSql = "SELECT COUNT(*) FROM MuonSach WHERE maSach = ?";
        String deleteSql = "DELETE FROM Sach WHERE maSach = ?";

        try (Connection conn = connect();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {

            // Kiểm tra xem sách có từng được mượn chưa
            checkStmt.setString(1, maSach);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // Hiển thị cảnh báo nếu sách đã được mượn
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Không thể xóa sách");
                alert.setHeaderText("Sách đã từng được mượn");
                alert.setContentText("Không thể xóa sách này vì đã có lượt mượn trong hệ thống.");
                alert.showAndWait();
                return false;
            }

            // Xóa sách nếu không bị ràng buộc
            deleteStmt.setString(1, maSach);
            int rowsAffected = deleteStmt.executeUpdate();
            if (rowsAffected > 0) {
                // Thông báo xóa thành công (nếu muốn)
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Xóa thành công");
                alert.setHeaderText(null);
                alert.setContentText("Sách đã được xóa khỏi hệ thống.");
                alert.showAndWait();
                return true;
            } else {
                // Không có sách nào bị xóa
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText("Xóa thất bại");
                alert.setContentText("Không tìm thấy sách để xóa.");
                alert.showAndWait();
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Thông báo lỗi nếu có exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi hệ thống");
            alert.setHeaderText("Lỗi khi xóa sách");
            alert.setContentText("Đã xảy ra lỗi: " + e.getMessage());
            alert.showAndWait();
            return false;
        }
    }



}
