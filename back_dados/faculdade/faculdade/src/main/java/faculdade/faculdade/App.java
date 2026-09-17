package faculdade.faculdade;

import java.sql.SQLException;

public class App {
    private static final String URL = "jdbc:sqlite:uirf.db"; // banco local (embarcado)

    public static void main(String[] args) {
        UsuarioDAO usuarioDAO = new UsuarioDAO(URL);
        SalaDAO salaDAO = new SalaDAO(URL);
        AgendamentoDAO agendamentoDAO = new AgendamentoDAO(URL);
        DenunciaDAO denunciaDAO = new DenunciaDAO(URL);
        DashboardDAO dashboardDAO = new DashboardDAO(URL);

        try {
            
            usuarioDAO.init();
            salaDAO.init();
            agendamentoDAO.init();
            denunciaDAO.init();
            dashboardDAO.init();

            
            System.out.println("  USUARIO  ");

            // CREATE
            int idAtendente = usuarioDAO
                    .create(new Usuario("Talita Pereira Rocha", "talita@fesp.br", "hash_senha_01", 1, 1));
            int idAluno = usuarioDAO.create(new Usuario("Renan Carlos Vieira", "renan@fesp.br", "hash_senha_02", 2, 1));
            System.out.println("Criado usuario id=" + idAtendente);
            System.out.println("Criado usuario id=" + idAluno);

            // LIST
            System.out.println("\nLista de usuarios:");
            for (Usuario u : usuarioDAO.listAll()) {
                u.exibirDados();
            }

            // READ
            var usuario = usuarioDAO.getById(idAtendente);
            System.out.println("GetById: " + usuario.map(Usuario::getNome).orElse("not found"));

            // UPDATE
            System.out.println("Update ok? " + usuarioDAO.update(
                    new Usuario(idAtendente, "Talita Pereira Rocha", "talita.rocha@fesp.br", "hash_senha_01", 3, 1)));

            // 
            System.out.println("\n  SALA  ");

            // CREATE
            int idAuditorio = salaDAO
                    .create(new Sala("DISPONIVEL", "Auditorio Central", 120, "Projetor, som, microfone"));
            System.out.println("Criada sala id=" + idAuditorio);

            // LIST
            System.out.println("Lista de salas:");
            for (Sala s : salaDAO.listAll()) {
                System.out.println(s.getId() + " - " + s.getNomeSala() + " - " + s.getSituacao() + " - cap. "
                        + s.getCapacidade() + " - " + s.getRecursos());
            }

            // READ
            var sala = salaDAO.getById(idAuditorio);
            System.out.println("GetById: " + sala.map(Sala::getNomeSala).orElse("not found"));

            // UPDATE
            System.out.println("Update ok? " + salaDAO
                    .update(new Sala(idAuditorio, "DISPONIVEL", "Auditorio Central", 150, "Projetor, som, palco")));

            
            System.out.println("\n  AGENDAMENTO  ");

            // CREATE
            int idAgenda1 = agendamentoDAO.create(new Agendamento("2026-10-15", "19:00", "22:00", idAuditorio,
                    idAtendente, "Workshop de carreiras"));
            System.out.println("Criado agendamento id=" + idAgenda1);

            
            try {
                agendamentoDAO.create(new Agendamento("2026-10-15", "20:00", "21:00", idAuditorio, idAtendente,
                        "Reuniao de coordenacao"));
                System.out.println("ERRO: a reserva sobreposta foi aceita!");
            } catch (IllegalStateException e) {
                System.out.println("Bloqueado -> " + e.getMessage());
            }

            
            int idAgenda2 = agendamentoDAO.create(new Agendamento("2026-10-15", "22:00", "23:00", idAuditorio,
                    idAtendente, "Reuniao de encerramento"));
            System.out.println("Criado agendamento id=" + idAgenda2 + " (mesma sala, horario livre)");

            
            System.out.println("\nAgenda detalhada:");
            for (String linha : agendamentoDAO.listAllDetalhado()) {
                System.out.println(linha);
            }
            System.out.println("Reservas em 2026-10-15: " + agendamentoDAO.listByData("2026-10-15").size());

            // READ
            var agendamento = agendamentoDAO.getById(idAgenda1);
            System.out.println("GetById: " + agendamento.map(Agendamento::getFinalidade).orElse("not found"));

            // UPDATE
            System.out.println("Update ok? " + agendamentoDAO.update(new Agendamento(idAgenda1, "2026-10-16", "19:30",
                    "21:30", idAuditorio, idAtendente, "Workshop de carreiras - nova data")));

            
            System.out.println("\n  DENUNCIA  ");

            // CREATE (uma identificada e uma anonima, pelo canal confidencial)
            int idDenuncia1 = denunciaDAO.create(new Denuncia("DEN-2026-001", "Sala ocupada indevidamente",
                    "Auditorio reservado foi ocupado por outra turma", "2026-09-16", "MEDIA", "PENDENTE", idAluno));
            int idDenuncia2 = denunciaDAO.create(new Denuncia("DEN-2026-002", "Demora no atendimento",
                    "Solicitacao aberta ha duas semanas sem retorno", "2026-09-16", "BAIXA", "PENDENTE", 0));
            System.out.println("Criada denuncia id=" + idDenuncia1);
            System.out.println("Criada denuncia anonima id=" + idDenuncia2);

            // LIST
            System.out.println("\nLista de denuncias:");
            for (Denuncia d : denunciaDAO.listAll()) {
                d.exibirDados();
            }
            System.out.println("Pendentes: " + denunciaDAO.listByStatus("PENDENTE").size());

            // READ
            var denuncia = denunciaDAO.getById(idDenuncia1);
            System.out.println("GetById: " + denuncia.map(Denuncia::getProtocolo).orElse("not found"));

            // UPDATE
            System.out.println("Update ok? " + denunciaDAO.update(new Denuncia(idDenuncia1, "DEN-2026-001",
                    "Sala ocupada indevidamente", "Auditorio reservado foi ocupado por outra turma", "2026-09-16",
                    "ALTA", "EM_ANDAMENTO", idAluno)));

            
            System.out.println("\n  DASHBOARD  ");

            // os indicadores sao calculados a partir dos dados gravados acima
            DashboardMetrics metricas = dashboardDAO.calcular("2026-10-01", "2026-10-31");
            int idMetrica = dashboardDAO.create(metricas);
            System.out.println(metricas);
            System.out.println("Fotografia salva no historico com id=" + idMetrica);
            System.out.println("Registros no historico: " + dashboardDAO.listAll().size());

          
            System.out.println("\n  DELETE  ");

            
            System.out.println("Delete metrica ok? " + dashboardDAO.delete(idMetrica));
            System.out.println("Delete denuncia ok? " + denunciaDAO.delete(idDenuncia1));
            System.out.println("Delete denuncia anonima ok? " + denunciaDAO.delete(idDenuncia2));
            System.out.println("Delete agendamento ok? " + agendamentoDAO.delete(idAgenda1));
            System.out.println("Delete agendamento ok? " + agendamentoDAO.delete(idAgenda2));
            System.out.println("Delete sala ok? " + salaDAO.delete(idAuditorio));
            System.out.println("Delete usuario ok? " + usuarioDAO.delete(idAtendente));
            System.out.println("Delete usuario ok? " + usuarioDAO.delete(idAluno));

           

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
