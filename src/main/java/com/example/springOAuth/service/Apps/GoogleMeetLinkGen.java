package com.example.springOAuth.service.Apps;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.springOAuth.response.BookingResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/* class to demonstrate use of Calendar events list API */
public class GoogleMeetLinkGen {

        private static RedirectStrategy authorizationRedirectStrategy = new DefaultRedirectStrategy();
        /**
         * Application name.
         */
        private static final String APPLICATION_NAME = "springOAuth";
        /**
         * Global instance of the JSON factory.
         */
        private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
        /**
         * Directory to store authorization tokens for this application.
         */
        private static final String TOKENS_DIRECTORY_PATH = "tokens";

        /**
         * Global instance of the scopes required by this quickstart.
         * If modifying these scopes, delete your previously saved tokens/ folder.
         */
        private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
        private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

        /**
         * Creates an authorized Credential object.
         *
         * @param HTTP_TRANSPORT The network HTTP Transport.
         * @return An authorized Credential object.
         * @throws IOException If the credentials.json file cannot be found.
         */
        private static String getCredentials(final NetHttpTransport HTTP_TRANSPORT, HttpServletRequest request,
                        HttpServletResponse response)
                        throws IOException {
                // Load client secrets.
                InputStream in = GoogleMeetLinkGen.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
                if (in == null) {
                        throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
                }
                GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

                // Build flow and trigger user authorization request.
                GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                                .setAccessType("offline")
                                .build();

                // LocalServerReceiver receiver = new
                // LocalServerReceiver.Builder().setPort(5173)
                // .setCallbackPath("/auth/calendar/callback")
                // .build();

                String redirectUri = UriComponentsBuilder.fromHttpUrl("http://localhost:5173/auth/calendar/callback")
                                .replaceQuery(null)
                                .build()
                                .toUriString();

                // var ss = receiver.getRedirectUri();

                // authorizationRedirectStrategy.sendRedirect(request, response,
                // receiver.toString());
                new AuthorizationCodeInstalledApp.DefaultBrowser().browse(redirectUri);

                var fg = flow.newAuthorizationUrl().setRedirectUri(redirectUri).toURL();

                var url = flow.newAuthorizationUrl().setRedirectUri(redirectUri).toString();

                // Credential credential = new AuthorizationCodeInstalledApp(flow, null)
                // .authorize("user");
                /// Credential route = credential.authorize("user");
                // // returns an authorized Credential object.

                return url;

                // return credential;
        }

        public static String createMeetingLink(BookingResponse bookingResponse, HttpServletRequest request,
                        HttpServletResponse response)
                        throws IOException, GeneralSecurityException {

                // Build a new authorized API client service.
                final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

                return getCredentials(HTTP_TRANSPORT, request, response);
                // Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                // getCredentials(HTTP_TRANSPORT, request, response))
                // .setApplicationName(APPLICATION_NAME)
                // .build();

                // Refer to the Java quickstart on how to setup the environment:
                // https://developers.google.com/calendar/quickstart/java
                // Change the scope to CalendarScopes.CALENDAR and delete any stored
                // credentials.

                // Convert ZonedDateTime to java.util.Date
                // Date startFormatted = Date.from(bookingResponse.getStartDate().toInstant());
                // Date endFormatted = Date.from(bookingResponse.getEndDate().toInstant());
                // Event event = new Event()
                // .setSummary("Google I/O 2015")
                // .setLocation("800 Howard St., San Francisco, CA 94103")
                // .setDescription("A chance to hear more about Google's developer products.");

                // EventDateTime start = new EventDateTime()
                // .setDateTime(new DateTime(startFormatted))
                // .setTimeZone("America/Los_Angeles");
                // event.setStart(start);

                // EventDateTime end = new EventDateTime()
                // .setDateTime(new DateTime(endFormatted))
                // .setTimeZone("America/Los_Angeles");
                // event.setEnd(end);

                // String[] recurrence = new String[] { "RRULE:FREQ=DAILY;COUNT=2" };
                // event.setRecurrence(Arrays.asList(recurrence));

                // EventAttendee[] attendees = new EventAttendee[] {
                // new EventAttendee().setEmail(bookingResponse.getAttendee().getEmail()),

                // };
                // event.setAttendees(Arrays.asList(attendees));

                // EventReminder[] reminderOverrides = new EventReminder[] {
                // new EventReminder().setMethod("email").setMinutes(24 * 60),
                // new EventReminder().setMethod("popup").setMinutes(10),
                // };
                // Event.Reminders reminders = new Event.Reminders()
                // .setUseDefault(false)
                // .setOverrides(Arrays.asList(reminderOverrides));
                // event.setReminders(reminders);

                // String calendarId = "primary";
                // event = service.events().insert(calendarId, event).execute();
                // System.out.printf("Event created: %s\n", event.getHtmlLink());

                // Create conference data with Google Meet
                // ConferenceData conferenceData = new ConferenceData();
                // CreateConferenceRequest createConferenceRequest = new
                // CreateConferenceRequest()
                // .setRequestId(UUID.randomUUID().toString()) // Unique request ID
                // .setConferenceSolutionKey(new
                // ConferenceSolutionKey().setType("hangoutsMeet"));
                // conferenceData.setCreateRequest(createConferenceRequest);
                // event.setConferenceData(conferenceData);

                // // Insert the event into the calendar
                // Event createdEvent = service.events().insert("primary", event)
                // .setConferenceDataVersion(1)
                // .execute();

                // // Get the Google Meet link
                // String hangoutLink = createdEvent.getHangoutLink();
                // System.out.println("Google Meet Link: " + hangoutLink);

                // // List the next 10 events from the primary calendar.
                // DateTime now = new DateTime(System.currentTimeMillis());
                // Events events = service.events().list("primary")
                // .setMaxResults(10)
                // .setTimeMin(now)
                // .setOrderBy("startTime")
                // .setSingleEvents(true)
                // .execute();
                // List<Event> items = events.getItems();
                // if (items.isEmpty()) {
                // System.out.println("No upcoming events found.");
                // } else {
                // System.out.println("Upcoming events");
                // for (Event event : items) {
                // DateTime start = event.getStart().getDateTime();
                // if (start == null) {
                // start = event.getStart().getDate();
                // }
                // System.out.printf("%s (%s)\n", event.getSummary(), start);
                // }
                // }
        }
}