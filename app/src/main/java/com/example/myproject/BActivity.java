package com.example.myproject;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BActivity extends Activity {
    //    private final String DEVICE_NAME="MyBTBee";
    String num1, num2, num3, num4, user_log;
    private CameraManager mCameraManager;
    private String mCameraId;
    private static final int REQUEST_LOCATION = 1;
    private final int REQUEST_CODE=1;

    private TextView textView;
    LocationManager locationManager;
    String latitude, longitude;
    SQLiteDatabase db;
    private final String DEVICE_ADDRESS = "00:19:08:35:DF:2B";
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    Button startButton, sendButton, clearButton, stopButton;
   // TextView textView;
    EditText editText;
    boolean deviceConnected = false;
    Thread thread;
    byte buffer[];
    int bufferPosition;
    boolean stopThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth);

        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        textView = (TextView) findViewById(R.id.id_textview);

        db = openOrCreateDatabase("project", Context.MODE_PRIVATE, null);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        startButton = (Button) findViewById(R.id.buttonStart);
        sendButton = (Button) findViewById(R.id.buttonSend);
        clearButton = (Button) findViewById(R.id.buttonClear);
        stopButton = (Button) findViewById(R.id.buttonStop);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        setUiEnabled(false);
    }

    public void setUiEnabled(boolean bool) {
        startButton.setEnabled(!bool);
        sendButton.setEnabled(bool);
        stopButton.setEnabled(bool);
        textView.setEnabled(bool);

    }

    public boolean BTinit() {
        boolean found = false;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Device doesnt Support Bluetooth", Toast.LENGTH_SHORT).show();
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if (bondedDevices.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Pair the Device first", Toast.LENGTH_SHORT).show();
        } else {
            for (BluetoothDevice iterator : bondedDevices) {
                if (iterator.getAddress().equals(DEVICE_ADDRESS)) {
                    device = iterator;
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    public boolean BTconnect() {
        boolean connected = true;
        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            connected = false;
        }
        if (connected) {
            try {
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream = socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return connected;
    }

    public void onClickStart(View view) {
        if (BTinit()) {
            if (BTconnect()) {
                setUiEnabled(true);
                deviceConnected = true;
                beginListenForData();
                textView.append("\nConnection Opened!\n");
            }

        }
    }

    void beginListenForData() {
        final Handler handler = new Handler();
        stopThread = false;
        buffer = new byte[1024];
        Thread thread = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopThread) {
                    try {
                        int byteCount = inputStream.available();
                        if (byteCount > 0) {
                            byte[] rawBytes = new byte[byteCount];
                            inputStream.read(rawBytes);
                            final String string = new String(rawBytes, "UTF-8");
                            handler.post(new Runnable() {
                                public void run() {
                                    //textView.append(string);
                                    textView.setText(string);
                                    try {
                                        sendSMS();
                                    } catch (CameraAccessException e) {
                                        e.printStackTrace();
                                    }
                                    //record_video();
                                }
                            });
                        }
                    } catch (IOException ex) {
                        stopThread = true;
                    }
                }
            }
        });

        thread.start();
    }

    public void onClickSend(View view) {
        String string = editText.getText().toString();
        string.concat("\n");
        try {
            outputStream.write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView.append("\nSent Data:" + string + "\n");


    }

    public void onClickStop(View view) throws IOException {
        stopThread = true;
        outputStream.close();
        inputStream.close();
        socket.close();
        setUiEnabled(false);
        deviceConnected = false;
        textView.append("\nConnection Closed!\n");
    }

    public void onClickClear(View view) {
        textView.setText("");
    }

    public void sendSMS() throws CameraAccessException {
            mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            mCameraId = mCameraManager.getCameraIdList()[0];
            mCameraManager.setTorchMode(mCameraId, true);

        db = openOrCreateDatabase("project", Context.MODE_PRIVATE, null);

        Cursor g = db.rawQuery("SELECT * FROM user_log", null);
        if (g.moveToNext()) {
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


                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                Location LocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location LocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Location LocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

                if (LocationGps != null) {
                    double lat = LocationGps.getLatitude();
                    double longi = LocationGps.getLongitude();

                    latitude = String.valueOf(lat);
                    longitude = String.valueOf(longi);
                    //textView.setText("Please help I'm in a Emergency, Copy this address and search for my location in maps "+ latitude +"," + longitude);
                    //textView.setText(latitude +"," + longitude + "\n" + "\n" +"Please copy the above numbers as such (latitude,longitude) and paste it in google maps to know their location. Please react fast as the sender is in some kind of emergency." + "\n" + "\n'" + "-- Technology is here to save you");
                    SmsManager sm = SmsManager.getDefault();
                    String msg = latitude +"," + longitude + "\n" + "\n" +"Please copy the above numbers as such (latitude,longitude) and paste it in google maps to know their location. Please react fast as the sender is in some kind of emergency." + "\n" + "\n" + "-- Technology is here to save you";
                    sm.sendTextMessage(num1, null, msg, null, null);
                    sm.sendTextMessage(num2, null, msg, null, null);
                    sm.sendTextMessage(num3, null, msg, null, null);
                    sm.sendTextMessage(num4, null, msg, null, null);
                    Toast.makeText(this, "Location SMS Sent to Emergency Numbers", Toast.LENGTH_SHORT).show();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+num1));
                    //callIntent.setData(Uri.parse("tel:"+num2));
                    //callIntent.setData(Uri.parse("tel:"+num3));
                    //callIntent.setData(Uri.parse("tel:"+num4));
                    startActivity(callIntent);
                    Toast.makeText(this, "Calling your Emergency Number", Toast.LENGTH_SHORT).show();
                } else if (LocationNetwork != null) {
                    double lat = LocationNetwork.getLatitude();
                    double longi = LocationNetwork.getLongitude();

                    latitude = String.valueOf(lat);
                    longitude = String.valueOf(longi);
                    //textView.setText(latitude +"," + longitude);
                    //textView.setText(latitude +"," + longitude + "\n" + "\n" +"Please copy the above numbers as such (latitude,longitude) and paste it in google maps to know their location. Please react fast as the sender is in some kind of emergency." + "\n" + "\n'" + "-- Technology is here to save you");
                    SmsManager sm = SmsManager.getDefault();
                    String msg = latitude +"," + longitude + "\n" + "\n" +"Please copy the above numbers as such (latitude,longitude) and paste it in google maps to know their location. Please react fast as the sender is in some kind of emergency." + "\n" + "\n" + "-- Technology is here to save you";
                    sm.sendTextMessage(num1, null, msg, null, null);
                    sm.sendTextMessage(num2, null, msg, null, null);
                    sm.sendTextMessage(num3, null, msg, null, null);
                    sm.sendTextMessage(num4, null, msg, null, null);
                    Toast.makeText(this, "Location SMS Sent to Emergency Numbers", Toast.LENGTH_SHORT).show();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+num1));
                    //callIntent.setData(Uri.parse("tel:"+num2));
                    //callIntent.setData(Uri.parse("tel:"+num3));
                    //callIntent.setData(Uri.parse("tel:"+num4));
                    startActivity(callIntent);
                    Toast.makeText(this, "Calling your Emergency Number", Toast.LENGTH_SHORT).show();
                } else if (LocationPassive != null) {
                    double lat = LocationPassive.getLatitude();
                    double longi = LocationPassive.getLongitude();

                    latitude = String.valueOf(lat);
                    longitude = String.valueOf(longi);

                    //textView.setText("Latitudeee : " + latitude + "\n" + "Longitude : " + longitude);
                    //textView.setText(latitude +"," + longitude + "\n" + "\n" +"Please copy the above numbers as such (latitude,longitude) and paste it in google maps to know their location. Please react fast as the sender is in some kind of emergency." + "\n" + "\n'" + "-- Technology is here to save you");
                    SmsManager sm = SmsManager.getDefault();
                    //String msg = textView.getText().toString();
                    String msg = latitude +"," + longitude + "\n" + "\n" +"Please copy the above numbers as such (latitude,longitude) and paste it in google maps to know their location. Please react fast as the sender is in some kind of emergency." + "\n" + "\n" + "-- Technology is here to save you";
                    sm.sendTextMessage(num1, null, msg, null, null);
                    sm.sendTextMessage(num2, null, msg, null, null);
                    sm.sendTextMessage(num3, null, msg, null, null);
                    sm.sendTextMessage(num4, null, msg, null, null);
                    Toast.makeText(this, "Location SMS Sent to Emergency Numbers", Toast.LENGTH_SHORT).show();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+num1));
                    //callIntent.setData(Uri.parse("tel:"+num2));
                    //callIntent.setData(Uri.parse("tel:"+num3));
                    //callIntent.setData(Uri.parse("tel:"+num4));
                    startActivity(callIntent);
                    Toast.makeText(this, "Calling your Emergency Number", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Not able Find Your Location. Please Enable GPS", Toast.LENGTH_SHORT).show();
                }
            }

        }
        textView.setText("");
    }
    public void record_video(){

        Intent i =new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//        i.putExtra(MediaStore.EXTRA_DURATION_LIMIT,300);
//        startActivityForResult(i,1);
        File video_file = getFile();

        Uri uri =  Uri.fromFile(video_file);

//        i.putExtra(MediaStore.EXTRA_OUTPUT,uri);
//        i.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);

        startActivityForResult(i,REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode == REQUEST_CODE){
            if(requestCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Video has been saved", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),"ERROR, Video not saved", Toast.LENGTH_LONG).show();
            }
        }
    }


    public File getFile(){

        File folder = new File("sdcard/myFolder");

        if(!folder.exists()){
            folder.mkdir();
        }

        File video_file = new File(folder,"myVideo.mp4");
        return video_file;
    }

    }
