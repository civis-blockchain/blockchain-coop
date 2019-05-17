package io.civis.blockchain.coop.rest

import io.civis.blockchain.coop.core.utils.JsonUtils
import org.assertj.core.api.Assertions.assertThat
import java.util.*
import org.junit.jupiter.api.*
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap





class CoopApiTest : WebBaseTest() {

    companion object {
        val hash = UUID.randomUUID().toString()
    }

    @Test
    fun shouldReturnNotEmptyValue_WhenExecuteQuery() {
        val uri = baseUrl().pathSegment("/")
                .queryParam("cmd", "query")
                .queryParam("fcn", "query")
                .queryParam("args", "a")
                .build().toUri()

        val res = this.restTemplate.getForEntity(uri,String::class.java)
        assertThat(res.statusCodeValue).isEqualTo(200)
        assertThat(res.body).isNotEmpty();
    }

    @Test
    fun shouldSUCCESSMessage_WhenInvokeWithFormUrlEncoded() {
        val uri = baseUrl().pathSegment("/").build().toUri()
        val map = LinkedMultiValueMap<String, String>()
        map.add("cmd", "invoke")
        map.add("fcn", "invoke")
        map.add("args", "a")
        map.add("args", "b")
        map.add("args", "10")
        val headers = HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED)

        val request = HttpEntity(map, headers)
        val res = this.restTemplate.postForEntity(uri, request, CoopController.InvokeReturn::class.java)
        assertThat(res.statusCodeValue).isEqualTo(200)
        assertThat(res.body).isNotNull
        assertThat(res.body!!.status).isEqualTo("SUCCESS")
        assertThat(res.body!!.transactionId).isNotEmpty()
    }

    @Test
    fun shouldSUCCESSMessage_WhenInvokeWithJSON() {
        val uri = baseUrl().pathSegment("/").build().toUri()
        val params = CoopController.InvokeParams(CoopController.Cmd.invoke, "invoke", arrayOf("a", "b", "10"))
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val request = HttpEntity(params, headers)
        val res = this.restTemplate.postForEntity(uri, request, CoopController.InvokeReturn::class.java)
        assertThat(res.statusCodeValue).isEqualTo(200)
        assertThat(res.body).isNotNull
        assertThat(res.body!!.status).isEqualTo("SUCCESS")
        assertThat(res.body!!.transactionId).isNotEmpty()
    }


}