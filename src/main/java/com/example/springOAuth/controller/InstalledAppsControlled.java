package com.example.springOAuth.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springOAuth.entity.User;
import com.example.springOAuth.service.CredentialService;
import com.example.springOAuth.service.Apps.CalendarService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/apps")
public class InstalledAppsControlled {

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private CredentialService credentialService;

    @GetMapping("/{appName}")
    public void getMethodName(@PathVariable String appName, HttpServletRequest request,
            HttpServletResponse response) throws IOException, GeneralSecurityException {
        // String redirect = GoogleMeetLinkGen.createMeetingLink(null, request,
        // response);

        String authorizeUrl = calendarService.getAuthorizationUrl(request, response);
        var stratigy = new DefaultRedirectStrategy();
        stratigy.sendRedirect(request, response, authorizeUrl);
    }

    @GetMapping("/installed")
    public ResponseEntity<?> getInstalledApps(@AuthenticationPrincipal User currentUser) {

        var response = credentialService.getInstalledAppHandler(currentUser);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
