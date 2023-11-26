import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
public class AuthenticationController {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @PostMapping("/generate-token")
    public ResponseEntity<String> generateToken(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        String token = generateJWTToken(email);

        return ResponseEntity.ok(token);
    }

    
    private String generateJWTToken(String email) {
        long currentTimeMillis = System.currentTimeMillis();
        long expirationTime = currentTimeMillis + 5 * 60 * 1000; 

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(expirationTime))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }
}
