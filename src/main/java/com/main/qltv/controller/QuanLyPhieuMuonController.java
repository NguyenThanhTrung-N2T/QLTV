package com.main.qltv.controller;

import com.main.qltv.DatabaseConnection;
import com.main.qltv.model.PhieuMuon;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
    @FXML ComboBox<String> comboTensach;
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

        ObservableList<String> allItems = FXCollections.observableArrayList(DatabaseConnection.layDanhSachTenSach());
        FilteredList<String> filteredItems = new FilteredList<>(allItems, s -> true);

        comboTensach.setEditable(true);
        comboTensach.setItems(filteredItems);

        TextField editor = comboTensach.getEditor();

        editor.textProperty().addListener((obs, oldValue, newValue) -> {
            // Nếu người dùng chọn item rồi thì bỏ qua
            if (comboTensach.getSelectionModel().getSelectedItem() != null &&
                    comboTensach.getSelectionModel().getSelectedItem().equals(newValue)) {
                return;
            }

            // Đặt toàn bộ logic lọc và hiển thị/ẩn vào Platform.runLater()
            // Điều này đảm bảo rằng các thao tác UI được thực hiện an toàn trên luồng JavaFX Application Thread
            Platform.runLater(() -> {
                filteredItems.setPredicate(item -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    return item.toLowerCase().contains(lowerCaseFilter);
                });

                if (filteredItems.isEmpty()) {
                    // Nếu không có kết quả lọc, ẩn ComboBox
                    if (comboTensach.isShowing()) {
                        comboTensach.hide();
                    }
                } else {
                    // Nếu có kết quả, đảm bảo ComboBox hiển thị
                    if (!comboTensach.isShowing()) {
                        comboTensach.show();
                    }
                }
                // Quan trọng: Đặt lại vị trí con trỏ về cuối văn bản sau khi show/hide
                // Điều này giúp tránh lỗi IllegalArgumentException khi con trỏ bị sai vị trí
                editor.positionCaret(editor.getText().length());
            });
        });






        // Sự kiện khi chọn dòng
        muonTraTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtMSSV.setText(newSelection.getMaSinhVien());
                comboTensach.setValue(DatabaseConnection.layTenSach(newSelection.getMaSach()));
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
        comboTensach.setValue(null);
        spnSoLuong.getValueFactory().setValue(1);
        dateMuon.setValue(null);
        dateTra.setValue(null);
        muonTraTable.getSelectionModel().clearSelection();
    }

//    @FXML
//    private void MuonSach(){
//        String maPhieuMuon =
//        String maSach = txtTenSach.getText();
//        String maSinhVien = txtMSSV.getText();
//        LocalDate ngayMuon = dateMuon.getValue();
//        LocalDate ngayTra = dateTra.getValue();
//        int soLuong = spnSoLuong.getValue();
//
//        if (maSach.isEmpty() || maSinhVien.isEmpty() || ngayMuon == null || ngayTra == null) {
//            Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ thông tin.");
//            alert.showAndWait();
//            return;
//        }
//
//        PhieuMuon phieuMuon = new PhieuMuon(maPhieuMuon, maSinhVien, Date.valueOf(ngayMuon), Date.valueOf(ngayTra), "Chưa trả", maSach, maSach, soLuong);
//        boolean ketQua = DatabaseConnection.themPhieuMuon(phieuMuon);
//        if (ketQua) {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Thêm phiếu mượn thành công.");
//            alert.showAndWait();
//            loadPhieuMUonTuDB();
//            lamMoi();
//        } else {
//            Alert alert = new Alert(Alert.AlertType.ERROR, "Thêm phiếu mượn thất bại.");
//            alert.showAndWait();
//        }
//    }

}
