package neo.baba.neonatalmonitoring;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dashboard);
        database = FirebaseDatabase.getInstance();

        final DatabaseReference temperature = database.getReference("Temperature");

        temperature.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String currentTemp = dataSnapshot.child("Current Temp").getValue().toString();
                float current = Math.round(Float.parseFloat(currentTemp));
                String avgTemp = dataSnapshot.child("Average Temp").getValue().toString();
                float average = Math.round(Float.parseFloat(avgTemp));

                TextView curr = findViewById(R.id.currTemp);
                curr.setText((int)current+"\u00b0");
                TextView avg = findViewById(R.id.avgTemp);
                avg.setText("Average:" + (int)average+"\u00b0");
                ConstraintLayout temp = findViewById(R.id.temperature);

                if(current > ROOM_TEMP+5){
                    createNotification("Your baby needs you!", "The temperature in the room is too hot!");
                    temp.setBackgroundColor(Color.parseColor("#e50000"));
                }
                else if(current < ROOM_TEMP-5){
                    createNotification("Your baby needs you!", "The temperature in the room is too cold!");
                    temp.setBackgroundColor(Color.parseColor("#4C9CEC"));
                }
                else{
                    temp.setBackgroundColor(Color.parseColor("#F39c12"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void createNotification(String title, String content){
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.white_baby_sleeping).setContentTitle(title).setContentText(content);

        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(001,nBuilder.build());
    }


}
