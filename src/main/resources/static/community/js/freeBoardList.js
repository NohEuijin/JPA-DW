
$(document).ready(function (){
    freeBoardList(0,searchFreeBoardForm());
})

$('.ddd').on('click', function (){
    freeBoardList(0, searchFreeBoardForm());

})

let keyword = $('#freeBoard-search-keyword').val();

//input에서 받은 결과를 넘긴다.
function searchFreeBoardForm(){
    return {
        keyword : $('#freeBoard-search-keyword').val()
    };
}

function freeBoardList( page, keyword, callback){


    $.ajax({

        url:`/communities/freeBoardList/${page}`,
        type:'get',
        // data:searchForm,
        data: { keyword: keyword.keyword },
        dataType:'json',
        success :function (result){
            console.log(result.pageable)
            console.log(result.number)
            console.log(result.content)

            showFreeBoardList(result.content)
            pagination(result)

        },error : function (a,b,c){
            console.error(c);
        }


    })
}

//자유게시판 리스트
function showFreeBoardList(result) {
    let text = '';
    let textInput = $('.list-contents-box');

    result.forEach(r => {
        text += createFreeBoardListItem(r);
    });

    textInput.html(text);
}

function createFreeBoardListItem(r) {
    let listItem = `
        <a href="/community/freeBoardDetail/${r.id}">
            <div class="list-content">
                <div class="content-text-box">
                    <input type="hidden" value="${r.id}" name="freeBoardId">
                    <div class="list-content-title">${r.freeBoardTitle}</div>
                    <div class="list-content-content">${r.freeBoardContent}</div>
                    <div class="list-content-etc">
                        <div class="list-content-id">
                            <div class="list-content-id-img"><img src="/img/dogImg.jpg" alt=""></div>
                            <span>${r.userAccount}</span>
                        </div>
                        <div class="list-content-reply">
                            <span>조회수 ${r.freeBoardViewCount}</span>
                            <span class="reply-count"></span>
                        </div>
                        <div class="list-content-time">
                            <span>${r.freeBoardRd}</span>
                            <div class="list-content-time">${r.freeBoardMd}</div>
                        </div>
                    </div>
                </div>
                <!-- 추가 부분 -->
                <div class="content-img-box">
                    <div class="content-img">
                        <img src="${r.freeBoardImgRoute}/${r.freeBoardImgUuid}" alt="${r.freeBoardImgName}">
                    </div>
                </div>
            </div>
        </div>
    </a>`;

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
        freeBoardList(page, searchFreeBoardForm());
    });
}


// 자유게시판 게시글 조회
// $(function() {
//     $("#freeBoard-search-keyword").keypress(function(e){
//         //검색어 입력 후 엔터키 입력하면 조회버튼 클릭
//         if(e.keyCode && e.keyCode == 13){
//             $(".result-btn").trigger("click");
//             return false;
//         }
//         //엔터키 막기
//         if(e.keyCode && e.keyCode == 13){
//             e.preventDefault();
//         }
//     });
// });








