package org.apache.shenyu.admin;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@Component
@Slf4j
public class RedissonTest {

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void set() {
        // 设置字符串
        RBucket<String> k2= redissonClient.getBucket("k2");
        //k2.set(v1236);
        RBucket<String> keyObj = redissonClient.getBucket("k1");
        log.info("################## ok ##################"+keyObj.get());
    }
}
