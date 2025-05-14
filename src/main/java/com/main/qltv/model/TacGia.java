package com.main.qltv.model;

public class TacGia {
    private String maTacGia;
    private String tenTacGia;

    public String getMaTacGia() {
        return maTacGia;
    }

    public String getTenTacGia() {
        return tenTacGia;
    }

    public TacGia(String maTacGia, String tenTacGia) {
        this.maTacGia = maTacGia;
        this.tenTacGia = tenTacGia;
    }

    public void setMaTacGia(String maTacGia) {
        this.maTacGia = maTacGia;
    }

    public void setTenTacGia(String tenTacGia) {
        this.tenTacGia = tenTacGia;
    }
}
