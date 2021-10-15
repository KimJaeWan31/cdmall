let member = undefined;
$(document).ready(() => {
    $.ajax("/members/member").done(result=>{
			member = result;
			printInfo();
	});
    total_fee_cal();
    let product_fee = $("#checkout_price").text();

    //결제창
    $("#payment").on("click", () => {
        //$.ajax({})
        let IMP = window.IMP; // 생략가능
        IMP.init('imp83437163'); 
        IMP.request_pay({
            pg: 'html5_inicis',
            pay_method: 'card',
            merchant_uid: 'merchant_' + new Date().getTime(),
            name: '주문명:결제테스트',
            amount: $("#checkout_total").text(), 
            buyer_email: 'iamport@siot.do',
            buyer_name: $("#customer_name").text(),
            buyer_tel: $("#tel").val(),
            buyer_addr: $("#address").val + $("#detail_address").val,
            buyer_postcode: '123-456',
            m_redirect_url: 'https://www.yourdomain.com/payments/complete'
        }, function (rsp) {
            console.log(rsp);
            if (rsp.success) {
                var msg = '결제가 완료되었습니다.';
                msg += '고유ID : ' + rsp.imp_uid;
                msg += '상점 거래ID : ' + rsp.merchant_uid;
                msg += '결제 금액 : ' + rsp.paid_amount;
                msg += '카드 승인번호 : ' + rsp.apply_num;
            } else {
                var msg = '결제에 실패하였습니다.';
                msg += '에러내용 : ' + rsp.error_msg;
            }
         alert(msg);
        });
    });

    //주소 받아오기
    $("#address_search").on("click", ()=>{
        new daum.Postcode({
            oncomplete: function(data){

                let addr = data.roadAddress;
                if (data.userSelectedType === 'R')
                    addr = data.roadAddress; // 도로명 주소    
                else  
                    addr = data.jibunAddress; // 지번 주소
    
                $("#address").val(addr);
                $("#detail_address").focus();  
            }
        }).open();
    });
    
    //주소록 열기
    $("#my_address").on("click", ()=>{
     	console.log("aa");
			let url = '/payment/order_address';
  			let name = 'addressOpen';
  			let option = "width=500, height=600, left=500, top=250";
  			window.open(url, name, option);
	});

    //메모
    $("#memo").on("change", memo_select);

     $("#isSame").on("change", ()=>{
        if($("#isSame").is(':checked')==true){
            $("#ship_name").val($("#customer_name").text());
            $("#ship_tel").val($("#customer_tel").text());
        }else{
            $("#ship_name").val("");
            $("#ship_tel").val("");
        }
    });
});

//총결제액 계산
const total_fee_cal =()=>{
    let product_fee = Number($("#checkout_price").text());
    let shipping_fee = Number($("#checkout_shipping").text());
    let discount_fee = Number($("#checkout_discount").text());
    $("#checkout_total").text(product_fee + shipping_fee - discount_fee)
}

const memo_select =()=>{
    let selected = $("#memo").val();

    if(selected !=0)
        $("#input_memo").val(selected).attr("disabled", "true");
    else
        $("#input_memo").val("").removeAttr("disabled");
}

const printInfo =()=>{
	$("#customer_name").text(member.irum);
	$("#customer_email").text(member.email);
	$("#customer_tel").text(member.tel);
	$("#checkout_point").text(member.point);
	
}