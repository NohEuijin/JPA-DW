import * as reply from './module/reply.js';

const slides = document.querySelector('.slider'); // 전체 슬라이드 컨테이너
const slideImg = document.querySelectorAll('.slider li'); // 모든 슬라이드들
let currentIdx = 0; // 현재 슬라이드 index
const slideCount = slideImg.length; // 슬라이드 개수
const prev = document.querySelector('.prev'); // 이전 버튼
const next = document.querySelector('.next'); // 다음 버튼
const slideWidth = 300; // 한개의 슬라이드 넓이

// 전체 슬라이드 컨테이너 넓이 설정
slides.style.width = slideWidth * slideCount + 'px';

function moveSlide(num) {
  slides.style.left = -num * 300 + 'px';
  currentIdx = num;
}

prev.addEventListener('click', function () {
  if (currentIdx !== 0) moveSlide(currentIdx - 1);
});

next.addEventListener('click', function () {
  if (currentIdx !== slideCount - 1) {
    moveSlide(currentIdx + 1);
  }
});

//날짜(포맷변경)
function formatDates(dateString) {
  let dateObj = new Date(dateString);
  let formattedDate = dateObj.getFullYear() +'-' + ('0' + (dateObj.getMonth() + 1)).slice(-2) +'-' +('0' + dateObj.getDate()).slice(-2) +' ' + ('0' + dateObj.getHours()).slice(-2) +':' +('0' + dateObj.getMinutes()).slice(-2);
  return formattedDate;
}
$('.info-date').text(formatDates($('.text-date').text()))

// 자유게시판 글 수정
$('.update-btn').on('click', function () {
  let freeBoardId = $(this).data('freeboardid');
  window.location.href = "/community/modify/" + freeBoardId;
});

// 자유게시판 글 삭제
$('.remove-btn').on('click', function () {
  let freeBoardId = $(this).data('freeboardid');
  if (confirm("글을 삭제하시겠습니까?")) {
    window.location.href = "/community/delete/" + freeBoardId;
  }
});

// 자유게시판 글 목록으로 이동
$('.list-btn').on('click', function () {
  window.location.href = "/community/freeBoardList";
});

/**
 * 여기서부터 댓글 js
 */
// 댓글 목록을 보여주는 함수를 호출하여 초기화면에 댓글을 표시
let freeBoardId = $('#freeBoardId').val();
showReplyList(freeBoardId);

let loginNumber = $('#userId').val();
console.log(loginNumber)

/**
 * '댓글 저장' 버튼 클릭시
 */
$('.btn-reply').on('click', function () {
  let content = $('#reply-content').val();
  let freeBoardId = $('#freeBoardId').val();
  let userId = loginNumber;

  // 로그인 여부 체크
  if (!userId) {
    alert('로그인 후 사용해주세요!');
    return;
  }
  // 댓글 내용 입력 여부 체크
  if (!content) {
    alert('댓글 내용을 입력해주세요!');
    return;
  }

  // 댓글 길이 체크 (200자 이내)
  if (reply.getTextLength(content)>200) {
    alert('댓글은 200자 이내로 작성해주세요!');
    return;
  }

  let replyObj = {
    freeBoardCommentContent: content,
    freeBoard: { id: freeBoardId },
    users: { id: userId },
  };

  // 댓글 저장 함수
  getReply(replyObj);

  // 댓글 저장 후 댓글 목록 갱신
  showReplyList(freeBoardId);
  $('#reply-content').val('');
});

//댓글 저장
function getReply(object){
  $.ajax({

    url:'/replies/writeComment',
    type:'post',
    data:JSON.stringify(object),
    contentType:'application/json; charset=utf-8',
    success : function (result){
    console.log(result)
    // 댓글 저장 후 댓글 목록 갱신
    showReplyList(freeBoardId);
    }
  })
}

//댓글 수정
export function modify(freeBoardCommentId, reply, callback){

  $.ajax({
    url : `/replies/modifyComment/${freeBoardCommentId}`,
    // 'patch' (업데이트 요청)
    type : 'patch',
    data : JSON.stringify(reply),
    contentType : 'application/json; charset=utf-8',
    success : function (){
      if(callback){
        callback();
      }
    },
    error : function (a, b, c){
      console.error(c);
    }
  });
}

//댓글 삭제
export function remove(freeBoardCommentId, callback) {
  $.ajax({
    url: `/replies/deleteComment/${freeBoardCommentId}`,
    type: 'delete',
    success: function () {
      if (callback) {
        callback();
      }
    },
    error: function (a, b, c) {
      console.error(c);
    }
  });
}

// 댓글 목록
function showReplyList(freeBoardId) {
  $.ajax({
    url: `/replies/readComment/${freeBoardId}`,
    type: 'get',
    dataType: 'json',
    success: function (result) {
      console.log(result)
      const text = freeBoardCommentList(result);
      $('.reply-list').html(text);

    },
    error: function (a, b, c) {
      console.error(c);
    }
  });
}

// 댓글 목록 HTML 생성
function freeBoardCommentList(result) {
  let text = '';

  result.forEach(r => {
    text += `
      <div class="reply" >
        <div class="id-and-date">
          <div class="reply-info">
                  `;
    //사용자 이미지 등록 처리
    if(r.userImgId ==null){
      text += ` <div class="reply-img"><img src="/img/dogImg02.jpg" alt=""></div>`;
    }else {
      text += `<div class="reply-img"><img src="/communities/freeUserImg?userImgPath=${r.userImgPath +'/' + r.userImgUuid +'_' + r.userImgName}" alt=""></div>`
    }
    //사용자 닉네임 처리
    if(r.userNickName == null){
      text += `<div class="reply-id">${r.userAccount}</div>`;
    }else{
      text +=`<div class="reply-id">${r.userNickName}</div>`;
    }

    text += ` 
          </div>
          <div class="reply-date">${r.freeBoardCommentMd ? timeForToday(r.freeBoardCommentMd) : timeForToday(r.freeBoardCommentRd)}</div>
        </div>
        <div class="reply-content-n-btns">
          <div class="reply-content"><p>${r.freeBoardCommentContent}</p></div>
`;
    //수정, 삭제 버튼 처리
    if (r.userId == loginNumber) {
      text += `
        <div class="reply-btns__box ">
          <button type="button" class="reply-modify-btn" data-num="${r.id}" >수정</button>
          <button type="button" class="reply-remove-btn" data-num="${r.id}" >삭제</button>
        </div>`;
    }

    text += `
        </div>
      </div>
    </div>`;
  });

  return text;
}

// 댓글 버튼 클릭 시
$('.reply-list').on('click', '.reply-btns', function () {
  // 댓글 버튼의 부모 요소인 .reply-btn-box에서 => 자식 .reply-btns__box을 찾아 저장
  let $replyBtnBox = $(this).closest('.reply-btn-box').find('.reply-btns__box');
  // 버튼 숨김
  $('.reply-btns__box').addClass('none');
  // 버튼 숨김/보임 상태 전환
  $replyBtnBox.toggleClass('none');
});

// 리플 삭제 버튼 처리
$('.reply-list').on('click', '.reply-remove-btn', function () {
  let freeBoardCommentId = $(this).closest('.reply').find('.reply-remove-btn').data('num');
  console.log(freeBoardCommentId)
  remove(freeBoardCommentId, function (){
    showReplyList(freeBoardId);
  });
});

// 리플 수정 버튼 처리
$('.reply-list').on('click', '.reply-modify-btn', function () {
  let $content = $(this).closest('.reply').find('.reply-content');

  $content.replaceWith(`
  <div class='modify-box'>
    <textarea class='modify-content'>${$content.text()}</textarea>
    <button type='button' class='modify-content-btn'>수정완료</button>
  </div>
  `);
  $('.reply-btns__box').addClass('none');
});

// 리플 수정 완료 처리
$('.reply-list').on('click', '.modify-content-btn', function () {
  console.log('modify!!!');
  let freeBoardCommentId = $(this).closest('.reply').find('.reply-modify-btn').data('num');
  console.log(freeBoardCommentId);

  let freeBoardCommentContent = $(this).closest('.modify-box').find('.modify-content').val();
  console.log(freeBoardCommentContent);

  let replyObj = {
    freeBoardCommentContent : freeBoardCommentContent,
    id : freeBoardCommentId
  };
  $('.reply-btns__box').removeClass('none');

  modify(freeBoardCommentId, replyObj, function (){
    showReplyList(freeBoardId);
  });
});

// 댓글 시간
export function timeForToday(value){
  const today = new Date(); //현재 날짜와 시간을 가진 객체
  const timeValue = new Date(value);
  // getTime() 1970년 1/1일을 기준으로 지금까지 몇 ms가 지났는지 알려준다.
  //Math.floor() 는 소수점을 버림 처리 해준다.
  const betweenTime = Math.floor((today.getTime() - timeValue.getTime()) / 1000 / 60);

  if(betweenTime < 1) { return "방금 전"; }

  if(betweenTime < 60) { return `${betweenTime}분 전`; }

  const betweenTimeHour = Math.floor(betweenTime/60);
  if(betweenTimeHour < 24) { return `${betweenTimeHour}시간 전`; }

  const betweenTimeDay = Math.floor(betweenTimeHour/ 24);
  if(betweenTimeDay < 365) { return `${betweenTimeDay}일 전`; }

  return `${Math.floor(betweenTimeDay / 365)}년 전`;
}