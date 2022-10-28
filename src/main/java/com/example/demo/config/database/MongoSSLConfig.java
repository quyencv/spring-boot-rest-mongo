package com.example.demo.config.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

@Configuration
public class MongoSSLConfig {

    @Value("${mongodb.sSLEnabled}")
    private boolean sSLEnabled;

    @Bean
    public MongoClientSettingsBuilderCustomizer mongoClientSettings() throws GeneralSecurityException, IOException {
        String trustStorePath = "/ssl/trust";
        // Create Truststore using Key store api
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null);
        // Add Certificate to Key store
        CertificateFactory certF = CertificateFactory.getInstance("X.509");
//        Certificate cert = certF.generateCertificate(new URL("https://s3.amazonaws.com/rds-downloads/rds-ca-2019-root.pem").openStream());
        File file = ResourceUtils.getFile("classpath:rds-combined-ca-bundle.pem");
        Certificate cert = certF.generateCertificate(new FileInputStream(file));
        keyStore.setCertificateEntry("mongo-cert", cert);

        // Write Key Store
        try (FileOutputStream out = new FileOutputStream(trustStorePath)) {
            keyStore.store(out, "notihub@123".toCharArray());
        }

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

        return builder -> {
//            builder.applyToSocketSettings(bldr -> {
//                //Apply any custom socket settings
//            });
            builder.applyToSslSettings(blockBuilder -> {
                blockBuilder.invalidHostNameAllowed(true);
                if (sSLEnabled) {
                    blockBuilder.enabled(true);
                    blockBuilder.context(sslContext);
                }
            });
        };
    }
}