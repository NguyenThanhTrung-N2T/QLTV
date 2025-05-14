package com.main.qltv.model;

import java.sql.Date;

public class SinhVien {
    private String MSSV;
    private String tenSinhVien;
    private String gioiTinh;
    private Date ngaySinh;
    private String diaChi;
    private String email;
    private String SDT;

    public void setMSSV(String MSSV) {
        this.MSSV = MSSV;
    }

    public void setTenSinhVien(String tenSinhVien) {
        this.tenSinhVien = tenSinhVien;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }


    public String getMSSV() { return MSSV; }
    public String getTenSinhVien() { return tenSinhVien; }
    public String getGioiTinh() { return gioiTinh; }
    public Date getNgaySinh() { return ngaySinh; }
    public String getDiaChi() { return diaChi; }
    public String getEmail() { return email; }
    public String getSDT() { return SDT; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        SinhVien sv = (SinhVien) obj;

        return MSSV.equals(sv.MSSV) &&
                tenSinhVien.equals(sv.tenSinhVien) &&
                gioiTinh.equals(sv.gioiTinh) &&
                ngaySinh.equals(sv.ngaySinh) &&
                diaChi.equals(sv.diaChi) &&
                email.equals(sv.email) &&
                SDT.equals(sv.SDT);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(MSSV, tenSinhVien, gioiTinh, ngaySinh, diaChi, email, SDT);
    }
}
