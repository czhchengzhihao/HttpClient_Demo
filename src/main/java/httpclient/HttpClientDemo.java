package httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import java.io.IOException;

public class HttpClientDemo {
    @Test
    public void test_1() throws IOException {
        //用来存放结果
        String result;
        HttpGet get=new HttpGet("http://baidu.com/");
        //用来执行get方法
        HttpClient client = new DefaultHttpClient();
        //DefaultHttpClient替代
        //HttpClient client = HttpClientBuilder.create().build();//获取DefaultHttpClient请求
        HttpResponse response= client.execute(get);
       /* 获取响应的内容response.getEntity();
       * 转换为字符串EntityUtils.toString()
       * */
        result=EntityUtils.toString(response.getEntity(),"UTF-8");
        System.out.println(result);
    }
}
