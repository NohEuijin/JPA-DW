package com.example.dw.api;

import com.example.dw.domain.form.OrderForm;
import com.example.dw.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders/*")
public class OrderApiController {

    private final OrderService orderService;

    /**
     * 결제시 데이터 받아오기
     * @param orderForm 주문서 저장을 위한 폼
     * @param session 세션
     * @throws IOException 입출력 예외 처리
     */
    @PostMapping("/orderList")
    public void orderList(@RequestBody OrderForm orderForm, HttpSession session) throws IOException {
        Long userId = (Long)session.getAttribute("userId");
        orderForm.setUserId(userId);
        try {
            orderService.register(orderForm, session);
            System.out.println("orderForm"+orderForm);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 결제시 단건 데이터 받아오기
     * @param orderForm 주문서 저장을 위한 폼
     * @param session 세션
     * @throws IOException 입출력 예외 처리
     */
    @PostMapping("/orderSinglePay")
    public void orderSinglePay(@RequestBody OrderForm orderForm, HttpSession session) throws IOException {
        Long userId = (Long)session.getAttribute("userId");
        orderForm.setUserId(userId);
        try {
            orderService.registerSingle(orderForm, session);
            System.out.println("orderForm"+orderForm);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
