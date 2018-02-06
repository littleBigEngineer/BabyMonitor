var firebase, auth, data;

var tempF, tempC;

function initFirebase(){
	var config = {
			apiKey: "AIzaSyCgj-4Kh-qGt8M9_xIXWzusCPeS-rfpyZk",
			authDomain: "baba-neonatal-monitoring.firebaseapp.com",
			databaseURL: "https://baba-neonatal-monitoring.firebaseio.com",
			projectId: "baba-neonatal-monitoring",
			storageBucket: "baba-neonatal-monitoring.appspot.com",
			messagingSenderId: "935817435019"
	};

	firebase.initializeApp(config);

	auth = firebase.auth();
	data = firebase.database();
}

function signInUser(){
	//initFirebase();
	var email = document.getElementById("loginEmail").value;
	var password = document.getElementById("loginPassword").value;

	if(email == "a"){
		email = "robert.crowley1@mycit.ie";
		password = "password1";
	}
	var user = firebase.auth().currentUser;
	firebase.auth().signInWithEmailAndPassword(email, password).catch(function(error) {

	});
	
	var user = firebase.auth().currentUser;
	if(user == null){
		alert("You Failure");
	}
	alert(user.uid);
	window.location.href = "../templates/dashboard.html";
	firebase.auth().signOut();
}

function getTemp(){
	var temp = firebase.database().ref('Temperature/Current Temp');
	temp.on('value', function(snapshot) {
		alert("Hello");
		var val = snapshot.val();
		tempC = Math.round(val);
		tempF = (val*1.8)+32;
		tempF = Math.round(tempF);
		console.log(val);
		console.log(tempC);
		document.getElementById("tempReading").innerHTML = tempC + ' &#8451;';
		alert("Bye");
	});
}