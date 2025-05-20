package com.main.qltv.controller;

import com.main.qltv.DatabaseConnection;
import com.main.qltv.model.PhieuMuon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.PortUnreachableException;
import java.sql.*;
import java.time.LocalDate;

public class QuanLyPhieuMuonController {
    // TextField và DatePicker
    @FXML TextField txtNguoiMuon;
    @FXML TextField txtMSSV;
    @FXML TextField txtTenSach;
    @FXML DatePicker dateMuon;
    @FXML DatePicker dateTra;
    @FXML  TextField txtTimKiem;
    @FXML Spinner spnSoLuong;

    // Các nút chức năng
    @FXML Button btnMuon;
    @FXML Button btnTra;
    @FXML Button btnCapNhat;
    @FXML Button btnLamMoi;

    // TableView và các cột
    @FXML TableView<PhieuMuon> muonTraTable;
    @FXML TableColumn<PhieuMuon, String> colMaPhieu;
    @FXML TableColumn<PhieuMuon, String> colMSSV;
    @FXML TableColumn<PhieuMuon, String> colTenNguoiMuon;
    @FXML TableColumn<PhieuMuon, String> colTenSach;
    @FXML TableColumn<PhieuMuon, Date> colNgayMuon;
    @FXML TableColumn<PhieuMuon, Date> colNgayTra;
    @FXML TableColumn<PhieuMuon, Integer> colSoLuong;
    @FXML TableColumn<PhieuMuon, String> colTinhTrang;

    // Danh sách phiếu mượn
    private ObservableList<PhieuMuon> danhSachPhieuMuon;

    public void loadPhieuMUonTuDB(){
        danhSachPhieuMuon.clear();
        danhSachPhieuMuon.addAll(DatabaseConnection.layDanhSachPhieuMuon());
        muonTraTable.refresh();
    }

    // Hàm khởi tạo
    @FXML
    public void initialize() {
        // load dữ liệu lên table view
        danhSachPhieuMuon = FXCollections.observableArrayList();
        muonTraTable.setItems(danhSachPhieuMuon);
        // Tải dữ liệu
        loadPhieuMUonTuDB();
        colMaPhieu.setCellValueFactory(new PropertyValueFactory<>("maPhieuMuon"));
        colMSSV.setCellValueFactory(new PropertyValueFactory<>("maSinhVien"));

        // hien thi ten sinh vien
        colTenNguoiMuon.setCellValueFactory(new PropertyValueFactory<>("maSinhVien"));
        colTenNguoiMuon.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String maSinhVien, boolean empty) {
                super.updateItem(maSinhVien, empty);
                if (empty || maSinhVien == null) {
                    setText(null);
                } else {
                    setText(DatabaseConnection.layTenSinhVien(maSinhVien));
                }
            }
        });

        // hien thi ten sach
        colTenSach.setCellValueFactory(new PropertyValueFactory<>("maSach"));
        colTenSach.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String maSach, boolean empty) {
                super.updateItem(maSach, empty);
                if (empty || maSach == null) {
                    setText(null);
                } else {
                    setText(DatabaseConnection.layTenSach(maSach));
                }
            }
        });

        colNgayMuon.setCellValueFactory(new PropertyValueFactory<>("ngayMuon"));
        // Định dạng ngày
        colNgayMuon.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(java.sql.Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(new java.text.SimpleDateFormat("dd/MM/yyyy").format(item));
                }
            }
        });

        colNgayTra.setCellValueFactory(new PropertyValueFactory<>("ngayTra"));
        // Định dạng ngày
        colNgayTra.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(java.sql.Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(new java.text.SimpleDateFormat("dd/MM/yyyy").format(item));
                }
            }
        });

        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        colTinhTrang.setCellValueFactory(new PropertyValueFactory<>("tinhTrang"));

        // Sự kiện khi chọn dòng
        muonTraTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtMSSV.setText(newSelection.getMaSinhVien());
                txtTenSach.setText(DatabaseConnection.layTenSach(newSelection.getMaSach()));
                txtNguoiMuon.setText(DatabaseConnection.layTenSinhVien(newSelection.getMaSinhVien()));
                dateMuon.setValue(newSelection.getNgayMuon().toLocalDate());
                dateTra.setValue(newSelection.getNgayTra().toLocalDate());
                spnSoLuong.getValueFactory().setValue(newSelection.getSoLuong());
            }
        });





        btnLamMoi.setOnAction(e-> lamMoi());
    }

    @FXML
    private void lamMoi() {
        txtNguoiMuon.clear();
        txtMSSV.clear();
        txtTenSach.clear();
        spnSoLuong.getValueFactory().setValue(1);
        dateMuon.setValue(null);
        dateTra.setValue(null);
        muonTraTable.getSelectionModel().clearSelection();
    }


}
