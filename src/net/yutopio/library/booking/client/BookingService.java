package net.yutopio.library.booking.client;

import java.util.Date;

import com.google.gwt.user.client.rpc.RemoteService;

import net.yutopio.library.booking.shared.ApplicationDetail;
import net.yutopio.library.booking.shared.BookingServiceException;

public interface BookingService extends RemoteService {
	boolean CheckAvailability(Date start, Date end) throws BookingServiceException;
	void Submit(ApplicationDetail data) throws BookingServiceException;
	ApplicationDetail Restore(String tokenString) throws BookingServiceException;
	void Accept(String tokenString, ApplicationDetail detail, String oauthToken)
		throws BookingServiceException;
	void Reject(String tokenString)  throws BookingServiceException;
}

