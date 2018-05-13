package com.neo.controller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileReader;

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

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Controller
public class StorageController {

	final String firebaseKey = "https://s3.us-east-2.amazonaws.com/elasticbeanstalk-us-east-2-522520740280/firebase-key.json";
	AmazonS3 s3client;
	String[] access_key;
	BufferedReader br;

	public void initStorage() throws IOException, FileNotFoundException, UnsupportedAudioFileException {


		AWSCredentials credentials = new BasicAWSCredentials("AKIAIPRKFXSZTWDDF4KA","I8/KzpWUvW3aJnCla4dYUPjPYurqNJ7C7j4nrr5r");
		s3client = AmazonS3ClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.EU_WEST_1)
				.build();
	}

	public ObjectListing getListFiles(String bucket) {
		ObjectListing objectListing = s3client.listObjects("baba123bucket");
		return objectListing;			
	}

	public void removeFile(String filename) {
		for (S3ObjectSummary file : s3client.listObjects("baba123bucket", filename).getObjectSummaries()){
			s3client.deleteObject("baba123bucket", file.getKey());
		}
		System.out.println("Deleted " + filename);
	}

	public void createBucket(String bucketName) {
		s3client.createBucket(bucketName);
	}

	public File convertFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		convFile.createNewFile(); 
		FileOutputStream fos = new FileOutputStream(convFile); 
		fos.write(file.getBytes());
		fos.close(); 
		return convFile;
	}

	public void uploadFile(MultipartFile file) throws UnsupportedAudioFileException, IOException {	
		File upload = convertFile(file);
		s3client.putObject(
				"baba123bucket", 
				upload.getName(),  
				upload
				);
	}

	@PostMapping("/upload")
	public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws UnsupportedAudioFileException, IOException {
		System.out.println("Uploaded");
		uploadFile(file);
		return "settings";
	}

	@RequestMapping(value = "/settings")
	public String settings(Model model, HttpSession session) throws IOException, UnsupportedAudioFileException {
		String page = "settings_bootstrap";
		initStorage();

		return page;
	}

	@RequestMapping(value = "/getFiles", method = RequestMethod.GET, produces = {"application/json"})
	public ResponseEntity<ArrayList<String>> getFiles(){
		ArrayList<String> files = new ArrayList<>();
		ObjectListing objectListing = getListFiles("baba123bucket");
		for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
			files.add(os.getKey());
		}		
		return new ResponseEntity<>(files, HttpStatus.OK);
	}

	@RequestMapping(value = "/removeFromLibrary", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody void Submit(@RequestParam("file") String name) {
		removeFile(name);
	}

}
