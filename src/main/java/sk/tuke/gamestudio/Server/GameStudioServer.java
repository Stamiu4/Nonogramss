package sk.tuke.gamestudio.Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.service.*;


//@SpringBootApplication
@Configuration

@SpringBootApplication(scanBasePackages = "sk.tuke.gamestudio")
@EntityScan(basePackages = "sk.tuke.gamestudio.entities")
public class GameStudioServer {

    public static void main(String[] args) {
        SpringApplication.run(GameStudioServer.class, args);
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceJPA();
    }
    @Bean
    public CommentService commentService() {
        return new CommentServiceJPA();
    }
    @Bean
    public RatingService ratingService() {
        return new RatingServiceJPA();
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public UserService userService() {
        return new UserServiceJPA();
    }
    @Bean
    public PuzzleProgressService puzzleProgressService() {
        return new PuzzleProgressServiceJPA();
    }

}
