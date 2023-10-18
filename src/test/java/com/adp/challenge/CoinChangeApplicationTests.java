package com.adp.challenge;

import com.adp.challenge.service.BillChangeService;
import com.adp.challenge.controllers.BillChangeController;
import com.adp.challenge.model.BillChangeRequest;
import com.adp.challenge.model.CoinsModel;
import com.adp.challenge.validator.BillChangeValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BillChangeController.class)
class CoinChangeApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BillChangeService billChangeService;

    @MockBean
    private BillChangeValidator billChangeValidator;

    @Test
    void changeForBill_createSession() throws Exception {
        CoinsModel coinsModel = mockCoinsModel();

        given(billChangeService.buildCoins())
                .willReturn(coinsModel);
        given(billChangeValidator.validate(100))
                .willReturn(true);

        BillChangeRequest billChangeRequest = new BillChangeRequest();
        billChangeRequest.setBill(100);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/bill")
                        .content(asJsonString(billChangeRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coins.quarters").value(3));

    }

    @Test
    void changeForBill_readFromSession() throws Exception {
        CoinsModel coinsModel = mockCoinsModel();

        given(billChangeService.buildCoins())
                .willReturn(coinsModel);
        given(billChangeValidator.validate(100))
                .willReturn(true);

        BillChangeRequest billChangeRequest = new BillChangeRequest();
        billChangeRequest.setBill(100);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/bill")
                        .content(asJsonString(billChangeRequest))
                        .requestAttr("COINS_SESSION", coinsModel)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coins.quarters").value(3));

    }


    private CoinsModel mockCoinsModel() {
        CoinsModel coinsModel = new CoinsModel();
        coinsModel.setDimes(5);
        coinsModel.setNickels(5);
        coinsModel.setQuarters(3);
        coinsModel.setPennies(1);
        return coinsModel;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
