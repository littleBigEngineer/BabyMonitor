package com.neo.controller;

import java.util.ArrayList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SensorController{

	DatabaseReference ref;
	Double temp = 0.0;
	String coStatus = "";
	ArrayList<ArrayList<Double>> values = new ArrayList<>();
	
	public String getCarbonMonoxideStatus() {
		ref = FirebaseDatabase.getInstance().getReference("Gasses/CO");
		ref.addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				coStatus = snapshot.getValue(String.class);					
			}

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub
				
			}
		});
		return coStatus;
	}
	public Double getTemperatureReading() {
		ref = FirebaseDatabase.getInstance().getReference("Temperature/Current Temp");
		ref.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				temp = dataSnapshot.getValue(Double.class);
//				System.out.println(temp);
			}

			@Override
			public void onCancelled(DatabaseError error) {

			}
		});
		return temp;
	}
}
