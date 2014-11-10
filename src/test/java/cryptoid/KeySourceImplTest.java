package cryptoid;

import static org.junit.Assert.assertEquals;

import java.security.SecureRandom;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import cryptoid.Key;
import cryptoid.KeySourceImpl;

public class KeySourceImplTest {
	
	KeySourceImpl keySourceImpl;
	
	@Before
	public void setupBeans() {
		this.keySourceImpl = new KeySourceImpl();
		this.keySourceImpl.loadKeys();
	}

	@Test
	public void testGetCurrentKey() {
		assertEquals("number-0", this.keySourceImpl.getCurrentKey().getName());
	}

	@Test
	public void testGetNamedKey() {
		assertEquals("number-1", this.keySourceImpl.getNamedKey("number-1").getName());
	}
	
//	@Test
	public void generateTestKeys() {
		Key keys[] = new Key[3];
		for (int i = 0; i < keys.length; i++) {
			byte[] key = new byte[128/8];
			try {
				SecureRandom.getInstance("SHA1PRNG").nextBytes(key);
			} catch (Exception e) {
				e.printStackTrace();
			}
			keys[i] = new Key("number-" + i, key );
		}
		ObjectMapper objectMapper = new ObjectMapper();
		final ObjectWriter w = objectMapper.writer();
		// enable one feature, disable another
		String json;
		try {
			json = w
			  .with(SerializationFeature.INDENT_OUTPUT)
			  .without(SerializationFeature.FAIL_ON_EMPTY_BEANS, SerializationFeature.WRAP_EXCEPTIONS)
			  .writeValueAsString(keys);
			System.out.println(json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}
