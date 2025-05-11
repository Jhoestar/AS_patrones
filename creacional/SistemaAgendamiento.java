// fabrica abstracta de especialidades médicas
interface FabricaEspecialidad {
    AgendadorCita crearAgendador();
    ServicioNotificacion crearNotificador();
}

// fábrica concreta para odontología
class FabricaOdontologia implements FabricaEspecialidad {
    @Override
    public AgendadorCita crearAgendador() {
        return new AgendadorOdontologia();
    }

    @Override
    public ServicioNotificacion crearNotificador() {
        return new NotificadorOdontologia();
    }
}

// fábrica concreta para cardiología
class FabricaCardiologia implements FabricaEspecialidad {
    @Override
    public AgendadorCita crearAgendador() {
        return new AgendadorCardiologia();
    }

    @Override
    public ServicioNotificacion crearNotificador() {
        return new NotificadorCardiologia();
    }
}

// productos de la fábrica
interface AgendadorCita {
    void agendar(String paciente, String fechaHora);
}

interface ServicioNotificacion {
    void notificar(String paciente, String fechaHora);
}

// agendador y notificador para Odontología
class AgendadorOdontologia implements AgendadorCita {
    @Override
    public void agendar(String paciente, String fechaHora) {
        System.out.printf("Cita de Odontología para %s agendada el %s\n", paciente, fechaHora);
    }
}

class NotificadorOdontologia implements ServicioNotificacion {
    @Override
    public void notificar(String paciente, String fechaHora) {
        System.out.printf("Enviando recordatorio a %s para su cita odontológica el %s\n", paciente, fechaHora);
    }
}

// agendador y notificador de Cardiología
class AgendadorCardiologia implements AgendadorCita {
    @Override
    public void agendar(String paciente, String fechaHora) {
        System.out.printf("Cita de Cardiología para %s agendada el %s\n", paciente, fechaHora);
    }
}

class NotificadorCardiologia implements ServicioNotificacion {
    @Override
    public void notificar(String paciente, String fechaHora) {
        System.out.printf("Enviando notificación a %s para su cita de cardiología el %s\n", paciente, fechaHora);
    }
}


public class SistemaAgendamiento {
    private final AgendadorCita agendador;
    private final ServicioNotificacion notificador;

    public SistemaAgendamiento(FabricaEspecialidad fabrica) {
        this.agendador = fabrica.crearAgendador();
        this.notificador = fabrica.crearNotificador();
    }

    public void agendar(String paciente, String fechaHora) {
        agendador.agendar(paciente, fechaHora);
        notificador.notificar(paciente, fechaHora);
        System.out.println();
    }

    public static void main(String[] args) {
        String paciente = "Ana Torres";
        String fecha = "2025-07-10 11:00";

        String paciente2 = "Luis Pérez";
        String fecha2 = "2025-07-10 12:00";

        SistemaAgendamiento odontologia2 = new SistemaAgendamiento(new FabricaOdontologia());
        odontologia2.agendar(paciente2, fecha2);

        SistemaAgendamiento odontologia = new SistemaAgendamiento(new FabricaOdontologia());
        odontologia.agendar(paciente, fecha);

        SistemaAgendamiento cardiologia = new SistemaAgendamiento(new FabricaCardiologia());
        cardiologia.agendar(paciente, fecha);
    }
}
