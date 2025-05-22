package br.unipar.uniclinica.uniclinica.controller;

import br.unipar.uniclinica.uniclinica.domain.Consulta;
import br.unipar.uniclinica.uniclinica.dto.ConsultaAgendarRequestDTO;
import br.unipar.uniclinica.uniclinica.dto.ConsultaCancelarRequestDTO;
import br.unipar.uniclinica.uniclinica.dto.ExceptionDTO;
import br.unipar.uniclinica.uniclinica.domain.MotivoCancelamento;
import br.unipar.uniclinica.uniclinica.exceptions.BusinessException;
import br.unipar.uniclinica.uniclinica.services.ConsultaService;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/consultas")
public class ConsultaController {

    private final ConsultaService consultaService = new ConsultaService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response agendar(ConsultaAgendarRequestDTO dto) {
        try {
            Consulta consulta = new Consulta(dto);
            Consulta novaConsulta = consultaService.agendar(consulta);

            return Response.status(Response.Status.CREATED)
                    .entity(novaConsulta)
                    .build();

        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ExceptionDTO(e.getMessage()))
                    .build();

        } catch (Exception e) {
            return Response.serverError()
                    .entity(new ExceptionDTO("Erro interno ao agendar consulta."))
                    .build();
        }
    }

    @PUT
    @Path("/cancelar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancelar(ConsultaCancelarRequestDTO dto) {
        try {
            Consulta consulta = new Consulta();
            consulta.setId(dto.getConsultaId());

            MotivoCancelamento motivo = MotivoCancelamento.valueOf(dto.getMotivo().toUpperCase());

            consultaService.cancelar(consulta, motivo);

            return Response.ok().build();

        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ExceptionDTO(e.getMessage()))
                    .build();

        } catch (Exception e) {
            return Response.serverError()
                    .entity(new ExceptionDTO("Erro interno ao cancelar consulta."))
                    .build();
        }
    }
}

