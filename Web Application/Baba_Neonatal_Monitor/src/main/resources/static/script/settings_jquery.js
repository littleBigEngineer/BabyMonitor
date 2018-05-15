$(document).ready(function() {
	var devices;
	getCurrentUser();
	getDevices();
	var uOne = "";
	var uTwo = "";
	var name = "";

	$("#button_0, #button_1").click(function(){
		console.log("Hellllo");
	});

	$(window).on('shown.bs.modal', function(e){
		$("#uOneField").val(uOne);
		$("#devNameField").val(name);
		if(!uTwo === "---")
			$("#uTwoField").val(uTwo);
	});

	$("#form_sub").click(function(){
		console.log("Here");
		$.ajax({
			type: "POST",
			url: "/updateDeviceName",
			data: {deviceName: $("#devNameField").val()}
		})		
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
		var color;
		var cdata;
		$.ajax({
			url: "/getDeviceAssoc",
			dataType: "json",
			method: "get"
		}).done(function(data){
			cdata = data;
			var index = 0;
			$.each(cdata, function(index, value) {
				var tab = $("<div class='col-sm-2 btn' style='background-color: #89cff0;'>"+value+"</div>");
				if(index == 0)
					populateInformation(value);
				$("#devices").append(tab);
				tab.attr('id', "button_" + index);
				tab.attr('name', value);
				index += 1;
			});
		});
		getFilesForDevice();
	}

	function populateInformation(deviceId){
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
						name = value;
					}
					if(index == 1){
						$("#userOne").html(value);
						uOne = value;
					}
					if(index == 2){
						if(value === "na")
							value = "---";
						$("#userTwo").html(value);
						uTwo = value;
					}
				});
				console.log(data);
			}
		});
	}
});
