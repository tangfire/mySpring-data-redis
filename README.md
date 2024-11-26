
使用SpringDataRedis操作redis

[jedis的简单使用(Jedis即用Java操作Redis)](https://github.com/tangfire/jedis-redis)

# 参考文章

[Spring Data Redis + Redis数据缓存学习笔记](https://blog.csdn.net/qq_45607971/article/details/140472184)


# 环境配置

## maven配置

```xml
<!--连接池依赖-->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.2</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.30</version>
            <scope>provided</scope>
        </dependency>
```

## 配置类

```java
package com.fire.myspringdataredis.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        //创建RedisTemplate对象
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 设置连接工厂
        template.setConnectionFactory(connectionFactory);
        // 创建JSON序列化工具
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        //设置key 的序列化
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        // 设置value的序列化
        template.setValueSerializer(jsonRedisSerializer);
        template.setHashValueSerializer(jsonRedisSerializer);
        //返回
        return template;
    }
}

```

# 测试

/test/java/com/fire/myspringdataredis/test/SpringDataRedisTest




# Redis基础

## 身份验证

使用 AUTH 命令进行身份验证
当 Redis 实例启用了密码验证后，任何客户端在执行操作之前都必须使用 AUTH 命令进行身份验证。

命令格式如下:

`auth <password>`

例如，如果你的 Redis 密码是 mypassword，你可以在 Redis CLI 中执行以下命令来进行认证：

```bash
auth mypassword
```

## Redis通用命令

1. Redis默认有16个数据库,切换到第2个数据库
```bash
select 1
```

2. 查看当前数据库key的数量

```bash
dbsize
```

3. 设置一个key为username，值为mike的数据

```bash
set username mike
```

4. 获取key为username的值

```bash
get username
```

5. 获取所有的key

```bash
keys *
```

6. 清除当前数据库

```bash
flushdb
```

7. 清除所有数据库

```bash
flushall
```


## Redis基本命令

1. 查询key为username是否存在

```bash
exists username
```

2. 指定key为username移动到1号数据库

```bash
move username 1
```

3. 指定key为username10s后过期

```bash
expire username 10
```

4. 查看key为username还有多久过期

```bash
ttl username 
```

5. 查看key为username是什么类型

```bash
type username
```

## 五种数据类型

### String类型

1. 设置key为name的值为htt

```bash
set name htt
```

2. 获取key为name的值

```bash
get name
```

3. 拼接key为name的值:httstudy

```bash
append name study
```

4. 获取key为name的值的长度

```bash
strlen name
```

5. 设置key为view的值加1

```bash
set view 0
incr view
```

6. 设置key为view的值减1

```bash
decr view
```

7. 设置key为view的值加10

```bash
incrby view 10
```

8. 设置key为view的值减10

```bash
decrby view 10
```

9. 截取下标为0-3之间的字符串，例如：abcdef，截取后abcd

```bash
getrange name 0 3
```

10. 从下标为1进行替换字符串，例如：abcdef，替换后a000efg

```bash
setrange name 1 100
```

11. 设置key为name的值为hello，10s后过期

```bash
setex name 10 hello
```

12. 如果不存在key为title的，值设置为redis，如果存在，则set失败

```bash
setnx title redis
```

13. 一次性设置多个值

```bash
mset k1 v1 k2 v2 k3 v3
```

```bash
mset user:1:name htt user:1:age 2
```


14. 一次性取多个值

```bash
mget k1 k2 k3
```

```bash
mget user:1:name user:1:age
```
15. 如果k1已经存在，则k1，k4全部设置失败，参考事务的原子性操作

```bash
msetnx k1 v1 k4 v4
```


16. 如果不存在key为username的值，则返回nil，然后set进去；如果存在值，则获取原来的值并设置新的值

```bash
getset username htt
```

### List集合类型

1. 将一个值或多个值插入到列表的头部

```bash
lpush list 1

```
example:
```bash
lpush list 1
lpush list 2
lpush list 3
lrange list 0 -1
```

2. 将一个值或者多个值插入到列表的尾部

```bash
rpush list 4
```

example:

```bash
rpush list 4
lrange list 0 -1
```

3. 通过区间获取具体的值

```bash
lrange list 0 -1

```

4. 移除list的第一个元素：3

```bash
lpop list 
```

5. 移除list的最后一个元素：4

```bash
rpop list
```

6. 通过下标获得list当中的某一个值

```bash
lindex list 0

```

7. 获取list的长度

```bash
llen list
```

8. 移除list集合指定个数的value，移除1个值为2的，精确匹配

```bash
lrem list 1 2
```

9. 截取list集合中下标为1到下标为2之间的元素集合，并覆盖原来的list集合

```bash
ltrim list 1 2
```


10. 更新list集合当中下标为0的值为bbb，如果下标0的值不存在，则报错

```bash
lset list 0 bbb
```

11. 将一个某一个具体的值插入到某一个具体元素（默认第一个）的前面或者后面

```bash
linsert list before bbb aaa

```
```bash
linsert list after bbb ccc
```


### Set集合类型

1. 往set集合中添加一个元素

```bash
sadd set hello
```

2. 查看set集合中所有元素

```bash
smembers set
```


3. 查看set集合中是否存在某元素

```bash
sismember set world
```


4. 随机抽取出1个元素

```bash
srandmember set
```


5. 随机抽取出2个元素

```bash
srandmember set 2
```


6. 随机删除set集合中某个元素

```bash
spop set
```


7. 移动set集合中的world元素到set2集合中

```bash
smove set set2 world
```


8. 作set2集合减去set集合的差集

```bash
sdiff set2 set
```

9. set和set2的交集


```bash
sinter set set2
```



10. set和set2作并集并去重

```bash
sunion set set2
```

11. 删除一个元素

```bash
srem set apple
```

12. 删除多个元素

```bash
srem set apple banana watermelon
```


### Hash集合类型

1. 往hash集合中存放键值对数据

```bash
hset hash username mike
```


2. 从hash集合中获取数据

```bash
hget hash username
```


3. 同时往hash集合中添加多个值

```bash
hmset hash username jack age 2
```


4. 同时往hash集合中获取多个值

```bash
hmget hash username age
```


5. 获取hash集合中所有的键值对

```bash
hgetall hash
```

6. 删除hash集合中指定的key字段

```bash
hdel hash username
```


7. 获取hash集合的长度

```bash
hlen hash
```


8. 判断hash集合中指定字段是否存在

```bash
hexists hash username
```


9. 获取hash集合中所有的key

```bash
hvals hash
```


10. 获取hash集合中所有的值
```bash
hkeys hash
```


11. 指定hash集合中指定增量

```bash
hincrby hash views 1
```


12. 如果不存在则直接设置值，存在则设置失败

```bash
hsetnx hash password 123456

```


### Zset有序集合类型

1. 添加一个值

```bash
zadd zset 1 first
```


2. 添加多个值

```bash
zadd zset 2 second 3 third 4 four
```


3. 获取zset集合中所有元素

```bash
zrange zset 0 -1
```


4. 给zset集合中的元素从小到大排序，-inf：负无穷，+inf：正无穷

```bash
zrangebyscore zset -inf +inf
```




5. 从小到大排序并输出键值

```bash
zrangebyscore zset -inf +inf withscores

```



6. 指定负无穷到1的范围

```bash
zrangebyscore zset -inf 1 withscores

```



7. 移除zset集合中指定的元素

```bash
zrem zset four
```


8. 查看zset集合中元素个数

```bash
zcard zset
```


9. 反转指定范围

```bash
zrevrange zset 1 2
```

----------------------

### Redis删除总结

Redis 支持多种数据类型（如字符串、哈希、列表、集合、有序集合等），但删除键的操作是通用的，不依赖于数据类型。

### 删除数据的基本命令

#### 1. **删除单个键**
使用 `DEL` 命令可以删除指定的键及其对应的数据：
   ```bash
   DEL <key>
   ```
例如，删除名为 `myKey` 的键：
   ```bash
   DEL myKey
   ```

如果删除的键不存在，`DEL` 命令不会报错，返回值为 `0`，如果删除成功，则返回值为 `1`。

#### 2. **删除多个键**
`DEL` 命令也支持删除多个键，可以一次性删除多个键：
   ```bash
   DEL key1 key2 key3
   ```

如果删除的某个键不存在，Redis 会跳过它，不会报错。

#### 3. **删除特定数据类型**
Redis 中的数据类型包括字符串、哈希、列表、集合、有序集合等。虽然删除键的命令 (`DEL`) 适用于所有类型的数据，但如果你希望删除某种数据类型中的特定元素，则需要使用与该数据类型相关的命令。

- **删除哈希表中的字段**
  如果你想删除哈希数据类型中的某个字段，可以使用 `HDEL` 命令：
  ```bash
  HDEL <hash> <field1> <field2> ...
  ```
  例如，删除哈希表 `user:1000` 中的字段 `name` 和 `age`：
  ```bash
  HDEL user:1000 name age
  ```

- **删除列表中的元素**
  如果要删除列表中的特定元素，可以使用 `LREM` 命令：
  ```bash
  LREM <list> <count> <value>
  ```
    - `count` 为 `0` 表示删除列表中所有匹配的元素，`count` 为正数表示从头部开始删除，负数表示从尾部开始删除。
      例如，删除列表 `myList` 中的所有值为 `hello` 的元素：
  ```bash
  LREM myList 0 hello
  ```

- **删除集合中的元素**
  如果要删除集合中的某个元素，可以使用 `SREM` 命令：
  ```bash
  SREM <set> <member1> <member2> ...
  ```
  例如，删除集合 `mySet` 中的元素 `member1` 和 `member2`：
  ```bash
  SREM mySet member1 member2
  ```

- **删除有序集合中的元素**
  删除有序集合（Sorted Set）中的元素使用 `ZREM` 命令：
  ```bash
  ZREM <zset> <member1> <member2> ...
  ```
  例如，删除有序集合 `myZSet` 中的成员 `member1` 和 `member2`：
  ```bash
  ZREM myZSet member1 member2
  ```

#### 4. **删除数据库中的所有键**
如果你想删除 Redis 数据库中的所有键，可以使用 `FLUSHDB` 命令。这个命令会删除当前数据库中的所有键（但不会影响其他数据库）：
   ```bash
   FLUSHDB
   ```

如果你希望删除所有数据库中的所有键，可以使用 `FLUSHALL` 命令：
   ```bash
   FLUSHALL
   ```

### 总结：
- **删除单个键**：使用 `DEL <key>`。
- **删除多个键**：使用 `DEL <key1> <key2> ...`。
- **删除哈希字段**：使用 `HDEL <hash> <field1> <field2> ...`。
- **删除列表元素**：使用 `LREM <list> <count> <value>`。
- **删除集合元素**：使用 `SREM <set> <member1> <member2> ...`。
- **删除有序集合成员**：使用 `ZREM <zset> <member1> <member2> ...`。
- **清空当前数据库**：使用 `FLUSHDB`。
- **清空所有数据库**：使用 `FLUSHALL`。

每种数据类型的删除操作适用于其特定的数据结构，但删除键的操作是相同的。

