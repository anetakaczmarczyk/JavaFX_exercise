package com.example.javafx_exercise;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ClassTeacher {
    public String groupName;
    public ArrayList<Teacher> teachers = new ArrayList<>();
    public int maxNumberOfTeachers;
    public ObservableList<Teacher> teacherList = FXCollections.observableArrayList();

    public ClassTeacher(String groupName, int maxNumberOfTeachers) {
        this.groupName = groupName;
        this.maxNumberOfTeachers = maxNumberOfTeachers;
    }

    public void addTeacher(Teacher teacher) {
        if (isTeacherInClass(teacher)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informacja");
            alert.setHeaderText("\nW tej grupie już istnieje taki nauczyciel");
            alert.showAndWait();
        }else{

            if (teachers.size() >= maxNumberOfTeachers){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informacja");
                alert.setHeaderText("\nBrak miejsca w grupie");
                alert.showAndWait();
            }
            else {
                teachers.add(teacher);
                teacherList.add(teacher);
            }
        }
    }

    boolean isTeacherInClass(Teacher teacher){
        for (Teacher t : teachers){
            if (t.name.equals(teacher.name) && t.surname.equals(teacher.surname)) {
                return true;
            }
        }
        return false;
    }
    int returnTeacherId(Teacher t){
        for (int i = 0; i < teachers.size(); i++){
            if (t.name.equals(teachers.get(i).name) && t.surname.equals(teachers.get(i).surname)){
                return i;
            }
        }
        return -1;
    }

    public void addSalary(Teacher t, int salary){
        if (isTeacherInClass(t)){
            t.salary += salary;
        }
        else{
            System.out.println("\nNauczyciel nie znajduje sie w grupie");
        }
    }
    public void removeTeacher(Teacher t){
        int id = returnTeacherId(t);
        if (id != -1){
            teachers.remove(id);
            teacherList.remove(t);
        }
        else{
            System.out.println("Nauczyciel nie znajduje sie w grupie");
        }

    }
  public void changeCondition(Teacher t, TeacherCondition condition) {
      if (isTeacherInClass(t)){
          t.condition = condition;
//          System.out.println("\nStan po zmianie: " +  t.condition);
      }
      else{
          System.out.println("\nNauczyciel nie znajduje sie w grupie");
      }
  }
  public ObservableList<Teacher> search(String surname){
        Teacher searchTeacher = new Teacher("", surname, TeacherCondition.nieobecny, 0, 0);
      ObservableList<Teacher> foundTeachers = FXCollections.observableArrayList();
        int i = 1;
        for (Teacher t : teachers){
            if (t.compareTo(searchTeacher) == 0){
//                System.out.println("\nZnaleziono " + i + " nauczyciela: ");
//                t.printing();
                foundTeachers.add(t);
                i++;
            }
        }
//        if (i == 1){
//            System.out.println("\nNie znaleziono nauczyciela z wprowadzonym nazwiskiem");
//        }
      return foundTeachers;
  }
  public ObservableList<Teacher> searchPartial(String s){
      ObservableList<Teacher> foundTeachers = FXCollections.observableArrayList();
        int i = 1;
        for (Teacher t : teachers){
            if (t.name.contains(s) || t.surname.contains(s)){
                foundTeachers.add(t);
                i++;
            }
        }
//      if (i == 1){
//          System.out.println("\nNie znaleziono nauczyciela zawierajacego podany ciag znakow w imieniu lub nazwisku");
//      }
      return foundTeachers;
  }

  public Integer countByCondition(TeacherCondition tc){
        int count = 0;
        for (Teacher t : teachers){
            if (t.condition.equals(tc)){
                count++;
            }
        }
        return count;
  }
  public void summary(){
        teachers.forEach(Teacher::printing);
  }
  public ObservableList<Teacher> sortByName(){
      ArrayList<Teacher> sortedTeachers = new ArrayList<>(teachers);
      ObservableList<Teacher> sortedT = FXCollections.observableArrayList();
      sortedTeachers.sort(Comparator.comparing(sortedTeacher -> sortedTeacher.name));
      sortedT.addAll(sortedTeachers);
      return sortedT;
  }
  public ObservableList<Teacher> sortBySalary(){
      ObservableList<Teacher> sortedT = FXCollections.observableArrayList();
        ArrayList<Teacher> sortedTeachers = new ArrayList<>(teachers);
        sortedTeachers.sort(new Comparator<Teacher>() {
            @Override
            public int compare(Teacher o1, Teacher o2) {
                return Integer.compare(o2.salary, o1.salary);
            }
        });
      sortedT.addAll(sortedTeachers);
      return sortedT;
  }

  public Teacher max(){
//        System.out.println("\nNauczyciel o największym wynagrodzeniu:");
        return Collections.max(teachers, Comparator.comparing(sortedTeacher -> sortedTeacher.salary));
  }

}
