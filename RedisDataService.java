import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisDataService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate; 

    private static final String REDIS_KEY_PREFIX = "data:"; 

    public String retrieveDataFromRedis(String identifier, String dataType) {
        String redisKey = REDIS_KEY_PREFIX + dataType + ":" + identifier;
        return redisTemplate.opsForValue().get(redisKey);
    }

    public void saveDataToRedis(String identifier, String dataType, String data) {
        String redisKey = REDIS_KEY_PREFIX + dataType + ":" + identifier;
        redisTemplate.opsForValue().set(redisKey, data);
    }

    
    public String retrieveSpeedFromRedisByEmail(String email) {
        return retrieveDataFromRedis(email, "speed");
    }

   
    public void saveSpeedToRedisByEmail(String email, String speed) {
        saveDataToRedis(email, "speed", speed);
    }

   
}
