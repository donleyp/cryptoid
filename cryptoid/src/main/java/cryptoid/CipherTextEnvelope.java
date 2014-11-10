package cryptoid;

public class CipherTextEnvelope {
	private String envelopeVersion;
	private String keyName;
	private byte[] initializationVector;
	private String authenticatedData;
	private byte[] cipherText;

	public CipherTextEnvelope() {

	}

	public CipherTextEnvelope(String envelopeVersion, String keyId,
			byte[] initializationVector, String authenticatedData,
			byte[] cipherText) {
		super();
		this.envelopeVersion = envelopeVersion;
		this.keyName = keyId;
		this.initializationVector = initializationVector;
		this.authenticatedData = authenticatedData;
		this.cipherText = cipherText;
	}

	public String getEnvelopeVersion() {
		return envelopeVersion;
	}

	public void setEnvelopeVersion(String envelopeVersion) {
		this.envelopeVersion = envelopeVersion;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyId) {
		this.keyName = keyId;
	}

	public byte[] getInitializationVector() {
		return initializationVector;
	}

	public void setInitializationVector(byte[] initializationVector) {
		this.initializationVector = initializationVector;
	}

	public String getAuthenticatedData() {
		return authenticatedData;
	}

	public void setAuthenticatedData(String authenticatedData) {
		this.authenticatedData = authenticatedData;
	}

	public byte[] getCipherText() {
		return cipherText;
	}

	public void setCipherText(byte[] cipherText) {
		this.cipherText = cipherText;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((authenticatedData == null) ? 0 : authenticatedData
						.hashCode());
		result = prime * result
				+ ((cipherText == null) ? 0 : cipherText.hashCode());
		result = prime
				* result
				+ ((initializationVector == null) ? 0 : initializationVector
						.hashCode());
		result = prime * result + ((keyName == null) ? 0 : keyName.hashCode());
		result = prime * result
				+ ((envelopeVersion == null) ? 0 : envelopeVersion.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CipherTextEnvelope other = (CipherTextEnvelope) obj;
		if (authenticatedData == null) {
			if (other.authenticatedData != null)
				return false;
		} else if (!authenticatedData.equals(other.authenticatedData))
			return false;
		if (cipherText == null) {
			if (other.cipherText != null)
				return false;
		} else if (!cipherText.equals(other.cipherText))
			return false;
		if (initializationVector == null) {
			if (other.initializationVector != null)
				return false;
		} else if (!initializationVector.equals(other.initializationVector))
			return false;
		if (keyName == null) {
			if (other.keyName != null)
				return false;
		} else if (!keyName.equals(other.keyName))
			return false;
		if (envelopeVersion == null) {
			if (other.envelopeVersion != null)
				return false;
		} else if (!envelopeVersion.equals(other.envelopeVersion))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CipherText [envelopeVersion=" + envelopeVersion + ", keyId="
				+ keyName + ", initializationVector=" + initializationVector
				+ ", authenticatedData=" + authenticatedData + ", cipherText="
				+ cipherText + "]";
	}
}
