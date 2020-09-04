package io.choerodon.todo.api.controller.v1;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.choerodon.todo.app.service.TaskService;
import io.choerodon.todo.infra.dto.TaskDTO;

/**
 * 〈功能简述〉
 * 〈〉
 *
 * @author wanghao
 * @since 2020/8/13 18:01
 */
@RestController
@RequestMapping(value = "/v1/tasks")
public class TaskController {
    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @Permission(level = ResourceLevel.SITE)
    @ApiOperation(value = "创建Task")
    public ResponseEntity<TaskDTO> create(@RequestBody TaskDTO taskDTO) {
        return new ResponseEntity<>(taskService.createOne(taskDTO), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @Permission(level = ResourceLevel.SITE)
    @ApiOperation(value = "根据Id查询task")
    public ResponseEntity<TaskDTO> queryById(@PathVariable Long id) {
        return new ResponseEntity<>(taskService.queryById(id), HttpStatus.OK);
    }

    @GetMapping("/taskNumber/{taskNumber}")
    @Permission(level = ResourceLevel.SITE)
    @ApiOperation(value = "根据TaskNumber查询task")
    public ResponseEntity<TaskDTO> queryByTaskNumber(@PathVariable String taskNumber) {
        return new ResponseEntity<>(taskService.queryByTaskNumber(taskNumber), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Permission(level = ResourceLevel.SITE)
    @ApiOperation(value = "根据Id删除ask")
    public void delete(@PathVariable Long id) {
        taskService.deleteById(id);
    }


    @PutMapping("/{id}")
    @Permission(level = ResourceLevel.SITE)
    @ApiOperation(value = "更新task")
    public ResponseEntity<TaskDTO> update(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        taskDTO.setId(id);
        return new ResponseEntity<>(taskService.update(taskDTO), HttpStatus.OK);
    }
}
