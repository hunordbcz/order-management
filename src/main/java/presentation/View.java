package presentation;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Generates PDFs
 *
 * @param <T>
 */
public class View<T> {
    PdfPTable table;
    Chunk text;

    public View() {
        text = null;
        table = null;
    }

    /**
     * Creates a new file, adds content to it and prints it
     * @param input The name of the output file
     */
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

    /**
     * Gets the fields names for an object
     *
     * @param obj The given object
     * @return String array of the field names
     */
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

    /**
     * Add table header to file
     *
     * @param fields String array of header names
     */
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

    /**
     * Add table header to file
     *
     * @param obj Object whose field names will be added as table headers
     */
    public void addTableHeader(T obj) {
        String[] fields = getDeclaredFields(obj);
        this.addTableHeader(fields);
    }

    /**
     * Add rows to the table
     *
     * @param fields String array of cell values
     */
    public void addRows(String[] fields) {
        for (String field : fields) {
            table.addCell(field);
        }
    }

    /**
     * Add rows to the table
     *
     * @param objects Object whose field values will be added as table headers
     */
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

    /**
     * @param message String that will be added to the output files first line
     */
    public void addMessage(String message) {
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        text = new Chunk(message, font);
    }
}
