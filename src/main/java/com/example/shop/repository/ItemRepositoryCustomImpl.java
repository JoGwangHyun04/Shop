package com.example.shop.repository;

import com.example.shop.constant.ItemSellStatus;
import com.example.shop.dto.ItemSearchDto;
import com.example.shop.dto.MainItemDto;
import com.example.shop.dto.QMainItemDto;
import com.example.shop.entity.Item;
import com.example.shop.entity.QItem;
import com.example.shop.entity.QItemImg;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        //상품판매상태 조건이 전체(null)일경우 null을 리턴, 결과값이 null이면 where절에서 해당조건은 무시
        //상품판매상태조건이 null이 아닌 판매중 or 품절상태라면 해당조건의 상품만 조회
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression regDtsAfter(String searchDateType){
        //searchDateType의 값에 따라 dateTime의 값을 이전시간의 값으로 세팅후 해당시간 이후로 등록된상품만 조회
        //예를들어 searchDateType의 값이 1m인경우 dateTime의 시간을 한달전으로 세팅후 최근한달동안 등록된상품만 조회

        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if(StringUtils.equals("1d",searchDateType)){
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w",searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m",searchDateType)){
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m",searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }
        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        //searchBy의 값에따라 상품명에 검색어를 포함하고있는 상품 또는 상품생성자의 아이디에 검색어를
        //포함하고있는 상품을 조회하도록 조건값 반환
        if(StringUtils.equals("itemNm",searchBy)){
            return QItem.item.itemNm.like("%"+searchQuery+"%");
        }else if (StringUtils.equals("createdBy",searchBy)){
            return QItem.item.createdBy.like("%"+searchQuery+"%");
        }
        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
//        List<Item> content = queryFactory
//                .selectFrom(QItem.item)
//                .where(regDtsAfter(itemSearchDto.getSearchDateType()), //BooleanExpression반환하는 조건문 ','단위로
//                                                                        //넣어줄경우 and조건으로 인식
//                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
//                        searchByLike(itemSearchDto.getSearchBy(),
//                                itemSearchDto.getSearchQuery()))
//                .orderBy(QItem.item.id.desc())
//                .offset(pageable.getOffset()) // 데이터를 가지고올 시작인덱스를 지정
//                .limit(pageable.getPageSize()) // 한번에 가지고올 최대개수를 지정
//                .fetch();
//
//        return new PageImpl<>(content, pageable, content.size());
        QueryResults<Item> results = queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(),
                                itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Item> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content,pageable,total);
    }


    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%"+searchQuery+"%");
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        /*List<MainItemDto> content = queryFactory
                .select(new QMainItemDto(
                        item.id, item.itemNm, item.itemDetail, itemImg.imgUrl, item.price)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repImgYn.eq("Y")) //상품이미지의경우 대표상품이미지만 불러옴
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, content.size());*/

        QueryResults<MainItemDto> results = queryFactory
                .select(new QMainItemDto(item.id, item.itemNm, item.itemDetail, itemImg.imgUrl, item.price))
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repImgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
}
