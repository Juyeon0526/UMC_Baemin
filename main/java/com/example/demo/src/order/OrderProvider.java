package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.order.model.GetDetailOrderRes;
import com.example.demo.src.order.model.GetOrderRes;
import com.example.demo.src.order.model.PostOrderReq;
import com.example.demo.src.order.model.PostOrderRes;
import com.example.demo.src.user.model.PostLoginReq;
import com.example.demo.src.user.model.PostLoginRes;
import com.example.demo.src.user.model.User;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class OrderProvider {

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************
    private final OrderDao orderDao;
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired //readme 참고
    public OrderProvider(OrderDao orderDao, JwtService jwtService) {
        this.orderDao = orderDao;
        this.jwtService = jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!
    }
    // ******************************************************************************

    // 해당 userIdx를 갖는 리뷰 조회
    public List<GetOrderRes> getOrdersByUserIdx(int userIdx, int pageNo, int pageSize) throws BaseException {
        try {
            List<GetOrderRes> getOrdersRes = orderDao.getOrdersByUserIdx(userIdx, pageNo, pageSize);
            return getOrdersRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetDetailOrderRes> getDetailOrdersByOrderIdx(int userIdx, int orderIdx) throws BaseException {
        try {
            List<GetDetailOrderRes> getDetailOrdersRes = orderDao.getDetailOrdersByOrderIdx(userIdx, orderIdx);
            return getDetailOrdersRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostOrderRes Order(PostOrderReq postOrderReq) throws BaseException {
        int orderIdx = orderDao.createOrder(postOrderReq);
        return new PostOrderRes(orderIdx);
//  *********** 해당 부분은 7주차 - JWT 수업 후 주석해제 및 대체해주세요!  **************** //
//            String jwt = jwtService.createJwt(userIdx);
//            return new PostLoginRes(userIdx,jwt);
//  **************************************************************************
    }
}
