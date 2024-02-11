let userId = $('#userId').val();
console.log(userId)

// 화면 진입 시
$(document).ready(function () {
    getCart(userId, function (result) {
        getCartList(result);
        updateTotalPrice();
    });
});

function getCart(userId, callback){

    $.ajax({

        url : `/shops/shopCartList/${userId}`,
        type : "get",
        dataType : "json",
        success : function(result){
            console.log(result);

            if (callback){
                callback(result)
            }
        }
    })
}

// 리스트
function getCartList(result){
    let text = '';
    let inputSection = $('.cart_list');

    result.cartItemDetails.forEach(r=>{

        if (r.goodsId == null) {
            $('.remove-all-btn').hide();
        }

        text += `
        <div class="cart_table">
          <div class="cart_form">
            <div class="remove-button">
              <button type="button"  class="remove-btn" data-cartitemid="${r.id}">X</button>
            </div>
            <div class="thumbnail">
          <a href="/shop/shopDetail/${r.goodsId}">
            <img src="/shops/shopImg?fileFullPath=${r.goodsMainImgPath +'/'+ r.goodsMainImgUuid +'_'+ r.goodsMainImgName}" alt="">
          </a>
              <div class="thumbnail-dox">
                <div class="thumbnail-dox-div">
                  <a href="/shop/shopDetail/${r.goodsId}">
                  <input type="hidden" class="goodsId" value="${r.goodsId}">
                    <p class="goodsName">${r.goodsName}</p>
                    <p id="price" class="item-price" data-price="${r.goodsPrice}">${addCommas(r.goodsPrice * r.cartItemQuantity)}</p>
                    <input type="hidden" value="${r.goodsPrice}" id="goodsPrice">
                  </a>
                </div>
                <div class="quantity-button">
                  <div class="quantity-box">
                      <button id="increase" class="increase-btn"><strong>+</strong></button>
                      <div><span id="number" class="quantity" >${r.cartItemQuantity}</span></div>
                      <button id="decrease" class="decrease-btn" "><strong>-</strong></button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        `

    })
    inputSection.html(text);

}
    // Increase 및 Decrease 버튼 클릭 시
    function handleQuantityChange(button, increase) {
        const quantityElement = button.closest('.quantity-box').find('.quantity');
        let quantity = parseInt(quantityElement.text(), 10);

        quantity += increase ? 1 : -1;
        quantity = Math.max(quantity, 1); // 최소값을 1로 유지

        quantityElement.text(quantity);

        let goodsPrice = button.closest('.thumbnail-dox').find('.item-price').data('price');
        let dynamicPrice = button.closest('.thumbnail-dox').find('.item-price');

        dynamicPrice.text(addCommas(goodsPrice * quantity) + ' 원');
        updateTotalPrice();
    }

// Increase 버튼 클릭 시
    $('.cart_list').on('click','.increase-btn', function() {
        handleQuantityChange($(this), true);
    });

// Decrease 버튼 클릭 시
    $('.cart_list').on('click','.decrease-btn', function () {
        handleQuantityChange($(this), false);
    });

// 총 결제 금액 업데이트 함수
function updateTotalPrice() {
    let dynamicPricesArray = [];

    $('.item-price').each(function () {
        dynamicPricesArray.push(parseInt($(this).text().replace(/[^\d]+/g, ''), 10));
        // 숫자만 추출하여 배열에 추가
    });

    let totalPrice = dynamicPricesArray.reduce((sum, price) => sum + price, 0);
    $('#total').text(addCommas(totalPrice) + ' 원');

}

    /**
     * collectData : 특정 선택자의 텍스트 데이터를 수집
     * collectInputData : 특정 선택자의 입력 필드 값을 수집
     * collectDataName : 특정 선택자의 데이터 이름의 값을 수집
     */
    function collectData(selector) {
        let data = [];
        $(selector).each(function () {
            data.push($(this).text());
        });
        return data;
    }
    function collectInputData(selector) {
        let data = [];
        $(selector).each(function () {
            data.push($(this).val());
        });
        return data;
    }
    function collectDataName(selector, dataName) {
        let data = [];
        $(selector).each(function () {
            data.push($(this).data(dataName));
        });
        return data;
    }

    /**
     * 결제 버튼 클릭 시 데이터를 수집
     */
    $('.cart-button').on('click', function () {
        let order = [];
        let currentPrices = collectDataName('.item-price', 'price');
        let goodsName = collectData('.thumbnail-dox-div .goodsName');
        let goodsId = collectInputData('.thumbnail-dox-div .goodsId');
        let cartItemQuantity = collectData('.quantity-box .quantity');
        userId = $('#userId').val();

        for (let i = 0; i < goodsId.length; i++) {
            let product = {
                goodsId: goodsId[i],
                goodsName: goodsName[i],
                goodsQuantity: cartItemQuantity[i],
                goodsPrice: currentPrices[i],
            };
            order.push(product);
        }
        console.log(order);

        $.ajax({
            url : '/shops/cartGoods',
            type : 'post',
            data: JSON.stringify(order),
            contentType:'application/json; charset=utf-8',
            success : function (result){

                console.log(result)

                if (goodsId.length === 0) {
                    confirm("결제할 상품이 없습니다!")
                    return;
                }
                if(confirm("결제페이지로 이동하시겠습니까?")){
                    window.location.href="/shop/shopPay/" + userId;
                }

            },error : function (a,b,c){
                console.error(c)
            }
        })
    });

// 콤마 찍기
function addCommas(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

//카트 아이템 삭제
function removeCartItem(cartItemId, callback){

    $.ajax({
        url : `/shops/delete/${cartItemId}`,
        type : "get",
        success : function (result){
            console.log(result);

            if (callback){
                callback(result);
            }
        }
    })
}

//카트 아이템 삭제 버튼
$('.cart_list').on('click', '.remove-btn',function(){
    let cartItemId = $(this).data('cartitemid');
    console.log(cartItemId)
    if(confirm("상품을 삭제 하시겠습니까?")){
        removeCartItem(cartItemId, function (){
            getCart(userId, getCartList)
        })
    }
})

//카트 삭제
function removeCart(callback){
    $.ajax({
        url : `/shops/deleteAll`,
        type : "get",
        success : function (result){
            console.log(result)
            if (callback){
                callback(result);
            }
        }
    })
}

//카트 삭제 버튼
$('.remove-all-btn').on('click', function(){
    let goodsId = collectInputData('.thumbnail-dox-div .goodsId');

    //상품이 없으면
    if (goodsId.length === 0) {
        return;
    }
    if(confirm("상품을 모두 삭제 하시겠습니까?")){
        removeCart(function (){
            getCart(userId, getCartList)

        })

    }
})
