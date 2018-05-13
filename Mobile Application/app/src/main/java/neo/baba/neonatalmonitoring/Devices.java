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

import neo.baba.neonatalmonitoring.neo.baba.neonatalmonitoring.model.Device;


public class Devices extends AppCompatActivity {

    private String username, notification = "";
    private DatabaseReference database;
    int num_devices, temp, sound;    boolean devicesDone, sendNotification;
    private ArrayList<String> listDevices;
    private ArrayList<Device> devices;
    private boolean pause = false;

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

    public void issueAlert(String device, int deviceNum){

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        Intent intent = new Intent(getApplicationContext(), Devices.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        mBuilder.setContentIntent(pendingIntent);

        mBuilder.setSmallIcon(R.drawable.sleeping_baby);
        mBuilder.setContentTitle("Uh Oh, I think we've got something");
        mBuilder.setContentText("(" + device + ") " + "Check on the kiddos!");

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(deviceNum, mBuilder.build());

    }

    public void updateValues(final String deviceId, final int deviceNum){
        sendNotification = false;
        notification = "";
        database.child("Devices").child(deviceId).child("temperature").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(Double.class) == null)
                    temp = 0;
                else
                    temp = (int)Math.round(0.0 + dataSnapshot.getValue(Double.class));

                int res = getResources().getIdentifier("temp_value_" + deviceNum, "id", getPackageName());
                final TextView tempVal = findViewById(res);

                res = getResources().getIdentifier("temp_" + deviceNum, "id", getPackageName());
                final ConstraintLayout tempView = findViewById(res);

                tempView.setBackgroundColor(Color.parseColor("#66ff66"));

                if(temp < 15) {
                    issueAlert(deviceId, deviceNum);
                    tempView.setBackgroundColor(Color.parseColor("#A5F2F3"));
                }
                if(temp > 25) {
                    issueAlert(deviceId, deviceNum);
                    tempView.setBackgroundColor(Color.parseColor("#ce2029"));
                }
                tempVal.setText(temp + "\u00b0C");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("ERROR");
            }
        });

        database.child("Devices").child(deviceId).child("currently_playing").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int res = getResources().getIdentifier("currPlaying_" + deviceNum, "id", getPackageName());
                final TextView currPlay = findViewById(res);
                currPlay.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        database.child("Devices").child(deviceId).child("carbon_dioxide").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(String.class).equals("Normal")){
                    int res = getResources().getIdentifier("smoke_" + deviceNum, "id", getPackageName());
                    final ImageView image = findViewById(res);
                    image.setImageResource(R.drawable.green_smoke);
                    issueAlert(deviceId, deviceNum);
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

        database.child("Devices").child(deviceId).child("humidity").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(double.class) > 60){
                    int res = getResources().getIdentifier("humid_" + deviceNum, "id", getPackageName());
                    final ImageView image = findViewById(res);
                    image.setImageResource(R.drawable.red_humid);
                    issueAlert(deviceId, deviceNum);
                }
                else{
                    int res = getResources().getIdentifier("humid_" + deviceNum, "id", getPackageName());
                    final ImageView image = findViewById(res);
                    image.setImageResource(R.drawable.green_humid);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        database.child("Devices").child(deviceId).child("sound").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(Double.class) == null)
                    sound = 0;
                else
                    sound = (int)Math.round(0.0 + dataSnapshot.getValue(Double.class));

                int res = getResources().getIdentifier("decb_value_" + deviceNum, "id", getPackageName());
                final TextView soundVal = findViewById(res);

                res = getResources().getIdentifier("sound_" + deviceNum, "id", getPackageName());
                final ConstraintLayout soundView = findViewById(res);

                soundView.setBackgroundColor(Color.parseColor("#66ff66"));

                if(sound < 30)
                    soundView.setBackgroundColor(Color.parseColor("#66ff66"));
                if(sound > 30 && sound < 50)
                    soundView.setBackgroundColor(Color.parseColor("#ffb347"));
                if(sound > 50) {
                    soundView.setBackgroundColor(Color.parseColor("#ffb347"));
                    issueAlert(deviceId, deviceNum);
                }
                soundVal.setText(sound + "dB");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("ERROR");
            }
        });

        database.child("Devices").child(deviceId).child("carbon_monoxide").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(String.class).equals("Normal")){
                    int res = getResources().getIdentifier("monoxide_" + deviceNum, "id", getPackageName());
                    final ImageView image = findViewById(res);
                    image.setImageResource(R.drawable.green_carbon);
                }
                else{
                    int res = getResources().getIdentifier("monoxide_" + deviceNum, "id", getPackageName());
                    final ImageView image = findViewById(res);
                    image.setImageResource(R.drawable.red_carbon);
                    issueAlert(deviceId, deviceNum);
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

    public void nextSong(View view){
        int num = Integer.parseInt(getResources().getResourceEntryName(view.getId()).substring(getResources().getResourceEntryName(view.getId()).length() - 1));
        database.child("Device_Instruction").child(listDevices.get(num-1)).setValue("next");
    }

    public void pauseSong(View view){
        int num = Integer.parseInt(getResources().getResourceEntryName(view.getId()).substring(getResources().getResourceEntryName(view.getId()).length() - 1));

        int res = getResources().getIdentifier("play_" + num, "id", getPackageName());
        final ImageView play = findViewById(res);

        if(!pause) {
            database.child("Device_Instruction").child(listDevices.get(num - 1)).setValue("pause");
            pause = true;
            play.setImageResource(R.drawable.pause);
        }
        else {
            database.child("Device_Instruction").child(listDevices.get(num - 1)).setValue("play");
            pause = false;
            play.setImageResource(R.drawable.play);
        }
    }

    public void prevSong(View view){
        int num = Integer.parseInt(getResources().getResourceEntryName(view.getId()).substring(getResources().getResourceEntryName(view.getId()).length() - 1));
        database.child("Device_Instruction").child(listDevices.get(num-1)).setValue("prev");
    }

    public void logout(View view){
        Intent logout = new Intent(Devices.this, Login.class);
        this.startActivity(logout);
        Toast.makeText(Devices.this, "Signed Out", Toast.LENGTH_SHORT).show();
        SaveSharedPreference.clearUserName(Devices.this);
    }
}