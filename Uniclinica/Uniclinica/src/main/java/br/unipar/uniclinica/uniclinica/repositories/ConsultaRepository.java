package br.unipar.uniclinica.uniclinica.repositories;

import br.unipar.uniclinica.uniclinica.domain.Consulta;
import br.unipar.uniclinica.uniclinica.domain.MotivoCancelamento;
import br.unipar.uniclinica.uniclinica.infrastructure.ConnectionFactory;

import javax.naming.NamingException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsultaRepository {

    private static final String INSERT = "INSERT INTO consulta (id_paciente, id_medico, data_hora, ativo) VALUES (?, ?, ?, true) RETURNING id";
    private static final String CANCELAR = "UPDATE consulta SET ativo = false, motivo_cancelamento = ?, data_cancelamento = ? WHERE id = ?";
    private static final String FIND_BY_ID = "SELECT * FROM consulta WHERE id = ?";
    private static final String FIND_MEDICO = "SELECT * FROM consulta WHERE id_medico = ? AND ativo = true";
    private static final String EXISTS_PACIENTE_DIA = "SELECT COUNT(*) FROM consulta WHERE id_paciente = ? AND DATE(data_hora) = ? AND ativo = true";
    private static final String EXISTS_MEDICO_HORA = "SELECT COUNT(*) FROM consulta WHERE id_medico = ? AND data_hora = ? AND ativo = true";

    public Consulta inserir(Consulta consulta) throws SQLException, NamingException {
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, consulta.getPaciente().getId());
            pstmt.setInt(2, consulta.getMedico().getId());
            pstmt.setTimestamp(3, Timestamp.valueOf(consulta.getDataHora()));
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    consulta.setId(rs.getInt(1));
                }
            }
        }
        return consulta;
    }

    public void cancelar(Consulta consulta, MotivoCancelamento motivo) throws SQLException, NamingException {
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(CANCELAR)) {

            pstmt.setString(1, motivo.name());
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(3, consulta.getId());
            pstmt.executeUpdate();
        }
    }

    public Consulta buscarPorId(int id) throws SQLException, NamingException {
        Consulta consulta = null;

        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(FIND_BY_ID)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    consulta = new Consulta();
                    consulta.setId(rs.getInt("id"));
                    consulta.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
                }
            }
        }

        return consulta;
    }

    public boolean temConsultaNoMesmoDia(int pacienteId, LocalDate data) throws SQLException, NamingException {
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(EXISTS_PACIENTE_DIA)) {

            pstmt.setInt(1, pacienteId);
            pstmt.setDate(2, Date.valueOf(data));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean existeConsultaComMedico(int medicoId, LocalDateTime dataHora) throws SQLException, NamingException {
        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(EXISTS_MEDICO_HORA)) {

            pstmt.setInt(1, medicoId);
            pstmt.setTimestamp(2, Timestamp.valueOf(dataHora));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public List<Consulta> buscarMedico(int medicoId) {
        List<Consulta> consultas = new ArrayList<>();
        try {
            Connection conn = new ConnectionFactory().getConnection();
            PreparedStatement ps = conn.prepareStatement(FIND_MEDICO);
            ps.setInt(1, medicoId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Consulta consulta = new Consulta();
                consulta.setId(rs.getInt("id"));
                consulta.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
                consulta.setAtivo(rs.getBoolean("ativo"));
                // Aqui você pode preencher paciente, médico, etc., se necessário
                consultas.add(consulta);
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return consultas;
    }
}
