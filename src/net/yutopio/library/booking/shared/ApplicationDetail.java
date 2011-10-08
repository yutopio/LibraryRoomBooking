package net.yutopio.library.booking.shared;

import java.io.Serializable;
import java.util.Date;

public class ApplicationDetail implements Serializable {
	static final long serialVersionUID = 1L;

	public String OrganizerName;
	public String OrganizerDepartment;
	public String OrganizerContact;
	public boolean OrganizerNeedsCopy;

	public int GroupSize;

	public Date Start;
	public Date End;

	public String[] Equipment;
	public String Note;

	public boolean IsPublicEvent;
	public String EventTitle;
	public String EventDescription;

	public boolean Validate() {
		// Organizer name must be provided within 50 characters.
		final int OrganizerNameMaxLength = 50;
		if (OrganizerName == null || OrganizerName.isEmpty() ||
				OrganizerName.length() > OrganizerNameMaxLength) return false;

		// Organizer department must be provided within 30 characters.
		final int OrganizerDepartmentMaxLength = 30;
		if (OrganizerDepartment == null || OrganizerDepartment.isEmpty() ||
				OrganizerDepartment.length() > OrganizerDepartmentMaxLength) return false;

		// Contact address must be provided in RFC822 format.
		if (OrganizerContact == null || OrganizerContact.isEmpty()) return false;
		if (!ValidateMail(OrganizerContact)) return false;

		// The number of people should be between 3 and 20.
		final int GroupMinSize = 3, GroupMaxSize = 20;
		if (GroupSize < GroupMinSize || GroupSize > GroupMaxSize) return false;

		// Booking duration should be no longer than 3 hours.
		// Both start and end should be on the same day.
		// We don't care below minutes.
		final long MaxDuration = 1000 * 60 * 60 * 3;
		final long DayDuration = 1000 * 60 * 60 * 24;
		final long MinuteDuration = 1000 * 60;
		if (Start == null || End == null || !End.after(Start)) return false;
		Start.setTime(Start.getTime() - (Start.getTime() % MinuteDuration));
		End.setTime(End.getTime() - (End.getTime() % MinuteDuration));
		if ((Start.getTime() / DayDuration) != (End.getTime() / DayDuration) ||
				(End.getTime() - Start.getTime()) > MaxDuration) return false;

		// All names of equipment shouldn't have ',' character.
		if (Equipment != null)
			for (String elem : Equipment)
				if (elem.contains(",")) return false;

		// Note must not be longer than 1000 characters.
		final long MaxNote = 1000;
		if (Note != null && Note.length() > MaxNote) return false;

		// If it is the public event, event title must be provided.
		if (IsPublicEvent && (EventTitle == null || EventTitle.isEmpty())) return false;

		// If provided, the event title must be provided within 30 characters.
		final int EventTitleMaxLength = 30;
		if (EventTitle != null && !EventTitle.isEmpty() &&
				EventTitle.length() > EventTitleMaxLength) return false;

		// If provided, the event description should be provided within 500 characters.
		final int EventDescriptionMaxLength = 500;
		if (EventDescription != null &&
				EventDescription.length() > EventDescriptionMaxLength) return false;

		return true;
	}

	public native boolean ValidateMail(String mail) /*-{
		// I hope nobody will tell me a crazy geeky address such like
		//   foo@133.11.0.1
		// But this expr accepts
		//   foo+bar@something.u-tokyo.ac.jp
		var format = /^[a-z0-9_]+(\.[a-z0-9_]+)*(\+[a-z0-9_\.\+]*)?@([a-z0-9_\-]+\.)*u\-tokyo\.ac\.jp$/i;
		return format.test(mail);
  	}-*/;
}

