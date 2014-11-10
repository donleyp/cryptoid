package cryptoid;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KeySourceImpl implements KeySource {
	private List<Key> keys = null;
	
	protected List<Key> getKeys() {
		return keys;
	}
	
	@PostConstruct
	public void loadKeys() {
		try {
			InputStream is = new ClassPathResource("keys.json").getInputStream();
			ObjectMapper objectMapper = new ObjectMapper();
			List<Key> _keys = objectMapper.readValue(is, new TypeReference<List<Key>>() { });
			this.keys = _keys;
		} catch (IOException e) {
			throw new Error("Unable to read keys!", e);
		}
	}
	
	public Key getCurrentKey() {
		try {
			return this.getKeys().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Key getNamedKey(String name) {
		for (Key key: this.getKeys()) {
			if (key.getName().equals(name)) {
				return key;
			}
		}
		return null;
	}
}
