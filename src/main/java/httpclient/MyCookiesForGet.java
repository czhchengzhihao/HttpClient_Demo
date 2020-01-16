package httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MyCookiesForGet {
    private String url;
    private ResourceBundle bundle;
    private CookieStore cookieStore;

    //测试之前加载出来
    @BeforeTest
    public void before() {
        //自动加载配置文件（配置文件必须在resources下）
        bundle = ResourceBundle.getBundle("application", Locale.CHINA);
        url = bundle.getString("test.url");
    }

    @Test
    public void test_getCookies() throws IOException {
        String result;
        String uri = bundle.getString("getCookies.uri");
        String testUrl = url + uri;
        HttpGet get = new HttpGet(testUrl);
        //用来执行get方法
        DefaultHttpClient client = new DefaultHttpClient();
        //DefaultHttpClient替代
        //HttpClient client = HttpClientBuilder.create().build();//获取DefaultHttpClient请求
        //执行
        HttpResponse response = client.execute(get);
        result = EntityUtils.toString(response.getEntity(), "UTF-8");
        System.out.println(result);
        //获取cookies信息
        cookieStore = client.getCookieStore();
        List<Cookie> cookieList = cookieStore.getCookies();
        /*for (int i = 0; i < cookieList.size() ; i++) {

        }*/
        for (Cookie cookie : cookieList) {
            String name = cookie.getName();
            String value = cookie.getValue();
            System.out.println("cookies name1:" + name + ";cookies value:" + value);
        }
    }

    //依赖
    @Test(dependsOnMethods = {"test_getCookies"})
    public void testGetWithCookies() throws IOException {
        String uri = bundle.getString("test.get.with.cookies");
        String testUrl = this.url + uri;
        HttpGet get = new HttpGet(testUrl);
        DefaultHttpClient client = new DefaultHttpClient();
        //设置cookies信息
        client.setCookieStore(cookieStore);
        //执行get请求
        HttpResponse response = client.execute(get);
        //获取响应状态码
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("响应状态码：" + statusCode);
        if (statusCode == 200) {
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            System.out.println(result);
        }
    }
}
