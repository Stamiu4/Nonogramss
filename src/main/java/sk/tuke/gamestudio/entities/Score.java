package sk.tuke.gamestudio.entities;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name = "score")
@Data
public class Score implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String game;

    @Column(nullable = false)
    String player;

    @Column(nullable = false)
    private int score;


    @Column(nullable = false)
    @JsonProperty("playedOn")
    private ZonedDateTime playedOn;
}
