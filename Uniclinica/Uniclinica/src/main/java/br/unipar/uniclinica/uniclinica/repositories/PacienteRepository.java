package br.unipar.uniclinica.uniclinica.repositories;

import br.unipar.uniclinica.uniclinica.domain.Paciente;
import br.unipar.uniclinica.uniclinica.infrastructure.ConnectionFactory;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteRepository {

    private static final String INSERT_ENDERECO = "INSERT INTO endereco (logradouro, numero, complemento, bairro, cidade, uf, cep) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
    private static final String INSERT = "INSERT INTO paciente (nome, email, telefone, cpf, id_endereco, ativo) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
    private static final String UPDATE_ENDERECO = "UPDATE endereco SET logradouro = ?, numero = ?, complemento = ?, bairro = ?, cidade = ?, uf = ?, cep = ? WHERE id = ?";
    private static final String UPDATE = "UPDATE paciente SET nome = ?, telefone = ?, id_endereco = ? WHERE id = ?";
    private static final String DESATIVAR_POR_ID = "UPDATE paciente SET nome = ?, ativo = false WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM paciente ORDER BY nome ASC";
    private static final String FIND_BY_ID = "SELECT * FROM paciente WHERE id = ?";

    public Paciente inserir(Paciente paciente) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = new ConnectionFactory().getConnection();

            pstmt = conn.prepareStatement(INSERT_ENDERECO, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, paciente.getEndereco().getLogradouro());
            pstmt.setInt(2, paciente.getEndereco().getNumero());
            pstmt.setString(3, paciente.getEndereco().getComplemento());
            pstmt.setString(4, paciente.getEndereco().getBairro());
            pstmt.setString(5, paciente.getEndereco().getCidade());
            pstmt.setString(6, paciente.getEndereco().getUf());
            pstmt.setString(7, paciente.getEndereco().getCep());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                paciente.getEndereco().setId(rs.getInt(1));
            } else {
                throw new SQLException("Erro ao obter ID do endereço.");
            }


            pstmt = conn.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, paciente.getNome());
            pstmt.setString(2, paciente.getEmail());
            pstmt.setString(3, paciente.getTelefone());
            pstmt.setString(4, paciente.getCpf());
            pstmt.setInt(5, paciente.getEndereco().getId());
            pstmt.setBoolean(6, true);
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                paciente.setId(rs.getInt(1));
            }

        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }

        return paciente;
    }

    public Paciente editar(Paciente paciente) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = new ConnectionFactory().getConnection();


            pstmt = conn.prepareStatement(UPDATE_ENDERECO);
            pstmt.setString(1, paciente.getEndereco().getLogradouro());
            pstmt.setInt(2, paciente.getEndereco().getNumero());
            pstmt.setString(3, paciente.getEndereco().getComplemento());
            pstmt.setString(4, paciente.getEndereco().getBairro());
            pstmt.setString(5, paciente.getEndereco().getCidade());
            pstmt.setString(6, paciente.getEndereco().getUf());
            pstmt.setString(7, paciente.getEndereco().getCep());
            pstmt.setInt(8, paciente.getEndereco().getId());
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement(UPDATE);
            pstmt.setString(1, paciente.getNome());
            pstmt.setString(2, paciente.getTelefone());
            pstmt.setInt(3, paciente.getEndereco().getId());
            pstmt.setInt(4, paciente.getId());
            pstmt.executeUpdate();

        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }

        return paciente;
    }

    public Paciente deletar(Paciente paciente) throws SQLException, NamingException {
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DESATIVAR_POR_ID)) {

            pstmt.setString(1, paciente.getNome());
            pstmt.setInt(2, paciente.getId());
            pstmt.executeUpdate();

        }
        return paciente;
    }

    public List<Paciente> buscarTodos() throws SQLException, NamingException {
        List<Paciente> pacientes = new ArrayList<>();

        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(FIND_ALL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Paciente paciente = new Paciente();
                paciente.setId(rs.getInt("id"));
                paciente.setNome(rs.getString("nome"));
                paciente.setEmail(rs.getString("email"));
                paciente.setTelefone(rs.getString("telefone"));
                paciente.setCpf(rs.getString("cpf"));
                paciente.setAtivo(rs.getBoolean("ativo"));
                pacientes.add(paciente);
            }
        }

        return pacientes;
    }

    public Paciente buscarPorId(Integer id) throws SQLException, NamingException {
        Paciente paciente = null;

        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(FIND_BY_ID)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    paciente = new Paciente();
                    paciente.setId(rs.getInt("id"));
                    paciente.setNome(rs.getString("nome"));
                    paciente.setEmail(rs.getString("email"));
                    paciente.setTelefone(rs.getString("telefone"));
                    paciente.setCpf(rs.getString("cpf"));
                    paciente.setAtivo(rs.getBoolean("ativo"));
                }
            }
        }

        return paciente;
    }
}
