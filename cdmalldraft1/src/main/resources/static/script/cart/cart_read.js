let _this = undefined;

// 전체 선택버튼을 위한 boolean 변수
const isChoice = false;
	
// csrf 처리를 위한 토큰과 헤더
/* const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content"); */
	
const main = {
	init : function() {
		$.ajax({ url: "/carts", method: "get"}).done(result=>{
			carts = result;
			this.printPage();
			console.log(carts);
			
		});
		
		$("#check_all").on("change", this.checkAll);
		$("#cart_div").on("click", ".inc", this.incProduct);
		$("#cart_div").on("click", ".dec", this.decProduct);
		$("#delete_product").on("click", this.deleteProduct);
		$("#order").on("click", this.order);
		$("#continueShopping").on("click", this.continueShopping);
		
		
		window._this = this;
	},
	
	continueShopping: function(){
		$.ajax({
			url: "/carts/get_url",
			method: "get"
		}).done(result=>{
			savedUrl = result;
			location.href = savedUrl;
		});
	},
	
	printPage: function() {
		// 장바구니가 비어있으면 emtpy_cart 이미지를, 아니면 출력 div를 
		if(carts.length==0) {
			$("#cart_div").hide();
			$("#empty_cart_div").css("text-align","center").css("height","400px").show();
		} else {
			$("#cart_div").show();
			$("#empty_cart_div").hide();
			this.printCarts();
		}
	},
	
	printCarts: function() {
		// 장바구니 전체 가격을 계산할 변수
		let totalPrice = 0;
		const $list = $("#list");
		$list.empty();
		$.each(carts, function(idx, cart) {
			totalPrice += cart.cartPrice;
			const $tr = $("<tr>").appendTo($list);
			const $td1 = $("<td>").appendTo($tr);
			$("<input>").attr("type","checkbox").attr("class","check").data("pno", cart.pno).appendTo($td1);
			
			const $td2 = $("<td>").appendTo($tr);
			$("<img>").attr("src", "/upload/productimage/"+cart.image).attr("class","cart_image").appendTo($td2);
			
			const $td3 = $("<td>").appendTo($tr);
			$("<div>").text(cart.manufacturer).appendTo($td3);
			$("<div>").text(cart.name).appendTo($td3);
			
			const $td4 = $("<td>").appendTo($tr);
			const $td4_div = $("<div class='item_amount'>").appendTo($td4);
			$("<span>").attr("class","dec").data("pno", cart.pno).text("-").appendTo($td4_div);
			$("<span class='count'>").text(cart.count).appendTo($td4_div);
			$("<span>").attr("class","inc").data("pno", cart.pno).text("+").appendTo($td4_div);
			
			const $td5 = $("<td>").appendTo($tr);
			$("<div>").text(cart.cartPrice + "원").appendTo($td5);
			$("<div>").append($("<button type='button' class='button order'>주문하기</button>").attr("data-cartNo", idx)).appendTo($td5);
		});	
		$("#total_price").text(totalPrice + "원");
	},
		
	checkAll : function() {
		if($(this).prop("checked")==true) {
			$.each($(".check"), function(idx, element) {
				$(element).prop("checked", true);
			}) 
		} else {
			$.each($(".check"), function(idx, element) {
				$(element).prop("checked", false);
			}) 
		}
	},
 
	incProduct: function() {
		const param = {
			pno: $(this).data("pno"),
			optionNo : $(this).data("optionNo")
		};
		$.ajax({
			url:"/carts/increase", 
			method:"patch",
			data:param,
			/* beforeSend: function(xhr) { 
				xhr.setRequestHeader(header, token) 
			} */
		}).done(result=>{
			carts = result;
			window._this.printCarts();
		}).fail(result=>alert(result.responseText));
	},
		
	decProduct: function() {
		const param = {
			pno: $(this).data("pno"),
			optionNo : $(this).data("optionNo")
		};
		$.ajax({
			url:"/carts/decrease", 
			method:"patch", data:param,
			/* beforeSend: function(xhr) { 
				xhr.setRequestHeader(header, token)
			} */
		}).done(result=>{
			carts = result;
			window._this.printCarts();
		});
	},
		
	// 체크박스를 선택하고 선택상품 삭제 버튼을 클릭하면
	deleteProduct:function() {
		// [1,2,3]으로 보내면 서버에서 @RequestBody를 이용해 ArrayList<Integer>로 받는다
		// 선택한 체크박스의 pno 값들을 읽어와 추가할 비어있는 배열
		const dto = [];
		
		// 선택한 체크박스의 pno를 읽어와 dto에 push
		$(".check").each(function(idx) {
			if($(this).is(":checked"))
				dto.push($(this).data("pno"));
		});
		
		$.ajax({
			url:"/carts",
			method:"delete",
			data: JSON.stringify(dto),
			contentType: "application/json",
			/*beforeSend: function(xhr) {
				xhr.setRequestHeader(header, token)
			}*/
		}).done(result=>{
			carts = result;
			window._this.printPage();
		});
	},
		
	// 주문버튼을 클릭하면 선택한 체크박스의 pno값들을 배열로 만들어 서버로 보낸다
	order: function() {
		const dto = [];
		$(".check").each(function(idx) {
			if($(this).is(":checked"))
				dto.push($(this).data("pno"));
		});
		$.ajax({
			url:"/orders/cart",
			method:"post",
			data: JSON.stringify(dto),
			contentType: "application/json",
			beforeSend: function(xhr) {
				xhr.setRequestHeader(header, token)
			}
		}).done((result, text, response)=>{
			location.href = response.getResponseHeader('Location')
		});
	}
}
	
window.onload = function() {
	main.init();
}