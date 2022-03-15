package com.example.demo.src.Review;


import com.example.demo.src.Review.model.GetReviewRes;
import com.example.demo.src.Review.model.PatchReviewReq;
import com.example.demo.src.like.model.PatchLikeReq;
import com.example.demo.src.store.model.GetStoreRes;
import com.example.demo.src.user.model.GetUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.List;

@Repository //  [Persistence Layer에서 DAO를 명시하기 위해 사용]

/**
 * DAO란?
 * 데이터베이스 관련 작업을 전담하는 클래스
 * 데이터베이스에 연결하여, 입력 , 수정, 삭제, 조회 등의 작업을 수행
 */
public class ReviewDao {

    // *********************** 동작에 있어 필요한 요소들을 불러옵니다. *************************

    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Store 테이블에 존재하는 전체 가게들의 리뷰 조회
    public List<GetReviewRes> getReviews() {
        String getReviewsQuery = "select storeName,reviewComments,reviewImg, starNum, nickName, case when timestampdiff(day, createAt, CURRENT_TIMESTAMP()) < 1 then '오늘' when timestampdiff(day, createAt, current_timestamp()) < 2 then '어제' when timestampdiff(day, createAt, current_timestamp()) < 3 then '그저께' when timestampdiff(day, createAt, current_timestamp()) < 7 then '이번주' when timestampdiff(month, createAt, current_timestamp()) < 1 then '한달전'else date_format(createAt, '%Y년-%n월-%d일') end as time from Review r join (select userIdx, nickName from User) as v on v.userIdx = r.userIdx join (select storeIdx, storeName from Store) as s on s.storeIdx = r.storeIdx where r.isDeleted = 'n'";
        return this.jdbcTemplate.query(getReviewsQuery,
                (rs, rowNum) -> new GetReviewRes(
                        rs.getString("storeName"),
                        rs.getString("reviewComments"),
                        rs.getString("reviewImg"),
                        rs.getInt("starNum"),
                        rs.getString("nickName"),
                        rs.getString("time")
                        ) // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
        ); // 복수개의 회원정보들을 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보)의 결과 반환(동적쿼리가 아니므로 Parmas부분이 없음)
    }

    // 해당 storeName을 갖는 가게의 리뷰 조회
    public List<GetReviewRes> getReviewsByStoreName(String storeName) {
        String getReviewsByStoreNameQuery = "select storeName,reviewComments,reviewImg, starNum, nickName,case when timestampdiff(day, createAt, CURRENT_TIMESTAMP()) < 1 then '오늘' when timestampdiff(day, createAt, current_timestamp()) < 2 then '어제' when timestampdiff(day, createAt, current_timestamp()) < 3 then '그저께' when timestampdiff(day, createAt, current_timestamp()) < 7 then '이번주' when timestampdiff(month, createAt, current_timestamp()) < 1 then '한달전'else date_format(createAt, '%Y년-%n월-%d일') end as time from Review r join (select userIdx, nickName from User) as v on v.userIdx = r.userIdx join (select storeIdx, storeName from Store) as s on s.storeIdx = r.storeIdx where r.isDeleted = 'n' && storeName =?";
        String getReviewsByStoreNameParams = storeName;
        return this.jdbcTemplate.query(getReviewsByStoreNameQuery,
                (rs, rowNum) -> new GetReviewRes(
                        rs.getString("storeName"),
                        rs.getString("reviewComments"),
                        rs.getString("reviewImg"),
                        rs.getInt("starNum"),
                        rs.getString("nickName"),
                        rs.getString("time")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getReviewsByStoreNameParams); // 정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    // 해당 userIdx을 갖는 유저의 리뷰 조회
    public List<GetReviewRes> getReviewsByUserIdx(int userIdx) {
        String getReviewsByUserIdxQuery = "select storeName,reviewComments,reviewImg, starNum, nickName,case when timestampdiff(day, createAt, CURRENT_TIMESTAMP()) < 1 then '오늘' when timestampdiff(day, createAt, current_timestamp()) < 2 then '어제' when timestampdiff(day, createAt, current_timestamp()) < 3 then '그저께' when timestampdiff(day, createAt, current_timestamp()) < 7 then '이번주' when timestampdiff(month, createAt, current_timestamp()) < 1 then '한달전'else date_format(createAt, '%Y년-%n월-%d일') end as time from Review r join (select userIdx, nickName from User) as v on v.userIdx = r.userIdx join (select storeIdx, storeName from Store) as s on s.storeIdx = r.storeIdx where r.isDeleted = 'n' && r.userIdx = ?";
        int getReviewsByUserIdxParams = userIdx;
        return this.jdbcTemplate.query(getReviewsByUserIdxQuery,
                (rs, rowNum) -> new GetReviewRes(
                        rs.getString("storeName"),
                        rs.getString("reviewComments"),
                        rs.getString("reviewImg"),
                        rs.getInt("starNum"),
                        rs.getString("nickName"),
                        rs.getString("time")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getReviewsByUserIdxParams); // 정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    // 리뷰 삭제
    @Transactional
    public int modifyReview(PatchReviewReq patchReviewReq) {
        String modifyReviewQuery = "update `Review` set isDeleted = ? where reviewIdx = ?"; // 해당 userIdx를 만족하는 User를 해당 profileImgUrl으로 변경한다.
        Object[] modifyReviewParams = new Object[]{patchReviewReq.getIsDeleted(), patchReviewReq.getReviewIdx()}; // 주입될 값들(profileImgUrl, userIdx) 순

        return this.jdbcTemplate.update(modifyReviewQuery, modifyReviewParams); // 대응시켜 매핑시켜 쿼리 요청(생성했으면 1, 실패했으면 0)
    }

}
