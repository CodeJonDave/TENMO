package com.techelevator.tenmo.services.util;

import com.techelevator.tenmo.model.AuthenticatedUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class Header {


    public static HttpEntity<String> generateHeaders(AuthenticatedUser currentUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }

    public static <T> HttpEntity<T> generateHeadersWithBody(AuthenticatedUser currentUser, T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(body, headers);
    }
}