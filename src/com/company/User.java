package com.company;

import com.company.models.Identity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.*;

public class User {

    private static final int BROKER_SERVER_PORT = 6789;
    private PrivateKey privateKey;

    public KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 2048;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);

        return keyPairGenerator.genKeyPair();
    }

    public void registerToBroker() throws IOException, NoSuchAlgorithmException {
        Socket userSocket = new Socket("localhost", BROKER_SERVER_PORT);

        DataOutputStream outToServer = new DataOutputStream(userSocket.getOutputStream());

        KeyPair keyPair = buildKeyPair();
        PublicKey pubKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();

        Identity identity = new Identity();
        identity.setIdentity("User");
        identity.setPublicKey(pubKey);

        String identityString = new ObjectMapper().writeValueAsString(identity);

        System.out.println(identityString);

        outToServer.writeBytes(identityString +"\n");

        userSocket.close();
    }
}
