package br.com.alura.tabelafipe.fipe.principal;

import br.com.alura.tabelafipe.fipe.model.Dados;
import br.com.alura.tabelafipe.fipe.model.Modelos;
import br.com.alura.tabelafipe.fipe.model.Veiculo;
import br.com.alura.tabelafipe.fipe.service.ConsumoAPI;
import br.com.alura.tabelafipe.fipe.service.ConverterDado;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner sc = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverterDado conversor = new ConverterDado();

    private final String URL_BASE= "https://parallelum.com.br/fipe/api/v1/";
    private final String API_KEY ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiZW1haWwiOiJleGFtcGxlLmNvbSIsImlhdCI6MTUxNjIzOTAyMn0.CmNu23cNxIhxZa9TABqIPD2t3Ja6Vmu_B0l2DJfiIaA";

    public void exibirMenu(){

        var menu = """
                **** OPÇÕES ****
                Carro
                Moto
                Caminhão
                
                Digite uma das opções para consultar:
                """;
        System.out.println(menu);

        String opcaoVeiculo = sc.nextLine();

        String endereco;

        if(opcaoVeiculo.toLowerCase().contains("carr")){
            endereco = URL_BASE+"carros/marcas";
        } else if (opcaoVeiculo.toLowerCase().contains("mot")) {
            endereco = URL_BASE+"motos/marcas";
        }else{
            endereco = URL_BASE+"caminhoes/marcas";
        }

        var json = consumo.obterDados(endereco);

        var marcas = conversor.obterLista(json, Dados.class);

        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Informe o codigo da marca para consulta:");
        var codMarca = sc.nextLine();

        endereco = endereco + "/"+ codMarca+"/modelos";
        json = consumo.obterDados(endereco);
        var modeloLista = conversor.obterDados(json, Modelos.class);

        System.out.println("\nModelos dessa marca:");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("\nDigite um trecho do nome do carro a ser buscado:");
        var nomeVeiculo = sc.nextLine();

        List<Dados> modelosFiltrados = modeloLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\nModelos filtrados:");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("Digite o codigo do modelo desejado:");
        var modeloSelecionado = sc.nextLine();

        endereco = endereco +"/"+ modeloSelecionado +"/anos";
        json = consumo.obterDados(endereco);
        List<Dados> anos = conversor.obterLista(json, Dados.class);
        List<Veiculo> veiculos = new ArrayList<>();

        for (int i =0;i< anos.size(); i++){
            var enderecoAno = endereco+"/"+ anos.get(i).codigo();
            json = consumo.obterDados(enderecoAno);

            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);

            veiculos.add(veiculo);
        }

        System.out.println("\nTodos os veiculos filtrados com avaliações por ano:");
        veiculos.forEach(System.out::println);

    }
}
