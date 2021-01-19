package me.rafaelrain.desafiotunts.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.rafaelrain.desafiotunts.info.Info;

import java.util.List;

@Data
@ToString
@RequiredArgsConstructor
public class Aluno {

    private final int id;
    private final String nome;

    private final int faltas;
    private final int[] notas;

    private String situacao;
    private int notaFaltando = 0;

    public static Aluno fromRow(List<Object> row) {
        return new Aluno(
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

    public int getMedia(){
        float soma = notas[0] + notas[1] + notas[2];
        return Math.round(soma / 3);
    }

    public int getRowId(){
        return id+3;
    }
}
