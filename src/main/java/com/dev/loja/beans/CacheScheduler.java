package com.dev.loja.beans;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
@Log4j2
public class CacheScheduler {

    @Scheduled(fixedDelay = 24, timeUnit = TimeUnit.HOURS)
    @CacheEvict(cacheNames = {"produtos", "produtos-vitrine"})
    public void limparCacheProdutos() {
        log.info("Feita a limpeza do cache 'Produtos'");
    }
}
