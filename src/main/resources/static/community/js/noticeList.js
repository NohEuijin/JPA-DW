import * as list from './module/list.js'
import * as page from './module/page.js';

// 검색버튼
$('.search-btn').on('click', function (){
    list.list(0,searchNoticeForm(),'notices','notice',showNoticeList);
});

// 페이지에 리스트 뿌려주는 스크립트
$(document).ready(function (){
    console.log("시작")
    list.list(0,searchNoticeForm(),'notices','notice',showNoticeList);
    page.enterKey('.search-notice','.search-btn');
});

//input에서 검색한 내용 넘겨주기
function searchNoticeForm(){
    let keyword = $('.search-notice').val();
    console.log(keyword);
    return {
        keyword : keyword
    };
}

/**
 * 자유게시판 비동기 리스트
 * 시간, 페이징
 */
function showNoticeList(result) {
    let text = '';
    let textInput = $('.notice-list');

    result.content.forEach(r => {
        let formattedDate;
        if(r.noticeBoardMd == null){
            formattedDate = formatDates(r.noticeBoardRd)
        }else{
            formattedDate = formatDates(r.noticeBoardMd)
        }

        text += `
        <tr class="body-columns">
            <td>${r.id}</td>
            <td><a href="/notice/noticeDetail/${r.id}">${r.noticeBoardTitle}</a></td>
            <td>관리자</td>
            <td>${formattedDate}</td>
            <td class="noticeBoardViewCount">${r.noticeBoardViewCount}</td>
        </tr>
            `;
    })

    textInput.html(text);

    let paginations = $('.pagination-ul');

    page.pagination(result,paginations)
    paginations.find('a').on('click', function (e) {
        e.preventDefault();
        const page = parseInt($(this).data('page'));
        list.list(page,searchNoticeForm(),'notices','notice',showNoticeList);
    });

    console.log($('.noticeBoardViewCount').text());
}
// 날짜 포맷 변경 함수
function formatDates(dateString) {
    let dateObj = new Date(dateString);
    let formattedDate = dateObj.getFullYear() + '-' + ('0' + (dateObj.getMonth() + 1)).slice(-2) + '-' + ('0' + dateObj.getDate()).slice(-2) + ' ' + ('0' + dateObj.getHours()).slice(-2) + ':' + ('0' + dateObj.getMinutes()).slice(-2);
    return formattedDate;
}

//자유게시판 인기글 top3
// 날짜 포맷 변경
function formatDates2(dateString) {
let dateObj = new Date(dateString);
let formattedDate =
dateObj.getFullYear() +
'-' +
('0' + (dateObj.getMonth() + 1)).slice(-2) +
'-' +
('0' + dateObj.getDate()).slice(-2) +
' ' +
('0' + dateObj.getHours()).slice(-2) +
':' +
('0' + dateObj.getMinutes()).slice(-2);
return formattedDate;
}
//날짜 요소 선택
let dateElements = $('.best-content-date');
// 각 날짜 요소에 대해 처리
dateElements.each(function (index, element) {
    // .info-date .best-content-date 요소 찾기
    let originalDate = $(element).closest('.best-container-content').find('.best-content-etc .best-content-date').text();
    // 날짜 포맷 변경
    let formattedDate = formatDates2(originalDate);
    // 변경된 형식으로 날짜를 설정
    $(element).text(formattedDate);
});

