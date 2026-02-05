package com.passerby.local.message.repository.impl;

import com.alibaba.fastjson2.JSON;
import com.passerby.local.message.po.TaskMessageEntityCommand;
import com.passerby.local.message.repository.LocalTaskRepository;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Author: sunjunyao
 * @Date: 2026/1/13 10:12
 * @Descrpition:
 */
@Slf4j
public class LocalTaskRepositoryImpl implements LocalTaskRepository {
    private DataSource dataSource;

    public LocalTaskRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(TaskMessageEntityCommand taskMessagePO)  throws Exception {
        String sql = "INSERT INTO local_task_message (task_id, task_name, notify_type, notify_config, status, parameter_json, house_number ,create_time, update_time) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, taskMessagePO.getTaskId());
            ps.setString(2, taskMessagePO.getTaskName());
            ps.setString(3, taskMessagePO.getNotifyType());
            ps.setString(4, JSON.toJSONString(taskMessagePO.getNotifyConfig()));
            ps.setInt(5, taskMessagePO.getStatus());
            ps.setString(6, taskMessagePO.getParameterJson());
            ps.setInt(7, taskMessagePO.getHouseNumber());
            ps.setObject(8, taskMessagePO.getCreateTime());
            ps.setObject(9, taskMessagePO.getUpdateTime());


            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("数据插入失败，taskId:{},",taskMessagePO.getTaskId(), e);
            throw e;
        }
    }

    @Override
    public int updateStatusByTaskId(String taskId, Integer status) {
        String sql = "UPDATE local_task_message SET status = ?, update_time = NOW() WHERE task_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, status);
            ps.setString(2, taskId);

            return ps.executeUpdate();

        } catch (SQLException e) {
            log.error("更新任务消息状态失败，taskId: {}, status: {}", taskId, status, e);
            throw new RuntimeException("TASK_MESSAGE_UPDATE_STATUS_ERROR 更新任务消息状态失败 " + e.getMessage());
        }
    }
}
