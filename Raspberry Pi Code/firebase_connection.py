import pyrebase

config = {"apiKey": "AIzaSyCgj-4Kh-qGt8M9_xIXWzusCPeS-rfpyZk","authDomain": "baba-neonatal-monitoring.firebaseapp.com","databaseURL": "https://baba-neonatal-monitoring.firebaseio.com","projectId": "baba-neonatal-monitoring","storageBucket": "baba-neonatal-monitoring.appspot.com","messagingSenderId": "935817435019"}
firebase = pyrebase.initialize_app(config)
db = firebase.database()
accounts = db.child("Accounts").get()
print(accounts.val())