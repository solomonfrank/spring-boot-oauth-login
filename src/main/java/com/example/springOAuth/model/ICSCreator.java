package com.example.springOAuth.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.UUID;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.PartStat;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.parameter.Rsvp;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStamp;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.Organizer;
import net.fortuna.ical4j.model.property.Priority;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Sequence;
import net.fortuna.ical4j.model.property.Status;
import net.fortuna.ical4j.model.property.Transp;
import net.fortuna.ical4j.model.property.Trigger;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.immutable.ImmutableCalScale;
import net.fortuna.ical4j.model.property.immutable.ImmutableVersion;

public class ICSCreator {

    public static File createIcs() throws FileNotFoundException, IOException {

        ZonedDateTime start = ZonedDateTime.now();
        ZonedDateTime end = ZonedDateTime.now()
                .plusDays(1);

        String eventName = "Demo event";
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddTHHmmss");

        VEvent meeting = new VEvent(start, end, eventName);

        ZonedDateTime now = ZonedDateTime.now();

        ZoneId zoneId = now.getZone();

        // Create a TimeZone
        TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
        TimeZone timezone = registry.getTimeZone(zoneId.getId());
        VTimeZone tz = timezone.getVTimeZone();

        meeting.add(new Organizer("solomonfrank73@hotmail.com"));
        meeting.add(new Transp("OPAQUE"));
        meeting.add(new Trigger());

        meeting.add(tz);

        // // generate unique identifier..
        // UidGenerator ug = new UidGenerator("uidGen");
        // Uid uid = ug.generateUid();
        // meeting.add(uid);
        // meeting.getProperties().add(new Uid(UUID.randomUUID().toString()));
        // meeting.getProperties().add(new Description("This is a sample event
        // description."));

        // add attendees..
        Attendee host = new Attendee(URI.create("mailto:dev1@bookme.com"));
        host.add(Role.REQ_PARTICIPANT);
        host.add(PartStat.ACCEPTED);

        host.add(Rsvp.TRUE);
        host.add(new Cn("Developer 1"));
        meeting.add(host);

        // Add properties

        meeting.add(new Description("This is a sample event description."));
        meeting.add(new Uid(UUID.randomUUID().toString()));
        meeting.add(new Location("Overmountain Shelter on the AT"));
        meeting.add(new DtStamp());
        meeting.add(new DtStart<Temporal>(start));
        meeting.add(new DtEnd<Temporal>(end));

        // meeting.add(new DtStart<>(start.format(formatter)));
        // meeting.add(new DtEnd<>(end.format(formatter)));

        Attendee dev2 = new Attendee(URI.create("mailto:enweremrock@gmail.com"));
        dev2.add(Role.OPT_PARTICIPANT);
        dev2.add(new Cn("Developer 2"));
        dev2.add(Rsvp.TRUE);
        dev2.add(PartStat.ACCEPTED);
        meeting.add(dev2);

        Calendar icsCalender = new Calendar();
        icsCalender.add(new ProdId("-//Bookme Fortuna//iCal4j 1.0//EN"));
        icsCalender.add(ImmutableVersion.VERSION_2_0);
        icsCalender.add(ImmutableCalScale.GREGORIAN);
        icsCalender.add(new Method("REQUEST"));
        icsCalender.add(new Sequence(0));
        icsCalender.add(new Priority(5));
        icsCalender.add(new Status("CONFIRMED"));

        icsCalender.add(meeting);

        // icsCalender.getComponents().add(meeting);
        String calendarContent = icsCalender.toString();

        File file = new File("invite.ics");

        try (FileOutputStream fout = new FileOutputStream(file)) {
            CalendarOutputter outputter = new CalendarOutputter();

            outputter.output(icsCalender, fout);
        }

        // try (FileOutputStream fout = new FileOutputStream("invite.ics")) {

        // }

        return file;

    }

}
