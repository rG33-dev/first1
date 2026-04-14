package com.example.first1.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController

  class HealthCheck {



   @GetMapping("/health")

     fun checkHealth() : String{
      return "Ok" ;

     }



}