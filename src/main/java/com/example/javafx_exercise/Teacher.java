package com.example.javafx_exercise;

public class Teacher implements Comparable<Teacher> {
    public String name;
    public String surname;
    public TeacherCondition condition;
    public int yearOfBirth;
    public int salary;

    public Teacher(
            String name,
            String surname,
            TeacherCondition condition,
            int yearOfBirth,
            int salary){

        this.name = name;
        this.surname = surname;
        this.condition = condition;
        this.yearOfBirth = yearOfBirth;
        this.salary = salary;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public Integer getYearOfBirth() {
        return yearOfBirth;
    }
    public Integer getSalary() {
        return salary;
    }

    public void printing() {
        System.out.println(this.name + " " + this.surname + " - " + this.condition);
        System.out.println("Rok urodzenia: " + this.yearOfBirth);
        System.out.println("Wynagrodzenie: " + this.salary);

    }


    @Override
    public int compareTo(Teacher o) {
        return this.surname.compareTo(o.surname);
        }
    }


