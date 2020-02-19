package com.mezyapps.new_reportanalyst.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class CommonPDFGenerate {

    private File pdfFile;
    private Context mContect;

    public CommonPDFGenerate(Context mContect) {
        this.mContect = mContect;
    }


    public void createPDF(String h1, String h2, String h3, String h4, List<Map<String, String>> list, String columnstr) throws FileNotFoundException, DocumentException {
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/REPORT_ANALYST_TDG");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i("TAG", "Created a new directory for PDF");
        }

        String pdfname = "REPORT.pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document(PageSize.A4);
        document.setMargins(20, 20, 20, 20);
        float fontSize = 12.0f;


        PdfWriter.getInstance(document, output);
        document.open();

        //Document Title
        if (h1 != null && !h1.equalsIgnoreCase("")) {
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 25.0f, Font.BOLD, BaseColor.BLACK);
            addNewItem(document, h1, Element.ALIGN_CENTER, titleFont);
        }
        if (h2 != null && !h2.equalsIgnoreCase("")) {
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.NORMAL, BaseColor.BLACK);
            addNewItem(document, h2, Element.ALIGN_CENTER, titleFont);
        }
        if (h3 != null && !h3.equalsIgnoreCase("")) {
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.NORMAL, BaseColor.BLACK);
            addNewItem(document, h3, Element.ALIGN_CENTER, titleFont);
        }
        if (h4 != null && !h4.equalsIgnoreCase("")) {
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.NORMAL, BaseColor.BLACK);
            addNewItem(document, h4, Element.ALIGN_CENTER, titleFont);
        }
        addLineSeperator(document);

        String colName[] = columnstr.split(",");
        float folatcell[] = new float[colName.length];
        for (int i = 0; i < colName.length; i++) {
            String cnst[] = colName[i].split("#");
            folatcell[i] = Float.parseFloat(cnst[2]);
        }


        PdfPTable table= new PdfPTable(folatcell);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(20);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        for (int i = 0; i < colName.length; i++) {
            String cnst[] = colName[i].split("#");
            table.addCell(cnst[1]);
        }

        table.setHeaderRows(1);

        PdfPCell[] cells = table.getRow(0).getCells();
        for (PdfPCell cell : cells) {
            cell.setBackgroundColor(BaseColor.GRAY);

        }

       // int sr_no = 1;
        for (Map<String, String> map : list) {
            //table.addCell(String.valueOf(sr_no));
            for (int i = 0; i < colName.length; i++) {
                for (Map.Entry<String, String> mapEntry : map.entrySet()) {
                    String key = mapEntry.getKey();
                    String value = mapEntry.getValue();
                    String cnst[] = colName[i].split("#");
                    if (key.equalsIgnoreCase(cnst[0])) {

                        table.addCell(String.valueOf(value));

                    }
                }
            }
            //sr_no++;
        }
        document.add(table);
        document.close();
        openGeneratedPDF();
    }

    public void addNewItem(Document document, String title, int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk(title, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);
    }

    private void addLineSeperator(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);
    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }


    private void openGeneratedPDF() {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/REPORT_ANALYST_TDG/REPORT.pdf");
            if (file.exists()) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri uri = FileProvider.getUriForFile(mContect, mContect.getPackageName() + ".fileprovider", file);
                intent.setDataAndType(uri, "application/pdf");

                // validate that the device can open your File!
                PackageManager pm = mContect.getPackageManager();
                if (intent.resolveActivity(pm) != null) {
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    ((Activity) mContect).startActivityForResult(Intent.createChooser(intent, "Backup"), 100);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
