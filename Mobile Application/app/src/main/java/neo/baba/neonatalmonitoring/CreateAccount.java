package neo.baba.neonatalmonitoring;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import neo.baba.neonatalmonitoring.neo.baba.neonatalmonitoring.model.Account;

public class CreateAccount extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String firstName, lastName, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_account_activity);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    public void back(View view){
        Intent back = new Intent(CreateAccount.this, Login.class);
        this.startActivity(back);
    }

    public void createAccount(View view) {
        boolean flag = true;
        final EditText fName = findViewById(R.id.first_name);
        final EditText lName = findViewById(R.id.last_name);
        final EditText emailAdd = findViewById(R.id.email);
        final EditText phoneNo = findViewById(R.id.phone_no);
        final EditText pOne = findViewById(R.id.password);
        final EditText pTwo = findViewById(R.id.password_again);

        firstName = fName.getText().toString();
        lastName = lName.getText().toString();
        String email = emailAdd.getText().toString();
        phone = phoneNo.getText().toString();
        String password = pOne.getText().toString();

        if (firstName.length() < 1) {
            fName.setText("");
            fName.setError("First Name is required");
            flag = false;
        }
        if (lastName.length() < 1) {
            lName.setText("");
            lName.setError("Last Name is required");
            flag = false;
        }
        if (!email.contains("@")) {
            emailAdd.setText("");
            emailAdd.setError("Valid E-mail Address is required (Containing '@')");
            flag = false;
        }
        if (phone.length() < 10 || phone.length() > 10) {
            phoneNo.setText("");
            phoneNo.setError("Valid 10-digit phone number required");
            flag = false;
        }
        if (!(passwordValidation(password) && password.equals(pTwo.getText().toString()))) {
            System.out.println(pOne.getText().toString());
            System.out.println(pTwo.getText().toString());
            pOne.setText("");
            pOne.setError("Valid Password required:\n- At least 8 characters in length\n- At least contain 1 uppercase letter\n- At " +
                    "least contain 1 number\n- At least contain 1 symbol (%^!*&)");
            pTwo.setText("");
            flag = false;
        }

        if (flag) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser newUser = mAuth.getCurrentUser();
                        addUser(newUser);
                        Toast.makeText(CreateAccount.this, "Thank you, Account Created", Toast.LENGTH_SHORT).show();
                        Intent backToLogin = new Intent(CreateAccount.this, Login.class);
                        CreateAccount.this.startActivity(backToLogin);
                    } else {
                        Toast.makeText(CreateAccount.this, "Sorry, Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(CreateAccount.this, "Sorry, Please re-enter the missing fields", Toast.LENGTH_SHORT).show();
        }
    }


    public void addUser(FirebaseUser newUser){
        String uid = newUser.getUid();
        Account newAccount = new Account(firstName, lastName, uid, phone);
        mDatabase.child("Accounts").child(uid).setValue(newAccount);
        mAuth.signOut();
    }

    public boolean passwordValidation(String password){
        boolean charCount = false;
        boolean uppercase = false;
        boolean number = false;
        boolean alpha = false;
        boolean space = false;

        if(password.length()>=8)
            charCount = true;

        for(int i = 0; i < password.length(); i++){
            char a = password.charAt(i);
            if(Character.isUpperCase(a))
                uppercase = true;
            if(Character.isDigit(a))
                number = true;
            if(!Character.isLetterOrDigit(a))
                alpha = true;
            if(a == ' ')
                space = true;
        }

        if(charCount && uppercase && number && alpha && !space) {
            return true;
        }
        else
            return false;
    }
}
