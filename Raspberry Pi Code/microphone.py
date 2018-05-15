from Adafruit_ADS1x15 import ADS1x15

from time import sleep
 
import math, signal, sys, os
import RPi.GPIO as GPIO
GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
 
delayTime = 0.5
  
ADS1015 = 0x00
ADS1115 = 0x01
 
gain = 4096 
# gain = 2048  # +/- 2.048V
# gain = 1024  # +/- 1.024V
# gain = 512   # +/- 0.512V
# gain = 256   # +/- 0.256V
 
sps = 64 
 
adc_channel_0 = 0 
adc_channel_1 = 1  
adc_channel_2 = 2 
adc_channel_3 = 3
 
adc = ADS1x15(ic=ADS1115)
 
Digital_PIN = 25
GPIO.setup(Digital_PIN, GPIO.IN, pull_up_down = GPIO.PUD_OFF)
  
try:
        while True:
                analog = adc.readADCSingleEnded(adc_channel_0, gain, sps)
  
                if GPIO.input(Digital_PIN) == False:
                        print "Analog voltage value:", analog,"mV, ","extreme value: not reached"
                else:
                        print "Analog voltage value:", analog, "mV, ", "extreme value: reached"
                print "---------------------------------------"
  
                sleep(delayTime)
  
  
  
except KeyboardInterrupt:
        GPIO.cleanup()