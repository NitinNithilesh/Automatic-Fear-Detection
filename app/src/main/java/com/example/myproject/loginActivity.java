package com.example.myproject;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;

import java.util.Random;


public class loginActivity extends MainActivity implements OnClickListener {

    EditText phone,pwd,edit_otp;
    Button login,signup,otp;
    SQLiteDatabase db;
    String otp_generated;
    Integer otp_generated_parsed,otp_entered;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        edit_otp = (EditText) findViewById(R.id.edit_otp);
        phone = (EditText)findViewById(R.id.phone);
        pwd = (EditText)findViewById(R.id.pwd);
        login = (Button)findViewById(R.id.login);
        signup = (Button)findViewById(R.id.signup);
        otp = (Button) findViewById(R.id.otp);

        login.setOnClickListener(this);

        db=openOrCreateDatabase("project", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS user_log(phone VARCHAR);");
    }

    public void onClick(View view)
    {
        if(view == otp){
            Random rand = new Random();
            otp_generated = String.format("%04d", rand.nextInt(10000));
            otp_generated_parsed = Integer.parseInt(otp_generated);

            // DB Operations
            db=openOrCreateDatabase("project", Context.MODE_PRIVATE, null);
            Cursor g=db.rawQuery("SELECT * FROM user",null);
            if(g.moveToNext()) {
                String o = phone.getText().toString();
                //String user_log = g.getString(1);
                //showMessage("success", user_log);

                Cursor m = db.rawQuery("SELECT phone FROM user WHERE phone='" + o + "'", null);
                if (m.moveToNext()) {
                    String num1 = m.getString(m.getColumnIndex("phone"));
                    SmsManager sm = SmsManager.getDefault();
                    //String msg = textView.getText().toString();
                    String msg = "Your OTP is " + otp_generated + ". Please do not share this message with anyone." +"\n" + "\n" + "-- Technology is here to save you";
                    sm.sendTextMessage(num1, null, msg, null, null);
                }
            }
        }
        if(view == login)
        {
            if(phone.getText().toString().trim().length()==0 || pwd.getText().toString().trim().length()==0 || edit_otp.getText().toString().trim().length()==0) {
                Toast.makeText(this, "Please Enter all the Values", Toast.LENGTH_SHORT).show();
                //showMessage("Error", "Please enter all values");
                return;
            }
            if(phone.getText().toString().length()==10) {
                //SQLiteDatabase db=this.getReadableDatabase();
                Cursor c = db.rawQuery("SELECT * FROM user WHERE phone='" + phone.getText() + "' AND password='"+pwd.getText()+"'", null);
                String p = edit_otp.getText().toString();
                otp_entered = Integer.parseInt(p);
                if (c.moveToNext()) {
                    System.out.println("OTP GENERATED = "+otp_generated_parsed);
                    System.out.println("OTP ENTERED = "+otp_entered);
                    if (otp_entered.equals(otp_generated_parsed)) {
                        db.execSQL("INSERT INTO user_log VALUES('" + phone.getText() + "');");
                        Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                        //showMessage("Success", "Login Successful");

                        final Context context = this;
                        Intent intent = new Intent(context, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Please enter a correct OTP", Toast.LENGTH_SHORT).show();
                        //showMessage("Error", "Mobile Number or Password is wrong. Please Check It");
                        return;
                    }
                } else {
                    Toast.makeText(this, "Please check the Mobile Number or the Password you entered", Toast.LENGTH_SHORT).show();
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
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Toast.makeText(loginActivity.this,"Sorry You Can't Go Back. Please Login to Continue",Toast.LENGTH_LONG).show();
        return;
    }
}
