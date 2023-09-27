package com.shopme.admin.setting.setting.service;

import com.shopme.admin.setting.GeneralSettingBag;
import com.shopme.common.entity.Setting;

import java.util.List;

public interface SettingService {
    List<Setting> allSettings();

    GeneralSettingBag getGeneralSettingBag();

    void saveAll(Iterable<Setting> settings);

    List<Setting> getMailServerSettings();

    List<Setting> getMailTemplateSettings();
}
