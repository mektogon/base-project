package ru.dorofeev.security.introspector.encrypted.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;
import ru.dorofeev.security.authentication.exception.SecurityException;
import ru.dorofeev.security.authentication.exception.enums.ErrorType;
import ru.dorofeev.security.session.section.CryptoSection;
import ru.dorofeev.security.session.service.SessionService;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class CryptoProvider {

    private final SessionService sessionService;
    private final CryptoSection cryptoSection;

    /**
     * Метод шифрования.
     *
     * @param value шифруемое значение.
     * @return [IV (12 байт)] + [зашифрованные данные (N байт) + тег (16 байт)]
     */
    public String encrypt(String value) {

        if (StringUtils.isBlank(value)) {
            return value;
        }

        try {
            Cipher cipher = createCipher(Cipher.ENCRYPT_MODE, null);
            byte[] cipherIV = cipher.getIV();
            byte[] encrypted = cipher.doFinal(value.getBytes());

            byte[] combined = new byte[cipherIV.length + encrypted.length];
            System.arraycopy(cipherIV, 0, combined, 0, cipherIV.length);
            System.arraycopy(encrypted, 0, combined, cipherIV.length, encrypted.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            log.error("Ошибка при шифровании: ", ex);
            throw new SecurityException(ErrorType.ENCRYPT_ERROR);
        }
    }

    /**
     * Метод дешифрования.
     *
     * @param value шифрованное значение, представленное как [IV (12 байт)] + [зашифрованные данные (N байт) + тег (16 байт)].
     * @return дешифрованное значение.
     */
    public String decrypt(String value) {

        if (StringUtils.isBlank(value)) {
            return value;
        }

        try {
            byte[] combined = Base64.getDecoder().decode(value);

            byte[] cipherIV = new byte[12];
            byte[] encrypted = new byte[combined.length - 12];

            System.arraycopy(combined, 0, cipherIV, 0, 12);
            System.arraycopy(combined, 12, encrypted, 0, encrypted.length);

            byte[] decryptedBytes = createCipher(Cipher.DECRYPT_MODE, getGCMParameterSpec(cipherIV))
                    .doFinal(encrypted);

            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            log.error("Ошибка при дешифровании: ", ex);
            throw new SecurityException(ErrorType.DECRYPT_ERROR);
        }
    }

    /**
     * Метод получения секретного ключа для шифрования/дешифрования. <br/>
     * Пример: BnVmYiJkZWZnN2g4aTprbG0xMjN0dXZ3eHk5MDg3NjU0QkNERUY=
     */
    public @NotNull String getCurrentSecretKey() {
        return (String) sessionService.getSectionAttribute(cryptoSection, CryptoSection.Attribute.SECRET_KEY);
    }

    /**
     * Метод генерации случайного набора символов для шифрования/дешифрования размерностью 256 бит (32 байта). <br/>
     * Пример: BnVmYiJkZWZnN2g4aTprbG0xMjN0dXZ3eHk5MDg3NjU0QkNERUY=
     */
    public static @NotNull String generate32ByteSecretKey() {
        BytesKeyGenerator keyGenerator = KeyGenerators.secureRandom(32);
        byte[] key = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(key);
    }

    /**
     * Получить алгоритм шифрования Advanced Encryption Standard.
     *
     * @return симметричный алгоритм блочного шифрования.
     */
    public static @NotNull String getAlgorithm() {
        return "AES";
    }

    /**
     * Получить трансформацию шифрования (Java Cryptography Architecture) в формате: "алгоритм/режим/выравнивание" <br/>
     * <li/> AES (Advanced Encryption Standard) - Симметричный алгоритм блочного шифрования. (Ключ 128, 192 или 256 бит)
     * <li/> GCM (Galois/Counter Mode) - Режим работы блочного шифра, который обеспечивает конфиденциальность и аутентификацию.
     * <li/> NoPadding - Отсутствие добавления данных до полноты блока. AES блок = 16 байт (128 бит).
     */
    public static @NotNull String getAlgorithmTransformation() {
        return getAlgorithm() + "/GCM/NoPadding";
    }

    /**
     * Получить объект {@link Cipher}.
     *
     * @param mode режим работы. {@link Cipher#ENCRYPT_MODE} или {@link Cipher#DECRYPT_MODE}
     * @param spec параметры для режима {@link Cipher#DECRYPT_MODE}.
     * @return объект {@link Cipher}.
     */
    private Cipher createCipher(int mode, @Nullable GCMParameterSpec spec) {
        try {
            Cipher cipher = Cipher.getInstance(getAlgorithmTransformation());
            switch (mode) {
                case Cipher.ENCRYPT_MODE -> cipher.init(mode, getKey());
                case Cipher.DECRYPT_MODE -> cipher.init(mode, getKey(), spec);
            }

            return cipher;
        } catch (NoSuchAlgorithmException
                 | NoSuchPaddingException
                 | InvalidKeyException
                 | InvalidAlgorithmParameterException ex
        ) {
            log.error("Ошибка при создании '{}': ", Cipher.class, ex);
            throw new SecurityException(ErrorType.CREATE_CIPHER_ERROR);
        }
    }

    /**
     * Получить объект {@link SecretKey}.
     *
     * @return объект {@link SecretKey}.
     */
    private SecretKey getKey() {
        String currentSecretKey = getCurrentSecretKey();
        if (StringUtils.isBlank(currentSecretKey)) {
            throw new SecurityException(ErrorType.SECRET_KEY_IS_EMPTY);
        }

        return new SecretKeySpec(
                Base64.getDecoder().decode(currentSecretKey),
                getAlgorithm()
        );
    }

    /**
     * Получить обертку для IV и длины тега аутентификации.
     *
     * @param cipherIV Initialization Vector (Вектор инициализации).
     * @return объект {@link GCMParameterSpec}.
     */
    public static GCMParameterSpec getGCMParameterSpec(byte[] cipherIV) {
        return new GCMParameterSpec(128, cipherIV);
    }
}
