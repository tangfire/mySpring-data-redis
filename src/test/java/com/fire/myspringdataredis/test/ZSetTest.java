package com.fire.myspringdataredis.test;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Set;

@SpringBootTest
public class ZSetTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private ZSetOperations<String, String> operations;

    @PostConstruct
    public void before() {
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
        operations = redisTemplate.opsForZSet();
    }

    //添加
    @Test
    public void testAdd() {
        operations.add("students", "zhangsan", 100);
        operations.add("students", "lisi", 80);
        operations.add("students", "wangwu", 90);
    }

    //增减分数
    @Test
    public void testScore() {
        //根据key-value来增减分数
        operations.incrementScore("students", "lisi", -50);
    }

    //查询一个元素的信息
    @Test
    public void testFindOne() {
        //查询个人分数
        Double score = operations.score("students", "lisi");
        System.out.println(score);

        //查询个人排名
        Long rank = operations.rank("students", "lisi");
        System.out.println(rank);
    }

    //列表查询:分为两大类，正序和逆序。以下只列表正序的，逆序的只需在方法前加上reverse即可
    @Test
    public void testFindList() {
        //通过排名区间获取集合元素
        Set<String> students1 = operations.range("students", 1, 2);
        for (String stu : students1) {
            System.out.println(stu);
        }

        //通过排名区间获取集合元素和分数
        Set<ZSetOperations.TypedTuple<String>> students2 =
                operations.rangeWithScores("students", 1, 2);
        for (ZSetOperations.TypedTuple<String> tuple : students2) {
            String value = tuple.getValue();
            Double score = tuple.getScore();
            System.out.println("value-->" + value + ",score-->" + score);
        }

        //通过分数区间获取集合元素
        Set<String> students3 = operations.rangeByScore("students", 80, 90);
        for (String stu : students3) {
            System.out.println(stu);
        }


        //通过分数区间获取集合元素和分数
        Set<ZSetOperations.TypedTuple<String>> students4 =
                operations.rangeByScoreWithScores("students", 80, 90);
        for (ZSetOperations.TypedTuple<String> tuple : students4) {
            String value = tuple.getValue();
            Double score = tuple.getScore();
            System.out.println("value-->" + value + ",score-->" + score);
        }
    }

    //统计元素
    @Test
    public void testCount() {
        //统计集合大小
        Long zCard = operations.zCard("students");
        System.out.println(zCard);

        //统计分数区间的元素数量
        Long count = operations.count("students", 85, 95);
        System.out.println(count);
    }

    //移除元素
    @Test
    public void testRemove() {
        //通过key--value删除
        operations.remove("students", "zhangsan");

        //通过排名区间删除
        operations.removeRange("students", 0, 1);

        //通过分数区间删除
        operations.removeRangeByScore("students", 80, 90);
    }
}
