package com.example.demo.project.ip.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Component;

import com.example.demo.project.ip.block.repository.IPRepository;
import com.example.demo.project.ip.block.repository.IpListEntity;

import lombok.Getter;

@Component
public class IpAddressMatcherManager {

    //@Getter
    //private List<IpAddressMatcher> ipAddressMatchers;
	
	//  public IpAddressMatcherManager() {
	//  this.ipAddressMatchers = Arrays.asList(
	//          new IpAddressMatcher("192.168.1.0/24"),
	//          new IpAddressMatcher("192.168.2.0/24")
	//  );
	//}
	
    @Autowired
    private  IPRepository iPRepository;
    
    public void addNotAccessIp(String ipAddress) { //접근 금지ip 등록
    	//IpListEntity ipEntity = IpListEntity.builder().ip("192.168.3.0").build();
    	IpListEntity ipEntity = IpListEntity.builder().ip(ipAddress).build();
    	Optional<IpListEntity> Optional = iPRepository.findByIp(ipEntity.getIp());
    	
    	if(!Optional.isPresent()) iPRepository.save(ipEntity);
    }
    
    public boolean isNotAccessible(String ipAddress) { //접근 금지ip 확인
        
    	List<IpAddressMatcher> ipAddressMatchers = new ArrayList<IpAddressMatcher>(); 
    
        List<IpListEntity> lists = iPRepository.findAll();
        // 192.168.3.0 , 0:0:0:0:0:0:0:1..등으로 등록되어 있음.
        
        for(IpListEntity ip : lists ) {
        	ipAddressMatchers.add(new IpAddressMatcher(ip.getIp()));
        }

        //List<IpAddressMatcher> ipAddressMatchers = this.getIpAddressMatchers();
        return ipAddressMatchers.stream().anyMatch(matcher -> matcher.matches(ipAddress));
    }
}