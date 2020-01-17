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
import android.widget.Toast;

public class UprofileActivity  extends MainActivity implements OnClickListener{

    EditText num,name,enum1,enum2,enum3,enum4;
    Button update,home;
    SQLiteDatabase db;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);

        num=(EditText)findViewById(R.id.num);
        name=(EditText)findViewById(R.id.name);
        enum1=(EditText)findViewById(R.id.enum1);
        enum2=(EditText)findViewById(R.id.enum2);
        enum3=(EditText)findViewById(R.id.enum3);
        enum4=(EditText)findViewById(R.id.enum4);
        update=(Button)findViewById(R.id.update);
        home=(Button)findViewById(R.id.home);

        update.setOnClickListener(this);

        // Creating database and table
        db=openOrCreateDatabase("project", Context.MODE_PRIVATE, null);
    }

    public void onClick(View view)
    {
        if(view == update)
        {
            /*if(num.getText().toString().trim().length() !=0 &&
                    (name.getText().toString().trim().length()==0||
                    enum1.getText().toString().trim().length()==0||
                            enum2.getText().toString().trim().length()==0||
                            enum3.getText().toString().trim().length()==0||
                            enum4.getText().toString().trim().length()==0))
            {
                showMessage("Error", "Please Enter all the Details");
                return;
            }*/
            if(num.getText().toString().trim().length()==10) {
                if (num.getText().toString().trim().length() != 0 && name.getText().toString().trim().length() != 0) {
                    Cursor c = db.rawQuery("SELECT * FROM user WHERE phone='" + num.getText() + "'", null);
                    if (c.moveToFirst()) {
                        db.execSQL("UPDATE user SET username='" + name.getText() + "' WHERE phone='" + num.getText() + "'");
                        Toast.makeText(this, "Name Updated", Toast.LENGTH_SHORT).show();
                        //showMessage("Success", "Name Updated");
                    }
                }
                if (num.getText().toString().trim().length() != 0 && enum1.getText().toString().trim().length() != 0 && enum1.getText().toString().trim().length()==10) {
                    Cursor c = db.rawQuery("SELECT * FROM enumber WHERE mobile='" + num.getText() + "'", null);
                    if (c.moveToFirst()) {
                        db.execSQL("UPDATE enumber SET number_1='" + enum1.getText() + "' WHERE mobile='" + num.getText() + "'");
                        Toast.makeText(this, "Emergency Number 1 Updated", Toast.LENGTH_SHORT).show();
                        //showMessage("Success", "Emergency Number 1 Updated");
                    }
                }
                else{
                    Toast.makeText(this, "Please Enter a Valid Emergency Number", Toast.LENGTH_SHORT).show();
                }
                if (num.getText().toString().trim().length() != 0 && enum2.getText().toString().trim().length() != 0 && enum2.getText().toString().trim().length()==10) {
                    Cursor c = db.rawQuery("SELECT * FROM enumber WHERE mobile='" + num.getText() + "'", null);
                    if (c.moveToFirst()) {
                        db.execSQL("UPDATE enumber SET number_2='" + enum2.getText() + "' WHERE mobile='" + num.getText() + "'");
                        Toast.makeText(this, "Emergency Number 2 Updated", Toast.LENGTH_SHORT).show();
                        //showMessage("Success", "Emergency Number 2 Updated");
                    }
                }
                else{
                    Toast.makeText(this, "Please Enter a Valid Emergency Number", Toast.LENGTH_SHORT).show();
                }
                if (num.getText().toString().trim().length() != 0 && enum3.getText().toString().trim().length() != 0 && enum3.getText().toString().trim().length()==10) {
                    Cursor c = db.rawQuery("SELECT * FROM enumber WHERE mobile='" + num.getText() + "'", null);
                    if (c.moveToFirst()) {
                        db.execSQL("UPDATE enumber SET number_3='" + enum3.getText() + "' WHERE mobile='" + num.getText() + "'");
                        Toast.makeText(this, "Emergency Number 3 Updated", Toast.LENGTH_SHORT).show();
                        //showMessage("Success", "Emergency Number 3 Updated");
                    }
                }
                else{
                    Toast.makeText(this, "Please Enter a Valid Emergency Number", Toast.LENGTH_SHORT).show();
                }
                if (num.getText().toString().trim().length() != 0 && enum4.getText().toString().trim().length() != 0 && enum4.getText().toString().trim().length()==10) {
                    Cursor c = db.rawQuery("SELECT * FROM enumber WHERE mobile='" + num.getText() + "'", null);
                    if (c.moveToFirst()) {
                        db.execSQL("UPDATE enumber SET number_4='" + enum4.getText() + "' WHERE mobile='" + num.getText() + "'");
                        Toast.makeText(this, "Emergency Number 4 Updated", Toast.LENGTH_SHORT).show();
                        //showMessage("Success", "Emergency Number 4 Updated");
                    }
                }
                else{
                    Toast.makeText(this, "Please Enter a Valid Emergency Number", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "Please Enter a Valid Mobile Number", Toast.LENGTH_SHORT).show();
            }
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
}
