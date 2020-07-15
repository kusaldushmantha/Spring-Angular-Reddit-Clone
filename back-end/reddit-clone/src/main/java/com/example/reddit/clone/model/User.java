package com.example.reddit.clone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long userId;

    @NotBlank( message = "Username is required" )
    @Column( unique = true )
    private String username;

    @NotBlank( message = "Password is required" )
    private String password;

    @Email( message = "Email should be valid" )
    @NotBlank( message = "Email is required" )
    private String email;

    private Instant createdDate;

    private boolean enabled;
}
