package com.adp.challenge.dto;

import com.adp.challenge.type.CoinsType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CoinDTO {
    private CoinsType coinsType;
    private Integer availableCount;
}
