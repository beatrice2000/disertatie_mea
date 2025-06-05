/* package com.beatrice.book.auth;

import com.beatrice.book.email.EmailService;
import com.beatrice.book.email.EmailTemplateName;
import com.beatrice.book.role.RoleRepository;
import com.beatrice.book.security.JwtService;
import com.beatrice.book.user.Token;
import com.beatrice.book.user.TokenRepository;
import com.beatrice.book.user.User;
import com.beatrice.book.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

//@Service
@RequiredArgsConstructor
public class AuthenticationService {
/*
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void register(RegistrationRequest request) throws MessagingException {
        var userRole = roleRepository.findByName("USER")
                // todo - improve exception handling
                .orElseThrow(() -> new IllegalArgumentException("ROLE USER must be initialized first."));
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        //dupa ce generez token ul il salvez in baza de date
        userRepository.save(user);
        sendValidationEmail(user);
        ///trebuie sa genaram si sa salvam tokenul de autentificare apoi sa trimitem email ul - am creat metoda mai jos
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        // trimit  email userului
        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );

    }

    // returnam un String pentru ca token ul e de tip String
    private String generateAndSaveActivationToken(User user) {
        // generate a token
        String generatedToken = generateActivationCode(9);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(30)) //expira in 30 de minute
                .user(user) //setam userul
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        // generam random si secure un token
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length()); // 0..9
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        // deja userul s-a authentificat corect
        var claims = new HashMap<String, Object>();
        // userul nu il mai iau din baza de date ma folosesc de auth si fac fetch
        var user = ((User)auth.getPrincipal());
        claims.put("fullName", user.fullName());
        var jwtToken = jwtService.generateToken(claims, user);
        // returnez obiectul de autenetificare/raspunsul autentificarii i-am dat si Token ul
        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }

    // @Transactional
    public void activateAccount(String token) throws MessagingException {
        //extrag token ul din baza de date
        Token savedToken = tokenRepository.findByToken(token)
                // todo exception has to be defined
                .orElseThrow(() -> new RuntimeException("Token not found"));
        // daca token ul a expirat trimit mail ul de validare din nou
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("The previous token has expired. A new token has been sent to your email address.");
        }
        //daca token ul nu e expirat
        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }

}
*/