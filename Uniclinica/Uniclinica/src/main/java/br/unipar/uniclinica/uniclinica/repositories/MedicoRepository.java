package br.unipar.uniclinica.uniclinica.repositories;

import br.unipar.uniclinica.uniclinica.domain.Medico;
import br.unipar.uniclinica.uniclinica.infrastructure.ConnectionFactory;

import javax.naming.NamingException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MedicoRepository {

    private static final String INSERT_ENDERECO = "INSERT INTO endereco (logradouro, numero, complemento, bairro," +
            " cidade, uf, cep) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";

    private static final String INSERT = "INSERT INTO medico (nome, email, telefone, crm, especialidade, id_endereco, ativo) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";

    private static final String UPDATE_ENDERECO = "UPDATE endereco SET logradouro = ?, numero = ?, complemento = ?, bairro = ?, cidade = ?, uf = ?, cep = ? WHERE id = ?";

    private static final String UPDATE = "UPDATE medico SET nome = ?, telefone = ?, id_endereco = ? WHERE id = ?";

    private static final String UPDATE_STATUS = "UPDATE medico SET nome = ?, ativo = false WHERE id = ?";

    private static final String FIND_ALL = "SELECT * FROM medico ORDER BY nome ASC";

    private static final String FIND_BY_ID = "SELECT * FROM medico WHERE id = ?";

    private final ConsultaRepository consultaRepository = new ConsultaRepository();

    public Medico inserir(Medico medico) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = new ConnectionFactory().getConnection();

            // Inserindo endereço
            pstmt = conn.prepareStatement(INSERT_ENDERECO, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, medico.getEndereco().getLogradouro());
            pstmt.setInt(2, medico.getEndereco().getNumero());
            pstmt.setString(3, medico.getEndereco().getComplemento());
            pstmt.setString(4, medico.getEndereco().getBairro());
            pstmt.setString(5, medico.getEndereco().getCidade());
            pstmt.setString(6, medico.getEndereco().getUf());
            pstmt.setString(7, medico.getEndereco().getCep());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                medico.getEndereco().setId(rs.getInt(1));
            } else {
                throw new SQLException("Erro ao obter o ID do endereço.");
            }

            // Inserindo médico
            pstmt = conn.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, medico.getNome());
            pstmt.setString(2, medico.getEmail());
            pstmt.setString(3, medico.getTelefone());
            pstmt.setString(4, medico.getCrm());
            pstmt.setString(5, medico.getEspecialidade());
            pstmt.setInt(6, medico.getEndereco().getId());
            pstmt.setBoolean(7, true);
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                medico.setId(rs.getInt(1));
                medico.setAtivo(true);
            }

        } finally {
            if (pstmt != null) pstmt.close();
            if (rs != null) rs.close();
            if (conn != null) conn.close();
        }

        return medico;
    }

    public Medico editar(Medico medico) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = new ConnectionFactory().getConnection();

            pstmt = conn.prepareStatement(UPDATE_ENDERECO);
            pstmt.setString(1, medico.getEndereco().getLogradouro());
            pstmt.setInt(2, medico.getEndereco().getNumero());
            pstmt.setString(3, medico.getEndereco().getComplemento());
            pstmt.setString(4, medico.getEndereco().getBairro());
            pstmt.setString(5, medico.getEndereco().getCidade());
            pstmt.setString(6, medico.getEndereco().getUf());
            pstmt.setString(7, medico.getEndereco().getCep());
            pstmt.setInt(8, medico.getEndereco().getId());
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement(UPDATE);
            pstmt.setString(1, medico.getNome());
            pstmt.setString(2, medico.getTelefone());
            pstmt.setInt(3, medico.getEndereco().getId());
            pstmt.setInt(4, medico.getId());
            pstmt.executeUpdate();

        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }

        return medico;
    }

    public List<Medico> listar() throws SQLException, NamingException {
        List<Medico> medicos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = new ConnectionFactory().getConnection();
            pstmt = conn.prepareStatement(FIND_ALL);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Medico medico = new Medico();
                medico.setId(rs.getInt("id"));
                medico.setNome(rs.getString("nome"));
                medico.setEmail(rs.getString("email"));
                medico.setTelefone(rs.getString("telefone"));
                medico.setCrm(rs.getString("crm"));
                medico.setEspecialidade(rs.getString("especialidade"));
                medico.setAtivo(rs.getBoolean("ativo")); // ADICIONADO
                medicos.add(medico);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }

        return medicos;
    }

    public Medico excluir(Medico medico) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = new ConnectionFactory().getConnection();
            pstmt = conn.prepareStatement(UPDATE_STATUS);
            pstmt.setString(1, medico.getNome());
            pstmt.setInt(2, medico.getId());
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }

        medico.setAtivo(false);
        return medico;
    }

    public Medico buscarPorId(Integer id) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Medico medico = null;

        try {
            conn = new ConnectionFactory().getConnection();
            pstmt = conn.prepareStatement(FIND_BY_ID);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                medico = new Medico();
                medico.setId(rs.getInt("id"));
                medico.setNome(rs.getString("nome"));
                medico.setEmail(rs.getString("email"));
                medico.setTelefone(rs.getString("telefone"));
                medico.setCrm(rs.getString("crm"));
                medico.setEspecialidade(rs.getString("especialidade"));
                medico.setAtivo(rs.getBoolean("ativo")); // ESSENCIAL
            }

        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }

        return medico;
    }

    public List<Medico> buscarMedicosDisponiveis(LocalDateTime dataHora) {
        List<Medico> disponiveis = new ArrayList<>();

        try {
            List<Medico> todosMedicos = listar();
            for (Medico medico : todosMedicos) {
                if (medico.isAtivo() && !consultaRepository.existeConsultaComMedico(medico.getId(), dataHora)) {
                    disponiveis.add(medico);
                }
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }

        return disponiveis;
    }
}
