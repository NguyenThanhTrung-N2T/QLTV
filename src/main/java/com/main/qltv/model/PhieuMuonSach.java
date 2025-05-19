package model;

import java.time.LocalDate;

public class PhieuMuon {
    private String maPhieu, maDocGia, tenDocGia, maSach, tenSach, trangThai;
    private LocalDate ngayMuon, ngayTra;

    public PhieuMuon(String maPhieu, String maDocGia, String tenDocGia,
                     LocalDate ngayMuon, LocalDate ngayTra,
                     String maSach, String tenSach, String trangThai) {
        this.maPhieu = maPhieu;
        this.maDocGia = maDocGia;
        this.tenDocGia = tenDocGia;
        this.ngayMuon = ngayMuon;
        this.ngayTra = ngayTra;
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.trangThai = trangThai;
    }

    // Getters + Setters
    public String getMaPhieu() { return maPhieu; }
    public String getMaDocGia() { return maDocGia; }
    public String getTenDocGia() { return tenDocGia; }
    public LocalDate getNgayMuon() { return ngayMuon; }
    public LocalDate getNgayTra() { return ngayTra; }
    public String getMaSach() { return maSach; }
    public String getTenSach() { return tenSach; }
    public String getTrangThai() { return trangThai; }

    public void setMaPhieu(String maPhieu) { this.maPhieu = maPhieu; }
    public void setMaDocGia(String maDocGia) { this.maDocGia = maDocGia; }
    public void setTenDocGia(String tenDocGia) { this.tenDocGia = tenDocGia; }
    public void setNgayMuon(LocalDate ngayMuon) { this.ngayMuon = ngayMuon; }
    public void setNgayTra(LocalDate ngayTra) { this.ngayTra = ngayTra; }
    public void setMaSach(String maSach) { this.maSach = maSach; }
    public void setTenSach(String tenSach) { this.tenSach = tenSach; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
