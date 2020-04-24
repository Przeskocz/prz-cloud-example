package prz.cloud.example.cutting.stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import prz.cloud.example.cutting.stock.DAO.InputData;
import prz.cloud.example.cutting.stock.DTO.GASettings;
import prz.cloud.example.cutting.stock.DTO.IndexFormDTO;
import prz.cloud.example.cutting.stock.algorithm.IGenAlgorithm;


import javax.servlet.http.HttpSession;
import javax.validation.Valid;


@Controller
public class MainController {

    @Autowired
    private IGenAlgorithm genAlgorithm;


    @GetMapping("/")
    public String getStartPage(Model model, HttpSession session){
        if (session.getAttribute("FORM") == null) {
            session.setAttribute("FORM", new IndexFormDTO());
        }

        IndexFormDTO form = (IndexFormDTO) session.getAttribute("FORM");

        model.addAttribute("indexFormDto", form);
        return "index";
    }

    @PostMapping("/")
    public ModelAndView getOptimizedResult(@ModelAttribute("indexFormDTO") @Valid IndexFormDTO indexFormDTO, HttpSession session) {
        long millisActualTime = System.currentTimeMillis();
        String results = "";
        ModelAndView modelAndView = new ModelAndView("index");
        if (indexFormDTO != null) {
            session.setAttribute("FORM", indexFormDTO);
            modelAndView.addObject("indexFormDto", indexFormDTO);
            InputData inputData = indexFormDTO.convertToDTO();
            GASettings settings = indexFormDTO.getSettings();
            if (inputData != null)
                results = genAlgorithm.getResult(inputData, settings);

        } else {
            modelAndView.addObject("indexFormDto", new IndexFormDTO());
        }
        long executionTime = System.currentTimeMillis() - millisActualTime;

        results = "Całkowity czas programu: " + executionTime + " ms.<br>" + results;
        modelAndView.addObject("algorithmResult", results);
        return modelAndView;
    }

    @GetMapping("/restore")
    public String getRestoreSettings(Model model, HttpSession session){
        IndexFormDTO form = (IndexFormDTO) session.getAttribute("FORM");
        if (form != null && form.getSettings() != null) {
            form.getSettings().resetSettings();
            session.setAttribute("FORM", form);
            model.addAttribute("indexFormDto", form);
        } else {
            model.addAttribute("indexFormDto", new IndexFormDTO());
            session.setAttribute("FORM", new IndexFormDTO());
        }

        return "redirect:/";
    }
}
