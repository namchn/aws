package com.example.demo._config.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.project.lotto.controller.LottoController;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("test")
public class RedisController_ {
//레디스 테스트

    @Autowired
    RedisService redisService;

    @GetMapping(value = "/redis/setString")
    @ResponseBody
    public void setValue(String testkey, String testvalue){
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        redisService.setValues(testkey,testvalue);
    }

    @GetMapping(value = "/redis")
    @ResponseBody
    public String getValue(){
   //public String getValue(String testkey){ 	
        //return redisService.getValues(testkey);
        return redisService.getValues("key2");
    }


    @GetMapping(value = "/redis/setSets")
    @ResponseBody
    public void setSets(String testkey,String... testvalues){
        redisService.setSets(testkey,testvalues);
    }

    @GetMapping(value = "/redis/getSets")
    @ResponseBody
    public Set getSets(String key){
        return redisService.getSets(key);
    }

}