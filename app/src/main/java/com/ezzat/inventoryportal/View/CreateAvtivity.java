package com.ezzat.inventoryportal.View;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ezzat.inventoryportal.Model.CreateItem;
import com.ezzat.inventoryportal.Model.Item;
import com.ezzat.inventoryportal.Model.Items;
import com.ezzat.inventoryportal.Model.User;
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
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreateAvtivity extends AppCompatActivity {

    private EditText itemNum;
    private EditText itemQuan;
    private EditText companyName;
    private EditText desc;
    private EditText catalog;
    private Spinner line;
    private Spinner machine;
    private ImageButton wrong;
    private ImageButton right;
    private String[] lines;
    private String[] machines;
    private User user;
    private String qLine = "Which Line ?";
    private String qMachine = "Which Machine ?";
    private Button scanBt;
    private Items items;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_avtivity);
        user = (User) getIntent().getSerializableExtra("user");
        items = (Items) getIntent().getSerializableExtra("items");
        setupViews();
        getPaths();
        setupActions();
        setupToolbar();
        Item item = (Item) getIntent().getSerializableExtra("item");
        if (item != null) {
            itemNum.setText(item.getItemNumber());
            itemNum.setEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        goToHome();
    }

    private void setupActions() {
        //Lines
        ArrayAdapter<String> adapterLines = new ArrayAdapter<String>(CreateAvtivity.this,
                R.layout.spinner_layout,lines){
            @Override
            public boolean isEnabled(int position) {
                if (position == 0){
                    return false;
                } else
                    return true;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }
        };
        adapterLines.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        line.setAdapter(adapterLines);

        //Machines
        ArrayAdapter<String> adapterMachines = new ArrayAdapter<String>(CreateAvtivity.this,
                R.layout.spinner_layout,machines){
            @Override
            public boolean isEnabled(int position) {
                if (position == 0){
                    return false;
                } else
                    return true;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }
        };
        adapterMachines.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        machine.setAdapter(adapterMachines);

        //Wrong
        wrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHome();
            }
        });

        //Right
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itmID = itemNum.getText().toString();
                String quan = itemQuan.getText().toString();
                String lineNum = line.getSelectedItem().toString();
                String machNum = machine.getSelectedItem().toString();
                String compN = companyName.getText().toString();
                String de = desc.getText().toString();
                String cata = catalog.getText().toString();
                /*Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = df.format(c);*/
                if (items.getItem(itmID) == null) {
                    items.addItem(new Item(itmID, de, quan, lineNum, machNum, cata));
                    new uploadCreate().execute(itmID, de, quan, lineNum, machNum, cata, compN);
                } else {
                    Toast.makeText(getApplicationContext(), "There is the same item", Toast.LENGTH_LONG).show();
                }
            }
        });

        scanBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTheCam();
            }
        });
    }

    private void goToHome() {
        Intent intent = new Intent(CreateAvtivity.this, HomeActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("items", items);
        startActivity(intent);
        finish();
    }

    private void setupViews() {
        itemNum = findViewById(R.id.itm_num);
        itemQuan = findViewById(R.id.itm_qnt);
        line = findViewById(R.id.line);
        machine = findViewById(R.id.mach);
        wrong = findViewById(R.id.cross);
        right = findViewById(R.id.tick);
        scanBt = findViewById(R.id.scan_it);
        companyName = findViewById(R.id.cmp_name);
        catalog = findViewById(R.id.cat_num);
        desc = findViewById(R.id.dsc);
    }

    public void getPaths() {
        ArrayList<String> linesArr = items.getLines();
        linesArr.add(0, qLine);
        lines = new String[linesArr.size()];
        lines = linesArr.toArray(lines);
        ArrayList<String> machinesArr = items.getMachines();
        machinesArr.add(0, qMachine);
        machines = new String[machinesArr.size()];
        machines = machinesArr.toArray(machines);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHome();
            }
        });
        final ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void startTheCam() {
        startAc(camHomeActivity.class);
    }

    private Class<?> mClss;

    public void startAc(Class<?> cls){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Class<?> mClss = cls;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            Intent intent = new Intent(this, cls);
            intent.putExtra("user", user);
            intent.putExtra("items", items);
            intent.putExtra("create", 0);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }


    class uploadCreate extends AsyncTask<String, String, String> {
        AlertDialog alertDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(CreateAvtivity.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            pDialog = new ProgressDialog(CreateAvtivity.this);
            pDialog.setMessage("Loading Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            SharedPreferences pref = getApplication().getSharedPreferences("create", 0);
            Gson gson = new Gson();
            SharedPreferences.Editor editor = pref.edit();
            int counter = pref.getInt("createsize", 0);
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = df.format(c);
            String json = gson.toJson(new CreateItem(args[0],args[1], args[2], args[3], args[4], args[5], args[6], formattedDate));
            editor.putString((counter+1)+"", json);
            editor.putInt("createsize", counter+1);
            editor.commit(); // commit changes
            return null;
        }


        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            Toast.makeText(getApplicationContext(), "The item has been successfully created", Toast.LENGTH_LONG).show();
            goToHome();
        }
    }
}
