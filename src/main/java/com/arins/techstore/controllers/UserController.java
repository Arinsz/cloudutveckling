package com.arins.techstore.controllers;

import com.arins.techstore.aws.Xray.XRayTimed;
import com.arins.techstore.cognito.CognitoHandler;
import com.arins.techstore.services.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private ProductService productService; // Injecting TodoService



    @GetMapping("/user")
    public String getUserPage(Model model, OAuth2AuthenticationToken token) {
        var sub = token.getPrincipal().getAttribute("sub");
        model.addAttribute("username", token.getName());
        model.addAttribute("userId", sub);
        return "user";
    }

    @XRayTimed(segmentName = "Delete user")
    @PostMapping("/delete-account")
    public String deleteAccount(HttpServletRequest request, HttpServletResponse response) {
        String userId = productService.getCurrentUserId();

        // Delete todos associated with the user
        productService.deleteProductsByUserId(userId);

        // Delete the user from AWS Cognito
        CognitoHandler.deleteUser(userId);

        // Logout the user
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        // Redirect to a confirmation page or homepage after deletion
        return "userdeleted_confirmationpage";
    }

}