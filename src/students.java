import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class students {
    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtCourse;
    private JTextField txtGrade;
    private JButton readButton;
    private JButton saveButton;
    private JLabel studentGrade;
    private JLabel courseName;
    private JLabel studentId;
    private JLabel studentName;
    private JPanel studentsForm;
    private JButton addButton;
    private JList list1;
    private JButton averageButton;
    private JTextField txtAverage;
    private JButton generateJSONButton;

    Connection con;
    PreparedStatement pst;

    int total;
    int NumberOfMarks;

    public void Connect()
    {
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://localhost/students", "postgres","123");
            System.out.println("Connection established");
        }
        catch (ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }


    public students() {
        Connect();

        total = 0;
        NumberOfMarks = 0;

        DefaultListModel Number = new DefaultListModel();

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name,id,course, average;
                ListModel grade;

                name = txtName.getText();
                id = txtId.getText();
                course = txtCourse.getText();
                grade = list1.getModel();
                average = txtAverage.getText();

                try {
                    pst = con.prepareStatement("insert into studentdetails(sname,reg,course,grade, average)values(?,?,?,?,?)");
                    pst.setString(1, name);
                    pst.setString(2, id);
                    pst.setString(3, course);
                    pst.setString(4, String.valueOf(grade));
                    pst.setString(5, average);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Record Added!");

                    txtName.setText("");
                    txtId.setText("");
                    txtCourse.setText("");
                    list1.setModel(Number);
                    txtAverage.setText("");
                    txtName.requestFocus();
                }

                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }

                try {
                    FileWriter Writer = new FileWriter("fineline.html", true);
                    Writer.write(""+name+" \t"+id+" \t"+course+" \t"+grade+"<br>");
                    Writer.write(System.getProperty("line.separator"));
                    Writer.close();

                }
                catch (Exception i) {
                    i.printStackTrace();
                }
                System.out.println("HTML file created");

            }
        });
        readButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    studentTable.main(new String[0]);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int num1 = Integer.parseInt(txtGrade.getText());

                Number.addElement(num1);
                list1.setModel(Number);
                total = total + num1;
                NumberOfMarks = NumberOfMarks + 1;
                txtGrade.setText(null);

                int count = list1.getModel().getSize();
                if (count == 9){
                    txtGrade.setEnabled(false);
                    addButton.setEnabled(false);

                }


            }
        });
        averageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double Average;
                String Answer;

                Average = total/NumberOfMarks;
                Answer = String.format("%.1f", Average);
                txtAverage.setText(Answer);
            }
        });

        generateJSONButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    studentJSON.main(new String[0]);
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                }

            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("students");
        frame.setContentPane(new students().studentsForm);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
