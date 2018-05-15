import RPi.GPIO as GPIO
import dht11
print("Here")

import time
import datetime

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)
print("Here")

instance = dht11.DHT11(pin=26)

while True:
    print("Again")
    result = instance.read()
    if result.is_valid():
        print("Temp:" + result.temperature)
        print("Hum:" + result.humidity)