

	
let product = undefined;
let _this = undefined;

/* const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content"); */

// 현재 화면을 담는 장바구니 1개
const cart = {
	init: function() {
		this.pno = product.pno;
		this.name = product.name;
		this.manufacturer = product.manufacturer;
		this.price = product.price;
		this.count = 1;
		this.image = product.imageFileName;
		this.cartPrice = this.price * this.count;
	},
		
	// 장바구니에 담은 상품 개수 증가, 감소
	increase() {
		this.count++;
		this.cartPrice = this.count * product.price;
	},
	decrease() {
		this.count--;
		this.cartPrice = this.count * product.price;
	}
};

const main = {
	init: function() {
		$("#inc").on("click", this.incProduct);
		$("#dec").on("click", this.decProduct);
		window._this = this;
		$("#add_to_cart").on("click", this.addToCart);
		$("#buy").on("click", this.buy);
				
		const pno = location.search.substr(5);
		$.ajax("/products/" + pno).done(result=>{
				
			product = result;
			this.printPage();
		});
	},
		
		// 개수와 총 가격을 출력하는 일이 여러번이라 메서드로 분리
	printTotalCountAndPrice: function() {
		const price = cart.cartPrice.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","); 
		$("#total_price").text(price);
		$("#total_count").text(cart.count);
	}, 
		
	printPage: function() {
		$("#image").attr("src", "/upload/productimage/" + product.imageFileName);
		$("#manufacturer").text(product.manufacturer);
		$("#name").text(product.name);
		if(isLogin==true ) {$("<span>").text("").css({"font-size":"15px","float":"right","positon":"fixed"}).appendTo($("#manufacturer"))};
		const param = {						
			pno : product.pno,
			loginId : currentUser
		} ;
		$.ajax({ async: false, url: "/product_member/is_exist", data: param, method: "get"})
			.done(heartCheck=>{
		 
	 			isHearted = heartCheck;
			 			
	 			if(isLogin==true ){
					if(isHearted == true){
						console.log("in TFT");
						const $button = $("<goodOn_btn>").attr("id","goodBtn_"+ product.pno).attr("type", "button").attr("class","far fa-star").css({"color": "black", "font-size": "25px","cursor":"pointer", "display":"none"})
							.attr("onclick","like("+product.pno+")").text("").appendTo($("#manufacturer span")); // c.writer 처럼 String값을 파라미터로 받으 때 '' 안에 넣어야 함
								
						const $button2 = $("<goodOff_btn>").attr("id","goodCancelBtn_" + product.pno).attr("type", "button").attr("class","fas fa-star").css({"color": "#FFE400", "font-size": "25px", "cursor":"pointer"})
							.attr("onclick","likeCancel("+product.pno+")").text("").appendTo($("#manufacturer span"));
								
						$("<span>").attr("id", "good_cnt_"+product.pno).text(product.goodCnt).css({"color": "black", "font-size": "15px","margin-left":"20px"}).appendTo($("#manufacturer span"));
			 			$("<span>").attr("id", "likeCheck").text("").css({"color": "black", "font-size": "15px"}).appendTo($("#manufacturer span"));
					}else{
						console.log("in TFF");
						const $button = $("<goodOn_btn>").attr("id","goodBtn_"+ product.pno).attr("type", "button").attr("class","far fa-star").css({"color": "black", "font-size": "25px","cursor":"pointer"})
							.attr("onclick","like("+product.pno+")").text("").appendTo($("#manufacturer span")); // c.writer 처럼 String값을 파라미터로 받으 때 '' 안에 넣어야 함
								
						const $button2 = $("<goodOff_btn>").attr("id","goodCancelBtn_" + product.pno).attr("type", "button").attr("class","fas fa-star").css({"color": "#FFE400", "font-size": "25px", "cursor":"pointer", "display":"none"})
							.attr("onclick","likeCancel("+product.pno+")").text("").appendTo($("#manufacturer span"));
								
						$("<span>").attr("id", "good_cnt_"+product.pno).text(product.goodCnt).css({"color": "black", "font-size": "15px","margin-left":"20px"}).appendTo($("#manufacturer span"));
			 			$("<span>").attr("id", "likeCheck").text("").css({"color": "black", "font-size": "15px"}).appendTo($("#manufacturer span"));
					}
				}else if(isLogin==false) {
					console.log("in F**");
					$("#goodOn_btn").prop("disabled", true);
					$("#goodOff_btn").prop("disabled", true);
				}
						 
			});
				
				
				
				
		const price = product.price.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ","); 
		$("#price span").text(price);
		// 상품 페이지로 들어오면 1개의 상품을 담은 장바구니 객체를 생성
		cart.init();	
		$("#count").text(cart.count);
		this.printTotalCountAndPrice();
				
				
				
	},
	
			// 옵션이 있는 경우 상품 개수 증가/감소
	incProduct: function() {
		let number = $("#count").text();
		const param = {pno:product.pno, count:++number};
		$.ajax({url:"/products/stock", data:param}).done(count=>{
			cart.increase();
		$("#count").text(cart.count);
			window._this.printTotalCountAndPrice()
		}).fail(result=>alert(result.responseText));
	},
	decProduct: function() {
		let number = $("#count").text();
		if(number>1) {
			cart.decrease();
			$("#count").text(cart.count);
			window._this.printTotalCountAndPrice()
		}
	},
		
	addToCart: function() {
		$.ajax({
			url:"/carts",
			method:"post",
			data: JSON.stringify(cart),
			contentType: "application/json",
			/* beforeSend: function(xhr) {
				xhr.setRequestHeader(header, token)
			} */
		}).done(
			Swal.fire({ 
				title: '장바구니 추가 완료!', 
				text: "장바구니 화면으로 이동 하시겠습니까?", 
				icon: 'success', 
				showCancelButton: true, 
				confirmButtonColor: '#3085d6', 
				cancelButtonColor: '#d33', 
				confirmButtonText: '승인', 
				cancelButtonText: '취소' 
			}).then((result) => {
				if (result.isConfirmed) { // 승인버튼 누르면
					location.href = "/cart/cart_read";
				}
			}),
		);
	},
			
	buy: function() {
		const param = {
			pno: product.pno,
			count: $("#count").text()
		}
		console.log(JSON.stringify(param));
		$.ajax({
			url:"/orders/product",
			method:"post",
			data: JSON.stringify(param),
			contentType: "application/json",
			/* beforeSend: function(xhr) {
				xhr.setRequestHeader(header, token)
			} */
		}).done((result, text, response)=>location.href = response.getResponseHeader('Location'));
	},
	
	getStar: function(){
		let pno = location.search.substr(5);
		
		let num = null;
		const param = {
			pno: pno
		}
		
		$.ajax({
			async: false,
			url: "/products/avgOfStar",
			method: "get",
			data: param,
			
		}).done(result=>{
				num=result;
				$(".avgStarH2").text("평균 별점("+num+")");
				this.printStar(num);
			});
	},
	
	printStar: function(num){
		$(".rating").attr("data-rate", num);
	
		let dataRate = $(".rating").attr("data-rate");
		var tmp = Math.ceil(dataRate);
		
		let $rating1 = $(".rating");
		for(let i = 0; i < tmp; i++){
			const $div1 = $("<div class='star-wrap'>").appendTo($rating1);
			const $div2 = $("<div class='star'>").appendTo($div1);
			$("<i class='fas fa-star fa-lg'>").appendTo($div2);
		}
					
		var rating = $('.rating');
		rating.each(function(){
			var $this = $(this);
			var targetScore = $this.attr('data-rate');
			var firstDigit = targetScore.split(".");
			if(firstDigit.length > 1){
				for(var i = 0; i < firstDigit[0]; i++){
					$this.find('.star').eq(i).css({width:'100%'});
				}
				$this.find('.star').eq(firstDigit[0]).css({width:firstDigit[1]+'0%'});
			}else{
				for(var i = 0; i < targetScore; i++){
					$this.find('.star').eq(i).css({width:'100%'});
				}
			}
		});
	}
	
};
	
	
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
	
window.onload = function(){
	currentUser = $("#username").text();
	console.log("currentuser: " + currentUser);
	main.init();
	main.getStar();
}
