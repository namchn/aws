package com.example.demo.project.file.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.project.file.service.JsonReadService;
import com.example.demo.project.user.service.UserService;
import com.example.frame.dto.ResponseDTO;


@RestController
@RequestMapping("file")
public class FileController  {

  @Autowired  //알아서 빈을 찾아서 인스턴스 가져오는 것
  private UserService service;
  
  @Autowired  //알아서 빈을 찾아서 인스턴스 가져오는 것
  private JsonReadService jsonReadService;

    @GetMapping("/zip")
    public void zip(HttpServletResponse response , HttpServletRequest req) throws IOException  {
        File dir = new File("D:\\chunwoo\\node");
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
 
        response.setContentType("application/zip");
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"files.zip\"");
 
        try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
            for (File file : files) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zos.putNextEntry(zipEntry);
 
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = fis.read(buffer)) != -1) {
                        zos.write(buffer, 0, read);
                    }
                    zos.closeEntry();
                }
            }
        }
    }
	
    @GetMapping("excel")
    public void excel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=sample.xlsx");
 
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("sheet1");
 
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            for (int i = 0; i < 10_000; i++) {
                sheet.createRow(i).createCell(0).setCellValue("hello world" + i);
                if (i % 1000 == 0) {
                    workbook.write(outputStream);
                    outputStream.flush();
                }
            }
            workbook.write(outputStream);
            outputStream.flush();
            workbook.close();
        }
    }
	
	

    @GetMapping("write")
    public ResponseEntity<?> write(HttpServletResponse response) throws IOException {
        //response.setContentType("application/vnd.ms-excel");
        //response.setHeader("Content-Disposition", "attachment; filename=sample.xlsx");
 
        JSONObject obj = new JSONObject();
		obj.put("name", "mine-it-record");
		obj.put("mine", "GN");
		obj.put("year", 2021);
		obj.put("query", "SELECT new com.example.demo.model.JoinEntity(t.id ,u.username , t.userId , t.title , u.authProvider) FROM UserEntity u left outer  join  TodoEntity t on  u.id = t.userId ");

		//JSONArray  arr = new JSONArray();
		

        String path = "D://json"; //폴더 경로
    	File Folder = new File(path);
    	// 해당 디렉토리가 없다면 디렉토리를 생성.
    	if (!Folder.exists()) {
    		Folder.mkdir(); //폴더 생성합니다. ("새폴더"만 생성)
    		System.out.println("새폴더 생성.");
    	}
		FileWriter file = new FileWriter("D://json/mine.json");
		file.write(obj.toJSONString());
		file.flush();
		file.close();
       
		return ResponseEntity.ok().body("json 생성");
    }
	
    
    
	
    @GetMapping("read")
    public ResponseEntity<?> read(HttpServletResponse response) throws IOException, ParseException {
        //response.setContentType("application/vnd.ms-excel");
        //response.setHeader("Content-Disposition", "attachment; filename=sample.xlsx");
 
        JSONParser parser = new JSONParser();
        
		FileReader reader = new FileReader("D://json/mine.json");
		Object obj = parser.parse(reader);
		JSONObject jsonObject = (JSONObject) obj;
		
		reader.close();
		
		System.out.println("======getQuery=========");
		System.out.println(jsonReadService.JsonRead());
			
		List<JSONObject> dtos = new ArrayList<JSONObject>();
		dtos.add(jsonObject);
		// list.add("Hello World! I'm ResponseEntity. And you got 400!");
		ResponseDTO<JSONObject> res = ResponseDTO.<JSONObject>builder()
															.error("")
															.data(dtos)
															.build();
		// (7) ResponseDTO를 리턴한다.
		return ResponseEntity.ok().body(res);
    }
	
  
  

}
