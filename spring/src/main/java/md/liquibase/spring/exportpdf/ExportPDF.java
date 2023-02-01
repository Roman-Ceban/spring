package md.liquibase.spring.exportpdf;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import md.liquibase.spring.model.Users;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ExportPDF {
    public class PDFGeneratorUtility {

        public static void usersDetailReport(HttpServletResponse response, List<Users> users) throws IOException {

            PdfWriter writer = new PdfWriter(response.getOutputStream());
            PdfDocument pdfDocument;
            pdfDocument = new PdfDocument(writer);

            Document document = new Document(pdfDocument, PageSize.A4.rotate());

            try {
                document.add(new Paragraph("Export users PDF").setBold().setPaddingLeft(200f));
                Table table = new Table(UnitValue.createPercentArray(new float[]{1f, 2f, 3f, 3f, 3f, 5f, 2f, 2f, 5f, 5f, 5f, 5f, 5f}));
                table.setWidth(UnitValue.createPercentValue(100))
                        .setPadding(0)
                        .setFontSize(9);

                Cell cell1 = new Cell();
                cell1.setTextAlignment(TextAlignment.CENTER);
                table.addCell(new Cell().add("ID").setBold());
                table.addCell(new Cell().add("Name").setBold());
                table.addCell(new Cell().add("Username").setBold());
                table.addCell(new Cell().add("Phone").setBold());
                table.addCell(new Cell().add("Email").setBold());
                table.addCell(new Cell().add("Website").setBold());
                table.addCell(new Cell().add("Street").setBold());
                table.addCell(new Cell().add("Suite").setBold());
                table.addCell(new Cell().add("City").setBold());
                table.addCell(new Cell().add("ZipCode").setBold());
                table.addCell(new Cell().add("NameCompany").setBold());
                table.addCell(new Cell().add("CatchPhras").setBold());
                table.addCell(new Cell().add("BS").setBold());

                users.forEach(users1 -> {
                    table.addCell(new Cell().add(String.valueOf(users1.getId())));
                    table.addCell(new Cell().add(String.valueOf(users1.getName())));
                    table.addCell(new Cell().add(String.valueOf(users1.getUsername())));
                    table.addCell(new Cell().add(String.valueOf(users1.getPhone())));
                    table.addCell(new Cell().add(String.valueOf(users1.getEmail())));
                    table.addCell(new Cell().add(String.valueOf(users1.getWebsite())));
                    table.addCell(new Cell().add(String.valueOf(users1.getAddress().getStreet())));
                    table.addCell(new Cell().add(String.valueOf(users1.getAddress().getSuite())));
                    table.addCell(new Cell().add(String.valueOf(users1.getAddress().getCity())));
                    table.addCell(new Cell().add(String.valueOf(users1.getAddress().getZipcode())));
                    table.addCell(new Cell().add(String.valueOf(users1.getCompany().getName())));
                    table.addCell(new Cell().add(String.valueOf(users1.getCompany().getCatchPhrase())));
                    table.addCell(new Cell().add(String.valueOf(users1.getCompany().getBs())));

                });

                document.add(table);
                document.close();
                writer.flush();
                writer.close();
            } catch (IOException e) { e.printStackTrace();
            }
        }
    }
}
