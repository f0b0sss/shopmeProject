package com.shopme.setting.service;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.enums.SettingCategory;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.repository.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingServiceImpl implements SettingService {

    @Autowired
    private SettingRepository settingRepository;


    @Override
    public  List<Setting> getGeneralSettings() {
        return settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
    }

    @Override
    public EmailSettingBag getEmailSettings(){
        List<Setting> settings = settingRepository.findByCategory(SettingCategory.MAIL_SERVER);

        settings.addAll(settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES));

        EmailSettingBag settingBag = new EmailSettingBag(settings);

        return new EmailSettingBag(settings);
    }


}
