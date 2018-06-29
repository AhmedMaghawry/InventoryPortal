package com.ezzat.inventoryportal.View;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ezzat.inventoryportal.Controller.publicMethods;
import com.ezzat.inventoryportal.R;

import java.util.concurrent.Callable;

public class WelcomeActivity extends AppCompatActivity {

    private Button enter_id;
    private Button scan_id;
    private Context self;
    private Class<?> mClss;
    private publicMethods pub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        self = this;
        pub = new publicMethods();
        setupViews();
        setupActions();
    }

    private void setupActions() {

        enter_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = "Employee ID";
                String desc = "Enter Your ID : ";
                pub.showMyDialog(title, desc, self, new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return checkLogin();
                    }
                });
            }
        });

        scan_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTheCam();
            }
        });
    }

    private void setupViews() {

        enter_id = findViewById(R.id.enter_id_bt);
        scan_id = findViewById(R.id.scan_id_bt);

    }

    private Boolean checkLogin() {
        return pub.searchInXSL();
    }


    public void startTheCam() {
        startAc(camActivity.class);
    }

    public void startAc(Class<?> cls){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = cls;
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
