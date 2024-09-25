package com.example.shop.repository;

import com.example.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    //상품이미지 아이디의 오름차순으로 가져오는 쿼리메서드
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
}
