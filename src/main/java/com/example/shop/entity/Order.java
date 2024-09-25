package com.example.shop.entity;

import com.example.shop.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문상태

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL // order_id가 order_item테이블에 있으므로 주인은 OrderItem엔티티
            ,orphanRemoval = true, fetch = FetchType.LAZY)
                                    //orderItem에 있는 order에 의해 관리됨, (주인의 필드인 order을 mappedBy 값으로 설정)
                                    //cascade ALL로 설정하여 부모엔티티의 영속성상태변화를 자식엔티티에 모두 전이
                                    //orphanRemoval=true 부모(주문)엔티티에서 자식(주문상품)엔티티 삭제시 orderItem 내 엔티티삭제

    private List<OrderItem> orderItems = new ArrayList<>(); // 하나의주문이 여러개의 주문상품을 갖으므로 List형으로 매핑

    //private LocalDateTime regTime;

   // private LocalDateTime updateTime;

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this); // Order엔티티 OrderItem엔티티가 양방향 참조관계이므로 orderitem객체도 order객체를세팅
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList){
        Order order = new Order();
        order.setMember(member);
        for(OrderItem orderItem : orderItemList){
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now()); // 현재시간을 주문시간으로 세팅
        return order;
    }

    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
