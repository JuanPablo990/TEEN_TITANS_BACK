package eci.edu.dosw.parcial.TEEN_TITANS_BACK.Sistema_General;

import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes.Estudiante;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Estudiantes.MateriaInscrita;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias.Materia;
import eci.edu.dosw.parcial.TEEN_TITANS_BACK.Gestión_de_Grupos_y_Materias.Profesor;


import java.util.Scanner;
import java.util.logging.Logger;

public class ProbarSistema {

    private static final Logger logger = Logger.getLogger(ProbarSistema.class.getName());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FachadaSistema fachada = new FachadaSistema(); // instancia de la fachada

        boolean salir = false;

        while (!salir) {
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("1. Registrar Estudiante");
            System.out.println("2. Registrar Materia");
            System.out.println("3. Crear Grupo");
            System.out.println("4. Crear Solicitud de Cambio");
            System.out.println("5. Ver Semáforo Académico");
            System.out.println("6. Generar Reporte Historial Estudiante");
            System.out.println("7. Generar Reporte Compuesto Ejemplo");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1:
                    System.out.print("ID estudiante: ");
                    String idEst = scanner.nextLine();
                    System.out.print("Nombre estudiante: ");
                    String nombre = scanner.nextLine();
                    // suponemos que FachadaSistema tiene un método registrarEstudiante
                    fachada.registrarEstudiante(idEst, nombre);
                    break;

                case 2:
                    System.out.print("Código materia: ");
                    String codMat = scanner.nextLine();
                    System.out.print("Nombre materia: ");
                    String nomMat = scanner.nextLine();
                    fachada.registrarMateria(codMat, nomMat);
                    break;

                case 3:
                    System.out.print("ID grupo: ");
                    String idGrupo = scanner.nextLine();
                    System.out.print("Código materia: ");
                    String codMateria = scanner.nextLine();
                    System.out.print("Nombre profesor: ");
                    String nomProf = scanner.nextLine();
                    System.out.print("Cupo máximo: ");
                    int cupo = scanner.nextInt();
                    scanner.nextLine();
                    // Aquí se puede usar un profesor simple y CapacidadDinamica nula por ahora
                    fachada.crearGrupo(idGrupo, new Materia(codMateria, "nombre temporal"), new Profesor(nomProf), cupo, null);
                    break;

                case 4:
                    System.out.print("ID estudiante: ");
                    String idEstS = scanner.nextLine();
                    System.out.print("ID solicitud: ");
                    String idSol = scanner.nextLine();
                    // Materia problema y sugerida de ejemplo; puedes adaptar
                    fachada.crearSolicitudCambioParaEstudiante(
                            new Estudiante(idEstS, "nombre temporal"),
                            idSol,
                            new MateriaInscrita("MATERIA1"),
                            new MateriaInscrita("MATERIA2")
                    );
                    break;

                case 5:
                    System.out.print("ID estudiante: ");
                    String idEstSem = scanner.nextLine();
                    String estado = fachada.verSemaforoAcademico(new Estudiante(idEstSem, "nombre temporal"));
                    System.out.println("Semáforo académico: " + estado);
                    break;

                case 6:
                    System.out.print("ID estudiante: ");
                    String idEstR = scanner.nextLine();
                    fachada.generarReporteHistorial(idEstR);
                    break;

                case 7:
                    fachada.generarReporteCompuestoEjemplo();
                    break;

                case 8:
                    salir = true;
                    System.out.println("Saliendo del sistema...");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }
        }

        scanner.close();
    }
}
