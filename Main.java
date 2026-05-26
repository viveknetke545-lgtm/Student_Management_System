import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static StudentManager manager;

    public static void main(String[] args) {
        printBanner();
        manager = new StudentManager();
        mainMenu();
        System.out.println("\n[✓] Thank you for using Student Management System. Goodbye!");
        sc.close();
    }

    // ─── MENUS ──────────────────────────────────────────────────────────────
    private static void mainMenu() {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║       STUDENT MANAGEMENT SYSTEM      ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Add Student                      ║");
            System.out.println("║  2. Edit Student                     ║");
            System.out.println("║  3. Delete Student                   ║");
            System.out.println("║  4. Search Student                   ║");
            System.out.println("║  5. Display All Students             ║");
            System.out.println("║  6. Grade / Marks Management         ║");
            System.out.println("║  7. Class Statistics                 ║");
            System.out.println("║  8. Export Report to File            ║");
            System.out.println("║  0. Exit                             ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("  Enter choice: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> addStudentMenu();
                case 2 -> editStudentMenu();
                case 3 -> deleteStudentMenu();
                case 4 -> searchMenu();
                case 5 -> manager.displayAll();
                case 6 -> gradesMenu();
                case 7 -> manager.displayStats();
                case 8 -> manager.exportReport();
                case 0 -> { return; }
                default -> System.out.println("[!] Invalid option. Please try again.");
            }
        }
    }

    // ─── ADD ────────────────────────────────────────────────────────────────
    private static void addStudentMenu() {
        System.out.println("\n--- Add New Student ---");
        System.out.print("  Name   : ");
        String name = readString();
        if (name.isBlank()) { System.out.println("[✗] Name cannot be empty."); return; }

        System.out.print("  Age    : ");
        int age = readInt();
        if (age < 1 || age > 120) { System.out.println("[✗] Invalid age."); return; }

        System.out.print("  Course : ");
        String course = readString();
        if (course.isBlank()) { System.out.println("[✗] Course cannot be empty."); return; }

        System.out.print("  Email  : ");
        String email = readString();
        if (!email.contains("@")) { System.out.println("[✗] Invalid email address."); return; }

        manager.addStudent(name, age, course, email);
    }

    // ─── EDIT ───────────────────────────────────────────────────────────────
    private static void editStudentMenu() {
        System.out.println("\n--- Edit Student ---");
        System.out.print("  Enter Student ID to edit: ");
        int id = readInt();

        System.out.print("  New Name   : ");
        String name = readString();
        System.out.print("  New Age    : ");
        int age = readInt();
        System.out.print("  New Course : ");
        String course = readString();
        System.out.print("  New Email  : ");
        String email = readString();

        manager.editStudent(id, name, age, course, email);
    }

    // ─── DELETE ─────────────────────────────────────────────────────────────
    private static void deleteStudentMenu() {
        System.out.println("\n--- Delete Student ---");
        System.out.print("  Enter Student ID to delete: ");
        int id = readInt();
        System.out.print("  Are you sure? (yes/no): ");
        String confirm = readString();
        if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
            manager.deleteStudent(id);
        } else {
            System.out.println("[i] Deletion cancelled.");
        }
    }

    // ─── SEARCH ─────────────────────────────────────────────────────────────
    private static void searchMenu() {
        System.out.println("\n--- Search Student ---");
        System.out.println("  1. Search by ID");
        System.out.println("  2. Search by Name");
        System.out.println("  3. Search by Course");
        System.out.print("  Enter choice: ");
        int choice = readInt();
        switch (choice) {
            case 1 -> {
                System.out.print("  Enter ID: ");
                manager.searchById(readInt());
            }
            case 2 -> {
                System.out.print("  Enter name keyword: ");
                manager.searchByName(readString());
            }
            case 3 -> {
                System.out.print("  Enter course keyword: ");
                manager.searchByCourse(readString());
            }
            default -> System.out.println("[!] Invalid option.");
        }
    }

    // ─── GRADES ─────────────────────────────────────────────────────────────
    private static void gradesMenu() {
        System.out.println("\n--- Grade / Marks Management ---");
        System.out.println("  1. Add/Update Subject Grade");
        System.out.println("  2. View Student Grades");
        System.out.print("  Enter choice: ");
        int choice = readInt();

        System.out.print("  Enter Student ID: ");
        int id = readInt();

        switch (choice) {
            case 1 -> {
                System.out.print("  Subject : ");
                String subject = readString();
                System.out.print("  Marks (0-100): ");
                double marks = readDouble();
                manager.addGrade(id, subject, marks);
            }
            case 2 -> manager.displayGrades(id);
            default -> System.out.println("[!] Invalid option.");
        }
    }

    // ─── UTILITIES ──────────────────────────────────────────────────────────
    private static int readInt() {
        try {
            int val = sc.nextInt();
            sc.nextLine();
            return val;
        } catch (InputMismatchException e) {
            sc.nextLine();
            return -1;
        }
    }

    private static double readDouble() {
        try {
            double val = sc.nextDouble();
            sc.nextLine();
            return val;
        } catch (InputMismatchException e) {
            sc.nextLine();
            return -1;
        }
    }

    private static String readString() {
        return sc.nextLine().trim();
    }

    private static void printBanner() {
        System.out.println("""
                ╔════════════════════════════════════════════════════╗
                ║                                                    ║
                ║      STUDENT MANAGEMENT SYSTEM  v1.0               ║
                ║      File Handling Project  |  Java                ║
                ║                                                    ║
                ╚════════════════════════════════════════════════════╝
                """);
    }
}
