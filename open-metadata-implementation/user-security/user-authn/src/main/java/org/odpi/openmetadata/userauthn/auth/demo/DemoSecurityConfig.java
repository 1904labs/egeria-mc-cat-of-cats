/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userauthn.auth.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * Properties used to configure the demo users authentication service.
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(value = "authentication.source", havingValue = "demo")
public class DemoSecurityConfig {

    @Autowired
    @Qualifier("demoUserDetailsService")
    private UserDetailsService userDetailsService;

    /**
     * Provide information about the demo (inmemory) authentication service (user directory).
     *
     * @return the authentication provider component
     */
    @Bean
    public AuthenticationProvider getDemoAuthenticationProvider()
    {
        var authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());

        return authProvider;
    }

//    protected AuthenticationExceptionHandler getAuthenticationExceptionHandler() {
//        return BadCredentialsException.class::isInstance;
//    }
}
