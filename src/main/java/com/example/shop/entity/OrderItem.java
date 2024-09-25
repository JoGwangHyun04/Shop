package com.example.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class OrderItem extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderprice;

    private int count;

    //private LocalDateTime regTime;

    //private LocalDateTime updateTime;

    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item); // 주문할상품체크
        orderItem.setCount(count); //주문할상품의 수량체크

        orderItem.setOrderprice(item.getPrice());

        item.removeStock(count);
        return orderItem;
    }

    public int getTotalPrice(){
        return orderprice*count;
    }
}
