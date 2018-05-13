package neo.baba.neonatalmonitoring;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.TimerTask;

import neo.baba.neonatalmonitoring.neo.baba.neonatalmonitoring.model.Device;

public class Devices extends AppCompatActivity {

    private String username, notification = "";
    private DatabaseReference database;
    int num_devices, temp;    boolean devicesDone, sendNotification;
    private ArrayList<String> listDevices;
    private ArrayList<Device> devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.devices_activity);

        Bundle extra = getIntent().getExtras();
        username = extra.getString("username");
        database = FirebaseDatabase.getInstance().getReference();
        final TextView curr_login = findViewById(R.id.curr_login);
        curr_login.setText(username);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){
            getUserDevices();

            final Handler handler = new Handler();
            int delay = 5000;

            handler.postDelayed(new Runnable(){
                public void run(){
                    for(int i = 1; i <= devices.size(); i++){
                        updateValues(devices.get(i-1).getDevice_id(), i);
                    }
                    handler.postDelayed(this, 5000);
                }
            }, delay);
        }
    }

    public void getUserDevices(){
        listDevices = new ArrayList<>();
        database.child("Device Assoc").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    listDevices.add(ds.getValue(String.class));
                }
                getDevices();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Nothing");
            }
        });
    }

    public void getDevices(){
        devices = new ArrayList<>();
        database.child("Devices").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(String device : listDevices) {
                    devices.add(dataSnapshot.child(device).getValue(Device.class));
                }
                displayDevices();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("ERROR");
            }
        });
    }

    public void displayDevices(){
        int i = 1;
        System.out.println(num_devices);
        for (i = 1; i <= devices.size(); i++) {
            System.out.println("Current:" + i);
            int res = getResources().getIdentifier("m" + i, "id", this.getPackageName());
            final ConstraintLayout mon = findViewById(res);
            mon.setVisibility(View.VISIBLE);

            res = getResources().getIdentifier("currPlaying_" + i, "id", this.getPackageName());
            final TextView playing = findViewById(res);
            playing.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            playing.setSelected(true);
            playing.setSingleLine(true);

            res = getResources().getIdentifier("device_name_" + i, "id", this.getPackageName());
            final TextView device_name = findViewById(res);
            device_name.setText(devices.get(i - 1).getDevice_name());

            updateValues(devices.get(i-1).getDevice_id(), i);
        }
        num_devices = devices.size();
        devicesDone = true;
    }

    public void issueAlert(String message, String device, int deviceNum){

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        Intent intent = new Intent(getApplicationContext(), Devices.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        mBuilder.setContentIntent(pendingIntent);

        mBuilder.setSmallIcon(R.drawable.sleeping_baby);
        mBuilder.setContentTitle("Uh Oh, I think we've got something");
        mBuilder.setContentText("(" + device + ") " + message);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(deviceNum, mBuilder.build());

    }

    public void updateValues(final String deviceId, final int deviceNum){
        sendNotification = false;
        notification = "";
        database.child("Temperature").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(deviceId).getValue(Double.class) == null)
                    temp = 0;
                else
                    temp = (int)Math.round(0.0 + dataSnapshot.child(deviceId).getValue(Double.class));

                int res = getResources().getIdentifier("temp_value_" + deviceNum, "id", getPackageName());
                final TextView tempVal = findViewById(res);

                res = getResources().getIdentifier("temp_" + deviceNum, "id", getPackageName());
                final ConstraintLayout tempView = findViewById(res);

                tempView.setBackgroundColor(Color.parseColor("#66ff66"));

                if(temp < 15) {
                    issueAlert("Temperature too low (" + temp + "\u00b0C)", deviceId, deviceNum);
                    tempView.setBackgroundColor(Color.parseColor("#A5F2F3"));
                }
                if(temp > 25) {
                    issueAlert("Temperature too high (" + temp + "\u00b0C)", deviceId, deviceNum);
                    tempView.setBackgroundColor(Color.parseColor("#ce2029"));
                }
                tempVal.setText(temp + "\u00b0C");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("ERROR");
            }
        });

        database.child("Gasses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(deviceId).child("CO").getValue(String.class).equals("Normal")){
                    int res = getResources().getIdentifier("monoxide_" + deviceNum, "id", getPackageName());
                    final ImageView image = findViewById(res);
                    image.setImageResource(R.drawable.green_carbon);
                    issueAlert("Carbon Monoxide Detected!", deviceId, deviceNum);
                }
                else{
                    int res = getResources().getIdentifier("monoxide_" + deviceNum, "id", getPackageName());
                    final ImageView image = findViewById(res);
                    image.setImageResource(R.drawable.red_carbon);
                }

                if(dataSnapshot.child(deviceId).child("CO2").getValue(String.class).equals("Normal")){
                    int res = getResources().getIdentifier("smoke_" + deviceNum, "id", getPackageName());
                    final ImageView image = findViewById(res);
                    image.setImageResource(R.drawable.green_smoke);
                    issueAlert("Smoke Detected!" + temp + "\u00b0C)", deviceId, deviceNum);
                }
                else{
                    int res = getResources().getIdentifier("smoke_" + deviceNum, "id", getPackageName());
                    final ImageView image = findViewById(res);
                    image.setImageResource(R.drawable.red_smoke);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        this.startActivity(i);
    }

    public void logout(View view){
        Intent logout = new Intent(Devices.this, Login.class);
        this.startActivity(logout);
        Toast.makeText(Devices.this, "Signed Out", Toast.LENGTH_SHORT).show();
        SaveSharedPreference.clearUserName(Devices.this);
    }

    public void settings(View view){
        System.out.println("PRESSED");
//        Intent intent = new Intent(getBaseContext(), Settings.class);
//        intent.putExtra("devices", deviceId);
//        intent.putExtra("username", username);
//        Devices.this.startActivity(intent);
    }
}