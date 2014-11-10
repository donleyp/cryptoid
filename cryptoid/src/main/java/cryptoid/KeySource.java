package cryptoid;

public interface KeySource {
	Key getCurrentKey();
	Key getNamedKey(String name);
}
