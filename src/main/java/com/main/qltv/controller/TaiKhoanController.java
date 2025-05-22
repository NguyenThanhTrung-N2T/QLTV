package com.main.qltv.controller;

import com.main.qltv.DatabaseConnection;
import com.main.qltv.QLTVApplication;
import com.main.qltv.model.PhieuMuon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;

public class TaiKhoanController {

    @FXML Label lblMaTaiKhoan;
    @FXML Label lblTenDangNhap;
    @FXML Label lblLoaiNguoiDung;
    @FXML Label lblMaSoCB;
    @FXML Label lblMaSinhVien;
    @FXML Label titleMaSoCB;
    @FXML Label titleMaSinhVien;
    @FXML Button btnCapNhatThongTin;
    @FXML GridPane yourGridPane;

    // TableView để hiển thị lịch sử mượn trả
    @FXML TableView<PhieuMuon> tableLichSuMuonTra;
    @FXML TableColumn<PhieuMuon, String> colMaPhieu;
    @FXML TableColumn<PhieuMuon, String> colMaSach;
    @FXML TableColumn<PhieuMuon, String> colTenSach;
    @FXML TableColumn<PhieuMuon, Date> colNgayMuon;
    @FXML TableColumn<PhieuMuon, Date> colNgayTra;
    @FXML TableColumn<PhieuMuon, Integer> colSoLuong;
    @FXML TableColumn<PhieuMuon, String> colTinhTrang;


    // Danh sách phiếu mượn
    private ObservableList<PhieuMuon> phieuMuonCaNhan;

    public void loadPhieuMUonCaNhanTuDB(){
        phieuMuonCaNhan.clear();
        phieuMuonCaNhan.addAll(DatabaseConnection.layDanhSachPhieuMuonTheoNguoiDung(QLTVApplication.nguoidangnhap.getMSSV(),
                QLTVApplication.nguoidangnhap.getMaSoCB()));
        tableLichSuMuonTra.refresh();
    }


    @FXML
    private void initialize() {


        tableLichSuMuonTra.setPlaceholder(new Label("Chưa có lịch sử mượn trả nào."));
        btnCapNhatThongTin.setOnAction(event -> capNhatThongTin());

        // Giả lập dữ liệu cho các label
        lblMaTaiKhoan.setText(QLTVApplication.nguoidangnhap.getMaTK());
        lblTenDangNhap.setText(QLTVApplication.nguoidangnhap.getTenDangNhap());
        lblLoaiNguoiDung.setText(QLTVApplication.nguoidangnhap.getLoaiTK());
        if (QLTVApplication.nguoidangnhap.getLoaiTK().equals("Quản lý")) {
            lblMaSoCB.setText(QLTVApplication.nguoidangnhap.getMaSoCB());
            removeRow(yourGridPane, 4); // ẩn dòng Mã sinh viên (row 4)
        } else {
            lblMaSinhVien.setText(QLTVApplication.nguoidangnhap.getMSSV());
            removeRow(yourGridPane, 3); // ẩn dòng Mã số CB (row 3)
        }

        // load dữ liệu lên table view
        phieuMuonCaNhan = FXCollections.observableArrayList();
        tableLichSuMuonTra.setItems(phieuMuonCaNhan);
        // Tải dữ liệu
        loadPhieuMUonCaNhanTuDB();

        colMaPhieu.setCellValueFactory(new PropertyValueFactory<>("maPhieuMuon"));
        colMaSach.setCellValueFactory(new PropertyValueFactory<>("maSach"));

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






    }

    @FXML
    private void capNhatThongTin() {
        try {
            // Load FXML cửa sổ cập nhật
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/main/qltv/view/CapNhatTaiKhoan.fxml"));
            Parent root = loader.load();

            // Lấy controller của cửa sổ cập nhật
            CapNhatTaiKhoanController controller = loader.getController();

            // Gửi thông tin hiện tại sang cửa sổ cập nhật
            controller.setThongTin(
                    lblTenDangNhap.getText(),
                    lblMaSoCB.getText(),
                    lblMaSinhVien.getText()
            );

            // Tạo và mở cửa sổ mới
            Stage stage = new Stage();
            stage.setTitle("Cập nhật thông tin tài khoản");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait(); // Chờ cập nhật xong mới quay lại

            // Sau khi cửa sổ đóng, có thể reload thông tin nếu cần
            // Ví dụ: loadThongTinMoi(); // bạn có thể viết hàm này nếu cập nhật DB

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeRow(GridPane grid, int row) {
        // Xóa tất cả node có rowIndex = row
        grid.getChildren().removeIf(node -> {
            Integer r = GridPane.getRowIndex(node);
            return r != null && r == row;
        });

        // Cập nhật lại rowIndex cho các dòng phía sau
        for (Node child : grid.getChildren()) {
            Integer r = GridPane.getRowIndex(child);
            if (r != null && r > row) {
                GridPane.setRowIndex(child, r - 1);
            }
        }
    }



}
