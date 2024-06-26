package com.shopme.setting.service;

import com.shopme.common.entity.setting.Setting;
import com.shopme.setting.CurrencySettingBag;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.PaymentSettingBag;

import java.util.List;

public interface SettingService {
    List<Setting> getGeneralSettings();

    EmailSettingBag getEmailSettings();

    CurrencySettingBag getCurrencySettings();

    PaymentSettingBag getPaymentSettings();

    String getCurrencyCode();
}
