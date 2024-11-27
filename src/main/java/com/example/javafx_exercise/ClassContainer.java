package com.example.javafx_exercise;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassContainer {
    public Map<String, ClassTeacher> classes = new HashMap<>();
    private ObservableList<String> observableClassNames = FXCollections.observableArrayList();

    public ObservableList<String> getObservableClassNames() {
        return observableClassNames;
    }
    public void addClass(String className, int max){
        if (classes.containsKey(className)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informacja");
            alert.setHeaderText("\nGrupa " + className + " juz istnieje");
            alert.showAndWait();
        }else{
            classes.put(className, new ClassTeacher(className, max));
            observableClassNames.add(className);
        }
    }

    public void removeClass(String className){
        if (classes.remove(className) != null){
            observableClassNames.remove(className);
            classes.remove(className);
        }
    }

    public List<String> findEmpty(){
        List<String> emptyClasses = new ArrayList<>();
        for(Map.Entry<String, ClassTeacher> entry : classes.entrySet()){
            if (entry.getValue().teachers.isEmpty()){
                emptyClasses.add(entry.getKey());
            }
        }
        return emptyClasses;
    }

    public String summary(String className){
        ClassTeacher classTeacher = classes.get(className);
        int numOfTeachers = classTeacher.teachers.size();
        int max = classTeacher.maxNumberOfTeachers;
        double fillPercentage = ((double) numOfTeachers / max) * 100;
        return String.format("%s - zapelnienie: %.2f%%", className, fillPercentage);
    }
}
