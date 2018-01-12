function showPopup(obj) {
    var item = document.getElementById(obj);
    item.style.visibility = 'visible';
}

function closePopup(obj) {
    var item = document.getElementById(obj);
    item.style.visibility = 'hidden';

    document.getElementById('firstName').value="";
    document.getElementById('lastName').value="";
    document.getElementById('email').value="";
    document.getElementById('phone').value="";
    document.getElementById('password').value="";
    document.getElementById('passwordConfirm').value="";
}

function passDisplay(obj) {
      document.getElementById(obj).type="text";
}

function passHide(obj) {
      document.getElementById(obj).type="password";
}
