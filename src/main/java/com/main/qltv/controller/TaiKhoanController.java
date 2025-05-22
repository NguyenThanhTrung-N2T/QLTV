package com.main.qltv.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class TaiKhoanController {

    @FXML private Label lblMaTaiKhoan;
    @FXML private Label lblTenDangNhap;
    @FXML private Label lblLoaiNguoiDung;
    @FXML private Label lblMaSoCB;
    @FXML private Label lblMaSinhVien;
    @FXML private Button btnCapNhatThongTin;

    @FXML
    private void initialize() {


        btnCapNhatThongTin.setOnAction(event -> capNhatThongTin());
        // TODO: Gán dữ liệu tài khoản khi màn hình được mở, ví dụ:
        // lblTenDangNhap.setText("user123");
    }

    @FXML
    private void capNhatThongTin() {
        try {
            // Load FXML cửa sổ cập nhật
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/main/qltv/view/CapNhatTaiKhoan.fxml"));
            Parent root = loader.load();

            // Lấy controller của cửa sổ cập nhật
            CapNhatTaiKhoanController controller = loader.getController();

            // Gửi thông tin hiện tại sang cửa sổ cập nhật
            controller.setThongTin(
                    lblTenDangNhap.getText(),
                    lblMaSoCB.getText(),
                    lblMaSinhVien.getText()
            );

            // Tạo và mở cửa sổ mới
            Stage stage = new Stage();
            stage.setTitle("Cập nhật thông tin tài khoản");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait(); // Chờ cập nhật xong mới quay lại

            // Sau khi cửa sổ đóng, có thể reload thông tin nếu cần
            // Ví dụ: loadThongTinMoi(); // bạn có thể viết hàm này nếu cập nhật DB

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
