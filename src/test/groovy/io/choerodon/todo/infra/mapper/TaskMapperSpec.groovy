package io.choerodon.todo.infra.mapper

import io.choerodon.todo.IntegrationTestConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import spock.lang.Specification
import spock.lang.Subject

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(IntegrationTestConfiguration)
@Subject(TaskMapper)
class TaskMapperSpec extends Specification {

    @Autowired
    private TaskMapper taskMapper;

    void setup() {
    }

    /**
     * 我们再init-data excel中初始化了一条全局的任务记录，这里直接查询该记录
     */
    def "QueryByTaskNumber"() {
        given:
        def taskId = 1L
        when:
        def taskDTO = taskMapper.selectByPrimaryKey(taskId)
        then:
        taskDTO.getTaskNumber() != null
    }
}
