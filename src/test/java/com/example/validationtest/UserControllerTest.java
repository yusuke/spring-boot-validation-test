package com.example.validationtest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("ALL")
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 名前が長すぎ() throws Exception {
        this.mockMvc.perform(post("/")
                        .locale(Locale.JAPANESE)
                        .param("name", """
                                寿限無寿限無五劫のすりきれ海砂利水魚水行末雲来末風来末
                                食う寝るところに住むところ、
                                やぶらこうじのぶらこうじ、
                                パイポ・パイポ・パイポのシューリンガン、
                                シューリンガンのグーリンダイ、
                                グーリンダイのポンポコピーのポンポコナの、
                                長久命の長助""")
                        .param("age", "30"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("personForm", 1))
                .andExpect(model().attributeHasFieldErrors("personForm", "name"))
                .andExpect(content().string(containsString("名前は2文字から30文字以内にしてください")));
    }

    @Test
    void 名前が短すぎ() throws Exception {
        this.mockMvc.perform(post("/")
                        .locale(Locale.JAPANESE)
                        .param("name", "a")
                        .param("age", "30"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("personForm", 1))
                .andExpect(model().attributeHasFieldErrors("personForm", "name"))
                .andExpect(content().string(containsString("名前は2文字から30文字以内にしてください")));
    }

    @Test
    void 名前空() throws Exception {
        this.mockMvc.perform(post("/")
                        .locale(Locale.JAPANESE)
                        .param("age", "30"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("personForm", 1))
                .andExpect(model().attributeHasFieldErrors("personForm", "name"))
                .andExpect(content().string(containsString("名前は必須です。")));
    }


    @Test
    void 年齢低すぎ() throws Exception {
        this.mockMvc.perform(post("/")
                        .locale(Locale.JAPANESE)
                        .param("name", "山本ユースケ")
                        .param("age", "18"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("personForm", 1))
                .andExpect(model().attributeHasFieldErrors("personForm", "age"))
                .andExpect(content().string(containsString("年齢は20以上である必要があります。")));
    }

    @Test
    void 年齢がない() throws Exception {
        this.mockMvc.perform(post("/")
                        .locale(Locale.JAPANESE)
                        .param("name", "山本ユースケ"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("personForm", 1))
                .andExpect(model().attributeHasFieldErrors("personForm", "age"))
                .andExpect(content().string(containsString("年齢は必須です。")));
    }

}