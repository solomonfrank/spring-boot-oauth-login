package com.example.springOAuth.service.Email;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.springframework.mail.javamail.MimeMessageHelper;

import com.example.springOAuth.model.ICSBuilder;
import com.example.springOAuth.response.BookingResponse;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.Authenticator;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class SmtpEmailProvider implements IEmailProvider {
    private final String smtpHost;
    private final int smtpPort;
    private final String username;
    private final String password;
    private final String protocol;
    private final String debug;
    private final String trust;
    private final boolean isStarttlsEnable;
    private final boolean isAuth;
    private final boolean enableSSl;

    private final String inviteFileName = "invite.ics";

    public SmtpEmailProvider(SmtpConfigModel config) {

        this.password = config.getPassword();
        this.smtpHost = config.getHost();
        this.smtpPort = config.getPort();
        this.username = config.getUsername();
        this.debug = config.getDebug();
        this.protocol = config.getProtocol();
        this.trust = config.getTrust();
        this.isStarttlsEnable = config.isStarttlsEnable();
        this.isAuth = config.isAuth();
        this.enableSSl = config.isEnableSSl();

    }

    private Session createSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.auth", isAuth);
        properties.put("mail.smtp.starttls.enable", this.isStarttlsEnable);
        properties.put("mail.transport.protocol", this.protocol);
        properties.put("mail.smtp.ssl.enable", this.enableSSl);
        properties.put("mail.smtp.ssl.trust", this.trust);
        properties.put("mail.debug", this.debug);

        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    @Override
    public void sendEmailVerificationLink() {

    }

    public void sendEventBookRemainder(BookingResponse bookingResponse) {
        try {

            // Create the ICS file
            File icsFile = ICSBuilder.createICS(bookingResponse);

            // Create a new email message

            String hostName = bookingResponse.getUser().getName().replace(" ", "");
            MimeMessage message = new MimeMessage(createSession());
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setTo(bookingResponse.getAttendee().getEmail());
            helper.setSubject(bookingResponse.getTitle());
            helper.setReplyTo(
                    new InternetAddress(hostName + "@springaouth.com"));
            // helper.setText(body);
            helper.setFrom(hostName + "@springaouth.com",
                    bookingResponse.getUser().getName());

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM dd, yyyy");
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mma");

            // Create the email body with event details and instructions
            String emailBody = String.format(
                    """
                                    <html><body>
                                    <h3>You're Invited!</h3>
                                    <p><strong>Event:</strong>%s</p>
                                    <p><strong>Date:</strong>%s</p>
                                    <p><strong>Time:</strong>%s</p>
                                    <p><strong>Description:</strong>%s</p>
                                    <p><strong>Location:</strong>%s</p>
                                    <p>Please find the attached calendar invitation in ICS format. You can add it to your calendar application by downloading the attached file.</p>
                                    </body></html>
                            """,
                    bookingResponse.getTitle(), bookingResponse.getStartDate().format(dateTimeFormatter),
                    bookingResponse.getStartDate().format(timeFormat), bookingResponse.getDescription(),
                    bookingResponse.getLocation());

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

            // Set email content
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(emailBody, "text/html");

            // Create a multipart message
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // Attach the ICS file
            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new FileDataSource(icsFile);
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(inviteFileName);
            multipart.addBodyPart(attachmentPart);

            // Set the complete message parts
            message.setContent(multipart);

            // Send the message
            Transport.send(message);

            System.out.println("Email sent successfully with ICS attachment, RSVP, and Organizer details.");
        } catch (MessagingException | IOException e) {
            e.printStackTrace(); // Log the error and handle it appropriately
        }
    }

}
