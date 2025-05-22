package com.main.qltv.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CapNhatTaiKhoanController {

    @FXML private TextField txtTenDangNhap;
    @FXML private TextField txtMaSoCB;
    @FXML private TextField txtMaSinhVien;
    @FXML private PasswordField txtMatKhauCu;
    @FXML private PasswordField txtMatKhauMoi;
    @FXML private PasswordField txtXacNhanMK;

    public void setThongTin(String tenDangNhap, String maCB, String maSV) {
        txtTenDangNhap.setText(tenDangNhap);
        txtMaSoCB.setText(maCB);
        txtMaSinhVien.setText(maSV);
    }

    @FXML
    private void LuuThongTin() {
        String tenDN = txtTenDangNhap.getText();
        String maCB = txtMaSoCB.getText();
        String maSV = txtMaSinhVien.getText();

        String mkCu = txtMatKhauCu.getText();
        String mkMoi = txtMatKhauMoi.getText();
        String mkXacNhan = txtXacNhanMK.getText();

        // TODO: kiểm tra mật khẩu cũ đúng chưa (truy vấn DB)
        if (!mkMoi.equals(mkXacNhan)) {
            showAlert(Alert.AlertType.ERROR, "Mật khẩu xác nhận không khớp.");
            return;
        }

        // TODO: cập nhật DB ở đây
        showAlert(Alert.AlertType.INFORMATION, "Cập nhật thành công!");

        // Đóng cửa sổ
        Stage stage = (Stage) txtTenDangNhap.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
