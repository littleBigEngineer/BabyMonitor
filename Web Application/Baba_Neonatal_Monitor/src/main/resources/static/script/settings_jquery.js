$(document).ready(function() {

	getDevices();
	getFilesForDevice();

	function getFilesForDevice(){
		var cdata;
		$.ajax({
			url: "/getFiles",
			dataType: "json",
			method: "get"
		}).done(function(data){
			cdata = data;
			$.each(cdata, function(index, value) {
				console.log(value);
				if(value.length > 15)
					value = value.slice(0,15) + "...";
				$('#library').after('<tr><th width="50%" class="track cell">' + value + '</th></tr>');
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
				if(index == 0)
					tab = $("<div class='sidebar_tab selected_tab'>" + value + "</div>");
				else
					tab = $("<div class='sidebar_tab'>" + value + "</div>");
				$("#sidebar").append(tab);
				tab.attr('id', value);
			});
		});
	}
});
