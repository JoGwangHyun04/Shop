package com.example.shop.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("상품 등록 페이지 권한 테스트")
    @WithMockUser(username = "admin",roles = "ADMIN") // 회원이름이 admin, 권한이 ADMIN인 유저가 로그인된상태로 테스트할수있게해줌
    public void itemFormTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new")) // 상품등록페이지에 get요청을 보낸다
                .andDo(print()) // 요청과 응답메시지를 확인할수있게 콘솔창에 출력
                .andExpect(status().isOk()); // 응답상태 코드가 정상인지 확인
    }

    @Test
    @DisplayName("상품등록페이지 일반회원 접근테스트")
    @WithMockUser(username = "user",roles = "USER")
    public void itemFormNotAdminTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))
                .andDo(print())
                .andExpect(status().isForbidden()); // 상품등록페이지 접근시 Forbidden예외발생하면 테스트정상
    }

}