package sk.tuke.gamestudio.entities;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Table(name = "comment")
@Data
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String game;

    @Column(nullable = false)
    private String player;

    @Column(nullable = false)
    private String comment;


    @Column(name = "commented_on", nullable = false, updatable = false)
    private LocalDateTime commentedOn;
}
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.Id;
//import java.io.Serializable;
//import java.util.Date;

//@Entity
//public class Comment implements Serializable {
//    @Id
//    @GeneratedValue
//    private int ident;
//
//    private String game;
//
//    private String player;
//
//    private String comment;
//
//    @Column(name = "commentedon", nullable = false)
//    private Date commentedOn;
//
//    public Comment(){}
//
//    public Comment(String game, String player, String comment, Date commentedOn) {
//        this.game = game;
//        this.player = player;
//        this.comment = comment;
//        this.commentedOn = commentedOn;
//    }
//
//    public int getIdent() {
//        return ident;
//    }
//
//    public void setIdent(int ident) {
//        this.ident = ident;
//    }
//
//    public String getGame() {
//        return game;
//    }
//
//    public void setGame(String game) {
//        this.game = game;
//    }
//
//    public String getPlayer() {
//        return player;
//    }
//
//    public void setPlayer(String player) {
//        this.player = player;
//    }
//
//    public String getComment() {
//        return comment;
//    }
//
//    public void setComment(String comment) {
//        this.comment = comment;
//    }
//
//    public Date getCommentedOn() {
//        return commentedOn;
//    }
//
//    public void setCommentedOn(Date commentedOn) {
//        this.commentedOn = commentedOn;
//    }
//
//    @Override
//    public String toString() {
//        return "Comment{" +
//                "game='" + game + '\'' +
//                ", player='" + player + '\'' +
//                ", comment=" + comment +
//                ", commentedOn=" + commentedOn +
//                '}';
//    }
//}