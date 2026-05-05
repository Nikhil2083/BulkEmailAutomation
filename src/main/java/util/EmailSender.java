package util;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.File;
import java.util.Properties;

public class EmailSender {

	public static String sendEmail(String toEmail) {
		if (toEmail == null || toEmail.trim().isEmpty()) {
		    System.out.println("⚠️ Skipping empty email");
		    return "SKIPPED";
		}

		String fromEmail = ConfigReader.get("email.from");
		String password = ConfigReader.get("email.password");

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

			message.setSubject(ConfigReader.get("email.subject"));

			String body = "<html>" + "<body style='font-family: Arial, sans-serif; line-height:1.6; color:#333;'>"

					+ "<p>Hi,</p>"

					+ "<p>I hope you are doing well.</p>"

					+ "<p>I recently came across your hiring requirement for a <b>Automation QA Engineer</b> position and would like to apply. Please find my details below:</p>"

					+ "<table style='border-collapse: collapse;'>"
					+ "<tr><td><b>Current Role:</b></td><td>Automation QA Engineer</td></tr>"
					+ "<tr><td><b>Experience:</b></td><td>4 Years</td></tr>"
					+ "<tr><td><b>Location:</b></td><td>Pune</td></tr>"
					+ "<tr><td><b>Notice Period:</b></td><td>Immediate Joiner</td></tr>"
					+ "<tr><td><b>Current CTC:</b></td><td>&#8377;4.5 LPA</td></tr>" + "</table>"

					+ "<br>"

					+ "<p><b style='color:#2e6c80;'>Technical Skills:</b><br>"
					+ "Manual Testing, MDM Testing, Automation Testing, Java, Selenium WebDriver, Jenkins CI/CD, POM, Git, GitHub, TestNG, API Testing, Agile & Scrum</p>"

					+ "<p><b style='color:#2e6c80;'>Tools:</b><br>"
					+ "Selenium WebDriver, TestNG, Cucumber, Maven, Jenkins, Git, Extent Reports, TestRail, FogBugz, Postman, MySQL, Eclipse</p>"

					+ "<p>I have hands-on experience in automation frameworks using Java, Selenium, and TestNG, along with API testing and Agile collaboration.</p>"

					+ "<p>My resume is attached for your review.</p>"

					+ "<p>Looking forward to your response.</p>"

					+ "<br>" + "<p style='font-size:16px;'><b>Thanks & Regards,</b></p>"
					+ "<p style='font-size:16px;'><b>Nikhil Gurav</b></p>" + "📞8888322083<br>" + "</p>"

					+ "<p>" + "<a href='mailto:nikhilguravnikil@gmail.com'>Email</a> | "
					+ "<a href='www.linkedin.com/in/er-nikhil-gurav-65a9831a3'>LinkedIn</a>" + "</p>"

					+ "</body></html>";

			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setContent(body, "text/html; charset=UTF-8");

			MimeBodyPart filePart = new MimeBodyPart();
			filePart.attachFile(new File("src/main/resources/Nikhil_Gurav_AutomationTesting_4Yrs.pdf"));

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(textPart);
			multipart.addBodyPart(filePart);

			message.setContent(multipart);

			
			Transport.send(message);
			System.out.println("✅ Sent: " + toEmail);
			return "SUCCESS";
			
			
		} catch (Exception e) {
		    System.out.println("❌ Failed: " + toEmail);
		    e.printStackTrace();
		    return "FAILED";
		}
	}
	public static void sendSummaryEmail(int success, int fail, int skip, int total) {

	    String fromEmail = ConfigReader.get("email.from");
	    String password = ConfigReader.get("email.password");

	    Properties props = new Properties();
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port", "587");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");

	    Session session = Session.getInstance(props,
	            new Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(fromEmail, password);
	                }
	            });

	    try {
	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(fromEmail));

	        message.setRecipients(Message.RecipientType.TO,
	                InternetAddress.parse("nikhilguravnkil@gmail.com"));

	        message.setSubject("Bulk Email Report ✅");

	        String body =
	                "<html><body>" +
	                "<h3>📊 Bulk Email Report</h3>" +
	                "<p>✅ Success: " + success + "</p>" +
	                "<p>❌ Failed: " + fail + "</p>" +
	                "<p>⚠️ Skipped: " + skip + "</p>" +
	                "<p>📧 Total: " + total + "</p>" +
	                "</body></html>";

	        MimeBodyPart textPart = new MimeBodyPart();
	        textPart.setContent(body, "text/html; charset=UTF-8");

	        Multipart multipart = new MimeMultipart();
	        multipart.addBodyPart(textPart);

	        message.setContent(multipart);

	        Transport.send(message);

	        System.out.println("📧 Summary email sent!");

	    } catch (Exception e) {
	        System.out.println("❌ Summary email failed!");
	        e.printStackTrace();
	    }
	}
}