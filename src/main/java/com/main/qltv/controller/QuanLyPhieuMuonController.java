package controller;

import database.Database;
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

    public void initialize() {
        listPhieuMuon = FXCollections.observableArrayList();
        cbTrangThai.setItems(FXCollections.observableArrayList("Chưa trả", "Đã trả"));

        setCellValue();
        loadDataFromDatabase();

        tablePhieuMuon.setItems(listPhieuMuon);
        tablePhieuMuon.setOnMouseClicked(event -> fillInputFromTable());
    }

    private void setCellValue() {
        colMaPhieu.setCellValueFactory(new PropertyValueFactory<>("maPhieu"));
        colMaDG.setCellValueFactory(new PropertyValueFactory<>("maDocGia"));
        colTenDG.setCellValueFactory(new PropertyValueFactory<>("tenDocGia"));
        colNgayMuon.setCellValueFactory(new PropertyValueFactory<>("ngayMuon"));
        colNgayTra.setCellValueFactory(new PropertyValueFactory<>("ngayTra"));
        colMaSach.setCellValueFactory(new PropertyValueFactory<>("maSach"));
        colTenSach.setCellValueFactory(new PropertyValueFactory<>("tenSach"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
    }

    private void loadDataFromDatabase() {
        listPhieuMuon.clear();
        String query = "SELECT * FROM PhieuMuon";

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                listPhieuMuon.add(new PhieuMuon(
                        rs.getString("maPhieu"),
                        rs.getString("maDocGia"),
                        rs.getString("tenDocGia"),
                        rs.getDate("ngayMuon").toLocalDate(),
                        rs.getDate("ngayTra").toLocalDate(),
                        rs.getString("maSach"),
                        rs.getString("tenSach"),
                        rs.getString("trangThai")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillInputFromTable() {
        PhieuMuon selected = tablePhieuMuon.getSelectionModel().getSelectedItem();
        if (selected != null) {
            tfMaPhieu.setText(selected.getMaPhieu());
            tfMaDG.setText(selected.getMaDocGia());
            tfTenDG.setText(selected.getTenDocGia());
            tfMaSach.setText(selected.getMaSach());
            tfTenSach.setText(selected.getTenSach());
            dpNgayMuon.setValue(selected.getNgayMuon());
            dpNgayTra.setValue(selected.getNgayTra());
            cbTrangThai.setValue(selected.getTrangThai());
        }
    }

    @FXML
    private void handleThem() {
        String insertQuery = "INSERT INTO PhieuMuon VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            pstmt.setString(1, tfMaPhieu.getText());
            pstmt.setString(2, tfMaDG.getText());
            pstmt.setString(3, tfTenDG.getText());
            pstmt.setDate(4, Date.valueOf(dpNgayMuon.getValue()));
            pstmt.setDate(5, Date.valueOf(dpNgayTra.getValue()));
            pstmt.setString(6, tfMaSach.getText());
            pstmt.setString(7, tfTenSach.getText());
            pstmt.setString(8, cbTrangThai.getValue());

            pstmt.executeUpdate();
            loadDataFromDatabase();
            clearFields();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSua() {
        String updateQuery = "UPDATE PhieuMuon SET maDocGia=?, tenDocGia=?, ngayMuon=?, ngayTra=?, maSach=?, tenSach=?, trangThai=? WHERE maPhieu=?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            pstmt.setString(1, tfMaDG.getText());
            pstmt.setString(2, tfTenDG.getText());
            pstmt.setDate(3, Date.valueOf(dpNgayMuon.getValue()));
            pstmt.setDate(4, Date.valueOf(dpNgayTra.getValue()));
            pstmt.setString(5, tfMaSach.getText());
            pstmt.setString(6, tfTenSach.getText());
            pstmt.setString(7, cbTrangThai.getValue());
            pstmt.setString(8, tfMaPhieu.getText());

            pstmt.executeUpdate();
            loadDataFromDatabase();
            clearFields();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleXoa() {
        String deleteQuery = "DELETE FROM PhieuMuon WHERE maPhieu=?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

            pstmt.setString(1, tfMaPhieu.getText());
            pstmt.executeUpdate();
            loadDataFromDatabase();
            clearFields();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLammoi() {
        clearFields();
    }

    private void clearFields() {
        tfMaPhieu.clear();
        tfMaDG.clear();
        tfTenDG.clear();
        tfMaSach.clear();
        tfTenSach.clear();
        dpNgayMuon.setValue(null);
        dpNgayTra.setValue(null);
        cbTrangThai.setValue(null);
    }
}
