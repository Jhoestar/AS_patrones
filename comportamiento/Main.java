import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// entidad cita médica
class Cita {
    private String paciente;
    private String doctor;
    private LocalDateTime fechaHora;

    public Cita(String paciente, String doctor, LocalDateTime fechaHora) {
        this.paciente = paciente;
        this.doctor = doctor;
        this.fechaHora = fechaHora;
    }

    public String getPaciente() {
        return paciente;
    }

    public String getDoctor() {
        return doctor;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    @Override
    public String toString() {
        return "[Cita{" +
               "paciente='" + paciente + '\'' +
               ", doctor='" + doctor + '\'' +
               ", fechaHora=" + fechaHora +
               "}]";
    }
}

// repositorio simulado
class RepositorioCitas {
    private static final List<Cita> citasAgendadas = new ArrayList<>();

    public static List<Cita> obtenerTodas() {
        return citasAgendadas;
    }

    public static void guardar(Cita cita) {
        citasAgendadas.add(cita);
    }
}

// manejador de citas
abstract class ManejadorCita {
    protected ManejadorCita siguiente;

    public ManejadorCita enlaceSiguiente(ManejadorCita siguiente) {
        this.siguiente = siguiente;
        return siguiente;
    }

    public void procesar(Cita cita) {
        if (manejaCita(cita)) {
            if (siguiente != null) {
                siguiente.procesar(cita);
            }
        }
    }

    protected abstract boolean manejaCita(Cita cita);
}

// verifica disponibilidad del doctor
class VerificarDisponibilidadDoctor extends ManejadorCita {
    @Override
    protected boolean manejaCita(Cita cita) {
        boolean ocupado = RepositorioCitas.obtenerTodas().stream()
            .anyMatch(existing ->
                existing.getDoctor().equals(cita.getDoctor())
                && existing.getFechaHora().equals(cita.getFechaHora())
            );
        if (ocupado) {
            System.out.println("El Dr. " + cita.getDoctor() + " no está disponible en " + cita.getFechaHora());
            return false;
        }
        System.out.println("Dr. " + cita.getDoctor() + " disponible en " + cita.getFechaHora());
        return true;
    }
}

// verifica que el paciente no tenga otra cita en ese mismo horario
class VerificarColisionPaciente extends ManejadorCita {
    @Override
    protected boolean manejaCita(Cita cita) {
        boolean tieneOtra = RepositorioCitas.obtenerTodas().stream()
            .anyMatch(existing ->
                existing.getPaciente().equals(cita.getPaciente())
                && existing.getFechaHora().equals(cita.getFechaHora())
            );
        if (tieneOtra) {
            System.out.println("El paciente " + cita.getPaciente() + " ya tiene cita en " + cita.getFechaHora());
            return false;
        }
        System.out.println("Paciente " + cita.getPaciente() + " sin colisiones en " + cita.getFechaHora());
        return true;
    }
}

// registra la cita
class RegistrarCitaHandler extends ManejadorCita {
    @Override
    protected boolean manejaCita(Cita cita) {
        RepositorioCitas.guardar(cita);
        System.out.println("Cita registrada: " + cita);
        return true;
    }
}

// clase principal
public class Main {
    public static void main(String[] args) {
        // simular que ya hay una cita en el sistema
        RepositorioCitas.guardar(new Cita("Ana", "Pérez", LocalDateTime.of(2025,5,12,10,0)));

        // crear la nueva solicitud de cita
        Cita nueva = new Cita("Juan", "Pérez", LocalDateTime.of(2025,5,12,10,0));

        // construir la cadena de responsabilidad
        ManejadorCita h1 = new VerificarDisponibilidadDoctor();
        ManejadorCita h2 = h1.enlaceSiguiente(new VerificarColisionPaciente());
        h2.enlaceSiguiente(new RegistrarCitaHandler());

        // ejecutar el flujo
        System.out.println("Iniciando procesamiento de cita:");
        h1.procesar(nueva);
    }
}
