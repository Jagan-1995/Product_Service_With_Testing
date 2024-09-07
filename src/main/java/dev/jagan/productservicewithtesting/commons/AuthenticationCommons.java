package dev.jagan.productservicewithtesting.commons;

import dev.jagan.productservicewithtesting.dtos.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthenticationCommons {

    private RestTemplate restTemplate;

    public AuthenticationCommons(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public UserDto validateToken(String token){
        // what is the type of request am i going to make?
        ResponseEntity<UserDto> userDtoResponseEntity = restTemplate.postForEntity(
                "http://localhost:8080/users/validate/" + token,
                null,
                UserDto.class
        );
        return userDtoResponseEntity.getBody();
    }
}
