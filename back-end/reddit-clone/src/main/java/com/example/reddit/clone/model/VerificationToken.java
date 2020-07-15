package com.example.reddit.clone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity( name = "verification_token" )
public class VerificationToken
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    private String token;

    @OneToOne( fetch = FetchType.LAZY )
    private User user;

    private Instant createdDate;
}
