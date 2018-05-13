package com.neo.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.neo.model.Device;

@Controller
public class DeviceController {
	
	ArrayList<ArrayList<String>> information = new ArrayList<>();
	ArrayList<Device> devicesList = new ArrayList<>();
	
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
				for(String dev : deviceList) {
					Device d = dataSnapshot.child(dev).getValue(Device.class);
					d.setDevice_id(dev);
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
	
	@RequestMapping(value = "/getDeviceInfo", method = RequestMethod.POST)
	public ResponseEntity<ArrayList<String>> getDeviceInfo(@RequestParam("device") String device, HttpSession session){
		devicesList.removeAll(devicesList);
		devices = getDeviceList(session.getAttribute("username").toString());
		ArrayList<String> info = new ArrayList<>();
		for(int i = 0; i < devices.size(); i++) {
			if(devices.get(i).getDevice_id().equals(device)) {
				info.add(devices.get(i).getDevice_name());
				info.add(devices.get(i).getUser_one());
				info.add(devices.get(i).getUser_two());
			}
		}
		return new ResponseEntity<>(info, HttpStatus.OK);
	} 
	
	@RequestMapping(value = "/getInformation", method = RequestMethod.GET, produces = {"application/json"})
	public ResponseEntity<ArrayList<ArrayList<String>>> getInformation(HttpSession session){
		information.removeAll(information);
		devicesList.removeAll(devicesList);

		devices = getDeviceList(session.getAttribute("username").toString());

		System.out.println(devicesList.size());
		for(int i = 0; i < devicesList.size(); i++) {
			ArrayList<String> info = new ArrayList<>();
			info.add(""+devices.get(i).getActive()); //0
			info.add(""+devices.get(i).getTemperature()); //1
			info.add(""+devices.get(i).getHumidity()); //2
			info.add(devices.get(i).getCarbon_dioxide()); //3
			info.add(devices.get(i).getCarbon_monoxide()); //4
			info.add("" + devices.get(i).getSound()); //5
			info.add(devices.get(i).getDevice_name()); //6
			information.add(info);
		}
		return new ResponseEntity<>(information, HttpStatus.OK);
	} 
	
	@RequestMapping(value = "/getLullaby", method = RequestMethod.GET, produces = {"application/json"})
	public ResponseEntity<ArrayList<String>> getLullaby(HttpSession session){
		ArrayList<String> info = new ArrayList<>();
		info.removeAll(info);
		devices.removeAll(devices);
		devices = getDeviceList(session.getAttribute("username").toString());
		for(int i = 0; i < devices.size(); i++) {
			info.add(devices.get(i).getCurrently_playing());
		}
		return new ResponseEntity<>(info, HttpStatus.OK);
	} 
	
	@RequestMapping(value="/getDevices", method=RequestMethod.GET)
	public ResponseEntity<ArrayList<String>> getDevices(HttpSession session){
		ArrayList<String> devs = new ArrayList<>();
		ArrayList<Device> devices = getDeviceList(session.getAttribute("username").toString());
		for(int i = 0; i < devices.size(); i++) {
			devs.add(devices.get(i).getDevice_name());
		}
		return new ResponseEntity<>(devs, HttpStatus.OK);

	}
}
