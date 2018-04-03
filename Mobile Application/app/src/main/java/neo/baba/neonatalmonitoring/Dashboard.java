package neo.baba.neonatalmonitoring;

import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
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

public class Dashboard extends AppCompatActivity {

    FirebaseDatabase database;
    final int ROOM_TEMP = 20;
    int reading;
    boolean type = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extra = getIntent().getExtras();
        System.out.println("Logged in as: " + extra.getString("username"));
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dashboard_activity);
        database = FirebaseDatabase.getInstance();

        final DatabaseReference temperature = database.getReference("Temperature");

        final ImageView cold = findViewById(R.id.cold);
        final ImageView temperate = findViewById(R.id.temperate);
        final ImageView hot = findViewById(R.id.hot);
        final ImageView timer = findViewById(R.id.timer_img);


        temperature.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String currentTemp = dataSnapshot.child("Current Temp").getValue().toString();
                float current = Math.round(Float.parseFloat(currentTemp));
                final TextView curr = findViewById(R.id.currTemp);
                reading = (int)current;
                curr.setText((int)current+"\u00b0C");
                updateWidget();

                if(current > ROOM_TEMP+5){
                    createNotification("Your baby needs you!", "The temperature in the room is too hot!");
                    notificationSound();
                    hot.setBackgroundResource(R.drawable.red_circ);
                    cold.setBackgroundResource(R.drawable.empty_circ);
                    temperate.setBackgroundResource(R.drawable.empty_circ);
                }
                else if(current < ROOM_TEMP-5){
                    createNotification("Your baby needs you!", "The temperature in the room is too cold!");
                    notificationSound();
                    cold.setBackgroundResource(R.drawable.blue_circ);
                    hot.setBackgroundResource(R.drawable.empty_circ);
                    temperate.setBackgroundResource(R.drawable.empty_circ);
                }
                else{
                    cold.setBackgroundResource(R.drawable.empty_circ);
                    hot.setBackgroundResource(R.drawable.empty_circ);
                    temperate.setBackgroundResource(R.drawable.green_circ);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void notificationSound(){
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),notification);
        r.play();
    }

//    public void tempSwitch(View view){
//        final ConstraintLayout tempLayout = findViewById(R.id.temperature);
//        //final Switch temp_switch = findViewById(R.id.temp_switch);
//        Boolean switch_status = temp_switch.isChecked();
//
//        if(!switch_status) {
//            tempLayout.setEnabled(false);
//
//            switchToast(0,"Temperature");
//        }
//        else {
//            tempLayout.setEnabled(true);
//            switchToast(1, "Temperature");
//        }
//    }

    public void celsToFar(View view){
        final TextView curr = findViewById(R.id.currTemp);
        if(type) {
            double far = (reading * 1.8) + 32;
            far = Math.round(far);
            curr.setText((int) far + "\u00b0F");
            type = false;
        }
        else {
            curr.setText(reading + "\u00b0C");
            type = true;
        }
    }

    public void timerScreen(View view){
        Intent timer = new Intent(Dashboard.this, Timer.class);
        Dashboard.this.startActivity(timer);
    }

    public void switchToast(int type, String module){
        String status = "";
        if(type == 0)
            status = "disabled";
        else
            status = "enabled";

        Toast.makeText(Dashboard.this, module + " Module " + status, Toast.LENGTH_SHORT).show();
    }
    public void logout(View view){
        Intent logout = new Intent(Dashboard.this, Login.class);
        Dashboard.this.startActivity(logout);
        Toast.makeText(Dashboard.this, "Signed Out", Toast.LENGTH_SHORT).show();
        SaveSharedPreference.clearUserName(Dashboard.this);    }

    public void createNotification(String title, String content){
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.white_baby_sleeping).setContentTitle(title).setContentText(content);

        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(001,nBuilder.build());
    }

    public void updateWidget(){
        Intent intent = new Intent(this, BabaWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(),BabaWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        this.startActivity(i);
    }
}
