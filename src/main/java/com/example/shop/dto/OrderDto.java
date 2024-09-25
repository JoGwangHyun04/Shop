package com.example.shop.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {

    @NotNull(message = "상품아이디는 필수입력값입니다.")
    private Long itemId;

    @Min(value = 1, message = "최소 주문량은 1개입니다.")
    @Max(value = 999, message = "최대 주문량은 999개입니다.")
    private int count;
}
