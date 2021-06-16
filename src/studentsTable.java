import java.awt.BorderLayout;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class studentTable
{

    private static Connection con = null;
    private static String URL = "jdbc:postgresql://localhost/students";
    private static String driver = "org.postgresql.Driver";
    private static String user = "postgres";
    private static String pass = "123";

    public static void main(String[] args) throws SQLException
    {

        Statement stmt;
        String query;
        ResultSet rs;

        Object rowData[][] = {{"Row1-Col1", "Row1-Col2", "Row1-Col3", "Row1-Col4", "Row1-Col5"}};
        Object columnNames[] = {"Name", "Student ID", "Course", "Grade", "Average"};

        DefaultTableModel newModel = new DefaultTableModel(rowData, columnNames);
        JTable table = new JTable(newModel);

        try {
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(URL, user, pass);
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }

        query = "SELECT sname, reg, course, grade, average FROM studentdetails";
        stmt = con.createStatement();
        rs = stmt.executeQuery(query);

        // the gui
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setSize(500, 200);
        frame.setVisible(true);

        // get rid of the temp row
        newModel.removeRow(0);

        //temporary object array
        Object[] rows;

        while (rs.next()) {
            rows = new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)};
            newModel.addRow(rows);
        }
    }
}