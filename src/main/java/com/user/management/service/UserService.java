package com.user.management.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

import com.user.management.Models.Addresses;
import com.user.management.Models.CompleteUsers;
import com.user.management.Models.Users;
import com.user.management.config.RpcConfig;
import com.user.management.exceptions.UserApiDatabaseException;
import com.user.management.exceptions.UserApiRcpException;
import com.user.management.repository.UserRepository;
import com.users.management.avro.AddressRecords;
import com.users.management.avro.AvroAddresses;

import org.apache.avro.ipc.netty.NettyTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private RpcConfig rpcConfig;

    public UUID save(CompleteUsers completeUsers) throws Exception {
        final UUID userId = UUID.randomUUID();
        final Users user  = completeUsers.getUser();
        final Addresses address = completeUsers.getAddress();
        user.setId(userId);
        address.setId(userId);

        try{
            usersRepository.save(user);
        } catch (Exception e ){
            throw new UserApiDatabaseException(e);
        }   
        final String savedAddressId = saveAddress(completeUsers.getAddress());
        return UUID.fromString(savedAddressId);  
    }

    public CompleteUsers getById(UUID id) throws IOException, InterruptedException {
        final Users user;

        try {
            user = usersRepository.findById(id).get();
        } catch(Exception e){
            throw new UserApiDatabaseException(e);
        }
        
        final AvroAddresses avroAddresses = getAddress(id.toString());
        final Addresses address = transformAvroAddressToAddress(avroAddresses);
        return CompleteUsers.builder().user(user).address(address).build();
    }


    private String saveAddress(final Addresses addresses) {
        try{
            NettyTransceiver rpcClient  = rpcConfig.getRpcClient();
            AddressRecords client = (AddressRecords) SpecificRequestor.getClient(AddressRecords.class, rpcClient);
            AvroAddresses avroMessage = transformAddressToAvroAddress(addresses);
            final String response = client.save(avroMessage).toString();
            log.info("Received a user id for address. Message: {}", response);   
            rpcClient.close(); 
            return response;
        } catch (Exception e) {
            throw new UserApiRcpException(e);
        }
        
        
    }

    private AvroAddresses getAddress(final String id) throws IOException {
        try{
            NettyTransceiver rpcClient  = rpcConfig.getRpcClient();
            AddressRecords client = (AddressRecords) SpecificRequestor.getClient(AddressRecords.class, rpcClient);
            final AvroAddresses response = (AvroAddresses) client.get(id);
            log.info("Received an avro addresses. Message: {}", response);   
            rpcClient.close(); 
            return response;
        } catch(Exception e) {
            throw new UserApiRcpException(e);
        }
    }

    private AvroAddresses transformAddressToAvroAddress(final Addresses address){
        return AvroAddresses.newBuilder()
            .setId(address.getId().toString())
            .setAddress1(address.getAddress1())
            .setAddress2(address.getAddress2())
            .setCity(address.getCity())
            .setState(address.getState())
            .setZip(address.getZip())
            .setCountry(address.getCountry())
            .build();
    }

    private Addresses transformAvroAddressToAddress(final AvroAddresses avroAddresses){
        return Addresses.builder()
            .id(UUID.fromString(avroAddresses.getId()))
            .address1(avroAddresses.getAddress1())
            .address2(avroAddresses.getAddress2())
            .city(avroAddresses.getCity())
            .state(avroAddresses.getState())
            .zip(avroAddresses.getZip())
            .country(avroAddresses.getCountry())
            .build();
    }
}
