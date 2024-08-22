package com.example.springOAuth.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.UUID;

import com.example.springOAuth.response.BookingResponse;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.CuType;
import net.fortuna.ical4j.model.parameter.PartStat;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.parameter.Rsvp;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStamp;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Organizer;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Sequence;
import net.fortuna.ical4j.model.property.Status;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Transp;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.XProperty;
import net.fortuna.ical4j.model.property.immutable.ImmutableCalScale;
import net.fortuna.ical4j.model.property.immutable.ImmutableVersion;

public class ICSBuilder {
    public static File createICS(BookingResponse bookingResponse) throws IOException {
        // StringBuilder icsContent = new StringBuilder();
        // String fromEmail = "solomonfrank73@hotmail.com";

        // // Calendar Header
        // icsContent.append("BEGIN:VCALENDAR\n");
        // icsContent.append("VERSION:2.0\n");
        // icsContent.append("PRODID:-//Your Company//NONSGML Your Product//EN\n");

        // // Event Header
        // icsContent.append("BEGIN:VEVENT\n");
        // icsContent.append("UID:").append(UUID.randomUUID().toString()).append("@example.com\n");
        // icsContent.append("DTSTAMP:20240726T090000Z\n"); // Timestamp when the event
        // was created
        // icsContent.append("DTSTART:20240726T090000\n"); // Start time of the event
        // icsContent.append("DTEND:20240726T100000\n"); // End time of the event
        // icsContent.append("SUMMARY:Sample Event\n"); // Event summary
        // icsContent.append("DESCRIPTION:This is a sample event description.\n"); //
        // Event description
        // icsContent.append("LOCATION:Virtual Meeting\n"); // Event location
        // icsContent.append("STATUS:CONFIRMED\n"); // Event status
        // icsContent.append("SEQUENCE:0\n"); // Event sequence

        // // Add the organizer
        // icsContent.append("ORGANIZER;CN=Organizer
        // Name:mailto:organizer@example.com\n");

        // // Add attendee with RSVP option
        // icsContent.append("ATTENDEE;CN=Recipient
        // Name;RSVP=TRUE:mailto:recipient@example.com\n");

        // // Event Footer
        // icsContent.append("END:VEVENT\n");

        // // Calendar Footer
        // icsContent.append("END:VCALENDAR");

        // File file = new File("sample_event.ics");
        // try (FileWriter writer = new FileWriter(file)) {
        // writer.write(icsContent.toString());
        // }

        // icalj
        // Create a new calendar
        Calendar calendar = new Calendar();
        calendar.add(new ProdId("//Google Inc//Google Calendar 70.9054//EN"));
        calendar.add(ImmutableVersion.VERSION_2_0);
        calendar.add(ImmutableCalScale.GREGORIAN);

        ZonedDateTime start = ZonedDateTime.now();
        ZonedDateTime end = ZonedDateTime.now()
                .plusDays(1);

        String eventName = bookingResponse.getTitle();

        // Create an event
        VEvent event = new VEvent(start, end, eventName);

        // Set event properties

        event.add(new Description(bookingResponse.getDescription()));
        event.add(new Uid(UUID.randomUUID().toString()));
        event.add(new Location(bookingResponse.getLocation()));
        event.add(new DtStamp());
        event.add(new DtStart<Temporal>(bookingResponse.getStartDate()));
        event.add(new DtEnd<Temporal>(bookingResponse.getEndDate()));
        event.add(new Summary(bookingResponse.getTitle()));
        event.add(new Status("CONFIRMED"));
        event.add(new Transp("OPAQUE"));
        event.add(new Sequence(0));
        event.add(new Sequence(0));
        Attendee dev2 = new Attendee(URI.create("mailto:" + bookingResponse.getAttendee().getEmail()));
        dev2.add(Role.OPT_PARTICIPANT);
        dev2.add(new Cn("Developer 2"));
        dev2.add(Rsvp.TRUE);
        dev2.add(CuType.INDIVIDUAL);
        dev2.add(PartStat.NEEDS_ACTION);
        event.add(dev2);

        event.add(new Organizer(URI.create("mailto:" + bookingResponse.getUser().getEmail())));

        XProperty customLocation = new XProperty("X-GOOGLE-CONFERENCE", bookingResponse.getMeetingUrl());
        event.add(customLocation);

        // Add event to calendar
        calendar.add(event);
        // calendar.add(new Method("REQUEST"));

        // Write to file
        File file = new File("invite.ics");
        try (FileOutputStream fout = new FileOutputStream(file)) {
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(calendar, fout);
        }

        return file;
    }

}
