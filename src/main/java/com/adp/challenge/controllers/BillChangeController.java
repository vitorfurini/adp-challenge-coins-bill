package com.adp.challenge.controllers;


import com.adp.challenge.service.BillChangeService;
import com.adp.challenge.exception.BillChangeException;
import com.adp.challenge.model.BillChangeRequest;
import com.adp.challenge.model.BillChangeResponse;
import com.adp.challenge.model.CoinsModel;
import com.adp.challenge.validator.BillChangeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import javax.servlet.http.HttpServletRequest;

@RestController
public class BillChangeController {

    private BillChangeService billChangeService;
    private BillChangeValidator billChangeValidator;

    @Autowired
    public BillChangeController(BillChangeService billChangeService, BillChangeValidator billChangeValidator) {
        this.billChangeService = billChangeService;
        this.billChangeValidator = billChangeValidator;
    }

    @PostMapping(path = "/bill")
    public ResponseEntity<BillChangeResponse> calculateChange(@RequestBody BillChangeRequest billChangeRequest,
            HttpServletRequest request) throws BillChangeException {
        BillChangeResponse billChangeResponse = new BillChangeResponse();
        if (!billChangeValidator.validate(billChangeRequest.getBill())) {
            throw new BillChangeException(MessageFormat.format("Invalid bill: {0}", billChangeRequest.getBill()));
        }

        CoinsModel coins = (CoinsModel) request.getSession()
                .getAttribute("COINS_SESSION");

        if (coins == null) {
            coins = billChangeService.buildCoins();
            request.getSession().setAttribute("COINS_SESSION", coins);
        } else {
            coins = billChangeService.getChange(coins, billChangeRequest.getBill());
        }

        billChangeResponse.setCoins(coins);
        return ResponseEntity.status(HttpStatus.OK)
                .body(billChangeResponse);
    }

    @PutMapping("/reset")
    public ResponseEntity<?> reset(HttpServletRequest request) {
        //invalidate the session , this will clear the data from configured database (Mongo)
        request.getSession().invalidate();
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
