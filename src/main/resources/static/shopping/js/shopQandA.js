let goodsId = $('#goodsId').val();
console.log(goodsId)

let userId = $('#userId').val();
console.log(userId)

//문의리스트 가져오기
function getGoodsQna(goodsId, callback){

    $.ajax({
        url : `/shops/shopQnaList/${goodsId}`,
        type: 'get',
        dataType: 'json',
        success : function (result){
            console.log(result)
            shopDetailView(result)
            if(callback){
                callback(result);
            }
        }, error : function (a,b,c){
            console.error(c);
        }
    })
}

/**
 * 문의 페이지 이동 버튼 클릭시
 */
$('.qna-btn').on('click', function (e){
    e.preventDefault();
    getGoodsQna(goodsId, shopDetailView);
})

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
        text = `

          <div class="review-div">
<!--            <img src="/img/ICON-24px-Search.svg" alt="" class="input-img">-->
<!--            <input type="text" placeholder="문의 검색" class="selecReview" maxlength="130">-->
         <form action="/shops/shopQandaWriteModal" method="post" class="writingForm">

<button type="button" class="review-button">문의하기</button>

                 <tbody class="content">

                    <!-- 모달 창 들어 가는 부분  -->
                <div class="modal">
                    <div class="modal_body">
                        <strong style="font-size: 30px; " class="modal__userName">
                            문의하기
                        </strong>
                      <textarea name="queContent" class="modal_textarea" cols="30" rows="10" placeholder="문의 내용을 입력해주세요"></textarea>

                      <button type="button" class="review-submit">등록</button>
                    </div>
                </div>
                    <!-- 모달 창 들어 가는 부분 끝 -->
                </tbody>
            </form>
          </div>
        `;
    result.forEach(r => {
        if(r.id != null){
            text +=`
          <!-- QandA li -->
          <div class="comments">
            <div class="commentlist">
                <div class="comment-text">
                  <p class="reviewWriter">
                  `;

        //사용자 닉네임 처리
        if(r.userNickName ==null){
            text+=`<span class="userName">${r.userAccount}</span>`;
        }else if(r.userNickName != null){
            text+=`<span class="userName">${r.userNickName}</span>`;
        }
        text +=`
            <span>-</span>
            <time>${formatDate(r.queRegisterDate)}</time>
          </p>

          <div class="description">
          <p class="userContent">${r.queContent}</p>
          </div>
                  
              </div>
              
            <!-- 관리자 QandA -->
            ${r.queReplyContent !== null ? `
            <div class="admin-review-box">
            <div class="admin-review">
              <div>
                <span>관리자 - </span>
                <span>${formatDate(r.queReplyRegisterDate)}</span>
                <p>${r.queReplyContent}</p>
              </div>

            </div>` : ''}
          </div>
            <!-- 관리자 QandA 끝 -->
            </div>
          </div>
          <!-- QandA li 끝 -->
      </div>
        `;
        }else {
          text += ` <div>등록된 문의글이 없습니다.</div>
        `}

    });
    inputSection.html(text)
}

// 문의 하기 버튼 클릭시
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

/**
 * 모달 창 닫기, 내용 존재 여부에 따른 처리
 */
$(document).ready(function() {

        $('.shop-form').on('click', '.modal', function (e){
            let modal = $('.modal');
            if (e.target === modal[0]) {
                modal.removeClass("show");
            }
        })

    // 모달 안의 등록 버튼 클릭 시 모달 창 닫기 및 폼 전송
    $('.shop-form').on("click", '.review-submit',function(e) {

        let modal = $('.modal');
        let id =$('#goodsId').val()
        let modalTextarea = $(".modal_textarea").val();

        if (modalTextarea !== null && modalTextarea !== undefined && modalTextarea !== "") {

            console.log(modalTextarea)
            e.preventDefault();
            modal.removeClass("show");

            console.log(id)
            $.ajax({
                url: '/shops/shopQandaWriteModal',
                type: 'post',
                data: {
                    queContent: modalTextarea,
                    goodsId: id,
                },
                success: function (result) {
                    console.log(result);
                    getGoodsQna(goodsId, shopDetailView);
                },
                error: function (error) {
                    console.error(error);
                }
            });
        }else{
            alert("내용을 입력해 주세요!")
        }
    });
});
