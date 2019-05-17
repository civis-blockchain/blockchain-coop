package io.civis.blockchain.coop.rest

import io.civis.blockchain.coop.core.FabricChainCodeClient
import io.civis.blockchain.coop.core.model.InvokeArgs
import io.civis.blockchain.coop.core.utils.JsonUtils
import io.civis.blockchain.coop.rest.config.CoopConfig
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
class CoopController(val fabricClient: FabricChainCodeClient, val coopConfig: CoopConfig, val fabricClientProvider: FabricClientProvider) {

    @GetMapping
    fun query(cmd: Cmd, fcn: String, args: Array<String>): CompletableFuture<String>  = execute(InvokeParams(cmd, fcn, args))

    @PostMapping(consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun invoke(@ModelAttribute args: InvokeParams): CompletableFuture<String> = execute(args)

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun invokeJson(@RequestBody args: InvokeParams): CompletableFuture<String> = execute(args)

    fun execute(args: InvokeParams): CompletableFuture<String> {
        val invokeArgs = InvokeArgs(args.fcn, args.args.iterator());
        if (Cmd.invoke.equals(args.cmd)) {
            return doInvoke(invokeArgs)
        } else {
            return doQuery(invokeArgs)
        }
    }

    private fun doQuery(invokeArgs :InvokeArgs): CompletableFuture<String> {
        val client = fabricClientProvider.get()
        return CompletableFuture.completedFuture(
                fabricClient.query(coopConfig.getEndorsers(), client, coopConfig.channel, coopConfig.chaincodeId, invokeArgs)
        )
    }

    private fun doInvoke(invokeArgs :InvokeArgs): CompletableFuture<String> {
        val client = fabricClientProvider.get()
        val future = fabricClient.invoke(coopConfig.getEndorsers(), client, coopConfig.channel, coopConfig.chaincodeId, invokeArgs)
        return future.thenApply {
            InvokeReturn("SUCCESS", "", it.transactionID).toJson()
        }
    }

    enum class Cmd {
        query, invoke
    }

    data class InvokeReturn(val status: String, val info: String, val transactionId: String){
        fun toJson() :String {
            return JsonUtils.toJson(this);
        }
    }

    data class InvokeParams(val cmd: Cmd, val fcn: String, val args: Array<String>)

}