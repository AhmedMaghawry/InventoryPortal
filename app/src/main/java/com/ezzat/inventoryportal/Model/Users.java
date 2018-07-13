package com.ezzat.inventoryportal.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ezzat.inventoryportal.R;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Users implements Serializable {

    private ArrayList<User> users;

    public Users(Context context) {
        users = new ArrayList<>();
    }

    public void addUser(User i) {
        users.add(i);
    }

    public boolean checkUser(String val) {
        for (User user : users) {
            if (user.getId().equals(val) || user.getName().equals(val))
                return true;
        }
        return false;
    }

    public User getUser(String val) {
        for (User user : users) {
            if (user.getId().equals(val) || user.getName().equals(val))
                return user;
        }
        return null;
    }

    public void loadUsers(Context context){
        SharedPreferences pref = context.getSharedPreferences("users", 0);
        if (pref.getBoolean("first", true)) {
            //First Time to load the app
            int size = 0;
            SharedPreferences.Editor editor = pref.edit();
            Log.i("dodo", "afterRR");
            InputStream stream = context.getResources().openRawResource(R.raw.test1);
            try {
                XSSFWorkbook workbook = new XSSFWorkbook(stream);
                Log.i("dodo", "afterW");
                XSSFSheet sheet = workbook.getSheetAt(1);
                Log.i("dodo", "afterSh");
                int rowsCount = sheet.getPhysicalNumberOfRows();
                Log.i("dodo", "afterR " + rowsCount);
                FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
                Log.i("dodo", "afterFE");
                for (int r = 0; r < rowsCount; r++) {
                    if (r == 0)
                        continue;
                    size++;
                    Row row = sheet.getRow(r);
                    int cellsCount = row.getPhysicalNumberOfCells();
                    String name = null;
                    String pin = null;
                    String id = null;
                    for (int c = 0; c < cellsCount; c++) {
                        String value = getCellAsString(row, c, formulaEvaluator);
                        switch (c) {
                            case 0:
                                name = value;
                                break;
                            case 1:
                                pin = value.substring(0, value.length() - 2);
                                break;
                            case 2:
                                id = value.substring(0, value.length() - 2);
                                break;
                            default:
                        }
                    }
                    User user = new User(name, pin, id);
                    Set<String> ro = new HashSet<>(Arrays.asList(name, pin, id));
                    editor.putStringSet(r+"", ro);
                    users.add(user);
                }
            } catch (Exception e){
                Log.i("dodo", e.getMessage());
            }
            editor.putInt("size", size);
            editor.putBoolean("first", false);
            editor.commit(); // commit changes
        } else {
            int size = pref.getInt("size", 0);
            for (int i = 0; i < size; i++) {
                Set<String> s = pref.getStringSet((i+1)+"", null);
                ArrayList<String> arrayList = new ArrayList<>(s);
                User u = new User(arrayList.get(0), arrayList.get(1), arrayList.get(2));
                users.add(u);
            }
        }
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
}
