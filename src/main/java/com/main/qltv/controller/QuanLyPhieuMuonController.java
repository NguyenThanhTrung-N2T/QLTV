package com.main.qltv.controller;

import com.main.qltv.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.PhieuMuon;

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
    @FXML TableColumn<PhieuMuon, String> colNgayMuon;
    @FXML TableColumn<PhieuMuon, String> colNgayTra;
    @FXML TableColumn<PhieuMuon, String> colTinhTrang;

    // Danh sách phiếu mượn
     ObservableList<PhieuMuon> danhSachPhieuMuon = FXCollections.observableArrayList();
}
