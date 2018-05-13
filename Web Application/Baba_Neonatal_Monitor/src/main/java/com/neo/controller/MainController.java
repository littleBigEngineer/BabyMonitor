package com.neo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
<<<<<<< HEAD
=======
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.amazonaws.services.appstream.model.Session;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.neo.model.Account;
import com.neo.model.Device;
>>>>>>> parent of 3c29281... Fix up

@Controller
public class MainController{

<<<<<<< HEAD
=======
	AccountController ac = new AccountController();
	FirebaseController fc = new FirebaseController();
	DeviceController dc = new DeviceController();
	SensorController sc = new SensorController();
	StorageController storeC = new StorageController();
	Thread mainThread = Thread.currentThread();

	String currentUser = "";
	boolean first = true;

	AmazonS3 s3client;

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
		System.out.println(username + " - " + password);
		Account account = ac.checkForAccount(username, password);
		if(account != null) {
			session.setAttribute("username", account.getUsername());
			System.out.println("New Session: " + username);
		}
		while(account == null) {

		}
		return "index_bootstrap";
	}

>>>>>>> parent of 3c29281... Fix up
	@RequestMapping(value = "/dashboard", method=RequestMethod.GET)
	public String dashboard(HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		System.out.println("USERNAME - " + session.getAttribute("username"));
		return "dashboard_boostrap";
	}

<<<<<<< HEAD
=======
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
	
>>>>>>> parent of 3c29281... Fix up
	@RequestMapping(value = "/getCurrentUser", method = RequestMethod.GET)
	public ResponseEntity<String> getCurrentUser(HttpSession session){
		String user = session.getAttribute("username").toString();
		System.out.println(user);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

<<<<<<< HEAD
=======
	@RequestMapping(value = "/getDeviceAssoc", method = RequestMethod.GET, produces = {"application/json"})
	public ResponseEntity<ArrayList<String>> getDeviceAssoc(HttpSession session){
		ArrayList<String> devices = new ArrayList<>();
		System.out.println(session.getAttribute("username").toString());
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
	
>>>>>>> parent of 3c29281... Fix up
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public @ResponseBody void logout(HttpSession session) {
		session.removeAttribute("username");
	}
<<<<<<< HEAD
=======
	
	@RequestMapping(value="/getDevices", method=RequestMethod.GET)
	public ResponseEntity<ArrayList<String>> getDevices(HttpSession session){
		ArrayList<String> devs = new ArrayList<>();
		ArrayList<Device> devices = dc.getDeviceList(session.getAttribute("username").toString());
		for(Device d : devices) {
			devs.add(d.getDevice_name());
		}
		return new ResponseEntity<>(devs, HttpStatus.OK);
		
	}

	@RequestMapping(value = "/customerdata", method = RequestMethod.GET, produces = {"application/json"})
	public ResponseEntity<ArrayList<String>> getCustomerData(){
		sc.getTemperatureReading();
		ArrayList<String> info = new ArrayList<>();
		int temp = (int) Math.round(sc.getTemperatureReading());
		String message = "" + temp;
		String coStatus = sc.getCarbonMonoxideStatus();

		info.add("Device Name");
		info.add(message);
		info.add(coStatus);

		return new ResponseEntity<>(info, HttpStatus.OK);
	}
>>>>>>> parent of 3c29281... Fix up
}