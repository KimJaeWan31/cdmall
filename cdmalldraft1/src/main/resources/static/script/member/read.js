	let member = undefined;
	
	const printPage=()=>{
		console.log(member.profile);
		$("#show_sajin").attr("src", member.profile);
		$("#irum").val(member.irum);
		$("#username").text(member.username);
		$("#tel").val(member.tel);
		$("#birthday").val(member.birthday);
		$("#joinday").text(member.createTime);
		$("#level").text(member.level);
		$("#days").text(member.days+ " 일");
		$("#point").text(member.point * 1);
		
		$("#email").val(member.email);
		const emails = member.email.split("@");
		
		const $options = $("#domain option");	
		let isFind = false;
		
		$.each($options, function(idx, option) {	
			if($(option).text()==emails[1]) {
				$(option).prop("selected", true);
				isFind = true;
			}
		});
		
		if(isFind==false) {
			$($options[0]).prop("selected", true)
		}		
	}
	
	const changeEmail = ()=>{
		const emails = member.email.split("@");
		const $choice = $("#domain").val();
		
		if($choice=="0")
			$("#email").val(emails[0] + "@") ;
		else
			$("#email").val(emails[0] + "@" + $choice) ;
	}
	
	// 프사를 출력하는 함수
	const loadSajin = ()=>{
		const file = $("#sajin")[0].files[0];
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
	}
	
	
	$(document).ready(()=>{
		$.ajax("/members/member").done(result=>{
			member = result;
			printPage();
		});
		
		$("#sajin").on("change", loadSajin);
		$("#pwdArea").css("display", "none");
		
		$("#activateChangePwd").on("click", ()=>{
		 	if($("#activateChangePwd").text() == "비밀번호 수정 ▼") 
		 		$("#activateChangePwd").text("비밀번호 수정 ▲");
    		else 
      			$("#activateChangePwd").text("비밀번호 수정 ▼");
  
			$("#pwdArea").toggle();		
		});
		
		$("#domain").on("change", changeEmail);
		
		//내 정보 보기
		$("#my_info").on("click", ()=>{
			location.href ="/member/read"
		});
		
		//내 펫 정보 보기
		$("#my_pet").on("click", ()=>{
			location.href ="/member/pet"
		});
		
		// 비밀번호 변경
		$("#changePwd").on("click", ()=>{
			const $password = $("#password").val();
			const $newPassword = $("#newPassword").val();
			const $newPassword2 = $("#newPassword2").val();
			
			const pattern = /^(?=.*[!@#$%^&*])^[A-Za-z0-9!@#$%^&*]{8,10}$/;
			
			if(pattern.test($password)==false) 
				return false;
			if(pattern.test($newPassword)==false)
				return false;
			if($newPassword!=$newPassword2)
				return false;
			
			const params= {
					password: $password,
					newPassword: $newPassword
			};
			
			$.ajax({
				url: "/members/member/password",
				method: "patch",
				data: params
			}).done(()=>Swal.fire("변경","비밀번호를 변경했습니다", "success"))
				.fail(()=>Swal.fire("변경 실패", "비밀번호를 변경하지 못했습니다", "error"));
			
		});
		
		// 전체 변경
		$("#update").on("click", ()=>{
			const formData = new FormData();
			if($("#sajin")[0].files[0]!=undefined)
				formData.append("sajin", $("#sajin")[0].files[0]);
			
			const $inputIrum = $("#irum").val();
			if(member.irum!=$inputIrum)
				formData.append("irum", $inputIrum);
			
			const $inputTel = $("#tel").val();
			if(member.tel!=$inputTel)
				formData.append("tel", $inputTel);
			
			const $inputEmail = $("#email").val();
			if(member.email!=$inputEmail)
				formData.append("email", $inputEmail);
			
			const $inputbirthday = $("#birthday").val();
			if(member.birthday!=$inputbirthday)
				formData.append("birthday", $inputbirthday);
			
			const $password = $("#password").val();
			const $newPassword = $("#newPassword").val();
			const $newPassword2 = $("#newPassword2").val();
			
			const pattern = /^(?=.*[!@#$%^&*])^[A-Za-z0-9!@#$%^&*]{8,10}$/;
			
			if(pattern.test($password)==true && pattern.test($newPassword)==true && $newPassword==$newPassword2) {
				formData.append("password", $password);
				formData.append("newPassword", $newPassword);
			} 
			
			$.ajax({
				url: "/members/member",
				method: "post",
				data: formData,
				processData:  false,
				contentType: false
			}).done(()=>Swal.fire("변경","회원 정보를 변경했습니다", "success"))
				.fail(()=>Swal.fire("변경 실패", "회원 정보를 변경하지 못했습니다", "error"));
		});
		
		$("#resign").on("click", ()=>{
			let choice = confirm('정말 탈퇴하시겠습니까?');
			if(choice==false)
				return;
			$.ajax({
				url: "/members/member",
				method : "delete"
			}).done(()=>location.href="/")
			.fail(()=>Swal.fire("삭제 실패", "회원 정보를 삭제하지 못했습니다", "error"));
		});
		
		$("#addressBtn").on("click", ()=>{
			let url = '/member/address';
  			let name = 'addressOpen';
  			let option = "width=520, height=700, left=500, top=250";
  			window.open(url, name, option);
		});
	})