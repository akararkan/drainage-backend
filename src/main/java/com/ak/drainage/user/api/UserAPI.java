package com.ak.drainage.user.api;

import com.ak.drainage.user.jwt.Token;
import com.ak.drainage.user.jwt.Token;
import com.ak.drainage.user.model.User;
import com.ak.drainage.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173") // Adjust this to your frontend URL
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserAPI {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    @PostMapping("/register")
    public ResponseEntity<Token> register(@RequestBody User user , HttpServletRequest request) throws Exception  {
        return userService.createUser(user  , getSiteURL(request));
    }
    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
    @GetMapping("/admin")
    public String helloAdmin(){
        return "Hello Admin";
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        logger.info("Received verification code: {}", code);

        if (userService.verify(code)) {
            logger.info("Verification successful for code: {}", code);
            return "<h1>verify_success</h1>";
        } else {
            logger.warn("Verification failed for code: {}", code);
            return "verify_fail";
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody User user) {
        return userService.login(user);
    }


}
