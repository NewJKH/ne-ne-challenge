package com.github.nenidan.ne_ne_challenge.domain.shop.order.application.dto;

import com.github.nenidan.ne_ne_challenge.domain.shop.vo.UserId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FindCursorOrderCommand {
    private UserId userId;
    private Long cursor;
    private int size;
    private String keyword;
}
