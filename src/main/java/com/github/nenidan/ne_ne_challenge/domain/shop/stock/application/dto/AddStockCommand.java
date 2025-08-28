package com.github.nenidan.ne_ne_challenge.domain.shop.stock.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class AddStockCommand {

    private final int quantity;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public AddStockCommand(
            @JsonProperty("quantity") int quantity
    ){
        this.quantity = quantity;
    }

    public static AddStockCommand from(int quantity) {
        return new AddStockCommand(quantity);
    }
}
