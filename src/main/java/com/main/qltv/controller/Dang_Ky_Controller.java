package com.main.qltv.controller;

import com.main.qltv.DatabaseConnection;
import com.main.qltv.QLTVController;
import com.main.qltv.model.TaiKhoan;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class Dang_Ky_Controller {

    @FXML TextField txtMaSinhVien;
    @FXML TextField txtTenDangNhap;
    @FXML TextField txtMatKhau;
    @FXML TextField txtXacNhanMatKhau;
    @FXML Button btnDangKy;
    @FXML Label lblThongBao;

    @FXML
    public void DangKy(){
        String tenDangNhap = txtTenDangNhap.getText().trim();
        String matKhau = txtMatKhau.getText().trim();
        String maSinhVien = txtMaSinhVien.getText().trim();
        String loaiNguoiDung = "Sinh viên";

        if (tenDangNhap.isEmpty() || matKhau.isEmpty() || maSinhVien.isEmpty()) {
            lblThongBao.setText("Vui lòng điền đầy đủ thông tin.");
            return;
        }

        // Kiểm tra MSSV có tồn tại trong bảng SinhVien không
        if (!DatabaseConnection.kiemTraSinhVienTonTai(maSinhVien)) {
            lblThongBao.setText("Mã sinh viên không tồn tại trong hệ thống.");
            return;
        }

        // Kiểm tra MSSV đã có tài khoản chưa
        if (DatabaseConnection.kiemTraTaiKhoanDaTonTai(maSinhVien)) {
            lblThongBao.setText("Mã sinh viên này đã có tài khoản.");
            return;
        }

        // Kiểm tra tên đăng nhập đã tồn tại chưa
        if (DatabaseConnection.KiemTraTK(tenDangNhap, matKhau)) {
            lblThongBao.setText("Tên đăng nhập đã tồn tại.");
            return;
        }

        // Nếu hợp lệ thì tạo tài khoản mới
        TaiKhoan tk = new TaiKhoan();
        tk.setTenDangNhap(tenDangNhap);
        tk.setMatKhauTK(matKhau);
        tk.setMSSV(maSinhVien);
        tk.setMaSoCB(null);
        tk.setLoaiTK(loaiNguoiDung);

        boolean ketQua = DatabaseConnection.themTaiKhoan(tk);
        if (ketQua) {
            lblThongBao.setText("Đăng ký tài khoản thành công!");
            // để trống các thông tin
            txtTenDangNhap.clear();
            txtMatKhau.clear();
            txtMaSinhVien.clear();
            txtXacNhanMatKhau.clear();
        } else {
            lblThongBao.setText("Đăng ký thất bại, vui lòng thử lại.");
        }
    }


}
