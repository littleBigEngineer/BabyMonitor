$(document).ready(function() {
	var usernames, emails;

	setInterval(getTemp, 500);
	var prevNum = 0;
	$("#devCo").attr('title', 'Carbon Monoxide');

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

	function getTemp(){
		getRegUsers();
		var cdata;
		$.ajax({
			url: "/customerdata",
			dataType: "json",
			method: "get"
		}).done(function(data){
			cdata = data;
			$.each(cdata, function( index, value ) {
				if(index == 0)
					$('#devName').html(value);
				if(index == 1){
					$('#devTemp').html(value + "&deg;C");
					if(parseInt(value) < 15)
						fillTherm(3);
					if(parseInt(value) > 25)
						fillTherm(9);
					if(parseInt(value) > 15 && parseInt(value) < 25)
						fillTherm(6);
				}
				if(index == 2){
					$('#devCo').html("CO Level: " + value);
					if(value === 'High')
						$('#devCo').css("color", "#f00");
					else
						$('#devCo').css("color", "#44C553");
				}
			});
		});
	}
});
