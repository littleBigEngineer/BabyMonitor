
var numDevices = 0;
var devices = []



function addDevice(){
  if(numDevices < 6){
    var newPos = devices.indexOf("");

    var device = document.createElement("DIV");
    device.classList.add('measureBox');

    if(newPos != -1){
      device.setAttribute("id", "device"+newPos);
      devices[newPos] = newPos;
    }
    else
      device.setAttribute("id", "device"+numDevices);

    devices.push(device.id.substr(device.id.length-1));

    var name = document.createElement("DIV");
    name.classList.add('deviceBoxName');
    name.innerHTML = device.id;
    var online = document.createElement("DIV");
    online.classList.add('deviceBoxOnline');
    online.innerHTML = "Currently Online";
    var roomName = document.createElement("DIV");
    roomName.classList.add('deviceBoxDetails');
    roomName.innerHTML = "Room: Conors Room";
    var child = document.createElement("DIV");
    child.classList.add('deviceBoxDetails');
    child.innerHTML = "Child: Conor";
    var childAge = document.createElement("DIV");
    childAge.classList.add('deviceBoxDetails');
    childAge.innerHTML = "Child Age: 2";
    device.appendChild(name);
    device.appendChild(online);
    device.appendChild(roomName);
    device.appendChild(child);
    device.appendChild(childAge);
    device.onmousedown = function(event){
      var num = device.id.substr(device.id.length-1);
      devices[num] = "";
      device.parentNode.removeChild(device);
      numDevices -= 1;

    }
    document.getElementById("content").appendChild(device);
    numDevices += 1;
  }
}
