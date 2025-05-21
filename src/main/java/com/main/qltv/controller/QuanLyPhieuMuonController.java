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
    @FXML Button btnGiaHan;
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

        // Đổ dữ liệu vào ComboBox
        ObservableList<String> allItems = FXCollections.observableArrayList(DatabaseConnection.layDanhSachTenSach());
        FilteredList<String> filteredItems = new FilteredList<>(allItems, s -> true);

        comboTensach.setEditable(true);
        comboTensach.setItems(filteredItems);
        comboTensach.setOnShowing(e -> {
            // Khi ComboBox được mở, đặt lại bộ lọc để hiển thị tất cả các mục
            filteredItems.setPredicate(s -> true);
        });

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
                    if (!comboTensach.isShowing() && editor.isFocused()) {
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

        // Search by student name, book name, or status
        txtTimKiem.textProperty().addListener((obs, oldValue, newValue) -> {
            String keyword = newValue.toLowerCase().trim();
            ObservableList<PhieuMuon> ketQuaTimKiem = FXCollections.observableArrayList();

            for (PhieuMuon pm : DatabaseConnection.layDanhSachPhieuMuon()) {
                String tenSV = DatabaseConnection.layTenSinhVien(pm.getMaSinhVien()).toLowerCase();
                String tenSach = DatabaseConnection.layTenSach(pm.getMaSach()).toLowerCase();
                String tinhTrang = pm.getTinhTrang().toLowerCase();
                String maSV = pm.getMaSinhVien().toLowerCase();

                if (tenSV.contains(keyword) || tenSach.contains(keyword) || tinhTrang.contains(keyword) || maSV.contains(keyword)) {
                    ketQuaTimKiem.add(pm);
                }
            }
            danhSachPhieuMuon.setAll(ketQuaTimKiem);
        });

        btnMuon.setOnAction(e-> muonSach());
        btnTra.setOnAction(e-> traSach());
        btnGiaHan.setOnAction(e-> giaHanPhieuMuon());
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

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @FXML
    private void muonSach() {
        String mssv = txtMSSV.getText();
        String tenSach = comboTensach.getValue();
        Integer soLuong = (Integer) spnSoLuong.getValue();
        LocalDate ngayMuon = dateMuon.getValue();
        LocalDate ngayTra = dateTra.getValue();

        if (mssv.isEmpty() || tenSach == null || soLuong == null || ngayMuon == null || ngayTra == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ thông tin.");
            return;
        }
        if (!DatabaseConnection.kiemTraTaiKhoanDaTonTai(mssv)) {
            showAlert(Alert.AlertType.ERROR, "Sinh viên chưa có tài khoản, không thể mượn sách.");
            return;
        }
        String maSach = DatabaseConnection.layMaSachTheoTen(tenSach);
        PhieuMuon pm = new PhieuMuon();
        pm.setMaSinhVien(txtMSSV.getText());
        pm.setMaSach(maSach);
        pm.setNgayMuon(java.sql.Date.valueOf(ngayMuon));
        pm.setNgayTra(java.sql.Date.valueOf(ngayTra));
        pm.setSoLuong(soLuong);
        pm.setTinhTrang("Chưa trả");

        // Kiểm tra ngày trả có hợp lệ không
        if (ngayTra.isBefore(ngayMuon)) {
            showAlert(Alert.AlertType.ERROR, "Ngày trả không hợp lệ.");
            return;
        }

        // Kiem tra sinh vien co tai khoan chua
        if (!DatabaseConnection.kiemTraTaiKhoanDaTonTai(mssv)) {
            showAlert(Alert.AlertType.ERROR, "Sinh viên chưa có tài khoản.");
            return;
        }

        // Kiểm tra số lượng sách có đủ không
        if (DatabaseConnection.laySoLuongSach(maSach) < soLuong) {
            showAlert(Alert.AlertType.ERROR, "Số lượng sách không đủ.");
            return;
        }
        if (DatabaseConnection.themPhieuMuon(pm)) {
            showAlert(Alert.AlertType.INFORMATION, "Mượn sách thành công.");
            loadPhieuMUonTuDB();
            lamMoi();
        } else {
            showAlert(Alert.AlertType.ERROR, "Mượn sách thất bại.");
        }
    }

    @FXML
    private void traSach() {
        PhieuMuon selected = muonTraTable.getSelectionModel().getSelectedItem();
        // Kiểm tra xem có phiếu mượn nào được chọn không
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn phiếu mượn để trả sách.");
            return;
        }
        // Kiểm tra xem sách đã được trả chưa
        if (selected.getTinhTrang().equals("Đã trả")) {
            showAlert(Alert.AlertType.WARNING, "Sách đã được trả.");
            return;
        }


        if (DatabaseConnection.capNhatTinhTrangPhieuMuon(selected.getMaPhieuMuon(), "Đã trả")) {
            showAlert(Alert.AlertType.INFORMATION, "Trả sách thành công.");
            loadPhieuMUonTuDB();
            lamMoi();
        } else {
            showAlert(Alert.AlertType.ERROR, "Trả sách thất bại.");
        }
    }

    @FXML
    private void giaHanPhieuMuon() {
        PhieuMuon selected = muonTraTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn phiếu mượn để gia hạn.");
            return;
        }

        // Kiểm tra xem sách đã được trả chưa
        if (selected.getTinhTrang().equals("Đã trả")) {
            showAlert(Alert.AlertType.WARNING, "Sách đã được trả.");
            return;
        }

        LocalDate ngayTraMoi = dateTra.getValue();
        if (ngayTraMoi == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn ngày trả mới.");
            return;
        }

        if (ngayTraMoi.isBefore(selected.getNgayMuon().toLocalDate())) {
            showAlert(Alert.AlertType.ERROR, "Ngày trả mới phải sau ngày mượn.");
            return;
        }
        if (DatabaseConnection.giaHanPhieuMuon(selected.getMaPhieuMuon(), java.sql.Date.valueOf(ngayTraMoi))) {
            showAlert(Alert.AlertType.INFORMATION, "Gia hạn phiếu mượn thành công.");
            loadPhieuMUonTuDB();
            lamMoi();
        } else {
            showAlert(Alert.AlertType.ERROR, "Gia hạn phiếu mượn thất bại.");
        }
    }

}
