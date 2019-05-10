package io.civis.blockchain.coop.rest

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.util.UriComponentsBuilder

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebBaseTest {

    @LocalServerPort
    protected lateinit var port: Integer

    @Autowired
    protected lateinit var restTemplate: TestRestTemplate


    protected fun baseUrl(): UriComponentsBuilder {
        return UriComponentsBuilder.fromHttpUrl("http://localhost:$port")
    }

}