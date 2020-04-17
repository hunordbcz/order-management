package presentation;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.List;

public class View<T> {
    PdfPTable table;
    Chunk text;

    public View() {
        text = null;
        table = null;
    }

    public void print(String input) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(input));
            document.open();
            if (text != null) {
                document.add(text);
            }
            if (table != null) {
                document.add(table);
            }
            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            text = null;
            table = null;
        }
    }

    private String[] getDeclaredFields(T obj) {
        String[] fields = new String[obj.getClass().getDeclaredFields().length];
        int size = 0;
        for (Field field : obj.getClass().getDeclaredFields()) {
            String name = field.getName();
            if (!name.startsWith("x_")) {
                fields[size++] = name;
            }
        }

        return fields;
    }

    public void addTableHeader(String[] fields) {
        table = new PdfPTable(fields.length);

        for (String field : fields) {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(field));
            table.addCell(header);
        }
    }

    public void addTableHeader(T obj) {
        String[] fields = getDeclaredFields(obj);
        this.addTableHeader(fields);
    }

    public void addRows(String[] fields) {
        table = new PdfPTable(fields.length);

        for (String field : fields) {
            table.addCell(field);
        }
    }

    public void addRows(List<T> objects) {
        for (T object : objects) {
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Object value = field.get(object);
                    table.addCell(value.toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addMessage(String message) {
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        text = new Chunk(message, font);
    }
}
