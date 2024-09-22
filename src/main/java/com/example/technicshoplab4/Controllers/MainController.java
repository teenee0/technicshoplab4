package com.example.technicshoplab4.Controllers;

import com.example.technicshoplab4.Models.Warehouse;
import com.example.technicshoplab4.Repository.WarehouseRepository;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
public class MainController {
    private final WarehouseRepository warehouseRepository;

    public MainController(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }
//        private final FilmsRepository filmsRepository;
//
//        public MainController(FilmsRepository filmsRepository) {
//            this.filmsRepository = filmsRepository;
//        }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная Страница");
        return "greeting";
    }
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Страница входа");
        return "login";
    }
    @GetMapping("/support")
    public String support(Model model) {
        model.addAttribute("title", "Поддержка");
        return "support";
    }
    @GetMapping("/warehouse")
    public String Warehouse(Model model) {
        Iterable<Warehouse> items = warehouseRepository.findAll();
        model.addAttribute("title","Страница склада");
        model.addAttribute("items", items);
        return "warehouse";
    }
    @GetMapping("/additem")
    public String addItem(Model model) {
        model.addAttribute("title","Страница добавления фильма");
        return "addItem";
    }

    @GetMapping("/warehouse/{id}")
    public String updateitem(@PathVariable long id, Model model) {
        if (!warehouseRepository.existsById(id)){
            return "redirect:/warehouse";
        }
        Optional<Warehouse> item = warehouseRepository.findById(id);
        ArrayList<Warehouse> res = new ArrayList<>();
        item.ifPresent(res::add);
        model.addAttribute("items", res);
        model.addAttribute("title", "Страница редактирования");
        return "itemDetails";

    }

    @GetMapping("/warehouse/filter")
    public String searchItems(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String group,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate importDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate exportDate,
            @RequestParam(required = false, defaultValue = "asc") String sortByDate,
            Model model) {
        System.out.println("Тип: " + type);
        System.out.println("Группа: " + group);
        System.out.println("Дата импорта: " + importDate);
        System.out.println("Дата экспорта: " + exportDate);
        if (type.isEmpty()) {
            type = null;
        }
        if (group.equals("Выберите тип")) {
            group = null;
        }


        Sort.Direction sortDirection = sortByDate.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sortBy = Sort.by(sortDirection, "importDate");

        List<Warehouse> items;


        if (type != null || group != null || importDate != null || exportDate != null) {
            items = warehouseRepository.findByParams(type, group, importDate, exportDate, sortBy);
        } else {
            items = warehouseRepository.findAll(sortBy);
        }

        model.addAttribute("items", items);

        return "warehouse";
    }




    @GetMapping("/warehouse/stats")
    public String stats(Model model) {
        List<Object[]> stats = warehouseRepository.findItemIssueStats();

        List<String> dates = new ArrayList<>();
        List<Long> counts = new ArrayList<>();

        for (Object[] row : stats) {
            dates.add(row[0].toString());
            counts.add((Long) row[1]);
        }

        model.addAttribute("dates", dates);
        model.addAttribute("counts", counts);
        return "item_stats";
    }



    @PostMapping("/additem")
    public String addItem(
            @RequestParam String type,
            @RequestParam String group,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate importDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate exportDate,
            @RequestParam String driverFullName) {

        Warehouse item = new Warehouse();
        item.setType(type);
        item.setGroup(group);
        item.setImportDate(importDate);
        if (exportDate != null) {
            item.setExportDate(exportDate);
        }
        item.setDriverFullName(driverFullName);

        warehouseRepository.save(item);

        return "redirect:/warehouse";
    }

    @PostMapping("/warehouse/save")
    public String saveFils(
            @RequestParam("id") long id,
            @RequestParam String type,
            @RequestParam String group,
            @RequestParam LocalDate importDate,
            @RequestParam(required = false) LocalDate exportDate,
            @RequestParam String driverFullName,
            Model model) {
        Warehouse item = warehouseRepository.findById(id).orElseThrow();
        item.setType(type);
        item.setGroup(group);
        item.setImportDate(importDate);
        item.setDriverFullName(driverFullName);
        if (exportDate != null) {
            item.setExportDate(exportDate);
        }else {
            item.setExportDate(null);
        }
        warehouseRepository.save(item);
        return "redirect:/warehouse";

    }

    @PostMapping("/warehouse/{id}/remove")
    public String removeitem(@PathVariable long id, Model model) {
        Warehouse item = warehouseRepository.findById(id).orElseThrow();
        warehouseRepository.delete(item);
        return "redirect:/warehouse";
    }
}
