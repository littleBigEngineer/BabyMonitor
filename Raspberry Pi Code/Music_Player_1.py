import pygame
import requests

url = "http://s3-eu-west-1.amazonaws.com/baba-music/lullabies.mp3"

pygame.mixer.init()
r = requests.get(url,stream=True)
pygame.mixer.music.load(r.raw)
pygame.mixer.music.play()
while pygame.mixer.music.get_busy() == True:
    continue