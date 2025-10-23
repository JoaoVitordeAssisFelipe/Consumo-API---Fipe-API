package br.com.alura.tabelafipe.fipe.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface IConvertDado {
    <T> T obterDados(String json, Class<T> classe);

    <T> List<T> obterLista(String json, Class<T>Class) throws JsonProcessingException;
}
