package cryptoid;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.SecureRandomFactoryBean;
import org.springframework.stereotype.Service;

@Service
public class SimpleCipherImpl implements SimpleCipher {
	private static final int IV_SIZE = 12;

	public static final String version = "1.0";
	
	@Autowired
	private KeySource keySource;
	@Autowired
	private SecureRandomFactoryBean secureRandomFactoryBean;
	
	@Override
	public CipherTextEnvelope encrypt(String clearText, String authenticatedData) {
		final byte[] clearTextBytes = clearText.getBytes();
		final byte[] adBytes = authenticatedData.getBytes();
		final byte[] iv = generateIV();
		final Key key = this.keySource.getCurrentKey();
		
		byte[] cipherTextBytes;
		try {
			cipherTextBytes = _encrypt(clearTextBytes, adBytes, iv, key);
		} catch (Exception e) {
			throw new RuntimeException("encryption failed.", e);
		}
		return new CipherTextEnvelope(version, this.keySource.getCurrentKey().getName(), iv, authenticatedData, cipherTextBytes);
	}

	@Override
	public String decrypt(CipherTextEnvelope cipherText) {
		final byte[] cipherTextBytes = cipherText.getCipherText();
		final byte[] adBytes = cipherText.getAuthenticatedData().getBytes();
		final byte[] iv = cipherText.getInitializationVector();
		final Key key = this.keySource.getNamedKey(cipherText.getKeyName());
		
		byte[] clearTextBytes;
		try {
			clearTextBytes = _decrypt(cipherTextBytes, adBytes, iv, key);
		} catch (Exception e) {
			throw new RuntimeException("decryption failed.", e);
		}
		return new String(clearTextBytes);
	}

	private SecureRandom getSecureRandom() {
		SecureRandom secureRandom = null;
		try {
			secureRandom = this.secureRandomFactoryBean.getObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return secureRandom;
	}
	
	private byte[] generateIV() {
		SecureRandom secureRandom = getSecureRandom();
		
		final byte[] iv = new byte[IV_SIZE];
		secureRandom.nextBytes(iv);
		return iv;
	}

	private byte[] _encrypt(final byte[] clearTextBytes, final byte[] adBytes, final byte[] iv, final Key key)
		throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		byte cipherTextBytes[];
		Cipher cipher = initializeCipher(Cipher.ENCRYPT_MODE, iv, key);
		cipher.updateAAD(adBytes);
		cipherTextBytes = cipher.doFinal(clearTextBytes);
		return cipherTextBytes;
	}
	
	private byte[] _decrypt(byte[] cipherTextBytes, byte[] adBytes, byte[] iv, Key key) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		byte clearTextBytes[] = null;
		Cipher cipher = initializeCipher(Cipher.DECRYPT_MODE, iv, key);
		cipher.updateAAD(adBytes);
		clearTextBytes = cipher.doFinal(cipherTextBytes);
		return clearTextBytes;
	}

	private Cipher initializeCipher(int mode, byte[] iv, Key key)
			throws InvalidKeyException, InvalidAlgorithmParameterException {
		try {
			SecretKeySpec secretKeySpec = new SecretKeySpec(key.getKey(), "AES");
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			cipher.init(mode, secretKeySpec, ivParameterSpec);
			return cipher;
		} catch (NoSuchAlgorithmException e) {
			throw new Error("Your installation doesn't have AES/GCM?!? Fix that please.", e);
		} catch (NoSuchPaddingException e) {
			throw new Error("Your installation doesn't support NoPadding with AES/GCM. Fix it.", e);
		}
	}

}
