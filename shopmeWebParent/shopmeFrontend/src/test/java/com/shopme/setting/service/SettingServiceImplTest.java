package com.shopme.setting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SettingServiceImplTest {

    @Autowired
    private SettingService settingService;


    @Test
    void getEmailSettings() {
//        EmailSettingBag settingBag = settingService.getEmailSettings();
//
//        System.out.println("settingBag - " + settingBag);
    }
}