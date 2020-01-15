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

public class EnumberActivity extends MainActivity implements OnClickListener {

    EditText num1,num2,num3,num4,rnum;
    Button enter,home;
    SQLiteDatabase db;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_enumber);

        rnum=(EditText)findViewById(R.id.rnum);
        num1=(EditText)findViewById(R.id.num1);
        num2=(EditText)findViewById(R.id.num2);
        num3=(EditText)findViewById(R.id.num3);
        num4=(EditText)findViewById(R.id.num4);
        enter=(Button)findViewById(R.id.enter);
        home=(Button)findViewById(R.id.home);

        enter.setOnClickListener(this);

        // Creating database and table
        db=openOrCreateDatabase("project", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS enumber(mobile VARCHAR,number_1 VARCHAR,number_2 VARCHAR,number_3 VARCHAR,number_4 VARCHAR);");
    }
    public void onClick(View view)
    {
        if(view == home)
        {
            final Context context = this;
            Intent intent = new Intent(context, HomeActivity.class);
            startActivity(intent);
        }
        if(view == enter)
        {
            if(rnum.getText().toString().trim().length()==0||
                    num1.getText().toString().trim().length()==0||
                    num2.getText().toString().trim().length()==0||
                    num3.getText().toString().trim().length()==0||
                    num4.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter all values");
                return;
            }
            db.execSQL("INSERT INTO enumber VALUES('"+rnum.getText()+"','"+num1.getText()+ "','"+num2.getText()+"','"+num3.getText()+"','"+num4.getText()+"');");
            showMessage("Success", "Emergency Numbers Added");
            clearText();
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
        rnum.setText("");
        num1.setText("");
        num2.setText("");
        num3.setText("");
        num4.setText("");
    }
}

