package gov.uscis.vis.api.controller;

import gov.uscis.vis.api.service.MetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cedennis on 3/13/17.
 */
@Controller
public class MetricsController {

    @Autowired
    private MetricsService metricsService;

    @RequestMapping("/analyzeMetrics")
    public String analyzeMetrics(
            @RequestParam(value="boardIdList", required=false) List<Integer> boardIdList,
            Model model) {
        if (boardIdList != null){
            model.addAttribute("metricsDto", metricsService.analyzeBoard(boardIdList).toString());
        } else {
            boardIdList = new ArrayList<Integer>();
            boardIdList.add(1332);
            model.addAttribute("metricsDto", metricsService.analyzeBoard(boardIdList).toString());
        }
        return "metricsPage";
    }
}