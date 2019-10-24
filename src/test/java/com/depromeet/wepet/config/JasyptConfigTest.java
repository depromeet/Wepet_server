package com.depromeet.wepet.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JasyptConfigTest {

    @Test
    public void test() {
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword(System.getProperty("password")); //2번 설정의 암호화 키를 입력

        String enc = pbeEnc.encrypt("wepet"); //암호화 할 내용
        System.out.println("enc = " + enc); //암호화 한 내용을 출력

    }
}
