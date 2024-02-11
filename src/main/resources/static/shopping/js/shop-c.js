function shop(page, searchForm, category) {
    $.ajax({
        url: `/shops/shop/${category}/${page}`,
        type: 'get',
        data: searchForm,
        dataType: 'json',
        success: function (result) {
            console.log(result.pageable);
            console.log(result.content);
            showShopList(result.content);
            pagination(result);
        },
        error: function (a, b, c) {
            console.error(c);
        }
    });
}

// 검색폼의 선택값에 따라 카테고리 값을 설정
function updateCategory(cate) {
    searchGoodsForm.cate = cate;
    console.log($('#search-cate').val())
    shop(0,searchGoodsForm(),'c', 'goodsList', showShopList);
}
// 검색 결과 조회
$('.result-submit-btn').on('click', function (){
    shop(0,searchGoodsForm(),'c', 'goodsList', showShopList);
});
//초기 데이터(쇼핑 리스트 조회)
$(document).ready(function (){
    shop(0,searchGoodsForm(),'c', 'goodsList', showShopList);
    enterKey('#shop-search-keyword', '.goods-list-search-btn');
    // 검색폼 변경시 조회
    $('#search-cate').change(function() {
        updateCategory();
    });
})