
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


const usernameCheck = ()=>{
	const $username = $("#username").val().toUpperCase();
	$("#username").val($username);
	const pattern = /^[0-9A-Z]{8,10}$/;
	return check($username, pattern, "아이디는 영문대문자와 숫자 8~10자입니다", $("#username_msg"));
}

const irumCheck = ()=>{
	$("#irum_msg").text("");
	const $value = $("#irum").val();
	const pattern = /^[가-힣]{2,10}$/; 
	return check($value, pattern, "이름은 한글 2~10자입니다", $("#irum_msg"));
}

const passwordCheck = () => {
	$("#password_msg").text("");
	const $password = $("#password").val();

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
	const pattern = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
	return check($email, pattern, "정확한 이메일을 입력하세요", $("#email_msg"))
}

// 실제로 회원 가입을 수행하는 함수 : 사용자가 입력한 데이터를 가지고 FormData 객체를 만들어 ajax 요청을 보낸다
const join = () =>{
	const formData = new FormData($("#join_form")[0]);
	
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
	$("#username").on("blur", ()=>{
		if(usernameCheck()==false)
			return false;
		$.ajax("/members/username/check?username=" + $("#username").val())
			.done(()=>$("#username_msg").text("좋은 아이디네요").attr("class", "success"))
			.fail(()=>$("#username_msg").text("사용중인 아이디입니다").attr("class", "fail"));
	});
	
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
	
	$("#join").on("click", ()=>{
		console.log("aaa");
		const r1 = usernameCheck();
		const r2 = passwordCheck();
		const r3 = password2Check();
		const r4 = irumCheck();
		const r5 = emailCheck();
		if((r1 && r2 && r3 && r4 && r5) == false)
			return false;

		$.when($.ajax("/members/username/check?username="+$("#username").val()), 
			$.ajax("/members/email/check?email="+$("#email").val()))
			.done(()=>join())
			.fail(()=>Swal.fire("실패", "아이디나 이메일이 사용중입니다", "error"));		
	});
});