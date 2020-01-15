package com.example.myproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.database.Cursor;

public class VprofileActivity extends MainActivity implements OnClickListener{

    EditText profile;
    Button vprofile,home;
    SQLiteDatabase db;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile);

        profile=(EditText)findViewById(R.id.profile);
        vprofile=(Button)findViewById(R.id.vprofile);
        home=(Button)findViewById(R.id.home);

        vprofile.setOnClickListener(this);

        // Creating database and table
        db=openOrCreateDatabase("project", Context.MODE_PRIVATE, null);
    }

    public void onClick(View view)
    {
        if(view == vprofile)
        {
            Cursor c=db.rawQuery("SELECT * FROM user WHERE phone='"+profile.getText()+"'", null);
            Cursor d=db.rawQuery("SELECT * FROM enumber WHERE mobile='"+profile.getText()+"'", null);
            if(c.getCount()==0 || d.getCount()==0)
            {
                showMessage("Error", "No records found");
                return;
            }
            StringBuffer buffer=new StringBuffer();
            while(c.moveToNext() && d.moveToNext())
            {
                buffer.append("Name: "+c.getString(0)+"\n\n");
                buffer.append("Mobile: "+c.getString(1)+"\n\n");
                buffer.append("Emergency Number 1: "+d.getString(1)+"\n\n");
                buffer.append("Emergency Number 2: "+d.getString(2)+"\n\n");
                buffer.append("Emergency Number 3: "+d.getString(3)+"\n\n");
                buffer.append("Emergency Number 4: "+d.getString(4)+"\n\n");
            }
            showMessage("Your Profile", buffer.toString());
        }
        if(view == home)
        {
            final Context context = this;
            Intent intent = new Intent(context, HomeActivity.class);
            startActivity(intent);
        }
    }
    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void clearText()
    {
        profile.setText("");
        //Rollno.requestFocus();
    }
}
