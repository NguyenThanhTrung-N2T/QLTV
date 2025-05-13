package com.main.qltv.controller;

import com.main.qltv.DatabaseConnection;
import com.main.qltv.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class QuanLySachController {

    @FXML TextField txtMaSach;
    @FXML TextField txtTenSach;
    @FXML ComboBox<String> cbTacGia;
    @FXML ComboBox<String> cbTheLoai;
    @FXML ComboBox<String> cbNXB;
    @FXML TextField txtSoLuong;
    @FXML DatePicker dpNgayXuatBan;
    @FXML TextField txtSoTrang;
    @FXML TextArea txtMoTa;
    @FXML TextField txtAnhBia;
    @FXML ImageView imgBia;
    @FXML TableView<Sach> tableSach;
    @FXML TableColumn<Sach, String> colMaSach;
    @FXML TableColumn<Sach, String> colTenSach;
    @FXML TableColumn<Sach, String> colTacGia;
    @FXML TableColumn<Sach, String> colTheLoai;
    @FXML TableColumn<Sach, String> colNXB;
    @FXML TableColumn<Sach, Integer> colSoLuong;
    @FXML TableColumn<Sach, Date> colNgayXuatBan;
    @FXML TableColumn<Sach, Integer> colSoTrang;
    @FXML Button btnTimKiem;
    @FXML Button btnThem;
    @FXML Button btnSua;
    @FXML Button btnXoa;
    @FXML Button btnLammoi;


    private ObservableList<Sach> sachList;

    public void loadSachTuDB(){
        sachList.clear();
        sachList.addAll(DatabaseConnection.LayDanhSachSach());
        tableSach.refresh();
    }

    @FXML
    public void initialize(){
        // load dữ liệu lên table view
        sachList = FXCollections.observableArrayList();
        tableSach.setItems(sachList);
        // Tải dữ liệu
        loadSachTuDB();
        colMaSach.setCellValueFactory(new PropertyValueFactory<>("maSach"));
        colTenSach.setCellValueFactory(new PropertyValueFactory<>("tenSach"));
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        colSoTrang.setCellValueFactory(new PropertyValueFactory<>("soTrang"));
        colNgayXuatBan.setCellValueFactory(new PropertyValueFactory<>("ngayXuatBan"));

        // Hiển thị TÊN thay vì MÃ bằng cellFactory tùy chỉnh
        colTacGia.setCellValueFactory(new PropertyValueFactory<>("maTacGia"));
        colTacGia.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String maTacGia, boolean empty) {
                super.updateItem(maTacGia, empty);
                if (empty || maTacGia == null) {
                    setText(null);
                } else {
                    setText(DatabaseConnection.layTenTacGia(maTacGia));
                }
            }
        });

        colTheLoai.setCellValueFactory(new PropertyValueFactory<>("maTheLoai"));
        colTheLoai.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String maTheLoai, boolean empty) {
                super.updateItem(maTheLoai, empty);
                if (empty || maTheLoai == null) {
                    setText(null);
                } else {
                    setText(DatabaseConnection.layTenTheLoai(maTheLoai));
                }
            }
        });

        colNXB.setCellValueFactory(new PropertyValueFactory<>("maNXB"));
        colNXB.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String maNXB, boolean empty) {
                super.updateItem(maNXB, empty);
                if (empty || maNXB == null) {
                    setText(null);
                } else {
                    setText(DatabaseConnection.layTenNXB(maNXB));
                }
            }
        });

        colNgayXuatBan.setCellFactory(column -> new TableCell<>() {
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
        cbTacGia.setItems(FXCollections.observableArrayList(DatabaseConnection.layDanhSachTenTacGia()));
        cbTheLoai.setItems(FXCollections.observableArrayList(DatabaseConnection.layDanhSachTenTheLoai()));
        cbNXB.setItems(FXCollections.observableArrayList(DatabaseConnection.layDanhSachTenNXB()));
        cbTacGia.setEditable(true);
        cbTheLoai.setEditable(true);
        cbNXB.setEditable(true);

        // Sự kiện khi chọn dòng
        tableSach.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtMaSach.setText(newSelection.getMaSach());
                txtTenSach.setText(newSelection.getTenSach());
                txtSoLuong.setText(String.valueOf(newSelection.getSoLuong()));
                txtSoTrang.setText(String.valueOf(newSelection.getSoTrang()));
                dpNgayXuatBan.setValue(newSelection.getNgayXuatBan().toLocalDate());
                txtMoTa.setText(newSelection.getMoTa());
                txtAnhBia.setText(newSelection.getAnhBia());

                // Lấy tên để set cho ComboBox
                cbTacGia.setValue(DatabaseConnection.layTenTacGia(newSelection.getMaTacGia()));
                cbTheLoai.setValue(DatabaseConnection.layTenTheLoai(newSelection.getMaTheLoai()));
                cbNXB.setValue(DatabaseConnection.layTenNXB(newSelection.getMaNXB()));

                // Load ảnh bìa (nếu có URL hợp lệ)
                try {
                    imgBia.setImage(new javafx.scene.image.Image(newSelection.getAnhBia(), true));
                } catch (Exception e) {
                    imgBia.setImage(null); // Trường hợp link ảnh sai
                }
            }
        });

        // Tìm kiếm theo các thuộc tính
        btnTimKiem.setOnAction(event -> {
            String tenSach = txtTenSach.getText().toLowerCase();
            String tacGia = cbTacGia.getValue();
            String theLoai = cbTheLoai.getValue();
            String nxb = cbNXB.getValue();

            ObservableList<Sach> ketQuaTimKiem = FXCollections.observableArrayList();

            for (Sach sach : DatabaseConnection.LayDanhSachSach()) {
                boolean match = true;

                if (!tenSach.isEmpty() && !sach.getTenSach().toLowerCase().contains(tenSach)) match = false;
                if (tacGia != null && !DatabaseConnection.layTenTacGia(sach.getMaTacGia()).equals(tacGia)) match = false;
                if (theLoai != null && !DatabaseConnection.layTenTheLoai(sach.getMaTheLoai()).equals(theLoai)) match = false;
                if (nxb != null && !DatabaseConnection.layTenNXB(sach.getMaNXB()).equals(nxb)) match = false;

                if (match) ketQuaTimKiem.add(sach);
            }

            sachList.setAll(ketQuaTimKiem);
        });

        btnThem.setOnAction(event -> {
            String maSach = txtMaSach.getText().trim();
            String tenSach = txtTenSach.getText().trim();
            String tenTacGia = cbTacGia.getValue();
            String tenTheLoai = cbTheLoai.getValue();
            String tenNXB = cbNXB.getValue();
            String soLuongStr = txtSoLuong.getText().trim();
            String soTrangStr = txtSoTrang.getText().trim();
            java.time.LocalDate ngayXuatBan = dpNgayXuatBan.getValue();
            String moTa = txtMoTa.getText().trim();
            String anhBia = txtAnhBia.getText().trim();

            // Kiểm tra rỗng
            if (maSach.isEmpty() || tenSach.isEmpty() || tenTacGia == null || tenTheLoai == null || tenNXB == null
                    || soLuongStr.isEmpty() || soTrangStr.isEmpty() || ngayXuatBan == null) {
                showAlert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ thông tin sách.");
                return;
            }

            // Kiểm tra mã sách đã tồn tại
            if (DatabaseConnection.kiemTraMaSachTonTai(maSach)) {
                showAlert(Alert.AlertType.ERROR, "Mã sách đã tồn tại!");
                return;
            }

            try {
                int soLuong = Integer.parseInt(soLuongStr);
                int soTrang = Integer.parseInt(soTrangStr);

                // Lấy mã từ tên (nếu cần)
                try {
                    String maTacGia = DatabaseConnection.layMaTacGiaTheoTen(tenTacGia);
                    String maTheLoai = DatabaseConnection.layMaTheLoaiTheoTen(tenTheLoai);
                    String maNXB = DatabaseConnection.layMaNXBTheoTen(tenNXB);

                    Sach sachMoi = new Sach(maSach, tenSach, maTacGia, maTheLoai, maNXB, soLuong,
                            Date.valueOf(ngayXuatBan), soTrang, moTa, anhBia);
                    TacGia tacgiamoi = new TacGia(maTacGia, tenTacGia);
                    TheLoai theloaimoi = new TheLoai(maTheLoai, tenTheLoai);
                    NhaXuatBan nxbmoi = new NhaXuatBan(maNXB, tenNXB);

                    boolean thanhCong = DatabaseConnection.themSach(sachMoi, tacgiamoi, theloaimoi, nxbmoi);
                    if (thanhCong) {
                        showAlert(Alert.AlertType.INFORMATION, "Thêm sách thành công.");
                        loadSachTuDB();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Thêm sách thất bại!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Lỗi truy vấn cơ sở dữ liệu!");
                }

            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Số lượng và số trang phải là số nguyên.");
            }
            lamMoi();

        });

        btnSua.setOnAction(event -> {
            String maSach = txtMaSach.getText().trim();
            String tenSach = txtTenSach.getText().trim();
            String tenTacGia = cbTacGia.getValue();
            String tenTheLoai = cbTheLoai.getValue();
            String tenNXB = cbNXB.getValue();
            String soLuongStr = txtSoLuong.getText().trim();
            String soTrangStr = txtSoTrang.getText().trim();
            LocalDate ngayXuatBan = dpNgayXuatBan.getValue();
            String moTa = txtMoTa.getText().trim();
            String anhBia = txtAnhBia.getText().trim();

            if (maSach.isEmpty() || tenSach.isEmpty() || tenTacGia == null || tenTheLoai == null || tenNXB == null
                    || soLuongStr.isEmpty() || soTrangStr.isEmpty() || ngayXuatBan == null) {
                showAlert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ thông tin sách.");
                return;
            }

            if (!DatabaseConnection.kiemTraMaSachTonTai(maSach)) {
                showAlert(Alert.AlertType.ERROR, "Mã sách không tồn tại để sửa!");
                return;
            }

            try {
                int soLuong = Integer.parseInt(soLuongStr);
                int soTrang = Integer.parseInt(soTrangStr);
                java.sql.Date sqlNgayXuatBan = java.sql.Date.valueOf(ngayXuatBan);

                String maTacGia = DatabaseConnection.layMaTacGiaTheoTen(tenTacGia);
                String maTheLoai = DatabaseConnection.layMaTheLoaiTheoTen(tenTheLoai);
                String maNXB = DatabaseConnection.layMaNXBTheoTen(tenNXB);

                Sach sachSua = new Sach(maSach, tenSach, maTacGia, maTheLoai, maNXB, soLuong,
                        sqlNgayXuatBan, soTrang, moTa, anhBia);

                TacGia tacgiamoi = new TacGia(maTacGia, tenTacGia);
                TheLoai theloaimoi = new TheLoai(maTheLoai, tenTheLoai);
                NhaXuatBan nxbmoi = new NhaXuatBan(maNXB, tenNXB);

                boolean thanhCong = DatabaseConnection.capNhatSach(sachSua, tacgiamoi, theloaimoi, nxbmoi);
                if (thanhCong) {
                    showAlert(Alert.AlertType.INFORMATION, "Cập nhật sách thành công.");
                    loadSachTuDB();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Cập nhật sách thất bại!");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Số lượng và số trang phải là số nguyên.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Lỗi truy vấn cơ sở dữ liệu!");
            }

            lamMoi();
        });




    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @FXML
    private void lamMoi(){
        txtMaSach.clear();
        txtTenSach.clear();
        cbTacGia.setValue(null);
        cbTheLoai.setValue(null);
        cbNXB.setValue(null);
        txtSoLuong.clear();
        txtSoTrang.clear();
        dpNgayXuatBan.setValue(null);
        txtMoTa.clear();
        txtAnhBia.clear();
        imgBia.setImage(null);

        // Bỏ chọn dòng trong TableView nếu có
        tableSach.getSelectionModel().clearSelection();

    }
}
