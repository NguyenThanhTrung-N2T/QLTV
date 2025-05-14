package com.main.qltv.model;

public class NhaXuatBan {
    private String maNXB;
    private String tenNXB;

    public String getMaNXB() {
        return maNXB;
    }

    public NhaXuatBan(String maNXB, String tenNXB) {
        this.maNXB = maNXB;
        this.tenNXB = tenNXB;
    }

    public void setMaNXB(String maNXB) {
        this.maNXB = maNXB;
    }

    public void setTenNXB(String tenNXB) {
        this.tenNXB = tenNXB;
    }

    public String getTenNXB() {
        return tenNXB;
    }
}
