$(document).ready(function() {
	var alert_red = "#F31431";
	var emails;
	var usernames;
	var alphaNumRegex = /^[0-9a-zA-Z]+$/;


	function getRegUsers(){
		var cdata;
		$.ajax({
			url: "/getRegistered",
			dataType: "json",
			method: "get"
		}).done(function(data){
			cdata = data;
			$.each(cdata, function(index, value) {
				if(index == 0){
					console.log(value);
					usernames = value;
				}
				if(index == 1){
					console.log(value);
					emails = value;
				}
			});
		});
	}

	$('#registerForm').on('submit', function(e) {
		e.preventDefault();
		var status = validateInput();
		if(status){
			console.log("All Good!")
		}
		// $.ajax({
		//     url : $(this).attr('action') || window.location.pathname,
		//     type: "GET",
		//     data: $(this).serialize(),
		//     success: function (data) {
		//         $("#form_output").html(data);
		//     },
		//     error: function (jXHR, textStatus, errorThrown) {
		//         alert(errorThrown);
		//     }
		// });
	});

	$("#reg-user").click(showRegisterPopup);
	$("#reg-user").css('cursor','pointer');
	$('#reg-user-close').click(closeRegisterPopup);
	$('#pass-img').click(function(){
		id= "#" + this.id;
		var type = $("#"+this.id).attr('type');
		console.log(id + " " + type);

		if(type == "password"){
			document.getElementById(id).type = 'text';
			console.log("changed to text");
		}
		if(type == "text"){
			document.getElementById(id).type = 'password';
			console.log("changed to password");
		}
	});
	function showRegisterPopup(){
		$("#registerPopup").css("visibility", "visible");
		getRegUsers();
	}

	function passDisplay(id){
		document.getElementById(id).type = 'text';
	}

	function passHide(id){
		document.getElementById(id).type = 'password';
	}

	function togglePassword(){

	}

	function closeRegisterPopup(){
		if (confirm('All details will be lost if you close this form. Proceed? This is Temporary!')) {
			$("#registerPopup").css("visibility", "hidden");
			$('#firstName').val("");
			$('#lastName').val("");
			$('#email').val("");
			$('#phone').val("");
			$('#password').val("");
			$('#passwordConfirm').val("");
			$('#passwordConfirm').val("");
			$('#username').val("");
		}
	}

	function validPass(){
		var num = false;
		var capital = false;
		var symb = false;

		for (var i=0; i < $("#password").val().length; i++) {
			if(!isNaN($("#password").val().charAt(i)))
				num = true;
			if($("#password").val().charAt(i).match(alphaNumRegex) && isNaN($("#password").val().charAt(i)) && $("#password").val().charAt(i) == $("#password").val().charAt(i).toUpperCase())
				capital = true;
			if(!$("#password").val().charAt(i).match(alphaNumRegex))
				symb = true;
		}

		if(num && capital && symb)
			return true;
	}

	function validateInput(){
		var status = true;
		if($("#firstName").val().length < 2 || $("#firstName").val().length > 25){
			$("#firstName").css("border-color", "#F31431");
			status = false;
		}
		else{
			$("#firstName").css("border-color", "");
			status = true;
		}


		if($("#lastName").val().length < 2 || $("#lastName").val().length > 25){
			$("#lastName").css("border-color", "#F31431");
			status = false;
		}
		else
			$("#lastName").css("border-color", "");


		if($.inArray($("#email").val(), emails) !== -1 || $("#email").val().length < 2 || $("#email").val().length > 40){
			$("#email").css("border-color", "#F31431");
			status = false;
		}
		else
			$("#email").css("border-color", "");

		if($.inArray($("#username").val(), usernames) !== -1 || $("#username").val().length < 2 || $("#username").val().length > 25){
			$("#username").css("border-color", "#F31431");
			status = false;
		}
		else
			$("#username").css("border-color", "");

		if($("#phone").val().length != 10){
			$("#phone").css("border-color", "#F31431");
			status = false;
		}
		else
			$("#phone").css("border-color", "");

		if(!validPass() || $("#password").val().length < 8 || $("#password").val().length > 20){
			$("#password").css("border-left-color", "#F31431");
			$("#password").css("border-top-color", "#F31431");
			$("#password").css("border-bottom-color", "#F31431");
			$("#pass-img").css("border-right-color", "#F31431");
			$("#pass-img").css("border-top-color", "#F31431");
			$("#pass-img").css("border-bottom-color", "#F31431");
			status = false;
		}
		else{
			$("#password").css("border-color", "");
			$("#pass-img").css("border-color", "");
		}
		if($("#password").val() == $("#passwordConfirm").val()){
			$("#password").css("border-left-color", "#F31431");
			$("#password").css("border-top-color", "#F31431");
			$("#password").css("border-bottom-color", "#F31431");
			$("#passwordConfirm").css("border-color", "#F31431");
		} else{
			$("#password").css("border-color", "")
			$("#passwordConfirm").css("border-color", "")
			status = false;
		}
		return status;
	}
});
