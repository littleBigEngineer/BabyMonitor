import sys
import Adafruit_DHT
import time

while True:
	humidity, temp = Adafruit_DHT.read_retry(11, 24)
	print(humidity)

