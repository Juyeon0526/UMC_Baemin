package com.example.demo.src.Review;

import com.example.demo.config.BaseException;
import com.example.demo.src.Review.model.GetReviewRes;
import com.example.demo.src.store.StoreDao;
import com.example.demo.src.store.model.GetStoreRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

//Provider : Read의 비즈니스 로직 처리
@Service    // [Business Layer에서 Service를 명시하기 위해서 사용] 비즈니스 로직이나 respository layer 호출하는 함수에 사용된다.
            // [Business Layer]는 컨트롤러와 데이터 베이스를 연결
/**
 * Provider란?
 * Controller에 의해 호출되어 실제 비즈니스 로직과 트랜잭션을 처리: Read의 비즈니스 로직 처리
 * 요청한 작업을 처리하는 관정을 하나의 작업으로 묶음
 * dao를 호출하여 DB CRUD를 처리 후 Controller로 반환
 */
public class ReviewProvider {


    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************
    private final ReviewDao reviewDao;
    private final JwtService jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired //readme 참고
    public ReviewProvider(ReviewDao reviewDao, JwtService jwtService) {
        this.reviewDao = reviewDao;
        this.jwtService = jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!
    }
    // ******************************************************************************

    // Store들 리뷰 조회
    public List<GetReviewRes> getReviews() throws BaseException {
        try {
            List<GetReviewRes> getReviewRes = reviewDao.getReviews();
            return getReviewRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 해당 name을 갖는 Store들의 리뷰 조회
    public List<GetReviewRes> getReviewsByStoreName(String storeName) throws BaseException {
        try {
            List<GetReviewRes> getReviewsRes = reviewDao.getReviewsByStoreName(storeName);
            return getReviewsRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 해당 userIdx를 갖는 리뷰 조회
    public List<GetReviewRes> getReviewsByUserIdx(int userIdx) throws BaseException {
        try {
            List<GetReviewRes> getReviewsRes = reviewDao.getReviewsByUserIdx(userIdx);
            return getReviewsRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
