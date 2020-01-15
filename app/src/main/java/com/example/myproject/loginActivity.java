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


public class loginActivity extends MainActivity implements OnClickListener {

    EditText phone,pwd;
    Button login,signup;
    SQLiteDatabase db;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        phone = (EditText)findViewById(R.id.phone);
        pwd = (EditText)findViewById(R.id.pwd);
        login = (Button)findViewById(R.id.login);
        signup = (Button)findViewById(R.id.signup);

        login.setOnClickListener(this);

        db=openOrCreateDatabase("project", Context.MODE_PRIVATE, null);
    }

    public void onClick(View view)
    {
        if(view == login)
        {
            if(phone.getText().toString().trim().length()==0 || pwd.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter all values");
                return;
            }
            //SQLiteDatabase db=this.getReadableDatabase();
            Cursor c=db.rawQuery("SELECT * FROM user WHERE phone='"+phone.getText()+"'", null);
            if(c.moveToNext())
            {
                showMessage("Success", "Login Successful");
                final Context context = this;
                Intent intent = new Intent(context, HomeActivity.class);
                startActivity(intent);
            }
            else
            {
                showMessage("Error", "Mobile Number or Password is wrong. Please check it");
            }
        }
        if(view == signup)
        {
            final Context context = this;
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
        }
    }
    /*public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }*/
}
