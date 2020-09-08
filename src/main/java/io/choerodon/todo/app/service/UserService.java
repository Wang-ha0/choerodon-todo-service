package io.choerodon.todo.app.service;

import io.choerodon.todo.infra.dto.UserDTO;

/**
 * 〈功能简述〉
 * 〈〉
 *
 * @author wanghao
 * @since 2020/8/13 17:53
 */
public interface UserService {
    UserDTO createOne(UserDTO userDTO);

    Boolean checkEmailExistInGitlab(String email);

    Boolean checkEmailExist(String email);

    UserDTO queryById(Long id);
}
