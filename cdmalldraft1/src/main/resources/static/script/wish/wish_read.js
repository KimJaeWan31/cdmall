let _this = undefined;
let product = undefined;
const isChoice = false;
	
// csrf 처리를 위한 토큰과 헤더
/* const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content"); */

const cart = {
	init: function() {
		console.log("************product.name: " + product.name);
		
		this.pno = product.pno;
		this.name = product.name;
		this.manufacturer = product.manufacturer;
		this.price = product.price;
		this.count = 1;
		this.image = product.imageFileName;
		this.cartPrice = this.price * this.count;
		
		$.ajax({
			async: false,
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
};

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
	
const main = {
	init : function() {
		/*$.ajax({ url: "/product_wish/wish", method: "get"}).done(result=>{
			wishList = result;
			console.log(wishList)
			this.printPage();
		});*/
		
		$.ajax({ url: "/products/wishList", method: "get"}).done(result=>{
			wishList = result;
			console.log(wishList);
			console.log(wishList.content);
			this.printPage();
		});
		
		$("#check_all").on("change", this.checkAll);
		$("#delete_product").on("click", this.deleteProduct);
		$("#continueShopping").on("click", this.continueShopping);
		window._this = this;
		

		
	
		//$("#add_to_cart").on("click", this.addToCart);
		//$(".addCart").on("click", this.addToCart);
		const pno = location.search.substr(5);
		/*$.ajax("/products/" + pno).done(result=>{
			product = result;
			this.printPage();
		});*/
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
		if(wishList.length==0) {
			$("#wish_div").hide();
			$("#empty_wish_div").css({"text-align":"center","height":"400px"}).show();
		} else {
			$("#wish_div").show();
			$("#empty_wish_div").hide();
			this.printwishList();
		}
	},
	

	printwishList: function() {
		// 장바구니 전체 가격을 계산할 변수
		let totalPrice = 0;
		const $list = $("#list");
		$list.empty();
		$.each(wishList.content, function(idx, wish) {
			const $tr = $("<tr>").appendTo($list);
			const $td1 = $("<td>").appendTo($tr);
			$("<input>").attr("type","checkbox").attr("class","check").data("pno", wish.pno).appendTo($td1);
			
			const $td2 = $("<td>").css({"width":"300px"}).appendTo($tr);
			$("<img>").attr("src", "/upload/productimage/"+wish.imageFileName).css({"height":"130px","width":"130px","cursor":"pointer"})
			.attr("onclick","productClicked("+ wish.pno +")").attr("class","cart_image")
			.attr("id", wish.pno+"_"+wish.imageFileName).appendTo($td2);
			
			const $td3 = $("<td>").css("width","800px").appendTo($tr);
			$("<div>").text("상품명: " + wish.name).attr("onclick","productClicked("+ wish.pno +")").css({"font-size":"15px","cursor":"pointer"}).attr("id","text_deco").appendTo($td3);
			$("<div>").text("제조사: " + wish.manufacturer).attr("onclick","productClicked("+ wish.pno +")").css({"margin-top":"20px","font-size":"15px","cursor":"pointer"}).attr("id","text_deco").appendTo($td3);
			$("<div>").text("가격: " + wish.price + "원").css({"margin-top":"20px","font-size":"15px","color":"#AE0000","font-weight":"700"}).appendTo($td3);
			
			
			
			
			
//			const $td4 = $("<td>").appendTo($tr);
//			const $td4_div = $("<div class='item_amount'>").appendTo($td4);
//			$("<span>").attr("class","dec").data("pno", cart.pno).text("-").appendTo($td4_div);
//			$("<span class='count'>").text(cart.count).appendTo($td4_div);
//			$("<span>").attr("class","inc").data("pno", cart.pno).text("+").appendTo($td4_div);
//			
			const $td5 = $("<td>").css({"float":"right","width":"200px"}).appendTo($tr);
//			$("<div>").text(cart.cartPrice + "원").appendTo($td5);
			$("<div>").append($("<button type='button' class='button addCart'>장바구니에 담기</button>").attr("id","add_to_cart_" + wish.pno)
				.attr("onclick", "window._this.addToCart(" + wish.pno + ")")
				.css({"margin-top":"20px"}).attr("data-cartNo", idx)).appendTo($td5);
			$("<div>").append($("<button type='button' class='button delete'>삭제</button>").css({"margin-top":"20px"})
			.attr("data-cartNo", idx)).appendTo($td5);
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
 
	
	getProductInfo: function(pno){
		$.ajax({
			async: false,
			url: "/products/" + pno,
			}).done(result=>{
				product = result;
				
				cart.init();
			});
		
		
	},
	
	addToCart: function(pno) {
		
		this.getProductInfo(pno);  // 장바구니에 담을 현제 상품의 정보 가져오기
		
		console.log("123123123123"+cart.name);
		
		
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
			beforeSend: function(xhr) {
				xhr.setRequestHeader(header, token)
			}
		}).done(result=>{
			carts = result;
			window._this.printPage();
		});
	},
};
	
window.onload = function() {
	main.init();
}