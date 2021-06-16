import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class studentJSON {
    public static ResultSet RetrieveData() throws Exception {
        DriverManager.registerDriver(new org.postgresql.Driver());
        String mysqlUrl = "jdbc:postgresql://localhost/students";
        Connection con = DriverManager.getConnection(mysqlUrl, "postgres", "123");
        Statement stmt = con.createStatement();

        ResultSet rs = stmt.executeQuery("Select * from studentdetails");
        return rs;
    }

    public static void main(String args[]) throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        ResultSet rs = RetrieveData();

        while(rs.next()) {
            JSONObject record = new JSONObject();
            record.put("name", rs.getString("sname"));
            record.put("course", rs.getString("course"));
            record.put("marks", rs.getString("grade"));
            record.put("average", rs.getString("average"));
            array.add(record);
        }
        jsonObject.put("Students_data", array);
        try {
            FileWriter file = new FileWriter("output.json");
            file.write(jsonObject.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("JSON file created!");
    }
}
