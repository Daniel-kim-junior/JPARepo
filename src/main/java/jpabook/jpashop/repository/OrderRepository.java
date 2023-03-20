package jpabook.jpashop.repository;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;


    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }


//    public List<Order> findAll(OrderSearch orderSearch) {
//        QOrder order = QOrder.order;
//        QMember member = QMember.member;
//
//        return query
//                .select(order)
//                .from(order)
//                .join(order.member, member)
//                .where(statusEq(orderSearch.getOrderStatus()),
//                       nameLike(orderSearch.getMemberName()))
//                .limit(1000)
//                .fetch();
//    }
//    private BooleanExpression statusEq(OrderStatus statusCond) {
//        if(statusCond == null) {
//            return null;
//        }
//        return order.status.eq(statusCond);
//    }
//
//    private BooleanExpression nameLike(String nameCond) {
//        if(!StringUtils.hasText(nameCond)) {
//            return null;
//        }
//        return member.name.like(nameCond);
//    }
}
