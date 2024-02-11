export function pagination(result, paginations) {
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
        //화살표 <
        if (currentPage > 0) {
            paginations.append(`<li><a href="#" data-page="${currentPage - 1}">&lt;</a></li>`);
        }else{
            paginations.append(`<li></li>`)
        }

        //페이징 버튼
        for (let i = startPage; i <= endPage; i++) {
            if(i==currentPage){
                paginations.append(`<li><a href="#" class="active-btn" data-page="${i}">${i + 1}</a></li>`);

            }else{
                paginations.append(`<li><a href="#" data-page="${i}">${i + 1}</a></li>`);

            }
        }

        //화살표 >
        if (currentPage < totalPages - 1) {
            paginations.append(`<li><a href="#" data-page="${currentPage + 1}">&gt;</a></li>`);
        }else{
            paginations.append(`<li></li>`)
        }
    }
}

export function enterKey(a,b){
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