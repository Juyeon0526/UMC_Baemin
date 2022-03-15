package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;

import com.example.demo.src.order.model.GetDetailOrderRes;
import com.example.demo.src.order.model.GetOrderRes;
import com.example.demo.src.order.model.PostOrderReq;
import com.example.demo.src.order.model.PostOrderRes;
import com.example.demo.src.user.model.PostLoginReq;
import com.example.demo.src.user.model.PostLoginRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/users")
public class OrderController {
    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************

    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log를 남기기: 일단은 모르고 넘어가셔도 무방합니다.

    @Autowired
    private final OrderProvider orderProvider;
    @Autowired
    private final OrderService orderService;
    @Autowired
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!


    public OrderController(OrderProvider orderProvider, OrderService orderService, JwtService jwtService) {
        this.orderProvider = orderProvider;
        this.orderService = orderService;
        this.jwtService = jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!
    }

    // ******************************************************************************

    //Query String
    @ResponseBody
    @GetMapping("/{userIdx}/orders") // (GET) 127.0.0.1:9000/app/:userIdx/orders
    // GET 방식의 요청을 매핑하기 위한 어노테이션
    public BaseResponse<List<GetOrderRes>> getOrders(@PathVariable("userIdx") int userIdx, @RequestParam int pageNo, int pageSize) {
        try {
            List<GetOrderRes> getOrdersRes;
            getOrdersRes = orderProvider.getOrdersByUserIdx(userIdx, pageNo, pageSize);
            return new BaseResponse<>(getOrdersRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }



    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}/orders/{orderIdx}") // (GET) 127.0.0.1:9000/app/:userIdx/orders/details
    public BaseResponse<List<GetDetailOrderRes>> getDetailOrders(@PathVariable("userIdx") int userIdx, @PathVariable("orderIdx") int orderIdx) {
        try {
            List<GetDetailOrderRes> getDetailOrdersRes = orderProvider.getDetailOrdersByOrderIdx(userIdx, orderIdx);
            return new BaseResponse<>(getDetailOrdersRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    @ResponseBody
    @PostMapping("/{userIdx}/orders")
    public BaseResponse<PostOrderRes> Order(@PathVariable("userIdx") int userIdx, @RequestBody PostOrderReq postOrderReq) {
        try {
            PostOrderRes postOrderRes = orderProvider.Order(postOrderReq);
            return new BaseResponse<>(postOrderRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}