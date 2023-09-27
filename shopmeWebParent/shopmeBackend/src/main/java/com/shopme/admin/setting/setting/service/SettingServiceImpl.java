package com.shopme.admin.setting.setting.service;

import com.shopme.admin.setting.GeneralSettingBag;
import com.shopme.admin.setting.setting.repository.SettingRepository;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingServiceImpl implements SettingService {

    @Autowired
    private SettingRepository settingRepository;

    @Override
    public List<Setting> allSettings() {
        return settingRepository.findAll();
    }

    @Override
    public GeneralSettingBag getGeneralSettingBag() {
        List<Setting> settings = new ArrayList<>();

        List<Setting> generalSettings = settingRepository.findByCategory(SettingCategory.GENERAL);
        List<Setting> currencySettings = settingRepository.findByCategory(SettingCategory.CURRENCY);

        settings.addAll(generalSettings);
        settings.addAll(currencySettings);

        return new GeneralSettingBag(settings);
    }

    @Override
    public void saveAll(Iterable<Setting> settings){
        settingRepository.saveAll(settings);
    }

    @Override
    public List<Setting> getMailServerSettings() {
        return settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
    }

    @Override
    public List<Setting> getMailTemplateSettings() {
        return settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES);
    }
}
