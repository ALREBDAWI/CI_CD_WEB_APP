package org.jee.ci_cd_web_app.controller;

import org.jee.ci_cd_web_app.service.RolesClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoleWebController {

    private final RolesClientService apiService;

    public RoleWebController(RolesClientService apiService) {
        this.apiService = apiService;
    }


    @GetMapping("/show-roles")
    public String showRoles(Model model) {
        model.addAttribute("rolesList", apiService.fetchRoles());
        return "rolesPage";
    }
}
