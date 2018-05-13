$(document).ready(function() {
	var alert_red = "#F31431";
	var emails;
	var usernames;
	var alphaNumRegex = /^[0-9a-zA-Z]+$/;

	getRegUsers();
	setInterval(getRegUsers, 30000);

	$('#first_name_error').prop('title', 'First Name must be <u><b>2-25 characters</b></u> in length.');
	$('#last_name_error').prop('title', 'Last Name must be <u><b>2-25 characters</b></u> in length.');
	$('#email_error').prop('title', "E-mail Address must be <u><b>2-40 characters</b></u> in length and <u><b>contain '@' character</b></u>.");
	$('#username_error').prop('title', "Username must be <u><b>2-25 characters</b></u> in length.");
	$('#phone_error').prop('title', "Phone number must be <u><b>10 numbers</b></u> in length.");
	$('#pass_one_error').prop('title', "Passwords must:</br><li>be <u><b>8-25 characters</b></u> in length</li><li>contain an <u><b>uppercase letter</b></u> (A-Z)</li><li>contain a <u><b>number</b></u> (0-9)</li><li>contain a <u><b>non-alphanumeric character</b></u> (!-$_ ...)</li>");
	$('#pass_two_error').prop('title', "Passwords <u><b>do not</b></u> match.");
	$('#loginPassword').prop('title', "Password may be incorrect.");
	$('#loginUsername').prop('title', "Username may be incorrect.");

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
					usernames = value;
				}
				if(index == 1){
					emails = value;
				}
			});
		});
	}

	$('#loginForm').on('submit', function(e) {
		if($("#loginUsername").val().length == 0 || $("#loginPassword").val().length == 0 ){
			e.preventDefault();
			$("#passError").css("visibility","visible");
			window.setTimeout(function(){ 
				$("#passError").css("visibility","hidden");
			},3000);
		}
		else{
			e.preventDefault();
			$.ajax({
		  	type: "GET", 
		    url: "/login",
		    data: {username: $("#loginUsername").val(), password: $("#loginPassword").val()}
			})
			$('#loadingModal').modal('toggle');
			window.setTimeout(function(){
				location.reload()},3000);
		}
	});

	$('#registerForm').on('submit', function(e) {
		e.preventDefault();
		var status = validateInput();
		if(status){
			$.ajax({
		  	type: "POST",
		    url: "/registerUser",
		    data: {fName: $("#firstName").val(), lName: $("#lastName").val(), email: $("#email").val(), phone: $("#phone").val(), password: $("#password").val(), username: $("#username").val()}
			})
			getRegUsers();
			$('#registerModal').modal('toggle');
			$("#createdAccount").css("visibility","visible");
			window.setTimeout( function(){ 
				$("#createdAccount").css("visibility","hidden");
			},3000 );
		}
	});

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
		console.log(status);
		if($("#firstName").val().length < 2 || $("#firstName").val().length > 25){
			$("#firstName").css("border-color", "#F31431");
			$("#first_name_error").css("visibility", "visible");
			$('#first_name_error[title]').qtip();
			$("#firstName").val("");
			status = false;
		}
		else{
			$("#firstName").css("border-color", "");
			$("#first_name_error").css("visibility", "hidden");
			status = true;
		}

		if($("#lastName").val().length < 2 || $("#lastName").val().length > 25){
			$("#lastName").css("border-color", "#F31431");
			$("#lastName").val("");
			$("#last_name_error").css("visibility", "visible");
			$('#last_name_error[title]').qtip();
			status = false;
		}
		else{
			$("#lastName").css("border-color", "");
			$("#last_name_error").css("visibility", "hidden");
			status = true;
		}

		if($.inArray($("#email").val(), emails) !== -1 || $("#email").val().length < 2 || $("#email").val().length > 40){
			$("#email").css("border-color", "#F31431");
			$("#email_error").css("visibility", "visible");
			$('#email_error[title]').qtip();
			$("#email").val("");
			status = false;
		}
		else{
			$("#email").css("border-color", "");
			$("#email_error").css("visibility", "hidden");
			status = true;
		}
		if($.inArray($("#username").val(), usernames) !== -1 || $("#username").val().length < 2 || $("#username").val().length > 25 || $("#username").val().indexOf(" ") != -1){
			$("#username").css("border-color", "#F31431");
			$("#username").val("");
			$("#username_error").css("visibility", "visible");
			$('#username_error[title]').qtip();
			status = false;
		}
		else{
			$("#username").css("border-color", "");
			$("#username_error").css("visibility", "hidden");
			status = true;
		}
		if($("#phone").val().length != 10){
			$("#phone_error").css("visibility", "visible");
			$("#phone").css("border-color", "#F31431");
			$('#phone_error[title]').qtip();
			$("#phone").val("");
			status = false;
		}
		else{
			$("#phone").css("border-color", "");
			$("#phone_error").css("visibility", "hidden");
			status = true;
		}
		if(!validPass() || $("#password").val().length < 8 || $("#password").val().length > 20){
			$("#password").css("border-color", "#F31431");
			$("#password_one_error").css("visibility", "visible");
			$('#pass_one_error[title]').qtip();
			status = false;
			$("#password").val("");
		}
		else{
			$("#password").css("border-color", "");
			$("#password_one_error").css("visibility", "hidden");
			status = true;
		}
		if($("#password").val() != $("#passwordConfirm").val() || $("#passwordConfirm").val().length === 0){
			$("#pass_two_error").css("visibility", "visible");
			$("#pass_one_error").css("visibility", "visible");
			$('#pass_two_error[title]').qtip();
			$("#passwordConfirm").val("");
			$("#password").css("border-color", "#F31431");
			$("#passwordConfirm").css("border-color", "#F31431");
			status = false;
		} else{
			$("#password").css("border-color", "")
			$("#passwordConfirm").css("border-color", "")
			$("#pass_two_error").css("visibility", "hidden");
			$("#pass_one_error").css("visibility", "hidden");
			status = true;
		}

		return status;
	}

});
