package com.neo.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.neo.model.Account;
import com.neo.model.Device;

@Controller
public class MainController{

	AccountController ac = new AccountController();
	FirebaseController fc = new FirebaseController();
	DeviceController dc = new DeviceController();
	SensorController sc = new SensorController();
	StorageController storeC = new StorageController();

	ArrayList<ArrayList<String>> information = new ArrayList<>();
	ArrayList<Device> devices = new ArrayList<>();

	String currentUser = "";
	boolean first = true;

	AmazonS3 s3client;


	@RequestMapping(value = "/getDeviceInfo", method = RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<ArrayList<String>> getDeviceInfo(@RequestParam("device") String device, HttpSession session){

		devices.removeAll(devices);
		devices = dc.getDeviceList(session.getAttribute("username").toString());
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
		devices.removeAll(devices);

		devices = dc.getDeviceList(session.getAttribute("username").toString());

		System.out.println(devices.size());
		for(int i = 0; i < devices.size(); i++) {
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

		devices = dc.getDeviceList(session.getAttribute("username").toString());
		for(int i = 0; i < devices.size(); i++) {
			info.add(devices.get(i).getCurrently_playing());
		}
		return new ResponseEntity<>(info, HttpStatus.OK);
	} 


	@PostMapping("/upload")
	public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws UnsupportedAudioFileException, IOException {
		System.out.println("Uploaded");
		storeC.uploadFile(file);
		return "settings";
	}

	@PostMapping("/registerUser")
	public String handleUserRegistration(@RequestParam("fName") String fName, @RequestParam("lName") String lName, @RequestParam("phone") String phone, @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("username") String username, RedirectAttributes redirectAttributes) throws UnsupportedAudioFileException, IOException {
		System.out.println("New User");
		ac.createAccount(new Account(email, fName, lName, phone, password, username));
		return "index";
	}

	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String handleLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) throws UnsupportedAudioFileException, IOException {
		while(username == null || password == null) {

		}
		Account account = ac.checkForAccount(username, password);
		if(account != null)
			session.setAttribute("username", account.getUsername());
		while(account == null) {

		}
		return "index_bootstrap";
	}

	@RequestMapping(value = "/dashboard", method=RequestMethod.GET)
	public String dashboard(HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		return "dashboard_bootstrap";
	}

	@RequestMapping(value = "/")
	public String index(Model model, HttpSession session) throws IOException {
		String page = "index_bootstrap";		
		if(session.getAttribute("username") != null)
			page = "dashboard_bootstrap";
		if(first) {
			fc.initFirebase();
			first = false;
		}
		return page;
	}

	@RequestMapping(value = "/settings")
	public String settings(Model model) throws IOException, UnsupportedAudioFileException {
		storeC.initStorage();
		return "settings_bootstrap";
	}

	@RequestMapping(value = "/getRegistered", method = RequestMethod.GET, produces = {"application/json"})
	public ResponseEntity<ArrayList<ArrayList<String>>> getRegistered(){
		ArrayList<ArrayList<String>> output = fc.getUsernames();
		return new ResponseEntity<>(output, HttpStatus.OK);
	}

	@RequestMapping(value = "/getCurrentUser", method = RequestMethod.GET)
	public ResponseEntity<String> getCurrentUser(HttpSession session){
		String user = session.getAttribute("username").toString();
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/getDeviceAssoc", method = RequestMethod.GET, produces = {"application/json"})
	public ResponseEntity<ArrayList<String>> getDeviceAssoc(HttpSession session){
		ArrayList<String> devices = new ArrayList<>();
		for(String device : ac.getAssocDevices(session.getAttribute("username").toString())) {
			devices.add(device);
		}
		return new ResponseEntity<>(devices, HttpStatus.OK);
	}

	@RequestMapping(value = "/getFiles", method = RequestMethod.GET, produces = {"application/json"})
	public ResponseEntity<ArrayList<String>> getFiles(){
		ArrayList<String> files = new ArrayList<>();
		ObjectListing objectListing = storeC.getListFiles("baba123");
		for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
			files.add(os.getKey());
		}		
		return new ResponseEntity<>(files, HttpStatus.OK);
	}

	@RequestMapping(value = "/removeFromLibrary", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody void Submit(@RequestParam("file") String name) {
		storeC.removeFile(name);
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public @ResponseBody void logout(HttpSession session) {
		session.removeAttribute("username");
	}

	@RequestMapping(value="/getDevices", method=RequestMethod.GET)
	public ResponseEntity<ArrayList<String>> getDevices(HttpSession session){
		ArrayList<String> devs = new ArrayList<>();
		ArrayList<Device> devices = dc.getDeviceList(session.getAttribute("username").toString());
		for(int i = 0; i < devices.size(); i++) {
			devs.add(devices.get(i).getDevice_name());
		}
		return new ResponseEntity<>(devs, HttpStatus.OK);

	}
}