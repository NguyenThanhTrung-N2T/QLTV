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
    @FXML PasswordField txtMatKhau;
    @FXML TextField txtMaSoCB;
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
    @FXML TableColumn<TaiKhoan,String> colMaSoCB;
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
        colMaSoCB.setCellValueFactory(new PropertyValueFactory<>("MaSoCB"));
        colMaSinhVien.setCellValueFactory(new PropertyValueFactory<>("MSSV"));

        // Tải dữ liệu
        loadTaiKhoanTuDB();

        // Sự kiện chọn dòng
        // Sự kiện khi chọn dòng
        tableTaiKhoan.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtMaTaiKhoan.setText(newSelection.getMaTK());
                txtTenDangNhap.setText(newSelection.getTenDangNhap());
                txtMatKhau.setText(newSelection.getMatKhauTK());
                txtMaSoCB.setText(newSelection.getMaSoCB());
                txtMaSinhVien.setText(newSelection.getMSSV());
                cbLoaiNguoiDung.setValue(newSelection.getLoaiTK());
            }
        });
        
        btnThem.setOnAction(e-> themTaiKhoan());
        btnLamMoi.setOnAction(e-> lamMoi());
    }

    @FXML
    private void lamMoi(){
        txtMaTaiKhoan.clear();
        txtMatKhau.clear();
        txtTenDangNhap.clear();
        txtMaSinhVien.clear();
        cbLoaiNguoiDung.setValue(null);
        tableTaiKhoan.getSelectionModel().clearSelection();
    }

    @FXML
    private void themTaiKhoan(){
        if (txtTenDangNhap.getText().isEmpty() || txtMatKhau.getText().isEmpty() ||
                cbLoaiNguoiDung.getValue() == null || (txtMaSinhVien.getText().isEmpty() && cbLoaiNguoiDung.getValue().toString().equals("sinhvien"))) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        String maSV = txtMaSinhVien.getText();
        if (maSV != null && DatabaseConnection.kiemTraTaiKhoanDaTonTai(maSV)) {
            showAlert(Alert.AlertType.ERROR, "Tài khoản của sinh viên đã tồn tại.");
            return;
        }

        if(txtMaSinhVien.getText().isEmpty() && cbLoaiNguoiDung.getValue().toString().equals("quanly"))
        {
            showAlert(Alert.AlertType.ERROR,"Không thể tạo tài khoản quản lý cho sinh viên.");
            return;
        }

        if(txtMaSinhVien.getText() != null  && !DatabaseConnection.kiemTraSinhVienTonTai(txtMaSinhVien.getText())){
            showAlert(Alert.AlertType.ERROR,"Sinh viên chưa được thêm vào cơ sở dữ liệu.");
            return;
        }

        TaiKhoan tk = new TaiKhoan();
        tk.setMSSV(txtMaSinhVien.getText());
        tk.setMaTK(txtMaTaiKhoan.getText());
        tk.setLoaiTK(cbLoaiNguoiDung.getValue().toString());
        tk.setMatKhauTK(txtMatKhau.getText());
        tk.setTenDangNhap(txtTenDangNhap.getText());

        if (DatabaseConnection.themTaiKhoan(tk)) {
            showAlert(Alert.AlertType.INFORMATION, "Thêm tài khoản thành công.");
            loadTaiKhoanTuDB();
            lamMoi();
        } else {
            showAlert(Alert.AlertType.ERROR, "Thêm tài khoản thất bại.");
        }

    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }



}
