package org.hyidi.dvdservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.hyidi.dvdservice.model.Dvd;
import org.hyidi.dvdservice.service.DvdInstanceService;
import org.hyidi.dvdservice.service.DvdService;
import org.hyidi.dvdservice.service.RentalService;

@Controller
@RequiredArgsConstructor
public class PageController {
    private final DvdService dvdService;
    private final DvdInstanceService dvdInstanceService;
    private final RentalService rentalService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("dvds", dvdService.getAllDvds());
        model.addAttribute("instances", dvdInstanceService.getAvailableInstances());
        model.addAttribute("rentals", rentalService.getAllRentals());
        return "home";
    }

    @PostMapping("/dvd/add")
    public String addDvd(@RequestParam String title,
                         @RequestParam String description,
                         @RequestParam Double dailyPrice) {
        Dvd dvd = new Dvd();
        dvd.setTitle(title);
        dvd.setDescription(description);
        dvd.setDailyPrice(dailyPrice);
        dvdService.addDvd(dvd);
        return "redirect:/";
    }

    @PostMapping("/instance/add")
    public String addInstance(@RequestParam Long dvdId) {
        Dvd dvd = dvdService.getAllDvds()
                .stream()
                .filter(d -> d.getId().equals(dvdId))
                .findFirst()
                .orElseThrow();
        dvdInstanceService.addDvdInstance(dvd);
        return "redirect:/";
    }

    @PostMapping("/rent")
    public String rentDvd(@RequestParam Long instanceId,
                          @RequestParam String renterName,
                          @RequestParam int rentalDays) {
        rentalService.rentDvd(instanceId, renterName, rentalDays);
        return "redirect:/";
    }

    @PostMapping("/return")
    public String returnDvd(@RequestParam Long rentalId) {
        rentalService.returnDvd(rentalId);
        return "redirect:/";
    }
}
