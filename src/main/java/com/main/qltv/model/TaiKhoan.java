package com.main.qltv.model;

public class TaiKhoan {
    private String maTK;
    private String tenDangNhap;
    private String matKhauTK;
    private String loaiTK;
    private String MSSV;

    public String getMaTK() { return maTK; }
    public String getTenDangNhap() { return tenDangNhap; }
    public String getMatKhauTK() { return matKhauTK; }
    public String getLoaiTK() { return loaiTK; }
    public String getMSSV() { return MSSV; }

    public void setMaTK(String maTK) {
        this.maTK = maTK;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public void setMatKhauTK(String matKhauTK) {
        this.matKhauTK = matKhauTK;
    }

    public void setLoaiTK(String loaiTK) {
        this.loaiTK = loaiTK;
    }

    public void setMSSV(String MSSV) {
        this.MSSV = MSSV;
    }
}
