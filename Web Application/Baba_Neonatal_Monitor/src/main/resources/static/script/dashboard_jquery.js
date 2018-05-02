$(document).ready(function() {
	var usernames, emails;

	getCurrentUser();
//	setInterval(getTemp, 1000);
	var prevNum = 0;

	function getCurrentUser(){
		$.ajax({
			url: "/getCurrentUser",
			method: "get",
			success: function(data){
				$('#usernameTag').html(data);
				getDevices();
			}
		});
	}
	
	$("#logout").click(function(){
		$.ajax({
			url: "/logout",
			method: "post",
			success: function(){
				location.reload();
			}
		});
	});
	
	function getDevices(){
		$.ajax({
			url: "/getDevices",
			method: "get",
			success: function(data){
				cdata = data;
				$.each(cdata, function(index, value) {
					console.log(value);
					$("#dv_" + index).css("visibility","visible");
					$("#name_" + index).html(value);
				});
			}
		});
	}

	$("#devCo").attr('title', 'Carbon Monoxide');
	$('#settingsButton').click(function settingsPage(){
		$.ajax({
			url: "/settings",
			dataType: "json",
			method: "get"
		});
	});

	function fillTherm(num){
		if(num != prevNum){
			prevNum = num;
			var color = "";

			if(num/3 == 1)
				color = "#a2d2df";
			else if(num/3 == 2)
				color = "#44C553";
			else if(num/3 == 3)
				color = "#f00";

			for(i = 1; i <= 9; i++){
				$('#dv1Therm'+ i).css("background-color", "#000");
			}

			for(i = 1; i <= num; i++){
				$('#dv1Therm'+ i).css("background-color", color);
			}
		}
	}
});
