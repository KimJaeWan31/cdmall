/*
프사			선택 입력
아이디		필수 입력
이름			필수 입력
비밀번호		필수 
비밀번호 확인	필수
이메일		필수
생일			필수

※ 오류 처리 단계
1. 필수인 경우 에러 메시지 : "필수 입력입니다" 에러 메시지
2. 패턴 테스트
	아이디 : 대문자, 숫자 8~10자
	이름 : 한글 2~10자
	비밀번호 : 영숫자/특수문자 8~10자. 특수문자 하나이상
	비밀번호 확인 : 비밀번호와 일치
	이메일 : 이메일 형식 확인
	생일 : 숫자 4 - 숫자 2 - 숫자 2
3. 사용가능 확인
	아이디, 이메일은 ajax로 서버에 사용가능 여부 확인
*/

// 오류메시지 출력 함수 : (value, pattern, message, element) -> 입력값, 패턴, 오류 메시지, 오류메시지를 출력할 element
const check = (value, pattern, message, element)=>{
	if(value=="") {
		element.text("필수 입력입니다").attr("class", "fail");
		return false;
	}	
	if(pattern.test(value)==false) {
		element.text(message).attr("class", "fail");
		return false;
	}
	return true;
}

// 프사를 출력하는 함수
/*const loadSajin = ()=>{
	// 자바스크립트 객체와 jQuery 객체는 다르다. jQuery 객체는 자바스크립트 객체 + 알파
	// jQuery 객체에 들어있는 html요소를 꺼내는 방법 : $("#sajin")[0]
	// $("#sajin")[0] == document.getElementById("sajin")
	
	// file 요소에 multiple 속성을 지정하면 사진을 여러장 선택할 수 있다 => 선택한 사진들이 files[] 배열을 이룬다
	const file = $("#sajin")[0].files[0];
	
	// jQuery로 여러개를 선택 : $("li") -> jQuery 객체
	// 그런 다음 $.each로 반복문 돌리면 <li></li>가 된다
	// $.each($("li.menu"), function(idlix, ) {
	// 		li는 jQuery 요소가 아니라 html이다
	//		여기서 jQuery를 사용하고 싶다면 $(li)로 jQuery 객체로 변환한다	
	// });
	
	const maxSize = 1024*1024;			
	if(file.size>maxSize) {
		Swal.fire('프로필 크기 오류', '프로필 사진은 1MB를 넘을 수 없습니다','error');
		$("#sajin").val("");
		$("#show_sajin").removeAttr("src");
		return false;
	}
	const reader = new FileReader();
	reader.readAsDataURL(file);
	reader.onload = function() {
		$("#show_sajin").attr("src", reader.result);
	}
	return true;
}*/

const usernameCheck = ()=>{
	const $username = $("#username").val().toUpperCase();
	$("#username").val($username);
	const pattern = /^[0-9A-Z]{8,10}$/;
	return check($username, pattern, "아이디는 대문자와 숫자 8~10자입니다", $("#username_msg"));
}

const irumCheck = ()=>{
	// 에러메시지를 지운다
	$("#irum_msg").text("");
	const $value = $("#irum").val();
	// 정규식으로 문자열 패턴을 지정. 자바스크립트는 정규식을 //로 감싼다
	// ^는 시작, $는 끝. 만약 [가-힣]{2,10} 패턴일 경우 "ab우리cdefg"로 통과한다
	const pattern = /^[가-힣]{2,10}$/; 
	return check($value, pattern, "이름은 한글 2~10자입니다", $("#irum_msg"));
}

const passwordCheck = () => {
	$("#password_msg").text("");
	const $password = $("#password").val();
	
	// 정규식에서 ()는 독립된 조건
	// ?=는 앞에서 부터 찾아라(전방 탐색)
	// .은 임의의 한글자
	// *는 앞글자가 0개이상, +는 앞글자가 1개이상 -> 특수문자가 1글자 이상
	const pattern = /^(?=.*[!@#$%^&*])^[A-Za-z0-9!@#$%^&*]{8,10}$/;
	return check($password, pattern, "비밀번호는 영숫자와 특수문자 8~10자입니다", $("#password_msg"));	
}
const password2Check = () => {
	$("#password2_msg").text("");
	const $password2 = $("#password2").val();
	if($password2=="") {
		$("#password2_msg").text("필수입력입니다").attr("class","fail");
		return false;
	} 
	if($password2!==$("#password").val()) {
		$("#password2_msg").text("비밀번호가 일치하지 않습니다").attr("class","fail");
		return false;
	}
	return true;
}
const emailCheck = ()=>{
	const $email = $("#email").val();
	// /i는 case insensitive, g는 global(한번 찾고 마무리하지말고 다 찾아라)
	// 정규식을 사용하는 대표적인 경우 두 가지는 test(), replace()
	const pattern = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
	return check($email, pattern, "정확한 이메일을 입력하세요", $("#email_msg"))
}
/*const birthdayCheck = ()=>{
	$("#birthday_msg").text("");
	const $birthday = $("#birthday").val();
	
	// <input type="date">로 날짜를 선택하면 1990-11-20 형식
	const pattern = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/;
	return check($birthday, pattern, "정확한 생일을 입력하세요", $("#birthday_msg"))
}*/

// 실제로 회원 가입을 수행하는 함수 : 사용자가 입력한 데이터를 가지고 FormData 객체를 만들어 ajax 요청을 보낸다
const join = () =>{
	// multipart/form-data 형식을 지정해야한다
	// 폼인 경우 : <form enctype="multipart/form-data">
	// 자바스크립트의 경우 FormData 내장 객체를 사용한다
	//		1. new FormData($("폼아이디")) - 폼 전제를 FormData로
	//		2. const formData = new FormData(); -> formData.append()로 하나씩 추가
	
	const formData = new FormData($("#join_form")[0]);
	
	// FormData에 담긴 값을 출력해보려면 of 반복문을 써야만 한다
	for(const key of formData.keys())
		console.log(key);
	for(const value of formData.values()) 
		console.log(value);
	
	// processData : $.ajax는 자바스크립트 객체를 urlencoded로 자동 변환 -> false를 줘서 금지
	// contentType : false면 multipart/form-data
	$.ajax({
		url: "/members/new",
		method: "post",
		data: formData,
		processData: false,
		contentType: false
	}).done(()=>Swal.fire("가입신청 완료","이메일을 확인하세요", "success"))
	.fail((msg)=>Swal.fire('가입신청 실패', msg,'error'));
}

$(document).ready(()=>{
	// 사진을 변경하면 출력하기
	//$("#sajin").on("change", loadSajin);
	
	// 아이디를 입력하면 usernameCheck 후 서버에 사용가능여부를 확인
	$("#username").on("blur", ()=>{
		// 아이디에 대해 필수 입력 체크, 패턴 체크
		if(usernameCheck()==false)
			return false;
		$.ajax("/members/username/check?username=" + $("#username").val())
			.done(()=>$("#username_msg").text("좋은 아이디네요").attr("class", "success"))
			.fail(()=>$("#username_msg").text("사용중인 아이디입니다").attr("class", "fail"));
	});
	
	// 이메일을 입력하면 emailCheck 후 서버에 사용가능 여부를 확인
	$("#email").on("blur", ()=>{
		if(emailCheck()==false)
			return false;
		$.ajax("/members/email/check?email=" + $("#email").val())
			.done(()=>$("#email_msg").text("사용할 수 있는 이메일입니다").attr("class", "success"))
			.fail(()=>$("#email_msg").text("사용중인 이메일입니다").attr("class", "fail"));
	});
	
	$("#email").on("blur", emailCheck);
	$("#irum").on("blur", irumCheck);
	$("#password").on("blur", passwordCheck);
	$("#password2").on("blur", password2Check);
	//$("#birthday").on("blur", birthdayCheck);
	
	// 입력 오류 메시지가 떠도 가입 버튼을 누르면 가입 신청 처리가 된다
	// 프로그래밍한 순서대로 실행되는 백엔드와 달리 프론트는 사용자 마음대로 실행할 수 있다
	// -> 프로그래머가 두번, 세번 확인하면서 처리해야 한다
	$("#join").on("click", ()=>{
		// join을 호출하기 전에 6개를 입력했고 패턴을 통과하는 지 테스트
		const r1 = usernameCheck();
		const r2 = passwordCheck();
		const r3 = password2Check();
		const r4 = irumCheck();
		const r5 = emailCheck();
		//const r6 = birthdayCheck();
		/*if((r1 && r2 && r3 && r4 && r5 && r6) == false)
			return false;*/
			
		if((r1 && r2 && r3 && r4 && r5) == false)
			return false;

		
		// jQuery의 $.when을 이용해 여러개의 ajax를 한꺼번에 처리하자
		$.when($.ajax("/members/username/check?username="+$("#username").val()), 
			$.ajax("/members/email/check?email="+$("#email").val()))
			.done(()=>join())
			.fail(()=>Swal.fire("실패", "아이디나 이메일이 사용중입니다", "error"));		
	});
});