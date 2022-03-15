package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.src.like.model.GetLikeRes;
import com.example.demo.src.like.model.PostLikeReq;
import com.example.demo.src.like.model.PostLikeRes;
import com.example.demo.src.order.OrderDao;
import com.example.demo.src.order.model.GetOrderRes;
import com.example.demo.src.order.model.PostOrderReq;
import com.example.demo.src.order.model.PostOrderRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import org.springframework.stereotype.Service;

@Service
public class LikeProvider {

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************
    private final LikeDao likeDao;
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired //readme 참고
    public LikeProvider(LikeDao likeDao, JwtService jwtService) {
        this.likeDao = likeDao;
        this.jwtService = jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!
    }
    // ******************************************************************************

    // 해당 userIdx를 갖는 리뷰 조회
    public List<GetLikeRes> getLikesByUserIdx(int userIdx) throws BaseException {
        try {
            List<GetLikeRes> getLikesRes = likeDao.getLikesByUserIdx(userIdx);
            return getLikesRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLikeRes Like(PostLikeReq postLikeReq) throws BaseException {
        int userIdx = likeDao.createLike(postLikeReq);
        return new PostLikeRes(userIdx);
    }
}
