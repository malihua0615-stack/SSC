## 登录Token

### 流程
- 用户登陆先生成jwt（设置UserID）
- 再次请求打到网关拦截器，取出jwt的userId 设置到请求头中。
- 网关把请求转到对应服务中。
- 服务中加载了common 的拦截器，拦截到了这个请求，把请求头里面的id 设置到ThreadLocal里面
- 此时就可以在任何地方获取到ThreadLocal 里面的用户信息了。
- 
### 依赖包
```xml

<!-- ====== 核心依赖（必须有） ====== -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <scope>provided</scope>
</dependency>
        <!-- ====== WebMVC 支持（optional，不强制传递） ====== -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <optional>true</optional>
    <scope>provided</scope> 
<!--设置一下域-->
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>${jjwt.version}</version>
</dependency>


```
### 流程
- 登录的时候调用生成token
```java
private static SecretKey getSecretKey() {
    byte[] keyBytes = SECRET.getBytes(StandardCharsets.UTF_8);
    // 如果密钥长度不足 32 字节，Keys.hmacShaKeyFor 会自动补足
    // 建议配置文件里直接把密钥写够 32 位以上

    return Keys.hmacShaKeyFor(keyBytes);
}

//EXPIRE 是过期时间long 类型
public static String getToken(UserEntity userEntity) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(userEntity.getId())
                .claim("username",userEntity.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ EXPIRE))
                .signWith(getSecretKey())
                .compact();
    }

public static Claims parseToken(String token) {
    try {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    } catch (ExpiredJwtException e) {
        log.warn("Token已过期: {}", e.getMessage());
        throw new BusinessException(401, "已过期，请重新登录");
    } catch (MalformedJwtException e) {
        log.warn("Token格式错误: {}", e.getMessage());
        throw new BusinessException(401, "Token格式错误");
    } catch (SignatureException e) {
        log.warn("Token签名验证失败: {}", e.getMessage());
        throw new BusinessException(401, "Token签名验证失败");
    } catch (IllegalArgumentException e) {
        log.warn("Token参数异常: {}", e.getMessage());
        throw new BusinessException(401, "Token参数异常");
    } catch (Exception e) {
        log.error("Token验证异常: {}", e.getMessage(), e);
        throw new BusinessException(401, "Token无效，请重新登录");
    }
}
```
- 添加过滤器,检查所有的地址
```java
// 我是在网关添加了一个过滤器 在配置文件里面添加了路由,让所有的地址都走网关.

@Component
@Slf4j
@RequiredArgsConstructor
//全局过滤器
public class LoginFilter implements GlobalFilter, Ordered {
    //白名单 
    private final String[] PATH = {
            "/auth/login",
    };

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String token = getToken(exchange);
        if (Arrays.asList(PATH).contains(path)){
            return chain.filter(exchange);
        }
        if (token == null ) {
            log.warn("token is null");
            return unauthorizedResponse(exchange,"先登录一下！");
        }
        String id = JwtUtil.getId(token);
        //获取到token之后 取出userId 存到header 里面
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-User-Id", id)
                .build();
        log.debug("Token 校验通过。: userId={}, path={}", id, path);
        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    @Override
    public int getOrder() {
        return -100;
    }


    private String getToken(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String a = request.getHeaders().getFirst("Authorization");
        if (a != null && a.startsWith("Bearer ")) {
            return a.substring(7);
        }
        return null;
    }

    /**
     * 返回 401
     */
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = String.format("{\"code\":401,\"message\":\"%s\"}", message);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}
```
- 到这里就可以从前台过来的token 里面获取到对应的userId了
- 然后在common里面添加拦截器,拦截所有的请求,把对应的id 转为userEntity 存到ThreadLocal 里面
- 
```java
//拦截器

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("SSC-User-Id");
        String username = request.getHeader("SSC-User-Name");
        String role = request.getHeader("SSC-User-Role");
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setUsername(username);
        userEntity.setRole(role);
        UserContext.setUserEntity(userEntity);
        log.debug("成功设置到threadLocal 里面了。。。。ID:{}",userId);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
    
    //请求结束之后自动清理掉threadLocal
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        UserContext.removeUserEntity();
    }
}

//用户信息的上下文
public class UserContext {

    private static final ThreadLocal<UserEntity>  userEntityThreadLocal = new ThreadLocal<>();
    public  static UserEntity getUserEntity(){
        return userEntityThreadLocal.get();
    }
    public static void setUserEntity(UserEntity userEntity){
        userEntityThreadLocal.set(userEntity);
    }
    public static void removeUserEntity(){
        userEntityThreadLocal.remove();
    }
}


//拦截器配置
//这里需要设置一下加载的条件

@Slf4j
@Configuration
//只在Servlet 容器里加载。
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(WebMvcConfigurer.class)
public class AuthWebMvcConfig implements WebMvcConfigurer {

    @PostConstruct
    public void init(){
        log.info("AuthWebMvcConfig has been initialized....");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/**")//拦截的路径
                .excludePathPatterns("/auth/login")//排除的路径
                .order(1);
        log.info("✅ UserInfoInterceptor 已注册（WebMVC 环境）");
    }
}
//这个是2.7之后的版本才是这样的
//设置自动加载
//在common 的resources/META-INFO.spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
//在文件中添加对应的拦截器配置文件的类限定名  com.example.common.web.AuthWebMvcConfig

```

