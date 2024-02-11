let userId = $('#userId').val();
console.log(userId)

$(document).ready(function () {
    getGoodsPickList(getPickList);
    getGoodsPickList(updateTotalPrice);
});

function getGoodsPickList(callback){

    $.ajax({
        url : `/shops/goodsPickList`,
        type : "get",
        success : function(result){
            console.log(result);

            if (callback){
                callback(result)
            }
        },
        error: function(a, b, c) {
            console.error(c);
        }
    })
}

function getPickList(result){
    let text = '';
    let inputSection = $('.cart_list')

    result.forEach(r=>{
        text += `
        <div className="product-total">
            <span> ${r.goodsName} </span>
            <span class="goodsPrice">${addCommas(r.goodsPrice)}</span> x
            <span class="goodsQuantity">${r.goodsQuantity}</span> =
            <span id="price" class="goodsTotal" data-price="${r.goodsPrice}">${addCommas(r.goodsPrice * r.goodsQuantity)}</span>
        </div>
        `
    })
    inputSection.html(text);
}

// 콤마 찍기
function addCommas(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

// 총 결제 금액 업데이트 함수
function updateTotalPrice() {
    let dynamicPricesArray = [];

    $('.goodsTotal').each(function () {
        dynamicPricesArray.push(parseInt($(this).text().replace(/[^\d]+/g, ''), 10));
        // 숫자만 추출하여 배열에 추가
    });
    let totalPrice = dynamicPricesArray.reduce((sum, price) => sum + price, 0);
    $('#total').text(addCommas(totalPrice) + ' 원');
}

// 카카오 페이 API
$(".cart-button").click(function () {

    const payAmount = parseInt($('#total').text().replace(',',''))
    const orderUserName = ($('#userName')).val()
    const orderUserAddressNumber = ($('#addressPost')).val();
    const orderAddressNormal = $('#orderAddressNormal').val();
    const orderAddressDetail = ($('#addressDetail')).val();
    const orderAddressDetails = ($('#addressDetails')).val();
    const orderUserPhoneNumber = ($('#userPhone')).val();
    const orderUserEmail = ($('#userEmail')).val();
    const orderMemo = ($('#orderMemo')).val();

    console.log("주문자 이름: " + orderUserName);
    console.log("도로명 및 번지: " + orderAddressNormal);
    console.log("주문 메모: " + orderMemo);

    var IMP = window.IMP; // 생략가능
    IMP.init('imp24106650');
    // i'mport 관리자 페이지 -> 내정보 -> 가맹점식별코드
    // ''안에 띄어쓰기 없이 가맹점 식별코드를 붙여넣어주세요. 안그러면 결제창이 안뜹니다.
    IMP.request_pay({
        pg: 'kakaopay.TC0ONETIME',
        pay_method: 'card',
        merchant_uid: 'merchant_' + new Date().getTime(),

        name: '(주)산책갈께 카카오 결제',
        amount: payAmount,

        buyer_name: orderUserName,
        buyer_postcode: orderUserAddressNumber,
        buyer_addr : orderAddressNormal,
        buyer_addr_d : orderAddressDetail,
        buyer_addr_ds : orderAddressDetails,
        buyer_tel : orderUserPhoneNumber,
        buyer_email : orderUserEmail,
        buyer_memo : orderMemo,

    }, function (rsp) {
        console.log(rsp);
        if (rsp.success) {
            var msg = '결제가 완료되었습니다.';
            msg += '결제 금액 : ' + rsp.paid_amount;

            console.log("주소노말"+orderAddressNormal)
            console.log("메모"+orderMemo)

            $.ajax({
                url : `/orders/orderList`,
                type : "post",
                data : JSON.stringify({
                    payAmount : payAmount,
                    orderUserName : orderUserName,
                    orderUserAddressNumber : orderUserAddressNumber,
                    orderAddressNormal : orderAddressNormal,
                    orderAddressDetail : orderAddressDetail,
                    orderAddressDetails : orderAddressDetails,
                    orderUserPhoneNumber : orderUserPhoneNumber,
                    orderUserEmail : orderUserEmail,
                    orderMemo : orderMemo
                }),
                contentType : "application/json; charset=utf-8",
                success : function(response){
                    console.log(response);

                    window.location.href="/mypg/orderpage/" +userId;
                }
            })

        } else {
            var msg = '결제에 실패하였습니다.';
            msg += '에러내용 : ' + rsp.error_msg;
        }
        alert(msg);
    });
});

//다음 주소API
function addressFind() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if(data.userSelectedType === 'R'){
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                } console.log(extraAddr);
                // 조합된 참고항목을 해당 필드에 넣는다.
                document.getElementById('addressDetail').value = extraAddr;
            
            } else {
                document.getElementById('addressDetail').value = '';
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('addressPost').value = data.zonecode;
            document.getElementById('orderAddressNormal').value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById('addressDetails').focus();
        }
    }).open();
    console.log("성공!");
}
