$(document).ready(function() {
	var devices;
	getCurrentUser();
	getDevices();
	
	$("#device").click(function(){
		console.log("click");
	});

	$("#library").delegate(".del", "click", function() {
		$.ajax({
			type: "POST",
			url: "/removeFromLibrary",
			data: { file: $(this).attr('id') }
		})

		$('#notification').fadeIn();
		$('#notification').text($(this).attr('id') + " Deleted!");

		setTimeout(function(){
			$('#notification').fadeOut();
		}, 2000);

		$(this).closest ('tr').remove();
	});
	
	function getCurrentUser(){
		$.ajax({
			url: "/getCurrentUser",
			method: "get",
			success: function(data){
				$('#usernameTag').html(data);
			}
		});
	}

	$("#back").click(function(){
		$.ajax({
			url: "/"
		});
	});
	
	function getFilesForDevice(){
		var cdata;
		$.ajax({
			url: "/getFiles",
			dataType: "json",
			method: "get"
		}).done(function(data){
			cdata = data;
			$.each(cdata, function(index, value) {
				if(value.length > 50){
					value = value.slice(0,50) + "...";
				}

				if(value === 'lullabies.mp3')
					$('#library tr:last').after("<tr><td class='align-middle'>" + value + "</td></tr>");
				else
					$('#library tr:last').after("<tr><td class='align-middle'>" + value + "<button class='del btn-danger float-right' id='" + value + "'>Delete</button></td></tr>");
			});
		});
	}
	
	function getDeviceId(){
		console.log("here");
	}

	function fileUpload(){

	}

	function getDevices(){
		var cdata;
		$.ajax({
			url: "/getDeviceAssoc",
			dataType: "json",
			method: "get"
		}).done(function(data){
			cdata = data;
			$.each(cdata, function(index, value) {
				var tab;					
				tab = $("<div class='col-sm-2 btn' style='background-color: #89cff0;' onclick='populateInformation(this)'>"+value+"</div>");
				if(index == 0)
					populateInformation(value);
				$("#devices").append(tab);
				tab.attr('id', value);
			});
		});
		getFilesForDevice();
	}
	
	$('#device').on("click", function(){
		console.log("clicked");
	});
	
	function populateInformation(device_this){
		deviceId = $(device_this).attr('id')
		var data = "";
		$.ajax({
		  	type: "POST",
		    url: "/getDeviceInfo",
		    data: {device: deviceId},
		    success: function(data){
		    	$("#deviceId").html(deviceId);
		    	$.each(data, function(index, value){
		    		if(index == 0){
		    			$("#deviceName").html(value);
		    		}
		    		if(index == 1){
		    			$("#userOne").html(value);
		    		}
		    		if(index == 2){
		    			if(value === "na")
		    				value = "---";
		    			$("#userTwo").html(value);
		    		}
		    	});
		    	console.log(data);
		    }
		});
	}
});
