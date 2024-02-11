let goodsId = $('#goodsId').val();
console.log(goodsId)

// 추가정보 버튼 눌럿을시
$('.info-btn').on('click', function(e){
    e.preventDefault();
    getGoodsAddInfo(goodsId, shopAddInfoView)
})

function getGoodsAddInfo(goodsId, callback){

    $.ajax({
        url : `/shops/shopAddInfo/${goodsId}`,
        type : 'get',
        dataType : 'json',
        success : function(result){
            console.log(result+"@@@@@@@@@@@@@@")
            if(callback){
                callback(result)
            }

        }
    })
}

function shopAddInfoView(result){
    let text ='';
    let inputSection = $('.row-content');

    text += `
        <p class="addInfoTitle">추가정보</p>
            <table class="infoTable">
                <colgroup>
                  <col width="150px">
                  <col width="340px">
                  <col width="150px">
                </colgroup>
                <tbody>
                  <tr>
                    <th>품명 및 모델명</th>
                    <td>${result.goodsName}</td>
                    <th>인증/허가 사항</th>
                    <td>사료관리법에 따른 성분등록번호 : ${result.goodsCertify}</td>
                  </tr>
                  <tr>
                    <th>제조국(원산지)</th>
                    <td>${result.goodsMade}</td>
                    <th>제조자(수입자)</th>
                    <td>컨텐츠 참조</td>
                  </tr>
                  <tr>
                    <th>소비자상담 관련 전화번호</th>
                    <td colspan="3">고객센터 1577-7011</td>
                  </tr>
                </tbody>
            </table>
    `
    inputSection.html(text)
}
