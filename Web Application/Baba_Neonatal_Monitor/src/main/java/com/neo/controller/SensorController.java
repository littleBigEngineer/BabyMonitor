package com.neo.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@Controller
public class SensorController{

	DatabaseReference ref;
	Double temp = 0.0;
	Double hum = 0.0;
	String coStatus = "";
	String co2Status = "";
	boolean done = false;
	ArrayList<ArrayList<Double>> values = new ArrayList<>();
	
	public String getCarbonMonoxideStatus(String device) {
		done = false;
		ref = FirebaseDatabase.getInstance().getReference("Gasses/" + device + "/CO");
		ref.addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				coStatus = snapshot.getValue(String.class);	
				System.out.println("Carbon Mon: " + coStatus);
				done = true;
			}

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub
				
			}
		});
		
		while(!done) {
			
		}
		
		return coStatus;
	}
	
	public String getCarbonDioxideStatus(String device) {
		done = false;
		ref = FirebaseDatabase.getInstance().getReference("Gasses/" + device + "/CO2");
		ref.addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				co2Status = snapshot.getValue(String.class);	
				System.out.println("Smoke: " + co2Status);
				done = true;
			}

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub
				
			}
		});
		
		while(!done) {
			
		}
		return co2Status;
	}
	
	public Double getTemperatureReading(String device) {
		done = false;
		ref = FirebaseDatabase.getInstance().getReference("Temperature/" + device);
		ref.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				temp = dataSnapshot.getValue(Double.class);
				System.out.println("Temperature: " + temp);
				done = true;
			}

			@Override
			public void onCancelled(DatabaseError error) {

			}
		});
		
		while(!done) {
			
		}
		
		return temp;
	}
	
	public Double getHumidityReading(String device) {
		done = false;
		ref = FirebaseDatabase.getInstance().getReference("Humidity/" + device);
		ref.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				hum = dataSnapshot.getValue(Double.class);
				System.out.println("Humidity: " + hum);
				done = true;
			}

			@Override
			public void onCancelled(DatabaseError error) {

			}
		});
		
		while(!done) {
			
		}
		return hum;
	}
}
