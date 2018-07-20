package com.ezzat.inventoryportal.Model;

import java.io.Serializable;

public class CheckItem implements Serializable, CustomItem {

    private String itemNumber, quan, line, mach, Datey;

    public CheckItem(String itemNumber, String quan, String line, String mach, String Datey) {
        this.itemNumber = itemNumber;
        this.quan = quan;
        this.line = line;
        this.mach = mach;
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

    public String getLine() {
        return line;
    }

    public String getMach() {
        return mach;
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

    @Override
    public String getBin() {
        return "";
    }

    @Override
    public String getType() {
        return "Checkout";
    }
}
