package com.beatrice.book.config;

import com.beatrice.book.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // aici obtin auditor curent
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // acesta e obiectul de autenificare
        // verificare pentru autentificare
        if(authentication == null || // daca userul nu e autentificat
                !authentication.isAuthenticated() ||
                    authentication instanceof AnonymousAuthenticationToken) {
                return Optional.empty();
        }
  //      User userPrincipal = (User) authentication.getPrincipal();
        return Optional.ofNullable(authentication.getName());
    }
}
