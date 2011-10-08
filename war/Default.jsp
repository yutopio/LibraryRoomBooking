<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="net.yutopio.library.booking.server.CalendarOperator" %>

<%
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
	String loginUrl = user != null ? null :
		userService.createLoginURL(request.getRequestURI());
%>

<!doctype html>
<html>
	<head>
		<link rel="shortcut icon" href="http://www.google.com/images/icons/product/forms-16.png" type="image/x-icon" />
		<meta http-equiv="Content-type" content="text/html; charset=utf-8" />
		<title>グループ学習室の予約</title>
		<link href='Default.css' type='text/css' rel='stylesheet' />
		<script type="text/javascript" language="javascript" src="booking/booking.nocache.js"></script>
	</head>
	<body class="ss-base-body">
		<div class="ss-form-container" style="background: white url(http://code.google.com/appengine/images/appengine-noborder-120x30.gif) no-repeat; background-position: right bottom">
			<div>
				<h1 class="ss-form-title">グループ学習室の予約</h1>
					<p class="ss-form-desc" style="padding-top: 15px;">
						このページから、<a href="http://park.itc.u-tokyo.ac.jp/cllib/eng2.html" target="_blank">東京大学 工学部2号館図書室</a>内のグループ学習室の予約を行うことができます。
						<a href="http://park.itc.u-tokyo.ac.jp/cllib/eng2.html#group" target="_blank">利用条件</a>を確認したうえで、グループ代表者が以下に利用申し込み内容を記入して送信してください。
						図書室スタッフが内容を確認したのち、グループ代表者のメール アドレスに利用の可否をお知らせします。</p>
<%	if (user == null) { %>
				<hr />
				<p class="ss-form-desc">Google アカウントで<a href="<%= loginUrl %>">ログイン</a>すると、申し込み時の連絡先に Google アカウントのアドレスが利用できます。</p>
<%	} %>
			</div>
			<div class="ss-form">
			<form action="/" method="POST" id="ss-form">
				<br />
				<div class="errorbox-good">
					<div class="ss-form-entry">
						<h2 class="ss-q-title">グループ代表者について</h2>
						<p class="ss-q-help">
							代表者は原則として、工学部、工学系研究科、情報理工学系研究科のいずれかに所属している方でなければなりません。
							メール アドレスについては、末尾が u-tokyo.ac.jp で終わる学内ドメインのものを入力するか、
<%	if (user == null) { %>
							Google アカウントをお持ちの方は<a href="<%= loginUrl %>">こちら</a>からログインしてください。
<%	} else { %>
							Google アカウントのアドレスを選択してください。
<%	} %>
						</p>
						<table border="0" cellpadding="5" cellspacing="0">
							<tbody>
								<tr class="ss-gridrow ss-grid-row-odd ss-grid-row-top">
									<th align="right" style="width: 120px">
										<label class="ss-q-help" for="Name">氏名</label>
									</th>
									<td style="width: 15px" />
									<td align="left" style="width: 200px">
										<input type="text" name="name" size="15" maxlength="50" id="Name" />
									</td>
									<td style="width: 15px" />
								</tr>
								<tr class="ss-gridrow ss-grid-row-even">
									<th style="text-align: right; width: 120px">
										<label class="ss-q-help" for="Department">所属</label>
									</th>
									<td />
									<td align="left">
										<input type="text" name="department" size="30" maxlength="30" id="Department" />
									</td>
									<td />
								</tr>
								<tr class="ss-gridrow ss-grid-row-odd">
									<th style="text-align: right; width: 120px">
										<label class="ss-q-help" for="MailAddress">メール アドレス</label>
									</th>
									<td />
									<td align="left">
<%	if (user != null) { %>
										<table>
											<tbody>
												<tr>
													<td><input type="radio" name="mailtype" value="1" class="ss-q-radio" id="MailType1" /></td>
													<td><label for="MailType1"><%= user.getEmail() %></label></td>
												</tr>
												<tr>
													<td><input type="radio" name="login" value="0" class="ss-q-radio" id="MailType2" /></td>
													<td><label for="MailType2"><input type="text" name="mail" size="25" maxlength="100" id="MailAddress" /></label></td>
												</tr>
											</tbody>
										</table>
<%	} else { %>
										<input type="hidden" name="mailtype" value="0" />
										<input type="text" name="mail" size="30" maxlength="100" id="MailAddress" />
<%	} %>
									<td />
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<br>
				<div class="errorbox-good">
					<div class="ss-form-entry">
						<h2 class="ss-q-title">希望日時について</h2>
						<p class="ss-q-help">
							原則として、翌日から 2 週間以内までの日時での予約申し込みができます。
							また、予約できる枠の長さは最大で 1 日あたり 3 時間までとなります。
							現在の空き状況は<a href="<%= CalendarOperator.PublicViewAddress %>" target="_blank">こちら</a>から確認できます。</p>
						<input type="text" name="date" value="2011/07/01" maxlength="10" size="10" class="ss-q-short" id="date" />
						<input type="text" name="start" value="10:00am" maxlength="7" size="7" class="ss-q-short" id="start" />
						to
						<input type="text" name="end" value="12:00pm" maxlength="7" size="7" class="ss-q-short" id="end" /></div>
				</div>
				<br>
				<div class="errorbox-good">
					<div class="ss-form-entry">
						<h2 class="ss-q-title">利用内容について</h2>
						<p class="ss-q-help">予約をするには、グループは少なくとも 3 人以上でなければなりません。</p>
						<table border="0" cellpadding="5" cellspacing="0">
							<tr class="ss-gridrow">
								<td>
								</td>
								<td>
									<label for="size1">3 - 5</label>
								</td>
								<td>
									<label for="size2">6 - 9</label>
								</td>
								<td>
									<label for="size3">10 - 12</label>
								</td>
								<td>
									<label for="size4">13+</label>
								</td>
							</tr>
							<tr class="ss-gridrow ss-grid-row-even ss-grid-row-top">
								<th align="right" style="width: 120px">
									予定人数
								</th>
								<td style="width: 60px">
									<input type="radio" name="size" value="3" class="ss-q-radio" id="size1" />
								</td>
								<td style="width: 60px">
									<input type="radio" name="size" value="6" class="ss-q-radio" id="size2" />
								</td>
								<td style="width: 60px">
									<input type="radio" name="size" value="10" class="ss-q-radio" id="size3" />
								</td>
								<td style="width: 60px">
									<input type="radio" name="size" value="13" class="ss-q-radio" id="size4" />
								</td>
							</tr>
						</table>
						<p class="ss-q-help">
							一般の方も参加できるようなイベントで利用する場合、
							公開イベントとして利用を申し込むことができます。</p>
						<p id="publicEvent">
							<label id="publicEventCheckLabel"><input id="publicEventCheck" name="pub" type="checkbox" class="ss-q-checkbox" /> 公開イベントにする</label>
							<table id="publicEventTable" border="0" cellpadding="5" cellspacing="0">
								<tbody>
									<tr class="ss-gridrow ss-grid-row-odd ss-grid-row-top">
										<th align="right" style="width: 120px">
											<label class="ss-q-help" for="EventTitle">イベント名</label>
										</th>
										<td style="width: 15px" />
										<td align="left" style="width: 200px">
											<input type="text" name="evttl" size="15" maxlength="30" id="EventTitle" />
										</td>
										<td style="width: 15px" />
									</tr>
									<tr class="ss-gridrow ss-grid-row-even">
										<th style="text-align: right; width: 120px">
											<label class="ss-q-help" for="Department">説明</label>
										</th>
										<td />
										<td align="left">
											<textarea cols="50" name="evdesc" rows="5" maxlength="500" id="EventDescription">
											</textarea>
										</td>
										<td />
									</tr>
								</tbody>
							</table>
						</p>
					</div>
				</div>
				<br />
				<div class="errorbox-good">
					<div class="ss-form-entry">
						<h2 class="ss-q-title">貸し出しを希望する備品</h2>
						<p class="ss-q-help"></p>
						<ul class="ss-choices">
							<li class="ss-choice-item"><label><input type="checkbox" name="proj" class="ss-q-checkbox" /> プロジェクタ</label></li>
						</ul>
					</div>
				</div>
				<br />
				<div class="errorbox-good">
					<div class="ss-form-entry">
						<h2 class="ss-q-title" for="entry_5">その他</h2>
						<p class="ss-q-help" for="entry_5">申し込み内容について図書室スタッフにお知らせしたいことがあれば、ご記入ください。</p>
						<textarea name="note" rows="5" cols="70"></textarea>
					</div>
				</div>
				<br />
				<div class="errorbox-good">
					<div class="ss-form-entry">
						<h2 class="ss-q-title" for="entry_5">送信</h2>
						<p class="ss-q-help" for="entry_5">
							もう一度お申し込み内容を確認して、送信してください。
							午前中にいただいたお申し込みは当日中、それ以降にいただいたお申し込みは翌日を目安として、
							スタッフからご利用の可否についてお知らせいたします。</p>
						<label><input type="checkbox" name="copy" class="ss-q-checkbox" /> 申し込み控えのメールを希望します</label><br />
						<input type="submit" name="submit" />
					</div>
				</div>
				</form>
			</div>
		</div>
	</body>
</html>

