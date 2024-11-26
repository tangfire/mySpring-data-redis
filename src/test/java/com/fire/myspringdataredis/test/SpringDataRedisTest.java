package com.fire.myspringdataredis.test;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class SpringDataRedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedisTemplate(){
        System.out.println(redisTemplate);
        //string数据操作
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //hash类型的数据操作
        HashOperations hashOperations = redisTemplate.opsForHash();
        //list类型的数据操作
        ListOperations listOperations = redisTemplate.opsForList();
        //set类型数据操作
        SetOperations setOperations = redisTemplate.opsForSet();
        //zset类型数据操作
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
    }


    /**
     * 简单测试
     */
    @Test
    public void test01() {
        redisTemplate.opsForValue().set("name","jack");
        String name =  (String)redisTemplate.opsForValue().get("name");
        System.out.println("name = "+name);
    }


    /**
     * 操作字符串类型的数据
     */
    @Test
    public void testString() {
        // 设置键值对
        redisTemplate.opsForValue().set("name", "小明");

        // 获取对应key的值并验证
        String city = (String) redisTemplate.opsForValue().get("name");
        System.out.println(city);


        // 添加String类型的数据，同时设置过期时间
        redisTemplate.opsForValue().set("code", "1234", 3, TimeUnit.MINUTES);

        // 检查 "code" 的过期时间是否设置正确（3分钟）
        Long expireTime = redisTemplate.getExpire("code", TimeUnit.MINUTES);
        System.out.println("code过期时间：" + expireTime + "minutes");  // 输出过期时间

        expireTime = redisTemplate.getExpire("code", TimeUnit.SECONDS);
        System.out.println("code过期时间:" + expireTime + "seconds");




        // 在某个key不存在时设置值
        boolean isSet = redisTemplate.opsForValue().setIfAbsent("lock", "1");

        System.out.println("isSet:" + isSet);


        // 再次尝试设置 "lock"，应该不成功
        isSet = redisTemplate.opsForValue().setIfAbsent("lock", "2");

        System.out.println("Second isSet:" + isSet);


        // 获取 "lock" 的值并验证
        String lockValue = (String) redisTemplate.opsForValue().get("lock");
        System.out.println("lock的值: " + lockValue);

    }


    /**
     * 操作集合类型的数据
     */
    @Test
    public void testSet() {
        SetOperations setOperations = redisTemplate.opsForSet();

        // 添加元素 set1不存在创建并添加，存在直接添加
        setOperations.add("set1", "a", "b", "c");
        setOperations.add("set2", "a", "c", "f");

        // 获取set1中的所有成员
        Set member = setOperations.members("set1");
        System.out.println(member);

        // 统计元素数量
        Long size = setOperations.size("set1");
        System.out.println(size);

        // 计算交集
        Set intersect = setOperations.intersect("set1", "set2");
        System.out.println(intersect);

        // 计算并集
        Set union = setOperations.union("set1", "set2");
        System.out.println(union);

        // 移除局部元素
        setOperations.remove("set1", "a", "b");

        member = setOperations.members("set1");
        System.out.println(member);

    }

    /**
     * 操作列表类型的数据
     */
    @Test
    public void testList() {
        ListOperations listOperations = redisTemplate.opsForList();

        // 将对多个值从左到右插入到list中
        listOperations.leftPushAll("mylist", "a", "b", "c");
        // 将多个值从左到右插入到list中（头部插入）
        listOperations.leftPush("mylist", "d");

        // 获取从0到-1的所有元素（即全部元素）
        List muList = listOperations.range("mylist", 0, -1);
        System.out.println(muList);

        // 从list的右边移除并返回最后一个元素
        listOperations.rightPop("mylist");

        muList = listOperations.range("mylist", 0, -1);

        System.out.println(muList);

        // 统计元素数量
        Long size = listOperations.size("mylist");
        System.out.println(size);
    }

    /**
     * 操作哈希类型的数据
     */
    @Test
    public void testHash() {
        HashOperations hashOperations = redisTemplate.opsForHash();

        // 添加一个hash字段并赋值==对象和属性的关系
        hashOperations.put("100", "name", "tom");
        hashOperations.put("100", "age", "20");

        // 获取字段中某个元素
        String name = (String) hashOperations.get("100", "name");
        System.out.println(name);

        // 获取所有的字段名
        Set keys = hashOperations.keys("100");
        System.out.println(keys);

        // 获取所有的字段值
        List values = hashOperations.values("100");
        System.out.println(values);

        // 删除字段age
        hashOperations.delete("100", "age");

        keys = hashOperations.keys("100");
        System.out.println(keys);

    }

    /**
     * 操作有序集合类型的数据
     */
    @Test
    public void testZset() {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();

        // 像zset中添加元素并设置其分数
        zSetOperations.add("zset1", "a", 10);
        zSetOperations.add("zset1", "b", 12);
        zSetOperations.add("zset1", "c", 9);

        // 按照分数高低获取zset中的所有元素
        Set zset1 = zSetOperations.range("zset1", 0, -1);
        System.out.println(zset1);

        // 将zset1中的c分数增加10
        zSetOperations.incrementScore("zset1", "c", 10);

        zset1 = zSetOperations.range("zset1", 0, -1);
        System.out.println(zset1);
        // 移除两个元素
        zSetOperations.remove("zset1", "a", "b");

        zset1 = zSetOperations.range("zset1", 0, -1);
        System.out.println(zset1);

    }


    /**
     * 通用命令操作
     */
    @Test
    public void testCommon() {
        // 获取所有键 * 通配符
        Set keys = redisTemplate.keys("*");
        System.out.println(keys);

        // 检查是否存在某个键 key = ？
        Boolean name = redisTemplate.hasKey("name");
        if(name){
            System.out.println("name is exist");
        }
        Boolean set1 = redisTemplate.hasKey("set1");

        if(set1){
            System.out.println("set1 is exist");
        }

        // 遍历keys集合获取每个keys的类型 DataType是枚举了redis的list\string\set\zset\hash等类型
        for (Object key : keys) {
            DataType type = redisTemplate.type(key);
            System.out.println(type.name());
        }

        // 删除某个键关联的数据
        redisTemplate.delete("mylist");

        Boolean mylist = redisTemplate.hasKey("mylist");
        if(mylist){
            System.out.println("mylist is exist");
        }else{
            System.out.println("mylist is not exist");
        }
    }

}
