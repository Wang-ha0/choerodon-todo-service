package io.choerodon.todo.app.service.impl;

import org.springframework.stereotype.Service;

import io.choerodon.core.exception.CommonException;
import io.choerodon.todo.app.service.TaskService;
import io.choerodon.todo.infra.dto.TaskDTO;
import io.choerodon.todo.infra.mapper.TaskMapper;

/**
 * 〈功能简述〉
 * 〈〉
 *
 * @author wanghao
 * @since 2020/8/13 17:56
 */
@Service
public class TaskServiceImpl implements TaskService {
    private TaskMapper taskMapper;

    public TaskServiceImpl(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskDTO createOne(TaskDTO taskDTO) {
        if (taskMapper.insert(taskDTO) != 1) {
            throw new CommonException("error.task.insert");
        }
        return taskDTO;
    }

    @Override
    public TaskDTO queryById(Long id) {
        return taskMapper.selectByPrimaryKey(id);
    }

    @Override
    public TaskDTO queryByTaskNumber(String taskNumber) {
        return taskMapper.queryByTaskNumber(taskNumber);
    }

    @Override
    public TaskDTO update(TaskDTO taskDTO) {
        if (taskMapper.updateByPrimaryKeySelective(taskDTO) != 1) {
            throw new CommonException("error.task.update");
        }
        return queryById(taskDTO.getId());
    }

    @Override
    public void deleteById(Long id) {
        taskMapper.deleteByPrimaryKey(id);
    }
}
