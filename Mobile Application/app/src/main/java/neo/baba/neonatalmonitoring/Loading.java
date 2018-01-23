package neo.baba.neonatalmonitoring;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import neo.baba.neonatalmonitoring.neo.baba.neonatalmonitoring.model.Account;

public class Loading extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    Account logged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_activity);
        Bundle extras = getIntent().getExtras();
        String uid = extras.getString("uid");
        logged = setAccount();
    }

    public Account setAccount(){
        Account a = new Account();

       // DatabaseReference ref = database.getReference("documents").child();

        a.setUid(mAuth.getUid());


        return a;
    }


//    public void logout(View view){
//        FirebaseAuth.getInstance().signOut();
//        System.out.println("Done");
//        Intent login = new Intent(Loading.this, Login.class);
//        System.out.println("Done again");
//        this.startActivity(login);
//    }
}
