package crmApp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class test {

    @Test
    void t(){
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(10);
        System.out.println(encoder.matches("admin","$2y$10$fMopG3eXQMpCAmixYiYeQuPXOoQ/.yjdE8NH/eZB0MDjEOcfrKMTm"));
    }
}
