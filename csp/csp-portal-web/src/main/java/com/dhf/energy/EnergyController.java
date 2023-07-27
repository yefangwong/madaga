package com.dhf.energy;

import com.dhf.hrsys.annotation.Layout;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用來取得國內的用電和供電資訊
 * @author yfwong
 * @date 2023/07/24
 */
@Controller
@RequestMapping("/energy")
@Layout("frontEnd")
public class EnergyController {

    @RequestMapping("/index")
    public String index(){
        return "/energy/index";
    }
}
