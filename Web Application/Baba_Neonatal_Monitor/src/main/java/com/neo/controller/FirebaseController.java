package com.neo.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@Controller
public class FirebaseController {

	DatabaseReference ref;
	DataSnapshot ds;
	AccountController ac;
	FirebaseDatabase fd;

	String uName = "";
	boolean first = true;
	Map<String, Object> output = new HashMap<>();

	ArrayList<String> username = new ArrayList<>();
	ArrayList<String> email = new ArrayList<>();
	boolean done = false;
	ArrayList<ArrayList<String>> returnValue = new ArrayList<>();

	final String firebaseKey = "https://drive.google.com/open?id=1Qpq42kGj0nJy6dDGfuqu4wNRTMEaYCwg";

	public void initFirebase() throws IOException {
		InputStream serviceAccount = new URL(firebaseKey).openStream();
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setDatabaseUrl("https://baba-neonatal-monitoring.firebaseio.com")
				.build();

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
	
	@RequestMapping(value = "/")
	public String index(Model model, HttpSession session) throws IOException {
		String page = "index_bootstrap";		
		if(session.getAttribute("username") != null)
			page = "dashboard_bootstrap";
		if(first) {
			initFirebase();
			first = false;
		}
		return page;
	}

	@RequestMapping(value = "/getRegistered", method = RequestMethod.GET, produces = {"application/json"})
	public ResponseEntity<ArrayList<ArrayList<String>>> getRegistered(){
		ArrayList<ArrayList<String>> output = getUsernames();
		return new ResponseEntity<>(output, HttpStatus.OK);
	}
}
