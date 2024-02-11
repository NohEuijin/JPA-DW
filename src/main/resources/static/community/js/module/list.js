// 리스트 불러오기
export function list(page,searchForm,name,section,callback) {

    $.ajax({

        url: `/${name}/${section}/${page}`,
        type: 'get',
        data: {
            keyword : searchForm.keyword
        },
        dataType: 'json',
        success: function (result) {
            console.log(result.content)
            console.log(result.pageable)


            if (callback) {
                callback(result)
            }
        }, error: function (a, b, c) {
            console.error(c)
        }
    })
}

// 리스트 불러오기
export function listSearchDto(page,searchForm,name,section,callback) {

    $.ajax({

        url: `/${name}/${section}/${page}`,
        type: 'get',
        data: searchForm,
        dataType: 'json',
        success: function (result) {
            console.log(result.content)
            console.log(result.pageable)


            if (callback) {
                callback(result)
            }
        }, error: function (a, b, c) {
            console.error(c)
        }
    })
}






