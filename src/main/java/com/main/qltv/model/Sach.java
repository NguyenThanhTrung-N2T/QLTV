package com.main.qltv.model;

import java.sql.Date;

public class Sach {
    public String maSach;
    public String tenSach;
    public String maTacGia;
    public String maTheLoai;
    public String maNXB;
    public int soLuong;
    public Date ngayXuatBan;
    public int soTrang;
    public String moTa;
    public String anhBia;

    public String getMaSach() {
        return maSach;
    }

    public String getMoTa() {
        return moTa;
    }

    public int getSoTrang() {
        return soTrang;
    }

    public Date getNgayXuatBan() {
        return ngayXuatBan;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public String getMaNXB() {
        return maNXB;
    }

    public String getMaTheLoai() {
        return maTheLoai;
    }

    public String getMaTacGia() {
        return maTacGia;
    }

    public String getTenSach() {
        return tenSach;
    }

    public String getAnhBia() {
        return anhBia;
    }
}
