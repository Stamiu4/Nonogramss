package sk.tuke.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import sk.tuke.gamestudio.UI.NanogramChooserUI;
import sk.tuke.gamestudio.service.*;

import javax.swing.*;
import java.awt.*;

    @SpringBootApplication
    @ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
            pattern = "sk.tuke.gamestudio.controllers.*"))
    public class TukeSpringBootApplication {

        public static void main(String[] args) {
            System.setProperty("java.awt.headless", "false");

            new SpringApplicationBuilder(TukeSpringBootApplication.class)
                    .web(WebApplicationType.NONE)
                    .run(args);
        }

        @Bean
        public CommandLineRunner runner() {
            return args -> {
                // Запускаем UI только если не headless
                if (!GraphicsEnvironment.isHeadless()) {
                    SwingUtilities.invokeLater(NanogramChooserUI::new);
                } else {
                    System.out.println("Headless mode detected – GUI not started.");
                }
            };
        }

    @Bean
    RatingService ratingService() {
        return new RatingServiceRestClient();
    }

    @Bean
    ScoreService scoreService() {
        return new ScoreServiceRestClient();
    }

    @Bean
    CommentService commentService() {
        return new CommentServiceRestClient();
    }
}
