package com.example.springOAuth.service.Apps;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.springOAuth.config.GoogleCredentialProperties;
import com.example.springOAuth.response.BookingResponse;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.ConferenceData;
import com.google.api.services.calendar.model.ConferenceSolutionKey;
import com.google.api.services.calendar.model.CreateConferenceRequest;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CalendarService {

        @Autowired
        private GoogleCredentialProperties googleCredentialProperties;

        private final String applicationName = "springOAuth";
        private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

        private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
        // private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

        private static final String TOKENS_DIRECTORY_PATH = "tokens";

        @Value("${spring.security.oauth2.client.registration.google.client-id}")
        private String CLIENT_ID;

        @Value("${spring.security.oauth2.client.registration.google.client-secret}")
        private String CLIENT_SECRET;

        public Event createEventHandler(BookingResponse bookingResponse)
                        throws IOException, GeneralSecurityException {
                final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

                Credential credential = getCredentials(bookingResponse.getUser().getEmail());

                if (credential == null) {
                        throw new RuntimeException("Missing credential details");
                }

                // Build a new authorized API client service.
                Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                                request -> request.getHeaders().setAuthorization("Bearer " +
                                                credential.getAccessToken()))
                                .setApplicationName(applicationName)
                                .build();

                // Convert ZonedDateTime to java.util.Date
                Date startFormatted = Date.from(bookingResponse.getStartDate().toInstant());
                Date endFormatted = Date.from(bookingResponse.getEndDate().toInstant());

                ZonedDateTime zonedDateTime = ZonedDateTime.now();

                ZoneId zoneId = zonedDateTime.getZone();

                Event event = new Event()
                                .setSummary(bookingResponse.getTitle())
                                .setLocation(bookingResponse.getLocation())
                                .setDescription(bookingResponse.getDescription());

                EventDateTime start = new EventDateTime()
                                .setDateTime(new DateTime(startFormatted))
                                .setTimeZone(zoneId.toString());
                event.setStart(start);

                EventDateTime end = new EventDateTime()
                                .setDateTime(new DateTime(endFormatted))
                                .setTimeZone(zoneId.toString());
                event.setEnd(end);

                // add attendee

                EventAttendee[] attendees = new EventAttendee[] {
                                new EventAttendee().setEmail(bookingResponse.getAttendee().getEmail()),

                };
                event.setAttendees(Arrays.asList(attendees));

                // Create conference data with Google Meet
                ConferenceData conferenceData = new ConferenceData();
                CreateConferenceRequest createConferenceRequest = new CreateConferenceRequest()
                                .setRequestId(UUID.randomUUID().toString()) // Unique request ID
                                .setConferenceSolutionKey(new ConferenceSolutionKey().setType("hangoutsMeet"));
                conferenceData.setCreateRequest(createConferenceRequest);
                event.setConferenceData(conferenceData);

                // Insert the event into the calendar
                var createdEvent = service.events().insert("primary", event)
                                .setConferenceDataVersion(1)
                                .execute();

                // Get the Google Meet link
                String hangoutLink = createdEvent.getHangoutLink();
                System.out.println("Google Meet Link: " + hangoutLink);

                return createdEvent;
        }

        private GoogleAuthorizationCodeFlow googleAuthFlow() throws IOException, GeneralSecurityException {

                final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
                // InputStream in =
                // CalendarService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);

                Details details = new Details();
                details.setTokenUri(googleCredentialProperties.getCredentials().getToken_uri())
                                .setClientId(googleCredentialProperties.getCredentials().getClient_id())
                                .setClientSecret(googleCredentialProperties.getCredentials().getClient_secret())
                                .setAuthUri(googleCredentialProperties.getCredentials().getAuth_uri())
                                .setRedirectUris(googleCredentialProperties.getCredentials().getRedirect_uris());

                GoogleClientSecrets clientSecrets = new GoogleClientSecrets();

                clientSecrets.setWeb(details);
                // GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                // new InputStreamReader(in));
                GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                                .setAccessType("offline")
                                .setApprovalPrompt("force")
                                .build();

                return flow;

        }

        public Credential getCredentials(String userId) throws IOException, GeneralSecurityException {

                // Build flow and trigger user authorization request.
                GoogleAuthorizationCodeFlow flow = googleAuthFlow();
                Credential credential = flow.loadCredential(userId);
                return credential;
        }

        public Credential storeCredentials(TokenResponse tokenResponse, String userId)
                        throws IOException, GeneralSecurityException {

                // Build flow and trigger user authorization request.
                GoogleAuthorizationCodeFlow flow = googleAuthFlow();
                Credential credential = flow.createAndStoreCredential(tokenResponse, userId);
                return credential;
        }

        // public String createMeetingLink(BookingResponse bookingResponse,
        // HttpServletRequest request,
        // HttpServletResponse response)
        // throws IOException, GeneralSecurityException {

        // // Build a new authorized API client service.
        // final NetHttpTransport HTTP_TRANSPORT =
        // GoogleNetHttpTransport.newTrustedTransport();

        // return getAuthorizationUrl(HTTP_TRANSPORT, request, response);

        // }

        public String getAuthorizationUrl(HttpServletRequest request,
                        HttpServletResponse response)
                        throws IOException, GeneralSecurityException {

                String redirectUri = UriComponentsBuilder
                                .fromHttpUrl(googleCredentialProperties.getCredentials().getRedirectUrl())
                                .replaceQuery(null)
                                .build()
                                .toUriString();

                GoogleAuthorizationCodeFlow flow = googleAuthFlow();
                var url = flow.newAuthorizationUrl().setRedirectUri(redirectUri).toString();

                return url;

        }
}
