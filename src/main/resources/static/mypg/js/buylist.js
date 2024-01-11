import * as list from "./module/list.js";
import * as page from "./module/page.js";


$(document).ready(function (){
    let userId = $('.menu-area').data('userid');
    console.log(userId);
    list.list(0,userId,'mypgs','orderList',showList);


})

function showList(result){

    let text = ``;
    let textinput = $('.buy-area');

    if(result.content.length === 0){

        text = `
               <div class="buyboxp"> 
                  <div class="nonepage">
                        <div class="none-img-area">
                            <div class="none-img">
                                <img src="../img/b556fdf429d8de25c3acf62f8186ddb9.png"/>
                            </div>
                        </div>
                        <div class="nonetext-area">
                            <p>등록된 내용이 없습니다.</p>
                        </div>
                  </div> 
               </div> 
        `;
        }else {

            result.content.forEach(r => {

                text += `
                            <li class="buy-text">
                                <div class="order-date" data-orderId="${r.id}">
                                    <p class="orderregisterdate">${r.orderDate}</p>
                                </div>
                                    <div class="buylistbox-overflow">
                          `;

                                r.orderItemDtoList.forEach(e => {

                                text += `
                                      
                                                <div class="buylistbox" data-orderItemId="${e.id}">
                                                    <div class="img-area-2">
                                                        <img src="/mypgs/goods?fileFullPath=${e.goodsMainImgPath + '/' + e.goodsMainImgUuid + '_' + e.goodsMainImgName}"    class="productimg"/>
                                                    </div>
                                                    <div class="buy-text-area-2">
                                                        <a href="#">
                                                            <p class="siteName">[산책갈개]</p>
                                                            <p class="productName">${e.goodsName}</p>
                                                        </a>
                                                    </div>
                                         `;
                                                if(e.orderReviewId == null ){
                                                    text += `<div class="price-2">
                                                                <a href="/mypg/productreview/${e.id}" class="text-page-2">
                                                                    <p>리뷰작성</p>
                                                                </a>
                                                             `;}
                                                else{
                                                    text += `<div class="price-2">
                                                                <a href="/mypg/html/productreview.html" class="text-page-2">
                                                                    <p>리뷰작성완료</p>
                                                                </a>
                                                             `;}
                                                text +=`
                                                        <span class="pricequntity">${e.orderQuantity}개</span>
                                                        <span class="pricetotal">${numberWithCommas(e.orderQuantity * e.orderPrice)}원</span>
                                                    </div>
                                                </div>
                                       
                                        `;
                                })
                            text += `  </div>`;

                         })
                    text += `</li>`;
            }

        console.log(text);
        textinput.html(text);

    let paginations = $('.pagination-ul');
    page.pagination(result, paginations)
    paginations.find('a').on('click', function (e) {
        e.preventDefault();
        const page = parseInt($(this).data('page'));
        let userId = $('.menu-area').data('userid');
        list.list(page,userId,'mypgs','orderList', showList)


    });


}




function numberWithCommas(price) {
    console.log(price);
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}