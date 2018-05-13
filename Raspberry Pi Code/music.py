import pygame
import requests
import pyrebase
from os import listdir
from os.path import isfile, join

config = {"apiKey": "AIzaSyCgj-4Kh-qGt8M9_xIXWzusCPeS-rfpyZk","authDomain": "baba-neonatal-monitoring.firebaseapp.com","databaseURL": "https://baba-neonatal-monitoring.firebaseio.com","projectId": "baba-neonatal-monitoring","storageBucket": "baba-neonatal-monitoring.appspot.com","messagingSenderId": "935817435019"}
firebase = pyrebase.initialize_app(config)
db = firebase.database()
index = -1

files = [f for f in listdir("music/") if isfile(join("music/", f))]
for f in files:
    print(f)

def main():
	global index
	
	prev_inst = ""

	pygame.mixer.init()
	playing = False 
	pause = False
	
	while True:
		instruction = db.child("Device_Instruction").child("baba123").get()
		instruction = str(instruction.val())

		if instruction == "pause":
			pygame.mixer.music.pause()
			playing = False
			pause = True
		
		if instruction == "next":
			if index == len(files) -1:
				index = 0
			else:
				index = index+1
			pygame.mixer.music.load("music/" + files[index])
			pygame.mixer.music.play()
			playing = True
			instrcution = "play"
			setPlay()

	
		if instruction == "prev":
			index -= 1
			pygame.mixer.music.load("music/" + files[index])
			pygame.mixer.music.play()
			playing = True
			setPlay()
			
		if not playing and instruction == "play":
			if pause:
				pygame.mixer.music.unpause()
				pause = False
				playing = True
				setPlay()
			else:
				pygame.mixer.music.load("music/" + files[index])
				pygame.mixer.music.play()
				playing = True
				db.child("Devices").child("baba123").child("currently_playing").set(files[index])
	

	
def setPlay():
        db.child("Devices").child("baba123").child("currently_playing").set(files[index])
	db.child("Device_Instruction").update({"baba123": "play"})
		

main()