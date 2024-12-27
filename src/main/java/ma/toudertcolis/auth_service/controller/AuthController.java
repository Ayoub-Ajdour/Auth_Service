package ma.toudertcolis.auth_service.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import ma.toudertcolis.auth_service.config.JwtUtils;
import ma.toudertcolis.auth_service.model.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    // Firebase Auth
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    // Generate a JWT token
    @PostMapping("/login")
    public JwtResponse login(@RequestParam String uid,@RequestParam String stayOnline,@RequestParam String role) {
        int cc = Integer.parseInt(stayOnline);
        System.out.println(cc);
        String token = jwtUtils.generateToken(uid, role,cc);
        System.out.println(token);
        return new JwtResponse(token);
    }

    // Validate a JWT token
    @GetMapping("/validate-token")
    public boolean validateToken(@RequestParam String token) {
        return jwtUtils.validateToken(token);
    }

    // Extract UID from a JWT token
    @GetMapping("/get-uid")
    public String getUid(@RequestParam String token) {
        return jwtUtils.getUidFromToken(token);
    }

    // Admin test endpoint
    @GetMapping("/admin/test")
    public String adminTest(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "").trim();

        if (jwtUtils.validateToken(token)) {
            String role = jwtUtils.getRoleFromToken(token);
            if ("ADMIN".equals(role)) {
                return "Access granted to the admin resource.";
            } else {
                return "Access denied. Admin role required.";
            }
        }
        return "Invalid token.";
    }
}
