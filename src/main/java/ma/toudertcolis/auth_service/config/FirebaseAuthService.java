package ma.toudertcolis.auth_service.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.UpdateRequest;

import java.util.Map;

public class FirebaseAuthService {

    public void addRoleToUser(String uid, String role) throws Exception {
        UserRecord user = FirebaseAuth.getInstance().getUser(uid);

        UpdateRequest request = new UserRecord.UpdateRequest(uid)
                .setCustomClaims(Map.of("role", role));  // Add custom claim for role

        FirebaseAuth.getInstance().updateUser(request);
    }
}
