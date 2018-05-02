package neo.baba.neonatalmonitoring;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Timer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.timer_activity);
        final TimePicker time = findViewById(R.id.time_fed);
        time.setIs24HourView(true);

        Calendar currentTime = Calendar.getInstance(Locale.getDefault());
        time.setHour(currentTime.get(Calendar.HOUR_OF_DAY));
        time.setMinute(currentTime.get(Calendar.MINUTE));
        time.animate();
    }
}
