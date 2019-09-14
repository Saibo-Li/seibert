package socket;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by JackLi on 2019/8/10 20:42.
 */
public class TestSocket {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress host = InetAddress.getLocalHost();
        String ip =host.getHostAddress();
        System.out.println("本机ip地址：" + ip);
    }
}
