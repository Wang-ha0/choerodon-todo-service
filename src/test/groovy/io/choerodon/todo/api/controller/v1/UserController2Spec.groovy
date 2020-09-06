package io.choerodon.todo.api.controller.v1

import io.choerodon.todo.IntegrationTestConfiguration
import io.choerodon.todo.app.service.UserService
import io.choerodon.todo.infra.dto.UserDTO
import io.choerodon.todo.infra.mapper.UserMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.test.util.ReflectionTestUtils
import spock.lang.Specification
import spock.lang.Subject

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(IntegrationTestConfiguration)
@Subject(UserController)
class UserController2Spec extends Specification {

    def BASE_URL = "/v1/users"
    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserService userService

    @Autowired
    UserMapper realUserMapper;

    UserMapper userMapper = Mock();

    /**
     * 为了演示Mock的使用，这里使用反射的方式把mock对象设置为userService的属性。但是这样会改变整个上下文中userService对
     * userMapper的引用关系。但是为了不影响其他测试类，需要在cleanup将引用关系还原。
     * @return
     */
    def setup() {
        ReflectionTestUtils.setField(userService, "userMapper", userMapper)
    }

    def cleanup() {
        ReflectionTestUtils.setField(userService, "userMapper", realUserMapper)
    }

    /**
     * 测试mock 预期的方法调用次数
     * @return
     */
    def "queryById"() {
        given: "构造参数"
        def userId = 1
        when: "根据id查询用户"
        def entity1 = testRestTemplate.getForEntity(BASE_URL + "/{userId}", UserDTO.class, userId)
        then: "校验结果"
        entity1.statusCode.is2xxSuccessful()
        1 * userMapper.selectByPrimaryKey(_)
    }


    /**
     * 测试mock 模拟返回值以及预期的方法调用次数
     * @return
     */
    def "queryById2"() {
        given: "构造参数"
        def userId = 1
        def userDto = new UserDTO()
        userDto.setEmail("test@hand.com")
        userDto.setEmployeeName("lisi")
        userDto.setEmployeeNumber("001")
        userDto.setId(1)
        when: "根据id查询用户"
        def entity1 = testRestTemplate.getForEntity(BASE_URL + "/{userId}", UserDTO.class, userId)
        then: "校验结果"
        entity1.statusCode.is2xxSuccessful()
        // 执行一次，并且执行时返回userDto
        1 * userMapper.selectByPrimaryKey(_) >> userDto
    }

}
