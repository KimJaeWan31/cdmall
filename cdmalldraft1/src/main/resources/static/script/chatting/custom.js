window.onbeforeunload = function(event) {
	$.ajax("/deregistraion/" + currentUser);
};

/*window.addEventListener('beforeunload', (event) => { 
	// 명세에 따라 preventDefault는 호출해야하며, 기본 동작을 방지합니다. 
	event.preventDefault(); // 대표적으로 Chrome에서는 returnValue 설정이 필요합니다.
	event.returnValue = ''; 
});*/


let $chatHistory;
let $button;
let $textarea;
let $chatHistoryList;





// 초기화작업(수정본)
$(document).ready(() => {
	
	cacheDOM();
	bindEvents(); 	// addMessage, saveMessage, addMessageEnter 이벤트
	getMemberInfo();	// 현재 유저 정보 가져오기
	document.onkeydown = doNotReload;	// 새로고침 방지
	

});

/*function closePage(event) {
	alert("asdfasdf");
	deregister();
}*/

// 임시태그 저장소
const cacheDOM = () => {
	$chatHistory = $('.chat-history');
	$button = $('#sendBtn');
	$exitbutton = $('#exitBtn');
	$savebutton = $('#saveBtn');
	$textarea = $('#message-to-send');
	$chatHistoryList = $chatHistory.find('ul');
}

// 바인드이벤트 - 메세지 보내기 이벤트
const bindEvents = () => {
	$button.on('click', addMessage.bind(this));
	$exitbutton.on('click', exitMessage.bind(this));
	$savebutton.on('click', saveMessage.bind(this));
	$textarea.on('keyup', addMessageEnter.bind(this));
}

// 유저정보 가져온 후 UserStorage에 등록후 Stomp에 연결
const getMemberInfo = () => {
	$.ajax("/message/memberinfo").done(result => {
		member = result;
		
		currentUser = member.username;
		
		getAuthorityInfo(member);
		
	});
}

// 유저의 권한 정보 가져오기
const getAuthorityInfo = (member) => {
	$.ajax({
		url: "/message/authorityinfo",
		method: "get",
		data: member
	}).done(result => {
		authority = result;
		currentAuthority = authority.authorityName;
		
		if(authority.authorityName == "ROLE_USER"){ 
			$('#people-list').attr("style","display:none;");
			$('#container').attr("style","width:450px;");
			/*$('#noRightClick').attr("style","width:450px;");*/
			
		}
		
		printInfo();
		
		registration(member.username);
		// 일반유저라면 마우스 우클릭 금지
		/*if(authority.authorityName == "ROLE_USER"){ 
			$("#noRightClick").attr("oncontextmenu", "return false");
			
		}*/
	});
}

// 유저정보 화면에 띄우기
const printInfo = () => {
	console.log("***LoginId: " + member.username);
	$("#userName").val(member.username);
	console.log("***AuthorityName: " + authority.authorityName);
	$("#authorityName").val(authority.authorityName);

}




// 채팅내용 저장
const saveMessage = ()=> {
	Swal.fire({ 
		title: '정말 저장하시겠습니까?', 
		text: "채팅내용은 DB에 저장되지만 현재 채팅은 종료됩니다.", 
		icon: 'warning', 
		showCancelButton: true, 
		confirmButtonColor: '#3085d6', 
		cancelButtonColor: '#d33', 
		confirmButtonText: '승인', 
		cancelButtonText: '취소' 
	}).then((result) => {
		if (result.isConfirmed) { // 승인 버튼 누르면 저장
			const param = {
				guestid: selectedUser,
				hostid: currentUser
			};

			$.ajax({
				url: "/message/add",
				data: param,
				method: 'get'
			});

			$("#chatlist *").remove();
			selectedUser = "";
			console.log("selected user: " + selectedUser);
			$('#selectedUserId').html('');
			Swal.fire('현재 채팅종료 완료!', 'DB에 저장도 완료!', 'success');
		} 
	});	
}


// 채팅종료 및 나가기
const exitMessage = () => {
	Swal.fire({ 
		title: '정말 종료하시겠습니까?', 
		text: "채팅내용은 안전하게 저장됩니다.", 
		icon: 'warning', 
		showCancelButton: true, 
		confirmButtonColor: '#3085d6', 
		cancelButtonColor: '#d33', 
		confirmButtonText: '승인', 
		cancelButtonText: '취소' 
	}).then((result) => {
		if (result.isConfirmed) { // 승인버튼 누르면
			
			let tmpSender = "챗봇";
			let tmpSendTime = getCurrentTime();
			let tmpMessage = currentUser + " 와 채팅이 종료 되었습니다.";
			sendMsg(tmpSender, tmpSendTime, tmpMessage);
			console.log("*********메세지 보내기");
			
			
			deregister();
		} 
	});
}

// 유저 비등록하기
const deregister = () =>{
	//stompClient.disconnect();
	console.log("*********"+currentUser+" disconnected");
	
	$.ajax("/deregistraion/" + currentUser); // userStorage 에서 remove하기
		
	Swal.fire('채팅종료 완료!', '다음에 또 이용해 주세요!', 'success')
	.then((result) => {
		if(result.isConfirmed){	// 확인 누르면
			window.close();
		}
	});
}

// 채팅창 스크롤 아래로 
const scrollToBottom = () => {
	$chatHistory.scrollTop($chatHistory[0].scrollHeight);
}

// 현재 시간 구하기
const getCurrentTime = () => {
	return new Date().toLocaleTimeString().replace(/([\d]+:[\d]{2})(:[\d]{2})(.*)/, "$1$3");
}

// 화면에 메세지 추가
const addMessage = () => {
	sendMessage($textarea.val());	//  보낼 메세지
}

// Enter키로 메세지 추가
const addMessageEnter = (event) => {
	// enter was pressed
	if (event.keyCode === 13) {
		addMessage();
	}
}

// 새로고침 막기
const doNotReload = () => {
	if ((event.ctrlKey == true && (event.keyCode == 82)) || (event.keyCode == 116)) { // ctrl+R, F5 
		event.keyCode = 0;
		event.cancelBubble = true;
		event.returnValue = false;

		//Swal.fire('새로고침 방지 알람 ','정보 보호 차원에서 금지 되었습니다.');
	}
	
	if (event.keyCode == 505) { 
       alert(document.body.onBeforeUnload);
    }
}

// 메세지 신호 보낼때 Json형식으로 stomp에 보내기
const sendMsg = (from, sendtime, text) => {
	stompClient.send("/app/chat/" + selectedUser, {}, JSON.stringify({
		sender: from,
		sendTime: sendtime,
		message: text
	}));
	/*let msgObject = new Object; //   메세지 임시 저장소
	msgObject.sender = from;
	msgObject.sendTime = sendtime;
	msgObject.message = text;
	messageArray.push(msgObject);
	console.log(messageArray);*/

}

// 메세지 보내기
const sendMessage = (message) => {
	let sender = $('#userName').val();
	let sendtime = getCurrentTime();
	console.log("sender: " + sender);
	sendMsg(sender, sendtime, message);	// 메세지 보내기 함수 실행
	scrollToBottom();

	// 메세지 기록 
	if (message.trim() !== '') {
		let template = Handlebars.compile($("#message-template").html()); // 핸들바로 메세지 컴파일하기
		let context = { // message-template에 상대방에게 보내는 메세지 정보
			messageOutput: message,	// 보여지는 메세지
			time: sendtime,	// 보내는 시간
			toUserName: selectedUser,	//	수신자
			messageSender: sender
		};

		$chatHistoryList.append(template(context)); // 채팅창에 보낸 메세지 기록
		scrollToBottom();
		$textarea.val('');
	}
}



// 랜더작업 (데이터를 html로 변환해서 renderer 에게 전달 )
// 메세지 받기
// html로 입력받아 해석해서 모니터로 출력 해줍니다
const render = (userName, sendTime, message) => {
	scrollToBottom();
	// responses
	let templateResponse = Handlebars.compile($("#message-response-template").html());
	let contextResponse = {	// message-response-template에 상대방에게서 받는 메세지 정보
		response: message, // 보여지는 메세지
		time: sendTime,	// 받는 시간
		userName: userName
	};

	setTimeout(function() {
		$chatHistoryList.append(templateResponse(contextResponse)); //  채팅창에 받은 메세지 기록
		scrollToBottom();
	}.bind(this), 1000);
}


