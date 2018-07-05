package sps.vn.learning.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
public class PdfDataController {


    private final String CORE_SPRING_FILE_NAME = "core-spring-4.2.a.RELEASE-student-handout-with-fixes.pdf";
    private final String APPLICATION_PDF = "application/pdf";

    @RequestMapping(value = "/view/core-spring", produces = APPLICATION_PDF)
    public @ResponseBody HttpEntity<byte[]> viewBookContentInAnotherTab() throws IOException {
        File file = getFile();
        byte[] document = FileCopyUtils.copyToByteArray(file);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "pdf"));
        header.set("Content-Disposition", "inline; filename=" + "\"core spring.pdf\"");
        header.setContentLength(document.length);

        return new HttpEntity<>(document, header);
    }

    @RequestMapping(value = "/save", method = RequestMethod.GET, produces = APPLICATION_PDF)
    public @ResponseBody void saveFile(HttpServletResponse response) throws IOException {
        File file = getFile();
        InputStream in = new FileInputStream(file);

        response.setContentType(APPLICATION_PDF);
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));

        FileCopyUtils.copy(in, response.getOutputStream());
    }

    private File getFile() throws IOException {
        File file = new File(getClass().getClassLoader().getResource(CORE_SPRING_FILE_NAME).getFile());
        if (!file.exists()){
            throw new FileNotFoundException("file with path: " + CORE_SPRING_FILE_NAME + " was not found.");
        }
        return file;
    }
}
