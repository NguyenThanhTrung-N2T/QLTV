package com.main.qltv.controller;

import com.main.qltv.DatabaseConnection;
import com.main.qltv.model.SinhVien;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class QuanLySinhVienController {
    @FXML TableView<SinhVien> tableSinhVien;
    @FXML TableColumn<SinhVien, String> colMaSinhVien;
    @FXML TableColumn<SinhVien, String> colTenSinhVien;
    @FXML TableColumn<SinhVien, String> colGioiTinh;
    @FXML TableColumn<SinhVien, java.sql.Date> colNgaySinh;
    @FXML TableColumn<SinhVien, String> colDiaChi;
    @FXML TableColumn<SinhVien, String> colEmail;
    @FXML TableColumn<SinhVien, String> colSoDienThoai;
    @FXML ComboBox<String> cbGioiTinh;
    @FXML DatePicker dpNgaySinh;
    @FXML TextField txtMaSinhVien, txtTenSinhVien, txtDiaChi, txtEmail, txtSoDienThoai;
    @FXML Button btnThem, btnSua, btnXoa, btnLamMoi;

    private ObservableList<SinhVien> sinhVienList;

    public void loadSinhVienTuDB(){
        sinhVienList.clear();
        sinhVienList.addAll(DatabaseConnection.LayDanhSachSinhVien());
        tableSinhVien.refresh(); // đảm bảo TableView vẽ lại
    }

    @FXML
    public void initialize() {
        dpNgaySinh.setPromptText("Chọn ngày sinh");
        // danh sách sinh viên
        sinhVienList = FXCollections.observableArrayList(); // KHỞI TẠO ở đây
        tableSinhVien.setItems(sinhVienList);
        colMaSinhVien.setCellValueFactory(new PropertyValueFactory<>("MSSV"));
        colTenSinhVien.setCellValueFactory(new PropertyValueFactory<>("tenSinhVien"));
        colGioiTinh.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        colNgaySinh.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        colDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colSoDienThoai.setCellValueFactory(new PropertyValueFactory<>("SDT"));
        // Đổ dữ liệu giới tính vào ComboBox
        cbGioiTinh.setItems(FXCollections.observableArrayList("Nam", "Nữ",null));
        // Định dạng ngày
        colNgaySinh.setCellFactory(column -> new TableCell<>() {
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

        // Tải dữ liệu khi controller khởi tạo
        loadSinhVienTuDB();

        // Sự kiện khi chọn dòng
        tableSinhVien.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtMaSinhVien.setText(newSelection.getMSSV());
                txtTenSinhVien.setText(newSelection.getTenSinhVien());
                cbGioiTinh.setValue(newSelection.getGioiTinh());
                dpNgaySinh.setValue(newSelection.getNgaySinh().toLocalDate());
                txtDiaChi.setText(newSelection.getDiaChi());
                txtEmail.setText(newSelection.getEmail());
                txtSoDienThoai.setText(newSelection.getSDT());
            }
        });
        btnThem.setOnAction(e -> ThemSinhVien());
        btnSua.setOnAction(e -> SuaSinhVien());
        btnXoa.setOnAction(e -> XoaSinhVien());
        btnLamMoi.setOnAction(e -> LamMoi());


    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @FXML
    private void ThemSinhVien() {
        if (txtMaSinhVien.getText().isEmpty() || txtTenSinhVien.getText().isEmpty() ||
                cbGioiTinh.getValue() == null || dpNgaySinh.getValue() == null ||
                txtDiaChi.getText().isEmpty() || txtEmail.getText().isEmpty() ||
                txtSoDienThoai.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        String maSV = txtMaSinhVien.getText();
        if (DatabaseConnection.kiemTraSinhVienTonTai(maSV)) {
            showAlert(Alert.AlertType.ERROR, "Sinh viên đã tồn tại.");
            return;
        }

        SinhVien sv = new SinhVien();
        sv.setMSSV(txtMaSinhVien.getText());
        sv.setTenSinhVien(txtTenSinhVien.getText());
        sv.setGioiTinh(cbGioiTinh.getValue());
        sv.setNgaySinh(java.sql.Date.valueOf(dpNgaySinh.getValue()));
        sv.setDiaChi(txtDiaChi.getText());
        sv.setEmail(txtEmail.getText());
        sv.setSDT(txtSoDienThoai.getText());
        if (DatabaseConnection.themSinhVien(sv)) {
            showAlert(Alert.AlertType.INFORMATION, "Thêm sinh viên thành công.");
            loadSinhVienTuDB();
            LamMoi();
        } else {
            showAlert(Alert.AlertType.ERROR, "Thêm sinh viên thất bại.");
        }
    }

    @FXML
    private void SuaSinhVien() {
        SinhVien selected = tableSinhVien.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn sinh viên cần sửa.");
            return;
        }

        SinhVien svMoi = new SinhVien();
        svMoi.setMSSV(txtMaSinhVien.getText());
        svMoi.setTenSinhVien(txtTenSinhVien.getText());
        svMoi.setGioiTinh(cbGioiTinh.getValue());
        svMoi.setNgaySinh(java.sql.Date.valueOf(dpNgaySinh.getValue()));
        svMoi.setDiaChi(txtDiaChi.getText());
        svMoi.setEmail(txtEmail.getText());
        svMoi.setSDT(txtSoDienThoai.getText());

        if (selected.equals(svMoi)) {
            showAlert(Alert.AlertType.INFORMATION, "Không có thay đổi nào.");
            return;
        }

        if (DatabaseConnection.capNhatSinhVien(svMoi)) {
            showAlert(Alert.AlertType.INFORMATION, "Cập nhật thành công.");
            loadSinhVienTuDB();
            LamMoi();
        } else {
            showAlert(Alert.AlertType.ERROR, "Cập nhật thất bại.");
        }
    }

    @FXML
    private void XoaSinhVien() {
        SinhVien selected = tableSinhVien.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn sinh viên cần xóa.");
            return;
        }

        // Xác nhận trước khi xóa
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Bạn có chắc chắn muốn xóa sinh viên này?");
        if (confirmAlert.showAndWait().get() != ButtonType.OK) {
            return;
        }

        // Xóa sinh viên
        if (DatabaseConnection.kiemTraTaiKhoanDaTonTai(selected.getMSSV())) {
            // Nếu có tài khoản thì xóa tài khoản trước
            DatabaseConnection.xoaTaiKhoan(selected.getMSSV());
            // xóa sịnh viên
            DatabaseConnection.xoaSinhVien(selected.getMSSV());
            showAlert(Alert.AlertType.INFORMATION, "Xóa thành công.");
            loadSinhVienTuDB();
            LamMoi();
        } else if(DatabaseConnection.kiemTraSinhVienTonTai(selected.getMSSV())) {
            // xóa sịnh viên
            DatabaseConnection.xoaSinhVien(selected.getMSSV());
            showAlert(Alert.AlertType.INFORMATION, "Xóa thành công.");
            loadSinhVienTuDB();
            LamMoi();
        }
        else {
            showAlert(Alert.AlertType.ERROR, "Xóa thất bại.");
        }
    }

    @FXML
    private void LamMoi() {
        txtMaSinhVien.clear();
        txtTenSinhVien.clear();
        cbGioiTinh.setValue(null);
        dpNgaySinh.setValue(null);
        txtDiaChi.clear();
        txtEmail.clear();
        txtSoDienThoai.clear();
        tableSinhVien.getSelectionModel().clearSelection();
    }



}
