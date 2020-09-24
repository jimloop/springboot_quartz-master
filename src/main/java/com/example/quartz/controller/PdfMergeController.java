package com.example.quartz.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@RestController
public class PdfMergeController {

    @RequestMapping("/mergePdf")
    public void pdfMerge(){
        String[] files={"H:\\文件\\精通Spring MVC 4.pdf",
                "H:\\文件\\精通Spring MVC 4 - 副本.pdf",
                "H:\\文件\\精通Spring MVC 4 - 副本 (2).pdf",
                "H:\\文件\\精通Spring MVC 4 - 副本 (3).pdf",
                "H:\\文件\\精通Spring MVC 4 - 副本 (4).pdf",
                "H:\\文件\\精通Spring MVC 4 - 副本 (5).pdf",
                "H:\\文件\\精通Spring MVC 4 - 副本 (6).pdf",
                "H:\\文件\\精通Spring MVC 4 - 副本 (7).pdf",
                "H:\\文件\\精通Spring MVC 4 - 副本 (8).pdf"
        };
        String savePath="D:\\AllMerge.pdf";
        try {
            Document document=new Document(new PdfReader(files[0]).getPageSize(1));
            PdfCopy copy= new PdfCopy(document,new FileOutputStream(savePath));
            document.open();
            for(int i=0;i<files.length;i++){
                PdfReader reader=new PdfReader((files[i]));
                int size=reader.getNumberOfPages();
                for(int j=1;j<=size;j++){
                    document.newPage();
                    PdfImportedPage page=copy.getImportedPage(reader,j);
                    copy.addPage(page);
            }
            }
            document.close();

        }catch (IOException | DocumentException e){
            e.printStackTrace();
        }
    }

    @RequestMapping("/cutPdfByPage")
    public void getMergePdfByPages(){

    }
}
