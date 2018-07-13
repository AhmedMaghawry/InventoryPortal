package com.ezzat.inventoryportal.Model;

import java.io.Serializable;

public class Item implements Serializable {

    private String itemNumber, desc, quan, line, mach, catalog;

    public Item(String itemNumber, String desc, String quan, String line, String mach, String catalog) {
        this.itemNumber = itemNumber;
        this.desc = desc;
        this.quan = quan;
        this.line = line;
        this.mach = mach;
        this.catalog = catalog;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public String getDesc() {
        return desc;
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

    public String getCatalog() {
        return catalog;
    }

    public String getBin() {
        return line+mach;
    }
}
