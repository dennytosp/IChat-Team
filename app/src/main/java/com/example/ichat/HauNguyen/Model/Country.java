package com.example.ichat.HauNguyen.Model;

public class Country {
    private String NameCountry;
    private int flag;
    private String codeCountry;

    public Country(String nameCountry, int flag, String codeCountry) {
        NameCountry = nameCountry;
        this.flag = flag;
        this.codeCountry = codeCountry;
    }

    public Country() {
    }

    public String getNameCountry() {
        return NameCountry;
    }

    public void setNameCountry(String nameCountry) {
        NameCountry = nameCountry;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getCodeCountry() {
        return codeCountry;
    }

    public void setCodeCountry(String codeCountry) {
        this.codeCountry = codeCountry;
    }
}
