package com.savingRate.SavingRate.Utils;

import com.savingRate.SavingRate.Model.Expences;
import com.savingRate.SavingRate.Model.ExpenseDAO;
import com.savingRate.SavingRate.Model.Income;
import com.savingRate.SavingRate.Model.IncomeDAO;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;

public class ExportUtility {

    // Export to Excel (.xlsx) using Apache POI
    public static void exportToExcel(String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Budget Data");

            // Header row
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Type");
            header.createCell(1).setCellValue("Date");
            header.createCell(2).setCellValue("Description");
            header.createCell(3).setCellValue("Value");
            header.createCell(4).setCellValue("Category");

            int rowIdx = 1;

            // Income data
            for (Income income : IncomeDAO.getAllIncome()) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue("Income");
                row.createCell(1).setCellValue(income.getDate().toString());
                row.createCell(2).setCellValue(income.getDescription());
                row.createCell(3).setCellValue(income.getValue());
                row.createCell(4).setCellValue("-");
            }

            // Expense data
            for (Expences expense : ExpenseDAO.getAllExpenses()) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue("Expense");
                row.createCell(1).setCellValue(expense.getDate().toString());
                row.createCell(2).setCellValue(expense.getDescription());
                row.createCell(3).setCellValue(expense.getValue());
                row.createCell(4).setCellValue(expense.getCategory());
            }

            // Save the Excel file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
                System.out.println("✅ Excel exported to " + filePath);
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to export Excel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Export to PDF using iText (v2.1.7 open source)
    public static void exportToPDF(String filePath) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph title = new Paragraph("Monthly Budget Summary", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); // spacer

            PdfPTable table = new PdfPTable(5);
            table.setWidths(new int[]{2, 3, 4, 2, 2});
            table.addCell("Type");
            table.addCell("Date");
            table.addCell("Description");
            table.addCell("Value");
            table.addCell("Category");

            // Income data
            for (Income income : IncomeDAO.getAllIncome()) {
                table.addCell("Income");
                table.addCell(income.getDate().toString());
                table.addCell(income.getDescription());
                table.addCell(String.valueOf(income.getValue()));
                table.addCell("-");
            }

            // Expense data
            for (Expences expense : ExpenseDAO.getAllExpenses()) {
                table.addCell("Expense");
                table.addCell(expense.getDate().toString());
                table.addCell(expense.getDescription());
                table.addCell(String.valueOf(expense.getValue()));
                table.addCell(expense.getCategory());
            }

            document.add(table);
            document.close();

            System.out.println("✅ PDF exported to " + filePath);
        } catch (Exception e) {
            System.err.println("❌ Failed to export PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
