package com.ezzat.inventoryportal.View;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.ezzat.inventoryportal.Controller.cameraClasses.MessageDialogFragment;
import com.ezzat.inventoryportal.Model.Items;
import com.ezzat.inventoryportal.Model.User;
import com.ezzat.inventoryportal.R;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class PasswordActivity extends AppCompatActivity implements MessageDialogFragment.MessageDialogListener {

    private EditText pswd;
    private Button btn;
    private User user;
    private Items items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (User) getIntent().getSerializableExtra("user");
        items = (Items) getIntent().getSerializableExtra("items");
        setContentView(R.layout.activity_password);
        pswd = findViewById(R.id.passwrd);
        btn = findViewById(R.id.enter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pswd.getText().toString().equals(user.getPin())) {
                    Intent intent = new Intent(PasswordActivity.this, HomeActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("items", items);
                    startActivity(intent);
                    finish();
                } else {
                    showMessageDialog("Wrong Password , Contact Shift supervisor");
                }
            }
        });
    }


    public void showMessageDialog(String message) {
        DialogFragment fragment = MessageDialogFragment.newInstance("Wrong", message, this);
        fragment.show(getSupportFragmentManager(), "wrong");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        closeDialog("Wrong");
    }

    public void closeDialog(String dialogName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragment fragment = (DialogFragment) fragmentManager.findFragmentByTag(dialogName);
        if(fragment != null) {
            fragment.dismiss();
        }
    }
}
