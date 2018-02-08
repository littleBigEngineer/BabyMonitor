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

	public void checkForAccount(String username, String password) {
		DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts/"+username);
		ref.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				account = dataSnapshot.getValue(Account.class);
				if(!password.equals(account.getPassword()))
					account = null;
			}

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub	
			}
		});
	}

	public void firebaseDatabase() {

	}
}
