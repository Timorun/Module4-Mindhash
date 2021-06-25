package com.mindhash.MindhashApp.Integration;
import com.mindhash.MindhashApp.model.ResMsg;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;


public class Sendgrid {
    final String PASSWORD_RESET_SUBJECT = "Password reset request";

    public String PASSWORD_RESET_BODY = "Hi, "
            + "Someone has requested to reset your password. If it was not you, please contact us as soon as possible, "
            + "otherwise here is your password reset link: "
            + "http://localhost:8080/mindhash/new-password.html?token=$token";

    public String EMAIL_VERIFICATION_SUBJECT = "Email verification required";

    public String EMAIL_VERIFICATION_BODY = "Hi, "
            + "Thank you for registering with Mindhash. There is one last step required in order to complete your registration, "
            + "please click on the following link and log in with your new credentials: "
            + "http://localhost:8080/mindhash/email-verification-sucess.html?token=$token";

    public boolean sendPasswordRequest(String email, String token, ResMsg res) {
        boolean result = false;
        String bodyWithToken = PASSWORD_RESET_BODY.replace("$token", token);
        Email from = new Email("test@example.com");
        String subject = PASSWORD_RESET_SUBJECT;
        Email to = new Email(email);
        Content content = new Content("text/plain", bodyWithToken);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid("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");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());

            //TODO: create thread which periodically checks for status code; remove current condition?
            if (!response.getHeaders().isEmpty()) {
                result = true;
            }
            /*if (response.getStatusCode() == 200) {
                result = true;
            } */

        } catch (IOException ex) {
            ex.getMessage();
        }
        return result;

        /*public static String PASSWORD_RESET_BODY = "Hi, "
            + "Someone has requested to reset your password. If it was not you, please contact us as soon as possible, "
            + "otherwise here is your password reset link: "
            + "http://localhost:8080/newpassword.html?token=$token"; */
    }

    public boolean sendEmailVerification(String email, String emailToken) {
        boolean result = false;
        String bodyWithToken = EMAIL_VERIFICATION_BODY.replace("$token", emailToken);
        Email from = new Email("test@example.com");
        String subject = EMAIL_VERIFICATION_SUBJECT;
        Email to = new Email(email);
        Content content = new Content("text/plain", bodyWithToken);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid("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");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());

            //TODO: create thread which periodically checks for status code; remove current condition?
            if (!response.getHeaders().isEmpty()) {
                result = true;
            }
            /*if (response.getStatusCode() == 200) {
                result = true;
            } */

        } catch (IOException ex) {
            ex.getMessage();
        }
        return result;

    }

    /*public static void main(String[] args) throws IOException {

        String bodyWithToken = PASSWORD_RESET_BODY.replace("$token", "1");

        Email from = new Email("test@example.com");
        Email to = new Email("d.kulyk@student.utwente.nl"); // use your own email address here

        String subject = "Test email";
        Content content = new Content("text/html", bodyWithToken);

        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid("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");
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sg.api(request);

        System.out.println(response.getStatusCode());
        System.out.println(response.getHeaders());
        System.out.println(response.getBody());
    } */
}
