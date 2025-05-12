package com.main.qltv.controller;

import com.main.qltv.DatabaseConnection;
import com.main.qltv.model.Sach;
import com.main.qltv.model.SinhVien;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.sql.Date;

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


        // Tải dữ liệu
        loadSachTuDB();
    }

}
