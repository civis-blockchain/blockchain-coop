package io.civis.blockchain.coop.core;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;
import io.civis.blockchain.coop.core.config.FabricConfig;
import io.civis.blockchain.coop.core.factory.FabricClientFactory;
import io.civis.blockchain.coop.core.model.Endorser;
import io.civis.blockchain.coop.core.model.InvokeArgs;
import io.netty.handler.ssl.ReferenceCountedOpenSslClientContext;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.User;
import org.junit.jupiter.api.Test;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

class FabricChainCodeClientTest {

    public static String CLIENT_CONFIG = "file:../infra/dev/fabric/config.json";
    public static String CRYPTO_CONFIG = "file:../infra/dev/fabric";
    public static String BCLAN = "bclan";
    public static String USER_NAME = "dfef919ef60296ca173d99f2132becde";
    public static String USER_PASSWORD = "660bd560b383333750f629362e0160f9";

    private FabricUserClient client = FabricUserClient.fromConfigFile(CLIENT_CONFIG, CRYPTO_CONFIG);
    private FabricConfig fabricConfig = FabricConfig.loadFromFile(CLIENT_CONFIG);
    private FabricClientFactory fabricClientFactory = FabricClientFactory.factory(fabricConfig, CRYPTO_CONFIG);
    private FabricChainCodeClient chainCodeClient = FabricChainCodeClient.fromConfigFile(CLIENT_CONFIG, CRYPTO_CONFIG);
    private List<Endorser> endorsers = ImmutableList.of(new Endorser("peer0", BCLAN));

    FabricChainCodeClientTest() throws IOException {
    }

    @Test
    void query() throws Exception {
        User user = client.enroll(USER_NAME, USER_PASSWORD, BCLAN);
        String value = chainCodeClient.query(endorsers, fabricClientFactory.getHfClient(user), "sandbox", "ex02", InvokeArgs.from("query", "a"));
        assertThat(Ints.tryParse(value)).isNotNull();
    }

    @Test
    void invoke() throws Exception {
        User user = client.enroll(USER_NAME, USER_PASSWORD, BCLAN);
        CompletableFuture<BlockEvent.TransactionEvent> value = chainCodeClient.invoke(endorsers, fabricClientFactory.getHfClient(user), "sandbox", "ex02", InvokeArgs.from(
                "invoke",
                "a", "b", "10"
        ));
        BlockEvent.TransactionEvent transaction = value.get();
        assertThat(transaction.isValid()).isTrue();
    }

}