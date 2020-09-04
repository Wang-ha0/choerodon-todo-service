package io.choerodon.todo.app.service;

import io.choerodon.todo.infra.dto.TaskDTO;

/**
 * 〈功能简述〉
 * 〈〉
 *
 * @author wanghao
 * @since 2020/8/13 17:56
 */
public interface TaskService {
    TaskDTO createOne(TaskDTO taskDTO);

    TaskDTO queryById(Long id);

    TaskDTO queryByTaskNumber(String taskNumber);

    TaskDTO update(TaskDTO taskDTO);

    void deleteById(Long id);
}
