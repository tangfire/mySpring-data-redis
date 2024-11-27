package com.fire.myspringdataredis.test;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Set;

@SpringBootTest
public class SetTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private SetOperations<String, String> operations;

    @PostConstruct
    public void before() {
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
        operations = redisTemplate.opsForSet();
    }

    //添加
    @Test
    public void testAdd() {
        operations.add("set_names", "zhangsan", "li", "wangwu");
    }

    //查看集合中元素
    @Test
    public void testFind() {
        //查询集合中的所有元素
        Set sets = operations.members("set_names");
        System.out.println(sets);
        System.out.println(sets.size());

        System.out.println("----------------------------");

        
        //随机获取集合中的一个元素
        String name = operations.randomMember("set_names");
        System.out.println(name);
		
        //随机获取集合中的多个元素
        operations.randomMembers("set_names",2).stream().forEach(System.out::println);
    }
    
    //移除元素
    @Test
    public void testRemove() {
        //根据指定的key--value进行移除
        operations.remove("set_names", "zhangsan", "li");

        //根据key随机移除并返回value
        String name = operations.pop("set_names");
        System.out.println(name);
    }
    
    //多集合的操作
    @Test
    public void testMoreSet() {
       operations.add("set_name1", "zhangsan", "li", "wangwu");
       operations.add("set_name2", "zhangsan", "li", "zhaoliu");

       //取交集
       operations.intersect("set_name1","set_name2").stream().forEach(System.out::println);

        System.out.println("---------------------------");
       //取并集
       operations.union("set_name1", "set_name2").stream().forEach(System.out::println);

        System.out.println("---------------------------");
       //取差集
       operations.difference("set_name1","set_name2").stream().forEach(System.out::println);
    }
}
