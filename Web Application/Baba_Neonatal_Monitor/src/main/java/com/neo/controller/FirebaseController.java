package com.neo.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseController {
	
	FirebaseAuth mAuth;

	public void initFirebase() throws IOException {
		FileInputStream serviceAccount = new FileInputStream("src/main/resources/static/firebase-key.json");

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setDatabaseUrl("https://baba-neonatal-monitoring.firebaseio.com")
				.build();

		FirebaseApp.initializeApp(options);
	}
	
	public void firebaseAuthentication() {	
		mAuth = FirebaseAuth.getInstance();
	}
	
	public void loginUser(String email, String pass) throws InterruptedException, ExecutionException {
		//UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmailAsync(email).get();	
	}
	
	public void getTemperatureReading() {
		
	}
	
	public void firebaseDatabase() {
		DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Temperature/Average Temp");
		ref.addValueEventListener(new ValueEventListener() {

			@Override
			public void onCancelled(DatabaseError arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				String document = dataSnapshot.getValue().toString();
		        System.out.println(document);
			}
			
		});
	}
}
