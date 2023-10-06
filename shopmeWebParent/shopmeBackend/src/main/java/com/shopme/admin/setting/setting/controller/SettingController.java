package com.shopme.admin.setting.setting.controller;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.setting.GeneralSettingBag;
import com.shopme.admin.setting.setting.service.CurrencyService;
import com.shopme.admin.setting.setting.service.SettingService;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.setting.Setting;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/settings")
public class SettingController {
    @Autowired
    private SettingService settingService;

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("")
    public String listAllSettings(Model model) {
        List<Setting> settingList = settingService.allSettings();
        List<Currency> currencyList = currencyService.allCurrenciesOrderByName();

        for (Setting setting : settingList) {
            model.addAttribute(setting.getKey(), setting.getValue());
        }

        model.addAttribute("currencyList", currencyList);

        return "settings/settings";
    }

    @PostMapping("/save-general")
    public String saveGeneralSettings(@RequestParam MultipartFile fileImage,
                                      HttpServletRequest request,
                                      RedirectAttributes redirectAttributes) throws IOException {
        GeneralSettingBag settingBag = settingService.getGeneralSettingBag();

        saveSiteLogo(fileImage, settingBag);
        saveCurrencySymbol(request, settingBag);

        updateSettingValuesFromForm(request, settingBag.list());

        redirectAttributes.addFlashAttribute(
                "message",
                "General settings have been saved successfully."
        );

        return "redirect:/settings";
    }

    private void saveSiteLogo(MultipartFile fileImage, GeneralSettingBag generalSettingBag) throws IOException {
        if (!fileImage.isEmpty()) {
            String filename = StringUtils.cleanPath(Objects.requireNonNull(fileImage.getOriginalFilename()));
            String value = "/site-logo/" + filename;
            generalSettingBag.updateSiteLogo(value);
            String uploadDir = "site-logo/";

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, filename, fileImage);
        }
    }

    private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag) {
        int currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
        Optional<Currency> findByIdResult = currencyService.findById(currencyId);

        if (findByIdResult.isPresent()) {
            Currency currency = findByIdResult.get();
            settingBag.updateCurrencySymbol(currency.getSymbol());
        }
    }

    private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> settingList) {
        for (Setting setting : settingList) {
            String value = request.getParameter(setting.getKey());

            if (value != null) {
                setting.setValue(value);
            }
        }
        settingService.saveAll(settingList);
    }

    @PostMapping("/save-mail-server")
    public String saveMailServerSettings(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        List<Setting> mailServerSettings = settingService.getMailServerSettings();
        updateSettingValuesFromForm(request, mailServerSettings);

        redirectAttributes.addFlashAttribute(
                "message",
                "Mail Sever settings have been saved successfully."
        );

        return "redirect:/settings#mailServer";
    }

    @PostMapping("/save-mail-template")
    public String saveMailTemplateSettings(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        List<Setting> mailTemplateSettings = settingService.getMailTemplateSettings();
        updateSettingValuesFromForm(request, mailTemplateSettings);

        redirectAttributes.addFlashAttribute(
                "message",
                "Mail template settings have been saved successfully."
        );

        return "redirect:/settings#mailTemplates";
    }

    @PostMapping("/save_payment")
    public String savePaymentSettings(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        List<Setting> paymentSettings = settingService.getPaymentSettings();
        updateSettingValuesFromForm(request, paymentSettings);

        redirectAttributes.addFlashAttribute(
                "message",
                "Payment settings have been saved successfully."
        );

        return "redirect:/settings#payment";
    }

}
