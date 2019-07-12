package team.ag.knmap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author weitangzhao
 **/
@Controller
public class IndexController {
    @GetMapping(value = {"/", "index"})
    public String index() {
        return "index";
    }
}
