package com.shopme.admin.order.controller;

import com.shopme.admin.order.service.OrderService;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.setting.setting.service.SettingService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.order.OrderStatus;
import com.shopme.common.entity.order.OrderTrack;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.setting.Setting;
import com.shopme.common.exception.OrderNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

@Controller
public class OrderController {
    private String defaultRedirectURL = "redirect:/orders/page/1?sortField=orderTime&sortDir=desc";

    @Autowired
    private OrderService orderService;
    @Autowired
    private SettingService settingService;

    @GetMapping("/orders")
    public String listFirstPage() {
        return defaultRedirectURL;
    }

    @GetMapping("/orders/page/{pageNum}")
    public String listByPage(
            @PagingAndSortingParam(listName = "listOrders", moduleUrl = "/orders") PagingAndSortingHelper helper,
            @PathVariable(name = "pageNum") int pageNum,
            HttpServletRequest request,
            @AuthenticationPrincipal ShopmeUserDetails loggedUser) {

        orderService.listByPage(pageNum, helper);
        loadCurrencySetting(request);

//        if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Salesperson") && loggedUser.hasRole("Shipper")) {
//            return "orders/orders_shipper";
//        }

        if (!loggedUser.hasRole("Admin") && loggedUser.hasRole("Salesperson") || loggedUser.hasRole("Shipper")) {
            return "orders/orders_shipper";
        }

        return "orders/orders";
    }

    private void loadCurrencySetting(HttpServletRequest request) {
        List<Setting> currencySettings = settingService.getCurrencySettings();

        for (Setting setting : currencySettings) {
            request.setAttribute(setting.getKey(), setting.getValue());
        }
    }

    @GetMapping("/orders/detail/{id}")
    public String viewOrderDetails(@PathVariable Long id, Model model,
                                   RedirectAttributes ra, HttpServletRequest request,
                                   @AuthenticationPrincipal ShopmeUserDetails loggedUser) {
        try {
            Order order = orderService.get(id);
            loadCurrencySetting(request);

            boolean isVisibleForAdminOrSalesperson = false;

            if (loggedUser.hasRole("Admin") || loggedUser.hasRole("Salesperson")) {
                isVisibleForAdminOrSalesperson = true;
            }

            model.addAttribute("isVisibleForAdminOrSalesperson", isVisibleForAdminOrSalesperson);
            model.addAttribute("order", order);

            return "orders/order_details_modal";
        } catch (OrderNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return defaultRedirectURL;
        }
    }

    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            orderService.delete(id);

            ra.addFlashAttribute("message", "The order ID " + id + " has been deleted.");
        } catch (OrderNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }

        return defaultRedirectURL;
    }

    @GetMapping("/orders/edit/{id}")
    public String editOrder(@PathVariable Long id, Model model, RedirectAttributes ra,
                            HttpServletRequest request) {
        try {
            Order order = orderService.get(id);

            List<Country> listCountries = orderService.listAllCountries();
            loadCurrencySetting(request);

            model.addAttribute("pageTitle", "Edit Order (ID: " + id + ")");
            model.addAttribute("order", order);
            model.addAttribute("listCountries", listCountries);

            return "orders/order_form";

        } catch (OrderNotFoundException ex) {
            System.out.println("hi1");
            ra.addFlashAttribute("message", ex.getMessage());
            return defaultRedirectURL;
        }

    }

    @PostMapping("/order/save")
    public String saveOrder(Order order, HttpServletRequest request, RedirectAttributes ra) {
        String countryName = request.getParameter("countryName");
        order.setCountry(countryName);

        updateProductDetails(order, request);
        updateOrderTracks(order, request);

        orderService.save(order);

        ra.addFlashAttribute("message", "The order ID " + order.getId() + " has been updated successfully");

        return defaultRedirectURL;
    }

    private void updateProductDetails(Order order, HttpServletRequest request) {
        String[] detailIds = request.getParameterValues("detailId");
        String[] productIds = request.getParameterValues("productId");
        String[] productPrices = request.getParameterValues("productPrice");
        String[] productDetailCosts = request.getParameterValues("productDetailCost");
        String[] quantities = request.getParameterValues("quantity");
        String[] productSubtotals = request.getParameterValues("productSubtotal");
        String[] productShipCosts = request.getParameterValues("productShipCost");

        Set<OrderDetail> orderDetails = order.getOrderDetails();

        for (int i = 0; i < detailIds.length; i++) {
            OrderDetail orderDetail = new OrderDetail();
            Long detailId = Long.parseLong(detailIds[i]);
            if (detailId > 0) {
                orderDetail.setId(detailId);
            }

            orderDetail.setOrder(order);
            orderDetail.setProduct(new Product(Long.parseLong(productIds[i])));
            orderDetail.setProductCost(Float.parseFloat(productDetailCosts[i]));
            orderDetail.setSubtotal(Float.parseFloat(productSubtotals[i]));
            orderDetail.setShippingCost(Float.parseFloat(productShipCosts[i]));
            orderDetail.setQuantity(Integer.parseInt(quantities[i]));
            orderDetail.setUnitPrice(Float.parseFloat(productPrices[i]));

            orderDetails.add(orderDetail);
        }
    }

    private void updateOrderTracks(Order order, HttpServletRequest request) {
        String[] trackIds = request.getParameterValues("trackId");
        String[] trackStatuses = request.getParameterValues("trackStatus");
        String[] trackDates = request.getParameterValues("trackDate");
        String[] trackNotes = request.getParameterValues("trackNotes");

        List<OrderTrack> orderTracks = order.getOrderTracks();
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");

        for (int i = 0; i < trackIds.length; i++) {
            OrderTrack trackRecord = new OrderTrack();

            Long trackId = Long.parseLong(trackIds[i]);
            if (trackId > 0) {
                trackRecord.setId(trackId);
            }

            trackRecord.setOrder(order);
            trackRecord.setStatus(OrderStatus.valueOf(trackStatuses[i]));
            trackRecord.setNotes(trackNotes[i]);

            try {
                trackRecord.setUpdatedTime(dateFormatter.parse(trackDates[i]));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            orderTracks.add(trackRecord);
        }
    }
}
