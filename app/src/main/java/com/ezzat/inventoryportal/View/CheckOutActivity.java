package com.ezzat.inventoryportal.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ezzat.inventoryportal.R;

public class CheckOutActivity extends AppCompatActivity {

    private EditText itemNum;
    private EditText itemQuan;
    private Spinner line;
    private Spinner machine;
    private ImageButton wrong;
    private ImageButton right;
    private String[] lines;
    private String[] machines;
    private String idUser;
    private String idItem;
    private String qLine = "Which Line ?";
    private String qMachine = "Which Machine ?";
    private Button scanBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        idUser = getIntent().getStringExtra("id");
        idItem = getIntent().getStringExtra("item");
        getPaths();
        setupViews();
        setupActions();
        setupToolbar();
    }

    private void setupActions() {
        //Lines
        ArrayAdapter<String> adapterLines = new ArrayAdapter<String>(CheckOutActivity.this,
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
        ArrayAdapter<String> adapterMachines = new ArrayAdapter<String>(CheckOutActivity.this,
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
                //ToDo: Check if in table
                goToHome();
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
        Intent intent = new Intent(CheckOutActivity.this, HomeActivity.class);
        intent.putExtra("id", idUser);
        startActivity(intent);
        finish();
    }

    private void setupViews() {
        itemNum = findViewById(R.id.itm_num);
        if (idItem != null) {
            itemNum.setText(idItem);
            itemNum.setEnabled(false);
        }
        itemQuan = findViewById(R.id.itm_qnt);
        line = findViewById(R.id.line);
        machine = findViewById(R.id.mach);
        wrong = findViewById(R.id.cross);
        right = findViewById(R.id.tick);
        scanBt = findViewById(R.id.scan_it);
    }

    public void getPaths() {
        //TODO: Get the line names
        lines = new String[]{qLine,"Test","Test","Test","Test","Test","Test","Test","Test","Test"};
        machines = new String[]{qMachine,"Test","Test","Test","Test","Test","Test","Test","Test","Test"};
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void startTheCam() {
        startAc(camActivity.class);
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
}
