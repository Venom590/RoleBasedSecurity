package de.unileipzig.bis.rbs.testApp.controllers;

import de.unileipzig.bis.rbs.testApp.RoleObjectConsistencyException;
import de.unileipzig.bis.rbs.testApp.model.Author;
import de.unileipzig.bis.rbs.testApp.model.Role;
import de.unileipzig.bis.rbs.testApp.model.User;
import de.unileipzig.bis.rbs.testApp.service.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * The author controller to manage authors in this application.
 *
 * @author Stephan Kemper
 */
@Controller
@RequestMapping("/manage/author")
public class AuthorController extends AbstractController {

    /**
     * The author repository to persist changes
     */
    @Autowired
    private AuthorRepository authorRepository;

    /**
     * Get all authors
     *
     * @param model the ui model
     * @return the view
     */
    @RequestMapping(method = RequestMethod.GET)
    public String authors(Model model) {
        User user = this.getCurrentUser();
        Iterable<Author> allAuthors = authorRepository.findAll();
        List<Author> authors = new ArrayList<>();
        for (Author author : allAuthors) {
            if (user.canRead(author)) {
                authors.add(author);
            }
        }
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
        if (canRead(author)) {
            model.addAttribute("author", author);
            return "author/author";
        } else {
            setHintMessage(new HintMessage(HintMessage.HintStatus.danger, "You do not have read rights for object with id: " + authorid));
            return "redirect:/manage/author";
        }
    }

    /**
     * Create new author (show mask)
     *
     * @param model the ui model
     * @return the author creation mask
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        if (isAdmin()) {
            Iterable<Role> roles = roleRepository.findAll();
            model.addAttribute("roles", roles);
            return "author/create";
        } else {
            setHintMessage(new HintMessage(HintMessage.HintStatus.danger, "You do not have read rights for objects here."));
            return "redirect:/manage/author";
        }
    }

    /**
     * Create new author (action)
     *
     * @param name the name
     * @param canReadRoleIds the role ids to set canRead
     * @param canWriteRoleIds the role ids to set canWrite
     * @param canDeleteRoleIds the role ids to set canDelete
     * @return the view (redirect)
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String doCreate(@RequestParam(value = "name") String name,
                           @RequestParam(value = "can_read[]", required = false) Long[] canReadRoleIds,
                           @RequestParam(value = "can_write[]", required = false) Long[] canWriteRoleIds,
                           @RequestParam(value = "can_delete[]", required = false) Long[] canDeleteRoleIds) {
        if (isAdmin()) {
            Author author = new Author(name);
            try {
                this.setRoleObjectsToObject(author, canReadRoleIds, canWriteRoleIds, canDeleteRoleIds);
                authorRepository.save(author);
            } catch (RoleObjectConsistencyException e) {
                setHintMessage(new HintMessage(HintMessage.HintStatus.danger, e.getMessage()));
            }
        } else {
            setHintMessage(new HintMessage(HintMessage.HintStatus.danger, "You do not have read rights for objects here."));
        }
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
        if (canWrite(author)) {
            model.addAttribute("author", author);
            Iterable<Role> roles = roleRepository.findAll();
            model.addAttribute("roles", roles);
            return "author/edit";
        } else {
            setHintMessage(new HintMessage(HintMessage.HintStatus.danger, "You do not have write rights for object with id: " + authorid));
            return "redirect:/manage/author";
        }
    }

    /**
     * Edit existing author (action)
     *
     * @param authorid the author id
     * @param name the new name
     * @param canReadRoleIds the role ids to set canRead
     * @param canWriteRoleIds the role ids to set canWrite
     * @param canDeleteRoleIds the role ids to set canDelete
     * @return the view (redirect)
     */
    @RequestMapping(value = "/edit/{authorid}", method = RequestMethod.POST)
    public String doEdit(@PathVariable String authorid,
                         @RequestParam(value = "name") String name,
                         @RequestParam(value = "can_read[]", required = false) Long[] canReadRoleIds,
                         @RequestParam(value = "can_write[]", required = false) Long[] canWriteRoleIds,
                         @RequestParam(value = "can_delete[]", required = false) Long[] canDeleteRoleIds) {
        Author author = authorRepository.findOne(Long.valueOf(authorid));
        if (canWrite(author)) {
            author.setName(name);
            try {
                setRoleObjectsToObject(author, canReadRoleIds, canWriteRoleIds, canDeleteRoleIds);
                authorRepository.save(author);
            } catch (RoleObjectConsistencyException e) {
                setHintMessage(new HintMessage(HintMessage.HintStatus.danger, e.getMessage()));
            }

            return "redirect:/manage/author/" + authorid;
        } else {
            setHintMessage(new HintMessage(HintMessage.HintStatus.danger, "You do not have write rights for object with id: " + authorid));
            return "redirect:/manage/author";
        }
    }

    /**
     * Delete existing author
     *
     * @param authorid the author id
     * @return the view (redirect)
     */
    @RequestMapping(value = "/delete/{authorid}", method = RequestMethod.GET)
    public String delete(@PathVariable String authorid) {
        if (canDelete(Long.valueOf(authorid))) {
            authorRepository.delete(Long.valueOf(authorid));
        } else {
            setHintMessage(new HintMessage(HintMessage.HintStatus.danger, "You do not have delete rights for object with id: " + authorid));
        }
        return "redirect:/manage/author";
    }

}