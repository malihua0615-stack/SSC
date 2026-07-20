## Redis 配置
### 依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```
### 配置文件
- 如果是分模化的项目最好把配置文件放到common 里面，其他项目直接添加这个依赖。
- 在需要用的模块的启动类的头部添加相关包的扫描
```java
//前面是配置文件所在的模块，后面是当前的模块。
//如果只加common 那么就会把当前规则给覆盖掉。
@ComponentScan(basePackages = {"com.example.common","com.example.product"})
```
- 配置文件
```java

//这个类的作用就是让字符串传入redis 的时候不会乱码。
//需要注意数据内容还是乱码，因为redis-cli 是不会解析中文的。
@Configuration
public class RedisConfig {

    @PostConstruct
    public void init(){
        System.out.println("loading RedisConfig......");
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        //这里是为了让时间能存进reids
        // 创建自定义 ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        // 注册 Java 8 时间类型支持
        objectMapper.registerModule(new JavaTimeModule());
        // 禁用将日期序列化为时间戳（使用 ISO-8601 格式）
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // 设置 Key 和 HashKey 使用 String 序列化
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // 设置 Value 和 HashValue 使用 JSON 序列化
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }


}

```