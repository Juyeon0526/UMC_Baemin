package com.example.demo.src.order;

import com.example.demo.src.Review.model.GetReviewRes;
import com.example.demo.src.order.model.GetDetailOrderRes;
import com.example.demo.src.order.model.GetOrderRes;
import com.example.demo.src.order.model.PostOrderReq;
import com.example.demo.src.user.model.PostUserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class OrderDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    public List<GetOrderRes> getOrdersByUserIdx(int userIdx, int pageNo, int pageSize) {
        String getOrdersByUserIdxQuery = "select o.orderIdx, storeName,case when timestampdiff(day, createAt, CURRENT_TIMESTAMP()) < 1 then '오늘' when timestampdiff(day, createAt, current_timestamp()) < 2 then '어제' when timestampdiff(day, createAt, current_timestamp()) < 3 then '그저께' when timestampdiff(day, createAt, current_timestamp()) < 7 then '이번주' when timestampdiff(month, createAt, current_timestamp()) < 1 then '한달전'else date_format(createAt, '%Y년-%n월-%d일') end as time from `Order` o join (select storeIdx, storeName from Store) as s on s.storeIdx = o.storeIdx join (select orderIdx from `Order` where userIdx = ? order by orderIdx desc limit ? offset ?) as temp on temp.orderIdx = o.orderIdx";
        //int getOrdersByUserIdxParams = userIdx, pageNo, pageSize;
        return this.jdbcTemplate.query(getOrdersByUserIdxQuery,
                (rs, rowNum) -> new GetOrderRes(
                        rs.getString("storeName"),
                        rs.getString("time")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                userIdx,
                pageSize,
                pageNo * pageSize); // 정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    public List<GetDetailOrderRes> getDetailOrdersByOrderIdx(int userIdx, int orderIdx) {
        String getDetailOrdersByOrderIdxQuery = "select storeName, createAt, menuName, concat(format(menuPrice, 0), '원') as menuPrice, amount, concat(format(sum(menuPrice * amount), 0), '원') as total from `Order` o join (select storeIdx, storeName from Store) as s on s.storeIdx = o.storeIdx join (select menuIdx, amount, orderIdx from OrderMenu) as w  on w.orderIdx = o.orderIdx join (select menuName, menuPrice, menuIdx from Menu) as m on m.menuIdx = w.menuIdx where o.orderIdx = ?";
        int getDetailOrdersByOrderIdxParams = userIdx & orderIdx;
        return this.jdbcTemplate.query(getDetailOrdersByOrderIdxQuery,
                (rs, rowNum) -> new GetDetailOrderRes(
                        rs.getString("storeName"),
                        rs.getTimestamp("createAt"),
                        rs.getString("menuName"),
                        rs.getString("menuPrice"),
                        rs.getInt("amount"),
                        rs.getString("total")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getDetailOrdersByOrderIdxParams); // 정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }



    /**
     * public List<BookPaginationDto> paginationCoveringIndexSql(String name, int pageNo, int pageSize) {
     * String query =
     *         "SELECT i.id, book_no, book_type, name " +
     *         "FROM book as i " +
     *         "JOIN (SELECT id FROM book WHERE name LIKE '?%' ORDER BY id DESC LIMIT ? OFFSET ?) as temp on temp.id = i.id" +
     *         "join (select orderIdx from `Order` where storeName like '?%' order by orderIdx desc limit ? offset ?) as temp on temp.orderIdx = o.orderIdx" +
     *         "       FROM book " +
     *         "       WHERE name LIKE '?%' " +
     *         "       ORDER BY id DESC " +
     *         "       LIMIT ? " +
     *         "       OFFSET ?) as temp on temp.id = i.id";
     *
     * return jdbcTemplate
     *         .query(query, new BeanPropertyRowMapper<>(BookPaginationDto.class),
     *                 name,
     *                 pageSize,
     *                 pageNo * pageSize);
     * }
     * @param postOrderReq
     * @return
     */

    @Transactional
    public int createOrder(PostOrderReq postOrderReq) {
        String createOrderQuery = "insert into `Order` (storeIdx, storeRQ, riderRQ, userIdx) VALUES (?,?,?,?)"; // 실행될 동적 쿼리문
        Object[] createOrderParams = new Object[]{postOrderReq.getStoreIdx(), postOrderReq.getStoreRQ(), postOrderReq.getRiderRQ(), postOrderReq.getUserIdx()}; // 동적 쿼리의 ?부분에 주입될 값
        this.jdbcTemplate.update(createOrderQuery, createOrderParams);

        String lastInserIdQuery = "select last_insert_id()"; // 가장 마지막에 삽입된(생성된) id값은 가져온다.
        return this.jdbcTemplate.queryForObject(lastInserIdQuery, int.class); // 해당 쿼리문의 결과 마지막으로 삽인된 유저의 userIdx번호를 반환한다.
    }
}
