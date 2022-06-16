package jpabook.jpashop.repository;

import jpabook.jpashop.domain.OrderStatus;
import lombok.*;

@Getter @Setter
public class OrderSearch {
    private String memberName; // 화원이름
    private OrderStatus orderStatus; //주문상태 : ORDER, CANCEL
}
