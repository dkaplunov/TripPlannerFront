package org.kdalib.trip_planner.controllers;

import lombok.extern.java.Log;
import org.kdalib.trip_planner.model.security.Users;
import org.kdalib.trip_planner.service.JwtHelper;
import org.kdalib.trip_planner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Log
@RestController
@RequestMapping("/api")
public class AutentificationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    JwtHelper jwtHelper;

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public ResponseEntity<?> login(HttpServletRequest request, @RequestBody Users user) { //@RequestParam Optional<String> username, @RequestParam Optional<String> password

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUserName(),
                        user.getPassword()
                )
        );

        return ResponseEntity.ok(jwtHelper.createToken(userService.findUserByName(user.getUserName()).getId()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleError(HttpServletRequest req, Exception ex) {
        log.info("Request: " + req.getRequestURL() + " raised " + ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
