package controller;

import java.util.Map;

import javax.validation.Valid;

import model.user.User;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import backingform.PasswordResetBackingForm;

@RestController
@RequestMapping("/passwordresets")
public class PasswordResetController {
    
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserRepository userRepository;
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> resetPassword(@Valid @RequestBody PasswordResetBackingForm form) {
        User user = userRepository.findUser(form.getUsername());
        if (user == null) {
            return new ResponseEntity<Map<String, Object>>(HttpStatus.NOT_FOUND);
        }

        // Generate a new random alphanumeric password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newPassword = RandomStringUtils.randomAlphanumeric(16);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.updateUser(user, true);

        // Send an email containing the new password to the user
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmailId());
        message.setSubject("BSIS Password reset");
        message.setText("Your password has been reset to \"" + newPassword +
                "\". You will be required to change it next time you log in.");
        mailSender.send(message);

        return new ResponseEntity<Map<String, Object>>(HttpStatus.CREATED);
    }

}
