package ma.toudertcolis.auth_service.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import ma.toudertcolis.auth_service.config.PasswordEncoderService;
import ma.toudertcolis.auth_service.model.UserDTO;
import ma.toudertcolis.auth_service.model.User;
import ma.toudertcolis.auth_service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FirebaseAuthService {

    private final UserRepository userRepository;

    public FirebaseAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(UserRecord userRecord, String nomComplet, String telephone, String email, String cin, String ville, String password) {
        // Encode the password before storing it
        String encodedPassword = PasswordEncoderService.encodePassword(password);

        // Create User object with Firebase UID and the encoded password
        User user = new User(userRecord.getUid(), nomComplet, telephone, email, cin, ville, encodedPassword);
        System.out.println("User saved to MySQL with UID: " + user.getId());
        // Save user to MySQL database
        return userRepository.save(user);
    }

    public List<UserDTO> getAllUsers() throws Exception {
        ListUsersPage page = FirebaseAuth.getInstance().listUsers(null);
        List<UserDTO> users = new ArrayList<>();

        for (UserRecord userRecord : page.iterateAll()) {
            UserDTO userDTO = new UserDTO(
                    userRecord.getUid(),
                    userRecord.getDisplayName(),
                    userRecord.getPhoneNumber(),
                    userRecord.getEmail(),
                    null,
                    null,
                    null
            );

            // Fetch custom claims if available
            if (userRecord.getCustomClaims() != null) {
                if (userRecord.getCustomClaims().containsKey("cin")) {
                    userDTO.setCin(userRecord.getCustomClaims().get("cin").toString());
                }
                if (userRecord.getCustomClaims().containsKey("ville")) {
                    userDTO.setVille(userRecord.getCustomClaims().get("ville").toString());
                }
            }

            // Now save to MySQL (ensure the password is properly encoded when saving)
            // Since we don't have passwords in Firebase, you might have them in a separate system or request.
            // If you have a default password or you are not handling passwords for this system, you can leave it as null or handle accordingly
            String defaultPassword = "defaultPassword";  // For example, you can assign a default password or pass it via your business logic
            String encodedPassword = PasswordEncoderService.encodePassword(defaultPassword);  // Encode the password before storing it

            User user = new User(
                    userRecord.getUid(),
                    userRecord.getDisplayName(),
                    userRecord.getPhoneNumber(),
                    userRecord.getEmail(),
                    userDTO.getCin(),
                    userDTO.getVille(),
                    encodedPassword // Save encoded password
            );

            userRepository.save(user); // Save user to MySQL

            users.add(userDTO);
        }

        return users;
    }
}
