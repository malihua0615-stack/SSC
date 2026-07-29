## OpenFeign
```text
    OpenFeign 是用来解决各个模块之间的调用问题,用的是Http 的方式进行交互的.
```
### Controller
```text
    OpenFeign 其实就是调用对应模块里面的controller,
    所以Controller 还是正常写的.
```
```java

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/getProductById/{id}")
    public Result<ProductEntity> getProductById(@PathVariable String id) {
        ProductEntity productById = productService.getProductById(Long.parseLong(id));
        return Result.success(productById);
    }

    @GetMapping("/getAllProducts")
    public Result<List<ProductEntity>> getAllProducts() {
        List<ProductEntity> allProducts = productService.getAllProducts();
        return Result.success(allProducts);
    }
}
```

### OpenFeign 接口
```text
    在common 模块里面写一个接口,类上写上@FeignClient 注解,
    里面的name 是对应模块的名字(和aaplication.yml中的name一致), 
    path 是对应模块的controller 的路径,
    然后在方法上写上对应的请求方式和路径就行了.
    其实跟Controller 里面的方法是一样的,只是多了一个@FeignClient 注解.
```
```java

@FeignClient(name = "product", path = "/product")
public interface ProductFeignClient {

    @GetMapping("/getProductById/{id}")
    Result<ProductEntity> getProductById(@PathVariable("id") String id);

    @GetMapping("/getAllProducts")
    Result<List<ProductEntity>> getAllProducts();
}

```
### 使用
```text
    在启动类上加上@EnableFeignClients 注解,然后在需要调用的地方注入这个接口就行了.
    
```
```java
@SpringBootApplication(scanBasePackages = {"com.example.common","com.example.order"})
@EnableFeignClients(basePackages = "com.example.common.openfeign")
@EnableDiscoveryClient
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}


```
```java

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final UserFeignClient userFeignClient;

    private final ProductFeignClient productFeignClient;

    public CreateOrderDto autoCreateOrder() {
        
        //这里和正常的service调用是一样的,只是调用的是feignClient 的方法,返回值是Result 类型的(其实就是controller的返回值).
        Result<List<ProductEntity>> allProducts = productFeignClient.getAllProducts();
        List<ProductEntity> data = allProducts.getData();
        int size = data.size();
        List<Integer> integers = RandomUtils.randomDistinctInts(0, size - 1, 3);
        List<ProductDto> getProducts = new ArrayList<>();
        BigDecimal totalPrice = new BigDecimal("0");
        //随机取商品，随机取数量
        for (Integer integer : integers) {
            ProductEntity productEntity = data.get(integer);
            ProductDto productDto = new ProductDto();
            productDto.setId(productEntity.getId());
            productDto.setPrice(productEntity.getPrice());
            if (productEntity.getStock() == 0) continue;
            int i = RandomUtils.randomInt(1, productEntity.getStock());
            productDto.setQuantity(i);
            BigDecimal multiply = productEntity.getPrice().multiply(new BigDecimal(i));
            productDto.setTotalPrice(multiply);
            totalPrice = totalPrice.add(multiply);
            getProducts.add(productDto);
        }
        CreateOrderDto createOrderDto = new CreateOrderDto();
        createOrderDto.setTotalPrice(totalPrice);
        createOrderDto.setPayPrice(totalPrice);
        createOrderDto.setProducts(getProducts);
        return createOrderDto;
    }
}
```

## 注意
```text
    1. 当前这个系统遇到过openfeign 调用的时候,不会走gateway 导致网关里的做的事情都不生效了.比如解析jwt 设置用户的消息头.
    对应的解决办法就是写一个RequestInterceptor 拦截器,请求里面的消息头设置到openfeign 的请求里面去,这样就可以正常请求了.
    这个的原理就是openfeign 会去调用所有的RequestInterceptor 拦截器,那么这个时候就可以把请求里面的相关信息设置到openfeign 
    请求里,当成正常的一个业务请求
```
```java
//这个非常重要.
//这个是在openfeign请求的时候不走网关导致的网关写的功能失效.
//目前就是为了解决网关过滤器解析jwt 生成消息头的问题.
@Configuration
public class FeignHeaderInterceptor {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return (RequestTemplate template) -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String userId = request.getHeader("SSC-User-Id");
                String username = request.getHeader("SSC-User-Name");
                if (userId != null) {
                    template.header("SSC-User-Id", userId);
                }
                if (username != null) {
                    template.header("SSC-User-Name", username);
                }
            }
        };
    }
}
```
