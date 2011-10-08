package net.yutopio.library.booking.server;

import java.util.Date;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;

import net.yutopio.library.booking.shared.ApplicationDetail;

public class DatastoreOperator {
	public static Token Add(ApplicationDetail detail) {
		Token token = new Token();
		TextConverter manipulator = new TextConverter(token.Key);
		Key appKey = KeyFactory.createKey("ApplicationId", token.ID);
		Entity entity = new Entity("ApplicationDetail", appKey);
		entity.setProperty("date", new Date());
		entity.setProperty("orgname", manipulator.Encode(detail.OrganizerName));
		entity.setProperty("orgdep", manipulator.Encode(detail.OrganizerDepartment));
		entity.setProperty("contact", manipulator.Encode(detail.OrganizerContact));
		entity.setProperty("numppl", detail.GroupSize);
		entity.setProperty("start", detail.Start.getTime());
		entity.setProperty("end", detail.End.getTime());
		entity.setProperty("equip", ToString(detail.Equipment));
		entity.setProperty("note", manipulator.Encode(detail.Note));
		entity.setProperty("public", detail.IsPublicEvent);
		entity.setProperty("evtitle", detail.EventTitle);
		entity.setProperty("evdesc", detail.EventDescription);
		entity.setProperty("check", manipulator.GetCheckByte());

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		datastore.put(entity);
		return token;
	}

	static String ToString(String[] array) {
		if (array == null || array.length == 0) return "";
		StringBuilder sb = new StringBuilder();
		boolean separator = false;
		for (String element : array) {
			if (separator) sb.append(',');
			separator = true;
			sb.append(element);
		}
		return sb.toString();
	}

	public static ApplicationDetail Lookup(Token token) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key appKey = KeyFactory.createKey("ApplicationId", token.ID);
		Query query = new Query("ApplicationDetail", appKey);

		try
		{
			Entity entity = datastore.prepare(query).asSingleEntity();
			if (entity == null) return null;

			TextConverter manipulator = new TextConverter(token.Key);
			ApplicationDetail ret = new ApplicationDetail();
			ret.OrganizerName = manipulator.Decode((byte[])entity.getProperty("orgname"));
			ret.OrganizerDepartment = manipulator.Decode((byte[])entity.getProperty("orgdep"));
			ret.OrganizerContact = manipulator.Decode((byte[])entity.getProperty("contact"));
			ret.GroupSize = (Integer)entity.getProperty("numppl");
			ret.Start = new Date((Long)entity.getProperty("start"));
			ret.End = new Date((Long)entity.getProperty("end"));
			ret.Equipment = ((String)entity.getProperty("equip")).split("$");
			ret.Note = manipulator.Decode((byte[])entity.getProperty("note"));
			ret.IsPublicEvent = (Boolean)entity.getProperty("public");
			ret.EventTitle = (String)entity.getProperty("evtitle");
			ret.EventDescription = (String)entity.getProperty("evdesc");
			return manipulator.GetCheckByte() != (Byte)entity.getProperty("check") ? null : ret;
		}
		catch (TooManyResultsException exp) { return null; }
	}

	public static void Delete(Token token) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key appKey = KeyFactory.createKey("ApplicationId", token.ID);
		datastore.delete(appKey);
	}
}

