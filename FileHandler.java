import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String DATA_FILE = "students.dat";
    private static final String BACKUP_FILE = "students_backup.dat";

    // Save all students to binary file
    public static void saveStudents(List<Student> students) {
        // Backup existing data before saving
        backupData();
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(DATA_FILE)))) {
            oos.writeObject(students);
            System.out.println("[✓] Data saved successfully to " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("[✗] Error saving data: " + e.getMessage());
        }
    }

    // Load all students from binary file
    @SuppressWarnings("unchecked")
    public static List<Student> loadStudents() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("[i] No existing data file found. Starting fresh.");
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(DATA_FILE)))) {
            List<Student> students = (List<Student>) ois.readObject();
            System.out.println("[✓] Loaded " + students.size() + " student(s) from " + DATA_FILE);
            return students;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[✗] Error loading data: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Export all students to a readable text report
    public static void exportToTextFile(List<Student> students) {
        String reportFile = "students_report.txt";
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(reportFile)))) {
            writer.println("============================================================");
            writer.println("          STUDENT MANAGEMENT SYSTEM - FULL REPORT          ");
            writer.println("============================================================");
            writer.printf("  Generated: %s%n", new java.util.Date());
            writer.printf("  Total Students: %d%n", students.size());
            writer.println("============================================================");
            writer.println();

            if (students.isEmpty()) {
                writer.println("  No student records found.");
            } else {
                for (Student s : students) {
                    writer.println("------------------------------------------------------------");
                    writer.printf("  ID      : %d%n", s.getId());
                    writer.printf("  Name    : %s%n", s.getName());
                    writer.printf("  Age     : %d%n", s.getAge());
                    writer.printf("  Course  : %s%n", s.getCourse());
                    writer.printf("  Email   : %s%n", s.getEmail());
                    if (!s.getGrades().isEmpty()) {
                        writer.println("  Grades  :");
                        for (java.util.Map.Entry<String, Double> entry : s.getGrades().entrySet()) {
                            writer.printf("    %-20s : %.2f%n", entry.getKey(), entry.getValue());
                        }
                        writer.printf("  Average : %.2f%n", s.calculateAverage());
                        writer.printf("  Grade   : %s%n", s.getGrade());
                    } else {
                        writer.println("  Grades  : No grades assigned yet.");
                    }
                }
                writer.println("------------------------------------------------------------");
            }

            writer.println();
            writer.println("============================================================");
            writer.println("                       END OF REPORT                       ");
            writer.println("============================================================");
            System.out.println("[✓] Report exported to: " + reportFile);
        } catch (IOException e) {
            System.out.println("[✗] Error exporting report: " + e.getMessage());
        }
    }

    // Backup current data
    private static void backupData() {
        File src = new File(DATA_FILE);
        if (src.exists()) {
            try (InputStream in = new FileInputStream(src);
                 OutputStream out = new FileOutputStream(BACKUP_FILE)) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
            } catch (IOException e) {
                // Silent backup failure
            }
        }
    }
}
