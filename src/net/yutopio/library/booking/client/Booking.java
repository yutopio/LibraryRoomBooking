package net.yutopio.library.booking.client;

import java.util.Date;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

public class Booking implements EntryPoint {

	// TODO: You can change the list of equipment which can be checked out.
	final String[] Equipment = new String[] { "Projector" };

	@SuppressWarnings("deprecation")
	@Override
	public void onModuleLoad() {
		final Document doc = Document.get();
		final TextBox dateBox = TextBox.wrap(doc.getElementById("date"));
		dateBox.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				final PopupPanel popup = new PopupPanel(true);
				final DatePicker datePicker = new DatePicker();
				datePicker.setValue(new Date(), true);
				datePicker.setVisible(true);
				datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
					@Override
					public void onValueChange(ValueChangeEvent<Date> event) {

						dateBox.setText(event.getValue().toString());
						popup.hide();
					}
				});

				popup.setWidget(datePicker);
				popup.showRelativeTo(dateBox);
			}});

		final HTML publicEventCheckLabel = HTML.wrap(doc.getElementById("publicEventCheckLabel"));
		final InputElement publicEventCheck = InputElement.as(doc.getElementById("publicEventCheck"));
		final HTML publicEventTable = HTML.wrap(doc.getElementById("publicEventTable"));
		publicEventCheckLabel.removeFromParent();
		final DisclosurePanel panel = new DisclosurePanel(publicEventCheckLabel);
		panel.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {
				publicEventCheck.setChecked(false);
			}
		});
		panel.addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				publicEventCheck.setChecked(true);
			}
		});
		publicEventTable.removeFromParent();
		panel.setAnimationEnabled(true);
		panel.setContent(publicEventTable);
		RootPanel.get("publicEvent").add(panel);
	}
}

