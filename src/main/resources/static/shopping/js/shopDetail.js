//각 버튼에 대한 클래스 변경
$('.img-btn, .info-btn, .review-btn, .qna-btn').on('click', function (e) {
    e.preventDefault();
    // 모든 .middle-span 요소의 클래스를 초기화
    $('.middle-span').removeClass('middle-span1');

    // 클릭된 버튼에 대한 처리
    let middleSpan = $(this).find('.middle-span');
    middleSpan.addClass('middle-span1');

    // 각 버튼에 대한 추가 로직
    if ($(this).hasClass('img-btn')) {
        getDetailImgs(goodsId, detailImgList);
        // 설명 버튼에 대한 추가 로직
    } else if ($(this).hasClass('info-btn')) {
        // getGoodsAddInfo(goodsId, shopAddInfoView)
        // 추가 정보 버튼에 대한 추가 로직
    } else if ($(this).hasClass('review-btn')) {
        // 리뷰 버튼에 대한 추가 로직
    } else if ($(this).hasClass('qna-btn')) {
        // getGoodsQna(goodsId, shopDetailView);
        // 문의 버튼에 대한 추가 로직
    }
});

//goodId 값 가져오기
let goodsId = $('#goodsId').val();
console.log(goodsId)

//첫 화면 진입시 
$(document).ready(function (){
    getDetailImgs(goodsId, detailImgList)
})

// 상세 페이지 제품 상세 사진 가져오기
function getDetailImgs(goodsId, callback){

    $.ajax({
        url : `/shops/shopDetilImgs/${goodsId}`,
        type:'get',
        dataType:'json',
        success : function (result){
            console.log(result);

            if(callback){
                callback(result)
            }
        }
    })
}

let userId = $('#userId').val();

//장바구니 버튼 처리
$('.basket1 button').on('click', function (e){
    e.preventDefault();
    userId = $('#userId').val();

    if(confirm("장바구니에 추가하시겠습니까?")){
        let cartItemQuantity = $('#number').text();
        let goodsId = $('#goodsId').val()

        $.ajax({
            url:`/shops/shopCart/${userId}`,
            type: 'get',
            data: {
                goodsId : goodsId,
                cartItemQuantity : cartItemQuantity
            },
            success : function (result){
                console.log(result+"장바구니 추가 성공");
                if(confirm("장바구니 페이지로 이동하시겠습니까?")){
                    window.location.href="/shop/shopCart/" +userId;
                }
            },error : function (a,b,c){
                console.error(c)
            }
        })
    }
})

/**
 * 바로구매 버튼 클릭 시 데이터를 수집
 */
$('.basket-button2').on('click', function () {
    let order = [];
    let currentPrices = $('#price').text().replace(',','');
    let goodsName = $('#goodsName').text();
    let goodsId = $('#goodsId').val();
    let cartItemQuantity = $('#number').text().replace(',','');
    userId = $('#userId').val();

        let product = {
            goodsId: goodsId,
            goodsName: goodsName,
            goodsQuantity: cartItemQuantity,
            goodsPrice: currentPrices,
        };
        order.push(product);

    console.log(order);

    $.ajax({
        url : '/shops/payGoods',
        type : 'post',
        data: JSON.stringify(order),
        contentType:'application/json; charset=utf-8',
        success : function (result){
            if(confirm("결제페이지로 이동하시겠습니까?")){
                window.location.href="/shop/shopSinglePay/" + userId;
            }
        },error : function (a,b,c){
            console.error(c)
        }
    })
});

function detailImgList(result){
    let text = '';
    let inputSection = $('.row-content');

    result.forEach(r=>{
        text += `
            <img src="/shops/shopImg?fileFullPath=${r.goodsDetailImgPath + '/' + r.goodsDetailImgUuid + '_'+ r.goodsDetailImgName}" alt="">
        `
    })
    inputSection.html(text);
}

// 콤마 찍기
function addCommas(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}
// 콤마 찍기 후 실행
document.addEventListener("DOMContentLoaded", function () {
    // detail.goodsPrice의 값을 가져와서 숫자로 변환 후 콤마 추가하여 출력
    let goodsPrice = document.getElementById("price").textContent;
    let parsedGoodsPrice = parseInt(goodsPrice.replace(/[^0-9]/g, ""));
    let formattedPrice = addCommas(parsedGoodsPrice);
    document.getElementById("price").textContent = formattedPrice + " ";
});

// 수량의 증가와 감소 값 계산하기
document.addEventListener("DOMContentLoaded", function () {
    const numberElement = document.getElementById("number"); // 갯수
    const priceElement = document.getElementById("price"); // 금액
    const increaseButton = document.getElementById("increase"); // 증가
    const decreaseButton = document.getElementById("decrease"); // 감소

    let quantity = 1;
    let unitPrice = parseFloat(priceElement.innerText.replace(" ", "").replace(",", ""));

    // 수량과 가격을 계산한 후에 해당 값을 화면에 업데이트
    function updatePriceAndQuantity() {
        const totalPrice = quantity * unitPrice;
        priceElement.innerText = totalPrice.toLocaleString() + " ";
        numberElement.innerText = quantity;
    }

    // 수량을 증가
    increaseButton.onclick = () => {
        quantity++;
        updatePriceAndQuantity();
    };

    // 수량을 감소
    decreaseButton.onclick = () => {
        if (quantity > 1) {
            quantity--;
            updatePriceAndQuantity();
        }
    };
});