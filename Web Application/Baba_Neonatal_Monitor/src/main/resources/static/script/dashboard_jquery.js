$(document).ready(function() {
	var usernames, emails;
	var devices;
	var paused = false;

	getCurrentUser();
	updateSong()
	setInterval(getInformation, 1000);
	setInterval(updateSong, 10000);
	var prevNum = 0;

//	$('#vid_0').hide();
//	$('#vid_1').hide();

//	$('#dv_0').click(function(){
//	$('#vid_0').show();
//	$('#therm_0').hide();
//	$('#sound_0').hide();
//	});

//	$('#dv_1').click(function(){
//	$('#vid_1').show();
//	$('#therm_1').hide();
//	$('#sound_1').hide();
//	});

//	$('#vid_0').click(function(){
//	console.log("click");
//	$('#vid_0').hide();
//	$('#therm_0').show();
//	$('#sound_0').show();
//	});

//	$('#vid_1').click(function(){
//	$('#vid_1').hide();
//	$('#therm_1').show();
//	$('#sound_1').show();
//	});


	function updateSong(){
		var cdata;
		$.ajax({
			url: "/getLullaby",
			dataType: "json",
			method: "get"
		}).done(function(data){
			cdata = data;
			$.each(cdata, function(index, value){
				console.log(cdata);
				$("#curr_" + index).html("<marquee>" + value + "</marquee>");
			})
		});
	}
	
	$("#settings_button").click(function(){
		$.ajax({
			type: "GET",
			url: "/settings"
		})
	});

	$("#prev_0, #prev_1, #prev_2, #prev_3, #prev_4, #prev_5").click(function(event){
		var id = parseInt($(this).prop("id").substr($(this).prop("id").length - 1));
		console.log();
		$.ajax({
			type: "POST",
			url: "/prevSong",
			data: {device: devices[id].split('::')[1]}
		})
	});
	
	$("#next_0, #next_1, #next_2, #next_3, #next_4, #next_5").click(function(event){
		var id = parseInt($(this).prop("id").substr($(this).prop("id").length - 1));
		console.log();
		$.ajax({
			type: "POST",
			url: "/nextSong",
			data: {device: devices[id].split('::')[1]}
		})
	});
	
	$("#play_0, #play_1, #play_2, #play_3, #play_4, #play_5").click(function(event){
		var id = parseInt($(this).prop("id").substr($(this).prop("id").length - 1));
		console.log();
		$.ajax({
			type: "POST",
			url: "/pauseSong",
			data: {device: devices[id].split('::')[1], pause: paused}
		})
		if(!paused){
			$(this).attr("src","../Media/pause.png");
			paused = true;
		}
		else{
			$(this).attr("src","../Media/play.png");
			paused = false;
		}
	});

	function getInformation(){
		var cdata;
		$.ajax({
			url: "/getInformation",
			dataType: "json",
			method: "get"
		}).done(function(data){
			cdata = data;
			$.each(cdata, function(index, value){
				$.each(value, function(indexTwo, valueTwo){
					if(indexTwo == 1){
						$("#deg_" + index).html(Math.round(valueTwo) + "&deg;C");
						if(valueTwo < 15)
							$("#therm_" + index).css("background-color", "#89cff0");
						if(valueTwo > 15 && valueTwo < 25)
							$("#therm_" + index).css("background-color", "#00FF00");
						if(valueTwo > 25)
							$("#therm_" + index).css("background-color", "#F31431");
					}
					if(indexTwo == 2){
						$("#hum_" + index).html("Humidity: " + Math.round(valueTwo) + "%");
						if(valueTwo > 60)
							$("#hum_" + index).css("color", "#F31431");
						else
							$("#hum_" + index).css("color", "#2dc553");
					}
					if(indexTwo == 3){
						if(valueTwo === "Normal"){
							$("#co2_" + index).html("CO<sub>2</sub> Level: Ok");
							$("#co2_" + index).css("color", "#2dc553");
						}
						else{
							$("#co2_" + index).html("CO<sub>2</sub> Level: High");
							$("#co2_" + index).css("color", "#F31431");
						}
					}
					if(indexTwo == 4){
						if(valueTwo === "Normal"){
							$("#co_" + index).html("CO Level: Ok");
							$("#co_" + index).css("color", "#2dc553");
						}
						else{
							$("#co_" + index).html("CO Level: High");
							$("#co_" + index).css("color", "#F31431");
						}
					}
					if(indexTwo == 5){
						$("#db_" + index).html(Math.round(valueTwo) + "dB");

						if(valueTwo > 60)
							$("#sound_" + index).css("background-color", "#F31431");
						else if(valueTwo < 30)
							$("#sound_" + index).css("background-color", "#00FF00");
						else if(valueTwo > 30 && valueTwo < 60)
							$("#sound_" + index).css("background-color", "#ffb347");
					}
					if(indexTwo == 6)
						$("#name_" + index).html(valueTwo.split('::')[0]);

				})
			})
		});
	}

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

	$("#settings").click(function(){
		window.location = window.location + "settings";
	});

	function getDevices(){
		$.ajax({
			url: "/getDevices",
			method: "get",
			success: function(data){
				devices = data;
				cdata = data;
				$.each(cdata, function(index, value) {
					console.log(value);
					$("#dv_" + index).css("visibility","visible");
					$("#name_" + index).html(value);
				});
			}
		});
	}

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
