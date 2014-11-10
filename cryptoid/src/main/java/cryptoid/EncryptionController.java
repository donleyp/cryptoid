package cryptoid;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EncryptionController {
	
	@Autowired
	private SimpleCipher simpleCipher;
	
	@RequestMapping(value="/encrypt", consumes="application/json", produces="application/json")
	public @ResponseBody CipherTextEnvelope encrypt(@RequestBody Map<String, String> clearTextData) {
		String clearText = clearTextData.get("clearText");
		String authenticatedData = clearTextData.get("authenticatedData");
		return this.simpleCipher.encrypt(clearText, authenticatedData);
	}
	
	@RequestMapping(value="/decrypt", consumes="application/json", produces="application/json")
	public @ResponseBody Map<String,String> decrypt(@RequestBody CipherTextEnvelope cipherText) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("clearText", this.simpleCipher.decrypt(cipherText));
		map.put("authenticatedData", cipherText.getAuthenticatedData());
		return map;
	}
}
