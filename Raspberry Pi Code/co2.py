import RPi.GPIO as GPIO
import time
 
GPIO.setmode(GPIO.BCM)
GPIO.setup(18, GPIO.IN)
GPIO.setup(27, GPIO.OUT)
GPIO.setup(23, GPIO.OUT)
 
try:
    while True:
        if GPIO.input(18) and GPIO.input(18) == 1:
			print("Detected Smoke")
			print(GPIO.input(23))
			time.sleep(0.1)
			GPIO.output(23, GPIO.HIGH)
	else:
		GPIO.output(23, GPIO.LOW)

except KeyboardInterrupt:
    GPIO.cleanup()
