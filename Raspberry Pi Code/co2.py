import RPi.GPIO as GPIO
import time
import pyrebase

 
GPIO.setmode(GPIO.BCM)
GPIO.setup(18, GPIO.IN)
GPIO.setup(27, GPIO.OUT)
GPIO.setup(23, GPIO.OUT)
reading = 0

config = {"apiKey": "AIzaSyCgj-4Kh-qGt8M9_xIXWzusCPeS-rfpyZk","authDomain": "baba-neonatal-monitoring.firebaseapp.com","databaseURL": "https://baba-neonatal-monitoring.firebaseio.com","projectId": "baba-neonatal-monitoring","storageBucket": "baba-neonatal-monitoring.appspot.com","messagingSenderId": "935817435019"}
firebase = pyrebase.initialize_app(config)
db = firebase.database()
 
try:
    while True:
        #if GPIO.input(18) and GPIO.input(18) == 1:
		if reading == 1:
			time.sleep(0.1)
			GPIO.output(23, GPIO.HIGH)
			db.child("Devices").child("baba123").child("carbon_dioxide").set("High")
		else:
			GPIO.output(23, GPIO.LOW)
			db.child("Devices").child("baba123").child("carbon_dioxide").set("Normal")

except KeyboardInterrupt:
    GPIO.cleanup()
