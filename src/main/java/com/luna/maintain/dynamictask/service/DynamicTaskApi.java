package com.luna.maintain.dynamictask.service;

import com.luna.maintain.dynamictask.entity.TaskDefinition;

/**
 * 
 * <p>Date: 14-1-17
 * <p>Version: 1.0
 */
public interface DynamicTaskApi {

    public void addTaskDefinition(TaskDefinition taskDefinition);
    public void updateTaskDefinition(TaskDefinition taskDefinition);
    public void deleteTaskDefinition(boolean forceTermination, Long... taskDefinitionIds);


    public void startTask(Long... taskDefinitionIds);
    public void stopTask(boolean forceTermination, Long... taskDefinitionId);


}
