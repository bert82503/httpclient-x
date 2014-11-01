package cn.fraudmetrix.http.impl.client;

import org.apache.http.impl.client.DefaultUserTokenHandler;
import org.apache.http.protocol.HttpContext;

/**
 * 类CustomUserTokenHandler.java的实现描述：自定义用户标记的{@link org.apache.http.client.UserTokenHandler UserTokenHandler}实现类。
 * 
 * @author huagang.li 2014年11月1日 下午7:05:05
 */
public class CustomUserTokenHandler extends DefaultUserTokenHandler {

    private String userToken;

    public CustomUserTokenHandler(String userToken){
        this.userToken = userToken;
    }

    @Override
    public Object getUserToken(final HttpContext context) {
        Object userPrincipal = super.getUserToken(context);
        if (userPrincipal == null) {
            userPrincipal = context.getAttribute(this.userToken);
        }
        return userPrincipal;
    }

}
