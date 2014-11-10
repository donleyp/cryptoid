package cryptoid;

public interface SimpleCipher {
	CipherTextEnvelope encrypt(String clearText, String authenticatedData);
	String decrypt(CipherTextEnvelope cipherText);
}
