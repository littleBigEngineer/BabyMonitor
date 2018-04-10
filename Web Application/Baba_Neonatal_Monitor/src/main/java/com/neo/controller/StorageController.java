package com.neo.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;

public class StorageController {

	final String firebaseKey = "https://s3.us-east-2.amazonaws.com/elasticbeanstalk-us-east-2-522520740280/firebase-key.json";
	AmazonS3 s3client;

	public void initStorage() throws IOException {
		AWSCredentials credentials = new BasicAWSCredentials("AKIAJVPOLCSCRSIFDFSQ","HawPaNngFOwMuFOQvgZ83lMFcoJnhpqcnHW+MAQD");
		s3client = AmazonS3ClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.US_EAST_2)
				.build();
	}

	public ObjectListing getListFiles(String bucket) {
		ObjectListing objectListing = s3client.listObjects("baba123");
		return objectListing;			
	}

	public void createBucket(String bucketName) {
		s3client.createBucket(bucketName);
	}
//	public class Main {
//		public static void main(String[] argv) throws Exception {

			//		    File f1 = new File("c:/program files/java/jdk1.6.0/bin/demo.mp3");
			//		            AudioInputStream fis =
			//		             AudioSystem.getAudioInputStream(f1);
			//		            System.out.println("File AudioFormat: " + fis.getFormat());
			//		            AudioInputStream ais = AudioSystem.getAudioInputStream(
			//		             AudioFormat.Encoding.PCM_SIGNED,fis);
			//		            AudioFormat af = ais.getFormat();
			//		            // int frmlength = (int)ais.getByteLength();
			//		            System.out.println("AudioFormat: " + af.toString());
			//		             DataLine.Info info = new DataLine.Info(SourceDataLine.class,af);
			//		    Clip clip = (Clip) AudioSystem.getLine(info);
			//		    double durationInSecs = clip.getBufferSize()
			//		        / (clip.getFormat().getFrameSize() * clip.getFormat().getFrameRate());
			//		        System.out.println("duration new : "+durationInSecs);
			//		    

//		}


		public void uploadFile(String filePath) throws UnsupportedAudioFileException, IOException {
			filePath = "C:\\Users\\rober\\Desktop\\Lady Gaga - Poker Face.mp3";
			File file = new File(filePath);
			AudioInputStream fis = AudioSystem.getAudioInputStream(file);
			s3client.putObject(
					"baba123", 
					"hello.txt", 
					new File(filePath)
					);
		}

		//	StorageReference storageRef = FirebaseStorage.getInstance().getReference();
	}
