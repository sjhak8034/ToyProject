package com.example.toyproject.common.enums;

import lombok.Getter;

@Getter
public enum BankType {
    KAKAO_BANK("카카오뱅크"),
    KOREA_POST_BANK("우체국"),
    NH_BANK("농협"),
    SHINHAN_BANK("신한은행"),
    KEB_HANA_BANK("하나은행"),
    WOORI_BANK("우리은행"),
    IBK_BANK("기업은행"),
    BUSAN_BANK("부산은행"),
    DAEGU_BANK("대구은행"),
    GYEONGNAM_BANK("경남은행"),
    JEONBUK_BANK("전북은행"),
    JEJU_BANK("제주은행");

    private final String bankName;

    BankType(String bankName) {
        this.bankName = bankName;
    }

}
