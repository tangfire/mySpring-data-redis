package com.fire.myspringdataredis.test;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@SpringBootTest
public class ListTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private ListOperations<String,String> operations;

    @PostConstruct
    public void before() {
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
        operations = redisTemplate.opsForList();
    }

    //增加
    @Test
    public void testAdd() {
        // 将所有指定的值插入存储在键的列表的头部（或尾部）。
        // 如果键不存在，则在执行推送操作之前将其创建为空列表。
        operations.leftPush("names", "zhangsan");
        operations.leftPushAll("names", "lisi","wangwu","zhaoliu");

        operations.rightPush("names", "sunqi");
        operations.rightPushAll("names", "lisi","wangwu","zhaoliu");
    }

    //根据索引查询元素
    @Test
    public void testFind() {
        //0代表左边开始第一个元素
        String name1 = operations.index("names", 0);
        System.out.println(name1);
		
        //-1代表右边开始第一个元素
        String name2 = operations.index("names", -1);
        System.out.println(name2);

        System.out.println("---------------------");
        
        //range代表一个范围(包含开始索引,结束索引)
        List<String> names = operations.range("names", 0, -1);
        for (String name : names) {
            System.out.println(name);
        }
    }
    
    //移除某个元素的值
    @Test
    public void testRemove() {
        //从坐标或者右边弹出第一个元素
        operations.rightPop("names");
        
        //弹出指定的元素
        // count > 0：删除左边起第几个等于指定值的元素
        // count < 0：删除右边起第几个等于指定值的元素
        // count = 0：删除所有等于value的元素。
        operations.remove("names", 1, "zhangsan");
    }
}
