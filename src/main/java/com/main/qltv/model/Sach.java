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

    public Sach(){}

    public Sach(String maSach, String tenSach, String maTacGia, String maTheLoai, String maNXB, int soLuong, Date ngayXuatBan, int soTrang, String moTa, String anhBia) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.maTacGia = maTacGia;
        this.maTheLoai = maTheLoai;
        this.maNXB = maNXB;
        this.soLuong = soLuong;
        this.ngayXuatBan = ngayXuatBan;
        this.soTrang = soTrang;
        this.moTa = moTa;
        this.anhBia = anhBia;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public void setMaTacGia(String maTacGia) {
        this.maTacGia = maTacGia;
    }

    public void setMaTheLoai(String maTheLoai) {
        this.maTheLoai = maTheLoai;
    }

    public void setMaNXB(String maNXB) {
        this.maNXB = maNXB;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public void setNgayXuatBan(Date ngayXuatBan) {
        this.ngayXuatBan = ngayXuatBan;
    }

    public void setSoTrang(int soTrang) {
        this.soTrang = soTrang;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public void setAnhBia(String anhBia) {
        this.anhBia = anhBia;
    }

    public String getTenSach() {
        return tenSach;
    }

    public String getAnhBia() {
        return anhBia;
    }
}
