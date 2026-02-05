package com.passerby.local.message.config;

import com.passerby.local.message.repository.LocalTaskRepository;
import com.passerby.local.message.repository.impl.LocalTaskRepositoryImpl;
import com.passerby.local.message.service.IHandleService;
import com.passerby.local.message.service.impl.HandleService;
import com.passerby.local.message.strategy.IStrategy;
import com.passerby.local.message.strategy.RocketMqStrategy;
import com.passerby.local.message.strategy.StrategyFactory;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.List;

/**
 * @Author: sunjunyao
 * @Date: 2026/1/6 10:13
 * @Descrpition:
 */
@Configuration
public class LocalMessageAutoConfig {

    @Bean
    public RocketMqStrategy rocketMqStrategy(RocketMQTemplate rocketMQTemplate,LocalTaskRepository localTaskRepository) {
        return new RocketMqStrategy(rocketMQTemplate,localTaskRepository);
    }

    @Bean
    public StrategyFactory strategyFactory(List<IStrategy> strategies) {
       return new StrategyFactory(strategies);
    }


    @Bean
    public LocalTaskRepository localTaskRepository(DataSource dataSource){
        return new LocalTaskRepositoryImpl(dataSource);
    }

    @Bean
    public IHandleService handleService(LocalTaskRepository localTaskRepository, ApplicationEventPublisher applicationEventPublisher) {
        return new HandleService(localTaskRepository,applicationEventPublisher);
    }
    @Bean
    @ConditionalOnMissingBean
    public TransactionTemplate transactionTemplate(
            PlatformTransactionManager txManager) {
        return new TransactionTemplate(txManager);
    }

    @Bean
    public LocalTaskMessageAspect localTaskMessageAspect(IHandleService iHandleService, TransactionTemplate transactionTemplate){
        return new LocalTaskMessageAspect(iHandleService,transactionTemplate);
    }
}
