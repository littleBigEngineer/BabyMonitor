package com.neo.controller;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.neo.model.Account;

public class FirebaseController {

	DatabaseReference ref;
	DataSnapshot ds;
	AccountController ac;
	Account account;
	boolean accountFlag;


	public void initFirebase() throws IOException {
		FileInputStream serviceAccount = new FileInputStream("src/main/resources/static/firebase-key.json");

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setDatabaseUrl("https://baba-neonatal-monitoring.firebaseio.com")
				.build();

		FirebaseApp.initializeApp(options);
	}

	public void getTemperatureReading() {

	}

	public Account checkForAccount(String username, String password) {
		System.out.println("Firebase");
		System.out.println(username);
		DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts/"+username);
		accountFlag = false;
		ref.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				System.out.println("onChange");
				account = dataSnapshot.getValue(Account.class);
				System.out.println(account.getFirstName());
				accountFlag = true;
			}

			@Override
			public void onCancelled(DatabaseError error) {
				
			}
		});
		while(!accountFlag) {
			
		}
		if(!password.equals(account.getPassword()))
			account = null;
		
		return account;
	}

	public void firebaseDatabase() {

	}
}
