package com.passerby.local.message.strategy;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: sunjunyao
 * @Date: 2026/2/3 09:58
 * @Descrpition:
 */

public class StrategyFactory {

    private List<IStrategy> strategies;
    private Map<String, IStrategy> strategyMap = new ConcurrentHashMap<>();

    public IStrategy getStrategy(String strategyName) {
        IStrategy strategy = strategyMap.get(strategyName);
        return strategy;
    }
//    public StrategyFactory(List<IStrategy> strategies){
//        for (IStrategy strategy : strategies) {
//            strategyMap.put(strategy.getClass().getName(), strategy);
//        }
//        return this;
//    }

    public StrategyFactory(List<IStrategy> strategies) {
        this.strategies = strategies;
        Map<String, IStrategy> strategyMap=new ConcurrentHashMap<>();
        for (IStrategy strategy : strategies) {
            strategyMap.put(strategy.getClass().getName(), strategy);
        }
        this.strategyMap = strategyMap;
    }
}
