package prz.cloud.example.cutting.stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import prz.cloud.example.cutting.stock.DAO.InputData;
import prz.cloud.example.cutting.stock.DTO.IndexFormDTO;
import prz.cloud.example.cutting.stock.algorithm.IGenAlgorithm;


import javax.validation.Valid;


@Controller
public class MainController {

    @Autowired
    private IGenAlgorithm genAlgorithm;


    @GetMapping("/")
    public String getStartPage(Model model){
        model.addAttribute("indexFormDto", new IndexFormDTO());
        return "index";
    }

    @PostMapping("/")
    public ModelAndView getOptimizedResult(@ModelAttribute("indexFormDTO") @Valid IndexFormDTO indexFormDTO) {

        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("indexFormDto", new IndexFormDTO());
        if (indexFormDTO != null) {
            InputData inputData = indexFormDTO.convertToDTO();
            if (inputData != null) {
                String results = genAlgorithm.getResult(inputData);
                modelAndView.addObject("algorithmResult", results);
            }
        }

        return modelAndView;
    }
}
