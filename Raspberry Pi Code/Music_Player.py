import vlc

print("Now Playing")
music = vlc.MediaPlayer("https://3-eu-west-1.amazonaws.com/baba-music/lullaby.wav")
music.play()
print(music.get_state())
print(music.get_state())
print(music.get_state())
print(music.get_state())
print(music.get_state())
print(music.get_state())