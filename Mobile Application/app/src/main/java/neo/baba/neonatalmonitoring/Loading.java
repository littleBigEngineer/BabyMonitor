package neo.baba.neonatalmonitoring;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Toast;

public class Loading extends AppCompatActivity {

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extra = getIntent().getExtras();
        username = extra.getString("logged");
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_activity);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent dashboard = new Intent(Loading.this, Devices.class);
                dashboard.putExtra("username", username);
                Loading.this.startActivity(dashboard);
            }
        }, 2000);
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(Loading.this, "Sorry, You can't do that right now", Toast.LENGTH_SHORT).show();
    }
}