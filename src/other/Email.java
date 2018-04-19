package other;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Created by zhuanggangqing on 2018/2/20.
 */
public class Email {

    private static final String HOST = "smtp.163.com";
    private static final String FROM = "13151522158@163.com";
    private static final String P = "SFABRAD2916";

    private static Session getSession(){
        Properties properties = new Properties();
        properties.put("mail.smtp.host",HOST);
        properties.put("mail.smtp.auth",true);

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, P);
            }
        };

        return Session.getDefaultInstance(properties,authenticator);
    }

    public static void sendEmail(String toEmail,String content){
        Session session = getSession();
        try{
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(FROM));
            InternetAddress[] internetAddress = {new InternetAddress(toEmail)};
            msg.setRecipients(Message.RecipientType.TO,internetAddress);
            msg.setSubject("账号激活邮件");
            msg.setSentDate(new Date());
            msg.setContent(content , "text/html;charset=utf-8");

            Transport.send(msg);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
