@RestController
public class AuthenticationController {

    @Autowired
    private TokenService tokenService;

    @PostMapping("/")
    public ResponseEntity<String> generateToken(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");

        // Validate email
        if (email == null || !email.matches("@gmail.com")) {
            return ResponseEntity.badRequest().body("Invalid email format");
        }

        // Generate token and set expiration to 5 minutes
        String token = tokenService.generateToken(email, 5 * 60 * 1000);

        return ResponseEntity.ok(token);
    }
}
