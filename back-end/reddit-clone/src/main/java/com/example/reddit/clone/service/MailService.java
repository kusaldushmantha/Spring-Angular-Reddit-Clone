package com.example.reddit.clone.service;

import com.example.reddit.clone.model.NotificationMail;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@AllArgsConstructor
@Service
public class MailService
{
    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    @Async
    public void sendMail( NotificationMail notificationMail )
    {
        MimeMessagePreparator messagePreparator = mimeMessage ->
        {
            MimeMessageHelper messageHelper = new MimeMessageHelper( mimeMessage );
            messageHelper.setFrom( "springredditcloneapp@email.com" );
            messageHelper.setTo( notificationMail.getRecipient() );
            messageHelper.setSubject( notificationMail.getSubject() );
            messageHelper.setText( mailContentBuilder.build( notificationMail.getBody() ) );
        };
        mailSender.send( messagePreparator );
        Logger.getLogger( getClass().getName() ).log( Level.INFO, "Activation mail sent" );
    }
}
