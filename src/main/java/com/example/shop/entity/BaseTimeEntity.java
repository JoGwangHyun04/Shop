package com.example.shop.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class}) // Auditing를 적용하기위해 해당어노테이션 추가
@MappedSuperclass
@Getter @Setter
public abstract class BaseTimeEntity {
    @CreatedDate // 엔티티가 생성되어 저장될때 시간을 자동으로 저장
    private LocalDateTime regTime;

    @LastModifiedDate // 엔티티값 변경시 시간을 자동으로저장
    private LocalDateTime updateTime;
}
