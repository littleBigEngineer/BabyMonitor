import RPi.GPIO as GPIO
import time
 
GPIO.setmode(GPIO.BCM)
GPIO.setup(14, GPIO.IN)
GPIO.setup(15, GPIO.OUT)
GPIO.setup(27, GPIO.OUT)
GPIO.setwarnings(False)
 
try:
    while True:
        if GPIO.input(14) and GPIO.input(14)==1:
            print("Detected Gas")
	    print(GPIO.input(14))
	    GPIO.output(15, GPIO.HIGH)
            time.sleep(0.1)
        if GPIO.input(14) == 0:
            GPIO.output(15, GPIO.LOW)
except KeyboardInterrupt:
    GPIO.cleanup()
