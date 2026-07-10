package com.example.customerservice.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("securityExpression")
public class SecurityExpression {

  public boolean isCurrentUser(Long customerId, Authentication authentication) {
    return customerId != null
        && authentication != null
        && String.valueOf(customerId).equals(authentication.getName());
  }
}
