package com.beatrice.book.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

//@Service
@RequiredArgsConstructor

public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async // nu vreau ca userul sa stepte dupa mail asa ca fac asta asincron
    public void sendEmail(
            String to, //cui ii trimit mail-ul
            String username,
            EmailTemplateName emailTemplate,
            String confirmationUrl,
            String activationCode,
            String subject
    ) throws MessagingException {
        String templateName;
        if(emailTemplate == null) {
            templateName = "confirm-email-needed";
        } else {
            templateName = emailTemplate.name();
        }

        // aici configurez trimiterea email urilor
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_MIXED,
                UTF_8.name()
        );
        // parsam chestii in template folosind Map
        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("confirmationUrl", confirmationUrl);
        properties.put("activation_code", activationCode);


        // un obiect de tip context ca sa pasram parametrii catre template
        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom("contact@beatrice.com");
        helper.setTo(to);
        helper.setSubject(subject);

        //trebuie sa procesez template-ul
        String template = templateEngine.process(templateName, context);

        // parsam true ca sa zicem daca e html sau nu
        helper.setText(template, true);

        // trimitem email ul
        mailSender.send(mimeMessage);
    }
}
