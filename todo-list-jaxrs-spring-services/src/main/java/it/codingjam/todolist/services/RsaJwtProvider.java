package it.codingjam.todolist.services;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.google.common.base.Preconditions;
import it.cosenonjaviste.security.jwt.utils.JwtTokenBuilder;
import org.apache.catalina.realm.GenericPrincipal;
import org.springframework.context.annotation.Profile;

import javax.inject.Named;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

@Named
//@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Profile("rsa")
public class RsaJwtProvider implements JwtProvider {

    @Override
    public String createJwt(GenericPrincipal principal) {
        return JwtTokenBuilder.create(Algorithm.RSA256(retrieveKey()))
                .userId(principal.getName())
                .roles(Arrays.asList(principal.getRoles()))
                .expirySecs(1800)
                .build();
    }

    private RSAKeyProvider retrieveKey() {
        String keystorePwd = System.getProperty("KEYSTORE_PWD");
        Preconditions.checkArgument(keystorePwd != null, "Please provide KEYSTORE_PWD system property");
        try {
            KeyStore keyStore = getKeyStore(keystorePwd);
            String alias = keyStore.aliases().nextElement();

            return new RSAKeyProvider() {
                @Override
                public RSAPublicKey getPublicKeyById(String keyId) {
                    try {
                        return (RSAPublicKey) keyStore.getCertificate(getPrivateKeyId()).getPublicKey();
                    } catch (KeyStoreException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public RSAPrivateKey getPrivateKey() {
                    try {
                        return (RSAPrivateKey) keyStore.getKey(getPrivateKeyId(), keystorePwd.toCharArray());
                    } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public String getPrivateKeyId() {
                    return alias;
                }
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public KeyStore getKeyStore(String keystorePwd) {
        String keystorePath = System.getProperty("KEYSTORE_PATH");
        Preconditions.checkArgument(keystorePath != null, "Please set KEYSTORE_PATH system property");
        try (InputStream in = new FileInputStream(keystorePath)) {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(in, keystorePwd.toCharArray());

            return keyStore;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
