$(document).ready(function() {

	getDevices();

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
				tab = $("<button class='col-sm-2 btn' style='background-color: #89cff0;'>"+value+"</button>");
				$("#devices").append(tab);
				tab.attr('id', value);
			});
		});
		getFilesForDevice();
	}
});
