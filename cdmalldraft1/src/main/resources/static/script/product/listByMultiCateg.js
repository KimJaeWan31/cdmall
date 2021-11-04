$(function() {
	$.ajax("/categories/all").done(categories=>{
		const $ul = $("#menu #ul");
		$.each(categories, function(idx, category) {
			// 대분류는 li에 붙여서 출력
			const $li = $("<li>").text(category.categoryName).appendTo($ul);
			
			// 중분류가 있다면
			if(category.categories.length>0) {
				// 중분류의 경우 ul을 만들고 li에 붙여서 출력
				const $sub_ul = $("<ul class='main2'>").appendTo($li);
				$.each(category.categories, function(idx, sub_category) {
					const $sub_li = $("<li>").text(sub_category.categoryName).attr("onclick","categoryRootClicked("+sub_category.categoryCode+")").css("cursor","pointer").appendTo($sub_ul);
					
					// 소분류가 있다면
					if(sub_category.categories.length>0) {
						// 소분류의 ul을 만들고 li에 붙여서 출력
						const $descendant_ul = $("<ul class='main3'>").appendTo($sub_li);
						$.each(sub_category.categories, function(idx, descendant_category) {
							$("<li>").data("categoryCode",descendant_category.categoryCode).text(descendant_category.categoryName).attr("onclick","categoryClicked("+descendant_category.categoryCode+")").css("cursor","pointer").appendTo($descendant_ul);
						});
					}
				});
			}
		});
	});
});

const categoryClicked=(categoryCode)=>{
	 let pageno = 1;
	 location.href = "/product/listByCateg?pageno="+pageno+"&categoryCode="+categoryCode;
}

const categoryRootClicked=(categoryCode)=>{
	 let pageno = 1;
	 location.href = "/product/listByRootCateg?pageno="+pageno+"&categoryCode="+categoryCode;
}
	
	
let currentUser = undefined;
let isHearted = undefined;
let page = null;

const printProduct = ()=>{
	console.log(page.content);
	const $list = $("#list");
	// 서버가 보낸 board들은 page.content에 저장되어 있다
	for(c of page.content) {
			
   
			
		const $li = $("<li>").appendTo($list);
		const $figure = $("<figure>").appendTo($li);
			// 
		$("<img>").attr("src", "/upload/productimage/" + c.imageFileName).attr("onclick","productClicked("+ c.pno +")").attr("class","prodImg")
				.css({"cursor":"pointer"}).attr("id", c.pno+"_"+c.imageFileName).appendTo($figure);
// 			console.log(c.imageFileName)
		$('img').attr('id', c.pno+"_"+c.imageFileName);
		const $figcaption = $("<figcaption>").attr("class","content_css").appendTo($li);
		const $div = $("<div>").appendTo($figcaption);
			/* $("<a>").attr("href", "/product/read?pno=" + c.pno).css({"color":"red" ,"text-decoration-line": "none","cursor":"pointer"}).text(c.name).appendTo($div); */		
		$("<p>").attr("class","text_deco").attr("id", c.pno+"_"+c.name).attr("onclick","productClicked("+ c.pno +")")
		.css({"color":"red" ,"text-decoration-line": "none","cursor":"pointer","font-size":"20px"}).text(c.name).appendTo($div);
		
		const price = c.price.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");  //1,000 단위 끊어서 출력하는 함수
// 			$("<p>").text( c.price +"원").css({"font-size":"15px","margin-top":"5px"}).appendTo($div);
		$("<p>").text( price +"원").css({"font-size":"15px","margin-top":"5px"}).appendTo($div);
		
		$("<p>").text("제조사 : "+ c.manufacturer).css({"font-size":"15px"}).appendTo($div);
		if(isLogin==true ) {$("<span>").text("찜하기 : ").css({"font-size":"15px","margin-bottom":"30px"}).appendTo($div)};
			
	    $('.prodImg').hover(function() {
			$(this).css({"border": "1px solid #346AFF"   , "border-radius": "10px"});
	    }, 
		function(){
			 $(".prodImg").css({"border":"none"});
		});
	    $('.text_deco').hover(function() {
			$(this).css({"text-decoration":"underline"});
	    }, function(){
	        $(".text_deco").css({"text-decoration":"none"});
		});
		    
			
		const param = {
						
			pno : c.pno,
			loginId : currentUser
		} ;
		$.ajax({
			async: false,
			url: "/product_member/is_exist", 
			data: param, method: "get"
		}).done(heartCheck=>{
			 
 			isHearted = heartCheck;
 			
 			if(isLogin==true ){
				if(isHearted == true){
					console.log("in TFT");
					const $button = $("<goodOn_btn>").attr("id","goodBtn_"+ c.pno).attr("type", "button").attr("class","far fa-star").css({"color": "black", "font-size": "25px","cursor":"pointer", "display":"none"})
					.attr("onclick","like("+c.pno+")").text("").appendTo($div); // c.writer 처럼 String값을 파라미터로 받으 때 '' 안에 넣어야 함
					
					const $button2 = $("<goodOff_btn>").attr("id","goodCancelBtn_" + c.pno).attr("type", "button").attr("class","fas fa-star").css({"color": "#FFE400", "font-size": "25px", "cursor":"pointer"})
					.attr("onclick","likeCancel("+c.pno+")").text("").appendTo($div);
					
					$("<span>").attr("id", "good_cnt_"+c.pno).text(c.goodCnt).css({"color": "black", "font-size": "15px","margin-left":"20px"}).appendTo($div);
		 			$("<span>").attr("id", "likeCheck").text("").css({"color": "black", "font-size": "15px","margin-left":"20px"}).appendTo($div);
				}else{
					console.log("in TFF");
					const $button = $("<goodOn_btn>").attr("id","goodBtn_"+ c.pno).attr("type", "button").attr("class","far fa-star").css({"color": "black", "font-size": "25px","cursor":"pointer"})
					.attr("onclick","like("+c.pno+")").text("").appendTo($div); // c.writer 처럼 String값을 파라미터로 받으 때 '' 안에 넣어야 함
					
					const $button2 = $("<goodOff_btn>").attr("id","goodCancelBtn_" + c.pno).attr("type", "button").attr("class","fas fa-star").css({"color": "#FFE400", "font-size": "25px", "cursor":"pointer", "display":"none"})
					.attr("onclick","likeCancel("+c.pno+")").text("").appendTo($div);
					
					$("<span>").attr("id", "good_cnt_"+c.pno).text(c.goodCnt).css({"color": "black", "font-size": "15px","margin-left":"20px"}).appendTo($div);
		 			$("<span>").attr("id", "likeCheck").text("").css({"color": "black", "font-size": "15px","margin-left":"20px"}).appendTo($div);
				}
			}else if(isLogin==false) {
				console.log("in F**");
				$("#goodOn_btn").prop("disabled", true);
				$("#goodOff_btn").prop("disabled", true);
			}
					 
		});
			
// 			$("<p>").text("평점 :" + c.avgOfStar).appendTo($figcaption).css({"font-size":"15px","margin-top":"-5px"});
	}
};
	
const getPagination = () => {
	// 한번에 다섯개의 페이지씩
	const blockSize = 5;
	
	// 서버 응답에 현재 페이지가 포함되어 있지 않다....재계산하자
	let pageno = location.search.substr(8);
	if(pageno=="")
		pageno=1;
	
	// 0번 블록 : 1~5 page, 1번 블록 : 6~10 page
	const blockNo = Math.floor((pageno-1)/blockSize);
	const prev = blockNo * blockSize;
	const first = prev + 1;
	let last = first + blockSize - 1;
	let next = last + 1;
	const countOfPage = Math.ceil(page.totalcount/15);
	if(last>=countOfPage) {
		last = countOfPage;
		next = 0;
	}
	return {pageno, prev, next, first, last};
};

// 구조 분해 할당 : 객체를 변수로 풀어헤치는 문법
// const {pageno, prev, next, first, last} = getPagination();
const printPagination = ({pageno, prev, next, first, last}) => {
	const $pagination = $("ul.pagination");
	const url = "/product/list?pageno="
			
	// 이전으로 
	if(prev>0) {
		const $li = $("<li>").appendTo($pagination)
		$("<a>").attr("href", url+prev).text("이전으로").appendTo($li);
	}
	
	// 시작 페이지에서 마지막 페이지....현재 페이지 번호일 경우 active 클래스 추가
	for(let idx=first; idx<=last; idx++) {
		const $li = $("<li>").appendTo($pagination)
		$("<a>").attr("href", url+idx).text(idx).appendTo($li);
		if(idx==pageno)
			$li.attr("class", "active");
	}
	
	// 다음으로
	if(next>0) {
		const $li = $("<li>").appendTo($pagination)
		$("<a>").attr("href", url+next).text("다음으로").appendTo($li);
	}
}

const like = (pno)=>{
	// 추천
	
	if(isLogin==true){
		const param = {
				pno: pno,
				isGood: 1
		}
		
		$.ajax({ url: "/product_member/good_or_bad", data: param, method: "patch"})
			.done(goodCnt=> goodClicked(goodCnt, pno));
	}	
}

const likeCancel = (pno)=>{
	// 추천 취소
	if(isLogin==true){
		const param = {
				pno: pno,
				isGood: 0
		}
		
		$.ajax({ url: "/product_member/good_or_bad", data: param, method: "patch"})
			.done(goodCnt=>goodCnlClicked(goodCnt, pno));
	}	
}

const goodClicked=(goodCnt, pno)=>{
	if($("#good_cnt_"+pno).text()!=goodCnt){
		$("#good_cnt_"+pno).text(goodCnt);
		$("#goodBtn_"+pno).hide();
		$("#goodCancelBtn_"+pno).show();
	}
}

 const goodCnlClicked=(goodCnt, pno)=>{
	 if($("#good_cnt_"+pno).text()!=goodCnt){
		$("#good_cnt_"+pno).text(goodCnt);
		$("#goodBtn_"+pno).show();
		$("#goodCancelBtn_"+pno).hide();
	} 
 }
 
 
 // product/read 화면 이동 전 세션에 현제 쇼핑 주소 저장
 const productClicked=(pno)=>{
	const param = {
		 url: window.location.href
	}
	$.ajax({
		 url: "/product/save_url",
		data: param, 
		method: "post"
	}).done(location.href = "/product/read?pno="+pno);
 }
	 
 $("#top_menu_authenticated").on("click", "#logout", (e)=> {
	// a태그는 클릭하면 이동(동작이 2개, 그때 두번째 동작을 막아버린다)
	e.preventDefault();
	
	// 이벤트가 발생한 곳을 넓은 곳에서 좁은 곳으로 찾는다
	// <div id="parent"><div id="child"><button>클릭</button></div></div>
	// e.stopPropagation();
	
	var choice = confirm('로그아웃하시겠습니까?');
	if(choice==false)
		return;
	$.ajax({
		url:"http://localhost:8081/member/logout",
		method: "post",
	}).done(()=> location.href = '/');
		
});	




$(document).ready(()=>{
	// 주소창에서 페이지 번호를 잘라낸다. 페이지 번호가 없으면 1로
			//const $username = $("#username").text();
	
	let searchParams = new URLSearchParams(location.search);
	let pageno = undefined;
	const dto = [];
	
	//dto.push(pageno);
	
	pageno = 1;
	
	searchParams.forEach(function(value, key){
		console.log("**************value: "+value);
		console.log("**************key: "+key);
		
		dto.push(value);
		/*if(key == 'pageno'){
			//console.log("***********in if pageno");
			pageno = value;
			
		}*/
	});
	
	//dto.push(pageno);
	
	const params ={
		pageno: pageno,
		categList: dto
	}
	
	console.log("*****************pageno: "+pageno);
	
	currentUser = $("#username").text();
	
	console.log("currentUser: "+currentUser);
	
	console.log("*************dto: "+JSON.stringify(dto));
	
	
	
	
	if(pageno==undefined){
		pageno=1;
	}
	
	console.log("********pageno2: " + pageno );
	$.ajax({
		url: "/products/allByMultiCateg",
		method: "get",
		data: params,
		//contentType: "application/json"
	})
	 .done(result=>{
		console.log("****result: "+JSON.stringify(result));
		page=result;
		printProduct();
		printPagination(getPagination());
	}); 
})

