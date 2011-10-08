package net.yutopio.library.booking.server;

public class Util {
	public static String RemoveNewLine(String text) {
		return text == null ? "" : text.replace("\r", "").replace("\n", "");
	}

	public static String HtmlEncode(String text) {
		if (text == null) return "";

		final String before = "&\"<>\r\n";
		final String[] after = { "&amp;", "&quot;", "&lt;", "&gt;", "<br />", "<br />" };

		StringBuffer ret = new StringBuffer();
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if (ch == '\r' && (++i == text.length() || text.charAt(i) != '\n')) i--;

			int conv = before.indexOf(ch);
			if (conv == -1) ret.append(ch);
			else ret.append(after[conv]);
		}

		return ret.toString();
	}
}

