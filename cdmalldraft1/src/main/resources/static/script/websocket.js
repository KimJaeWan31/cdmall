$(function() {
	let websocket;
	function connect() {
		if(websocket==undefined) {
			websocket = new WebSocket("ws://localhost:8081/web/socket");
			console.log(websocket);
		}
		$.ajax("/memos/unread").done((result)=> $("#msg").text("읽지 않은 메모 : " + result));
	}

	connect();
	websocket.onmessage = function (e) {
		// 자바스크립트 객체일수도 아닐수도 있다
		const message = JSON.parse(e.data);
		toastr.options={"progressBar": true};
		
		if(message.title=="메모 도착")
			toastr.success(message.content, message.sender + "님의 메시지");
		else if(message.title=="읽지않은 메모")
			$("#msg").text("읽지 않은 메모 : " + message.content);	
	}
});