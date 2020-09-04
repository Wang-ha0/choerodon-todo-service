package io.choerodon.todo.infra.mapper;

import feign.Param;

import io.choerodon.mybatis.common.BaseMapper;
import io.choerodon.todo.infra.dto.TaskDTO;

/**
 * 〈功能简述〉
 * 〈〉
 *
 * @author wanghao
 * @since 2020/8/13 17:51
 */
public interface TaskMapper extends BaseMapper<TaskDTO> {

    TaskDTO queryByTaskNumber(@Param("taskNumber") String taskNumber);
}
