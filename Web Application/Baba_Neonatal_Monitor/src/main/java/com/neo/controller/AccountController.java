package com.neo.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.neo.model.Account;

@Controller
public class AccountController {

	boolean accountFlag, devicesFlag;
	DatabaseReference ref;
	Account account;
	ArrayList<String> devices = new ArrayList<>();

	final String username = "babaneonatal@gmail.com";
	final String password = "NeoN@t@l1";
	
	public void createAccount(Account newAccount) {
		ref = FirebaseDatabase.getInstance().getReference("Accounts/");

		Map<String, Object> account = new HashMap<>();
		account.put(newAccount.getUsername(), newAccount);
		ref.updateChildrenAsync(account);
	}
	
	public ArrayList<String> getAssocDevices(String username) {
		devicesFlag = false;
		devices.removeAll(devices);
		ref = FirebaseDatabase.getInstance().getReference("Device Assoc/" + username);
		ref.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for(DataSnapshot ds: dataSnapshot.getChildren()) {
					devices.add(ds.getValue(String.class));
				}
				devicesFlag = true;
			}

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub
				
			}
		});
		
		while(!devicesFlag) {
			
		}
		
		return devices;
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
		System.out.println(password);
		if(!password.equals(account.getPassword()))
			account = null;

		return account;
	}	

	public void sendEmail(String address, int type) throws UnsupportedEncodingException {

		//0 = sign up
		//1 = forgotten password
		//2 = weekly report

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username, "Baba Neonatal"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("robert.crowley1995@gmail.com"));
			if(type == 0) {
				message.setSubject("Welcome to Baba");
				message.setContent(null);
			}
			else if(type == 1) {
				message.setSubject("Forgotten Password");
				message.setContent("<div style=\"width: 100%; background-color: #ffd357; float: left;\">\r\n<img src=\"https://s3.us-east-2.amazonaws.com/elasticbeanstalk-us-east-2-522520740280/header.png\" style=\"float: left; width: 20%; margin-left: 40%; margin-top: 2.5vh; margin-bottom: 2.5vh;\">\r\n</div>\r\n<div style=\"margin-top: 5vh; background-color: #4cbac0; width: 40%; margin-left: 30%; border: solid #000 thick; float: left;\">\r\n<h1 style=\"font-weight: bolder; width: 100%;text-align: center;\">Forgotten your password?</h1>\r\n<h2 style=\"width: 100%; text-align: center;\">Click the link below</h2>\r\n</div>","text/html");
			}
			else if(type == 2) {
				message.setSubject("Weeky Update");
				message.setContent(null);
			}
			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	@RequestMapping(value = "/getDeviceAssoc", method = RequestMethod.GET)
	public ResponseEntity<ArrayList<String>> getDeviceAssoc(HttpSession session){
		ArrayList<String> devices = new ArrayList<>();
		for(String device : getAssocDevices(session.getAttribute("username").toString())) {
			devices.add(device);
		}
		return new ResponseEntity<>(devices, HttpStatus.OK);
	}
	
	@PostMapping("/registerUser")
	public String handleUserRegistration(@RequestParam("fName") String fName, @RequestParam("lName") String lName, @RequestParam("phone") String phone, @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("username") String username, RedirectAttributes redirectAttributes) throws UnsupportedAudioFileException, IOException {
		System.out.println("New User");
		createAccount(new Account(email, fName, lName, phone, password, username));
		return "index";
	}

	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String handleLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) throws UnsupportedAudioFileException, IOException {
		while(username == null || password == null) {

		}
		Account account = checkForAccount(username, password);
		if(account != null)
			session.setAttribute("username", account.getUsername());
		while(account == null) {

		}
		return "index_bootstrap";
	}
}
