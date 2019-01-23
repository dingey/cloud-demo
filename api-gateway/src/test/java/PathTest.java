import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

@Slf4j
public class PathTest {
    @Test
    public void test() {
        PathMatcher matcher = new AntPathMatcher();
        //完全路径url方式路径匹配                                                                                            
        //String requestPath="http://localhost:8080/pub/login.jsp";//请求路径                                                                                                                                                                                            
        //String patternPath="**/login.jsp";//路径匹配模式
        //不完整路径uri方式路径匹配
        //String requestPath="/app/pub/login.do";//请求路径
        //String patternPath="/**/login.do";//路径匹配模式
        //模糊路径方式匹配
        //String requestPath="/app/pub/login.do";//请求路径
        //String patternPath="/**/*.do";//路径匹配模式
        //包含模糊单字符路径匹配
        String requestPath = "/app/user/1";//请求路径
        String patternPath = "/app/user/{id}";//路径匹配模式
        boolean result = matcher.match(patternPath, requestPath);
        log.info("{}", result);
    }
}
