package com.main.qltv.controller;

import com.main.qltv.DatabaseConnection;
import com.main.qltv.QLTVApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;

public class Dang_Nhap_Controller {
    @FXML TextField txtTenDangNhap;
    @FXML TextField txtMatKhau;
    @FXML Button btnDangNhap;

    @FXML public void DanhNhap(){
        String tenDangNhap = txtTenDangNhap.getText().trim();
        String matKhau = txtMatKhau.getText().trim();

        if (DatabaseConnection.KiemTraTK(tenDangNhap, matKhau)) {
            // Hiển thị thông báo thành công
            QLTVApplication.nguoidangnhap = DatabaseConnection.LayThongTinTK(tenDangNhap,matKhau);
            try {
                Stage currentStage = (Stage) ((Node) txtTenDangNhap).getScene().getWindow();
                currentStage.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Man_Hinh_Chinh-view.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Hệ thống quản lý thư viện");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Hiển thị thông báo lỗi
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi đăng nhập");
            alert.setHeaderText(null);
            alert.setContentText("Tài khoản hoặc mật khẩu không đúng.");
            alert.showAndWait();
        }
    }



}
