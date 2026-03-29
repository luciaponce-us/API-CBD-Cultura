package com.tfg.cultura.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dummy")
public class DummyController {

    @GetMapping
    public String getDummyData() {
        return "This is some dummy data from the API!";
    }

}
