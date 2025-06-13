package com.savingRate.SavingRate.Utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.savingRate.SavingRate.Model.Income;
import com.savingRate.SavingRate.Model.InvoiceItem;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

public class InvoiceUtillty {

    public static void exportIncomeInvoice(String filePath, List<Income> incomeList, String invoiceNumber, LocalDate date, String clientName) {
        List<InvoiceItem> itemList = incomeList.stream()
                .map(i -> new InvoiceItem(i.getDate().toLocalDate(), i.getDescription(), i.getValue(), false))
                .toList();
        exportUnifiedInvoice(filePath, itemList, invoiceNumber, date, clientName);
    }

    public static void exportUnifiedInvoice(String filePath, List<InvoiceItem> items, String invoiceNumber, LocalDate date, String clientName) {
        Document document = new Document(PageSize.A4, 40, 40, 10, 10);

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            BaseColor darkGreen = new BaseColor(0, 128, 100);
            BaseColor lightGreen = new BaseColor(220, 255, 244);
            BaseColor white = BaseColor.WHITE;
            BaseColor black = BaseColor.BLACK;
            BaseColor gray = new BaseColor(100, 100, 100);

            Font logoFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, darkGreen);
            Font invoiceFont = new Font(Font.FontFamily.HELVETICA, 25, Font.BOLD, darkGreen);
            Font subTextFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, black);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, white);
            Font cellFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, black);
            Font boldBlack = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, black);
            Font boldGreen = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, darkGreen);
            Font italicGray = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, gray);

            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{2f, 3f});

            PdfPCell logoCell = new PdfPCell();
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setVerticalAlignment(Element.ALIGN_TOP);

            try {
                Image logo = Image.getInstance("src/main/resources/images/logozo.png");
                logo.scaleToFit(150, 150);
                logo.setAlignment(Image.ALIGN_LEFT);
                logoCell.addElement(logo);
            } catch (Exception imgEx) {
                Paragraph altLogo = new Paragraph("LOGOZO", logoFont);
                altLogo.setAlignment(Element.ALIGN_LEFT);
                logoCell.addElement(altLogo);
            }

            PdfPCell invoiceCell = new PdfPCell();
            invoiceCell.setBorder(Rectangle.NO_BORDER);
            invoiceCell.setVerticalAlignment(Element.ALIGN_TOP);

            Paragraph title = new Paragraph("INVOICE", invoiceFont);
            title.setAlignment(Element.ALIGN_RIGHT);
            invoiceCell.addElement(title);
            invoiceCell.addElement(new Paragraph(" "));

            Paragraph invNum = new Paragraph("INVOICE #: " + invoiceNumber, cellFont);
            invNum.setAlignment(Element.ALIGN_RIGHT);
            invoiceCell.addElement(invNum);

            Paragraph invDate = new Paragraph("DATE: " + date.toString(), cellFont);
            invDate.setAlignment(Element.ALIGN_RIGHT);
            invoiceCell.addElement(invDate);

            Paragraph dueDate = new Paragraph("DUE DATE: " + date.plusDays(10), cellFont);
            dueDate.setAlignment(Element.ALIGN_RIGHT);
            invoiceCell.addElement(dueDate);

            headerTable.addCell(logoCell);
            headerTable.addCell(invoiceCell);
            document.add(headerTable);
            document.add(new Paragraph(" "));

            PdfPTable info = new PdfPTable(1);
            info.setWidthPercentage(100);
            info.setSpacingBefore(5);

            PdfPCell billTo = new PdfPCell();
            billTo.setBorder(Rectangle.NO_BORDER);
            billTo.addElement(new Paragraph("BILL TO:", boldBlack));
            billTo.addElement(new Paragraph(clientName, cellFont));
            billTo.addElement(new Paragraph("Phone: ", cellFont));

            info.addCell(billTo);
            document.add(info);
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{2, 6, 2});
            table.setSpacingBefore(10);

            String[] headers = {"Date", "Description", "Amount (Rs)"};
            for (String h : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(h, headerFont));
                headerCell.setBackgroundColor(darkGreen);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setPadding(8);
                table.addCell(headerCell);
            }

            boolean even = true;
            double subTotal = 0;
            for (InvoiceItem item : items) {
                BaseColor bg = even ? white : lightGreen;
                addStyledCell(table, item.getDate().toString(), cellFont, bg);
                addStyledCell(table, item.getDescription(), cellFont, bg);
                addStyledCell(table, String.format("%.2f", item.getValue()), cellFont, bg);
                subTotal += item.getValue();
                even = !even;
            }

            if (items.isEmpty()) {
                PdfPCell empty = new PdfPCell(new Phrase("No records available.", cellFont));
                empty.setColspan(3);
                empty.setHorizontalAlignment(Element.ALIGN_CENTER);
                empty.setBackgroundColor(lightGreen);
                empty.setPadding(10);
                table.addCell(empty);
            }

            document.add(table);
            document.add(new Paragraph(" "));

            double tax = subTotal * 0;
            double discount = subTotal * 0;
            double total = subTotal + tax - discount;

            PdfPTable totals = new PdfPTable(2);
            totals.setTotalWidth(240);
            totals.setWidths(new float[]{2, 1});
            totals.setLockedWidth(true);
            totals.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totals.setSpacingBefore(5);

            totals.addCell(getNoBorderCell("Sub Total", cellFont));
            totals.addCell(getNoBorderCell("Rs. " + String.format("%.2f", subTotal), cellFont));
//            totals.addCell(getNoBorderCell("Tax (0%)", cellFont));
//            totals.addCell(getNoBorderCell("Rs. " + String.format("%.2f", tax), cellFont));
            totals.addCell(getNoBorderCell("Discount (0%)", cellFont));
            totals.addCell(getNoBorderCell("Rs. " + String.format("%.2f", discount), cellFont));
            totals.addCell(getNoBorderCell("Grand Total", boldBlack));
            totals.addCell(getNoBorderCell("Rs. " + String.format("%.2f", total), boldGreen));

            document.add(totals);
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Payment Details", boldBlack));
            document.add(new Paragraph("Bank: NSB Bank \nAccount No: 100970310425\nName: S G D C DEWMIN\nBranch: Tissamaharama", cellFont));
            document.add(new Paragraph("Please make the payment by due date.", italicGray));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Thank you for your business!", boldGreen));
            document.add(new Paragraph("If you have any questions, contact us at: logozo.info@gmail.com | www.logozoai.com", italicGray));

            try {
                Image seal = Image.getInstance("src/main/resources/images/logozoseal.png");
                seal.scaleToFit(130, 130);
                seal.setAlignment(Image.ALIGN_RIGHT);
                document.add(seal);
            } catch (Exception imgEx) {
                System.out.println("Seal image not found.");
            }

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addStyledCell(PdfPTable table, String text, Font font, BaseColor bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bgColor);
        cell.setPadding(7);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
    }

    private static PdfPCell getNoBorderCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(5);
        return cell;
    }
}
