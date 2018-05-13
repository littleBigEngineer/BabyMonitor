package com.neo.controller;

import java.util.ArrayList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.neo.model.Device;

public class DeviceController {
	
	boolean deviceFlag;
	DatabaseReference ref;
	
	ArrayList<String> deviceList = new ArrayList<>();
	ArrayList<Device> devices = new ArrayList<>();
	
	public void getDevices(){
		devices.removeAll(devices);
		ref = FirebaseDatabase.getInstance().getReference("Devices");
		ref.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for (int i = 0; i < deviceList.size(); i++) {
                    Device d = dataSnapshot.child(deviceList.get(i)).getValue(Device.class);
                    d.setDevice_id(deviceList.get(i));
                    devices.add(d);
                }
				deviceFlag = true;
			}

			@Override
			public void onCancelled(DatabaseError error) {
			}
		});
	}
	
	public ArrayList<Device> getDeviceList(String userId){
		devices.removeAll(devices);
		deviceList.removeAll(deviceList);
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
}
