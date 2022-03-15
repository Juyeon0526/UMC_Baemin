package com.example.demo.src.like;

import com.example.demo.src.like.model.GetLikeRes;
import com.example.demo.src.like.model.PatchLikeReq;
import com.example.demo.src.like.model.PostLikeReq;
import com.example.demo.src.order.model.GetOrderRes;
import com.example.demo.src.order.model.PostOrderReq;
import com.example.demo.src.user.model.PatchProfileReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class LikeDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetLikeRes> getLikesByUserIdx(int userIdx) {
        String getLikesByUserIdxQuery = " SELECT storeName, concat('최소주문 ', format(minDeliveryMoney, 0), '원') as minDeliveryMoney FROM `Like` l join (select storeIdx, storeName, minDeliveryMoney from Store) as s on s.storeIdx = l.storeIdx where l.status = 'active' && l.userIdx = ?";
        int getLikesByUserIdxParams = userIdx;
        return this.jdbcTemplate.query(getLikesByUserIdxQuery,
                (rs, rowNum) -> new GetLikeRes(
                        rs.getString("storeName"),
                        rs.getString("minDeliveryMoney")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getLikesByUserIdxParams); // 정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    public int createLike(PostLikeReq postLikeReq) {
        String createLikeQuery = "insert into `Like` (userIdx, storeIdx) VALUES (?,?)"; // 실행될 동적 쿼리문
        Object[] createLikeParams = new Object[]{postLikeReq.getUserIdx(), postLikeReq.getStoreIdx()}; // 동적 쿼리의 ?부분에 주입될 값
        this.jdbcTemplate.update(createLikeQuery, createLikeParams);

        String lastInserIdQuery = "select last_insert_id()"; // 가장 마지막에 삽입된(생성된) id값은 가져온다.
        int a = this.jdbcTemplate.queryForObject(lastInserIdQuery, int.class);
        return a;
        //return this.jdbcTemplate.queryForObject(lastInserIdQuery, int.class); // 해당 쿼리문의 결과 마지막으로 삽인된 유저의 userIdx번호를 반환한다.
    }

    // 회원정보 변경
    public int modifyLike(PatchLikeReq patchLikeReq) {
        String modifyLikeQuery = "update `Like` set status = ? where userIdx = ? && storeIdx = ?"; // 해당 userIdx를 만족하는 User를 해당 profileImgUrl으로 변경한다.
        Object[] modifyLikeParams = new Object[]{patchLikeReq.getStatus(), patchLikeReq.getUserIdx(), patchLikeReq.getStoreIdx()}; // 주입될 값들(profileImgUrl, userIdx) 순

        return this.jdbcTemplate.update(modifyLikeQuery, modifyLikeParams); // 대응시켜 매핑시켜 쿼리 요청(생성했으면 1, 실패했으면 0)
    }
}
