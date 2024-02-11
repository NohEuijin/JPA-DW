//상품 설명
let goodsId = $('#goodsId').val();
shopDetail(goodsId);
console.log(goodsId)

// 리뷰 버튼 눌럿을시
$('.review-btn').on('click', function(e){
    e.preventDefault();
    shopDetail(goodsId, shopDetailView)
})

// 리뷰 보여주는 함수
function shopDetail(goodsId, callback){
    // 댓글 목록을 비동기로 받아오기
    $.ajax({
        url: `/shops/shopReview/${goodsId}`,
        type: 'get',
        dataType: 'json',
        success: function (result) {
            console.log(result)
            if(callback){
                callback(result);
            }
        }
    })
}

// 날짜 포맷을 변경하는 함수
function formatDate(dateString) {
    const options = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' };
    const formattedDate = new Date(dateString).toLocaleString('en-US', options);
    return formattedDate;
}

// 상세 페이지
function shopDetailView(result) {
    let text = '';
    let inputSection = $('.row-content');

    if(result.length > 0) {
    result.forEach(r => {
            text +=`
          <!-- 리뷰 li -->
          <input type="hidden" value="${r.id}">
          <div class="comments">
            <div class="commentlist">
                <div class="comment-text">
                  <div class="star-div">
                    `;
                    if(r.rating == 1) {
                      text +=`<span class="rating-count">★</span>`;
                    }else if(r.rating == 2){
                        text +=`<span class="rating-count">★★</span>`;
                    }else if(r.rating ==3){
                        text +=`<span class="rating-count">★★★</span>`;
                    }else if(r.rating == 4){
                        text +=`<span class="rating-count">★★★★</span>`;
                    }else if(r.rating == 5){
                        text +=`<span class="rating-count">★★★★</span>`;
                    }
                text +=`
                  </div>
                  
                  <p class="reviewWriter">
                  `;
        //사용자 닉네임 처리
        if(r.userNickName ==null){
            text+=`<span class="userName">${r.userAccount}</span>`;
        }else if(r.userNickName != null){
            text+=`<span class="userName">${r.userNickName}</span>`;
        }
        text +=`
                    <span> - </span>
                    <time>${formatDate(r.reviewRd)}</time>
                  </p>

                  <div class="description">
                  <p>${r.content}</p>
                  </div>
                
                  <div class="reviewImg">
                  `;
                r.goodsReviewImgDtos.forEach(e => {
                text += `
                <div class="imgbox">
                    <img src="/mypgs/reviews?fileFullPath=${e.reviewimgPath + '/' + e.reviewimgUuid + '_' + e.reviewimgFileName}" alt="" class="imgs" >
                </div>
                `;
                });
            text += `
                  </div>
              </div>
<!-- 관리자 리뷰 -->
${r.goodsReviewReplyContent !== null ? `
<div class="admin-review-box">
    <div class="admin-review">
    <div>
    <span>관리자 - </span>
<span>${formatDate(r.goodsReviewReplyRD)}</span>
<p>${r.goodsReviewReplyContent}</p>
</div>

</div>
</div>` : ''}

<!-- 관리자 리뷰 끝 -->

            </div>
          </div>
          <!-- 리뷰 li 끝 -->
      </div>
        `;

    });
    } else {
        text += `<div>등록된 리뷰가 없습니다.</div>`;
    }
    inputSection.html(text)
}