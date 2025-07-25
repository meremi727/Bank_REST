package com.example.bankcards.config;

import com.example.bankcards.entity.User;
import com.example.bankcards.enums.UserRole;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FirstAdminCreator implements ApplicationRunner {
    @Value("${admin.login}")
    private String login;

    @Value("${admin.email}")
    private String email;

    @Value("${admin.password}")
    private String password;

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.existsByLogin(login)) {
            log.info("First admin user alredy exists.");
            return;
        }

        try {
            User firstAdmin = new User();
            firstAdmin.setEmail(email);
            firstAdmin.setLogin(login);
            firstAdmin.setFirstName("Admin");
            firstAdmin.setMiddleName("Admin");
            firstAdmin.setEnabled(true);
            firstAdmin.setRole(UserRole.ADMIN);
            firstAdmin.setPassword(encoder.encode(password));
            userRepository.save(firstAdmin);
        } catch (Exception e) {
            log.error("Error while creating first admin user.", e);
        }
    }
}
