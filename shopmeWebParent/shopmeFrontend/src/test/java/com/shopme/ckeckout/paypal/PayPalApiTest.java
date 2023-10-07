package com.shopme.ckeckout.paypal;

import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

class PayPalApiTest {
    private static final String BASE_URL = "https://api.sandbox.paypal.com";
    private static final String GET_ORDER_API = "/v2/checkout/orders/";
//    private static final String CLIENT_ID = "PAYPAL_CLIENT_ID";
    private static final String CLIENT_ID = "AWTXUaUfYtbrH383LKyjOBsRVRqq0Vk05q0AvD5qAq0di2Wt63f-GTiCVYnS4rZpJbrNGTcoHNN4z-jp";
//    private static final String CLIENT_SECRET = "PAYPAL_CLIENT_SECRET";
    private static final String CLIENT_SECRET = "ECommgOVz2pxtanU7jMT8Ye83roJibNjZrmyF0tcxH2elPy49_ugc8r5jyobqAXZ20wh5vfINM0Wt7r_";


    @Test
    public void testGetOrderDetails() {
        String orderId = "4A027975W0474063L";
        String requestURL = BASE_URL + GET_ORDER_API + orderId;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Accept-Language", "en_US");
        headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<PayPalOrderResponse> response = restTemplate.exchange(
                requestURL, HttpMethod.GET, request, PayPalOrderResponse.class);
        PayPalOrderResponse orderResponse = response.getBody();

        System.out.println("Order ID: " + orderResponse.getId());
        System.out.println("Validated: " + orderResponse.validate(orderId));

    }



}