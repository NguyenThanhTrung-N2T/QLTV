package com.main.qltv.controller;


import com.main.qltv.QLTVApplication;
import com.main.qltv.QLTVController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Man_Hinh_Chinh_Controller {

    @FXML Label lBthongbao;
    @FXML Button btnQLSinhVien;
    @FXML Button btnQLSach;
    @FXML Button btnQLPhieuMuon;
    @FXML Button btnQLTaiKhoan;
    @FXML Button btnTaiKhoan;
    @FXML Button btnDangXuat;
    @FXML StackPane mainContent;

    @FXML
    public void initialize() {

        if(QLTVApplication.nguoidangnhap.getLoaiTK().equals("Sinh viên"))
        {
            btnQLSinhVien.setVisible(false);
            btnQLSinhVien.setManaged(false);
            btnQLTaiKhoan.setVisible(false);
            btnQLTaiKhoan.setManaged(false);
        }
        else if(QLTVApplication.nguoidangnhap.getLoaiTK().equals("Cộng tác viên"))
        {
            btnQLSinhVien.setVisible(false);
            btnQLSinhVien.setManaged(false);
        }

        lBthongbao.setText("Xin chào, " + QLTVApplication.nguoidangnhap.getTenDangNhap());
        // Gán sự kiện cho từng nút
        btnQLSinhVien.setOnAction(event -> loadContent("/com/main/qltv/QuanLySinhVien.fxml"));
        btnQLSach.setOnAction(event -> loadContent("/com/main/qltv/QuanLySach.fxml"));
        btnQLPhieuMuon.setOnAction(event -> loadContent("/com/main/qltv/QuanLyPhieuMuon.fxml"));
        btnQLTaiKhoan.setOnAction(actionEvent -> loadContent("/com/main/qltv/QuanLyTaiKhoan.fxml"));
        btnTaiKhoan.setOnAction(event -> loadContent("/com/main/qltv/TaiKhoan.fxml"));
        btnDangXuat.setOnAction(event -> {
            Stage currentStage = (Stage) ((Node) btnDangXuat).getScene().getWindow();
            currentStage.close();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(QLTVApplication.class.getResource("start-view.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("Library Management Software");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadContent(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent content = loader.load();
            mainContent.getChildren().setAll(content); // Clear rồi load nội dung mới
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
