package com.user.management.config;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.avro.ipc.netty.NettyTransceiver;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RpcConfig {

    public NettyTransceiver getRpcClient() throws IOException{
        log.info("Setting up RPC client connection.");
        return new NettyTransceiver(new InetSocketAddress(65110));
    } 
}
