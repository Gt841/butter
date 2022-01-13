package com.rswy.getopenid;

import com.rswy.getopenid.domain.GZHProps;
import com.rswy.getopenid.domain.WXMPProps;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class H5getOpenidApplicationTests {
    @Autowired
    private GZHProps GZHProps;
    @Autowired
    private WXMPProps WXMPProps;

    @Test
    void contextLoads() {
        //显示一下公众号的参数
        System.out.println(GZHProps.toString());
        System.out.println(WXMPProps);
    }

}
