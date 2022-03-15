package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.src.like.model.PatchLikeReq;
import com.example.demo.src.order.OrderDao;
import com.example.demo.src.order.OrderProvider;
import com.example.demo.src.user.model.PatchProfileReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class LikeService {
    final Logger logger = LoggerFactory.getLogger(this.getClass()); // Log 처리부분: Log를 기록하기 위해 필요한 함수입니다.

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************
    private final LikeDao likeDao;
    private final LikeProvider likeProvider;
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!


    @Autowired //readme 참고
    public LikeService(LikeDao likeDao, LikeProvider likeProvider, JwtService jwtService) {
        this.likeDao = likeDao;
        this.likeProvider = likeProvider;
        this.jwtService = jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!

    }

    public void modifyLike(PatchLikeReq patchLikeReq) throws BaseException {
        try {
            int result = likeDao.modifyLike(patchLikeReq); // 해당 과정이 무사히 수행되면 True(1), 그렇지 않으면 False(0)입니다.
            if (result == 0) { // result값이 0이면 과정이 실패한 것이므로 에러 메서지를 보냅니다.
                throw new BaseException(MODIFY_FAIL_LIKE);
            }
        } catch (Exception exception) { // DB에 이상이 있는 경우 에러 메시지를 보냅니다.
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
