package com.example.shop.service;

import com.example.shop.constant.ItemSellStatus;
import com.example.shop.dto.ItemFormDto;
import com.example.shop.entity.Item;
import com.example.shop.entity.ItemImg;
import com.example.shop.repository.ItemImgRepository;
import com.example.shop.repository.ItemRepository;
import groovy.util.logging.Slf4j;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@Slf4j
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    List<MultipartFile> createMultipartFiles() {

        List<MultipartFile> multipartFileList = new ArrayList<>();

        for(int i=0; i<5;i++){
            String path = "C:/shop/item/";
            String imageName = "image"+i+".jpg";
            MockMultipartFile multipartFile = new MockMultipartFile(path, imageName, "image/jpg",
                    new byte[]{1,2,3,4});

            multipartFileList.add(multipartFile);
        }
        return multipartFileList;
    }

    @Test
    @DisplayName("상품등록테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveItem() {
        ItemFormDto itemFormDto = new ItemFormDto();
        itemFormDto.setItemNm("테스트상품");
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);
        itemFormDto.setItemDetail("테스트상품입니다");
        itemFormDto.setPrice(1000);
        itemFormDto.setStockNumber(100);

        List<MultipartFile> multipartFileList = createMultipartFiles();


        Long itemId = itemService.saveItem(itemFormDto, multipartFileList); // 상품데이터와 이미지정보를 파라미터로넘겨서
                                                                            //저장된 상품의 아이디값을 반환
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        assertEquals(itemFormDto.getItemNm(), item.getItemNm());
        assertEquals(itemFormDto.getItemSellStatus(), item.getItemSellStatus());
        assertEquals(itemFormDto.getItemDetail(), item.getItemDetail());
        assertEquals(itemFormDto.getPrice(), item.getPrice());
        assertEquals(itemFormDto.getStockNumber(), item.getStockNumber());
        assertEquals(multipartFileList.get(0).getOriginalFilename(), itemImgList.get(0).getOriImgName());
        //assertEquals(multipartFileList.get(1).getOriginalFilename(), itemImgList.get(1).getOriImgName());
        //assertEquals(multipartFileList.get(2).getOriginalFilename(), itemImgList.get(2).getOriImgName());
        //assertEquals(multipartFileList.get(3).getOriginalFilename(), itemImgList.get(3).getOriImgName());
    }
}