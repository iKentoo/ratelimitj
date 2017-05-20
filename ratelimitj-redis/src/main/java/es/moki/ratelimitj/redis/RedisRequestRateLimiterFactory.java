package es.moki.ratelimitj.redis;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import es.moki.ratelimitj.core.limiter.request.AsyncRequestRateLimiter;
import es.moki.ratelimitj.core.limiter.request.RequestLimitRule;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiterFactory;
import es.moki.ratelimitj.core.limiter.request.ReactiveRequestRateLimiter;

import java.io.IOException;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public class RedisRequestRateLimiterFactory implements RequestRateLimiterFactory {

    private final RedisClient client;
    private StatefulRedisConnection<String, String> connection;

    public RedisRequestRateLimiterFactory(RedisClient client) {
        this.client = requireNonNull(client);
    }

    @Override
    public RequestRateLimiter getInstance(Set<RequestLimitRule> rules) {
        return new RedisSlidingWindowRequestRequestRequestRateLimiter(getConnection(), rules);
    }

    @Override
    public AsyncRequestRateLimiter getInstanceAsync(Set<RequestLimitRule> rules) {
        return new RedisSlidingWindowRequestRequestRequestRateLimiter(getConnection(), rules);
    }

    @Override
    public ReactiveRequestRateLimiter getInstanceReactive(Set<RequestLimitRule> rules) {
        return new RedisSlidingWindowRequestRequestRequestRateLimiter(getConnection(), rules);
    }

    @Override
    public void close() throws IOException {
        client.shutdown();
    }

    private StatefulRedisConnection<String, String> getConnection() {
        // going to ignore race conditions at the cost of having multiple connections
        if (connection == null) {
            connection = client.connect();
        }
        return connection;
    }
}