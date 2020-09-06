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
    UserService userService
    @Autowired
    UserMapper userMapper

    DevopsFeignClient devopsFeignClient = Mock();

    def setup() {
        ReflectionTestUtils.setField(userService, "devopsFeignClient", devopsFeignClient)
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
        entity.getBody().getEmployeeName() == userDto.getEmployeeName()
        entity.getBody().getId() != null

        cleanup: "清除数据"
        // 为了不影响其他单元测试结果，需要在测试结束时删除插入的数据
        userMapper.deleteByPrimaryKey(entity.getBody().getId())

    }

    /**
     * 测试 连续调用时返回不同的值
     * @return
     */
    def "check_email_exist"() {
        given: "构造参数"
        def email = "test@hand.com"
        ResponseEntity<Boolean> responseEntity1 = new ResponseEntity<>(true, HttpStatus.OK)
        ResponseEntity<Boolean> responseEntity2 = new ResponseEntity<>(false, HttpStatus.OK)
        devopsFeignClient.checkGitlabEmail(_ as String) >>> [responseEntity1, responseEntity2]

        when: "校验邮箱是否被使用"
        def entity1 = testRestTemplate.getForEntity(BASE_URL + "/check_email_exist?email={email}", Boolean.class, email)
        then: "校验结果 - 邮箱已使用"
        entity1.statusCode.is2xxSuccessful()
        entity1.body == true

        when: "校验邮箱是否被使用"
        def entity2 = testRestTemplate.getForEntity(BASE_URL + "/check_email_exist?email={email}", Boolean.class, email)
        then: "校验结果 - 邮箱未使用"
        entity2.statusCode.is2xxSuccessful()
        entity2.body == false

    }

}
