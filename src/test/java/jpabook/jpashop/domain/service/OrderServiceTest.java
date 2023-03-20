package jpabook.jpashop.domain.service;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.service.OrderService;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.exception.NotEnoughStockException;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired private OrderService orderService;
    @Autowired private EntityManager em;
    @Autowired private OrderRepository orderRepository;

    @Test
    public void 상품주문() {
        // given
        Member member = createMember();
        Item book = createItem("시골 jpa", 10000, 10);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야한다.", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 10000 * orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수만큼 재고가 줄어야한다.", 8, book.getStockQuantity());
    }



    @Test
    public void 주문취소() throws Exception {
        // given
        Member member = createMember();
        Item book = createItem("시골 jpa", 10000, 10);

        // when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        orderService.cancelOrder(orderId);

        // then
        Order order = orderRepository.findOne(orderId);

        assertEquals("주문 취소 시 캔슬 상태로 변경", OrderStatus.CANCEL ,order.getStatus());
        assertEquals("주문이 취소 된 상품은 그만큼 재고가 증가하여야 한다.", 10, book.getStockQuantity());

    }

    @Test
    public void 상품주문_재고수량초과() {
        // given
        Member member = createMember();
        Item book = createItem("시골 jpa", 10000, 10);

        int orderCount = 11;

        // then
        assertThrows(NotEnoughStockException.class,
                     () -> orderService.order(member.getId(), book.getId(), orderCount));
    }

    private Item createItem(String name, int price, int stockQuantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "경기" , "123-123"));
        em.persist(member);
        return member;
    }
}