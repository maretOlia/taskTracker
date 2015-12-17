package giraffe.service.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import giraffe.security.GiraffeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Service
public class TokenAuthenticationService {

    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private final Mac hmac;

    private static final String AUTH_HEADER = "X-AUTH-TOKEN";

    private static final Base64.Decoder decoder = Base64.getDecoder();


    @Autowired
    public TokenAuthenticationService(@Value("${token.secret}") final String secret) {
        byte[] secretKey = DatatypeConverter.parseBase64Binary(secret);
        try {
            hmac = Mac.getInstance(HMAC_ALGORITHM);
            hmac.init(new SecretKeySpec(secretKey, HMAC_ALGORITHM));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException("Failed to initialize HMAC", e);
        }
    }

    public GiraffeUserDetails parseUserFromToken(final String token) {
        final String[] parts = token.split("\\.");
        if (parts.length == 2 && parts[0].length() > 0 && parts[1].length() > 0) {
            try {
                final byte[] userDetailsBytes = decoder.decode(parts[0]);
                final byte[] hashBytes = decoder.decode(parts[1]);

                // check if hash is valid
                if (Arrays.equals(createHmac(userDetailsBytes), hashBytes)) {
                    GiraffeUserDetails userDetails = new ObjectMapper().readValue(new ByteArrayInputStream(userDetailsBytes), GiraffeUserDetails.class);

                    if (userDetails.getExpires() > System.currentTimeMillis()) {
                        return userDetails;
                    }
                }
            } catch (IllegalArgumentException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private synchronized byte[] createHmac(byte[] content) {
        return hmac.doFinal(content);
    }

    public String createTokenForUser(final GiraffeUserDetails userDetails) {
        final Base64.Encoder encoder = Base64.getEncoder();
        byte[] userBytes = new byte[0];
        try {
            userBytes = new ObjectMapper().writeValueAsBytes(userDetails);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        byte[] hash = createHmac(userBytes);
        final StringBuilder sb = new StringBuilder();
        sb.append(encoder.encodeToString(userBytes));
        sb.append(".");
        sb.append(encoder.encodeToString(hash));
        return sb.toString();
    }

}
