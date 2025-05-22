package com.main.qltv;

import com.main.qltv.model.*;
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
                nguoidung.setMaTK(rs.getString("maTaiKhoan"));
                nguoidung.setTenDangNhap(rs.getString("tenDangNhap"));
                nguoidung.setMatKhauTK(rs.getString("matKhau"));
                nguoidung.setLoaiTK(rs.getString("loaiNguoiDung"));
                nguoidung.setMSSV(rs.getString("maSinhVien"));
                nguoidung.setMaSoCB(rs.getString("maSoCB"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nguoidung; // Trả về thông tin tài khoản (null nếu không tồn tại)
    }

    public static boolean kiemTraSinhVienTonTai(String maSinhVien) {
        String sql = "SELECT COUNT(*) FROM SinhVien WHERE maSinhVien = ? COLLATE SQL_Latin1_General_CP1_CS_AS";

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
        String sql = "SELECT COUNT(*) FROM TaiKhoan WHERE maSinhVien = ? COLLATE SQL_Latin1_General_CP1_CS_AS";
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

    public static boolean kiemTraTaiKhoanDaTonTaiCuaCB(String maSoCB) {
        String sql = "SELECT COUNT(*) FROM TaiKhoan WHERE maSoCB = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maSoCB);
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

        String sql = "INSERT INTO TaiKhoan(maTaiKhoan, tenDangNhap, matKhau, loaiNguoiDung, maSoCB, maSinhVien) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maTKMoi);
            pstmt.setString(2, tk.getTenDangNhap());
            pstmt.setString(3, tk.getMatKhauTK());
            pstmt.setString(4, tk.getLoaiTK());
            pstmt.setString(5,tk.getMaSoCB());
            pstmt.setString(6, tk.getMSSV());

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
                sinhVien.setMSSV(rs.getString("maSinhVien"));
                sinhVien.setTenSinhVien(rs.getString("tenSinhVien"));
                sinhVien.setGioiTinh(rs.getString("gioiTinh"));
                sinhVien.setNgaySinh(rs.getDate("ngaySinh"));
                sinhVien.setDiaChi(rs.getString("diaChi"));
                sinhVien.setEmail(rs.getString("email"));
                sinhVien.setSDT(rs.getString("soDienThoai"));

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

            pstmt.setString(1, sv.getMSSV());
            pstmt.setString(2, sv.getTenSinhVien());
            pstmt.setString(3, sv.getGioiTinh());
            pstmt.setDate(4, sv.getNgaySinh());
            pstmt.setString(5, sv.getDiaChi());
            pstmt.setString(6, sv.getEmail());
            pstmt.setString(7, sv.getSDT());

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

            pstmt.setString(1, sv.getTenSinhVien());
            pstmt.setString(2, sv.getGioiTinh());
            pstmt.setDate(3, sv.getNgaySinh());
            pstmt.setString(4, sv.getDiaChi());
            pstmt.setString(5, sv.getEmail());
            pstmt.setString(6, sv.getSDT());
            pstmt.setString(7, sv.getMSSV());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean xoaSinhVien(String maSinhVien) {
        String selectPhieuMuon = "SELECT maPhieuMuon FROM PhieuMuon WHERE maSinhVien = ?";
        String deleteMuonSach = "DELETE FROM MuonSach WHERE maPhieuMuon = ?";
        String deletePhieuMuon = "DELETE FROM PhieuMuon WHERE maPhieuMuon = ?";
        String deleteTaiKhoan = "DELETE FROM TaiKhoan WHERE maSinhVien = ?";
        String deleteSinhVien = "DELETE FROM SinhVien WHERE maSinhVien = ?";

        if(sinhVienConPhieuMuonChuaTra(maSinhVien)){
            return false;
        }
        try (Connection conn = connect()) {
            conn.setAutoCommit(false);

            // 1. Get all loan slip IDs of the student
            List<String> phieuMuonIds = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement(selectPhieuMuon)) {
                stmt.setString(1, maSinhVien);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    phieuMuonIds.add(rs.getString("maPhieuMuon"));
                }
            }

            // 2. Delete all MuonSach records for each loan slip
            for (String maPhieuMuon : phieuMuonIds) {
                try (PreparedStatement stmt = conn.prepareStatement(deleteMuonSach)) {
                    stmt.setString(1, maPhieuMuon);
                    stmt.executeUpdate();
                }
            }

            // 3. Delete all PhieuMuon records
            for (String maPhieuMuon : phieuMuonIds) {
                try (PreparedStatement stmt = conn.prepareStatement(deletePhieuMuon)) {
                    stmt.setString(1, maPhieuMuon);
                    stmt.executeUpdate();
                }
            }

            // 4. Delete account if exists
            try (PreparedStatement stmt = conn.prepareStatement(deleteTaiKhoan)) {
                stmt.setString(1, maSinhVien);
                stmt.executeUpdate();
            }

            // 5. Delete the student
            try (PreparedStatement stmt = conn.prepareStatement(deleteSinhVien)) {
                stmt.setString(1, maSinhVien);
                int affected = stmt.executeUpdate();
                if (affected == 0) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean xoaTaiKhoan(String maSinhVien) {
        if (kiemTraSinhVienTonTai(maSinhVien)) {
            if (sinhVienConPhieuMuonChuaTra(maSinhVien)) {
                return false;
            }
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
        String sql = "DELETE FROM TaiKhoan WHERE maSoCB = ?";
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
                sach.setMaSach(rs.getString("maSach"));
                sach.setTenSach(rs.getString("tenSach"));
                sach.setMaTacGia(rs.getString("maTacGia"));
                sach.setMaTheLoai(rs.getString("maTheLoai"));
                sach.setMaNXB(rs.getString("maNXB"));
                sach.setSoLuong(rs.getInt("soLuong"));
                sach.setNgayXuatBan(rs.getDate("ngayXuatBan"));
                sach.setSoTrang(rs.getInt("soTrang"));
                sach.setMoTa(rs.getString("moTa"));
                sach.setAnhBia(rs.getString("anhBia"));
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

    public static void xoaTacGiaNeuKhongDung(Connection conn, String maTacGia) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Sach WHERE maTacGia = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maTacGia);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM TacGia WHERE maTacGia = ?")) {
                    deleteStmt.setString(1, maTacGia);
                    deleteStmt.executeUpdate();
                }
            }
        }
    }

    public static void xoaTheLoaiNeuKhongDung(Connection conn, String maTheLoai) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Sach WHERE maTheLoai = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maTheLoai);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM TheLoai WHERE maTheLoai = ?")) {
                    deleteStmt.setString(1, maTheLoai);
                    deleteStmt.executeUpdate();
                }
            }
        }
    }

    public static void xoaNXBNeuKhongDung(Connection conn, String maNXB) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Sach WHERE maNXB = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maNXB);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM NhaXuatBan WHERE maNXB = ?")) {
                    deleteStmt.setString(1, maNXB);
                    deleteStmt.executeUpdate();
                }
            }
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

            // === 4. Cập nhật sách ===
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
                    // Commit cập nhật trước khi xóa dữ liệu cũ
                    conn.commit();

                    // Xóa dữ liệu cũ nếu không còn được dùng
                    xoaTacGiaNeuKhongDung(conn, maTacGiaCu);
                    xoaTheLoaiNeuKhongDung(conn, maTheLoaiCu);
                    xoaNXBNeuKhongDung(conn, maNXBCu);

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
        // Prevent deletion if there are unreturned loans
        if (sachConPhieuMuonChuaTra(maSach)) {
            return false;
        }

        String deleteMuonSachSql = "DELETE FROM MuonSach WHERE maSach = ? AND maPhieuMuon IN (SELECT maPhieuMuon FROM PhieuMuon WHERE tinhTrang = N'Đã trả')";
        String deletePhieuMuonSql = "DELETE FROM PhieuMuon WHERE maPhieuMuon NOT IN (SELECT maPhieuMuon FROM MuonSach)";
        String deleteSachSql = "DELETE FROM Sach WHERE maSach = ?";
        String getTacGiaSql = "SELECT maTacGia FROM Sach WHERE maSach = ?";
        String getTheLoaiSql = "SELECT maTheLoai FROM Sach WHERE maSach = ?";
        String getNXBSql = "SELECT maNXB FROM Sach WHERE maSach = ?";

        try (Connection conn = connect()) {
            conn.setAutoCommit(false);

            // 1. Delete returned MuonSach records for this book
            try (PreparedStatement stmt = conn.prepareStatement(deleteMuonSachSql)) {
                stmt.setString(1, maSach);
                stmt.executeUpdate();
            }

            // 2. Delete PhieuMuon that no longer have any MuonSach
            try (PreparedStatement stmt = conn.prepareStatement(deletePhieuMuonSql)) {
                stmt.executeUpdate();
            }

            // 3. Get related info for cleanup
            String maTacGia = getStringField(conn, getTacGiaSql, maSach);
            String maTheLoai = getStringField(conn, getTheLoaiSql, maSach);
            String maNXB = getStringField(conn, getNXBSql, maSach);

            // 4. Delete the book
            try (PreparedStatement stmt = conn.prepareStatement(deleteSachSql)) {
                stmt.setString(1, maSach);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    conn.rollback();
                    return false;
                }
            }

            // 5. Clean up unused TacGia, TheLoai, NXB
            xoaTacGiaNeuKhongDung(conn, maTacGia);
            xoaTheLoaiNeuKhongDung(conn, maTheLoai);
            xoaNXBNeuKhongDung(conn, maNXB);

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String getStringField(Connection conn, String sql, String param) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, param);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getString(1) : null;
        }
    }

    public static ObservableList<TaiKhoan> LayDanhSachTaiKhoan() {
        ObservableList<TaiKhoan> danhSach = FXCollections.observableArrayList();
        String sql = "SELECT * FROM TaiKhoan";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                TaiKhoan taiKhoan = new TaiKhoan();
                taiKhoan.setMaTK(rs.getString("maTaiKhoan"));
                taiKhoan.setTenDangNhap(rs.getString("tenDangNhap"));
                taiKhoan.setMatKhauTK(rs.getString("matKhau"));
                taiKhoan.setLoaiTK(rs.getString("loaiNguoiDung"));
                taiKhoan.setMaSoCB(rs.getString("maSoCB"));
                taiKhoan.setMSSV(rs.getString("maSinhVien"));

                danhSach.add(taiKhoan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public static boolean capNhatTaiKhoan(TaiKhoan tk) {
        String sql = "UPDATE TaiKhoan SET tenDangNhap = ?, matKhau = ?, loaiNguoiDung = ?, maSoCB = ?, maSinhVien = ?" +
                " WHERE maTaiKhoan = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tk.getTenDangNhap());
            pstmt.setString(2, tk.getMatKhauTK());
            pstmt.setString(3, tk.getLoaiTK());
            pstmt.setString(4, tk.getMaSoCB());
            pstmt.setString(5, tk.getMSSV());
            pstmt.setString(6, tk.getMaTK());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String layTenSinhVien(String maSinhVien) {
        String sql = "SELECT tenSinhVien FROM SinhVien WHERE maSinhVien = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maSinhVien);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("tenSinhVien");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String layTenSach(String maSach) {
        String sql = "SELECT tenSach FROM Sach WHERE maSach = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maSach);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("tenSach");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ObservableList<PhieuMuon> layDanhSachPhieuMuon() {
        ObservableList<PhieuMuon> danhSach = FXCollections.observableArrayList();
        String sql = "SELECT pm.maPhieuMuon, pm.maSinhVien, pm.ngayMuon, pm.ngayTra, pm.tinhTrang, " +
                "ms.maMuonSach, ms.maSach, ms.soLuong, s.tenSach " +
                "FROM PhieuMuon pm " +
                "JOIN MuonSach ms ON pm.maPhieuMuon = ms.maPhieuMuon " +
                "JOIN Sach s ON ms.maSach = s.maSach";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PhieuMuon pm = new PhieuMuon(
                        rs.getString("maPhieuMuon"),
                        rs.getString("maSinhVien"),
                        rs.getDate("ngayMuon"),
                        rs.getDate("ngayTra"),
                        rs.getString("tinhTrang"),
                        rs.getString("maMuonSach"),
                        rs.getString("maSach"),
                        rs.getInt("soLuong")
                );
                danhSach.add(pm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public static List<String> layDanhSachTenSach() {
        List<String> ds = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT tenSach FROM Sach");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ds.add(rs.getString("tenSach"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public static String layMaSachTheoTen(String tenSach) {
        String sql = "SELECT maSach FROM Sach WHERE tenSach = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tenSach);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("maSach");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int laySoLuongSach(String maSach) {
        String sql = "SELECT soLuong FROM Sach WHERE maSach = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maSach);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("soLuong");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean themPhieuMuon(PhieuMuon pm) {
        String insertPhieuMuon = "INSERT INTO PhieuMuon (maPhieuMuon, maSinhVien, ngayMuon, ngayTra, tinhTrang) VALUES (?, ?, ?, ?, ?)";
        String insertMuonSach = "INSERT INTO MuonSach (maMuonSach, maPhieuMuon, maSach, soLuong) VALUES (?, ?, ?, ?)";
        String updateSoLuongSach = "UPDATE Sach SET soLuong = soLuong - ? WHERE maSach = ? AND soLuong >= ?";
        try (Connection conn = connect()) {
            conn.setAutoCommit(false);

            // 1. Thêm phiếu mượn
            String maPhieuMuon = pm.getMaPhieuMuon();
            if (maPhieuMuon == null || maPhieuMuon.isEmpty()) {
                maPhieuMuon = "PM" + System.currentTimeMillis(); // Hoặc sinh mã theo ý bạn
            }
            try (PreparedStatement stmt = conn.prepareStatement(insertPhieuMuon)) {
                stmt.setString(1, maPhieuMuon);
                stmt.setString(2, pm.getMaSinhVien());
                stmt.setDate(3, pm.getNgayMuon());
                stmt.setDate(4, pm.getNgayTra());
                stmt.setString(5, pm.getTinhTrang());
                stmt.executeUpdate();
            }

            // 2. Thêm mượn sách
            String maMuonSach = "MS" + System.currentTimeMillis(); // Hoặc sinh mã theo ý bạn
            try (PreparedStatement stmt = conn.prepareStatement(insertMuonSach)) {
                stmt.setString(1, maMuonSach);
                stmt.setString(2, maPhieuMuon);
                stmt.setString(3, pm.getMaSach());
                stmt.setInt(4, pm.getSoLuong());
                stmt.executeUpdate();
            }

            // 3. Trừ số lượng sách
            try (PreparedStatement stmt = conn.prepareStatement(updateSoLuongSach)) {
                stmt.setInt(1, pm.getSoLuong());
                stmt.setString(2, pm.getMaSach());
                stmt.setInt(3, pm.getSoLuong());
                int updated = stmt.executeUpdate();
                if (updated == 0) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean capNhatTinhTrangPhieuMuon(String maPhieuMuon, String tinhTrang) {
        String updatePhieu = "UPDATE PhieuMuon SET tinhTrang = ? WHERE maPhieuMuon = ?";
        String selectMuonSach = "SELECT maSach, soLuong FROM MuonSach WHERE maPhieuMuon = ?";
        String updateSach = "UPDATE Sach SET soLuong = soLuong + ? WHERE maSach = ?";
        try (Connection conn = connect()) {
            conn.setAutoCommit(false);

            // Update status
            try (PreparedStatement pstmt = conn.prepareStatement(updatePhieu)) {
                pstmt.setString(1, tinhTrang);
                pstmt.setString(2, maPhieuMuon);
                if (pstmt.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }
            }

            // If returning, increase book quantity
            if ("Đã trả".equals(tinhTrang)) {
                try (PreparedStatement pstmt = conn.prepareStatement(selectMuonSach)) {
                    pstmt.setString(1, maPhieuMuon);
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        String maSach = rs.getString("maSach");
                        int soLuong = rs.getInt("soLuong");
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSach)) {
                            updateStmt.setInt(1, soLuong);
                            updateStmt.setString(2, maSach);
                            updateStmt.executeUpdate();
                        }
                    }
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean giaHanPhieuMuon(String maPhieuMuon, Date ngayTraMoi) {
        String sql = "UPDATE PhieuMuon SET ngayTra = ? WHERE maPhieuMuon = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, ngayTraMoi);
            pstmt.setString(2, maPhieuMuon);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean sinhVienConPhieuMuonChuaTra(String maSinhVien) {
        String sql = "SELECT COUNT(*) FROM PhieuMuon WHERE maSinhVien = ? AND tinhTrang != N'Đã trả'";
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

    private static boolean sachConPhieuMuonChuaTra(String maSach) {
        String sql = "SELECT COUNT(*) FROM MuonSach ms JOIN PhieuMuon pm ON ms.maPhieuMuon = pm.maPhieuMuon WHERE ms.maSach = ? AND pm.tinhTrang != N'Đã trả'";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maSach);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ObservableList<PhieuMuon> layDanhSachPhieuMuonTheoNguoiDung(String maSinhVien, String maSoCB) {
        ObservableList<PhieuMuon> danhSach = FXCollections.observableArrayList();
        StringBuilder sql = new StringBuilder(
                "SELECT pm.maPhieuMuon, pm.maSinhVien, pm.ngayMuon, pm.ngayTra, pm.tinhTrang, " +
                        "ms.maMuonSach, ms.maSach, ms.soLuong, s.tenSach " +
                        "FROM PhieuMuon pm " +
                        "JOIN MuonSach ms ON pm.maPhieuMuon = ms.maPhieuMuon " +
                        "JOIN Sach s ON ms.maSach = s.maSach "
        );

        if (maSinhVien != null && !maSinhVien.isEmpty()) {
            sql.append("WHERE pm.maSinhVien = ?");
        } else if (maSoCB != null && !maSoCB.isEmpty()) {
            sql.append("JOIN TaiKhoan tk ON pm.maSinhVien = tk.maSinhVien WHERE tk.maSoCB = ?");
        } else {
            return danhSach;
        }

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            if (maSinhVien != null && !maSinhVien.isEmpty()) {
                stmt.setString(1, maSinhVien);
            } else {
                stmt.setString(1, maSoCB);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PhieuMuon pm = new PhieuMuon(
                        rs.getString("maPhieuMuon"),
                        rs.getString("maSinhVien"),
                        rs.getDate("ngayMuon"),
                        rs.getDate("ngayTra"),
                        rs.getString("tinhTrang"),
                        rs.getString("maMuonSach"),
                        rs.getString("maSach"),
                        rs.getInt("soLuong")
                );
                danhSach.add(pm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public static boolean kiemTraMatKhau(String maTaiKhoan, String matKhauNhap) {
        String sql = "SELECT 1 FROM TaiKhoan WHERE maTaiKhoan = ? AND matKhau = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maTaiKhoan);
            stmt.setString(2, matKhauNhap);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean capNhatTaiKhoan(String maTaiKhoan, String tenDangNhapMoi, String matKhauMoi) {
        String sql = "UPDATE TaiKhoan SET tenDangNhap = ?, matKhau = ? WHERE maTaiKhoan = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tenDangNhapMoi);
            stmt.setString(2, matKhauMoi);
            stmt.setString(3, maTaiKhoan);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
