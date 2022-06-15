package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Fail.fail;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class OrderServiceTest {

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;
    @Autowired EntityManager em;

    @Test
    public void 상품주문() throws Exception
    {
        //given
        Member member = getMember();
        Item book = getItem("bookA", 10000, 10);

        int orderCount = 3;
        //when

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        Order getOrder = orderRepository.findOne(orderId);

        //then
        //assertEquals(orderId, orderRepository.findOne(orderId) );

        assertEquals("상품주문시 상태는 ORDERS", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 수가 일치해야한다", 1, getOrder.getOrderItems().size());
        assertEquals("주문가격은 가격*수량이다", 10000*orderCount, getOrder.getTotalPrice());
        assertEquals("주문수량만큼 재고가 줄어야한다", 7, book.getStockQuantity());

    }



    @Test (expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception
    {
        //given
        //Long order(Long memberId, Long itemId, int count){
       Member member = getMember();
       Item  item = getItem("bookA", 10000, 10);

        int orderCount = 20;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //then
        // 예외발생
        fail("재고수량 부족 예외가 발생해야한다.");

    }
    @Test
    public void 주문취소() throws Exception
    {
        //cancelOrder(Long orderId)
        //given
        Member member = getMember();
        em.persist(member);
        Item item = getItem("아이템1",1000,5);
        em.persist(item);

        int orderCount=3;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        Assert.assertEquals("주문 취소한 수량만큼 다시 증가되는지 확인", 5, item.getStockQuantity());
        assertEquals("주문 취소시 상태는 CANCEL", OrderStatus.CANCLE, getOrder.getStatus());

    }

    // command+ option + P : 매개변수로 설정 가능
    private Item getItem(String name, int price, int stockQuantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }


    private Member getMember() {
        Member member= new Member();
        member.setName("memberA");
        member.setAddress(new Address("서울", "답십리", "123-123"));
        em.persist(member);
        return member;
    }


}
