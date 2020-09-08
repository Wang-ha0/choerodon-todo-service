package io.choerodon.todo.app.service.impl

import io.choerodon.core.exception.CommonException
import io.choerodon.todo.IntegrationTestConfiguration
import io.choerodon.todo.app.service.UserService
import io.choerodon.todo.infra.dto.UserDTO
import io.choerodon.todo.infra.feign.DevopsFeignClient
import io.choerodon.todo.infra.mapper.UserMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.util.ReflectionTestUtils
import spock.lang.Specification
import spock.lang.Subject

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(IntegrationTestConfiguration)
@Subject(UserServiceImpl)
class UserServiceImplSpec extends Specification {

    @Autowired
    UserService userService

    UserMapper userMapper = Mock()

    DevopsFeignClient devopsFeignClient = Mock()

    void setup() {
        ReflectionTestUtils.setField(userService, "devopsFeignClient", devopsFeignClient)
        ReflectionTestUtils.setField(userService, "userMapper", userMapper)
    }

    /**
     * 测试调用次数
     * @return
     */
    def "queryById"() {
        given: "构造参数"
        def userId = 1L
        when: "根据id查询用户"
        userService.queryById(userId)
        then: "校验结果"
        1 * userMapper.selectByPrimaryKey(_)
    }

    /**
     * 测试返回值以及调用次数
     * @return
     */
    def "queryById2"() {
        given: "构造参数"
        def userId = 1L
        def user = new UserDTO()
        user.setEmail("test@hand.com")
        user.setEmployeeName("lisi")
        user.setEmployeeNumber("001")
        user.setId(1)

        when: "根据id查询用户"
        def userDTO = userService.queryById(userId)
        then: "校验结果"
        1 * userMapper.selectByPrimaryKey(_) >> user
        userDTO == user
    }

    def "createOne"() {
        given: "构造参数"
        def userDto = new UserDTO()
        userDto.setEmail("test@hand.com")
        userDto.setEmployeeName("lisi")
        userDto.setEmployeeNumber("001")

        when: "新建用户 - 创建成功"
        userService.createOne(userDto)
        then: "校验结果"
        1 * userMapper.insert(_) >> 1
        // 使用notThrown校验，是否没有出现异常
        notThrown(CommonException)

        when: "新建用户 - 创建失败"
        userService.createOne(userDto)
        then: "校验结果"
        1 * userMapper.insert(_) >> 0
        // 使用thrown校验，是否出现指定异常
        def e = thrown(Exception)
        e instanceof CommonException
        e.getMessage() == "error.user.insert"
    }

    /**
     * 测试方法返回值
     * @return
     */
    def "checkEmailExistInGitlab"() {
        given: "构造参数"
        def email = "test@hand.com"
        ResponseEntity<Boolean> responseEntity = new ResponseEntity<>(true, HttpStatus.OK)
        devopsFeignClient.checkGitlabEmail(_ as String) >> responseEntity

        when: "校验邮箱是否被使用"
        def result = userService.checkEmailExistInGitlab(email)
        then: "校验结果 - 邮箱已使用"
        result == true
    }

    /**
     *  测试连续调用时返回不同的值
     * @return
     */
    def "checkEmailExistInGitlab2"() {
        given: "构造参数"
        def email = "test@hand.com"
        ResponseEntity<Boolean> responseEntity1 = new ResponseEntity<>(true, HttpStatus.OK)
        ResponseEntity<Boolean> responseEntity2 = new ResponseEntity<>(false, HttpStatus.OK)
        devopsFeignClient.checkGitlabEmail(_ as String) >>> [responseEntity1, responseEntity2]

        when: "校验邮箱是否被使用"
        def result1 = userService.checkEmailExistInGitlab(email)
        then: "校验结果 - 邮箱已使用"
        result1 == true

        when: "校验邮箱是否被使用"
        def result2 = userService.checkEmailExistInGitlab(email)
        then: "校验结果 - 邮箱未使用"
        result2 == false
    }
}
