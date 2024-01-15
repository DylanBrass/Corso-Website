package com.corso.springboot.Review_Subdomain.datalayer;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@Builder
@Table(name = "Reviews")
@AllArgsConstructor
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public ReviewIdentifier reviewId;

    public String userId;

    public String customerFullName;

    public LocalDate reviewDate;

    public String message;

    public int reviewRating;

    public boolean pinned;


}
