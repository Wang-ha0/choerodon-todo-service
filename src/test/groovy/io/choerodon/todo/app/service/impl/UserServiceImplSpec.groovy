package io.choerodon.todo.app.service.impl

import io.choerodon.core.exception.CommonException
import io.choerodon.todo.IntegrationTestConfiguration
import io.choerodon.todo.app.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import spock.lang.Specification
import spock.lang.Subject

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(IntegrationTestConfiguration)
@Subject(UserServiceImpl)
class UserServiceImplSpec extends Specification {

    @Autowired
    UserService userService

    void setup() {
    }

    /**
     * 测试thrown（）
     * @return
     */
    def "QueryById"() {
        when: "根据id查询用户"
        userService.queryById(null)
        then: "校验结果"
        def e = thrown(Exception)
        e instanceof CommonException
        e.getMessage() == "error.id.is.null"
    }

    /**
     * 测试notThrown（）
     * @return
     */
    def "QueryById2"() {
        when: "根据id查询用户"
        def userDto = userService.queryById(1)
        then: "校验结果"
        notThrown(CommonException)
        userDto == null
    }
}
