package com.football_school_spring.utils;

import com.football_school_spring.models.dto.FixtureDTO;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import static org.apache.log4j.Logger.getLogger;

public class CalendarEventCreator {
    private static final Logger logger = getLogger(CalendarEventCreator.class);
    private static final String APPLICATION_NAME = "Football School";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "src/main/resources/tokens";

    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private static Credential getCredentials(NetHttpTransport httpTransport) throws IOException {
        InputStream in = CalendarEventCreator.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static String createEventHtmlLink(FixtureDTO fixtureDTO) {
        try {
            NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service = new Calendar.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            Event event = getSuppliedEvent(fixtureDTO);
            String calendarId = "primary";
            event = service.events().insert(calendarId, event).execute();

            return prepareHtmlLink(event);
        } catch (IOException | GeneralSecurityException exception) {
            logger.error("Error during calendar event creation");
            throw new RuntimeException(exception);
        }
    }

    private static String prepareHtmlLink(Event event) {
        String generalPrefix = "https://www.google.com/calendar/event?eid=";
        String eventId = event.getHtmlLink().substring(generalPrefix.length());

        String resultPrefix = "https://calendar.google.com/calendar/event?action=TEMPLATE&tmeid=";
        String resultSuffix = "&tmsrc=football.school.app%40gmail.com&catt=false&pprop=HowCreated:DUPLICATE&hl=pl&scp=ONE";
        return resultPrefix + eventId + resultSuffix;
    }

    private static Event getSuppliedEvent(FixtureDTO fixtureDTO) {
        String summary = "Match against " + fixtureDTO.getOpponent();
        String description = String.format("%s%nAddress: %s", summary, fixtureDTO.getAddress());
        String startDateTimeStr = String.format("%sT%s:00+01:00", fixtureDTO.getDate(), fixtureDTO.getTime());
        String endDateTimeStr = String.format("%sT%s:00+01:00", fixtureDTO.getDate(), fixtureDTO.getTime().plusHours(2));

        return new Event().setSummary(summary).setDescription(description)
                .setStart(getEventDateTime(startDateTimeStr))
                .setEnd(getEventDateTime(endDateTimeStr));
    }

    private static EventDateTime getEventDateTime(String time) {
        DateTime dateTime = new DateTime(time);
        return new EventDateTime()
                .setDateTime(dateTime)
                .setTimeZone("Europe/Warsaw");
    }
}