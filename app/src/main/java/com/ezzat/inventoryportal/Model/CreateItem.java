package com.ezzat.inventoryportal.Model;

import java.io.Serializable;

public class CreateItem implements Serializable, CustomItem {

    private String itemNumber, desc, quan, line, mach, catalog, CompanyName, Datey;

    public CreateItem(String itemNumber, String desc, String quan, String line, String mach, String catalog, String CompanyName, String Datey) {
        this.itemNumber = itemNumber;
        this.desc = desc;
        this.quan = quan;
        this.line = line;
        this.mach = mach;
        this.catalog = catalog;
        this.CompanyName = CompanyName;
        this.Datey = Datey;
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
    public String getCompany() {
        return CompanyName;
    }
    public String getDatey() {
        return Datey;
    }

    public String getBin() {
        return line+mach;
    }

    @Override
    public String getType() {
        return "Create";
    }
}
