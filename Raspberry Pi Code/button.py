import RPi.GPIO as GPIO
import time

num = 0

GPIO.setmode(GPIO.BCM)
GPIO.setup(18,GPIO.IN,pull_up_down=GPIO.PUD_UP)
GPIO.setup(21,GPIO.OUT)

while True:
	GPIO.output(21,GPIO.HIGH)
	inputValue = GPIO.input(18)
	if inputValue == False:
		num += 1
		print "Button Press", num
	time.sleep(0.3)
