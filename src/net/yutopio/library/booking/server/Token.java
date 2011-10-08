package net.yutopio.library.booking.server;

import java.security.SecureRandom;
import java.util.Formatter;

public class Token {
	public String ID;
	public byte[] Key;

	public Token() {
		ID = ToHex(SecureRandom.getSeed(4));
		Key = SecureRandom.getSeed(20);
	}

	public Token(String id, byte[] key) {
		ID = id;
		Key = key;
	}

	@Override
	public String toString() {
		return ID + ToHex(Key);
	}

	public static Token Parse(String representation) {
		return new Token(representation.substring(0, 8),
				FromHex(representation.substring(8)));
	}

	static byte[] FromHex(String hex) {
		byte[] ret = new byte[hex.length() / 2];
		for (int i = 0, j = 0; j < hex.length() / 2; i++, j += 2)
			ret[i] = Byte.decode("0x" + hex.substring(j, j + 2));
		return ret;
	}

	static String ToHex(byte[] value) {
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb);
		for (int i = 0; i < value.length; i++)
			formatter.format("%1$02X", value[i]);
		return sb.toString();
	}
}

