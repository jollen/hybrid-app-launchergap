(function($) {
var ws;
var content;

function onWsMessage(message) {
   //alert(message.data);

   var json = JSON.parse(message.data);

   if (json.type === 'message') {
   	content.prepend('<p>' + json.data.message + '</p>');
   }
}

$.fn.receiveWebSocket = function () {
     content = this;

     ws.onmessage = onWsMessage;
};

$.fn.sendMessage = function () {
	$(this).click(function() {
    	ws.send("[message]");
	});
};

$.fn.createWebSocket = function () {
	
  content = this;
  
  if ("WebSocket" in window)
  {
     // Let us open a web socket
     ws = new WebSocket("ws://metromenu.me:8080/", ['echo-protocol']);
     ws.onopen = function()
     {
		content.append("<h2>open</h2>");
     };

     ws.onclose = function()
     { 
		content.append("<h2>closed</h2>");
     };
     
     ws.onerror = function()
     { 
		content.append("<h2>error</h2>");
     };
  }
  else
  {
     // The browser doesn't support WebSocket
     alert("WebSocket NOT supported by your Browser!");
  }
};

})($);
