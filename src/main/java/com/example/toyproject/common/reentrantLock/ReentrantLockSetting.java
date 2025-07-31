package com.example.toyproject.common.reentrantLock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;


/**
 * Redisson 을 활용한 분산 락을 적용하는 어노테이션. 특정 메서드에 락을 설정하여 동시성 문제를 방지하는 역할.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReentrantLockSetting {

    /**
     * 락의 고유 키 (ex: "orderId")
     */
    String key();

    /**
     * 락의 이름 (선택 사항, 기본값은 빈 문자열)
     * 락의 이름을 지정하여 락을 구분할 수 있음.
     */
    String name() default "LOCK:";

    /**
     * 락을 설정할 시간 단위 (기본값: 초 단위)
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 락을 기다릴 시간 (기본값: 5초)
     */
    long waitTime() default 5L;

}
