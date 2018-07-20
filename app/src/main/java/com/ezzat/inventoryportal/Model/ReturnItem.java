package com.ezzat.inventoryportal.Model;

import java.io.Serializable;

public class ReturnItem implements Serializable, CustomItem {

    private String itemNumber, quan, bin, Datey;

    public ReturnItem(String itemNumber, String quan, String bin, String Datey) {
        this.itemNumber = itemNumber;
        this.quan = quan;
        this.bin = bin;
        this.Datey = Datey;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    @Override
    public String getDesc() {
        return "";
    }

    public String getQuan() {
        return quan;
    }

    @Override
    public String getLine() {
        return "";
    }

    @Override
    public String getMach() {
        return "";
    }

    @Override
    public String getCatalog() {
        return "";
    }

    @Override
    public String getCompany() {
        return "";
    }

    public String getDatey() {
        return Datey;
    }

    public String getBin() {
        return bin;
    }

    @Override
    public String getType() {
        return "Return";
    }
}
