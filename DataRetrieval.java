import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataRetrievalController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate; // Autowire RedisTemplate

    @Value("${jwt.secret}") // Load the secret key from properties file or environment variable
    private String jwtSecret;

    // Constants
    private static final String REDIS_SPEED_KEY_PREFIX = "speed:";

    @GetMapping("/")
    public ResponseEntity<Integer> getLatestSpeed(@RequestHeader("Authorization") String token) {
        // Validate token (check expiration, format, etc.)
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String jwt = token.substring(7);
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            String email = claims.getSubject(); // Assuming subject holds the email

            // Retrieve latest speed data from Redis based on user information
            String latestSpeed = retrieveSpeedFromRedis(email); // Implement this method

            if (latestSpeed == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(Integer.parseInt(latestSpeed));

        } catch (ExpiredJwtException | MalformedJwtException | SignatureException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    // Method to retrieve speed data from Redis based on user information
    private String retrieveSpeedFromRedis(String email) {
        // Implement logic to fetch speed data from Redis based on user information
        String latestSpeed = redisTemplate.opsForValue().get(REDIS_SPEED_KEY_PREFIX + email);
        return latestSpeed;
    }
}
