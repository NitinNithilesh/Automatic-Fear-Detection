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

public class HomeActivity extends MainActivity implements OnClickListener{

    Button emergency,view_pdata,view_profile,update_profile,update_enumber;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        emergency = (Button)findViewById(R.id.emergency);
        update_profile = (Button)findViewById(R.id.update_profile);
        view_profile = (Button)findViewById(R.id.view_profile);
        update_enumber = (Button)findViewById(R.id.update_enumber);
        view_pdata = (Button)findViewById(R.id.view_pdata);


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
        if(view == emergency)
        {
            final Context context = this;
            Intent intent = new Intent(context, MapActivity.class);
            startActivity(intent);
        }
    }
}