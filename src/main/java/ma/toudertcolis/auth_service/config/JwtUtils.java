package ma.toudertcolis.auth_service.config;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {

    // Base64-encoded secret key for HMAC SHA-256 (HS256)
    private final String base64EncodedSecretKey = "5LmR5dYpCh3hzF1S9fq07bxL6s5qznwNxEkFv4ZALv0="; // Example secret key
    private final SecretKey jwtSecretKey = new SecretKeySpec(Base64.getDecoder().decode(base64EncodedSecretKey), SignatureAlgorithm.HS256.getJcaName());

    private long jwtExpirationMs = 3600000; // JWT expiration time in milliseconds (1 hour)

    // Constructor (can be used to initialize other fields if needed)
    public JwtUtils() {
        // Secret key is already initialized in the declaration
    }

    // Generate a JWT token with user UID and role
    public String generateToken(String uid, String role,int stayOnline) {
        return Jwts.builder()
                .setSubject(uid)
                .claim("role", role)
                .claim("iss", "auth_service") // Issuer claim (matching the configuration key_claim_name)
                .setIssuedAt(new Date()) // Current time as issued at
                .setExpiration(new Date(System.currentTimeMillis() + stayOnline*60)) // Set expiration time
                .signWith(jwtSecretKey, SignatureAlgorithm.HS256) // Sign the JWT with the secret key using HS256
                .compact(); // Compact the JWT into a string
    }

    // Extract claims from the token
    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey) // Set the signing key for validation
                .build()
                .parseClaimsJws(token) // Parse the JWT
                .getBody(); // Return the claims (payload) of the token
    }

    // Extract UID from the token
    public String getUidFromToken(String token) {
        return getClaimsFromToken(token).getSubject(); // Extract the 'sub' claim (subject)
    }

    // Extract role from the token
    public String getRoleFromToken(String token) {
        return getClaimsFromToken(token).get("role", String.class); // Extract the 'role' claim
    }

    // Validate the JWT token's signature and expiration
    public boolean validateToken(String token) {
        try {
            // Try to parse the JWT and validate its signature and expiration
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey) // Validate using the correct signing key
                    .build()
                    .parseClaimsJws(token); // Parse the token
            return true; // If parsing is successful, the token is valid
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired: " + e.getMessage()); // Handle expired token
        } catch (SignatureException e) {
            System.out.println("Invalid token signature: " + e.getMessage()); // Handle invalid signature
        } catch (MalformedJwtException e) {
            System.out.println("Malformed token: " + e.getMessage()); // Handle malformed token
        } catch (Exception e) {
            System.out.println("Invalid token: " + e.getMessage()); // Handle any other errors
        }
        return false; // Return false if the token is invalid
    }
}
