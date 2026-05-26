import java.util.*;
import java.util.stream.Collectors;

public class StudentManager {
    private List<Student> students;
    private int nextId;

    public StudentManager() {
        students = FileHandler.loadStudents();
        nextId = students.stream().mapToInt(Student::getId).max().orElse(0) + 1;
    }

    // ─── ADD ────────────────────────────────────────────────────────────────
    public void addStudent(String name, int age, String course, String email) {
        if (emailExists(email)) {
            System.out.println("[✗] A student with this email already exists.");
            return;
        }
        Student s = new Student(nextId++, name, age, course, email);
        students.add(s);
        FileHandler.saveStudents(students);
        System.out.println("[✓] Student added successfully! ID: " + s.getId());
    }

    // ─── EDIT ───────────────────────────────────────────────────────────────
    public boolean editStudent(int id, String name, int age, String course, String email) {
        Student s = findById(id);
        if (s == null) {
            System.out.println("[✗] Student with ID " + id + " not found.");
            return false;
        }
        if (!s.getEmail().equalsIgnoreCase(email) && emailExists(email)) {
            System.out.println("[✗] Another student already uses this email.");
            return false;
        }
        s.setName(name);
        s.setAge(age);
        s.setCourse(course);
        s.setEmail(email);
        FileHandler.saveStudents(students);
        System.out.println("[✓] Student ID " + id + " updated successfully.");
        return true;
    }

    // ─── DELETE ─────────────────────────────────────────────────────────────
    public boolean deleteStudent(int id) {
        Student s = findById(id);
        if (s == null) {
            System.out.println("[✗] Student with ID " + id + " not found.");
            return false;
        }
        students.remove(s);
        FileHandler.saveStudents(students);
        System.out.println("[✓] Student ID " + id + " deleted successfully.");
        return true;
    }

    // ─── GRADES ─────────────────────────────────────────────────────────────
    public boolean addGrade(int id, String subject, double marks) {
        Student s = findById(id);
        if (s == null) {
            System.out.println("[✗] Student with ID " + id + " not found.");
            return false;
        }
        if (marks < 0 || marks > 100) {
            System.out.println("[✗] Marks must be between 0 and 100.");
            return false;
        }
        s.addGrade(subject, marks);
        FileHandler.saveStudents(students);
        System.out.printf("[✓] Grade added for %s: %s = %.2f%n", s.getName(), subject, marks);
        return true;
    }

    public void displayGrades(int id) {
        Student s = findById(id);
        if (s == null) {
            System.out.println("[✗] Student with ID " + id + " not found.");
            return;
        }
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.printf("│  Grades for: %-28s│%n", s.getName());
        System.out.println("├──────────────────────────┬──────────────┤");
        System.out.println("│ Subject                  │ Marks        │");
        System.out.println("├──────────────────────────┼──────────────┤");
        if (s.getGrades().isEmpty()) {
            System.out.println("│     No grades assigned yet             │");
        } else {
            for (Map.Entry<String, Double> entry : s.getGrades().entrySet()) {
                System.out.printf("│ %-24s │ %-12.2f │%n", entry.getKey(), entry.getValue());
            }
            System.out.println("├──────────────────────────┼──────────────┤");
            System.out.printf("│ %-24s │ %-12.2f │%n", "Average", s.calculateAverage());
            System.out.printf("│ %-24s │ %-12s │%n", "Final Grade", s.getGrade());
        }
        System.out.println("└──────────────────────────┴──────────────┘");
    }

    // ─── DISPLAY ALL ────────────────────────────────────────────────────────
    public void displayAll() {
        if (students.isEmpty()) {
            System.out.println("[i] No student records found.");
            return;
        }
        System.out.println("\n" + "═".repeat(110));
        System.out.printf("%-6s %-22s %-5s %-17s %-27s %-8s %-6s%n",
                "ID", "Name", "Age", "Course", "Email", "Avg", "Grade");
        System.out.println("═".repeat(110));
        for (Student s : students) {
            System.out.printf("%-6d %-22s %-5d %-17s %-27s %-8.2f %-6s%n",
                    s.getId(), s.getName(), s.getAge(), s.getCourse(),
                    s.getEmail(), s.calculateAverage(),
                    s.getGrades().isEmpty() ? "N/A" : s.getGrade());
        }
        System.out.println("═".repeat(110));
        System.out.println("Total: " + students.size() + " student(s)");
    }

    // ─── SEARCH ─────────────────────────────────────────────────────────────
    public void searchByName(String keyword) {
        List<Student> results = students.stream()
                .filter(s -> s.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
        printSearchResults(results, "name containing \"" + keyword + "\"");
    }

    public void searchById(int id) {
        Student s = findById(id);
        if (s == null) {
            System.out.println("[✗] No student found with ID: " + id);
        } else {
            System.out.println("\n[✓] Student Found:");
            System.out.println(s);
            displayGrades(id);
        }
    }

    public void searchByCourse(String course) {
        List<Student> results = students.stream()
                .filter(s -> s.getCourse().toLowerCase().contains(course.toLowerCase()))
                .collect(Collectors.toList());
        printSearchResults(results, "course containing \"" + course + "\"");
    }

    // ─── STATS ──────────────────────────────────────────────────────────────
    public void displayStats() {
        if (students.isEmpty()) {
            System.out.println("[i] No data available for statistics.");
            return;
        }
        OptionalDouble avg = students.stream()
                .filter(s -> !s.getGrades().isEmpty())
                .mapToDouble(Student::calculateAverage)
                .average();

        Student top = students.stream()
                .filter(s -> !s.getGrades().isEmpty())
                .max(Comparator.comparingDouble(Student::calculateAverage))
                .orElse(null);

        Student lowest = students.stream()
                .filter(s -> !s.getGrades().isEmpty())
                .min(Comparator.comparingDouble(Student::calculateAverage))
                .orElse(null);

        Map<String, Long> courseCount = students.stream()
                .collect(Collectors.groupingBy(Student::getCourse, Collectors.counting()));

        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║           CLASS STATISTICS               ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.printf("║  Total Students     : %-19d║%n", students.size());
        System.out.printf("║  Class Average      : %-19s║%n",
                avg.isPresent() ? String.format("%.2f", avg.getAsDouble()) : "N/A");
        System.out.printf("║  Top Performer      : %-19s║%n",
                top != null ? top.getName() + " (" + String.format("%.1f", top.calculateAverage()) + ")" : "N/A");
        System.out.printf("║  Needs Improvement  : %-19s║%n",
                lowest != null ? lowest.getName() + " (" + String.format("%.1f", lowest.calculateAverage()) + ")" : "N/A");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  Students per Course:                    ║");
        for (Map.Entry<String, Long> e : courseCount.entrySet()) {
            System.out.printf("║    %-20s : %-14d║%n", e.getKey(), e.getValue());
        }
        System.out.println("╚══════════════════════════════════════════╝");
    }

    // ─── HELPERS ────────────────────────────────────────────────────────────
    private Student findById(int id) {
        return students.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
    }

    private boolean emailExists(String email) {
        return students.stream().anyMatch(s -> s.getEmail().equalsIgnoreCase(email));
    }

    private void printSearchResults(List<Student> results, String criteria) {
        if (results.isEmpty()) {
            System.out.println("[i] No student found with " + criteria + ".");
        } else {
            System.out.println("\n[✓] Found " + results.size() + " result(s) for " + criteria + ":");
            System.out.println("─".repeat(110));
            for (Student s : results) System.out.println(s);
            System.out.println("─".repeat(110));
        }
    }

    public void exportReport() {
        FileHandler.exportToTextFile(students);
    }
}
