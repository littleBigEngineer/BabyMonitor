package com.neo.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseController {

	DatabaseReference ref;
	DataSnapshot ds;
	AccountController ac;
	FirebaseDatabase fd;

	String uName = "";
	Map<String, Object> output = new HashMap<>();

	ArrayList<String> username = new ArrayList<>();
	ArrayList<String> email = new ArrayList<>();
	boolean done = false;
	ArrayList<ArrayList<String>> returnValue = new ArrayList<>();

	final String firebaseKey = "src/main/resources/static/firebase-key.json";


	public void initFirebase() throws IOException {
		FileInputStream serviceAccount = new FileInputStream(firebaseKey);
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setDatabaseUrl("https://baba-neonatal-monitoring.firebaseio.com")
				.build();
		System.out.println("Done");
		FirebaseApp.initializeApp(options);
	}
	
	public ArrayList<String> getUsers(){
		getUsernames();
		return username;
	}

	public ArrayList<ArrayList<String>> getUsernames(){
		DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");
		ref.addListenerForSingleValueEvent(new ValueEventListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				username.clear();
				email.clear();

				for(DataSnapshot ds: snapshot.getChildren()) {
					output = (Map<String, Object>) ds.getValue();
					username.add(output.get("username").toString());
					email.add(output.get("email").toString());
				}

				returnValue.clear();

				returnValue.add(username);
				returnValue.add(email);
				done = true;
			}

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub

			}
		});

		while(!done) {

		}
		return returnValue;
	}
}
