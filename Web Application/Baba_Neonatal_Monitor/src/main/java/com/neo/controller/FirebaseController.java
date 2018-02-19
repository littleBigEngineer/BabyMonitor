package com.neo.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.neo.model.Account;
import com.neo.model.Device;

public class FirebaseController {

	DatabaseReference ref;
	DataSnapshot ds;
	AccountController ac;
	Account account;
	FirebaseApp firebaseApp;
	boolean accountFlag, deviceFlag;
	ArrayList<String> deviceList = new ArrayList<>();
	ArrayList<String> childrenId = new ArrayList<>();
	ArrayList<Device> devices = new ArrayList<>();
	
	public void initFirebase() throws IOException {
		FileInputStream serviceAccount = new FileInputStream("src/main/resources/static/firebase-key.json");

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setDatabaseUrl("https://baba-neonatal-monitoring.firebaseio.com")
				.build();

		firebaseApp = FirebaseApp.initializeApp(options);
		FirebaseAuth.getInstance(firebaseApp);
	}
	
	public void getTemperatureReading() {

	}
	public Account checkForAccount(String username, String password) {
		ref = FirebaseDatabase.getInstance().getReference("Accounts/"+username);
		accountFlag = false;
		ref.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				account = dataSnapshot.getValue(Account.class);
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
	public ArrayList<Device> getDeviceList(String userId){
		devices.removeAll(devices);
		deviceList.removeAll(deviceList);
		childrenId.removeAll(childrenId);
		deviceFlag = false;
		ref = FirebaseDatabase.getInstance().getReference("Device Assoc").child(userId);
		ref.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for(int i = 1; i <= 6; i++) {
                    if(dataSnapshot.hasChild(""+i)) {
                        String device = dataSnapshot.child("" + i).getValue().toString();
                        if(!deviceList.contains(device))
                        	deviceList.add(device);
                    }
                }
				getDevices();
			}

			@Override
			public void onCancelled(DatabaseError error) {
				
			}
		});
		while(!deviceFlag) {
			
		}
		return devices;
	}	
	public void getDevices(){
		ref = FirebaseDatabase.getInstance().getReference("Devices");
		ref.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for (int i = 0; i < deviceList.size(); i++) {
                    Device d = dataSnapshot.child(deviceList.get(i)).getValue(Device.class);
                    d.setId(deviceList.get(i));
                    System.out.println(d.getDevice_name());
                    childrenId.add(d.getChild());
                    devices.add(d);
                }
				deviceFlag = true;
			}

			@Override
			public void onCancelled(DatabaseError error) {
			}
		});
	}
}
