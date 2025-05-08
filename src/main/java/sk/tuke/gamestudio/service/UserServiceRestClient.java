package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class UserServiceRestClient implements UserService{
    private final String url = "http://localhost:8080/api/connect";

    @Autowired
    RestTemplate restTemplate;

    @Override
    public int doesUsernameExist(String username) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "/check/" + username)
                .queryParam("username", username);
        return restTemplate.getForObject(builder.toUriString(), Integer.class);
    }


    @Override
    public boolean register(String username, String password) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "/register/" + username + "/" + password)
                .queryParam("username", username)
                .queryParam("password", password);
        return restTemplate.getForObject(builder.toUriString(), Boolean.class);
    }

    @Override
    public boolean login(String username, String password) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "/login/" + username + "/" + password)
                .queryParam("username", username)
                .queryParam("password", password);
        return restTemplate.getForObject(builder.toUriString(), Boolean.class);
    }

}
