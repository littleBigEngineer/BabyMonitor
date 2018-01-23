package neo.baba.neonatalmonitoring;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_activity);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("Checking");
        if(mAuth.getCurrentUser() != null){
            FirebaseUser currentUser = mAuth.getCurrentUser();
            System.out.println("Email: " + currentUser.getEmail());
            loading(currentUser.getUid());
        }
    }

    public void loading(String uid){
        Intent loading = new Intent(Login.this, Loading.class);
        loading.putExtra("uid", uid);
        Login.this.startActivity(loading);
    }

    public void createAccount(View view){
        Intent createAccount = new Intent(Login.this, CreateAccount.class);
        Login.this.startActivity(createAccount);
    }

    public void login(View view){
        final EditText email = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);

        String e = email.getText().toString();
        String p = password.getText().toString();
        if(!(e.length() > 0 && p.length() > 0))
            AuthError();
        else {
            mAuth.signInWithEmailAndPassword(e, p).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        loading(user.getUid());
                    } else {
                        task.getException();
                        AuthError();
                    }
                }
            });
        }
    }

    public void AuthError(){
        Toast.makeText(Login.this, "Invalid Email Address/Password Provided", Toast.LENGTH_LONG).show();
    }
}
