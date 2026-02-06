package ru.dorofeev.unit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import static org.testcontainers.shaded.org.bouncycastle.pqc.legacy.math.linearalgebra.ByteUtils.xor;

@Slf4j
class XOREncryptedTest extends BaseUnitTest {

    @DisplayName("XOR: Пример шифрования токена через XOR-операцию")
    @Test
    void encrypted() {
        final String token = "415ed3a1-7f96-406f-80d1-cc0ab81df420";

        byte[] tokenBytes = token.getBytes(StandardCharsets.UTF_8);
        int tokenSize = tokenBytes.length;

        byte[] randomBytes = new byte[tokenSize];
        new SecureRandom().nextBytes(randomBytes);

        //XOR-шифрование.
        byte[] xoredBytes = xor(tokenBytes, randomBytes);

        //Объединяем random + xored
        byte[] combined = new byte[tokenSize * 2];
        System.arraycopy(randomBytes, 0, combined, 0, tokenSize);
        System.arraycopy(xoredBytes, 0, combined, tokenSize, tokenSize);

        //Base64 URL-safe кодирование.
        String encryptedToken = Base64.getUrlEncoder().withoutPadding().encodeToString(combined);
        Assertions.assertEquals(token, token);
        log.info("""
                        \n
                        AFTER ENCRYPTED: {}
                        BEFORE ENCRYPTED: {}
                        """,
                token,
                encryptedToken
        );
    }
}
