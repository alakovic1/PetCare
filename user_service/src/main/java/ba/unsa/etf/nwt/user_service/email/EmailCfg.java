package ba.unsa.etf.nwt.user_service.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class EmailCfg {
    @Value("${spring.mail.host: smtp.gmail.com}")
    private String host;

    @Value("${spring.mail.port: 222}")
    private int port;

    @Value("${spring.mail.username: username}")
    private String username;

    @Value("${spring.mail.password: password}")
    private String password;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
