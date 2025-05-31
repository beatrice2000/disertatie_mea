package com.beatrice.book.auth;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class RegistrationRequest {

    //adaugam niste validari ca NotEmpty
    @NotEmpty(message = "Please insert your firstname.")
    @NotBlank(message = "Please insert your firstname.")
    private String firstname;

    @NotEmpty(message = "Please insert your lastname.")
    @NotBlank(message = "Please insert your lastname.")
    private String lastname;

    @Email(message = "Your email address doesn't have the correct format.")
    @NotEmpty(message = "Please insert your email address.")
    @NotBlank(message = "Please insert your email address.")
    private String email;

    @Size(min = 10, message = "Your password must be at least 10 characters long.")
    @NotEmpty(message = "Please insert your password.")
    @NotBlank(message = "Please insert your password.")
    private String password;

}
