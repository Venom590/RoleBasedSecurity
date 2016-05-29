package de.unileipzig.bis.rbs.testApp.controllers;

import de.unileipzig.bis.rbs.testApp.model.Author;
import de.unileipzig.bis.rbs.testApp.model.Role;
import de.unileipzig.bis.rbs.testApp.model.RoleObject;
import de.unileipzig.bis.rbs.testApp.service.AuthorRepository;
import de.unileipzig.bis.rbs.testApp.service.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * The author controller to manage authors in this application.
 *
 * @author Lukas Werner
 */
@Controller
@RequestMapping("/manage/author")
public class AuthorController extends AbstractController {

    /**
     * The author repository to persist changes
     */
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Get all authors
     *
     * @param model the ui model
     * @return the view
     */
    @RequestMapping(method = RequestMethod.GET)
    public String authors(Model model) {
        Iterable<Author> authors = authorRepository.findAll();
        model.addAttribute("authors", authors);
        return "author/all-authors";
    };

    /**
     * Get author by id
     *
     * @param authorid the id
     * @param model the ui model
     * @return the view
     */
    @RequestMapping(value = "/{authorid}", method = RequestMethod.GET)
    public String author(@PathVariable String authorid, Model model) {
        Author author = authorRepository.findOne(Long.valueOf(authorid));
        model.addAttribute("author", author);
        return "author/author";
    }

    /**
     * Create new author (show mask)
     *
     * @return the author creation mask
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        Iterable<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        return "author/create";
    }

    /**
     * Create new author (action)
     *
     * @param name the name
     * @return the view (redirect)
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String doCreate(@RequestParam(value = "name") String name,
                           @RequestParam(value = "can_read[]", required = false) Long[] canReadRoleIds,
                           @RequestParam(value = "can_write[]", required = false) Long[] canWriteRoleIds,
                           @RequestParam(value = "can_delete[]", required = false) Long[] canDeleteRoleIds) {
        Author author = new Author(name);
        this.setRoleObjectsToObject(author, canReadRoleIds, canWriteRoleIds, canDeleteRoleIds);

        authorRepository.save(author);
        return "redirect:/manage/author";
    }

    /**
     * Edit existing author (show mask)
     *
     * @param authorid the author id
     * @param model the ui model
     * @return the view
     */
    @RequestMapping(value = "/edit/{authorid}", method = RequestMethod.GET)
    public String edit(@PathVariable String authorid, Model model) {
        Author author = authorRepository.findOne(Long.valueOf(authorid));
        model.addAttribute("author", author);
        Iterable<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        return "author/edit";
    }

    /**
     * Edit existing author (action)
     *
     * @param authorid the author id
     * @param name the new name
     * @return the view (redirect)
     */
    @RequestMapping(value = "/edit/{authorid}", method = RequestMethod.POST)
    public String doEdit(@PathVariable String authorid,
                         @RequestParam(value = "name") String name,
                         @RequestParam(value = "can_read[]", required = false) Long[] canReadRoleIds,
                         @RequestParam(value = "can_write[]", required = false) Long[] canWriteRoleIds,
                         @RequestParam(value = "can_delete[]", required = false) Long[] canDeleteRoleIds) {
        Author author = authorRepository.findOne(Long.valueOf(authorid));
        author.setName(name);
        this.setRoleObjectsToObject(author, canReadRoleIds, canWriteRoleIds, canDeleteRoleIds);

        authorRepository.save(author);
        return "redirect:/manage/author/" + authorid;
    }

    /**
     * Delete existing author
     *
     * @param authorid the author id
     * @return the view (redirect)
     */
    @RequestMapping(value = "/delete/{authorid}", method = RequestMethod.GET)
    public String delete(@PathVariable String authorid) {
        authorRepository.delete(Long.valueOf(authorid));
        return "redirect:/manage/author";
    }

}