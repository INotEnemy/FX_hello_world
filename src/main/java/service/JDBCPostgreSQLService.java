package service;

import dto.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JDBCPostgreSQLService {
    final private String DB_URL = "jdbc:postgresql://127.0.0.1:5432/postgres";
    final private String USER = "postgres";
    final private String PASS = "password";

    public List<Student> getStudents() {

        Connection connection = null;
        List<Student> students = new ArrayList<>();

        try {
            connection = DriverManager
                    .getConnection(DB_URL, USER, PASS);
            connection.setSchema("db");
            connection.setAutoCommit(false);
            ResultSet resultSet = connection.createStatement().executeQuery("select * from student");
            while (resultSet.next()) {
                students.add(studentColsSetter(resultSet));
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
        }
        return students;
    }
    public void createStudent(Student student) {
        Connection connection = null;
        try {
            connection = DriverManager
                    .getConnection(DB_URL, USER, PASS);
            connection.setSchema("db");
            connection.setCatalog("db");
            connection.setAutoCommit(false);

            PreparedStatement prepSt = connection.prepareStatement("insert into db.student(stud_num,fio,grpnum,grants,profnum,zachnum) values ("+
                    "?,?,?,?,?,?);");
            prepSt.setInt(1, getRandom());
            prepSt.setString(2,student.getFio());
            prepSt.setShort(3, student.getGrpNum());
            prepSt.setBigDecimal(4,student.getGrants());
            prepSt.setInt(5,student.getProfNum());
            prepSt.setInt(6, Integer.valueOf(student.getZachNum()));
            prepSt.executeUpdate();

            connection.commit();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
        }
    }



    private Student studentColsSetter(ResultSet resultSet) {
        try {
            Student student = new Student();
            student.setFio(resultSet.getString("fio"));
            student.setGrpNum(resultSet.getShort("grpnum"));
            student.setGrants(resultSet.getBigDecimal("grants"));
            student.setProfNum(resultSet.getInt("profnum"));
            student.setZachNum(resultSet.getString("zachnum"));
            return student;
        }
        catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
        }
        return null;
    }

    private int getRandom() {
        Random r = new Random();
        return r.nextInt(9999999);
    }
}
