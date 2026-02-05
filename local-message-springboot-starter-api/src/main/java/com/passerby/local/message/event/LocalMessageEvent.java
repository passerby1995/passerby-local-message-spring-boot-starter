package com.passerby.local.message.event;

import com.passerby.local.message.po.TaskMessageEntityCommand;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * @Author: sunjunyao
 * @Date: 2026/2/2 16:57
 * @Descrpition:
 */
public class LocalMessageEvent extends ApplicationEvent {

    private TaskMessageEntityCommand taskMessageEntityCommand;

    public LocalMessageEvent(Object source, TaskMessageEntityCommand taskMessageEntityCommand) {
        super(source);
        this.taskMessageEntityCommand = taskMessageEntityCommand;
    }

    public TaskMessageEntityCommand getTaskMessageEntityCommand() {
        return taskMessageEntityCommand;
    }
}
