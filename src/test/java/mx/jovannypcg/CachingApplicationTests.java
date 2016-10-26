package mx.jovannypcg;

import mx.jovannypcg.dao.UserRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CachingApplicationTests {
    private static final String REDIS_USERS_KEY = "caching:users";
    private static final String REDIS_KEY_SUFIX = "~keys";

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private RedisTemplate redisTemplate;

    @Autowired
    private UserRepository userRepository;

    @After
    public void finishTesting() {
        userRepository.flushCache();
    }

	@Test
	public void cacheConfigurationShouldBeOK() {
		assertNotNull("cacheManager is null", cacheManager);
		assertNotNull("redisTemplate is null", redisTemplate);

		assertTrue("cacheManager must be an instance of RedisCacheManager",
				cacheManager instanceof RedisCacheManager);

		RedisConnectionFactory redisConnectionFactory = redisTemplate.getConnectionFactory();

		assertNotNull("redisTemplate must have a valid RedisConnectionFactory",
				redisConnectionFactory);
		assertTrue("redisConnection factory must be an instance of JedisConnectionFactory",
				redisConnectionFactory instanceof JedisConnectionFactory);

		JedisConnectionFactory jedisConnectionFactory = (JedisConnectionFactory) redisConnectionFactory;

		assertNotNull("Redis host is null",
				jedisConnectionFactory.getHostName());
		assertNotNull("Redis port is null",
				jedisConnectionFactory.getPort());
	}

    @Test
    public void usersCacheShouldBeCleaned() throws Exception {
        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();

        userRepository.flushCache();

        assertFalse("Key " + REDIS_USERS_KEY+ REDIS_KEY_SUFIX + " should not exist",
                redisConnection.exists((REDIS_USERS_KEY + REDIS_KEY_SUFIX).getBytes()));
    }
}
