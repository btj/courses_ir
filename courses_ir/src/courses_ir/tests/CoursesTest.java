package courses_ir.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;

import courses_ir.Course;
import courses_ir.Student;

class CoursesTest {

	@Test
	void test() {
		Course logic = new Course("Logic");
		Course math = new Course("Math");
		Student student1 = new Student("r0001");
		Student student2 = new Student("r0002");
		logic.enroll(student1);
		logic.enroll(student2);
		math.enroll(student1);
		
		assertEquals(Set.of(student1, student2), logic.getStudents());
		assertEquals(Set.of(student1), math.getStudents());
		assertEquals(Set.of(logic, math), student1.getCourses());
		assertEquals(Set.of(logic), student2.getCourses());
		
		assertEquals(logic.getStudent("r0001"), student1);
		assertEquals(student1.getCourse("Logic"), logic);
	}

}
