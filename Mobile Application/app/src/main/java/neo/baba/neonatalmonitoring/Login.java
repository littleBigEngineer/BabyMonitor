package neo.baba.neonatalmonitoring;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import neo.baba.neonatalmonitoring.neo.baba.neonatalmonitoring.model.Account;

public class Login extends AppCompatActivity {

    private boolean forgotten = false;
    private DatabaseReference mDatabase;
    private String u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if(SaveSharedPreference.getUserName(Login.this).length() == 0)
        {
            String a = SaveSharedPreference.getUserName(Login.this);
        }
        else
        {
            String user = SaveSharedPreference.getUserName(Login.this);
            loading(user);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.login_activity);
    }

    public void loading(String username){
        Intent loading = new Intent(Login.this, Loading.class);
        loading.putExtra("logged", username);
        Login.this.startActivity(loading);
    }

    public void createAccount(View view){
        Intent createAccount = new Intent(Login.this, CreateAccount.class);
        Login.this.startActivity(createAccount);
    }

    public void login(View view){
        final EditText username = findViewById(R.id.username);
        u = username.getText().toString();

        if(!forgotten) {
            final EditText password = findViewById(R.id.password);
            String p = password.getText().toString();

            if (u.equals("a")) {
                u = "RobCrowley";
                p = "Password1!";
            }

            SaveSharedPreference.setUserName(Login.this, u);

            if (!(u.length() > 0 && p.length() > 0))
                AuthError();
            else {
                DatabaseReference acc = mDatabase.child("Accounts");
                acc.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.hasChild(u)) {
                            AuthError();
                        }
                        else{
                            Account a = dataSnapshot.child(u).getValue(Account.class);
                            loading(a.getUsername());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
        else{
            forgottenPassword(u);
        }
    }

    public void forgotten(View view){

        final ImageView passImg = findViewById(R.id.password_icon);
        final EditText email = findViewById(R.id.username);
        final TextView forgotten_txt = findViewById(R.id.forgot_pass);
        final Button button = findViewById(R.id.login_button);


        if(forgotten){
            Intent login = new Intent(Login.this, Login.class);
            Login.this.startActivity(login);
        }
        else {
            forgotten = true;
            email.setHint("Verification e-mail");
            EditText password = findViewById(R.id.password);
            password.setEnabled(false);
            password.setVisibility(View.GONE);
            passImg.setVisibility(View.GONE);
            forgotten_txt.setText(R.string.return_login);
            button.setText(R.string.send_email);
        }
    }

    public void forgottenPassword(String email){
        Toast.makeText(Login.this, "Input e-mail address in ", Toast.LENGTH_LONG).show();
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Login.this, "Verification e-mail sent", Toast.LENGTH_LONG).show();
                    Intent login = new Intent(Login.this, Login.class);
                    Login.this.startActivity(login);
                    forgotten = false;
                }
            }
        });
    }

    public void AuthError(){
        Toast.makeText(Login.this, "Invalid Email Address/Password Provided", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        this.startActivity(i);
    }
}
