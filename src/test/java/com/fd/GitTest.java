package com.fd;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GitTest {

    @Test
    public void test1() {

        System.out.println("dev分支做的修改");
    }
}
