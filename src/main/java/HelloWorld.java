import dto.Student;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;

import javafx.stage.Stage;
import service.JDBCPostgreSQLService;

import java.math.BigDecimal;


public class HelloWorld extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ObservableList<Student> students = getStudents();
        TableView<Student> tableView = buildStudentTableView(students);
        ObservableList<String> filterColNames = FXCollections.observableArrayList("Номер зачетной книжки", "ФИО студента");
        ComboBox<String> comboBox = new ComboBox<>(filterColNames);
        Label lbl = new Label();
        comboBox.setOnAction(event -> lbl.setText(comboBox.getValue()));

        TextField fieldForSearch = new TextField("type value for search");

        TextField fio     = new TextField("fio");
        TextField grpnum  = new TextField("99");
        TextField grants  = new TextField("15000");
        TextField profnum = new TextField("992");
        TextField zachnum = new TextField("1235123512");
        Button createBtn = new Button("Create");
        createBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Student stud = new Student();
                stud.setFio(fio.getText());
                stud.setGrpNum(Short.valueOf(grpnum.getText()));
                stud.setGrants(BigDecimal.valueOf(Integer.valueOf(grants.getText())));
                stud.setProfNum(Integer.valueOf(profnum.getText()));
                stud.setZachNum(zachnum.getText());
                createStud(stud);
                tableView.getItems().add(stud);
            }
        });

        Button searchBtn = new Button();
        searchBtn.setText("Search");
        searchBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String valueForFilter = fieldForSearch.getText();
                String fieldForFilter = comboBox.getValue();
                Student s = tableView.getItems().stream().filter(item -> {
                    boolean flg = false;
                    switch(fieldForFilter) {
                        case "Номер зачетной книжки":
                            flg = item.getZachNum().contains(valueForFilter);
                            break;
                        case "ФИО студента":
                            flg = item.getFio().contains(valueForFilter);
                            break;
                    }
                    return flg;
                }).findAny().get();
                int index = tableView.getItems().indexOf(s);
                tableView.getSelectionModel().select(index);
            }
        });
        FlowPane root = new FlowPane(Orientation.HORIZONTAL);
        root.getChildren().add(comboBox);
        root.getChildren().add(fieldForSearch);
        root.getChildren().add(searchBtn);
        root.getChildren().add(tableView);
        root.getChildren().addAll(fio, grpnum, grants, profnum, zachnum);
        root.getChildren().add(createBtn);
        primaryStage.setWidth(600);
        primaryStage.setHeight(800);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    public void createStud (Student s) {
        JDBCPostgreSQLService sqlService = new JDBCPostgreSQLService();
        sqlService.createStudent(s);
    }

    public ObservableList<Student> getStudents() {
        JDBCPostgreSQLService sqlService = new JDBCPostgreSQLService();
        ObservableList<Student> students = FXCollections.observableArrayList();
        students.addAll(sqlService.getStudents());
        return students;
    }

    public TableView<Student> buildStudentTableView (ObservableList<Student> students) {
        TableView<Student> tableView = new TableView<>(students);

        tableView.setPrefWidth(498);
        tableView.setPrefHeight(580);

        TableColumn<Student, String> fio = new TableColumn<>("ФИО студента");
        fio.setCellValueFactory(new PropertyValueFactory<Student, String>("fio"));
        tableView.getColumns().add(fio);

        TableColumn<Student, Short> grpNme = new TableColumn<>("Номер группы");
        grpNme.setCellValueFactory(new PropertyValueFactory<Student, Short>("grpNum"));
        tableView.getColumns().add(grpNme);

        TableColumn<Student, BigDecimal> grants = new TableColumn<>("Стипенсия");
        grants.setCellValueFactory(new PropertyValueFactory<Student, BigDecimal>("grants"));
        tableView.getColumns().add(grants);

        TableColumn<Student, Integer> profNum = new TableColumn<>("Номер направления обучения");
        profNum.setCellValueFactory(new PropertyValueFactory<Student, Integer>("profNum"));
        tableView.getColumns().add(profNum);

        TableColumn<Student, String> zachNum = new TableColumn<>("Номер зачетной книжки");
        zachNum.setCellValueFactory(new PropertyValueFactory<Student, String>("zachNum"));
        tableView.getColumns().add(zachNum);

        return tableView;
    }
}