// 전역 변수
const url = 'http://localhost:8081';
let stompClient;
let newMessages = new Object();
// let messageArray = new Array();
let selectedUser;
let currentUser;
let currentAuthority;
let member = undefined;
let authority = undefined;
//------------------------------------------ 전역 변수 끝



// 서버에 유저 등록하기
const registration = (userName) => {
	console.log("****in registration");
	//let userName = document.getElementById("userName").value;
	
	//currentUser = userName;
	
	$.get(url + "/registration/" + userName, function() {
		// connectToChat();
	}).fail(function(error) {
		if (error.status === 400) {
			alert("이미 로그인이 되어 있습니다.");
		}
	});
	
	connectToChat(userName);
	fetchAll();
	setInterval(fetchAll, 5000);
}

// SockJS로 Stomp에 연결
const connectToChat = (userName) => {
	
	console.log("connecting to chat...")
	let socket = new SockJS(url + '/chat');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {	// fram에 연결 
		console.log("connected to: " + frame);
		console.log("authorityName: " + currentAuthority);
		if(currentAuthority == "ROLE_USER"){
			let tmpSender = "챗봇";
			let tmpSendTime = getCurrentTime();
			let tmpMessage = "조금만 기다려 주세요. 상담자와 곧 연결됩니다.";
		
			render(tmpSender, tmpSendTime, tmpMessage);
		}
		stompClient.subscribe("/topic/messages/" + userName, function(response) { // 이 채널에 연결
			console.log("subscribed address: /topic/messages/" + userName );
			let data = JSON.parse(response.body); // response가 stompClient.subscribe 에서 온 메세지 Json
			if (selectedUser == data.sender) {
				console.log("running render for:" + selectedUser);
				console.log(data.message);
				render(data.sender, data.sendTime, data.message); // 메세지 받기 
			}else if(data.sender=="챗봇"){
				console.log(data.message);
				render(data.sender, data.sendTime, data.message); // 메세지 받기 
			}else {
				console.log("newMessage detected");
				
				// 새로운메세지 임시저장
				newMessages.fromLogin = data.sender;	
				newMessages.sendTime = data.sendTime;
				newMessages.message = data.message;
				
				// 새로운 메세지 알림 by 메세지 개수
				$('#userNameAppender_' + data.sender).
				append('<span id="newMessage_' + data.sender
				 + '" style="color: red"> +1</span>');
				selectUser(data.sender);
			}
		});
	});
}

// 등록된 유저확인
const fetchAll = () => {
	$.get(url + "/fetchAllUsers", function(response) {
		let users = response;
		let usersTemplateHTML = "";
		for (let i = 0; i < users.length; i++) {	// 등록된 유저 리스트 
			if(users[i]!=currentUser){
				usersTemplateHTML = usersTemplateHTML + '<a href="#" onclick="selectUser(\'' + users[i] + '\')"><li class="clearfix">\n' +
				'                <img src="/image/chatting/defaultAvatar.jpg" width="55px" height="55px" alt="avatar" />\n' +
				'                <div class="about">\n' +
				'                    <div id="userNameAppender_' + users[i] + '" class="name" style="color: white;">' + users[i] + '</div>\n' +
				'                    <div class="status">\n' +
				'                        <i class="fa fa-circle online" sytle="color=#42ce5;"></i>\n' +
				'                    </div>\n' +
				'                </div>\n' +
				'            </li></a>';
			}
		}
		$('#usersList').html(usersTemplateHTML);
		
		if(currentAuthority=="ROLE_USER"){
			$('#usersList').attr("style","display:none;");	
		}
	});
}

// 현재 채팅 유저 선택하기 + 새로운 메세지 오면 알림
const selectUser = (userName) => {
	selectedUser = userName;
	//let isNew = document.getElementById("newMessage_" + userName) !== null;
	if (currentAuthority == "ROLE_USER") {
		//console.log(isNew);
		//let element = document.getElementById("newMessage_" + userName);
		//console.log(element);
		//element.parentNode.removeChild(element);
		//console.log(element.parentNode);
		console.log(newMessages.sender);
		console.log(newMessages.sendTime);
		console.log(newMessages.message);
		
		
		render(userName, newMessages.sendTime, newMessages.message );
	}
	console.log("selected user: " + selectedUser);

	$('#selectedUserId').html('');
	$('#selectedUserId').append("상대 채팅 유저: " + selectedUser);
}













