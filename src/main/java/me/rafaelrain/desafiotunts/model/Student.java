package me.rafaelrain.desafiotunts.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@RequiredArgsConstructor
public class Student {

    private final int id;
    private final String name;

    private final int absences;
    private final int[] grades;

    private String situation;
    private int remainGrade = 0;

    public static Student fromRow(List<Object> row) {
        return new Student(
                Integer.parseInt((String) row.get(0)),
                (String) row.get(1),
                Integer.parseInt((String) row.get(2)),
                new int[]{
                        Integer.parseInt((String) row.get(3)),
                        Integer.parseInt((String) row.get(4)),
                        Integer.parseInt((String) row.get(5)),
                }
        );
    }

    public void analyze(int days){
        if(absences > ( (days*25) /100) ){
            setSituation("Reprovado por falta");
            return;
        }

        int average = getAverage();
        if(average < 50){
            setSituation("Reprovado por Nota");
        } else if(average < 70){
            setSituation("Exame Final");
            setRemainGrade(100-average);
        } else {
            setSituation("Aprovado");
        }
    }

    private int getAverage() {
        float sum = grades[0] + grades[1] + grades[2];
        return Math.round(sum / 3);
    }

    public  int getRowId() {
        return id + 3;
    }
}
