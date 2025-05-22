package com.main.qltv.controller;

import com.main.qltv.QLTVApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class CapNhatTaiKhoanController {

    @FXML TextField txtTenDangNhap;
    @FXML TextField txtMaSoCB;
    @FXML TextField txtMaSinhVien;
    @FXML PasswordField txtMatKhauCu;
    @FXML PasswordField txtMatKhauMoi;
    @FXML PasswordField txtXacNhanMK;
    @FXML Button btnHuy;

    public void setThongTin(String tenDangNhap, String maCB, String maSV) {
        txtTenDangNhap.setText(tenDangNhap);
        txtMaSoCB.setText(maCB);
        txtMaSinhVien.setText(maSV);
    }

    @FXML private void initialize() {
        txtMaSoCB.setEditable(false);
        txtMaSinhVien.setEditable(false);
        if(QLTVApplication.nguoidangnhap.getLoaiTK().equals("Quản lý")) {
            txtMaSinhVien.setVisible(false);
            txtMaSinhVien.setManaged(false);
        } else {
            txtMaSoCB.setVisible(false);
            txtMaSoCB.setManaged(false);
        }

        btnHuy.setOnAction(e->Huy());

    }

    @FXML
    private void LuuThongTin() {
        String tenDN = txtTenDangNhap.getText().trim();
        String maCB = txtMaSoCB.getText().trim();
        String maSV = txtMaSinhVien.getText().trim();
        String mkCu = txtMatKhauCu.getText();
        String mkMoi = txtMatKhauMoi.getText();
        String mkXacNhan = txtXacNhanMK.getText();

        // kiem tra thông tin
        if (tenDN.isEmpty() || mkCu.isEmpty() || mkMoi.isEmpty() || mkXacNhan.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        // kiem tra tên đăng nhập
        if (!mkMoi.equals(mkXacNhan)) {
            showAlert(Alert.AlertType.ERROR, "Mật khẩu xác nhận không khớp.");
            return;
        }

        // lay ma tai khoan
        String maTK = QLTVApplication.nguoidangnhap.getMaTK();

        // Check old password
        if (!com.main.qltv.DatabaseConnection.kiemTraMatKhau(maTK, mkCu)) {
            showAlert(Alert.AlertType.ERROR, "Mật khẩu cũ không đúng.");
            return;
        }

        // Update account
        boolean updated = com.main.qltv.DatabaseConnection.capNhatTaiKhoan(maTK, tenDN, mkMoi);
        if (updated) {
            QLTVApplication.nguoidangnhap.setTenDangNhap(txtTenDangNhap.getText());
            QLTVApplication.nguoidangnhap.setMatKhauTK(txtMatKhauMoi.getText());
            showAlert(Alert.AlertType.INFORMATION, "Cập nhật thành công! Vui lòng đăng nhập lại.");
            Stage stage = (Stage) txtTenDangNhap.getScene().getWindow();
            stage.close();

            Platform.runLater(() -> {
                // Copy windows to avoid ConcurrentModificationException
                Window[] windows = Window.getWindows().toArray(new Window[0]);
                for (Window window : windows) {
                    if (window instanceof Stage) {
                        ((Stage) window).close();
                    }
                }
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/main/qltv/Dang_Nhap-view.fxml"));
                    Parent root = loader.load();
                    Stage newStage = new Stage();
                    newStage.setTitle("Đăng Nhập");
                    newStage.setScene(new Scene(root));
                    newStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } else {
            showAlert(Alert.AlertType.ERROR, "Cập nhật thất bại. Vui lòng thử lại.");
        }
    }

    @FXML
    private void Huy() {
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
