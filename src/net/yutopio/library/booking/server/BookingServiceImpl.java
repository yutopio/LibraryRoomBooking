package net.yutopio.library.booking.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gdata.util.ServiceException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import net.yutopio.library.booking.shared.ApplicationDetail;
import net.yutopio.library.booking.shared.BookingServiceException;
import net.yutopio.library.booking.client.BookingService;

@SuppressWarnings("serial")
public class BookingServiceImpl extends RemoteServiceServlet implements BookingService {

	// TODO: Insert administrator information here.
	static final String Administrator = "ko2 yakan";
	static final String AdministratorContact = "Admin@gmail.com";

	public boolean CheckAvailability(Date start, Date end) throws BookingServiceException {
		try {
			return CalendarOperator.CheckAvailability(start, end);
		} catch (IOException e) {
			throw new BookingServiceException();
		} catch (ServiceException e) {
			throw new BookingServiceException();
		}
	}

	public void Submit(ApplicationDetail data)
			throws BookingServiceException {
		if (!data.Validate()) throw new SecurityException();

		// We do some additional validation to the user input data.

		// The address should be the one inside u-tokyo.ac.jp domain or
		// the one of currently logged-in Google account.
		final String UniversityDomain = "u-tokyo.ac.jp";
		String loweredContact = data.OrganizerContact.toLowerCase();
		if (GetLoggedInAddress() != loweredContact &&
				!loweredContact.endsWith(UniversityDomain))
			throw new SecurityException();

		// Encode user-input string in HTML.
		data.OrganizerName = Util.RemoveNewLine(data.OrganizerName);
		data.OrganizerDepartment = Util.RemoveNewLine(data.OrganizerDepartment);
		data.Note = Util.HtmlEncode(data.Note);
		data.EventTitle = Util.RemoveNewLine(data.EventTitle);
		data.EventDescription = Util.HtmlEncode(data.EventDescription);

		// Add to datastore.
		Token token = DatastoreOperator.Add(data);

		// Send a notification to the administrator.
		try {
			Message msg = CreateNewMessage();
			msg.addRecipient(Message.RecipientType.TO,
				new InternetAddress(AdministratorContact, Administrator));
			String[] text = StringTemplates.MailAdministratorNotification(data, token);
			msg.setSubject(text[0]);
			msg.setText(text[1]);
			Transport.send(msg);

			// If the organizer requested a copy, send one.
			if (data.OrganizerNeedsCopy) {
				msg = CreateNewMessage();
				msg.addRecipient(Message.RecipientType.TO,
					new InternetAddress(data.OrganizerContact, data.OrganizerName));
				text = StringTemplates.MailOrganizersCopy(data);
				msg.setSubject(text[0]);
				msg.setText(text[1]);
				Transport.send(msg);
			}
		} catch (UnsupportedEncodingException e) {
			throw new BookingServiceException();
		} catch (MessagingException e) {
			throw new BookingServiceException();
		}
	}

	public ApplicationDetail Restore(String tokenString) {
		if (!IsAdministrator()) throw new SecurityException();

		Token token = Token.Parse(tokenString);
		return DatastoreOperator.Lookup(token);
	}

	public void Accept(String tokenString, ApplicationDetail detail, String oauthToken)
			throws BookingServiceException {
		if (!IsAdministrator()) throw new SecurityException();

		// Remove the specified application record.
		Token token = Token.Parse(tokenString);
		DatastoreOperator.Delete(token);

		// Accept the application given as a parameter.
		try {
			CalendarOperator.CreateCalendarEntry(detail, oauthToken);
		} catch (IOException e) {
			throw new BookingServiceException();
		} catch (ServiceException e) {
			throw new BookingServiceException();
		}

		// Send an email to the organizer.
		// TODO:
	}

	public void Reject(String tokenString) {
		if (!IsAdministrator()) throw new SecurityException();

		// Remove the specified application record.
		Token token = Token.Parse(tokenString);
		DatastoreOperator.Delete(token);

		// Send an email to the organizer.
		// TODO:
	}

	String GetLoggedInAddress() {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		return user == null ? "" : user.getEmail().toLowerCase();
	}

	boolean IsAdministrator() {
		return GetLoggedInAddress() == AdministratorContact.toLowerCase();
	}

	Message CreateNewMessage() throws UnsupportedEncodingException, MessagingException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(AdministratorContact, Administrator));
		return msg;
	}
}

