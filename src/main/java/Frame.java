import Controller.AuthorController;
import Controller.BookController;
import Controller.LeasingController;
import Controller.ReadersController;
import Model.Reader;
import com.itextpdf.text.*;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.*;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.TextField;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

import Model.*;
import static com.itextpdf.text.Image.MIDDLE;
import static com.itextpdf.text.Image.RIGHT;
import static javax.swing.SwingConstants.CENTER;

public class Frame {
    BookController controlB;
    AuthorController controlA;
    ReadersController controlR;
    LeasingController controlL;

    private static final Logger log = Logger.getLogger("Frame.class");
    private JFrame Frame;

    private JButton add;
    private JButton edit;
    private JButton delete;
    private JButton print;

    private JToolBar toolBar;

    private JButton[] pages;

    private DefaultTableModel model;
    private JTable table;
    private JScrollPane scroll;

    private JComboBox author;
    private JComboBox authorToAdd;
    private JTextField bookName;
    private JButton find;
    private JButton showAll;
    private JPanel findPanel;
    private JTextField bookISBN;
    private JLabel note;

    private JButton takeBook;
    private JButton returnBook;
    private JButton seeReaderBooks;
    private JComboBox booksToTake;
    private JComboBox booksToReturn;
    private JPanel readerPanel;

    private BufferedImage img;
    private JLabel welcome;

    private int current;
    private JPanel pagesPanel;

    public void show() {
        log.info("Program started");;
        controlB = new BookController();
        controlA = new AuthorController();
        controlR = new ReadersController();
        controlL = new LeasingController();

        Frame = new JFrame("Library Personal Computer");
        Frame.setSize(1400, 700);
        Frame.setLocation(100, 100);
        Frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        add = new JButton("Add", new ImageIcon("src/main/resources/img/add.png"));
        edit = new JButton("Edit", new ImageIcon("src/main/resources/img/edit.png"));
        delete = new JButton("Delete", new ImageIcon("src/main/resources/img/delete.png"));
        print = new JButton("Print", new ImageIcon("src/main/resources/img/print.png"));

        toolBar = new JToolBar("Toolbar");
        toolBar.setFloatable(false);
        toolBar.add(add);
        toolBar.add(edit);
        toolBar.add(delete);
        toolBar.add(print);

        Frame.setLayout(new BorderLayout());
        Frame.add(toolBar, BorderLayout.NORTH);

        pages = new JButton[]{new JButton("Books"), new JButton("Authors"), new JButton("Readers")};
        pagesPanel = new JPanel(new GridLayout(3, 1));

        for (int i = 0; i < 3; i++) pagesPanel.add(pages[i]);

        JPanel west = new JPanel(new FlowLayout());
        west.add(pagesPanel);
        Frame.add(west, BorderLayout.WEST);

        loadAuthorsToPanel();
        bookName = new JTextField("Book name", 10);
        bookISBN = new JTextField("Book ISBN", 10);
        find = new JButton("Find");
        showAll = new JButton("Show all");
        note = new JLabel("Note: You can search only by name, author or ISBN - not at once.");
        note.setHorizontalAlignment(CENTER);

        JPanel fPanel = new JPanel();
        fPanel.add(bookName);
        fPanel.add(author);
        fPanel.add(bookISBN);
        fPanel.add(find);
        fPanel.add(showAll);

        findPanel = new JPanel(new GridLayout(2,1));
        findPanel.add(fPanel);
        findPanel.add(note);

        takeBook = new JButton("Take a book");
        returnBook = new JButton("Return a book");
        seeReaderBooks = new JButton("See books taken by reader");
        readerPanel = new JPanel();
        readerPanel.add(takeBook);
        readerPanel.add(returnBook);
        readerPanel.add(seeReaderBooks);
        try {
            img = ImageIO.read(new File("src/main/resources/img/welcome.jpg"));
            welcome = new JLabel(new ImageIcon(img));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        Frame.add(welcome, BorderLayout.CENTER);
        Frame.setVisible(true);
        current = -1;

        for (int i = 0; i < 3; i++) {
            final int next = i;
            pages[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    Frame.remove(welcome);
                    if (current != -1) {
                        if (current == 2) Frame.remove(readerPanel);
                        if (current == 0) Frame.remove(findPanel);
                    }
                    switch (next){
                        case 0:
                            if (current != 0)
                                loadBooks();
                            break;
                        case 1:
                            if (current != 1)
                                loadAuthors();
                            break;
                        case 2:
                            if (current != 2)
                                loadReaders();
                            break;
                    }
                    current = next;
                }
            });
        }

        Frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Object[] options = {"Yes", "No"};
                int n = JOptionPane
                        .showOptionDialog(e.getWindow(), "Do you really want to exit?",
                                "Confirmation", JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, options,
                                options[0]);
                if (n == 0) {
                    e.getWindow().setVisible(false);
                    log.info("Program stopped");
                    System.exit(0);
                }
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] options = {"Yes", "No"};
                try {
                    checkTableSelected();
                    checkPage();
                    checkTable(table);
                    int n = JOptionPane
                            .showOptionDialog(null, "Are you sure you want to delete item?",
                                    "Confirmation", JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE, null, options,
                                    options[0]);
                    if (n == 0) {
                        int id = Integer.parseInt(model.getValueAt(table.getSelectedRow(),0).toString());
                        boolean deleted = false;
                        switch (current) {
                            case 0:
                                checkBook(id);
                                deleted = controlB.delete(id);
                                controlL.deleteB(id);
                                break;
                            case 2:
                                checkReader(id);
                                deleted = controlR.delete(id);
                                controlL.deleteR(id);
                                break;
                        }
                        if (deleted) {
                            model.removeRow(table.getSelectedRow());
                            JOptionPane.showMessageDialog(null, "Data is successfully deleted!");
                        }
                        else JOptionPane.showMessageDialog(null, "Error while deleting!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NSException | DAException | NCException | TakenException | HasBookException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

       edit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    checkTableSelected();
                    checkTable(table);
                    log.info("Start editing");
                    log.info("Choose the entity and row");
                    JPanel panel = new JPanel();
                    int n = model.getColumnCount();
                    int currentRow = table.getSelectedRow();
                    int c = 1; if (current == 0) c = 3;
                    JTextField[] fields = new JTextField[n-c];
                    int k = 1;
                    for (int i = 0; i < n-c; i++) {
                        panel.add(new Label(model.getColumnName(i+k)));
                        panel.add(fields[i] = new JTextField((String) model.getValueAt(currentRow, i+k), 20));
                        if (i == 0 && current == 0) k = 2;
                    }

                    int input = JOptionPane.showConfirmDialog(Frame, panel, "Edit:"
                            , JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (input == JOptionPane.OK_OPTION) {
                        int confirm = JOptionPane.OK_OPTION;
                        for (int i = 0; i < n-c; i++) {
                            if (fields[i].getText().equals("") || fields[i] == null) {
                                confirm = JOptionPane.showConfirmDialog(null,
                                        "Not all fields are filled, they'll be empty. Do you want to continue?",
                                        "Confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                                if (confirm != JOptionPane.OK_OPTION) break;
                            }
                        }
                        if (confirm == JOptionPane.OK_OPTION) {
                            boolean edited = false;
                            if (current == 0) {
                                checkNumber(fields[2]);
                                checkNumber(fields[3]);
                            }
                            if (current == 2)
                                checkNumber(fields[2]);
                            int id = Integer.parseInt(model.getValueAt(currentRow,0).toString());
                            String[] fieldsString = new String[fields.length];
                            for (int i = 0; i < fieldsString.length; i++)
                                fieldsString[i] = replaceName(fields[i].getText());
                            switch(current){
                                case 0:
                                    if(controlB.edit(id, fieldsString)) edited = true;
                                    break;
                                case 1:
                                    if(controlA.edit(id, fieldsString)) edited = true;
                                    loadAuthorsToPanel();
                                    break;
                                case 2:
                                    if(controlR.edit(id, fieldsString)) edited = true;
                                    break;
                            }
                            if (edited) {
                                switch(current){
                                    case 0 -> loadBooks();
                                    case 1 -> loadAuthors();
                                    case 2 -> loadReaders();
                                }
                                JOptionPane.showMessageDialog(null, "Data is successfully updated!");

                            }
                            else JOptionPane.showMessageDialog(null, "Error while updating!", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    log.info("Editing done");
                } catch (NSException | NCException | NNException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
       });

       add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    checkTableSelected();

                    if (current == 0) {
                        JTextField name = new JTextField(15);
                        JTextField year = new JTextField(5);
                        JTextField count = new JTextField(3);

                        JPanel myPanel = new JPanel();
                        myPanel.add(new JLabel("Name:"));
                        myPanel.add(name);
                        myPanel.add(new JLabel("Author:"));
                        myPanel.add(authorToAdd);
                        myPanel.add(new JLabel("Year:"));
                        myPanel.add(year);
                        myPanel.add(new JLabel("Count:"));
                        myPanel.add(count);

                        int result = JOptionPane.showConfirmDialog(null, myPanel,
                                "Add a new book", JOptionPane.OK_CANCEL_OPTION);

                        if (result == JOptionPane.OK_OPTION) {
                            try {
                                checkChoice(authorToAdd);
                                checkText(name);
                                checkNumber(year);
                                checkNumber(count);

                                Book b = new Book();
                                String nameCorrect = replaceName(name.getText());
                                b.setBook_name(nameCorrect);
                                String [] author_name = authorToAdd.getSelectedItem().toString().split(" ",2);

                                Author select = controlA.getAuthor(author_name);
                                b.setBook_authorID(select.getAuthorID());
                                b.setPublish_year(year.getText());
                                b.setBook_count(Integer.parseInt(count.getText()));

                                controlB.addToDB(b);
                                 loadBooks();
                                JOptionPane.showMessageDialog(null, "Data is successfully added!");
                            } catch (NBException | NAException | NNException ex) {
                                JOptionPane.showMessageDialog(null, ex.getMessage());
                            } catch (NullPointerException ex) {
                                JOptionPane.showMessageDialog(null, ex.toString());
                            }

                        }
                    }
                    else {
                        JPanel panel = new JPanel();
                        int n = model.getColumnCount();
                        JTextField[] fields = new JTextField[n];
                        for (int i = 0; i < n-1; i++) {
                            panel.add(new Label(model.getColumnName(i+1)));
                            panel.add(fields[i] = new JTextField( 10));
                        }

                        int result = JOptionPane.showConfirmDialog(Frame, panel, "Add a new item:"
                                , JOptionPane.OK_CANCEL_OPTION);

                        if (result == JOptionPane.OK_OPTION) {
                            try {
                                for (int i = 0; i < n-1; i++) checkText(fields[i]);
                                String nameCorrect, lastNameCorrect;
                                switch(current){
                                    case 1:
                                        Author a = new Author();
                                        nameCorrect = replaceName(fields[0].getText());
                                        a.setName(nameCorrect);
                                        lastNameCorrect = replaceName(fields[1].getText());
                                        a.setLastName(lastNameCorrect);
                                        controlA.addToDB(a);
                                        loadAuthorsToPanel();
                                        loadAuthors();
                                        JOptionPane.showMessageDialog(null, "Data is successfully added!");
                                        break;
                                    case 2:
                                        Reader r = new Reader();
                                        nameCorrect = replaceName(fields[0].getText());
                                        r.setName(nameCorrect);
                                        lastNameCorrect = replaceName(fields[1].getText());
                                        r.setLastName(lastNameCorrect);
                                        checkNumber(fields[2]);
                                        r.setReader_doc(Integer.parseInt(fields[2].getText()));
                                        if(controlR.addToDB(r)) {
                                            loadReaders();
                                            JOptionPane.showMessageDialog(null, "Data is successfully added!");
                                        } else
                                            JOptionPane.showMessageDialog(null, "Error while adding!", "Error",
                                                JOptionPane.ERROR_MESSAGE);
                                        break;
                                    default:
                                        throw new IllegalStateException("Unexpected value: " + current);
                                }
                            } catch (NullPointerException ex) {
                                JOptionPane.showMessageDialog(null, ex.toString());
                            } catch (NNException ex) {
                                JOptionPane.showMessageDialog(null, ex.getMessage());
                            }
                        }
                    }
                } catch (NCException | NBException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }

       });


       find.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    checkText(bookName);
                    String str = bookName.getText();
                    String[][] allFound = controlB.show();
                    int n = 0;
                    for (String[] strings : allFound) if (strings[3].toLowerCase().contains(str.toLowerCase())) n++;
                    String[][] result = new String[n][7];
                    for (int i = 0, k = 0; i < allFound.length; i++)
                        if (allFound[i][3].toLowerCase().contains(str.toLowerCase())) {
                            for (int j = 0; j < 7; j++)
                                result[k][j] = allFound[i][j];
                            k++;
                        }
                    if (result.length > 0){
                        Frame.remove(scroll);
                        model = new DefaultTableModel(result, Data.books_columns);
                        table = new JTable(model);
                        scroll = new JScrollPane(table);
                        Frame.add(scroll, BorderLayout.CENTER);
                        Frame.revalidate();
                    }
                    else JOptionPane.showMessageDialog(null, "No matches found!");
                } catch (NullPointerException | NBException ex) {
                    try {
                        checkChoice(author);
                        Author a = controlA.getAuthor(author.getSelectedItem().toString().split(" ", 2));
                        int id = a.getAuthorID();
                        String[][] data = controlB.findByAuthor(id);
                        if (data.length > 0) {
                            Frame.remove(scroll);
                            model = new DefaultTableModel(controlB.findByAuthor(id), Data.books_columns);
                            table = new JTable(model);
                            scroll = new JScrollPane(table);
                            Frame.add(scroll, BorderLayout.CENTER);
                            Frame.revalidate();
                        }
                        else JOptionPane.showMessageDialog(null, "No matches found!");
                    } catch (NAException naEx) {
                        try {
                            checkText(bookISBN);
                            Book b = controlB.findByISBN(bookISBN.getText());
                            if (b != null) {
                                Frame.remove(scroll);
                                model = new DefaultTableModel(controlB.bookWithID(b.getBookID()), Data.books_columns);
                                table = new JTable(model);
                                scroll = new JScrollPane(table);
                                Frame.add(scroll, BorderLayout.CENTER);
                                Frame.revalidate();
                            }
                            else JOptionPane.showMessageDialog(null, "No matches found!");
                        } catch (NBException Nex) {
                            JOptionPane.showMessageDialog(null, Nex.getMessage());
                        }
                        catch (NullPointerException nullEx){
                            JOptionPane.showMessageDialog(null, nullEx.toString());
                        }
                    }
                }
            }
       });

       print.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Document document = new Document(PageSize.A4, 20, 20, 20, 20);
                try {
                    PdfWriter.getInstance(document, new FileOutputStream("Information.pdf"));
                    BaseFont bfComic;
                    bfComic = BaseFont.createFont("fonts\\timesnewromanpsmt.ttf" ,BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    Font font = new Font(bfComic, 18, Font.BOLD);
                    Font font1 = new Font(bfComic, 14);
                    Font font1b = new Font(bfComic, 14, Font.BOLD);
                    Font font2 = new Font(bfComic, 10);
                    Paragraph header = new Paragraph("Information about the work of the library for the month", font);
                    header.setAlignment(MIDDLE);
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String today = dateFormat.format(date);
                    LocalDate date1 = LocalDate.now().minusMonths(1);
                    String aMonthAgo = date1.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    Date timeNow = new Date();
                    Paragraph month = new Paragraph("from "+aMonthAgo+" to "+today, font1);
                    month.setAlignment(RIGHT);
                    Paragraph readers = new Paragraph("\nNumber of readers for today: "+controlR.count(), font1);
                    Paragraph newReaders = new Paragraph("Readers signed up for the month: "+controlL.readersForMonth(), font1);
                    Paragraph booksTaken = new Paragraph("Books issued in a month: "+controlL.booksForMonth(),font1);
                    Paragraph now = new Paragraph("\n\nGenerated at "+timeNow, font2);
                    now.setAlignment(RIGHT);

                    Paragraph table = new Paragraph("\n\nBooks taken for today:\n\n", font1);
                    table.setAlignment(MIDDLE);
                    PdfPTable t = new PdfPTable(5);
                    t.addCell(new PdfPCell(new Phrase("Reader passport", font1b)));
                    t.addCell(new PdfPCell(new Phrase("Reader name", font1b)));
                    t.addCell(new PdfPCell(new Phrase("Book name", font1b)));
                    t.addCell(new PdfPCell(new Phrase("Author", font1b)));
                    t.addCell(new PdfPCell(new Phrase("Date of issue", font1b)));
                    String[][] info = controlL.Info();
                    for (String[] strings : info) {
                        for (int j = 0; j < 5; j++) {
                            t.addCell(new Phrase(strings[j], font1));
                        }
                    }
                    document.open();
                    try {
                        document.add(header);
                        document.add(month);
                        document.add(readers);
                        document.add(newReaders);
                        document.add(booksTaken);
                        document.add(table);
                        document.add(t);
                        document.add(now);
                    } catch (DocumentException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                    document.close();
                    JOptionPane.showMessageDialog(null, "Data successfully uploaded to Information.pdf!");
                }
                catch (DocumentException | IOException | ParseException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
       });

       takeBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    checkTable(table);
                    JPanel panel = new JPanel();
                    int reader_id = Integer.parseInt(model.getValueAt(table.getSelectedRow(), 0).toString());
                    Reader r = controlR.getById(reader_id);
                    String[][] books = controlB.show();
                    String[] bookNames = new String[books.length];
                    for (int i = 0; i < books.length; i++){
                        bookNames[i] = books[i][3];
                    }
                    booksToTake = new JComboBox(bookNames);
                    panel.add(booksToTake);

                    int result = JOptionPane.showConfirmDialog(Frame, panel, "Choose a book:"
                            , JOptionPane.OK_CANCEL_OPTION);

                    if (result == JOptionPane.OK_OPTION) {
                        String bookSelected = booksToTake.getSelectedItem().toString();
                        Book b = controlB.findByName(bookSelected);
                        checkBookInStock(b);
                        int book_id = b.getBookID();
                        r.GetBook(book_id);
                        controlB.takeBook(book_id);
                        JOptionPane.showMessageDialog(null, "Book is successfully issued!");
                    }
                } catch (NSException | ReadersController.IsTakenException | NotInStockException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
       });

       returnBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    checkTable(table);
                    int reader_id = Integer.parseInt(model.getValueAt(table.getSelectedRow(), 0).toString());
                    checkBooks(reader_id);

                    Reader r = controlR.getById(reader_id);
                    String[][] books = r.PrintMyBooks();
                    String[] bookNames = new String[books.length];
                    for (int i = 0; i < books.length; i++) bookNames[i] = "";
                    for (int k = 0; k < books.length; k++)
                        bookNames[k] = books[k][1];

                    JPanel panel = new JPanel();
                    booksToReturn  = new JComboBox(bookNames);
                    panel.add(booksToReturn);
                    int result = JOptionPane.showConfirmDialog(Frame, panel, "Choose a book:"
                            , JOptionPane.OK_CANCEL_OPTION);

                    if (result == JOptionPane.OK_OPTION) {
                        String bookSelected = booksToReturn.getSelectedItem().toString();
                        Book b = controlB.findByName(bookSelected);
                        int book_id = b.getBookID();
                        r.ReturnBook(book_id);
                        controlB.returnBook(book_id);
                        JOptionPane.showMessageDialog(null, "Book is successfully returned!");
                    }
                } catch (NSException | NoBookException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
       });

       seeReaderBooks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    checkTable(table);
                    int reader_id = Integer.parseInt(model.getValueAt(table.getSelectedRow(), 0).toString());
                    checkBooks(reader_id);
                    Reader r = controlR.getById(reader_id);
                    String[][] books = r.PrintMyBooks();
                    String[] hat = new String[]{"ISBN","Name","Author","Date of issue"};
                    DefaultTableModel modelBook = new DefaultTableModel(books, hat);
                    JTable tableBook = new JTable(modelBook);
                    JScrollPane scrollBook = new JScrollPane(tableBook);
                    scrollBook.setPreferredSize(new Dimension(500,100));
                    JOptionPane.showMessageDialog(null, scrollBook, "Books taken by reader", JOptionPane.PLAIN_MESSAGE);

                } catch (NSException | NoBookException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
       });
       showAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch(current){
                    case 0 -> loadBooks();
                    case 1 -> loadAuthors();
                    case 2 -> loadReaders();
                }
            }
       });
    }

    void loadAuthorsToPanel() {
        String[][] author_data = controlA.show();
        String [] authors = new String[author_data.length+1];
        authors[0] = "Author";
        for (int i = 1; i < author_data.length+1; i++){
            authors[i] = author_data[i-1][1] + " " + author_data[i-1][2];
        }
        author = new JComboBox(authors);
        authorToAdd = new JComboBox(authors);
    }

    public void loadBooks() {
        if (Frame.isAncestorOf(scroll)) Frame.remove(scroll);
        model = new DefaultTableModel(controlB.show(), Data.books_columns);
        table = new JTable(model);
        scroll = new JScrollPane(table);
        Frame.add(scroll, BorderLayout.CENTER);
        Frame.add(findPanel, BorderLayout.SOUTH);
        Frame.revalidate();
        Frame.repaint();
    }
    public void loadAuthors() {
        if (Frame.isAncestorOf(scroll)) Frame.remove(scroll);
        model = new DefaultTableModel(controlA.show(), Data.authors_columns);
        table = new JTable(model);
        scroll = new JScrollPane(table);
        Frame.add(scroll, BorderLayout.CENTER);
        Frame.revalidate();
        Frame.repaint();
    }
    public void loadReaders(){
        if (Frame.isAncestorOf(scroll)) Frame.remove(scroll);
        model = new DefaultTableModel(controlR.show(), Data.readers_columns);
        table = new JTable(model);
        scroll = new JScrollPane(table);
        Frame.add(scroll, BorderLayout.CENTER);
        Frame.add(readerPanel, BorderLayout.SOUTH);
        Frame.revalidate();
        Frame.repaint();
    }

    private class NBException extends Exception { //name book
        public NBException(){
            super("You didn't enter the name of the book!");
        }
    }

    private class NAException extends Exception { //name author
        public NAException(){
            super("You didn't choose the name of the author!");
        }
    }

    private class NSException extends Exception { //not selected
        public NSException(){
            super("You didn't choose the row!");
        }
    }

    private class DAException extends Exception { //delete author
        public DAException(){
            super("You can't remove authors!");
        }
    }

    private class NCException extends Exception { //not selected table
        public NCException(){
            super("You didn't choose the table!");
        }
    }

    private class NNException extends Exception { //not number value
        public NNException(){
            super("Incorrect value!");
        }
    }

    private class NoBookException extends Exception { //not selected table
        public NoBookException(){
            super("This reader doesn't have books!");
        }
    }

    private class TakenException extends Exception { //book cannot be deleted, bcs is taken
        public TakenException() {super("You can't remove this book! It's taken by readers.");}
    }

    private class NotInStockException extends Exception {
        public NotInStockException() {super("Sorry, this book is not enabled now.");}
    }

    private class HasBookException extends Exception {
        public HasBookException() {super("This reader cannot be deleted! It has books now.");}
    }

    public void checkTableSelected() throws NCException {
        if (current == -1) throw new NCException();
    }

    public void checkText(JTextField name) throws NBException, NullPointerException{
        String str = name.getText();
        if (str.contains("Book name") || str.contains("Book ISBN")) throw new NBException();
        if (str.length()==0) throw new NullPointerException();
    }

    public void checkNumber(JTextField num) throws NullPointerException, NNException {
        String str = num.getText();
        if (!new Scanner(str).hasNextInt()) throw new NNException();
        if (str.length()==0) throw new NullPointerException();
    }

    public void checkChoice(JComboBox name) throws NAException{
        String str = name.getSelectedItem().toString();
        if (str.contains("Author")) throw new NAException();
    }

    public void checkTable(JTable table) throws NSException{
        int row = table.getSelectedRow();
        if (row == -1) throw new NSException();
    }

    public void checkPage() throws DAException{
        if (current == 1) throw new DAException();
    }

    public void checkBooks(int reader_id) throws NoBookException {
        Reader r = controlR.getById(reader_id);
        if (r.PrintMyBooks().length == 0) throw new NoBookException();
    }

    public void checkBook(int book_id) throws TakenException {
        Leasing l = controlL.findByBook(book_id);
        if (l != null) throw new TakenException();
    }

    public void checkReader(int reader_id) throws HasBookException {
        Leasing l = controlL.findByReader(reader_id);
        if (l != null) throw new HasBookException();
    }

    public void checkBookInStock(Book b) throws NotInStockException {
        if (!b.getInStock()) throw new NotInStockException();
    }

    public String replaceName(String s){
        if (s.contains("'")) s = s.replace("'","`");
        return s;
    }
}


