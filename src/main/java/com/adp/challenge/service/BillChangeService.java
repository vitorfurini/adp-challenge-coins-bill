package com.adp.challenge.service;

import com.adp.challenge.dto.CoinDTO;
import com.adp.challenge.exception.BillChangeException;
import com.adp.challenge.model.CoinsModel;
import com.adp.challenge.type.CoinsType;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.aspectj.runtime.internal.Conversions.intValue;

@Data
@Component
public class BillChangeService {

    @Value("${bill.exchange.coins.quantity}")
    private String quantityValue;

    @Value("${bill.exchange.coins.strategy}")
    private String coinsStrategy;

    private static final String COINS_EMPTY = "Coins depleted to Zero count";

    private static final String COINS_INVALID = "Invalid coin type";

    public BillChangeService() {
    }


    @Transactional
    public CoinsModel getChange(CoinsModel coins, Integer bill) throws BillChangeException {
        // convert the list to coin DTO object
        List<CoinDTO> coinDTOList = getCoinDTOS(coins);
        // consider only available coins
        coinDTOList = getAvailableCoins(coinDTOList);

        if (CollectionUtils.isEmpty(coinDTOList)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, COINS_EMPTY);
        }

        BigDecimal billInCents = new BigDecimal(100)
                .multiply(new BigDecimal(bill));

        for (CoinDTO coinDTO : coinDTOList) {
            Integer coinDenominationNeeded = intValue(divide(billInCents.doubleValue(), coinDTO.getCoinsType().value));


            if (coinDenominationNeeded <= coinDTO.getAvailableCount()) {
                billInCents = billInCents.remainder(new BigDecimal(coinDTO.getCoinsType().value));
                coinDTO.setAvailableCount(coinDTO.getAvailableCount() - coinDenominationNeeded);
            } else {
                billInCents = billInCents.subtract(
                        new BigDecimal(coinDTO.getCoinsType().value)
                                .multiply(new BigDecimal(coinDTO.getAvailableCount())));
                coinDTO.setAvailableCount(0);
            }
        }

        if (billInCents.compareTo(BigDecimal.ZERO) != 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, MessageFormat.format(COINS_EMPTY,
                    coinDTOList, billInCents));
        }
        return convertCoinsDTOToCoins(coinDTOList, coins);

    }

    private CoinsModel convertCoinsDTOToCoins(List<CoinDTO> coinDTOList, CoinsModel coins) throws BillChangeException {
        for (CoinDTO coinDTO : coinDTOList) {
            switch (coinDTO.getCoinsType()) {
                case QUARTERS -> coins.setQuarters(coinDTO.getAvailableCount());
                case DIMES -> coins.setDimes(coinDTO.getAvailableCount());
                case NICKELS -> coins.setNickels(coinDTO.getAvailableCount());
                case PENNIES -> coins.setPennies(coinDTO.getAvailableCount());
                default -> throw new BillChangeException(COINS_INVALID);
            }
        }
        return coins;
    }

    private double divide(double d1, double d2) {
        BigDecimal b1 = BigDecimal.valueOf(d1);
        BigDecimal b2 = BigDecimal.valueOf(d2);
        return b1.divide(b2, 2, RoundingMode.DOWN).doubleValue();
    }

    private List<CoinDTO> getCoinDTOS(CoinsModel coins) {
        return Arrays.asList(
                new CoinDTO(CoinsType.QUARTERS, coins.getQuarters()),
                new CoinDTO(CoinsType.DIMES, coins.getDimes()),
                new CoinDTO(CoinsType.NICKELS, coins.getNickels()),
                new CoinDTO(CoinsType.PENNIES, coins.getPennies())
        );
    }

    private List<CoinDTO> getAvailableCoins(List<CoinDTO> coinDTOList) {
        List<CoinDTO> resultantDTO = new java.util.ArrayList<>(coinDTOList.stream()
                .filter(coinDTO -> coinDTO.getAvailableCount() > 0).toList());
        if (coinsStrategy != null && coinsStrategy.equals("ASC")) {
            Collections.reverse(resultantDTO);
        }

        return resultantDTO;
    }


    public CoinsModel buildCoins() {
        Integer quantity = Integer.valueOf(quantityValue);
        CoinsModel resetCoins = new CoinsModel();
        resetCoins.setDimes(quantity);
        resetCoins.setNickels(quantity);
        resetCoins.setQuarters(quantity);
        resetCoins.setPennies(quantity);
        return resetCoins;
    }
}
