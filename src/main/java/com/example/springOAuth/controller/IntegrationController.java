package com.example.springOAuth.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springOAuth.entity.Credential;
import com.example.springOAuth.entity.User;
import com.example.springOAuth.repository.CredentialRepository;
import com.example.springOAuth.repository.UserRepository;
import com.example.springOAuth.service.Apps.CalendarService;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/vi/integration")
public class IntegrationController {

        @Autowired
        private CredentialRepository credentialRepository;

        @Autowired
        private UserRepository userRepository;

        @Value("${spring.security.oauth2.client.registration.google.client-id}")
        private String CLIENT_ID;

        @Value("${spring.security.oauth2.client.registration.google.client-secret}")
        private String CLIENT_SECRET;

        private static final String REDIRECT_URI = "http://localhost:5173/auth/calendar/callback";
        private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

        @Autowired
        private CalendarService calendarService;

        @GetMapping("/googlecalender/callback")
        public ResponseEntity<?> getMethodName(@RequestParam String code, @RequestParam String returnTo,
                        @AuthenticationPrincipal User currentUser, HttpServletRequest request,
                        HttpServletResponse response)
                        throws GeneralSecurityException, IOException {

                if (CLIENT_ID.isEmpty()) {
                        throw new RuntimeException("Missing client_id");
                }

                if (CLIENT_SECRET.isEmpty()) {
                        throw new RuntimeException("Missing client_secret");
                }

                if (REDIRECT_URI.isEmpty()) {
                        throw new RuntimeException("Missing redirect url");
                }

                final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

                // Exchange the authorization code for an access token
                TokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                                HTTP_TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, code, REDIRECT_URI)
                                .execute();

                calendarService.storeCredentials(tokenResponse, currentUser.getEmail());

                User user = userRepository.findByEmail(currentUser.getEmail())
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                Credential credential = Credential.builder().user(user).slug("google_calendar").build();
                credentialRepository.save(credential);

                // var redirectUrl =
                // UriComponentsBuilder.fromUriString(returnTo).queryParam("status",
                // "success").build()
                // .toUriString();

                // var redirectStrategy = new DefaultRedirectStrategy();

                // redirectStrategy.sendRedirect(request, response, redirectUrl);

                return ResponseEntity.status(HttpStatus.OK).body("success");

                // var creds = calendarService.getCredentials("solo");

                // // Use the access token to create a Calendar service
                // Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                // tokenResponse.getAccessToken())
                // .setApplicationName("Google Calendar API Java Quickstart")
                // .build();

                // Build a new authorized API client service.
                // Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                // request -> request.getHeaders().setAuthorization("Bearer " +
                // tokenResponse.getAccessToken()))
                // .setApplicationName(APPLICATION_NAME)
                // .build();

                // Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                // getCredentials(HTTP_TRANSPORT, tokenResponse))
                // .setApplicationName(APPLICATION_NAME)
                // .build();

                // Event event = new Event()
                // .setSummary("Google I/O 2015")
                // .setLocation("800 Howard St., San Francisco, CA 94103")
                // .setDescription("A chance to hear more about Google's developer products.");

                // EventDateTime start = new EventDateTime()
                // .setDateTime(new DateTime("2024-07-30T06:00:00+02:00"))
                // .setTimeZone("America/Los_Angeles");
                // event.setStart(start);

                // EventDateTime end = new EventDateTime()
                // .setDateTime(new DateTime("2024-08-02T06:00:00+02:00"))
                // .setTimeZone("America/Los_Angeles");
                // event.setEnd(end);

                // // Create conference data with Google Meet
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

        }

}
