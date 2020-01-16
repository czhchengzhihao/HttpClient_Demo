package httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MyCookiesForPost {
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
        /*for (int i = 0; i < cookieList.size() ; i++) {9
        }*/
        for (Cookie cookie : cookieList) {
            String name = cookie.getName();
            String value = cookie.getValue();
            System.out.println("cookies name:" + name + ";cookies value:" + value);
        }
    }

    //依赖
    @Test(dependsOnMethods = {"test_getCookies"})
    public void testPostWithCookies() throws IOException {
        String uri = bundle.getString("test.post.with.cookies");
        String testUrl = url + uri;
        //声明一个Client对象用来方法执行
        DefaultHttpClient client = new DefaultHttpClient();
        //声明一个方法，这个方法就是post方法
        HttpPost post = new HttpPost(testUrl);
        //添加参数,声明json对象
        JSONObject param = new JSONObject();
        param.put("name", "huhansan");
        param.put("age", "18");
        //设置请求头信息 设置header
        post.setHeader("content-type", "application/json");
        //将参数信息添加到方法中
        StringEntity entity = new StringEntity(param.toString(), "UTF-8");
        post.setEntity(entity);
        //声明一个对象来进行响应结果的存储
        String result;
        //设置cookies信息
        client.setCookieStore(cookieStore);
        //执行post请求
        HttpResponse response = client.execute(post);
        //获取响应状态码
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("响应状态码：" + statusCode);
        if (statusCode == 200) {
            System.out.println("响应成功");
        }
        //获取响应结果
        result = EntityUtils.toString(response.getEntity(), "UTF-8");
        System.out.println(result);
        //处理结果，判断结果是否符合预期
        //将返回的响应结果字符串转化成为json对象
        JSONObject resultJson = new JSONObject(result);
        //获取到结果值
        //具体判断返回结果值
        String key = (String) resultJson.get("huhansan");
        //器期望结果和实际结果的比对
        Assert.assertEquals("success", key);
        String value = (String) resultJson.get("status");
        Assert.assertEquals("1", value);
    }
}

