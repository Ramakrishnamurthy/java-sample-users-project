package com.example.demo.controller;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;

import com.example.demo.controller.FileController;
import com.example.demo.service.FileService;

@SuppressWarnings("deprecation")
@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class FileControllerTest {
	
	private MockMvc mockMvc;
	
    @Mock
    private FileService fileService;
    
    @InjectMocks
    private FileController fileController;

	@Before
	public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(fileController).build();
	}

	@Test
	public void testCreateFile() throws Exception {
        String filename = "test.txt";
        String expectedResponse = "File created";
        when(fileService.createFile(filename, "Hello")).thenReturn("File created");
        mockMvc.perform(MockMvcRequestBuilders.post("/file/create")
                .param("filename", filename)
                .param("content", "Hello")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

	@Test
	public void testReadFile() throws Exception{
        String filename="test.txt";
        String expectedResponse ="Read success";
        when(fileService.readFile(filename)).thenReturn(expectedResponse);
        mockMvc.perform(get("/file/read")
                .param("filename", filename))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

	@Test
	public void testDeleteFile() throws Exception{
        String filename="test.txt";
        String expectedResult="File deleted successfully";
        when(fileService.deleteFile(filename)).thenReturn("File deleted successfully");
        mockMvc.perform(delete("/file/delete")
            .param("filename", filename))
        .andExpect(status().isOk())
        .andExpect(content().string(expectedResult));
    }
}
