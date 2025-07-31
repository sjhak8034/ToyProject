package com.example.toyproject.common.reentrantLock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static com.example.toyproject.common.reentrantLock.KeyValueProcessor.kv;


/**
 * @DistributedLock 어노테이션이 적용된 메서드에 대해 Redisson 기반의 분산 락을 적용하는 AOP 클래스.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class ReentrantLockAop {

    private final AopForTransaction aopForTransaction;

    /**
     * 분산 락이 적용된 메서드 실행 시 락을 획득하고, 메서드 실행 후 락을 해제하는 메서드.
     *
     * @param joinPoint AOP의 실행 지점을 나타내는 객체
     * @return 메서드 실행 결과
     * @throws Throwable 예외 발생 시 예외를 던짐
     */
    @Around("@annotation(ReentrantLockSetting)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ReentrantLockSetting annotation = method.getAnnotation(ReentrantLockSetting.class);

        // 락의 고유 키를 생성
        String key = annotation.name() +  CustomSpringELParser.getDynamicValue(
            signature.getParameterNames(), joinPoint.getArgs(), annotation.key());
        KeyLockManager keyLockManager = new KeyLockManager();


        try {
            // 락 획득 시도 (대기 시간 및 임대 시간 적용)
            boolean available = keyLockManager.tryLock(
                key,
                annotation.waitTime(),
                annotation.timeUnit()
            );
            if (!available) {
                return false; // 락 획득 실패 시 false 반환
            }
            log.info("Lock acquired for key [{}]", key);
            // 락을 획득한 상태에서 메서드 실행
            return aopForTransaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            throw new InterruptedException(); // 쓰레드 인터럽트 예외 발생 시 처리
        } finally {
            try {
                // 락 해제
                log.info("Lock Unlocked ");
                keyLockManager.unlock(key);
            } catch (IllegalMonitorStateException e) {
                log.info("Lock Already Unlocked {} {}",
                    kv("serviceName", method.getName()),
                    kv("key", key)
                );
            }
        }
    }
}
