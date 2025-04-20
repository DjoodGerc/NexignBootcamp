package brtApp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class TestClass {

    @Test
    public void test(){
        LocalDateTime ldt=LocalDateTime.of(2024,2,31,2,3);
        System.out.println(ldt);
    }
}
