package controller;

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

    @FXML
    private TableView<PhieuMuon> tablePhieuMuon;
    @FXML
    private TableColumn<PhieuMuon, String> colMaPhieu;
    @FXML
    private TableColumn<PhieuMuon, String> colMaDG;
    @FXML
    private TableColumn<PhieuMuon, String> colTenDG;
    @FXML
    private TableColumn<PhieuMuon, LocalDate> colNgayMuon;
    @FXML
    private TableColumn<PhieuMuon, LocalDate> colNgayTra;
    @FXML
    private TableColumn<PhieuMuon, String> colMaSach;
    @FXML
    private TableColumn<PhieuMuon, String> colTenSach;
    @FXML
    private TableColumn<PhieuMuon, String> colTrangThai;

    @FXML
    private TextField tfMaPhieu, tfMaDG, tfTenDG, tfMaSach, tfTenSach;
    @FXML
    private DatePicker dpNgayMuon, dpNgayTra;
    @FXML
    private ComboBox<String> cbTrangThai;

    @FXML
    private Button btnThem, btnSua, btnXoa, btnLammoi;

    private ObservableList<PhieuMuon> listPhieuMuon;
}
