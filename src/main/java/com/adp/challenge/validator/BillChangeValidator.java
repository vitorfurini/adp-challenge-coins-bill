package com.adp.challenge.validator;

import com.adp.challenge.type.BillsType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Component
public class BillChangeValidator {

    public boolean validate(Integer bill){
        if (bill == null || bill <= 0){
            return false;
        }
        return Arrays.stream(BillsType.values())
                .map(BillsType::getValue)
                .anyMatch(x -> Objects.equals(x, bill));
    }

}
