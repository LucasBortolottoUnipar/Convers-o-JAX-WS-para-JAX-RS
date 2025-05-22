package br.unipar.uniclinica.uniclinica.services;

import java.util.regex.Pattern;
import br.unipar.uniclinica.uniclinica.domain.Endereco;

public class Validacoes {
    //Nome do usuário antes do "@" -- "@"deve estar na Stirng -- dominio depois do "@"-- . depois do dominio -- extensão
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static boolean validaEmail(String email){
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    //^ = Iniciar string - +? = O + pode estar presente ou não no número - \\ = utilizada antes de caracteres especiais
    //s = indica que podem haver espaços - {10-15} indica tamanho do telefone.
    private static final String TELEFONE_REGEX = "^\\+?[0-9\\- \\s\\(\\)]{10,15}$";
    private static final Pattern TELEFONE_PATTERN = Pattern.compile(TELEFONE_REGEX);

    public static boolean validaTelefone(String telefone){
        return telefone != null && TELEFONE_PATTERN.matcher(telefone).matches();
    }

    private static final String CRM_REGEX = "^[0-9]{6}+/+[A-Za-z]{2}$";
    private static final Pattern CRM_PATTERN = Pattern.compile(CRM_REGEX);

    public static boolean validaCRM(String crm){
        return crm != null && CRM_PATTERN.matcher(crm).matches();
    }

    //CPF tem que ser completamente válido e utilizavel, dá para utilizar um gerador de CPF para teste
    public static boolean validaCPF(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            return false;
        }

        if (cpf.chars().distinct().count() == 1) {
            return false;
        }

        try {
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
            }

            int primeiroDigito = 11 - (soma % 11);
            if (primeiroDigito >= 10) primeiroDigito = 0;

            if (primeiroDigito != Character.getNumericValue(cpf.charAt(9))) {
                return false;
            }

            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
            }

            int segundoDigito = 11 - (soma % 11);
            if (segundoDigito >= 10) segundoDigito = 0;

            return segundoDigito == Character.getNumericValue(cpf.charAt(10));
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validarLogradouro(String logradouro){
        return logradouro != null && !logradouro.isEmpty();
    }

    public static boolean validarNumero(int numero){
        return numero > 0;
    }

    public static boolean validarComplemento(String complemento){
        return complemento != null && !complemento.isEmpty();
    }

    public static boolean validarBairro(String bairro){
        return bairro != null && !bairro.isEmpty() && bairro.length() < 100;
    }

    public static boolean validarCep(String cep){
        return cep != null && !cep.isEmpty() && cep.length() == 9;
    }

    public static boolean validarCidade(String cidade){
        return cidade != null && !cidade.isEmpty();
    }

    public static boolean validarUF(String uf){
        return uf != null && !uf.isEmpty() && uf.length() == 2;
    }

}
