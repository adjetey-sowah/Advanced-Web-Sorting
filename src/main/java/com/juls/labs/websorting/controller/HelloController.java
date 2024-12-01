package com.juls.labs.websorting.controller;

import com.juls.labs.websorting.model.Participant;
import com.juls.labs.websorting.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class HelloController {

    @Autowired
    @Qualifier("part")
    private User participant;

    @GetMapping("/greet")
    public ResponseEntity<Map<String,String>> home(){
        Map<String,String> response = new HashMap<>();
        response.put("message", "Hello world");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping("names")
    public ResponseEntity<User> pname(){
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(participant);
    }
}
