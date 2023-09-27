package com.shopme.setting.service;

import com.shopme.common.entity.Setting;
import com.shopme.setting.EmailSettingBag;

import java.util.List;

public interface SettingService {
    List<Setting> getGeneralSettings();

    EmailSettingBag getEmailSettings();
}
