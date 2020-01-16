package com.example.myproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.SmsManager;
import android.widget.TextView;

public class MapActivity extends AppCompatActivity{

    String num1,num2,num3,num4,user_log;

    private static final int REQUEST_LOCATION=1;

    private TextView textView;
    LocationManager locationManager;
    String latitude,longitude;
    SQLiteDatabase db;
    //private LocationManager locationManager;
    //private boolean GpsStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        ActivityCompat.requestPermissions(this, new     String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        textView = (TextView) findViewById(R.id.id_textview);

        db=openOrCreateDatabase("project", Context.MODE_PRIVATE, null);

        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            OnGPS();
        }
        else
        {
            getLocation();
        }
        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        /*if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;

         //   && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        }*/
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    Activity#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for Activity#requestPermissions for more details.
//
//            return;
//        }


       // locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        //assert locationManager != null;
        //GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

//            Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
//
//            onLocationChanged(location);

    }

    private void getLocation() {



        if(ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String []
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else{
            db=openOrCreateDatabase("project", Context.MODE_PRIVATE, null);

            Cursor g=db.rawQuery("SELECT * FROM user_log",null);
            if(g.moveToNext()) {
                String user_log = g.getString(0);
                //showMessage("success", user_log);

                Cursor c = db.rawQuery("SELECT number_1 FROM enumber WHERE mobile='" + user_log + "'", null);
                //Cursor c = db.rawQuery("SELECT number_1 FROM enumber WHERE mobile=9487813680", null);
                Cursor d = db.rawQuery("SELECT number_2 FROM enumber WHERE mobile='" + user_log + "'", null);
                Cursor e = db.rawQuery("SELECT number_3 FROM enumber WHERE mobile='" + user_log + "'", null);
                Cursor f = db.rawQuery("SELECT number_4 FROM enumber WHERE mobile='" + user_log + "'", null);

                if (c.moveToNext() && d.moveToNext() && e.moveToNext() && f.moveToNext()) {
                    String num1 = c.getString(c.getColumnIndex("number_1"));
                    String num2 = d.getString(d.getColumnIndex("number_2"));
                    String num3 = e.getString(e.getColumnIndex("number_3"));
                    String num4 = f.getString(f.getColumnIndex("number_4"));


                    Location LocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Location LocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    Location LocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

                    if (LocationGps != null) {
                        double lat = LocationGps.getLatitude();
                        double longi = LocationGps.getLongitude();

                        latitude = String.valueOf(lat);
                        longitude = String.valueOf(longi);

                        textView.setText("Your Location : " + "\n" + "Latitude : " + latitude + "\n" + "Longitude" + longitude);
                        SmsManager sm = SmsManager.getDefault();
                        String msg = textView.getText().toString();
                        sm.sendTextMessage(num1, null, msg, null, null);
                        sm.sendTextMessage(num2, null, msg, null, null);
                        sm.sendTextMessage(num3, null, msg, null, null);
                        sm.sendTextMessage(num4, null, msg, null, null);
                    } else if (LocationNetwork != null) {
                        double lat = LocationNetwork.getLatitude();
                        double longi = LocationNetwork.getLongitude();

                        latitude = String.valueOf(lat);
                        longitude = String.valueOf(longi);

                        textView.setText("Your Location : " + "\n" + "Latitude : " + latitude + "\n" + "Longitude" + longitude);
                        SmsManager sm = SmsManager.getDefault();
                        String msg = textView.getText().toString();
                        sm.sendTextMessage(num1, null, msg, null, null);
                        sm.sendTextMessage(num2, null, msg, null, null);
                        sm.sendTextMessage(num3, null, msg, null, null);
                        sm.sendTextMessage(num4, null, msg, null, null);
                    } else if (LocationPassive != null) {
                        double lat = LocationPassive.getLatitude();
                        double longi = LocationPassive.getLongitude();

                        latitude = String.valueOf(lat);
                        longitude = String.valueOf(longi);

                        textView.setText("Your Location : " + "\n" + "Latitude : " + latitude + "\n" + "Longitude" + longitude);
                        SmsManager sm = SmsManager.getDefault();
                        String msg = textView.getText().toString();
                        sm.sendTextMessage(num1, null, msg, null, null);
                        sm.sendTextMessage(num2, null, msg, null, null);
                        sm.sendTextMessage(num3, null, msg, null, null);
                        sm.sendTextMessage(num4, null, msg, null, null);
                    } else {
                        Toast.makeText(this, "GPS not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }}
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


//    @Override
//    public void onLocationChanged(Location location) {
//        double longitude=location.getLongitude();
//        double latitude=location.getLatitude();
//        textView.setText("Longitude : " +longitude+"\n" + "Latitude : " +latitude);
//        sendSMS();
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }

//    public void sendSMS()
//    {
//        SmsManager sm = SmsManager.getDefault();
//        String msg = textView.getText().toString();
//        sm.sendTextMessage(num1, null, msg, null, null);
//        sm.sendTextMessage(num2, null, msg, null, null);
//        sm.sendTextMessage(num3, null, msg, null, null);
//        sm.sendTextMessage(num4, null, msg, null, null);
//    }
    public void showMessage(String title,String message)
    {
        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
