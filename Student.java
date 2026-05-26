import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private int age;
    private String course;
    private String email;
    private Map<String, Double> grades; // Subject -> Marks

    public Student(int id, String name, int age, String course, String email) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.course = course;
        this.email = email;
        this.grades = new HashMap<>();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Map<String, Double> getGrades() { return grades; }
    public void setGrades(Map<String, Double> grades) { this.grades = grades; }

    public void addGrade(String subject, double marks) {
        grades.put(subject, marks);
    }

    public double calculateAverage() {
        if (grades.isEmpty()) return 0.0;
        double total = 0;
        for (double marks : grades.values()) {
            total += marks;
        }
        return total / grades.size();
    }

    public String getGrade() {
        double avg = calculateAverage();
        if (avg >= 90) return "A+";
        else if (avg >= 80) return "A";
        else if (avg >= 70) return "B";
        else if (avg >= 60) return "C";
        else if (avg >= 50) return "D";
        else return "F";
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Name: %-20s | Age: %2d | Course: %-15s | Email: %-25s | Avg: %.2f | Grade: %s",
                id, name, age, course, email, calculateAverage(), grades.isEmpty() ? "N/A" : getGrade());
    }
}
