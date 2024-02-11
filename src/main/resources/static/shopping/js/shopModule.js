let userId = $('#userId').val()
console.log(userId)

// 검색폼 (카테, 키워드 반환)
function searchGoodsForm(){
    let cate = $('#search-cate').val();
    let keyword = $('#shop-search-keyword').val();
    console.log("카테"+cate)
    console.log("키워"+keyword)
    return {
        cate : cate,
        keyword : keyword
    };
}
// // 검색폼의 선택값에 따라 카테고리 값을 설정하는 함수
// function updateCategory(cate) {
//     searchGoodsForm.cate = cate;
//     console.log($('#search-cate').val())
//     shop(0,searchGoodsForm(),'goods', 'goodsList', showShopList);
// }
// // 검색 결과 조회
// $('.result-submit-btn').on('click', function (){
//     shop(0,searchGoodsForm(),'goods', 'goodsList', showShopList);
// });
// //초기 데이터(쇼핑 리스트 조회)
// $(document).ready(function (){
//     shop(0,searchGoodsForm(),'goods', 'goodsList', showShopList);
//     enterKey('#shop-search-keyword', '.goods-list-search-btn');
//     // 검색폼 변경시 조회
//     $('#search-cate').change(function() {
//         updateCategory();
//     });
// })

//엔터키
function enterKey(a,b){
    $(a).keypress(function(e){
        //검색어 입력 후 엔터키 입력하면 조회버튼 클릭
        if(e.keyCode && e.keyCode == 13){
            $(b).trigger("click");
            return false;
        }
        //엔터키 막기
        if(e.keyCode && e.keyCode == 13){
            e.preventDefault();
        }
    });
}

// 검색어 입력창에서 검색 버튼 클릭 시 동작
$('.result-submit-btn').on('click', function () {
    // 전역 변수에 할당하지 않고, 함수 호출 시에 값을 전달

    // 카테고리 값 가져오기
    let cate = $('#search-cate').val();
    // shop 함수 호출 시 카테고리 값을 전달
    shop(0, searchGoodsForm(), cate);
});

//리스트
function showShopList(result) {
    let text = '';
    let textInput = $('.ul-list');

    result.forEach(r => {
        text += createShopListItem(r);
    });

    textInput.html(text);
}

// 리스트 아이템 생성 함수
function createShopListItem(r) {
    let listItem = `
        <li class="li-list">
            <a href="/shop/shopDetail/${r.id}">
                <div class="list-div"> 
        `;

    listItem += `
            <div class="li-list-full-img">
                <img src="/shops/shopImg?fileFullPath=${r.goodsMainImgPath}/${r.goodsMainImgUuid}_${r.goodsMainImgName}" alt="" class="li-img" >
            </div>
        `;

    listItem += `
            <div>
                <div class="list-name1"></div>
                <div class="list-name2">${r.goodsName}</div>
            </div>
            <div class="reviw">
                <img src="https://store.bemypet.kr/wp-content/themes/bemypet-store-child/assets/images/star_rate@3x.png" alt="" width="16">
                &nbsp;
                <div class="review-avg">
                    ${r.ratingAvg >= 0.5 ? '★' : '☆'}
                    ${r.ratingAvg >= 1.5 ? '★' : '☆'}
                    ${r.ratingAvg >= 2.5 ? '★' : '☆'}
                    ${r.ratingAvg >= 3.5 ? '★' : '☆'}
                    ${r.ratingAvg >= 4.5 ? '★' : '☆'}
                </div>
                <div class="reviw1">
                    <span>리뷰</span>
                    <span>&nbsp; ${r.reviewCount}</span>
                </div>
            </div>
            <div class="won">
                <span id="price">${addCommas(r.goodsPrice)}</span>
                <span>&nbsp;원</span>
            </div>
        </div>
    </a>
    <div class="basket">
     <a class="" href="" data-id="${r.id}"><img src="/img/shoppingcart.png" alt="" ></a>
    </div>
    </li>
    `;

    return listItem;
}

function pagination(result) {
    let paginations = $('.pagination-ul');
    paginations.empty();

    const totalPages = result.totalPages;
    const currentPage = result.number;

    if (totalPages > 0) {
        const maxButtons = 5;
        let startPage = Math.max(0, currentPage - Math.floor(maxButtons / 2));
        let endPage = Math.min(totalPages - 1, startPage + maxButtons - 1);

        if (totalPages <= maxButtons) {
            startPage = 0;
            endPage = totalPages - 1;
        } else if (endPage - startPage < maxButtons - 1) {
            startPage = Math.max(0, totalPages - maxButtons);
        }

        if (currentPage > 0) {
            paginations.append(`<li><a href="#" data-page="${currentPage - 1}">&lt;</a></li>`);
        }

        for (let i = startPage; i <= endPage; i++) {
            paginations.append(`<li><a href="#" data-page="${i}">${i + 1}</a></li>`);
        }

        if (currentPage < totalPages - 1) {
            paginations.append(`<li><a href="#" data-page="${currentPage + 1}">&gt;</a></li>`);
        }
    }

    paginations.find('a').on('click', function (e) {
        e.preventDefault();
        const page = parseInt($(this).data('page'));
        shop(page, searchGoodsForm());
    });
}

// 장바구니에 상품 추가
$('.ul-list').on('click','.basket a', function (e){
    e.preventDefault();
    userId = $('#userId').val();
    let goodsId = $(this).data('id');
    let cartItemQuantity = '1';
    console.log(goodsId)
    console.log(cartItemQuantity)

    if (userId !== null && userId !== undefined && userId !== "") {
        if(confirm("장바구니에 추가하시겠습니까?")){
            // 서버로 장바구니에 추가하는 요청을 보냄
            $.ajax({
                url:`/shops/shopCart/${userId}`,
                type: 'get',
                data: {
                    goodsId: goodsId,
                    cartItemQuantity : cartItemQuantity
                },
                success : function (result){
                    console.log(result + "장바구니 추가 성공");
                    if(confirm("장바구니 페이지로 이동하시겠습니까?")){
                        window.location.href="/shop/shopCart/" + userId;
                    }
                }, error : function (a, b, c){
                    console.error(c);
                }
            });
        }
    } else {
        // userId가 null 또는 빈 문자열이면 로그인 페이지로 이동
        window.location.href = '/user/enterLogin';
    }
});

// 콤마 찍기 함수
function addCommas(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

// 문의 하기 버튼 클릭시 이벤트
$('.shop-form').on('click', '.review-button', function () {
    let userId = $('#userId').val();
    console.log(userId);
    console.log("넘어왔나?");

    if (userId !== null && userId !== undefined && userId !== "") {
        // 모달 창에 'show' 클래스를 추가하여 모달을 표시
        $('.modal').addClass('show');
    } else {
        // userId가 null 또는 빈 문자열이면 로그인 페이지로 이동
        window.location.href = '/user/enterLogin';
    }
});