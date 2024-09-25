package com.example.shop.dto;

import com.example.shop.constant.ItemSellStatus;
import com.example.shop.entity.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;



@Getter
@Setter
public class ItemFormDto {
    private Long id;

    @NotBlank(message = "상품명은 필수입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수입니다")
    private Integer price;

    @NotBlank(message = "이름은 필수입니다")
    private String itemDetail;

    @NotNull(message = "재고는 필수입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>(); // 상품저장후 수정할때 상품 이미지정보를 저장하는 리스트

    private List<Long> itemImgIds = new ArrayList<>(); // 상품의 이미지아이디를 저장하는 리스트
                                            //상품등록시에는 아직 상품의이미지를 저장하지않아서 아무값없음,
                                            //수정시에 이미지 아이디를 담아둘 용도로 사용함

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class); // 모델매퍼를 이용하여 엔티티와 dto객체를 복사, 복사한객체를반환
    }

    public static ItemFormDto of(Item item){
        return modelMapper.map(item, ItemFormDto.class); // 모델매퍼를 이용하여 엔티티와 dto객체를 복사, 복사한객체를반환
    }

}

