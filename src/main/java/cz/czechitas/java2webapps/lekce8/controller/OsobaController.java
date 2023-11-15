package cz.czechitas.java2webapps.lekce8.controller;

import cz.czechitas.java2webapps.lekce8.entity.Osoba;
import cz.czechitas.java2webapps.lekce8.repository.OsobaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class OsobaController {

    OsobaRepository repository;

    public OsobaController(OsobaRepository repository) {
        this.repository = repository;
    }

    @InitBinder
    public void nullStringBinding(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/")
    public ModelAndView seznam() {
        return new ModelAndView("seznam")
                .addObject("osoby", repository.findAll());
    }

    @GetMapping("/novy")
    public ModelAndView novy() {
        return new ModelAndView("detail")
                .addObject("osoba", new Osoba());
    }

    @PostMapping("/novy")
    public String pridat(@ModelAttribute("osoba") @Valid Osoba osoba, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "detail";
        }
        repository.save(osoba);
        return "redirect:/";
    }

    @GetMapping("/{id:[0-9]+}")
    public ModelAndView detail(@PathVariable long id) {
        return new ModelAndView("detail")
                .addObject("osoba", repository.findById(id).orElseThrow());
    }

    @PostMapping("/{id:[0-9]+}")
    public String ulozit(@PathVariable long id, @ModelAttribute("osoba") @Valid Osoba osoba, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "detail";
        }
        repository.save(osoba);
        return "redirect:/";
    }

    @PostMapping(value = "/{id:[0-9]+}", params = "akce=smazat")
    public String smazat(@PathVariable long id) {
        repository.deleteById(id);
        return "redirect:/";
    }
}
