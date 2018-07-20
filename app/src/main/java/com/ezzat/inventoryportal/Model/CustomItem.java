package com.ezzat.inventoryportal.Model;

public interface CustomItem {

    String itemNumber="", quan="", line="", mach="", Datey = "", desc = "", catalog = "", CompanyName = "", bin = "";

    public String getItemNumber();
    public String getDesc();
    public String getQuan();
    public String getLine();
    public String getMach();
    public String getCatalog();
    public String getCompany();
    public String getDatey();
    public String getBin();
    public String getType();
}
