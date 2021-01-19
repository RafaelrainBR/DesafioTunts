package me.rafaelrain.desafiotunts.worker;

import lombok.RequiredArgsConstructor;
import me.rafaelrain.desafiotunts.model.Aluno;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class StudentWorker {

    private final int aulas;

    private final List<Aluno> alunoList = new ArrayList<>();

    public void add(Aluno aluno) {
        analyze(aluno);
        alunoList.add(aluno);
    }

    private void analyze(Aluno aluno){
        int faltas = aluno.getFaltas();

        if(faltas > ( (aulas*25) /100) ){
            aluno.setSituacao("Reprovado por falta");
            alunoList.add(aluno);
            return;
        }

        int media = aluno.getMedia();
        if(media < 5){
            aluno.setSituacao("Reprovado por Nota");
        } else if(media < 7){
            aluno.setSituacao("Exame Final");
            aluno.setNotaFaltando(100-media);
        } else {
            aluno.setSituacao("Aprovado");
        }
    }

    public int size() {
        return alunoList.size();
    }

    public Collection<Aluno> values() {
        return alunoList;
    }
}
