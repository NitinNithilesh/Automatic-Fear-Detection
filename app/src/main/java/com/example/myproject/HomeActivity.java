package com.example.myproject;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;

import java.util.ResourceBundle;

public class HomeActivity extends MainActivity implements OnClickListener{

    Button emergency,view_pdata,view_profile,update_profile,update_enumber,logout;
    SQLiteDatabase db;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        emergency = (Button)findViewById(R.id.emergency);
        update_profile = (Button)findViewById(R.id.update_profile);
        view_profile = (Button)findViewById(R.id.view_profile);
        update_enumber = (Button)findViewById(R.id.update_enumber);
        view_pdata = (Button)findViewById(R.id.view_pdata);
        logout = (Button)findViewById(R.id.logout);

        db=openOrCreateDatabase("project", Context.MODE_PRIVATE, null);

    }
    public void onClick(View view)
    {
        if(view == update_enumber)
        {
            final Context context = this;
            Intent intent = new Intent(context, EnumberActivity.class);
            startActivity(intent);
        }
        if(view == view_profile)
        {
            final Context context = this;
            Intent intent = new Intent(context, VprofileActivity.class);
            startActivity(intent);
        }
        if(view == update_profile)
        {
            final Context context = this;
            Intent intent = new Intent(context, UprofileActivity.class);
            startActivity(intent);
        }
        if(view == logout)
        {
            db.execSQL("DELETE FROM user_log");
            Toast.makeText(this, "Successfully Logged Out", Toast.LENGTH_SHORT).show();
            final Context context = this;
            Intent intent = new Intent(context, loginActivity.class);
            startActivity(intent);
        }
        if(view == emergency)
        {
            final Context context = this;
//            Intent intent = new Intent(context, MapActivity.class);
//            startActivity(intent);
            //final Context context = this;
            Intent intent = new Intent(context, MapActivity.class);
            //EditText phone = (EditText) findViewById(R.id.phone);
//            String phone=phone;
//            Bundle bundle = new Bundle();
//            bundle.putString("phone_number", phone);
//            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}