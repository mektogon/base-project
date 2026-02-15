package unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.dorofeev.security.introspector.encrypted.provider.CryptoProvider;
import ru.dorofeev.security.session.section.CryptoSection;
import ru.dorofeev.security.session.service.SessionService;

import javax.crypto.spec.GCMParameterSpec;

class CryptoProviderTest extends BaseUnitTest {

    private static final String EXPECTED_SECRET_KEY = "ralnOdF9k3YYR8sjFxMIkoAQyzBeWCzH0haiqE10TJ8=";
    private static final String ENCRYPTED_VALUE = "7dB7DisWdlKUz6zGoaSXnx7h+d06vxFQgzSBnUZn9JSU25hHLrnJ";
    private static final String DECRYPTED_VALUE = "cryptoValue";

    @InjectMocks
    private CryptoProvider cryptoProvider;

    @Mock
    private SessionService sessionService;

    @Mock
    private CryptoSection cryptoSection;

    @DisplayName("[UNIT] Успешное шифрование значения")
    @Test
    void encrypt_shouldReturnEncryptValue() {
        Mockito.when(sessionService.getSectionAttribute(cryptoSection, CryptoSection.Attribute.SECRET_KEY)).thenReturn(EXPECTED_SECRET_KEY);
        Assertions.assertNotNull(cryptoProvider.encrypt(DECRYPTED_VALUE));
    }

    @DisplayName("[UNIT] Успешное дешифрование значения")
    @Test
    void decrypt_shouldReturnDecryptValue() {
        Mockito.when(sessionService.getSectionAttribute(cryptoSection, CryptoSection.Attribute.SECRET_KEY)).thenReturn(EXPECTED_SECRET_KEY);
        String decrypted = cryptoProvider.decrypt(ENCRYPTED_VALUE);
        Assertions.assertNotNull(decrypted);
        Assertions.assertEquals(DECRYPTED_VALUE, decrypted);
    }

    @DisplayName("[UNIT] Успешное получение секретного ключа пользователя из сессии")
    @Test
    void getCurrentSecretKey_shouldReturnSecretKeyFromUserSession() {
        Mockito.when(sessionService.getSectionAttribute(cryptoSection, CryptoSection.Attribute.SECRET_KEY)).thenReturn(EXPECTED_SECRET_KEY);
        Assertions.assertEquals(EXPECTED_SECRET_KEY, cryptoProvider.getCurrentSecretKey());
    }

    @DisplayName("[UNIT] Успешная генерация 32 байтного секретного ключа")
    @Test
    void generate32ByteSecretKey_shouldGenerate32BytesBitSecretKey() {
        Assertions.assertNotNull(CryptoProvider.generate32ByteSecretKey());
    }

    @DisplayName("[UNIT] Успешное получение алгоритма шифрования (AES)")
    @Test
    void getAlgorithm_shouldReturnAESAlgorithm() {
        Assertions.assertEquals("AES", CryptoProvider.getAlgorithm());
    }

    @DisplayName("[UNIT] Успешное получение трансформации шифрования (AES/GCM/NoPadding)")
    @Test
    void getAlgorithmTransformation() {
        Assertions.assertEquals("AES/GCM/NoPadding", CryptoProvider.getAlgorithmTransformation());
    }

    @DisplayName("[UNIT] Успешное контейнера параметров GCM")
    @Test
    void getGCMParameterSpec() {
        GCMParameterSpec gcmParameterSpec = CryptoProvider.getGCMParameterSpec(new byte[12]);
        Assertions.assertNotNull(gcmParameterSpec);
        Assertions.assertNotNull(gcmParameterSpec.getIV());
        Assertions.assertEquals(128, gcmParameterSpec.getTLen());
    }
}