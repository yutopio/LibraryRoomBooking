package net.yutopio.library.booking.server;

public class TextConverter {
	byte[] key;
	int index;
	byte check;

	public TextConverter(byte[] key) {
		this.key = key;
		this.index = 0;
		this.check = 0;
	}

	public byte[] Encode(String string) {
		byte[] ret = string.getBytes();
		for (int i = 0; i < ret.length; i++) {
			check ^= ret[i];
			ret[i] ^= key[index++];
			if (index == key.length) index = 0;
		}
		return ret;
	}

	public String Decode(byte[] bytes) {
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] ^= key[index++];
			if (index == key.length) index = 0;
			check ^= bytes[i];
		}
		return new String(bytes);
	}

	public byte GetCheckByte() {
		return check;
	}
}

