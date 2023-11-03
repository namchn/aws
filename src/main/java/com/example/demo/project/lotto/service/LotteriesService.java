package com.example.demo.project.lotto.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;


//@Component
public class LotteriesService {
	
	private final NumberGenerator numberGenerator;

    public LotteriesService(NumberGenerator numberGenerator) {
        this.numberGenerator = numberGenerator;
    }

    public List<Integer>  create(int numberLength,int numberSize, boolean same) {
        int length = numberLength;
        
        // ...로또 자동
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < length; i++) {
        	
        	//double number = (double)numberGenerator.generateFrom();
        	//double number = run();
        	//int num = (int)(number * numberSize+1);
        	//Lotteries lottos = Lotteries.builder().num(num).build();
        	
        	
        	int n = 0;
        	boolean re = true ; 
        	while(re) {
        		
        		double number = (double)numberGenerator.generateFrom();
            	//double number = run();
            	int num = (int)(number * numberSize+1);
            	if(   !list.contains(num)   ) {
            		//double number = (double)numberGenerator.generateFrom();	
            		// (double)numberGenerator.generateFrom();
            		n = num;
            		re = false ; 
            	}
            	if(same) { //중복허용시 
            		n = num;
            		re = false ; 
            	}
            	
        	}
        	list.add(n);
        }
        
        
        
        return list;
    }
    
    
    public double run() {
    	return (double)numberGenerator.generateFrom();
    }
	
}
