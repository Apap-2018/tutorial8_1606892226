package com.apap.tutorial6.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.apap.tutorial6.service.UserRoleService;
import com.apap.tutorial6.model.UserRoleModel;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


@Controller
@RequestMapping("/user")
public class UserRoleController {
    @Autowired
    private UserRoleService userService;

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    private String addUserSubmit(@ModelAttribute UserRoleModel user, Model model) {
        String password = user.getPassword();
        List<String> errorList = validatePassword(password);

        if (errorList.size() > 0) {
            model.addAttribute("errorFlagTambahUser", true);
            model.addAttribute("errorValueTambahUser", errorList);
        } else {
            userService.addUser(user);
        }
        return "home";
    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    private String updatePasswordSubmit(
            Model model,
            @RequestParam("passwordLama") String passwordLama,
            @RequestParam("passwordBaru") String passwordBaru,
            @RequestParam("passwordBaruKonfirmasi") String passwordBaruKonfirmasi
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserRoleModel user = userService.findByUsername(authentication.getName());
        PasswordEncoder token = new BCryptPasswordEncoder();

        List<String> errorList = validatePassword(passwordBaru);
        if (errorList.size() > 0) {
            model.addAttribute("errorListFlagUpdatePassword", true);
            model.addAttribute("errorListValueUpdatePassword", errorList);
            return "home";
        }

        boolean validitasPasswordLama = token.matches(passwordLama, user.getPassword());
        if (!validitasPasswordLama) {
            model.addAttribute("errorFlagUpdatePassword", true);
            model.addAttribute("errorValueUpdatePassword", "Password lama yang anda masukkan salah");
        } else {
            if (passwordBaru.equals(passwordBaruKonfirmasi)) {
                userService.editPassword(passwordBaru, authentication.getName());
            } else {
                model.addAttribute("errorFlagUpdatePassword", true);
                model.addAttribute("errorValueUpdatePassword", "Password baru dan konfirmasi password baru berbeda");
            }
        }
        return "home";
    }

    private List<String> validatePassword(String password) {
        List<String> resp = new ArrayList<>();
        if (password.length() < 8) {
            resp.add("Minimal jumlah karakter untuk password adalah 8");
        }
        if (!Pattern.compile("[0-9]").matcher(password).find()) {
            resp.add("Minimal password mengandung 1 angka");
        }
        if (!Pattern.compile("[a-zA-Z]").matcher(password).find()) {
            resp.add("Minimal password mengandung 1 huruf");
        }
        return resp;
    }
}