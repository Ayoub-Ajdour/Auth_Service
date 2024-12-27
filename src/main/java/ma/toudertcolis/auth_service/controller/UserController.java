package ma.toudertcolis.auth_service.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import ma.toudertcolis.auth_service.model.User;
import ma.toudertcolis.auth_service.model.UserDTO;
import ma.toudertcolis.auth_service.repository.UserRepository;
import ma.toudertcolis.auth_service.service.FirebaseAuthService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/users")
public class UserController {

    private final FirebaseAuthService firebaseAuthService;
    private UserRepository userRepository;
    public UserController(FirebaseAuthService firebaseAuthService, UserRepository userRepository) {
        this.firebaseAuthService = firebaseAuthService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() throws Exception {
        return firebaseAuthService.getAllUsers();
    }
    @GetMapping("/{uid}")
    public Optional<User> getById(@PathVariable String uid) throws Exception {
        return userRepository.findById(uid);
    }

    @PostMapping("/create")
    public User addUser(@RequestParam String nomComplet,
                        @RequestParam(required = false) String telephone,
                        @RequestParam String email,
                        @RequestParam(required = false) String cin,
                        @RequestParam(required = false) String ville,
                        @RequestParam String password) throws Exception {
        System.out.println("👌👌👌👌Phone number: " + telephone);
        System.out.println("Nom Complet: " + nomComplet);
        System.out.println("Email: " + email);

        // Ensure phone number starts with "+" and is valid
        if (telephone != null) {
            if (!telephone.startsWith("+")) {
                telephone = "+" + telephone;  // Add '+' if missing
            }

            if (telephone.length() < 10) {
                throw new IllegalArgumentException("Phone number is too short. It should be at least 10 digits.");
            }
        }

        // Create a new user in Firebase Authentication with the password
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password)
                .setDisplayName(nomComplet);
//                .setPhoneNumber(telephone)
//                .setUid(email); // Use the email as a unique ID for the user

        // Firebase does not directly support setting a password during user creation
        // Password is handled separately via Firebase Authentication
        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        System.out.println("Firebase UID: " + userRecord.getUid());
        System.out.println("User created in Firebase");
        // After creating user in Firebase, store the UID in the MySQL database with other user data
        User user = new User(userRecord.getUid(), nomComplet, telephone, email, cin, ville, password); // Store password
        System.out.println("👍👍👍Saving user in MySQL with UID: " + user.getId());
        // Store user in MySQL
        return firebaseAuthService.addUser(userRecord, nomComplet, telephone, email, cin, ville, password);
    }





}
