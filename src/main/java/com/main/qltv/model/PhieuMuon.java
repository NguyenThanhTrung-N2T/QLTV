package com.main.qltv.model;
import java.sql.Date;

public class PhieuMuon{
    private String maPhieuMuon;
    private String maSinhVien;
    private Date ngayMuon;
    private Date ngayTra;
    private String tinhTrang;
    private String maMuonSach;
    private String maSach;
    private int soLuong;

    public PhieuMuon(String maPhieuMuon, String maSinhVien, Date ngayMuon, Date ngayTra, String tinhTrang, String maMuonSach, String maSach, int soLuong) {
        this.maPhieuMuon = maPhieuMuon;
        this.maSinhVien = maSinhVien;
        this.ngayMuon = ngayMuon;
        this.ngayTra = ngayTra;
        this.tinhTrang = tinhTrang;
        this.maMuonSach = maMuonSach;
        this.maSach = maSach;
        this.soLuong = soLuong;
    }

    public String getMaPhieuMuon() {
        return maPhieuMuon;
    }

    public void setMaPhieuMuon(String maPhieuMuon) {
        this.maPhieuMuon = maPhieuMuon;
    }

    public String getMaSinhVien() {
        return maSinhVien;
    }

    public void setMaSinhVien(String maSinhVien) {
        this.maSinhVien = maSinhVien;
    }

    public Date getNgayMuon() {
        return ngayMuon;
    }

    public void setNgayMuon(Date ngayMuon) {
        this.ngayMuon = ngayMuon;
    }

    public Date getNgayTra() {
        return ngayTra;
    }

    public void setNgayTra(Date ngayTra) {
        this.ngayTra = ngayTra;
    }

    public String getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public String getMaMuonSach() {
        return maMuonSach;
    }

    public void setMaMuonSach(String maMuonSach) {
        this.maMuonSach = maMuonSach;
    }

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
