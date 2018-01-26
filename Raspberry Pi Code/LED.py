import RPi.GPIO as GPIO
import time
while True:
	GPIO.setmode(GPIO.BCM)
	GPIO.setwarnings(False)
	GPIO.setup(18,GPIO.OUT)
	GPIO.setup(23,GPIO.OUT)

	choice = raw_input("Red or Blue")
	if choice == "B":
		GPIO.output(23,GPIO.LOW)
		GPIO.output(18,GPIO.HIGH)
	if choice == "R":
		GPIO.output(18,GPIO.LOW)
		GPIO.output(23,GPIO.HIGH)
