package io.choerodon.todo.api.controller.v1

import io.choerodon.todo.IntegrationTestConfiguration
import io.choerodon.todo.app.service.UserService
import io.choerodon.todo.infra.dto.UserDTO
import io.choerodon.todo.infra.feign.DevopsFeignClient
import io.choerodon.todo.infra.mapper.UserMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.util.ReflectionTestUtils
import spock.lang.Specification
import spock.lang.Subject

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(IntegrationTestConfiguration)
@Subject(UserController)
class UserControllerSpec extends Specification {

    def BASE_URL = "/v1/users"
    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserController userController

    UserService userService = Mock()

    def setup() {
        ReflectionTestUtils.setField(userController, "userService", userService)
    }

    def "create"() {
        given: "构造参数"
        def userDto = new UserDTO()
        userDto.setEmail("test@hand.com")
        userDto.setEmployeeName("lisi")
        userDto.setEmployeeNumber("001")

        when: "新建用户"
        def entity = testRestTemplate.postForEntity(BASE_URL, userDto, UserDTO.class)

        then: "校验参数"
        entity.statusCode.is2xxSuccessful()
        1 * userService.createOne(_)
    }


    def "check_email_exist"() {
        given: "构造参数"
        def email = "test@hand.com"

        when: "校验邮箱是否被使用"
        def entity1 = testRestTemplate.getForEntity(BASE_URL + "/check_email_exist?email={email}", Boolean.class, email)

        then: "校验结果"
        entity1.statusCode.is2xxSuccessful()
        1 * userService.checkEmailExist(_)

    }

    def "queryById"() {
        given: "构造参数"
        def userId = 1L

        when: "根据id查询用户"
        def entity1 = testRestTemplate.getForEntity(BASE_URL + "/{userId}", UserDTO.class, userId)

        then: "校验结果"
        entity1.statusCode.is2xxSuccessful()
        1 * userService.queryById(_)
    }


}
