package com.example.spring.restclient.resttemplate.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class HelloController
{
    // ============================== [Fields] ==============================

    // -------------------- [Private Fields] --------------------

    // ============================== [Construction / Destruction] ==============================

    // -------------------- [Public Construction / Destruction] --------------------

    // ============================== [Getter/Setter] ==============================

    // -------------------- [Private Getter/Setter] --------------------

    // -------------------- [Public Getter/Setter] --------------------

    // ============================== [Methods] ==============================

    // -------------------- [Private Methods] --------------------

    // -------------------- [Public Methods] --------------------

    @GetMapping(path = "/hello",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public Hello get()
    {
        return new Hello("Hello, GET!");
    }

    @PostMapping(path = "/hello",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public Hello post(@RequestBody Hello hello)
    {
        return new Hello("Hello, POST!");
    }

    @GetMapping(path = "/hello/error/{status}",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> get(@PathVariable("status") Integer status)
    {
        return ResponseEntity.status(status).build();
    }
}
