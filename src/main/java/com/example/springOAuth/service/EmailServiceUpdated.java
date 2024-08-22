package com.example.springOAuth.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.Authenticator;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmailServiceUpdated {

    private final JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.application.name}")
    private String appName;

    public void sendMail(String to, String subject, String body) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true); // true indicates HTML format
        helper.setFrom("noreply@springaouth.com");

        javaMailSender.send(message);
    }

    public void sendEmailVerificationLink(String to, String body)
            throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        // MimeMessage message = new MimeMessage(session);
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Email Verification");

        // helper.setText(body);
        // helper.setFrom("noreply@springaouth.com");
        helper.setFrom(new InternetAddress("no_reply@springbooking.com", appName));

        helper.setReplyTo(new InternetAddress("no_reply@springbooking.com"));

        Context context = new Context();
        context.setVariable("name", to);
        context.setVariable("body", body);
        String emailContent = templateEngine.process("email-template", context);

        helper.setText(emailContent, true);

        javaMailSender.send(message);
    }

    public void sendTemplateEmail(String to, String subject, String body, String pathToAttachment)
            throws MessagingException {

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.debug", "true");
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("solomonrock13@gmail.com", "ijwk qghc fcfk kcan");
            }
        });
        MimeMessage message = new MimeMessage(session);

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        // helper.setText(body);
        ;
        helper.setFrom(new InternetAddress("no_reply@springbooking.com"));

        Context context = new Context();
        context.setVariable("name", to);
        context.setVariable("body", body);
        String emailContent = templateEngine.process("email-template", context);

        helper.setText(emailContent, true);

        if (pathToAttachment != null) {
            FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
            helper.addAttachment("Invoice", file);
        }

        javaMailSender.send(message);
    }

    // public void sendScheduleEventEmail1(BookingResponse bookingResponse)
    // throws MessagingException, FileNotFoundException, IOException {
    // Properties props = new Properties();
    // props.put("mail.transport.protocol", "smtp");
    // props.put("mail.smtp.auth", "true");
    // // props.put("mail.smtp.starttls.enable", false);
    // props.put("mail.smtp.ssl.enable", true);
    // props.put("mail.smtp.port", "465");
    // props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
    // props.put("mail.debug", "true");
    // props.put("mail.smtp.host", "smtp.gmail.com");

    // Session session = Session.getInstance(props, new Authenticator() {
    // @Override
    // protected PasswordAuthentication getPasswordAuthentication() {
    // return new PasswordAuthentication("solomonrock13@gmail.com", "ijwk qghc fcfk
    // kcan");
    // }
    // });
    // // MimeMessage message = new MimeMessage(session);
    // MimeMessage message = javaMailSender.createMimeMessage();
    // MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
    // helper.setTo(bookingResponse.getAttendee().getEmail());
    // helper.setSubject(bookingResponse.getTitle());
    // // helper.setText(body);
    // helper.setFrom("noreply@springaouth.com", "sssolo");

    // Context context = new Context();
    // context.setVariable("name", bookingResponse.getAttendee().getEmail());
    // context.setVariable("body", bookingResponse.getDescription());
    // context.setVariable("attendee", bookingResponse.getAttendee().getEmail());
    // context.setVariable("date", bookingResponse.getStartDate());
    // context.setVariable("location", bookingResponse.getLocation());
    // context.setVariable("host", "Goodness");
    // context.setVariable("eventName", bookingResponse.getTitle());
    // // String emailContent = templateEngine.process("booked-template", context);

    // // helper.setText(emailContent, true);

    // helper.setText("Please find the attached ICS calendar invitation from text.
    // here",
    // true);

    // // Set email content type to text/calendar
    // // message.setContent("Please find the attached ICS calendar invitation.",
    // // "text/calendar");

    // File file = ICSCreator.createIcs(); // Create ICS file
    // // String iscString = ICSCreator.createIcs();
    // // DataSource source = new FileDataSource(file);
    // // helper.addAttachment(file.getName(), file);

    // // Create a MimeBodyPart for the ICS file attachment
    // MimeBodyPart icsAttachment = new MimeBodyPart();
    // DataSource source = new FileDataSource(file);
    // icsAttachment.setDataHandler(new DataHandler(source));
    // icsAttachment.setFileName(file.getName());
    // icsAttachment.setHeader("Content-Type", "text/calendar; charset=UTF-8");
    // icsAttachment.setHeader("Content-Disposition", "attachment; filename=\"" +
    // file.getName() + "\"");

    // // icsAttachment.setDisposition(MimeBodyPart.ATTACHMENT);

    // // icsAttachment.setHeader("Content-Class",
    // // "urn:content-classes:calendarmessage");
    // // icsAttachment.setHeader("Content-ID", "calendar_message");
    // // icsAttachment.setDataHandler(new DataHandler(
    // // new ByteArrayDataSource(iscString,
    // // "text/calendar;method=REQUEST;name=\"invite.ics\"")));

    // // icsAttachment.addHeader("Content-Class",
    // // "urn:content-classes:calendarmessage");
    // // icsAttachment.setContent(iscString, "text/calendar;method=CANCEL");

    // // Create a multipart message
    // MimeMultipart multipart = new MimeMultipart();
    // multipart.addBodyPart(new MimeBodyPart() {
    // {
    // setContent("Please find the attached ICS calendar invitation.",
    // "text/plain");
    // }
    // });

    // multipart.addBodyPart(icsAttachment);

    // // Set the content of the message
    // // message.setContent(multipart);
    // // Transport.send(message);
    // // Send email
    // javaMailSender.send(message);

    // // // new way
    // // // MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    // // mimeMessage.addHeaderLine("method=REQUEST");
    // // mimeMessage.addHeaderLine("charset=UTF-8");
    // // mimeMessage.addHeaderLine("component=VEVENT");
    // // mimeMessage.setFrom(new InternetAddress("noreply@springaouth.com",
    // // "sssolo"));
    // // mimeMessage.addRecipient(RecipientType.TO, new
    // // InternetAddress(bookingResponse.getAttendee().getEmail()));
    // // mimeMessage.setSubject(bookingResponse.getTitle());
    // // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd
    // HHmmss");
    // // StringBuilder builder = new StringBuilder();
    // // var fromEmail = "solomonrock13@gmail.com";
    // // builder.append("BEGIN:VCALENDAR\n" +
    // // "METHOD:REQUEST\n" +
    // // "PRODID:Microsoft Exchange Server 2010\n" +
    // // "VERSION:2.0\n" +
    // // "BEGIN:VTIMEZONE\n" +
    // // "TZID:Asia/Kolkata\n" +
    // // "END:VTIMEZONE\n" +
    // // "BEGIN:VEVENT\n" +
    // // "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:" +
    // // bookingResponse.getAttendee().getEmail() + "\n" +
    // // "ORGANIZER;CN=Foo:MAILTO:" + fromEmail + "\n" +
    // // "DESCRIPTION;LANGUAGE=en-US:" + bookingResponse.getDescription() + "\n" +
    // // "UID:" + bookingResponse.getId() + "\n" +
    // // "SUMMARY;LANGUAGE=en-US:Discussion\n" +
    // // "DTSTART:" + formatter.format(bookingResponse.getStartDate()).replace(" ",
    // // "T") + "\n" +
    // // "DTEND:" + formatter.format(bookingResponse.getEndDate()).replace(" ",
    // "T") +
    // // "\n" +
    // // "CLASS:PUBLIC\n" +
    // // "PRIORITY:5\n" +
    // // "DTSTAMP:20200922T105302Z\n" +
    // // "TRANSP:OPAQUE\n" +
    // // "STATUS:CONFIRMED\n" +
    // // "SEQUENCE:$sequenceNumber\n" +
    // // "LOCATION;LANGUAGE=en-US:Microsoft Teams Meeting\n" +
    // // "BEGIN:VALARM\n" +
    // // "DESCRIPTION:REMINDER\n" +
    // // "TRIGGER;RELATED=START:-PT15M\n" +
    // // "ACTION:DISPLAY\n" +
    // // "END:VALARM\n" +
    // // "END:VEVENT\n" +
    // // "END:VCALENDAR");

    // // MimeBodyPart messageBodyPart = new MimeBodyPart();

    // // messageBodyPart.setHeader("Content-Class",
    // // "urn:content-classes:calendarmessage");
    // // messageBodyPart.setHeader("Content-ID", "calendar_message");
    // // messageBodyPart.setDataHandler(new DataHandler(
    // // new ByteArrayDataSource(builder.toString(),
    // // "text/calendar;method=REQUEST;name=\"invite.ics\"")));

    // // MimeMultipart multipart = new MimeMultipart();

    // // multipart.addBodyPart(messageBodyPart);

    // // mimeMessage.setContent(multipart);

    // // System.out.println(builder.toString());

    // // javaMailSender.send(mimeMessage);
    // // System.out.println("Calendar invite sent");

    // }

    // public void sendScheduleEventEmail2(BookingResponse bookingResponse) {
    // try {
    // // Create the ICS file
    // File icsFile = ICSBuilder.createICS();

    // // Create a new email message
    // MimeMessage message = javaMailSender.createMimeMessage();
    // MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

    // // Set email properties
    // helper.setTo(bookingResponse.getAttendee().getEmail());
    // helper.setSubject(bookingResponse.getTitle());
    // // helper.setText(body);
    // helper.setFrom("noreply@springaouth.com", "sssolo");

    // // Create the email body with event details and a call to action
    // String emailBody = "Please find the attached ICS calendar invitation. ok";

    // // Set email content
    // helper.setText(emailBody, true); // true indicates HTML content

    // // Create a MimeBodyPart for the ICS file attachment
    // DataSource source = new FileDataSource(icsFile);
    // helper.addAttachment("Event Invitation.ics", source);

    // // Send email
    // javaMailSender.send(message);

    // System.out.println("Email sent successfully with ICS attachment and event
    // details in the body.");
    // } catch (MessagingException | IOException e) {
    // e.printStackTrace(); // Log the error and handle it appropriately
    // }
    // }

    // public void sendScheduleEventEmail(BookingResponse bookingResponse) {
    // try {

    // // Create the ICS file
    // File icsFile = ICSBuilder.createICS();

    // // Create a new email message
    // MimeMessage message = javaMailSender.createMimeMessage();
    // MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
    // helper.setTo(bookingResponse.getAttendee().getEmail());
    // helper.setSubject(bookingResponse.getTitle());
    // // helper.setText(body);
    // helper.setFrom("noreply@springaouth.com", "sssolo");

    // // Create the email body with event details and instructions
    // String emailBody = "<html><body>"
    // + "<h3>You're Invited!</h3>"
    // + "<p><strong>Event:</strong> Sample Event</p>"
    // + "<p><strong>Date:</strong> July 26, 2024</p>"
    // + "<p><strong>Time:</strong> 09:00 AM - 10:00 AM</p>"
    // + "<p><strong>Description:</strong> This is a sample event description.</p>"
    // + "<p><strong>Location:</strong> Virtual Meeting</p>"
    // + "<p>Organizer: Organizer Name (organizer@example.com)</p>"
    // + "<p>Please find the attached calendar invitation in ICS format. You can add
    // it to your calendar application by downloading the attached file.</p>"
    // + "<p>To RSVP, please respond to this invitation or use your calendar
    // application’s RSVP feature.</p>"
    // + "</body></html>";

    // // Set email content
    // MimeBodyPart messageBodyPart = new MimeBodyPart();
    // messageBodyPart.setContent(emailBody, "text/html");

    // // Create a multipart message
    // Multipart multipart = new MimeMultipart();
    // multipart.addBodyPart(messageBodyPart);

    // // Attach the ICS file
    // MimeBodyPart attachmentPart = new MimeBodyPart();
    // DataSource source = new FileDataSource(icsFile);
    // attachmentPart.setDataHandler(new DataHandler(source));
    // attachmentPart.setFileName("Event Invitation.ics");
    // multipart.addBodyPart(attachmentPart);

    // // Set the complete message parts
    // message.setContent(multipart);

    // // Send the message
    // javaMailSender.send(message);

    // System.out.println("Email sent successfully with ICS attachment, RSVP, and
    // Organizer details.");
    // } catch (MessagingException | IOException e) {
    // e.printStackTrace(); // Log the error and handle it appropriately
    // }
    // }

}
