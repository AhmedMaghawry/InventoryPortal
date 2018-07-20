package com.ezzat.inventoryportal.Controller;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ezzat.inventoryportal.Model.Item;
import com.ezzat.inventoryportal.Model.Items;
import com.ezzat.inventoryportal.Model.User;
import com.ezzat.inventoryportal.Model.Users;
import com.ezzat.inventoryportal.R;
import com.ezzat.inventoryportal.View.PasswordActivity;
import com.ezzat.inventoryportal.View.ReturnActivity;
import com.ezzat.inventoryportal.View.camHomeActivity;

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
        TextView or = dialog.findViewById(R.id.or);
        or.setVisibility(View.VISIBLE);
        Button scan = dialog.findViewById(R.id.scan_it);
        scan.setVisibility(View.VISIBLE);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTheCam(context, id, items);
            }
        });
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

    public void showMyDialogDesc(Item item, final Context context) {
        // custom dialog
        if (item != null) {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_desc);
            dialog.setTitle("Description");
            TextView itemNum = dialog.findViewById(R.id.item);
            TextView quantity = dialog.findViewById(R.id.quan);
            TextView decrp = dialog.findViewById(R.id.desc);
            TextView bin = dialog.findViewById(R.id.bin);
            TextView cat = dialog.findViewById(R.id.cat);
            Button ok = dialog.findViewById(R.id.ok);

            itemNum.setText(item.getItemNumber());
            quantity.setText(item.getQuan());
            decrp.setText(item.getDesc());
            bin.setText(item.getBin());
            cat.setText(item.getCatalog());
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else {
            Toast.makeText(context, "Invalid Item", Toast.LENGTH_LONG);
        }
    }

    public void startTheCam(Context context, User user, Items items) {
        startAc(camHomeActivity.class, context, user, items);
    }

    private Class<?> mClss;

    public void startAc(Class<?> cls, Context context, User user, Items items){
            Intent intent = new Intent(context, cls);
            intent.putExtra("user", user);
            intent.putExtra("items", items);
            intent.putExtra("create", 2);
            context.startActivity(intent);
    }

}
