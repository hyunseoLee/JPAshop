package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
public class OrderItem {
    @Id
    @GeneratedValue
    @Column(name= "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    private int orderPrice; //주문가격
    private int count; // 주문수량

    /* Lombok 사용 : @NoArgsConstructor(access =  AccessLevel.PROTECTED)
    protected OrderItem(){

    }
    */
    //=== 생성 메서드 ==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem=  new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        // 주문수량만큼 재고를 감소한다.
        item.removeStock(count);
        return orderItem;
    }
     //=== 비즈니스 로직 ===//
    public void cancel(){
        getItem().addStock(count); // 재고수량을 원복해준다.
    }

    //=== 조회 로직 ===//
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
