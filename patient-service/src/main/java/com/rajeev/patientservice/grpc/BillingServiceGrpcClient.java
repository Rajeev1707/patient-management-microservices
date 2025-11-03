package com.rajeev.patientservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class BillingServiceGrpcClient {

    private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpcClient.class);
    private final BillingServiceGrpc.BillingServiceBlockingStub stub;

    //localhost:9090/BillingService/createPatientAccount
    public BillingServiceGrpcClient(
        @Value("${billing.service.address:localhost}") String serverAddress,
        @Value("${billing.service.grpc.port:9090}") int serverPort)
    {
    log.info("connecting to billing service GRPC service at {}: {}", serverAddress,serverPort);
    ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort).usePlaintext().build();
    stub = BillingServiceGrpc.newBlockingStub(channel);
    }

    public BillingResponse createBilling(String patientId,String name, String email) {
        BillingRequest request = BillingRequest.newBuilder().setName(name).setEmail(email).build();
        BillingResponse response = stub.createBillingAccount(request);
        log.info("billing account created successfully recieved response from GRPC server :{}", response);
        return response;
    }
}
