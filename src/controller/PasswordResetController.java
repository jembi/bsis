package controller;

import java.util.Map;

import javax.validation.Valid;

import model.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import repository.UserRepository;
import service.PasswordGenerationService;
import backingform.PasswordResetBackingForm;

@RestController
@RequestMapping("/passwordresets")
public class PasswordResetController {
    
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordGenerationService passwordGenerationService;
    
    @Value("${password.reset.subject}")
    private String passwordResetSubject;
    @Value("${password.reset.message}")
    private String passwordResetMessage;
    
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public void setPasswordGenerationService(PasswordGenerationService passwordGenerationService) {
        this.passwordGenerationService = passwordGenerationService;
    }

    public void setPasswordResetSubject(String passwordResetSubject) {
        this.passwordResetSubject = passwordResetSubject;
    }

    public void setPasswordResetMessage(String passwordResetMessage) {
        this.passwordResetMessage = passwordResetMessage;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> resetPassword(@Valid @RequestBody PasswordResetBackingForm form) {
        User user = userRepository.findUser(form.getUsername());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Generate a new random alphanumeric password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newPassword = passwordGenerationService.generatePassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordReset(true);
        userRepository.updateUser(user, true);

        // Send an email containing the new password to the user
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmailId());
        message.setSubject(passwordResetSubject);
        message.setText(String.format(passwordResetMessage, newPassword));
        mailSender.send(message);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
