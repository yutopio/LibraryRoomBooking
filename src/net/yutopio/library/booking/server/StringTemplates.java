package net.yutopio.library.booking.server;

import java.util.Formatter;

import net.yutopio.library.booking.shared.ApplicationDetail;

@SuppressWarnings("deprecation")
public class StringTemplates {

	// TODO: Specify the application root URI.
	public static final String applicationRoot = "https://********.appspot.com/";

	public static String[] MailOrganizersCopy(ApplicationDetail detail) {
		final String subject = "予約申し込みを受け付けました";

		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb);
		formatter.format(
				"%1$s\n" +
				"%2$s さま\n" +
				"\n" +
				"東京大学 工学部2号館 図書室です。\n" +
				"%3$d 月 %4$d 日 %5$d 時 %6$d 分から %7$d 時 %8$d 分まで、\n" +
				"図書室内のグループ学習室の利用申し込みを受け付けました。\n" +
				"スタッフが申し込み内容を確認し次第、可否を返答いたします。\n" +
				"ご利用ありがとうございました。\n",
				detail.OrganizerDepartment, detail.OrganizerName,
				detail.Start.getMonth(), detail.Start.getDay(),
				detail.Start.getHours(), detail.Start.getMinutes(),
				detail.End.getHours(), detail.End.getMinutes());
		sb.append(Signature());

		return new String[] { subject, sb.toString() };
	}

	public static String[] MailAdministratorNotification(ApplicationDetail detail, Token token) {
		final String subject = "新着の予約申し込みを受け付けました";

		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb);
		formatter.format(
				"%1$s の %2$s さんから、\n" +
				"%3$d 月 %4$d 日 %5$d 時 %6$d 分から %7$d 時 %8$d 分まで、\n" +
				"グループ学習室の利用申し込みを受け付けました。\n" +
				"\n" +
				"次の URL から申し込み内容を確認し、\n" +
				"承認もしくは却下の処理を行ってください。\n" +
				"\n" +
				"%9sconfirm.jsp?\n",
				detail.OrganizerDepartment, detail.OrganizerName,
				detail.Start.getMonth(), detail.Start.getDay(),
				detail.Start.getHours(), detail.Start.getMinutes(),
				detail.End.getHours(), detail.End.getMinutes(),
				applicationRoot);

		return new String[] { subject, sb.toString() };
	}

	public static String[] MailAccept(ApplicationDetail detail) {
		final String subject = "予約申し込みが承認されました";

		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb);
		formatter.format(
				"%1$s\n" +
				"%2$s さま\n" +
				"\n" +
				"先日お申し込みいただいたグループ学習室の利用予約につきまして、\n" +
				"%3$d 月 %4$d 日 %5$d 時 %6$d 分から %7$d 時 %8$d 分まで、\n" +
				"グループ学習室の利用を許可しますので、お知らせします。\n" +
				"ご利用にあたっては今一度、次のページから利用条件をご確認ください。\n" +
				"http://park.itc.u-tokyo.ac.jp/cllib/eng2.html#group\n" +
				"\n" +
				"ご不明な点があれば、図書室までお問い合わせください。\n",
				detail.OrganizerDepartment, detail.OrganizerName,
				detail.Start.getMonth(), detail.Start.getDay(),
				detail.Start.getHours(), detail.Start.getMinutes(),
				detail.End.getHours(), detail.End.getMinutes());
		sb.append(Signature());

		return new String[] { subject, sb.toString() };
	}

	public static String[] MailReject(ApplicationDetail detail) {
		final String subject = "予約申し込み";

		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb);
		formatter.format(
				"%1$s\n" +
				"%2$s さま\n" +
				"\n" +
				"先日お申し込みいただいたグループ学習室の利用予約につきまして、\n" +
				"スタッフが検討いたしました結果、まことに申し訳ございませんが、\n" +
				"こちらの都合により、お受けできないこととなりました。\n" +
				"詳細につきましては、図書室までお問い合わせください。\n",
				detail.OrganizerDepartment, detail.OrganizerName);
		sb.append(Signature());

		return new String[] { subject, sb.toString() };
	}

	public static String Signature() {
		return "\n" +
			"東京大学　工学部2号館 図書室 (5階52A)\n" +
			"03-5841-6315 (内線 26315)\n" +
			"http://park.itc.u-tokyo.ac.jp/cllib/eng2.html\n" +
			"mailto:kogaku2＠lib.u-tokyo.ac.jp";
	}

 	public static String PrivateEventDescription(ApplicationDetail detail) {
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb);

		sb.append("<dl>");
		formatter.format("<dt>Organizer</dt><dd>%s</dd>", Util.HtmlEncode(detail.OrganizerName));
		formatter.format("<dt>Department</dt><dd>%s</dd>", Util.HtmlEncode(detail.OrganizerDepartment));
		formatter.format("<dt>Contact</dt><dd>%s</dd>", Util.HtmlEncode(detail.OrganizerContact));

		sb.append("<dt>Equipment</dt><dd>");
		if (detail.Equipment == null || detail.Equipment.length == 0)
			sb.append("No");
		else {
			boolean separator = false;
			for (String element : detail.Equipment) {
				if (separator) sb.append(", ");
				separator = true;
				sb.append(element);
			}
			return sb.toString();
		}
		sb.append("</dd>");

		// Note doesn't need to be encoded.
		if (detail.Note != null && !detail.Note.isEmpty())
			formatter.format("<dt>Note</dt><dd>%s</dd>", detail.Note);

		return sb.toString();
	}
}

