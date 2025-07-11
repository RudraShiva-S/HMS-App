package com.hms.util;

import com.hms.entity.Property;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;


@Service
public class PDFService {
    public void generateBookingPdf(String filePath, Property property){
      try {
          Document document = new Document();
          PdfWriter.getInstance(document, new FileOutputStream(filePath));

          document.open();

         // Document document = new Document();
          //PdfWriter.getInstance(document, new FileOutputStream(filePath));
         // document.open();
          PdfPTable table = new PdfPTable(2);
        {
              table.addCell("Id");
              table.addCell(property.getId().toString());
              table.addCell("Name");
              table.addCell(property.getName());
            table.addCell("no of guests");
            table.addCell(property.getNo_of_guest().toString());
          }
          document.add(table);
          document.close();

          Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
          Chunk chunk = new Chunk("Hello World", font);

          document.add(chunk);
          document.close();

      } catch (Exception e) {
          e.printStackTrace();
      }
    }
}
