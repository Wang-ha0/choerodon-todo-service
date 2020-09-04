package io.choerodon.todo

import com.fasterxml.jackson.databind.ObjectMapper
import io.choerodon.core.exception.CommonException
import io.choerodon.core.oauth.CustomUserDetails
import io.choerodon.liquibase.LiquibaseConfig
import io.choerodon.liquibase.LiquibaseExecutor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.security.jwt.JwtHelper
import org.springframework.security.jwt.crypto.sign.MacSigner
import org.springframework.security.jwt.crypto.sign.Signer
import org.springframework.test.context.TestPropertySource
import spock.mock.DetachedMockFactory

import javax.annotation.PostConstruct

@TestConfiguration
@Import(LiquibaseConfig)
@TestPropertySource("classpath:config/application-test.yml")
class IntegrationTestConfiguration {

    // spock提供的可以在外部配置类中mock对象的工厂类
    private final detachedMockFactory = new DetachedMockFactory()

    @Value('${choerodon.oauth.jwt.key:hzero}')
    String key

    @Autowired
    LiquibaseExecutor liquibaseExecutor

    @Autowired
    TestRestTemplate testRestTemplate

    ObjectMapper objectMapper = new ObjectMapper()


    @PostConstruct
    void init() {
        // 初始化数据库
        liquibaseExecutor.execute()
        // 给请求头添加jwt_token
        setTestRestTemplateJWT()
    }

    /**
     * 请求头添加jwt_token
     */
    private void setTestRestTemplateJWT() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory())
        testRestTemplate.getRestTemplate().setInterceptors([new ClientHttpRequestInterceptor() {
            @Override
            ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
                httpRequest.getHeaders()
                        .add('Jwt_token', createJWT(key, objectMapper))
                return clientHttpRequestExecution.execute(httpRequest, bytes)
            }
        }])
    }

    String createJWT(final String key, final ObjectMapper objectMapper) {
        Signer signer = new MacSigner(key)
        CustomUserDetails details = new CustomUserDetails('default', 'unknown', Collections.emptyList())
        details.setUserId(1L);
        details.setLanguage("zh_CN");
        details.setTimeZone("GMT+8");
        details.setEmail("hand@hand-china.com");
        details.setOrganizationId(1L);
        try {
            return 'Bearer ' + JwtHelper.encode(objectMapper.writeValueAsString(details), signer).getEncoded()
        } catch (IOException e) {
            throw new CommonException(e)
        }
    }
}

