package com.passerby.local.message.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: sunjunyao
 * @Date: 2026/1/13 10:14
 * @Descrpition:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskMessageEntityCommand {
    /**
     * 自增主键
     */
    private Long id;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 通知类型
     */
    private String notifyType;

    /**
     * 通知配置（JSON格式，包含mqTopic和url等信息）
     */
    private NotifyConfig notifyConfig;

    /**
     * 状态（0-待处理，1-处理中，2-已完成，3-失败）
     */
    private Integer status;

    /**
     * 参数JSON
     */
    private String parameterJson;

    /**
     * 门牌号
     */
    private Integer houseNumber;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * RocketMQ配置类
     * <p>
     * 用于配置RocketMQ的主题、标签等信息
     * </p>
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RocketMQ {
        /**
         * RocketMQ主题名称
         */
        private String topic;

        /**
         * 消息标签（可选）
         */
        private String tag;

        /**
         * 消息键（可选）
         */
        private String key;

        /**
         * 延迟级别（可选）
         */
        private Integer delayLevel;

        /**
         * 生产者组（可选）
         */
        private String producerGroup;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NotifyConfig {
        /**
         * RocketMQ通知配置
         */
        private RocketMQ rocketMQ;
    }
}
