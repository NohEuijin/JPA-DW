// reply.js는 모듈을 만들어 두는 파일이다.
// 자바스크립트의 함수, 클래스를 모듈화 시켜 저장하는 공간이다.
// 우리는 함수를 부품처럼 만들어두고 다른 파일에서 사용할 것이다.
// 이 모듈들을 밖에서 사용할 수 있도록 내보내는 키워드가 export이다.

// jquery ajax 프로퍼티 종류
// url : 요청보내는 경로
// type : method (요청 방식)
// data : 요청 보낼때 전송할 데이터
// dataType : 받는 데이터의 타입 -> 'json'
// contentType : 전송할 데이터의 타입 -> 'application/json; charset=utf-8'
// success : 성공했을 때 실행할 함수
// error :  실패 했을 때 실행할 함수

// JSON.stringify()
//    js 객체 -> json
// JSON.parse()
//    json -> js 객체

// 저장
export function add(reply, callback) {

    $.ajax({
        url: "/replies",
        type: "post",
        data: JSON.stringify(reply),
        contentType: 'application/json; charset=utf-8',
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

// 'getList' 함수
export function getList(freeBoardId, callback) {
    $.ajax({
        url: `/replies/freeBoard/${freeBoardId}`,
        type: 'get',
        dataType: 'json',
        success: function (result) {
            if (callback) {
                callback(result);
            }
        },
        error: function (a, b, c) {
            console.error(c);
        }
    });
}

export function getDetails(freeBoardCommentId, callback){
    $.ajax({
        url : `/replies/${freeBoardCommentId}`,
        type : 'get',
        dataType : 'json',
        success : function (result){
            if(callback){
                callback(result);
            }
        },
        error : function (a, b, c){
            console.error(c);
        }
    });
}

// 수정
export function modify(freeBoardCommentId, reply, callback){
    $.ajax({
        url : `/replies/${freeBoardCommentId}`,
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

//삭제
export function remove(freeBoardCommentId, callback) {
    $.ajax({
        url: `/replies/${freeBoardCommentId}`,
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

export function getListPage(pageInfo, callback){
    $.ajax({
        url: `/replies/freeBoard/${pageInfo.freeBoardId}/${pageInfo.page}`,
        type : 'get',
        dataType : 'json',
        success : function (result){
            if(callback){
                callback(result);
            }
        },
        error : function (a,b,c){
            console.error(c);
        }
    });
}

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

//입력된 글자 바이트 계산
export function getTextLength(text) {
    let len = 0;
    for (let i = 0; i < text.length; i++) {
        if (escape(text.charAt(i)).length == 6) {
            len++;
        }
        len++;
    }
    return len;
}

//댓글 입력창 글자 수 제한
export function limitText(contents, section){
    $(contents).keyup(function(e) {

        let cont = $(this).val();
        $(section).html(`<span class="overWrite">${getTextLength(cont) + ' / 200'}  </span>`); //실시간 글자수 카운팅
        if (getTextLength(cont) > 200) {
            $('.overWrite').css('color', 'red')
        }
    });
}

//댓글 수정 입력창 글자 수 제한
export function limitModifyText(section, binding, lengthCheck, replace) {
    $(section).on('keyup', binding, function () {
        const modifyContent = $(this).val(); //this는 바인딩기준
        console.log(getTextLength(modifyContent));
        $(lengthCheck).css('display', 'none')
        $(replace).html(`<span class="overWrite">${getTextLength(modifyContent) + ' / 200'}</span>`);
        if (getTextLength(modifyContent) > 200) {
            $('.overWrite').css('color', 'red');
        } else {
            $('.overWrite').css('color', '');
        }
    });
}
