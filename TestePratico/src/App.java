import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import model.Funcionario;

public class App {
    // 3 Deve conter uma classe Principal para executar as seguintes ações:
    public static void main(String[] args) throws Exception {
        // 3.1 Inserir todos os funcionários, na mesma ordem e informações da tabela acima
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        ArrayList<Funcionario> funcionarios = new ArrayList<>();
        FileReader file = new FileReader("src/funcionarios.txt");
        BufferedReader br = new BufferedReader(file);
                
        String linha = br.readLine();
        String[] campo = null;
                
        while(linha != null) {
            campo = linha.split(","); 
                    
            Funcionario f = new Funcionario(
                campo[0], 
                LocalDate.parse(campo[1], dtf), 
                new BigDecimal(campo[2]), 
                campo[3]
            );
                
            funcionarios.add(f);
            linha = br.readLine();
        }

        br.close();

        // 3.2 Remover o funcionário "João" da lista
        Iterator<Funcionario> it = funcionarios.iterator();

        while (it.hasNext()) {
            Funcionario f = it.next();
            if (f.getNome().equalsIgnoreCase("joao")) {
                it.remove();
            }
        }

        // 3.3 Imprimir todos os funcionários com todas suas informações, sendo que:

            // informação de data deve ser exibido no formato dd/mm/aaaa
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            // informação de valor numérico deve ser exibida no formatado com separador de milhar como ponto e decimal como vírgula
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "BR"));
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        String pattern = "##,##0.00";
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        
        System.out.println("Nome\tNascimento\tSalario\tFuncao");
        
        for(Funcionario f : funcionarios) {
            System.out.println(
                f.getNome() + "\t" +
                f.getDataNascimento().format(formatter) + "\t"+
                decimalFormat.format(f.getSalario()) + "\t" +
                f.getFuncao()
            );
        }

        // 3.4 Os funcionários receberam 10% de aumento de salário, atualizar a lista de funcionários com novo valor
        BigDecimal aumento = new BigDecimal(1.1);
        for (Funcionario f : funcionarios) {
            f.setSalario(
                f.getSalario()
                .multiply(aumento)
                .setScale(2, RoundingMode.HALF_EVEN)
            ); 
        }

        // 3.5 Agrupar os funcionários por função em um MAP, sendo a chave a "função" e o valor a "lista de funcionários"
        Map<String,List<Funcionario>>funcionariosPorFuncao = funcionarios
            .stream()
            .collect(
                Collectors
                .groupingBy(Funcionario::getFuncao)
            );

        // 3.6 Imprimir os funcionários, agrupados por função
        funcionariosPorFuncao.entrySet().stream().forEach(System.out::println);

        // 3.8 Imprimir os funcionários que fazem aniversário no mês 10 e 12
        // Não soube desenvolver
        
        // 3.9 Imprimir o funcionário com a maior idade, exibir os atributos: nome e idade
        Optional<Funcionario> maisVelho = funcionarios
            .stream()
            .min(
                Comparator
                .comparing(Funcionario::getDataNascimento)
            );
        maisVelho.ifPresent(System.out::println);

        // 3.10 Imprimir a lista de funcionários por ordem alfabética
        funcionarios.sort(Comparator.comparing(Funcionario::getNome));
        System.out.println("\n" + funcionarios);

        // 3.11 Imprimir o total dos salários dos funcionários
        BigDecimal somaSalarios = BigDecimal.ZERO;
        for (Funcionario f : funcionarios) {
            somaSalarios = somaSalarios.add(f.getSalario());
        }     
        System.out.println("Soma dos salarios\n" + decimalFormat.format(somaSalarios));
       
        // 3.12 Imprimir quantos salários mínimos ganha cada funcionário, considerando que o salário mínimo é R$1212.00
        System.out.println("Nome\tQuantidade de salarios minimos");
        final BigDecimal salarioMinimo = new BigDecimal(1212.00);
        for (Funcionario f : funcionarios) {
            BigDecimal qtdeSalariosMinimos = f.getSalario().divide(salarioMinimo, RoundingMode.HALF_EVEN);
            System.out.println(f.getNome() + "\t" + qtdeSalariosMinimos);
        } 
    }
}
