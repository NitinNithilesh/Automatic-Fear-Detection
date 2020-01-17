package com.example.myproject;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;


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
        db.execSQL("CREATE TABLE IF NOT EXISTS user_log(phone VARCHAR);");
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
            if(phone.getText().toString().length()==10) {
                //SQLiteDatabase db=this.getReadableDatabase();
                Cursor c = db.rawQuery("SELECT * FROM user WHERE phone='" + phone.getText() + "'", null);
                if (c.moveToNext()) {
                    db.execSQL("INSERT INTO user_log VALUES('" + phone.getText() + "');");
                    Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                    //showMessage("Success", "Login Successful");
                    final Context context = this;
                    Intent intent = new Intent(context, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Mobile Number or Password is wrong. Please Check It", Toast.LENGTH_SHORT).show();
                    //showMessage("Error", "Mobile Number or Password is wrong. Please Check It");
                    return;
                }
            }
            else{
                Toast.makeText(this, "Please Enter a Valid Mobile Number", Toast.LENGTH_SHORT).show();
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
