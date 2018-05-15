import RPi.GPIO as GPIO
import time
import pyrebase

config = {"apiKey": "AIzaSyCgj-4Kh-qGt8M9_xIXWzusCPeS-rfpyZk","authDomain": "baba-neonatal-monitoring.firebaseapp.com","databaseURL": "https://baba-neonatal-monitoring.firebaseio.com","projectId": "baba-neonatal-monitoring","storageBucket": "baba-neonatal-monitoring.appspot.com","messagingSenderId": "935817435019"}
firebase = pyrebase.initialize_app(config)
db = firebase.database()
 
GPIO.setmode(GPIO.BCM)
GPIO.setup(14, GPIO.IN)
GPIO.setup(15, GPIO.OUT)
GPIO.setup(27, GPIO.OUT)
GPIO.setwarnings(False)
 
try:
    while True:
        if GPIO.input(14) and GPIO.input(14)==1:
	    GPIO.output(15, GPIO.HIGH)
            time.sleep(0.1)
            db.child("Devices").child("baba123").child("carbon_monoxide").set("High")
        if GPIO.input(14) == 0:
            GPIO.output(15, GPIO.LOW)
            db.child("Devices").child("baba123").child("carbon_monoxide").set("Normal")
except KeyboardInterrupt:
    GPIO.cleanup()
