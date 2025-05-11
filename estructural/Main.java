import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

class CitaMedica {
    private String doctor;
    private String especialidad;
    private LocalDate fecha;

    public CitaMedica(String doctor, String especialidad, LocalDate fecha) {
        this.doctor = doctor;
        this.especialidad = especialidad;
        this.fecha = fecha;
    }

    public String getDoctor() {
        return doctor;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public LocalDate getFecha() {
        return fecha;
    }
    
    @Override
    public String toString() {
        return "CitaMedica{" +
                "doctor='" + doctor + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", fecha=" + fecha +
                '}';
    }
}

// OBJETO CRITERIO: representa un filtro individual de búsqueda
class Criterio {
    private String campo;
    private String operador;
    private Object valor;

    public Criterio(String campo, String operador, Object valor) {
        this.campo = campo;
        this.operador = operador;
        this.valor = valor;
    }

    public Predicate<CitaMedica> cumple() {
        switch (campo) {
            case "doctor":
                return cita -> operador.equals("=") && cita.getDoctor().equals(valor);
            case "especialidad":
                return cita -> operador.equals("=") && cita.getEspecialidad().equals(valor);
            case "fecha":
                return cita -> operador.equals("=") && cita.getFecha().equals(valor);
            default:
                return cita -> true;
        }
    }
}

// QUERY OBJECT: construye y ejecuta consultas
class ConsultaCita {
    private List<Criterio> criterios = new ArrayList<>();

    public void agregarCriterio(Criterio criterio) {
        criterios.add(criterio);
    }

    public List<CitaMedica> ejecutar(List<CitaMedica> citas) {
        List<CitaMedica> resultados = new ArrayList<>();

        for (CitaMedica cita : citas) {
            boolean cumpleTodos = true;

            for (Criterio criterio : criterios) {
                Predicate<CitaMedica> condicion = criterio.cumple();
                if (!condicion.test(cita)) {
                    cumpleTodos = false;
                    break;
                }
            }

            if (cumpleTodos) {
                resultados.add(cita);
            }
        }

        return resultados;
    }
}

public class Main {
    public static void main(String[] args) {
        // Nos creamos una lista de citas médicas
        List<CitaMedica> citas = List.of(
            new CitaMedica("Dr. Pérez", "Cardiología", LocalDate.now()),
            new CitaMedica("Dra. Gómez", "Dermatología", LocalDate.now().plusDays(1)),
            new CitaMedica("Dr. Pérez", "Cardiología", LocalDate.now().plusDays(2)),
            new CitaMedica("Dr. López", "Neurología", LocalDate.now())
        );

        // Crear consulta y agregamos los criterios de búsqueda
        ConsultaCita consulta = new ConsultaCita();
        consulta.agregarCriterio(new Criterio("doctor", "=", "Dr. Pérez"));
        consulta.agregarCriterio(new Criterio("fecha", "=", LocalDate.now()));

        // Ejecutar la consulta en nuestra lista de citas
        List<CitaMedica> resultados = consulta.ejecutar(citas);

        // Mostrar resultados
        System.out.println("Citas encontradas:");
        for (CitaMedica cita : resultados) {
            System.out.println(cita);
        }
    }
}
