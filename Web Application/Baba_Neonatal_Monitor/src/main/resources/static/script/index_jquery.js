$(document).ready(function() {
	var alert_red = "#F31431";
	var emails;
	var usernames;
	var alphaNumRegex = /^[0-9a-zA-Z]+$/;

	$('#first_name_error').prop('title', 'First Name must be <u><b>2-25 characters</b></u> in length.');
	$('#last_name_error').prop('title', 'Last Name must be <u><b>2-25 characters</b></u> in length.');
	$('#email_error').prop('title', "E-mail Address must be <u><b>2-40 characters</b></u> in length and <u><b>contain '@' character</b></u>.");
	$('#username_error').prop('title', "Username must be <u><b>2-25 characters</b></u> in length.");
	$('#phone_error').prop('title', "Phone number must be <u><b>10 numbers</b></u> in length.");
	$('#pass_one_error').prop('title', "Passwords must:</br><li>be <u><b>8-25 characters</b></u> in length</li><li>contain an <u><b>uppercase letter</b></u> (A-Z)</li><li>contain a <u><b>number</b></u> (0-9)</li><li>contain a <u><b>non-alphanumeric character</b></u> (!-$_ ...)</li>");
	$('#pass_two_error').prop('title', "Passwords <u><b>do not</b></u> match.");

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
	$("#reg_close").click(function(){
		$("#registerFormClose").css("visibility", "hidden");
		$("#registerPopup").css("visibility", "hidden");
		$('#firstName').val("");
		$('#lastName').val("");
		$('#email').val("");
		$('#phone').val("");
		$('#password').val("");
		$('#passwordConfirm').val("");
		$('#username').val("");

		$('#firstName').css("border-color", "");
		$('#lastName').css("border-color", "");
		$('#email').css("border-color", "");
		$('#phone').css("border-color", "");
		$('#password').css("border-color", "");
		$('#passwordConfirm').css("border-color", "");
		$('#username').css("border-color", "");

		$('#first_name_error').css("visibility","hidden");
		$('#last_name_error').css("visibility","hidden");
		$('#email_error').css("visibility","hidden");
		$('#username_error').css("visibility","hidden");
		$('#phone_error').css("visibility","hidden");
		$('#pass_one_error').css("visibility","hidden");
		$('#pass_two_error').css("visibility","hidden");
	});
	$("#reg_open").click(function(){
		$("#registerPopup").css("visibility", "visible");
		$("#registerFormClose").css("visibility", "hidden");
	})
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
	function registerFormClose(){
		$("#registerPopup").css("visibility", "hidden");
		$("#registerFormClose").css("visibility", "visible");
	}
	function showRegisterPopup(){
		$("#registerPopup").css("visibility", "visible");
		getRegUsers();
	}
	function closeRegisterPopup(){
		registerFormClose();
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
			$("#first_name_error").css("visibility", "visible");
			$('#first_name_error[title]').qtip();
			status = false;
		}
		else{
			$("#firstName").css("border-color", "");
			$("#first_name_error").css("visibility", "hidden");
			status = true;
		}

		if($("#lastName").val().length < 2 || $("#lastName").val().length > 25){
			$("#lastName").css("border-color", "#F31431");
			$("#last_name_error").css("visibility", "visible");
			$('#last_name_error[title]').qtip();
			status = false;
		}
		else{
			$("#lastName").css("border-color", "");
			$("#last_name_error").css("visibility", "hidden");
		}

		if($.inArray($("#email").val(), emails) !== -1 || $("#email").val().length < 2 || $("#email").val().length > 40){
			$("#email").css("border-color", "#F31431");
			$("#email_error").css("visibility", "visible");
			$('#email_error[title]').qtip();
			status = false;
		}
		else{
			$("#email").css("border-color", "");
			$("#email_error").css("visibility", "hidden");
		}
		if($.inArray($("#username").val(), usernames) !== -1 || $("#username").val().length < 2 || $("#username").val().length > 25){
			$("#username").css("border-color", "#F31431");
			$("#username_error").css("visibility", "visible");
			$('#username_error[title]').qtip();
			status = false;
		}
		else{
			$("#username").css("border-color", "");
			$("#username_error").css("visibility", "hidden");
		}
		if($("#phone").val().length != 10){
			$("#phone_error").css("visibility", "visible");
			$("#phone").css("border-color", "#F31431");
			$('#phone_error[title]').qtip();
			status = false;
		}
		else{
			$("#phone").css("border-color", "");
			$("#phone_error").css("visibility", "hidden");
		}
		if(!validPass() || $("#password").val().length < 8 || $("#password").val().length > 20){
			$("#password").css("border-color", "#F31431");
			$("#password_one_error").css("visibility", "visible");
			$('#pass_one_error[title]').qtip();
			status = false;
		}
		else{
			$("#password").css("border-color", "");
			$("#password_one_error").css("visibility", "hidden");
		}
		if($("#password").val() != $("#passwordConfirm").val() || $("#passwordConfirm").val().length === 0){
			$("#pass_two_error").css("visibility", "visible");
			$("#pass_one_error").css("visibility", "visible");
			$('#pass_two_error[title]').qtip();
			$("#password").css("border-color", "#F31431");
			$("#passwordConfirm").css("border-color", "#F31431");
		} else{
			$("#password").css("border-color", "")
			$("#passwordConfirm").css("border-color", "")
			$("#pass_two_error").css("visibility", "hidden");
			$("#pass_one_error").css("visibility", "hidden");
			status = false;
		}
		return status;
	}
});
