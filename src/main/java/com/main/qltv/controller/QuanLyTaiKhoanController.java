package com.main.qltv.controller;

import com.main.qltv.DatabaseConnection;
import com.main.qltv.model.SinhVien;
import com.main.qltv.model.TaiKhoan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class QuanLyTaiKhoanController {
    @FXML TextField txtTimKiem;
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

        // tìm kiếm
        FilteredList<TaiKhoan> filteredList = new FilteredList<>(taiKhoanList, p -> true);
        txtTimKiem.textProperty().addListener((obs, oldVal, newVal) -> {
            String keyword = newVal.toLowerCase();
            filteredList.setPredicate(tk -> {
                if (keyword.isEmpty()) return true;
                return tk.getMaTK().toLowerCase().contains(keyword)
                        || tk.getTenDangNhap().toLowerCase().contains(keyword)
                        || tk.getLoaiTK().toLowerCase().contains(keyword)||(tk.getMSSV()!= null && tk.getMSSV().toLowerCase().contains(keyword))
                        || (tk.getMaSoCB()!= null && tk.getMaSoCB().toLowerCase().contains(keyword));
            });

            tableTaiKhoan.setItems(filteredList);
        });

        // Tải dữ liệu
        loadTaiKhoanTuDB();

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
        btnSua.setOnAction(e -> SuaTaiKhoan());
        btnLamMoi.setOnAction(e-> lamMoi());
        btnXoa.setOnAction(e-> XoaTaiKhoan());
    }

    @FXML
    private void lamMoi(){
        txtMaTaiKhoan.clear();
        txtMatKhau.clear();
        txtTenDangNhap.clear();
        txtMaSinhVien.clear();
        txtMaSoCB.clear();
        cbLoaiNguoiDung.setValue(null);
        tableTaiKhoan.getSelectionModel().clearSelection();
    }

    @FXML
    private void themTaiKhoan(){
        if (txtTenDangNhap.getText().isEmpty() || txtMatKhau.getText().isEmpty() ||
                cbLoaiNguoiDung.getValue() == null || (txtMaSinhVien.getText().isEmpty() && (cbLoaiNguoiDung.getValue().toString().equals("Sinh viên") || cbLoaiNguoiDung.getValue().toString().equals("Cộng tác viên")))) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ thông tin tài khoản.");
            return;
        }

        if(txtMaSinhVien.getText().isEmpty() && txtMaSoCB.getText().isEmpty())
        {
            showAlert(Alert.AlertType.WARNING,"Vui lòng nhập đầy đủ thông tin tài khoản.");
            return;
        }


        String maSV = txtMaSinhVien.getText();
        if (maSV != null && DatabaseConnection.kiemTraTaiKhoanDaTonTai(maSV)) {
            showAlert(Alert.AlertType.ERROR, "Tài khoản của sinh viên đã tồn tại.");
            return;
        }

        String maCB = txtMaSoCB.getText();
        if(maCB != null && DatabaseConnection.kiemTraTaiKhoanDaTonTaiCuaCB(maCB)){
            showAlert(Alert.AlertType.ERROR,"Tài khoản của cán bộ đã tồn tại.");
            return;
        }


        if(cbLoaiNguoiDung.getValue().toString().equals("Quản lý") && txtMaSoCB.getText().isEmpty())
        {
            showAlert(Alert.AlertType.ERROR,"Không thể tạo tài khoản quản lý cho sinh viên.");
            return;
        }

        if(txtMaSinhVien.getText() != null  && !DatabaseConnection.kiemTraSinhVienTonTai(txtMaSinhVien.getText()) && txtMaSoCB.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR,"Sinh viên chưa được thêm vào cơ sở dữ liệu.");
            return;
        }

        TaiKhoan tk = new TaiKhoan();
        if( txtMaSinhVien.getText().isEmpty())
            tk.setMSSV(null);
        else
            tk.setMSSV(txtMaSinhVien.getText());
        tk.setMaTK(txtMaTaiKhoan.getText());
        tk.setLoaiTK(cbLoaiNguoiDung.getValue().toString());
        tk.setMatKhauTK(txtMatKhau.getText());
        tk.setTenDangNhap(txtTenDangNhap.getText());
        if (txtMaSoCB.getText().isEmpty())
            tk.setMaSoCB(null);
        else
            tk.setMaSoCB(txtMaSoCB.getText());

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

    @FXML
    private void XoaTaiKhoan() {
        TaiKhoan selected = tableTaiKhoan.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn tài khoản cần xóa.");
            return;
        }

        // Xác nhận trước khi xóa
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Bạn có chắc chắn muốn xóa tài khoản này?");
        if (confirmAlert.showAndWait().get() != ButtonType.OK) {
            return;
        }

        if(selected.getMSSV()!=null)
        {
            // xóa tài khoản
            if(DatabaseConnection.xoaTaiKhoan(selected.getMSSV())){


                showAlert(Alert.AlertType.INFORMATION, "Xóa thành công.");
                loadTaiKhoanTuDB();
                lamMoi();
            }
            else {
                showAlert(Alert.AlertType.ERROR, "Xóa thất bại. Tài khoản đang mượn sách hoặc lỗi hệ thống.");
            }
        }else{
            if(DatabaseConnection.xoaTaiKhoan(selected.getMaSoCB())){
                showAlert(Alert.AlertType.INFORMATION, "Xóa thành công.");
                loadTaiKhoanTuDB();
                lamMoi();
            }
            else {
                showAlert(Alert.AlertType.ERROR, "Xóa thất bại.");
            }
        }
    }

    @FXML
    private void SuaTaiKhoan() {
        TaiKhoan selected = tableTaiKhoan.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng chọn tài khoản cần sửa.");
            return;
        }

        TaiKhoan tkMoi = new TaiKhoan();
        tkMoi.setMaTK(txtMaTaiKhoan.getText());
        tkMoi.setTenDangNhap(txtTenDangNhap.getText());
        tkMoi.setMatKhauTK(txtMatKhau.getText());
        tkMoi.setLoaiTK(cbLoaiNguoiDung.getValue().toString());
        if (txtMaSinhVien != null) {
            tkMoi.setMSSV(txtMaSinhVien.getText());
        } else {
            tkMoi.setMSSV(null);
        }
        if (txtMaSoCB != null) {
            tkMoi.setMaSoCB(txtMaSoCB.getText());
        } else {
            tkMoi.setMaSoCB(null);
        }

        if (selected.equals(tkMoi)) {
            showAlert(Alert.AlertType.INFORMATION, "Không có thay đổi nào.");
            return;
        }

        if (DatabaseConnection.capNhatTaiKhoan(tkMoi)) {
            showAlert(Alert.AlertType.INFORMATION, "Cập nhật tài khoản thành công.");
            loadTaiKhoanTuDB();
            lamMoi();
        } else {
            showAlert(Alert.AlertType.ERROR, "Cập nhật tài khoản thất bại.");
        }
    }
}
