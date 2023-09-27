package com.shopme.admin.setting.country.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.admin.setting.country.service.CountryService;
import com.shopme.common.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CountryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    CountryService countryService;

    @Test
    @WithMockUser(username = "admin@mail.com", password = "admin", roles = "ADMIN")
    public void listCountries() throws Exception {
        String url = "/countries";

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Country[] countries = objectMapper.readValue(jsonResponse, Country[].class);

        assertThat(countries).hasSizeGreaterThan(2);
    }

    @Test
    @WithMockUser(username = "admin@mail.com", password = "admin", roles = "ADMIN")
    public void createCountry() throws Exception {
        String url = "/countries/save";

        String countryName = "Germany";
        String countryCode = "CA";

        Country country = new Country(countryName, countryCode);

        MvcResult result = mockMvc.perform(post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(country))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer countryId = Integer.parseInt(response);

        Optional<Country> findById = countryService.findById(countryId);

        assertThat(findById.isPresent());

        Country savedCountry = findById.get();

        assertThat(savedCountry.getName()).isEqualTo(countryName);
    }

    @Test
    @WithMockUser(username = "admin@mail.com", password = "admin", roles = "ADMIN")
    public void updateCountry() throws Exception {
        String url = "/countries/save";

        Integer countryId = 4;
        String countryName = "China";
        String countryCode = "CN";

        Country country = new Country(countryId, countryName, countryCode);

        mockMvc.perform(post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(country))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(countryId)));

        Optional<Country> findById = countryService.findById(countryId);

        assertThat(findById.isPresent());

        Country savedCountry = findById.get();

        assertThat(savedCountry.getName()).isEqualTo(countryName);
    }

    @Test
    @WithMockUser(username = "admin@mail.com", password = "admin", roles = "ADMIN")
    public void deleteCountry() throws Exception {
        Integer countryId = 4;
        String url = "/countries/delete/" + countryId;

        mockMvc.perform(get(url))
                .andExpect(status().isOk());

        Optional<Country> findById = countryService.findById(countryId);

        assertThat(findById.isEmpty());
    }


}