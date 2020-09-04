package io.choerodon.todo.app.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.choerodon.core.exception.CommonException;
import io.choerodon.todo.app.service.UserService;
import io.choerodon.todo.infra.dto.UserDTO;
import io.choerodon.todo.infra.feign.DevopsFeignClient;
import io.choerodon.todo.infra.mapper.UserMapper;

/**
 * 〈功能简述〉
 * 〈〉
 *
 * @author wanghao
 * @since 2020/8/13 17:54
 */
@Service
public class UserServiceImpl implements UserService {
    private UserMapper userMapper;

    private DevopsFeignClient devopsFeignClient;

    public UserServiceImpl(UserMapper userMapper, DevopsFeignClient devopsFeignClient) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO createOne(UserDTO userDTO) {
        if (userMapper.insert(userDTO) != 1) {
            throw new CommonException("error.user.insert");
        }
        return userDTO;
    }

    @Override
    public Boolean checkEmailExist(String email) {
        // 校验email在gitlab中是否已经使用
        ResponseEntity<Boolean> responseEntity = devopsFeignClient.checkGitlabEmail(email);

        Boolean existFlag = responseEntity.getBody();
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        int count = userMapper.selectCount(userDTO);

        return existFlag || count > 0;
    }

    @Override
    public UserDTO queryById(Long id) {
        if (id == null) {
            throw new CommonException("error.id.is.null");
        }
        return userMapper.selectByPrimaryKey(id);
    }
}
