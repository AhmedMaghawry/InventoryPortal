package com.ezzat.inventoryportal.Model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.ezzat.inventoryportal.R;
import com.google.gson.Gson;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Items implements Serializable {

    private ArrayList<Item> items;


    public Items (Context context) {
        items = new ArrayList<>();
    }

    public void addItem(Item i) {
        items.add(i);
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public Item getItem (String num) {
        Log.i("didi", items.size()+"");
        for (Item item : items) {
            if (item == null)
                continue;
            Log.i("didi", "search now " + item.getItemNumber());
            Log.i("didi", "search me " + num);
            if (item.getItemNumber().equals(num))
                return item;
        }
        Log.i("didi", "NULLLLL");
        return null;
    }

    public boolean updateItemID(String id, String newId, Context context) {
        if (getItem(newId) == null) {
            Log.i("dodi", id + " IDDD");
            for (int i = 0; i < items.size(); i++) {
                Log.i("dodi", "search now " + items.get(i).getItemNumber());
                Log.i("dodi", "search me " + id);
                if (items.get(i).getItemNumber().equals(id)) {
                    Log.i("dodi", "Geet IT");
                    items.set(i, new Item(newId, items.get(i).getDesc(), items.get(i).getQuan(), items.get(i).getLine(), items.get(i).getMach(), items.get(i).getCatalog()));
                    break;
                }
            }
            return true;
        } else {
            //Toast.makeText(context, "There is the same id before", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void updateQuan(String id, String quan, Context context) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItemNumber() == id) {
                items.set(i, new Item(items.get(i).getItemNumber(), items.get(i).getDesc(), (Integer.parseInt(items.get(i).getQuan()) + Integer.parseInt(quan))+"",items.get(i).getLine(),items.get(i).getMach(),items.get(i).getCatalog()));
                break;
            }
        }
    }

    public void loadItems(Context context){
        SharedPreferences pref = context.getSharedPreferences("items", 0);
        Gson gson = new Gson();
        if (pref.getBoolean("first", true)) {
            //First Time to load the app
            int size = 0;
            SharedPreferences.Editor editor = pref.edit();
            Log.i("dodo", "afterRR");
            InputStream stream = context.getResources().openRawResource(R.raw.test1);
            try {
                XSSFWorkbook workbook = new XSSFWorkbook(stream);
                Log.i("dodo", "afterW");
                XSSFSheet sheet = workbook.getSheetAt(0);
                Log.i("dodo", "afterSh");
                int rowsCount = sheet.getPhysicalNumberOfRows();
                Log.i("dodo", "afterR "+rowsCount);
                FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
                Log.i("dodo", "afterFE");
                for (int r = 0; r<rowsCount; r++) {
                    if (r == 0)
                        continue;
                    size++;
                    Row row = sheet.getRow(r);
                    int cellsCount = row.getPhysicalNumberOfCells();
                    String num = null;
                    String des = null;
                    String qua = null;
                    String lineMach = null;
                    String cat = null;
                    for (int c = 0; c<cellsCount; c++) {
                        String value = getCellAsString(row, c, formulaEvaluator);
                        switch (c) {
                            case 0 :
                                Log.i("ddi", value.charAt(value.length() - 2)+"");
                                num = value.charAt(value.length() - 2) == '.'? value.substring(0, value.length() - 2) : value;
                                break;
                            case 1 :
                                des = value;
                                break;
                            case 2 :
                                qua = value.substring(0, value.length() - 2);
                                break;
                            case 3 :
                                lineMach = value;
                                break;
                            case 4 :
                                cat = value;
                                break;
                            default:
                        }
                    }
                    String line = getLine(lineMach);
                    String machine = getMachine(lineMach);
                    Item item = new Item(num, des, qua, line, machine, cat);
                    String json = gson.toJson(item);
                    editor.putString(r+"", json);
                    items.add(item);
                }
            } catch (Exception e) {
                /* proper exception handling to be here */
                Log.i("dodo",e.toString());
            }
            editor.putInt("size", size);
            editor.putBoolean("first", false);
            editor.commit(); // commit changes
        } else {
            int size = pref.getInt("size", 0);
            for (int i = 0; i < size; i++) {
                String json = pref.getString((i+1)+"", "");
                Item it = gson.fromJson(json, Item.class);
                items.add(it);
            }
        }
    }

    private String getLine(String lineMach) {
        if (lineMach == null)
            return "";
        if (lineMach.length() > 0 && lineMach.length() <= 3)
            return lineMach.substring(0,2);
        else if (lineMach.length() > 3)
            return lineMach;
        else
            return "";
    }

    private String getMachine(String lineMach) {
        if (lineMach == null)
            return "";
        if (lineMach.length() == 3)
            return lineMach.substring(2);
        else
            return "";
    }

    public void saveItems(Context context){
        SharedPreferences pref = context.getSharedPreferences("items", 0);
        Gson gson = new Gson();
        SharedPreferences.Editor editor = pref.edit();
        int counter = 1;
        for (Item i : items) {
            String json = gson.toJson(i);
            editor.putString(counter+"", json);
            counter++;
        }
        editor.putInt("size", items.size());
        editor.commit(); // commit changes
    }

    public void saveItemForCheckOut(Context context, Item item){
        SharedPreferences pref = context.getSharedPreferences("check", 0);
        Gson gson = new Gson();
        SharedPreferences.Editor editor = pref.edit();
        int counter = pref.getInt("checksize", 0);
        String json = gson.toJson(item);
        editor.putString((counter+1)+"", json);
        editor.putInt("checksize", counter+1);
        editor.commit(); // commit changes
    }

    public void generateXLS_and_send(Items items, Context context) {
        Workbook workbook = new XSSFWorkbook();
        String[] cols = {"ID", "Item#", "Quantity", "Line", "Machine", "Date"};
        Sheet sheet = workbook.createSheet("Check Out");
        Row head = sheet.createRow(0);
        for (int i = 0; i < cols.length; i++) {
            Cell cell = head.createCell(i);
            cell.setCellValue(cols[i]);
        }

        int rowNum = 1;
        for (Item ite : items.getItems()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(rowNum-1);
            row.createCell(1).setCellValue(ite.getItemNumber());
            row.createCell(2).setCellValue(ite.getQuan());
            row.createCell(3).setCellValue(ite.getLine());
            row.createCell(4).setCellValue(ite.getMach());
            row.createCell(5).setCellValue(ite.getCatalog());
        }

        String filename="report.xlsx";
        File filelocation = new File(context.getExternalCacheDir(), filename);
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(filelocation);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri path = Uri.fromFile(filelocation);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
// set the type to 'email'
        //vnd.android.cursor.dir/email
        emailIntent .setType("text/plain");
        String to[] = {"foxracer1869@gmail.com"};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
// the attachment
        emailIntent .putExtra(Intent.EXTRA_STREAM, path);
// the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Results");
        context.startActivity(Intent.createChooser(emailIntent , "Send email..."));
    }

    protected String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = ""+cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if(HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("dd/MM/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = ""+numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = ""+cellValue.getStringValue();
                    break;
                default:
                    value = "";
            }
        } catch (NullPointerException e) {
            /* proper error handling should be here */
            //Log.i("dodo","Ezzat "+e.toString());
            value = "";
        }
        return value;
    }

    public ArrayList<String> getLines() {
        Set<String> res = new HashSet<>();
        for (Item i : items) {
            if (i == null || i.getLine().equals(""))
                continue;
            res.add(i.getLine());
        }
        return new ArrayList<String>(res);
    }

    public ArrayList<String> getMachines() {
        Set<String> res = new HashSet<>();
        for (Item i : items) {
            if (i == null || i.getMach().equals(""))
                continue;
            res.add(i.getMach());
        }
        return new ArrayList<String>(res);
    }

}
