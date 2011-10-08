package net.yutopio.library.booking.client;

import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;

import net.yutopio.library.booking.shared.ApplicationDetail;
import net.yutopio.library.booking.shared.BookingServiceException;

@SuppressWarnings("unchecked")
public interface BookingServiceAsync {
	void CheckAvailability(Date start, Date end, AsyncCallback<Boolean> callback)
			throws BookingServiceException;
	void Submit(ApplicationDetail data, AsyncCallback callback) throws BookingServiceException;
	void Restore(String tokenString, AsyncCallback<ApplicationDetail> callback)
			throws BookingServiceException;
	void Accept(String tokenString, ApplicationDetail detail, String oauthToken,
			AsyncCallback callback) throws BookingServiceException;
	void Reject(String tokenString, AsyncCallback callback) throws BookingServiceException;;
}

