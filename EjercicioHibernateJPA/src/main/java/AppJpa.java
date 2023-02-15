import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Atleta;

public class AppJpa {
	static EntityManager entity = JPAUtil.getEntityManagerFactory().createEntityManager();
	static Query querySelect = entity.createQuery("SELECT a FROM Atleta a ORDER BY Total DESC", Atleta.class);
	static Query querySelectHombre = entity
			.createQuery("SELECT a FROM Atleta a WHERE Genre = 'H' ORDER BY Total DESC", Atleta.class);
	static Query querySelectMujer = entity
			.createQuery("SELECT a FROM Atleta a WHERE Genre = 'M' ORDER BY Total DESC", Atleta.class);
	static Query querySelectMaster = entity.createQuery("SELECT a FROM Atleta a WHERE Age >= 32 ORDER BY Total DESC",
			Atleta.class);
	static Query querySelectJuveniles = entity.createQuery("SELECT a FROM Atleta a WHERE Age < 32 ORDER BY Total DESC",
			Atleta.class);
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		int id;
		String fullName;
		int age;
		String genre;
		int benchPress;
		int deadlift;
		int squat;
		int total;
		Atleta atleta = new Atleta();
		int option;

		do {
			System.out.println("-----------------------------------");
			System.out.println("\tMENÚ REGISTRO ATLETA");
			System.out.println("-----------------------------------");
			System.out.println("1. Introducir Atleta");
			System.out.println("2. Consultar Atleta");
			System.out.println("3. Borrar Atleta");
			System.out.println("4. Actualizar Atleta");
			System.out.println("5. Consultar BD");
			System.out.println("6. Ver Record");
			System.out.println("7. Salir");
			System.out.print("Elija una opción (1-7): ");

			option = sc.nextInt();

			switch (option) {
			case 1:
				System.out.println("-----------------------------------");
				System.out.println("\tIntroduce los datos");
				System.out.println("-----------------------------------");
				try {
					System.out.print("Nombre Completo: ");
					fullName = sc.next();
					System.out.print("Edad: ");
					age = Integer.parseInt(sc.next());
					System.out.print("Género: ");
					genre = sc.next();
					System.out.print("Press Banca(kg): ");
					benchPress = Integer.parseInt(sc.next());
					System.out.print("Peso Muerto(kg): ");
					deadlift = Integer.parseInt(sc.next());
					System.out.print("Sentadillas(Nº): ");
					squat = Integer.parseInt(sc.next());
					total = calcularTotal(benchPress, deadlift, squat);

					atleta.setFullName(fullName);
					atleta.setAge(age);
					atleta.setGenre(genre);
					atleta.setBenchPress(benchPress);
					atleta.setDeadlift(deadlift);
					atleta.setSquat(squat);
					atleta.setTotal(total);

					insertarAtleta(atleta);
				} catch (Exception e) {
					System.out.println("Atleta no Insertado, Error: " + e);
				}
				break;
			case 2:
				System.out.println("-----------------------------------");
				System.out.println("\tIntroduce el id");
				System.out.println("-----------------------------------");
				try {
					System.out.print("Id: ");
					id = Integer.parseInt(sc.next());
					buscarAtleta(id);
				} catch (Exception e) {
					System.out.println("Formato de Búsqueda no válido, Error: " + e);
				}
				break;
			case 3:
				System.out.println("-----------------------------------");
				System.out.println("\tIntroduce el id");
				System.out.println("-----------------------------------");
				try {
					System.out.print("Id: ");
					id = Integer.parseInt(sc.next());
					if (buscarAtleta(id) == true) {
						borrarAtleta(id);
					}
				} catch (Exception e) {
					System.out.println("Formato de Búsqueda no válido, Error: " + e);
				}
				break;
			case 4:
				System.out.println("-----------------------------------");
				System.out.println("\tIntroduce el id");
				System.out.println("-----------------------------------");
				try {
					System.out.print("Id: ");
					id = Integer.parseInt(sc.next());
					if (buscarAtleta(id) == true) {
						actualizarAtleta(id);
					}
				} catch (Exception e) {
					System.out.println("Formato de Búsqueda no válido, Error: " + e);
				}
				break;
			case 5:
				try {
					consultarBD();
				} catch (Exception e) {
					System.out.println("Formato de Búsqueda no válido, Error: " + e);
				}
				break;
			case 6:
				try {
					verRecord();
				} catch (Exception e) {
					System.out.println("Formato de Búsqueda no válido, Error: " + e);
				}
				break;
			case 7:
				System.out.println("Cerrando Aplicación.....");
				break;
			default:
				System.out.println("Opción inválida. Inténtalo de nuevo.");
			}
		} while (option != 7);

		sc.close();

	}

	private static void verRecord() {
		entity.getTransaction().begin();
		List<Atleta> listaAtletas = querySelect.getResultList();
		entity.getTransaction().commit();
		System.out.println("\n-----------------------------------");
		System.out.println("Atleta Con Mayor Press Banca");
		System.out.println("-----------------------------------");
		Atleta maxScoreBenchPress = null;
		for (Atleta atleta : listaAtletas) {
			if (maxScoreBenchPress == null || atleta.getBenchPress() > maxScoreBenchPress.getBenchPress()) {
				maxScoreBenchPress = atleta;
			}
		}
		System.out.print("\nId(" + maxScoreBenchPress.getId() + ")" + " Nombre=>" + maxScoreBenchPress.getFullName()
				+ " Edad=>" + maxScoreBenchPress.getAge() + " Género=>" + maxScoreBenchPress.getGenre()
				+ " Press Banca(kg)=>" + maxScoreBenchPress.getBenchPress() + " Peso Muerto(kg)=>"
				+ maxScoreBenchPress.getDeadlift() + " Sentadillas(Nº)=>" + maxScoreBenchPress.getSquat() + " TOTAL=>"
				+ maxScoreBenchPress.getTotal() + "\n");

		System.out.println("\n-----------------------------------");
		System.out.println("Atleta Con Mayor Peso Muerto");
		System.out.println("-----------------------------------");
		Atleta maxScoreDeadLift = null;
		for (Atleta atleta : listaAtletas) {
			if (maxScoreDeadLift == null || atleta.getDeadlift() > maxScoreDeadLift.getDeadlift()) {
				maxScoreDeadLift = atleta;
			}
		}
		System.out.print("\nId(" + maxScoreDeadLift.getId() + ")" + " Nombre=>" + maxScoreDeadLift.getFullName()
				+ " Edad=>" + maxScoreDeadLift.getAge() + " Género=>" + maxScoreDeadLift.getGenre()
				+ " Press Banca(kg)=>" + maxScoreDeadLift.getBenchPress() + " Peso Muerto(kg)=>"
				+ maxScoreDeadLift.getDeadlift() + " Sentadillas(Nº)=>" + maxScoreDeadLift.getSquat() + " TOTAL=>"
				+ maxScoreDeadLift.getTotal() + "\n");

		System.out.println("\n-----------------------------------");
		System.out.println("Atleta Con Mayor Número de Sentadillas");
		System.out.println("-----------------------------------");
		Atleta maxScoreSquat = null;
		for (Atleta atleta : listaAtletas) {
			if (maxScoreSquat == null || atleta.getSquat() > maxScoreSquat.getSquat()) {
				maxScoreSquat = atleta;
			}
		}
		System.out.print("\nId(" + maxScoreSquat.getId() + ")" + " Nombre=>" + maxScoreSquat.getFullName() + " Edad=>"
				+ maxScoreSquat.getAge() + " Género=>" + maxScoreSquat.getGenre() + " Press Banca(kg)=>"
				+ maxScoreSquat.getBenchPress() + " Peso Muerto(kg)=>" + maxScoreSquat.getDeadlift()
				+ " Sentadillas(Nº)=>" + maxScoreSquat.getSquat() + " TOTAL=>" + maxScoreSquat.getTotal() + "\n");

		System.out.println("\n-----------------------------------");
		System.out.println("Atleta Con Mayor Puntuación Total");
		System.out.println("-----------------------------------");
		Atleta maxScoreTotal = null;
		for (Atleta atleta : listaAtletas) {
			if (maxScoreTotal == null || atleta.getTotal() > maxScoreTotal.getTotal()) {
				maxScoreTotal = atleta;
			}
		}
		System.out.print("\nId(" + maxScoreTotal.getId() + ")" + " Nombre=>" + maxScoreTotal.getFullName() + " Edad=>"
				+ maxScoreTotal.getAge() + " Género=>" + maxScoreTotal.getGenre() + " Press Banca(kg)=>"
				+ maxScoreTotal.getBenchPress() + " Peso Muerto(kg)=>" + maxScoreTotal.getDeadlift()
				+ " Sentadillas(Nº)=>" + maxScoreTotal.getSquat() + " TOTAL=>" + maxScoreTotal.getTotal() + "\n");

	}

	private static void consultarBD() {
		int option;
		do {
			System.out.println("-----------------------------------");
			System.out.println("\tMENÚ REGISTRO ATLETA");
			System.out.println("-----------------------------------");
			System.out.println("1. Mostrar BD Completa");
			System.out.println("2. Filtrar por Sexo");
			System.out.println("3. Filtrar por Categoría");
			System.out.println("4. Salir");
			System.out.print("Elija una opción (1-4): ");

			option = sc.nextInt();

			switch (option) {
			case 1:
				entity.getTransaction().begin();
				List<Atleta> listaAtletas = querySelect.getResultList();
				entity.getTransaction().commit();
				System.out.println("\n-----------------------------------");
				System.out.println("\tBD Atletas");
				System.out.println("-----------------------------------");
				for (Atleta atleta1 : listaAtletas) {
					System.out.print("\nId(" + atleta1.getId() + ")" + " Nombre=>" + atleta1.getFullName() + " Edad=>"
							+ atleta1.getAge() + " Género=>" + atleta1.getGenre() + " Press Banca(kg)=>"
							+ atleta1.getBenchPress() + " Peso Muerto(kg)=>" + atleta1.getDeadlift()
							+ " Sentadillas(Nº)=>" + atleta1.getSquat() + " TOTAL=>" + atleta1.getTotal() + "\n");
				}
				break;
			case 2:
				entity.getTransaction().begin();
				List<Atleta> listaAtletasHombres = querySelectHombre.getResultList();
				entity.getTransaction().commit();
				System.out.println("\n-----------------------------------");
				System.out.println("\tAtletas Masculinos");
				System.out.println("-----------------------------------");
				for (Atleta atleta1 : listaAtletasHombres) {
					System.out.print("\nId(" + atleta1.getId() + ")" + " Nombre=>" + atleta1.getFullName() + " Edad=>"
							+ atleta1.getAge() + " Género=>" + atleta1.getGenre() + " Press Banca(kg)=>"
							+ atleta1.getBenchPress() + " Peso Muerto(kg)=>" + atleta1.getDeadlift()
							+ " Sentadillas(Nº)=>" + atleta1.getSquat() + " TOTAL=>" + atleta1.getTotal() + "\n");
				}
				entity.getTransaction().begin();
				List<Atleta> listaAtletasMujeres = querySelectMujer.getResultList();
				entity.getTransaction().commit();
				System.out.println("\n-----------------------------------");
				System.out.println("\tAtletas Femeninas");
				System.out.println("-----------------------------------");
				for (Atleta atleta1 : listaAtletasMujeres) {
					System.out.print("\nId(" + atleta1.getId() + ")" + " Nombre=>" + atleta1.getFullName() + " Edad=>"
							+ atleta1.getAge() + " Género=>" + atleta1.getGenre() + " Press Banca(kg)=>"
							+ atleta1.getBenchPress() + " Peso Muerto(kg)=>" + atleta1.getDeadlift()
							+ " Sentadillas(Nº)=>" + atleta1.getSquat() + " TOTAL=>" + atleta1.getTotal() + "\n");
				}
				break;
			case 3:
				entity.getTransaction().begin();
				List<Atleta> listaAtletasMaster = querySelectMaster.getResultList();
				entity.getTransaction().commit();
				System.out.println("\n-----------------------------------");
				System.out.println("\tAtletas Master");
				System.out.println("-----------------------------------");
				for (Atleta atleta1 : listaAtletasMaster) {
					System.out.print("\nId(" + atleta1.getId() + ")" + " Nombre=>" + atleta1.getFullName() + " Edad=>"
							+ atleta1.getAge() + " Género=>" + atleta1.getGenre() + " Press Banca(kg)=>"
							+ atleta1.getBenchPress() + " Peso Muerto(kg)=>" + atleta1.getDeadlift()
							+ " Sentadillas(Nº)=>" + atleta1.getSquat() + " TOTAL=>" + atleta1.getTotal() + "\n");
				}
				entity.getTransaction().begin();
				List<Atleta> listaAtletasJuveniles = querySelectJuveniles.getResultList();
				entity.getTransaction().commit();
				System.out.println("\n-----------------------------------");
				System.out.println("\tAtletas Juveniles");
				System.out.println("-----------------------------------");
				for (Atleta atleta1 : listaAtletasJuveniles) {
					System.out.print("\nId(" + atleta1.getId() + ")" + " Nombre=>" + atleta1.getFullName() + " Edad=>"
							+ atleta1.getAge() + " Género=>" + atleta1.getGenre() + " Press Banca(kg)=>"
							+ atleta1.getBenchPress() + " Peso Muerto(kg)=>" + atleta1.getDeadlift()
							+ " Sentadillas(Nº)=>" + atleta1.getSquat() + " TOTAL=>" + atleta1.getTotal() + "\n");
				}
				break;
			default:
				System.out.println("Opción inválida. Inténtalo de nuevo.");
			}
		} while (option != 4);

	}

	private static void actualizarAtleta(int id) {
		entity.getTransaction().begin();
		Atleta atleta = entity.find(Atleta.class, id);

		System.out.println("-----------------------------------");
		System.out.println("\tIntroduce los Nuevos Datos");
		System.out.println("-----------------------------------");
		System.out.print("Nombre Completo: ");
		String fullName = sc.next();
		System.out.print("Edad: ");
		int age = Integer.parseInt(sc.next());
		System.out.print("Género: ");
		String genre = sc.next();
		System.out.print("Press Banca(kg): ");
		int benchPress = Integer.parseInt(sc.next());
		System.out.print("Peso Muerto(kg): ");
		int deadlift = Integer.parseInt(sc.next());
		System.out.print("Sentadillas(Nº): ");
		int squat = Integer.parseInt(sc.next());
		int total = calcularTotal(benchPress, deadlift, squat);

		atleta.setFullName(fullName);
		atleta.setAge(age);
		atleta.setGenre(genre);
		atleta.setBenchPress(benchPress);
		atleta.setDeadlift(deadlift);
		atleta.setSquat(squat);
		atleta.setTotal(total);
		atleta.setFullName(fullName);

		entity.merge(atleta);
		entity.getTransaction().commit();
		System.out.println("\nAtleta id(" + id + ") actualizado.\n");
	}

	private static void borrarAtleta(int id) {
		Atleta atleta = entity.find(Atleta.class, id);
		entity.getTransaction().begin();
		entity.remove(atleta);
		entity.getTransaction().commit();
		System.out.println("\nAtleta id(" + id + ") eliminado.\n");
	}

	public static int calcularTotal(int benchPress, int deadlift, int squat) {
		int total = benchPress + deadlift + squat;
		return total;
	}

	public static void insertarAtleta(Atleta atleta) {
		entity.getTransaction().begin();
		entity.persist(atleta);
		entity.getTransaction().commit();

		System.out.println("Atleta insertado con éxito");

	}

	public static boolean buscarAtleta(int id) {
		Atleta atleta = entity.find(Atleta.class, id);

		if (atleta != null) {
			System.out.println("\nAtleta id(" + id + "): " + atleta.getFullName() + "\nEdad: " + atleta.getAge()
					+ "\nGénero: " + atleta.getGenre() + "\nPress Banca(kg): " + atleta.getBenchPress()
					+ "\nPeso Muerto(kg): " + atleta.getDeadlift() + "\nSentadillas(Nº): " + atleta.getSquat()
					+ "\nTotal: " + atleta.getTotal() + "\n");
			return true;
		} else {
			System.out.println("\nAtleta id(" + id + ") NO ENCONTRADO EN LA BD.\n");
			return false;
		}

	}
}
