package com.passerby.local.message.trigger;

import com.passerby.local.message.event.LocalMessageEvent;
import com.passerby.local.message.po.TaskMessageEntityCommand;
import com.passerby.local.message.strategy.IStrategy;
import com.passerby.local.message.strategy.StrategyFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

/**
 * @Author: sunjunyao
 * @Date: 2026/2/5 15:18
 * @Descrpition: 事件监听器
 */

public class LocalMessageEventListener {

    private StrategyFactory strategyFactory;

    public LocalMessageEventListener(StrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }
    @EventListener
    public void onApplicationEvent(LocalMessageEvent localMessageEvent) {

        TaskMessageEntityCommand taskMessageEntityCommand = localMessageEvent.getTaskMessageEntityCommand();
        //todo 目前只支持rocketmq，后续可以添加其他支持
        TaskMessageEntityCommand.NotifyConfig notifyConfig = taskMessageEntityCommand.getNotifyConfig();
        TaskMessageEntityCommand.RocketMQ rocketMQ = notifyConfig.getRocketMQ();
        IStrategy strategy = strategyFactory.getStrategy("RocketMqStrategy");
        strategy.apply(taskMessageEntityCommand);

    }
}
