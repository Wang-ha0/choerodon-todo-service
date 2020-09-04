package io.choerodon.todo.api.controller.v1;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.choerodon.todo.app.service.UserService;
import io.choerodon.todo.infra.dto.UserDTO;

/**
 * 〈功能简述〉
 * 〈〉
 *
 * @author wanghao
 * @since 2020/8/13 18:00
 */
@RestController
@RequestMapping(value = "/v1/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Permission(level = ResourceLevel.SITE, permissionLogin = true)
    @ApiOperation(value = "创建todo用户")
    private ResponseEntity<UserDTO> create(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.createOne(userDTO), HttpStatus.OK);
    }

    @GetMapping("check_email_exist")
    @Permission(level = ResourceLevel.SITE, permissionLogin = true)
    @ApiOperation(value = "校验gitlab用户邮箱是否存在")
    private ResponseEntity<Boolean> checkEmailExist(@RequestParam String email) {
        return new ResponseEntity<>(userService.checkEmailExist(email), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Permission(level = ResourceLevel.SITE, permissionLogin = true)
    @ApiOperation(value = "根据id查询用户")
    private ResponseEntity<UserDTO> queryById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.queryById(id), HttpStatus.OK);
    }
}