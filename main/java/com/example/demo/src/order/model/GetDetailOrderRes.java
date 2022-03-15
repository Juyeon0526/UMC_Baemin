package com.example.demo.src.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
@AllArgsConstructor
public class GetDetailOrderRes {
    private String storeName;
    private Timestamp createAt;
    private String menuName;
    private String menuPrice;
    private int amount;
    private String total;
}
