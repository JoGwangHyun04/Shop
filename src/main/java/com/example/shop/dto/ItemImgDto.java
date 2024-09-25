package com.example.shop.dto;

import com.example.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDto {
    private Long id;

    private String itemName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDto of(ItemImg itemImg){
        return modelMapper.map(itemImg, ItemImgDto.class);
        //ItemImg엔티티 객체를 파라미터로받아 ItemImg객체의 자료형과 맴버변수 이름이 같으면 ItemImgDto로 값을 복사하여 반환
        //static 메소드로 선언해 ItemImgDto 객체를 생성하지않아도 호출할수있음
    }
}
