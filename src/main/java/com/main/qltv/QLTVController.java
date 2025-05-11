package com.main.qltv;

import com.main.qltv.model.TaiKhoan;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class QLTVController {
    private static Stage previousStage; // Lưu cửa sổ trước đó

    public static void setPreviousStage(Stage stage) {
        previousStage = stage;
    }
    @FXML Button DangNhap;
    @FXML Button DangKy;
    @FXML Button Exit;

    @FXML
    public void Exit(){
        Platform.exit();
    }

    @FXML
    public void DangNhap() { // Đây là phương thức gây ra lỗi theo stack trace\
        try {
            // Lấy cửa sổ hiện tại và lưu lại
            Stage currentStage = (Stage) ((Node) DangNhap).getScene().getWindow();
            setPreviousStage(currentStage); // Lưu cửa sổ trước đó
            currentStage.hide(); // Ẩn cửa sổ hiện tại nhưng không đóng

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dang_Nhap-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Đăng Nhập");
            stage.setScene(new Scene(root));
            // Nếu người dùng nhấn "X", cửa sổ trước đó sẽ hiển thị lại
            stage.setOnCloseRequest(event -> {
                if (previousStage != null) {
                    previousStage.show();
                }
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(getClass().getResource("/view/Dang_Nhap-view.fxml"));
    }
}