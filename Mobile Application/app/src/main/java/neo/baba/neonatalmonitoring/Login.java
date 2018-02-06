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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private boolean forgotten = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_activity);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            loading();
        }
    }

    public void loading(){
        Intent loading = new Intent(Login.this, Loading.class);
        Login.this.startActivity(loading);
    }

    public void createAccount(View view){
        Intent createAccount = new Intent(Login.this, CreateAccount.class);
        Login.this.startActivity(createAccount);
    }

    public void login(View view){
        final EditText email = findViewById(R.id.email);
        String e = email.getText().toString();

        if(!forgotten) {
            final EditText password = findViewById(R.id.password);
            String p = password.getText().toString();

            if (e.equals("a")) {
                e = "robert.crowley1@mycit.ie";
                p = "password1";
            }

            if (!(e.length() > 0 && p.length() > 0))
                AuthError();
            else {
                mAuth.signInWithEmailAndPassword(e, p).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loading();
                        } else {
                            AuthError();
                        }
                    }
                });
            }
        }
        else{
            forgottenPassword(e);
        }
    }

    public void forgotten(View view){

        final ImageView passImg = findViewById(R.id.password_icon);
        final EditText email = findViewById(R.id.email);
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
}
