package com.example.javafx_exercise;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.StringConverter;
import javafx.scene.layout.GridPane;
import java.util.Optional;

public class MainController {
    @FXML
    private ListView<String> groupList;
    private String selectedItem = null;
    private Teacher selectedTeacher = null;
    private ClassContainer classContainer = new ClassContainer();
    @FXML
    private TableView<Teacher> teacherTable;
    @FXML
    private TableColumn<Teacher, String> nameColumn;
    @FXML
    private TableColumn<Teacher, String> surnameColumn;
    @FXML
    private TableColumn<Teacher, Integer> salaryColumn;
    @FXML
    private TableColumn<Teacher, String> statusColumn;
    @FXML
    private TableColumn<Teacher, Integer> yearColumn;
    @FXML
    private TextField filterField;


    @FXML
    protected void initialize() {
        groupList.setItems(classContainer.getObservableClassNames());

        // Dodanie przykładowych grup
        classContainer.addClass("Polski", 5);
        classContainer.addClass("Angielski", 10);
        classContainer.addClass("Matematyka", 2);
        classContainer.addClass("Informatyka", 3);

        //Tworzenie nauczycieli
        Teacher t1 = new Teacher("Agnieszka", "Grabowska", TeacherCondition.chory, 1999, 5100);
        Teacher t2 = new Teacher("Angelika", "Ziemna", TeacherCondition.nieobecny, 1985, 6300);
        Teacher t3 = new Teacher("Anna", "Jasielinska", TeacherCondition.delegacja, 2003, 3900);
        Teacher t4 = new Teacher("Martyna", "Szczurowska", TeacherCondition.obecny, 2004, 4200);
        Teacher t5 = new Teacher("Aneta", "Kaczmarczyk", TeacherCondition.obecny, 2002, 4500);

        //Pobranie wszystkich grup
        ClassTeacher polski = classContainer.classes.get("Polski");
        ClassTeacher angielski = classContainer.classes.get("Angielski");
        ClassTeacher matematyka = classContainer.classes.get("Matematyka");
        ClassTeacher informatyka = classContainer.classes.get("Informatyka");

        //Dodawanie nauczycieli do grup
        angielski.addTeacher(t1);
        angielski.addTeacher(t4);
        matematyka.addTeacher(t2);
        informatyka.addTeacher(t3);
        informatyka.addTeacher(t5);
        informatyka.addTeacher(t4);

        changeGroupsData();
        groupList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && !groupList.getSelectionModel().isEmpty()) {
                selectedItem = groupList.getSelectionModel().getSelectedItem();
                showTeachersInGroup();
                selectedTeacher = null;
            }
        });
        teacherTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && !groupList.getSelectionModel().isEmpty()) {
                selectedTeacher = teacherTable.getSelectionModel().getSelectedItem();
            }
        });
    }
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    @FXML
    public void addGroup() {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Dodaj grupę");
        dialog.setHeaderText("Podaj dane grupy:");

        ButtonType okButton = new ButtonType("OK");
        ButtonType cancelButton = ButtonType.CANCEL;
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        GridPane grid = new GridPane();
        TextField groupName = new TextField();
        groupName.setPromptText("Nazwa grupy");
        TextField max = new TextField();
        max.setPromptText("Pojemność grupy");

        grid.add(groupName, 0, 0);
        grid.add(max, 0, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButton) {
                    if (groupName.getText().isEmpty() || max.getText().isEmpty() || !isInteger(max.getText()) ) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Informacja");
                        alert.setHeaderText("Nie wszystkie dane zostały podane lub podano nieprawidłowe dane");
                        alert.showAndWait();
                        return null;
                    } else {
                        String[] groupData = new String[2];
                        groupData[0] = groupName.getText();
                        groupData[1] = max.getText();
                        return groupData;
                    }
                }
            return null;
        });

        // Wyświetlenie dialogu i czekanie na akcję użytkownika
        Optional<String[]> result = dialog.showAndWait();
        result.ifPresent(data -> {
            classContainer.addClass(data[0], Integer.parseInt(data[1]));
        });

    }
    @FXML
    public void removeGroup() {
        if (selectedItem != null){
            classContainer.removeClass(selectedItem);
            selectedItem = null;
            showTeachersInGroup();
        }
    }

    @FXML
    public void showEmptyGroups(){
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Puste grupy");

        ButtonType closeButton = new ButtonType("Zamknij");
        dialog.getDialogPane().getButtonTypes().addAll(closeButton);

        ListView<String> listView = new ListView<>();
        listView.getItems().addAll(classContainer.findEmpty());
        dialog.getDialogPane().setContent(listView);

        dialog.setResultConverter(dialogButton -> {
            return null;
                });

        dialog.showAndWait();

    }

    @FXML
    public void addTeacher() {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Dodaj nauczyciela");
        dialog.setHeaderText("Podaj dane nauczyciela:");

        ButtonType okButton = new ButtonType("OK");
        ButtonType cancelButton = ButtonType.CANCEL;
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        GridPane grid = new GridPane();
        TextField name = new TextField();
        name.setPromptText("Imię");
        TextField surname = new TextField();
        surname.setPromptText("Nazwisko");
        TextField year = new TextField();
        year.setPromptText("Rok urodzenia");
        TextField salary = new TextField();
        salary.setPromptText("Wynagrodzenie");
        ComboBox condition = new ComboBox();
        condition.getItems().addAll(TeacherCondition.values());
        condition.setPromptText("Wybierz stan");


        grid.add(name, 0, 0);
        grid.add(surname, 0, 1);
        grid.add(year, 0, 2);
        grid.add(salary, 0, 3);
        grid.add(condition, 0, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                if (name.getText().isEmpty() || surname.getText().isEmpty() || !isInteger(year.getText()) || !isInteger(salary.getText()) || condition.getValue() == null ) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Informacja");
                    alert.setHeaderText("Nie wszystkie dane zostały podane lub podano nieprawidłowe dane");
                    alert.showAndWait();
                    return null;
                } else {
                    String[] teacherData = new String[5];
                    teacherData[0] = name.getText();
                    teacherData[1] = surname.getText();
                    teacherData[2] = year.getText();
                    teacherData[3] = salary.getText();
                    teacherData[4] = condition.getValue().toString();
                    return teacherData;
                }
            }
            return null;
        });

        Optional<String[]> result = dialog.showAndWait();
        result.ifPresent(data -> {
            if (selectedItem != null) {
                Teacher t = new Teacher(data[0], data[1], TeacherCondition.valueOf(data[4]), Integer.parseInt(data[2]), Integer.parseInt(data[3]));
                classContainer.classes.get(selectedItem).addTeacher(t);
                showTeachersInGroup();
                teacherTable.refresh();
                changeGroupsData();
            }
        });
    }

    @FXML
    public void showTeachersInGroup(){
        if (selectedItem != null){
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
            statusColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().condition.name()));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("yearOfBirth"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        teacherTable.setItems(classContainer.classes.get(selectedItem).teacherList);
        }else{
            teacherTable.setItems(FXCollections.observableArrayList());
        }
    }

    @FXML
    public void removeTeacher() {
        if (selectedTeacher != null){
            classContainer.classes.get(selectedItem).removeTeacher(selectedTeacher);
            selectedTeacher = null;
            changeGroupsData();
            teacherTable.refresh();
        }
    }

    public void changeGroupsData(){
        groupList.setCellFactory(param -> new TextFieldListCell<>(new StringConverter<String>() {
            @Override
            public String toString(String className) {
                return classContainer.summary(className); // Zwracamy nazwę grupy i zapelnienie
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        }));
    }

    @FXML
    public void addSalary(){
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Dodaj wynagrodzenie");

        ButtonType okButton = new ButtonType("OK");
        ButtonType cancelButton = ButtonType.CANCEL;
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        GridPane grid = new GridPane();
        TextField addSalary = new TextField();
        addSalary.setPromptText("Wysokosc podwyżki");

        grid.add(addSalary, 0, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                if (!isInteger(addSalary.getText()) ) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Informacja");
                    alert.setHeaderText("Nie wszystkie dane zostały podane lub podano nieprawidłowe dane");
                    alert.showAndWait();
                    return null;
                } else {
                    return new String[]{addSalary.getText()};
                }
            }
            return null;
        });

        // Wyświetlenie dialogu i czekanie na akcję użytkownika
        Optional<String[]> result = dialog.showAndWait();
        result.ifPresent(data -> {
            if(selectedTeacher != null){
                classContainer.classes.get(selectedItem).addSalary(selectedTeacher, Integer.parseInt(data[0]));
                teacherTable.refresh();
            }
        });
    }

    @FXML
    public void changeCondition(){
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Zmien status");

        ButtonType okButton = new ButtonType("OK");
        ButtonType cancelButton = ButtonType.CANCEL;
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        GridPane grid = new GridPane();
        ComboBox condition = new ComboBox();
        condition.getItems().addAll(TeacherCondition.values());
        condition.setPromptText("Wybierz status");

        grid.add(condition, 0, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                if (condition.getValue() == null ) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Informacja");
                    alert.setHeaderText("Nie wszystkie dane zostały podane lub podano nieprawidłowe dane");
                    alert.showAndWait();
                    return null;
                } else {
                    return new String[]{condition.getValue().toString()};
                }
            }
            return null;
        });

        Optional<String[]> result = dialog.showAndWait();
        result.ifPresent(data -> {
            if (selectedItem != null) {
                classContainer.classes.get(selectedItem).changeCondition(selectedTeacher,TeacherCondition.valueOf(data[0]));
                teacherTable.refresh();
            }
        });
    }

    @FXML
    public void searchBySurname(){
            teacherTable.setItems(classContainer.classes.get(selectedItem).search(filterField.getText()));
    }

    @FXML
    public void searchByPartial(){
        teacherTable.setItems(classContainer.classes.get(selectedItem).searchPartial(filterField.getText()));
    }

    @FXML
    public void sortByName(){
        teacherTable.setItems(classContainer.classes.get(selectedItem).sortByName());
    }

    @FXML
    public void sortBySalary(){
        teacherTable.setItems(classContainer.classes.get(selectedItem).sortBySalary());
    }

    @FXML
    public void countByStatus(){
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Policz po statusie");
        dialog.getDialogPane().setStyle("-fx-pref-width: 270px;");
        ButtonType closeButton = new ButtonType("Zamknij");
        dialog.getDialogPane().getButtonTypes().addAll(closeButton);

        ComboBox<TeacherCondition> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll(TeacherCondition.values());
        statusComboBox.setPromptText("Wybierz status");

        Label result = new Label("Proszę wybrać status");
        statusComboBox.setOnAction(event -> {
            TeacherCondition condition = statusComboBox.getValue();
            if (condition != null){
                int count = classContainer.classes.get(selectedItem).countByCondition(condition);
                result.setText("Status '" + condition.name() + "' występuje " + count + " razy.");
            }else{
                result.setText("Proszę wybrać status");
            }
        });
        dialog.setResultConverter(dialogButton -> {
            return null;
        });

        GridPane grid = new GridPane();
        grid.add(statusComboBox, 0, 0);
        grid.add(result, 0, 1);
        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait();
    }

    @FXML
    public void maxT(){
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Nauczyciel max");
        dialog.getDialogPane().setStyle("-fx-pref-width: 350px;");
        ButtonType closeButton = new ButtonType("Zamknij");
        dialog.getDialogPane().getButtonTypes().addAll(closeButton);
        Teacher maxTeacher = classContainer.classes.get(selectedItem).max();
        Label name = new Label("Imie: " + maxTeacher.getName());
        Label surname = new Label("Nazwisko: " + maxTeacher.getSurname());
        Label condition = new Label("Status: " + maxTeacher.condition);
        Label yearOfBirth = new Label("Rok urodzenia: " + maxTeacher.getYearOfBirth());
        Label salary = new Label("Wynagrodzenie: " + maxTeacher.getSalary());

        dialog.setResultConverter(dialogButton -> {
            return null;
        });

        GridPane grid = new GridPane();
        grid.add(name, 0, 0);
        grid.add(surname, 0, 1);
        grid.add(condition, 0, 2);
        grid.add(yearOfBirth, 0, 3);
        grid.add(salary, 0, 4);
        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait();
    }

}

