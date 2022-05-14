package ru.gb.thymeleafshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.gb.gbapi.security.AuthenticationUserDto;
import ru.gb.gbapi.security.api.AuthGateway;
import ru.gb.gbapi.security.api.UserGateway;
import ru.gb.gbapi.security.UserDto;
import ru.gb.thymeleafshop.config.SessionScopeBean;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserGateway userGateway;
    private final AuthGateway authGateway;
    private final SessionScopeBean sessionScopeBean;

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {

        model.addAttribute("userDto", AuthenticationUserDto.builder().build());
        return "auth/login-form";
    }

    @PostMapping("/login")
    public String getToken(@Valid AuthenticationUserDto userDto) {

        ResponseEntity<AuthenticationUserDto> login = authGateway.login(userDto);

        if (login.getStatusCode().equals(HttpStatus.OK)){
            SecurityContext context = SecurityContextHolder.getContext();
            AuthenticationUserDto body = login.getBody();
            sessionScopeBean.setName(body.getUsername());
            sessionScopeBean.setToken(body.getToken());
            return "redirect:/product/all";
        }
        return "auth/login-form";
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "auth/registration-form";
    }

    @PostMapping("/register")
    public String handleRegistration(@Valid UserDto userDto, BindingResult bindingResult, Model model) {
        String username = userDto.getUsername();
        log.debug("Process registration username: {}", username);
        if (bindingResult.hasErrors()) {
            return "auth/registration-form";
        }
        ResponseEntity<?> responseEntity = userGateway.handlePost(userDto);

        model.addAttribute("username", username);
        return "auth/registration-confirmation";

    }
}
