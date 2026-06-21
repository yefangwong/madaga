package com.dhf.energy;

import com.dhf.hrsys.annotation.Layout;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用來取得國內的用電和供電資訊
 * @author yfwong
 * @date 2023/07/24
 */
@Controller
@RequestMapping("/energy")
@Layout(Layout.none)
public class EnergyController {

    @RequestMapping("/index")
    public String index(Model model){
        model.addAttribute("title", "能源數據監測 (Energy)");
        model.addAttribute("description", "我們正在開發台灣用電與永續能源供應現況的實時監測儀表板，提供直觀、精準的低碳能效分析。");
        model.addAttribute("switchUrl", "/energy/index_en");
        return "coming_soon_zh";
    }

    @RequestMapping("/index_en")
    public String indexEn(Model model){
        model.addAttribute("title", "Energy Monitor");
        model.addAttribute("description", "We are developing a real-time monitor dashboard for power consumption and sustainable energy supply, offering intuitive and accurate low-carbon analytics.");
        model.addAttribute("switchUrl", "/energy/index");
        return "coming_soon";
    }
}
