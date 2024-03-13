package com.thetechmaddy.ecommerce.controllers;

import com.thetechmaddy.ecommerce.models.responses.PingResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/ping")
public class PingController extends BaseController {

    @GetMapping
    public PingResponse ping() {
        return new PingResponse("success");
    }

}
