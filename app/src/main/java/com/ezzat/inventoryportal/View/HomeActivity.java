package com.ezzat.inventoryportal.View;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.ezzat.inventoryportal.BuildConfig;
import com.ezzat.inventoryportal.Controller.publicMethods;
import com.ezzat.inventoryportal.Model.CheckItem;
import com.ezzat.inventoryportal.Model.CreateItem;
import com.ezzat.inventoryportal.Model.Item;
import com.ezzat.inventoryportal.Model.Items;
import com.ezzat.inventoryportal.Model.ReturnItem;
import com.ezzat.inventoryportal.Model.User;
import com.ezzat.inventoryportal.R;
import com.google.gson.Gson;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class HomeActivity extends AppCompatActivity {

    private Button checkOut;
    private Button ret;
    private Button create;
    private Items items;
    private User user;
    private Context self;
    private publicMethods pub;
    private Button sendReport;
    private ProgressDialog pDialog;
    private FloatingActionButton floatingActionButton;
    private SearchView searchView;
    private TextView balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        pub = new publicMethods();
        setContentView(R.layout.activity_home);
        items = (Items) getIntent().getSerializableExtra("items");
        user = (User) getIntent().getSerializableExtra("user");
        checkOut = findViewById(R.id.ch_o);
        ret = findViewById(R.id.returnIt);
        create = findViewById(R.id.create);
        sendReport = findViewById(R.id.report);
        floatingActionButton = findViewById(R.id.floatB);
        searchView = findViewById(R.id.search);
        balance = findViewById(R.id.balance);
        setupAnimation();
        setupToolbar();
        setupActions();
    }

    public ArrayList<CheckItem> loadCheckouts() {
        ArrayList<CheckItem> res = new ArrayList<>();
        SharedPreferences pref = getApplication().getSharedPreferences("check", 0);
        Gson gson = new Gson();
        int size = pref.getInt("checksize", 0);
        for (int i = 0; i < size; i++) {
            String json = pref.getString((i+1)+"", "");
            CheckItem it = gson.fromJson(json, CheckItem.class);
            res.add(it);
        }
        return res;
    }

    public ArrayList<CreateItem> loadCreate() {
        ArrayList<CreateItem> res = new ArrayList<>();
        SharedPreferences pref = getApplication().getSharedPreferences("create", 0);
        Gson gson = new Gson();
        int size = pref.getInt("createsize", 0);
        for (int i = 0; i < size; i++) {
            String json = pref.getString((i+1)+"", "");
            CreateItem it = gson.fromJson(json, CreateItem.class);
            res.add(it);
        }
        return res;
    }

    public ArrayList<ReturnItem> loadReturn() {
        ArrayList<ReturnItem> res = new ArrayList<>();
        SharedPreferences pref = getApplication().getSharedPreferences("return", 0);
        Gson gson = new Gson();
        int size = pref.getInt("returnsize", 0);
        for (int i = 0; i < size; i++) {
            String json = pref.getString((i+1)+"", "");
            ReturnItem it = gson.fromJson(json, ReturnItem.class);
            res.add(it);
        }
        return res;
    }

    private void setupActions() {
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTo(CheckOutActivity.class);
            }
        });

        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = "Search Ite,";
                String desc = "Enter Item ID : ";
                pub.showMyDialogReturn(title, desc, self, items, user);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTo(CreateAvtivity.class);
            }
        });
        try {
            sendReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new sendReportForAdmin().execute();
                }
            });
        } catch (Exception io){
            Toast.makeText(this, io.getMessage(), Toast.LENGTH_LONG).show();
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTo(CartActivity.class);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                pub.showMyDialogDesc(items.getItem(query), self);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void goTo(Class activityClass) {
        Intent intent = new Intent(HomeActivity.this, activityClass);
        intent.putExtra("user", user);
        intent.putExtra("items", items);
        startActivity(intent);
    }

    private void setupAnimation() {

        checkOut.setTranslationX(-1000);
        ret.setTranslationX(-1000);
        create.setTranslationX(-1000);

        checkOut.animate()
                .translationX(0)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ret.animate()
                        .translationX(0)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                create.animate()
                                        .translationX(0)
                                        .setDuration(300);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        })
                        .setDuration(300);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .setDuration(300);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem;
        menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0, "LogOut");
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_auto_focus:
                Intent intent = new Intent(HomeActivity.this, WelcomeActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class sendReportForAdmin extends AsyncTask<String, String, String> {
        AlertDialog alertDialog;

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage("Loading Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... strings) {
            ArrayList<CheckItem> iteCheck = loadCheckouts();
            ArrayList<CreateItem> iteCreate = loadCreate();
            ArrayList<ReturnItem> iteReturn = loadReturn();

            Workbook workbook = new XSSFWorkbook();
            String[] colsCheck = {"ID", "Item#", "Quantity", "Line", "Machine", "Date"};
            String[] colsCreate = {"ID", "Item#","Description", "Quantity", "Line", "Machine", "Catalog","Company", "Date"};
            String[] colsReturn = {"ID", "Item#", "Quantity", "Bin", "Date"};
            Sheet sheet = workbook.createSheet("Check Out");
            Row head = sheet.createRow(0);
            for (int i = 0; i < colsCheck.length; i++) {
                Cell cell = head.createCell(i);
                cell.setCellValue(colsCheck[i]);
            }

            int rowNum = 1;
            for (CheckItem ite : iteCheck) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rowNum-1);
                row.createCell(1).setCellValue(ite.getItemNumber());
                row.createCell(2).setCellValue(ite.getQuan());
                row.createCell(3).setCellValue(ite.getLine());
                row.createCell(4).setCellValue(ite.getMach());
                row.createCell(5).setCellValue(ite.getDatey());
            }

            Sheet sheet2 = workbook.createSheet("Create");
            Row head2 = sheet2.createRow(0);
            for (int i = 0; i < colsCreate.length; i++) {
                Cell cell = head2.createCell(i);
                cell.setCellValue(colsCreate[i]);
            }

            int rowNum2 = 1;
            for (CreateItem ite : iteCreate) {
                Row row = sheet2.createRow(rowNum2++);
                row.createCell(0).setCellValue(rowNum2-1);
                row.createCell(1).setCellValue(ite.getItemNumber());
                row.createCell(2).setCellValue(ite.getDesc());
                row.createCell(3).setCellValue(ite.getQuan());
                row.createCell(4).setCellValue(ite.getLine());
                row.createCell(5).setCellValue(ite.getMach());
                row.createCell(6).setCellValue(ite.getCatalog());
                row.createCell(7).setCellValue(ite.getCompany());
                row.createCell(8).setCellValue(ite.getDatey());
            }

            Sheet sheet3 = workbook.createSheet("Return");
            Row head3 = sheet3.createRow(0);
            for (int i = 0; i < colsReturn.length; i++) {
                Cell cell = head3.createCell(i);
                cell.setCellValue(colsReturn[i]);
            }

            int rowNum3 = 1;
            for (ReturnItem ite : iteReturn) {
                Row row = sheet3.createRow(rowNum3++);
                row.createCell(0).setCellValue(rowNum3-1);
                row.createCell(1).setCellValue(ite.getItemNumber());
                row.createCell(2).setCellValue(ite.getQuan());
                row.createCell(3).setCellValue(ite.getBin());
                row.createCell(4).setCellValue(ite.getDatey());
            }

            String filename="report.xlsx";
            File filelocation = new File(getExternalCacheDir(), filename);
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
            Uri path = FileProvider.getUriForFile(HomeActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    filelocation);
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            // set the type to 'email'
            //vnd.android.cursor.dir/email
            emailIntent .setType("message/rfc822");
            String to[] = {"trouch@titosvodka.com"};
            emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
            // the attachment
            emailIntent .putExtra(Intent.EXTRA_STREAM, path);
            // the mail subject
            emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Report");
            startActivityForResult(Intent.createChooser(emailIntent , "Send email..."),1);
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            //removeCurrentCheckouts();
            pDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getApplicationContext(), "The report has been successfully sent", Toast.LENGTH_LONG).show();
    }

    private void removeCurrentCheckouts() {
        SharedPreferences prefCheck = getSharedPreferences("check", 0);
        SharedPreferences.Editor editorCheck = prefCheck.edit();
        editorCheck.clear();
        editorCheck.commit(); // commit changes
        SharedPreferences prefCreate = getSharedPreferences("create", 0);
        SharedPreferences.Editor editorCreate = prefCreate.edit();
        editorCreate.clear();
        editorCreate.commit();
        SharedPreferences prefReturn = getSharedPreferences("return", 0);
        SharedPreferences.Editor editorReturn = prefReturn.edit();
        editorReturn.clear();
        editorReturn.commit();
    }
}
