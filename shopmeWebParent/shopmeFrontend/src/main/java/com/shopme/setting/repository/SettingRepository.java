package com.shopme.setting.repository;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SettingRepository extends JpaRepository<Setting, String> {

    @Query("select s from Setting s where s.category = ?1 or s.category = ?2")
    List<Setting> findByTwoCategories(SettingCategory first, SettingCategory second);

    List<Setting> findByCategory(SettingCategory settingCategory);

    Setting findByKey(String key);

}
