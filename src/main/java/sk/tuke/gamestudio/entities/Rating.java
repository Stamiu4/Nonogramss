package sk.tuke.gamestudio.entities;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name = "rating")
@Data
public class Rating implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String game;

    @Column(nullable = false)
    private String player;

    @Column(nullable = false)
    private int rating;


    @Column(name = "rated_on",nullable = false,updatable = false)
    private ZonedDateTime ratedOn;
}
