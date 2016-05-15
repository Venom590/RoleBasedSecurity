package de.unileipzig.bis.rbs.testApp.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

/**
 * Global exception handling
 *
 * @author Stephan Kemper
 */
@ControllerAdvice
public class GlobalExceptionHandlingControllerAdvice{

    public GlobalExceptionHandlingControllerAdvice() {
    }

    @ExceptionHandler({SQLException.class})
    public String sqlError(Model model, Exception e) {
        model.addAttribute("error", e.toString());
        return "error/sql";
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public String constraintError(Model model, Exception e) {
        model.addAttribute("error", e.toString());
        return "error/data-integrity";
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public String Error(Model model, Exception e) {
        model.addAttribute("error", e.toString());
        return "error/error";
    }

}
