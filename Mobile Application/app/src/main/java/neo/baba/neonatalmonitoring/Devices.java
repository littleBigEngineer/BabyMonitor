package neo.baba.neonatalmonitoring;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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

import neo.baba.neonatalmonitoring.fragment.LogoutFragment;
import neo.baba.neonatalmonitoring.neo.baba.neonatalmonitoring.model.Child;
import neo.baba.neonatalmonitoring.neo.baba.neonatalmonitoring.model.Device;

public class Devices extends AppCompatActivity {

    private String username;
    private DrawerLayout mDrawerLayout;
    private FirebaseDatabase database;
    private ArrayList<String> deviceId = new ArrayList<>();
    private ArrayList<Device> devices = new ArrayList<>();
    private ArrayList<String> childrenId = new ArrayList<>();
    private ArrayList<Child> children = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle extra = getIntent().getExtras();
        username = extra.getString("username");

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.devices_activity);
        database = FirebaseDatabase.getInstance();
        getDevices();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    public void getDevices(){
        final DatabaseReference deviceAssoc = database.getReference("Device Assoc").child(username);
        deviceAssoc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(int i = 1; i <= 6; i++) {
                    if(dataSnapshot.hasChild(""+i)) {
                        String device = dataSnapshot.child("" + i).getValue().toString();
                        if(!deviceId.contains(device))
                            deviceId.add(device);
                    }
                }

                popDevices();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void popDevices(){
        final DatabaseReference devs = database.getReference("Devices");
        devs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (int i = 0; i < deviceId.size(); i++) {
                    Device d = dataSnapshot.child(deviceId.get(i)).getValue(Device.class);
                    childrenId.add(d.getChild());
                    devices.add(d);
                }
                popChildren();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void popChildren(){
        final DatabaseReference cld = database.getReference("Child");
        cld.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(int i = 0; i < childrenId.size(); i++){
                    Child c = dataSnapshot.child(childrenId.get(i)).getValue(Child.class);
                    children.add(c);
                }
                display();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void display(){
        System.out.println("IN HERE");
        for(int i = 0; i < devices.size(); i++ ){
            int d = i+1;
            int device = this.getResources().getIdentifier("device_"+d, "id", getPackageName());
            int name_id = this.getResources().getIdentifier("device_"+d+"_name", "id", getPackageName());
            int child_id = this.getResources().getIdentifier("device_"+d+"_child", "id", getPackageName());
            int on_circ = this.getResources().getIdentifier("device_"+d+"_on", "id", getPackageName());
            int off_circ = this.getResources().getIdentifier("device_"+d+"_off", "id", getPackageName());

            ConstraintLayout dev = findViewById(device);
            dev.setVisibility(dev.VISIBLE);
            TextView name = findViewById(name_id);
            final TextView child = findViewById(child_id);
            //final TextView room = findViewById(room_id);
            Device sel_dev = devices.get(i);
            String childName = "";

            for(Child c: children){
                if(c.getId().equals(sel_dev.getChild()))
                    childName = c.getFirstName();
            }

            final ImageView on = findViewById(on_circ);
            final ImageView off = findViewById(off_circ);

            if(sel_dev.getActive() == 0){
                on.setBackgroundResource(R.drawable.empty_circ);
                off.setBackgroundResource(R.drawable.red_circ);
                dev.setEnabled(false);
            }
            else{
                on.setBackgroundResource(R.drawable.green_circ);
                off.setBackgroundResource(R.drawable.empty_circ);
            }

            name.setText(sel_dev.getDevice_name());
            child.setText("Child: " + childName);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public boolean onNavigationItemSelected(MenuItem item){
       int id = item.getItemId();
       Fragment fragment = null;
       Bundle bundle = new Bundle();
       if(id == R.id.nav_logout)
           fragment = new LogoutFragment();

       if(fragment !=  null) {
           FragmentTransaction ft = getFragmentManager().beginTransaction();
           ft.replace(R.id.content_frame, fragment);
           ft.commit();
       }
       DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
       drawer.closeDrawer(GravityCompat.START);
       return true;
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
        Devices.this.startActivity(logout);
        Toast.makeText(Devices.this, "Signed Out", Toast.LENGTH_SHORT).show();
        SaveSharedPreference.clearUserName(Devices.this);
    }
}