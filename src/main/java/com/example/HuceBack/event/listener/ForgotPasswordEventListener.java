package com.example.HuceBack.event.listener;

import com.example.HuceBack.entity.User;
import com.example.HuceBack.event.ForgotPasswordCompleteEvent;
import com.example.HuceBack.event.RegistrationCompleteEvent;
import com.example.HuceBack.repository.ForgotPasswordRepository;
import com.example.HuceBack.repository.VerifyEmailRepository;
import com.example.HuceBack.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;
@Slf4j
@Service
public class ForgotPasswordEventListener implements ApplicationListener<ForgotPasswordCompleteEvent> {
    @Autowired
    private AuthService userService;
    @Autowired
    private JavaMailSender mailSender;
    private User theUser;
    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;

    public ForgotPasswordEventListener() {
    }

    @Override
    public void onApplicationEvent(ForgotPasswordCompleteEvent event) {
        // 1. Get the newly registered user
        theUser = event.getUser();
        //2. Create a verification token for the user
        String verificationToken = String.valueOf(generateRandomNumber());
        //3. Save the verification token for the user
        while (true) {
            if (!forgotPasswordRepository.existsByToken(verificationToken)) {
                userService.saveForgotPassword(theUser, verificationToken);
                break;
            } else {
                verificationToken = String.valueOf(generateRandomNumber());
            }
        }
        //4 Build the verification url to be sent to the user
        //5. Send the email.
        try {
            sendVerificationEmail(verificationToken);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("Click the link to verify your registration :  {}", verificationToken);
    }
    public void sendVerificationEmail(String verificationToken) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email xác nhận quên mật khẩu";
        String senderName = "Trung tâm hỗi trợ người dùng";
        String mailContent = "<p> Xin chào, "+ theUser.getName()+ ", </p>"+
                "<p >Mã OTP của bạn là: <p style=\"color:black;font-size: 20px;font-weight: 700;\"> " + verificationToken +"</p>  </p>"+
                "<p>Mã OTP này sẽ hết hạn sau 5 phút, Vui lòng không cung cấp mã này cho người khác </p>"+
                "<p> Cảm ơn bạn <br> Dịch vụ cổng đăng ký người dùng";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("khuatkhanh121202@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
    int generateRandomNumber() {
        Random random = new Random();
        return 1000 + random.nextInt(9000); // Số ngẫu nhiên từ 1000 đến 9999
    }
}
