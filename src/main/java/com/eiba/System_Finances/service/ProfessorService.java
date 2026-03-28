package com.eiba.System_Finances.service;

import com.eiba.System_Finances.entity.Professor;
import com.eiba.System_Finances.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    public Professor cadastrarProfessor(Professor professor){
        return professorRepository.save(professor);
    }

    public Professor buscarPorId(String id){
        if(!professorRepository.existsById(id)){
            throw new RuntimeException("Id não encontrado");
        }
        return professorRepository.findById(id).orElse(null);
    }

    public List<Professor> listarTodos(){
       return professorRepository.findAll();
    }

    public void deletarById(String id){
        if(professorRepository.findById(id) == null){
            throw new RuntimeException("Id não encontrado");
        }
        professorRepository.deleteById(id);
    }

    public Professor atualizarProfessor(Professor professor){
        if(!professorRepository.existsById(professor.getId())){
            throw new RuntimeException("Professor não encontrado");
        }
        return professorRepository.save(professor);
    }
}
