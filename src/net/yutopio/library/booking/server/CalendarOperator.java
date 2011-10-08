package net.yutopio.library.booking.server;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.HtmlTextConstruct;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.extensions.When;
import com.google.gdata.data.extensions.BaseEventEntry.Transparency;
import com.google.gdata.data.extensions.BaseEventEntry.Visibility;
import com.google.gdata.util.ServiceException;

import net.yutopio.library.booking.shared.ApplicationDetail;

public class CalendarOperator {

	// TODO: Insert calendar information here.
	public static final String ApplicationIdentifier = "UniversityOfTokyo-LibraryRoomBooking-1";
	public static final String CalendarOwner = "InsertCalendarIdHere@group.calendar.google.com";
	public static final String PublicFeedFormat = "https://www.google.com/calendar/feeds/" + CalendarOwner + "/public/full";
	public static final String PrivateFeedFormat = "https://www.google.com/calendar/feeds/" + CalendarOwner + "/private/full?oauth_token=%s";
	public static final String PublicViewAddress = "https://www.google.com/calendar/embed?src=" + CalendarOwner;

	public static boolean CheckAvailability(Date start, Date end)
			throws IOException, ServiceException {

		CalendarService service = new CalendarService(ApplicationIdentifier);

		URL feedUrl = new URL(PublicFeedFormat);
		CalendarQuery query = new CalendarQuery(feedUrl);
		query.setMinimumStartTime(new DateTime(start));
		query.setMaximumStartTime(new DateTime(end));
		CalendarEventFeed resultFeed = service.query(query, CalendarEventFeed.class);
		return resultFeed.getTotalResults() == 0;
	}

	public static void CreateCalendarEntry(ApplicationDetail detail,
			String oauthToken) throws IOException, ServiceException {

		CalendarService service = new CalendarService(ApplicationIdentifier);
		URL queryURL = new URL(String.format(PrivateFeedFormat, oauthToken));

		When eventTimes = new When();
		eventTimes.setStartTime(new DateTime(detail.Start));
		eventTimes.setEndTime(new DateTime(detail.End));

		CalendarEventEntry entry = new CalendarEventEntry();
		entry.setTitle(new PlainTextConstruct(detail.OrganizerName));
		entry.setContent(new HtmlTextConstruct(StringTemplates.PrivateEventDescription(detail)));
		entry.setTransparency(detail.IsPublicEvent ? Transparency.TRANSPARENT : Transparency.OPAQUE);
		entry.setVisibility(Visibility.PRIVATE);
		entry.addTime(eventTimes);
		service.insert(queryURL, entry);

		if (detail.IsPublicEvent) {
			entry = new CalendarEventEntry();
			entry.setTitle(new PlainTextConstruct(detail.EventTitle));
			entry.setTitle(new HtmlTextConstruct(detail.EventDescription));
			entry.setTransparency(Transparency.OPAQUE);
			entry.setVisibility(Visibility.PUBLIC);
			entry.addTime(eventTimes);
			service.insert(queryURL, entry);
		}
	}
}

