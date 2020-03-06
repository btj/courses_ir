package courses_ir;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @invar | getUsername() != null
 * @invar | getCourses() != null
 * @invar The bidirectional association between students and courses is consistent.
 *    | getCourses().stream().allMatch(c -> c.getStudents().contains(this))
 */
public class Student {
	
	/**
	 * @invar | getPeerGroup(this) != null
	 */
	String username;
	Map<String, Course> courses =
			new HashMap<String, Course>();
	
	public String getUsername() { return username; }
	
	public Set<Course> getCourses() {
		return Set.copyOf(courses.values());
	}
	
	public Course getCourse(String name) {
		return courses.get(name);
//		for (Course course : courses)
//			if (course.name.equals(name))
//				return course;
//		return null;
	}
	
	public Student(String username) {
		this.username = username;
	}
	
	/**
	 * 
	 */
	public int getNbStudentsWithSameProgram() {
		
	}

}
