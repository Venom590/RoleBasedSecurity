package de.unileipzig.bis.rbs.testApp.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collection;

/**
 * The abstract controller as superclass for all other controllers.
 * Child controller need to be annotated with '@Controller'.
 *
 * @author Stephan Kemper
 */
public abstract class AbstractController {

    /**
     * Obtains the currently authenticated principal, or an authentication request token.
     *
     * @return the current authentication
     */
    protected Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * @return true if user is admin
     */
    @ModelAttribute("isAdmin")
    public boolean isAdmin() {
        Authentication auth = getAuthentication();
        if (auth == null) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        if (authorities == null) {
            return false;
        }
        return authorities.toString().contains("admin");
    }

    /**
     * @return true if user is logged in
     */
    @ModelAttribute("isLogged")
    public boolean isLogged() {
        Authentication auth = getAuthentication();
        if (auth == null) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        if (authorities == null) {
            return false;
        }
        return !authorities.toString().contains("ROLE_ANONYMOUS");
    }

}
