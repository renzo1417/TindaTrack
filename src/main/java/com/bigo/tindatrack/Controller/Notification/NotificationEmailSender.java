package com.bigo.tindatrack.Controller.Notification;

import com.bigo.tindatrack.data.models.User;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

import static com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.loadUser;

public class NotificationEmailSender {

    // Replace with your Gmail + App Password
    private static final String SENDER_EMAIL    = "your_app_email@gmail.com";
    private static final String SENDER_PASSWORD = "your_app_password"; // Gmail App Password

    public static void send(int userId, String subject, String body) {
        Thread emailThread = new Thread(() -> {
            try {
                User user = loadUser();
                if (user == null) {
                    System.err.println("EmailSender: No active session found.");
                    return;
                }

                String recipientEmail = user.getEmail();
                if (recipientEmail == null || recipientEmail.isBlank()) {
                    System.err.println("EmailSender: User has no email address.");
                    return;
                }

                //  SMTP config for Gmail
                Properties props = new Properties();
                props.put("mail.smtp.auth",            "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host",            "smtp.gmail.com");
                props.put("mail.smtp.port",            "587");
                props.put("mail.smtp.ssl.trust",       "smtp.gmail.com");

                Session session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
                    }
                });

                // Build the email
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(SENDER_EMAIL, "TindaTrack Alerts"));
                message.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(recipientEmail)
                );
                message.setSubject(subject);

                // HTML body for a nicer look
                String htmlBody = buildHtmlEmail(user.getUsername(), body);
                message.setContent(htmlBody, "text/html; charset=utf-8");

                Transport.send(message);
                System.out.println("Email sent to: " + recipientEmail);

            } catch (Exception e) {
                System.err.println("EmailSender error: " + e.getMessage());
            }
        }, "tindatrack-email-thread");

        emailThread.setDaemon(true);
        emailThread.start();
    }

    private static String buildHtmlEmail(String username, String body) {
        return """
                <!DOCTYPE html>
                <html>
                <body style="margin:0;padding:0;background-color:#f4f4f4;font-family:'Segoe UI',sans-serif;">
                  <table width="100%%" cellpadding="0" cellspacing="0">
                    <tr>
                      <td align="center" style="padding:40px 0;">
                        <table width="560" cellpadding="0" cellspacing="0"
                               style="background:#ffffff;border-radius:12px;overflow:hidden;
                                      box-shadow:0 2px 8px rgba(0,0,0,0.08);">

                          <!-- Header -->
                          <tr>
                            <td style="background:#2e8b2e;padding:28px 32px;">
                              <h1 style="margin:0;color:#ffffff;font-size:22px;
                                         font-weight:700;letter-spacing:0.5px;">
                                🔔 TindaTrack Alert
                              </h1>
                            </td>
                          </tr>

                          <!-- Body -->
                          <tr>
                            <td style="padding:32px;">
                              <p style="margin:0 0 12px;color:#333333;font-size:15px;">
                                Hi <strong>%s</strong>,
                              </p>
                              <p style="margin:0 0 24px;color:#444444;font-size:15px;
                                         line-height:1.6;">
                                %s
                              </p>
                              <p style="margin:0;color:#aaaaaa;font-size:12px;">
                                This is an automated alert from TindaTrack.
                                Please do not reply to this email.
                              </p>
                            </td>
                          </tr>

                          <!-- Footer -->
                          <tr>
                            <td style="background:#f9f9f9;padding:16px 32px;
                                        border-top:1px solid #eeeeee;">
                              <p style="margin:0;color:#aaaaaa;font-size:11px;">
                                © TindaTrack — Store Management System
                              </p>
                            </td>
                          </tr>

                        </table>
                      </td>
                    </tr>
                  </table>
                </body>
                </html>
                """.formatted(username, body);
    }
}