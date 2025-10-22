package com.alfheim.aflheimcommunity.controller.user.auth;

import com.alfheim.aflheimcommunity.security.details.UserDetailsImpl;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String getLoginForm(Model model) {

        // Auth. Filter
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("isAuthenticated", false);
            return "/users/auth/login_page";
        }

        return "redirect:/profile";
    }
}
package com.alfheim.aflheimcommunity.controller.user.auth;

import com.alfheim.aflheimcommunity.dto.user.UserRegistrationForm;
import com.alfheim.aflheimcommunity.exception.user.UserUnauthorizedRequestException;
import com.alfheim.aflheimcommunity.service.user.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
        import org.thymeleaf.standard.processor.StandardHrefTagProcessor;

import jakarta.validation.Valid;

@Controller
public class RegisterController {

    @Autowired
    private RegistrationService registrationService;

    @GetMapping("/register")
    public String getRegistrationForm(Model model) {

        // Auth. Filter
        UserRegistrationForm registrationForm = new UserRegistrationForm();
        model.addAttribute("registrationForm", registrationForm);
        return "/users/auth/register_page";
    }

    @PostMapping("/register")
    public ResponseEntity<Object> createAccount(@Valid @ModelAttribute("registrationForm") UserRegistrationForm registrationForm,
                                                BindingResult result,
                                                Model model) {

        if (result.hasErrors()) {
            // Fields input errors
            model.addAttribute("registrationForm", registrationForm);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You have made a bad request");
        }

        System.out.println("REGISTRATION PROCEEDING...");
        int responseStatus = registrationService.registerUser(registrationForm);

        return ResponseEntity.status(HttpStatus.OK).body("DONE");
    }

    @GetMapping("/register/usernameCheck")
    @ResponseBody
    public ResponseEntity<Object> doUsernameCheck(@RequestParam("username") String username) {

        boolean result = registrationService.isUsernameUnique(username);

        return ResponseEntity.ok(result);
    }

}