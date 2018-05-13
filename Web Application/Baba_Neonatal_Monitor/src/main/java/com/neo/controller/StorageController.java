package com.neo.controller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class StorageController {

	final String firebaseKey = "https://s3.us-east-2.amazonaws.com/elasticbeanstalk-us-east-2-522520740280/firebase-key.json";
	AmazonS3 s3client;

	public void initStorage() throws IOException, UnsupportedAudioFileException {

		AWSCredentials credentials = new BasicAWSCredentials("AKIAINIYX46W64AI3GQA","tPDaKHMIk+YVwgSpqckL8FxnIoGeMwXb+sCbmVDi");
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
}
