package com.main.qltv.controller;

import com.main.qltv.DatabaseConnection;
import com.main.qltv.model.SinhVien;
import com.main.qltv.model.TaiKhoan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class QuanLyTaiKhoanController {
    @FXML TextField txtMaTaiKhoan;
    @FXML TextField txtTenDangNhap;
    @FXML TextField txtMatKhau;
    @FXML TextField txtMaSinhVien;
    @FXML ComboBox cbLoaiNguoiDung;
    @FXML Button btnThem;
    @FXML Button btnXoa;
    @FXML Button btnSua;
    @FXML Button btnLamMoi;
    @FXML TableView<TaiKhoan> tableTaiKhoan;
    @FXML TableColumn<TaiKhoan,String> colMaTaiKhoan;
    @FXML TableColumn<TaiKhoan,String> colTenDangNhap;
    @FXML TableColumn<TaiKhoan,String> colMatKhau;
    @FXML TableColumn<TaiKhoan,String> colLoaiNguoiDung;
    @FXML TableColumn<TaiKhoan,String> colMaSinhVien;

    private ObservableList<TaiKhoan> taiKhoanList;

    public void loadTaiKhoanTuDB(){
        taiKhoanList.clear();
        taiKhoanList.addAll(DatabaseConnection.LayDanhSachTaiKhoan());
        tableTaiKhoan.refresh(); // đảm bảo TableView vẽ lại
    }

    @FXML
    public void initialize(){
        // danh sách tài khoản
        taiKhoanList = FXCollections.observableArrayList(); // Khởi tạo
        tableTaiKhoan.setItems(taiKhoanList);
        colMaTaiKhoan.setCellValueFactory(new PropertyValueFactory<>("maTK"));
        colTenDangNhap.setCellValueFactory(new PropertyValueFactory<>("tenDangNhap"));
        colMatKhau.setCellValueFactory(new PropertyValueFactory<>("matKhauTK"));
        colLoaiNguoiDung.setCellValueFactory(new PropertyValueFactory<>("loaiTK"));
        colMaSinhVien.setCellValueFactory(new PropertyValueFactory<>("MSSV"));

        // Tải dữ liệu
        loadTaiKhoanTuDB();

    }




}
