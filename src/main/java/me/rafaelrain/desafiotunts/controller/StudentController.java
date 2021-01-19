package me.rafaelrain.desafiotunts.controller;

import lombok.RequiredArgsConstructor;
import me.rafaelrain.desafiotunts.model.Student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class StudentController {

    private final int totalDays;

    private final List<Student> students = new ArrayList<>();

    public void add(Student student) {
        students.add(student);
    }

    public void analyzeAll() {
        for (Student student : values()) {
            student.analyze(totalDays);
        }
    }

    public int size() {
        return students.size();
    }

    public Collection<Student> values() {
        return students;
    }
}
