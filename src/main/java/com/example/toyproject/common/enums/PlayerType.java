package com.example.toyproject.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlayerType {
    AI ("AI"),
    USER ("USER");
    private final String name;
}
