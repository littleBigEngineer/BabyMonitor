import RPi.GPIO as GPIO
import time
 
GPIO.setmode(GPIO.BCM)
GPIO.setup(23, GPIO.IN)
GPIO.setup(27, GPIO.OUT)
GPIO.setup(21, GPIO.OUT)
 
try:
    while True:
	#print("Current Detection" + str(GPIO.input(23)))
        if GPIO.input(23) and GPIO.input(23)!=1:
        	print("Detected Smoke")
		print(GPIO.input(23))
         	GPIO.output(27, False)
         	time.sleep(0.1)
         	GPIO.output(27, True)
		GPIO.output(21, GPIO.LOW)
	else:
		GPIO.output(21, GPIO.HIGH)

except KeyboardInterrupt:
    GPIO.cleanup()
