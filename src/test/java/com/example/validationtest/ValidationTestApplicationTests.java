package com.example.validationtest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ValidationTestApplicationTests {

    @Autowired
    private MessageSource messageSource;

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Autowired
    Validator validator;

    @Test
    public void testLocalizedValidationMessages() {
        // バリデーションエラーメッセージをテストする対象のBeanを作成
        PersonForm personForm = new PersonForm();
        personForm.setAge(30);
        personForm.setName("""
                寿限無寿限無五劫のすりきれ海砂利水魚水行末雲来末風来末
                食う寝るところに住むところ、
                やぶらこうじのぶらこうじ、
                パイポ・パイポ・パイポのシューリンガン、
                シューリンガンのグーリンダイ、
                グーリンダイのポンポコピーのポンポコナの、
                長久命の長助""");

        // ロケールを日本語に設定
        LocaleContextHolder.setLocale(Locale.JAPANESE);

        // バリデーションを実行しエラー情報を取得
        Set<ConstraintViolation<PersonForm>> violations = validator.validate(personForm);

        // 1つのバリデーションエラーがあることを確認
        assertEquals(1, violations.size());

        // バリデーションエラーメッセージを取得
        ConstraintViolation<PersonForm> violation = violations.iterator().next();
        String violationMessage = violation.getMessage();

        // バリデーションエラーメッセージが日本語ロケールにおける期待値と一致することを確認
        String expectedMessage = messageSource.getMessage("name.size", null, Locale.JAPANESE);
        assertEquals(expectedMessage, violationMessage);
    }
}