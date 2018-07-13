package com.ezzat.inventoryportal.Controller;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ezzat.inventoryportal.Model.Item;
import com.ezzat.inventoryportal.Model.Items;
import com.ezzat.inventoryportal.Model.User;
import com.ezzat.inventoryportal.Model.Users;
import com.ezzat.inventoryportal.R;
import com.ezzat.inventoryportal.View.HomeActivity;
import com.ezzat.inventoryportal.View.PasswordActivity;
import com.ezzat.inventoryportal.View.ReturnActivity;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class publicMethods {

    String val = "";

    public void showMyDialog(String title, String desc, final Context context, final Users users, final Items items) {
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle(title);

        // set the custom dialog components - text, button
        TextView text = dialog.findViewById(R.id.desc_tv);
        text.setText(desc);
        final EditText editText = dialog.findViewById(R.id.input_et);
        editText.setHint(title + " ...");

        Button dialogCan = dialog.findViewById(R.id.cancel);
        // if button is clicked, close the custom dialog
        dialogCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button dialogOk = dialog.findViewById(R.id.ok);
        // if button is clicked
        dialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    val = editText.getText().toString();
                    Boolean b = users.checkUser(val);
                    if (b) {
                        dialog.dismiss();
                        Intent intent = new Intent(context, PasswordActivity.class);
                        intent.putExtra("user", users.getUser(val));
                        intent.putExtra("items", items);
                        context.startActivity(intent);
                    } else {
                        TextView wro = dialog.findViewById(R.id.wrong);
                        editText.clearComposingText();
                        wro.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.show();
    }

    public void showMyDialogReturn(String title, String desc, final Context context, final Items items, final User id) {
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle(title);

        // set the custom dialog components - text, button
        TextView text = dialog.findViewById(R.id.desc_tv);
        text.setText(desc);
        final EditText editText = dialog.findViewById(R.id.input_et);
        editText.setHint(title + " ...");

        Button dialogCan = dialog.findViewById(R.id.cancel);
        // if button is clicked, close the custom dialog
        dialogCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button dialogOk = dialog.findViewById(R.id.ok);
        // if button is clicked
        dialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    val = editText.getText().toString();
                Item b = items.getItem(val);
                if (b != null) {
                        Log.i("didi", "Not Null");
                        dialog.dismiss();
                        Intent intent = new Intent(context, ReturnActivity.class);
                        intent.putExtra("user", id);
                        intent.putExtra("items", items);
                        intent.putExtra("item", b);
                        context.startActivity(intent);
                    } else {
                        TextView wro = dialog.findViewById(R.id.wrong);
                        editText.clearComposingText();
                        wro.setVisibility(View.VISIBLE);
                    }
            }
        });

        dialog.show();
    }

}
