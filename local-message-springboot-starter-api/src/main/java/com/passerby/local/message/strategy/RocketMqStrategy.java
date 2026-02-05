package com.passerby.local.message.strategy;

import cn.hutool.json.JSONUtil;
import com.passerby.local.message.po.TaskMessageEntityCommand;
import com.passerby.local.message.repository.LocalTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @Author: sunjunyao
 * @Date: 2026/2/2 17:33
 * @Descrpition:
 */
@Slf4j
public class RocketMqStrategy implements IStrategy {
    private RocketMQTemplate rocketMQTemplate;
    private LocalTaskRepository localTaskRepository;

    public RocketMqStrategy(RocketMQTemplate rocketMQTemplate,LocalTaskRepository localTaskRepository) {
        this.rocketMQTemplate = rocketMQTemplate;
        this.localTaskRepository = localTaskRepository;
    }

    @Override
    public Object apply(TaskMessageEntityCommand command) {
        try {
            TaskMessageEntityCommand.RocketMQ rocketMQ = command.getNotifyConfig().getRocketMQ();

            // 发送RocketMQ消息
            sendRocketMQMessage(rocketMQ, command.getParameterJson(), command.getTaskId());

            // 通知成功，更新状态为成功
            localTaskRepository.updateStatusByTaskId(command.getTaskId());
            log.info("RocketMQ通知成功 - TaskId: {}, Topic: {}", command.getTaskId(), rocketMQ.getTopic());

            return "success";
        } catch (Exception e) {
            log.error("RocketMQ通知失败 - TaskId: {}, 配置: {}",
                    command.getTaskId(), JSONUtil.toJsonStr(command.getNotifyConfig()), e);
            // 通知失败，更新状态为失败
            localTaskRepository.updateStatusByTaskId(command.getTaskId());
            throw e;
        }
    }

    /**
     * 发送RocketMQ消息
     *
     * @param rocketMQ       RocketMQ配置
     * @param messageContent 消息内容
     * @param taskId         任务ID
     */
    private void sendRocketMQMessage(TaskMessageEntityCommand.RocketMQ rocketMQ,
                                     String messageContent, String taskId) {
        try {
            if (null == rocketMQTemplate) {
                log.error("应用服务方，尚未配置 RocketMQ Template 不能完成 RocketMQ 发送");
                return;
            }

            // 构建目标地址
            String destination = buildDestination(rocketMQ);

            // 构建消息
            Message<String> message = MessageBuilder
                    .withPayload(messageContent)
                    .setHeader("taskId", taskId)
                    .build();

            // 根据是否有延迟级别选择发送方式
            if (rocketMQ.getDelayLevel() != null && rocketMQ.getDelayLevel() > 0) {
                // 发送延迟消息
                rocketMQTemplate.syncSend(destination, message, 3000, rocketMQ.getDelayLevel());
                log.info("RocketMQ延迟消息发送成功 - TaskId: {}, Topic: {}, DelayLevel: {}",
                        taskId, rocketMQ.getTopic(), rocketMQ.getDelayLevel());
            } else {
                // 发送普通消息
                rocketMQTemplate.syncSend(destination, message);
                log.info("RocketMQ消息发送成功 - TaskId: {}, Topic: {}", taskId, rocketMQ.getTopic());
            }

        } catch (Exception e) {
            log.error("发送RocketMQ消息失败 - Topic: {}, Message: {}", rocketMQ.getTopic(), messageContent, e);
            throw e;
        }
    }

}
