package net.lr.tasklist.persistence.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import net.lr.tasklist.model.Task;
import net.lr.tasklist.model.TaskService;

@Named
public class TaskServiceImpl implements TaskService {
    Map<Integer, Task> taskMap;

    public TaskServiceImpl() {
        taskMap = new HashMap<Integer, Task>();
        Task task = new Task();
        task.setId(1);
        task.setTitle("Buy some coffee");
        task.setDescription("Take the extra strong");
        addTask(task);
        task = new Task();
        task.setId(2);
        task.setTitle("Finish karaf tutorial");
        task.setDescription("Last check and wiki upload");
        addTask(task);
    }

    @Override
    public Task getTask(Integer id) {
        return taskMap.get(id);
    }

    @Override
    public void addTask(Task task) {
        taskMap.put(task.getId(), task);
    }

    @Override
    public Collection<Task> getTasks() {
        // taskMap.values is not serializable
        return new ArrayList<Task>(taskMap.values());
    }

    @Override
    public void updateTask(Task task) {
        taskMap.put(task.getId(), task);
    }

    @Override
    public void deleteTask(Integer id) {
        taskMap.remove(id);
    }

}