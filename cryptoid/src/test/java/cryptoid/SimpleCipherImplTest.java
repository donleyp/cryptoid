package cryptoid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.SecureRandom;
import java.security.Security;
import java.util.Random;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.security.core.token.SecureRandomFactoryBean;
import org.springframework.test.util.ReflectionTestUtils;

import cryptoid.CipherTextEnvelope;
import cryptoid.Key;
import cryptoid.KeySource;
import cryptoid.SimpleCipherImpl;


public class SimpleCipherImplTest {
	
	private static final String TEST_AD_1 = "TEST";
	private static final String TEST_DATA_1 = "THIS IS A TEST";
	private static final int KEY_SIZE = 128/8;
	private static final int IV_SIZE = 96/8;
	
	SimpleCipherImpl simpleCipherImpl = new SimpleCipherImpl();
	Key currentKey = null;
	Key namedKey = null;
	
	@BeforeClass
	public static void initTests() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

	@Before
	public void setUp() throws Exception {
		KeySource mockKeySource = mock(KeySource.class);
		SecureRandomFactoryBean mockSecureRandomFactoryBean = mock(SecureRandomFactoryBean.class);
		ReflectionTestUtils.setField(this.simpleCipherImpl, "keySource", mockKeySource);
		ReflectionTestUtils.setField(this.simpleCipherImpl, "secureRandomFactoryBean", mockSecureRandomFactoryBean);
		
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		when(mockSecureRandomFactoryBean.getObject()).thenReturn(secureRandom);
		
		byte key[] = new byte[KEY_SIZE]; 
		secureRandom.nextBytes(key);
		this.currentKey = new Key("current", key);
		
		key = new byte[KEY_SIZE]; 
		secureRandom.nextBytes(key);
		this.namedKey = new Key("named", key);
		
		when(mockKeySource.getCurrentKey()).thenReturn(this.currentKey);
		when(mockKeySource.getNamedKey("named")).thenReturn(this.namedKey);
		when(mockKeySource.getNamedKey("current")).thenReturn(this.currentKey);
	}

	@Test
	public void testEncrypt() {
		CipherTextEnvelope cipherText = encryptTest1();
		assertEquals(this.currentKey.getName(), cipherText.getKeyName());
		assertEquals(ReflectionTestUtils.getField(this.simpleCipherImpl, "version"), cipherText.getEnvelopeVersion());
		assertEquals(TEST_AD_1, cipherText.getAuthenticatedData());
		assertEquals(IV_SIZE, cipherText.getInitializationVector().length);
	}

	@Test
	public void testDecrypt() {
		CipherTextEnvelope cipherText = encryptTest1();
		String clearText = this.simpleCipherImpl.decrypt(cipherText);
		assertEquals(TEST_DATA_1, clearText);
	}
	
	@Test
	public void testTamperWithAAD() {
		CipherTextEnvelope cipherText = encryptTest1();
		cipherText.setAuthenticatedData("BADAAD");
		try {
			this.simpleCipherImpl.decrypt(cipherText);
			fail("decryption should have failed.");
		}
		catch (Exception e) {
			// It's cool.
		}
	}

	@Test
	public void testTamperWithData() {
		CipherTextEnvelope cipherText = encryptTest1();
		byte[] badData = new byte [TEST_DATA_1.length()];
		new Random().nextBytes(badData);
		cipherText.setCipherText(badData);
		try {
			this.simpleCipherImpl.decrypt(cipherText);
			fail("decryption should have failed.");
		}
		catch (Exception e) {
			// It's cool.
		}
	}
	
	private CipherTextEnvelope encryptTest1() {
		CipherTextEnvelope cipherText = this.simpleCipherImpl.encrypt(TEST_DATA_1, TEST_AD_1);
		return cipherText;
	}
}
