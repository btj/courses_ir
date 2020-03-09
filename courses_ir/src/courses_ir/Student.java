package courses_ir;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @invar | getPeerGroupState() != null
 */
public class Student {
	
	/**
	 * @pre | courses != null
	 */
	Map<String, Object> state() {
		return Map.of(
				"username", username,
				"courses", Set.copyOf(courses.values()));
	}
	
	/**
	 * Returns this student's state as a map from property names to property values.
	 * 
	 * @post | result != null
	 * @post | result.equals(Map.of("username", getUsername(), "courses", getCourses()))
	 */
	public Map<String, Object> getState() {
		return state();
	}
	
	/**
	 * Returns a map that maps each course and each student related directly or
	 * indirectly to this student to its current state, represented as a map from
	 * property names to property values.
	 * 
	 * @post | result != null
	 * @post | result.equals(Course.getPeerGroupState(null, this))
	 */
	public Map<Object, Map<String, Object>> getPeerGroupState() {
		return Course.peerGroupState(null, this);
	}
	
	/**
	 * @invar | Course.peerGroupState(null, this) != null
	 */
	String username;
	/**
	 * @representationObject
	 * @peerObjects
	 */
	Map<String, Course> courses =
			new HashMap<String, Course>();
	
	public String getUsername() { return username; }
	
	/**
	 * @creates | result
	 */
	public Set<Course> getCourses() {
		return Set.copyOf(courses.values());
	}
	
	/**
	 * Returns the course taken by this student whose name equals the given name,
	 * or {@code null} if there is no such course.
	 * 
	 * @pre | name != null
	 * @post
	 *    | getCourses().stream().anyMatch(c -> c.getName().equals(name)) ?
	 *    |     result != null && getCourses().contains(result) &&
	 *    |     result.getName().equals(name)
	 *    | :
	 *    |     result == null
	 */
	public Course getCourse(String name) {
		return courses.get(name);
//		for (Course course : courses)
//			if (course.name.equals(name))
//				return course;
//		return null;
	}
	
	/**
	 * Initializes this object with the given username and an empty set of courses.
	 * 
	 * @pre | username != null
	 * 
	 * @mutates | this
	 * 
	 * @post This object's username equals the given username
	 *    | getUsername().equals(username)
	 * @post This object's set of courses is empty
	 *    | getCourses().isEmpty()
	 */
	public Student(String username) {
		this.username = username;
	}
	
	/**
	 * @post | result == getPeerGroupState().keySet().stream().filter(object ->
	 *       |     object instanceof Student &&
	 *       |     ((Student)object).getCourses().equals(this.getCourses())
	 *       | ).count()
	 */
	public int getNbStudentsWithSameProgram() {
		int size = 0;
		//Set<Student> students = new HashSet<Student>();
		//courses.values().iterator().next();
		for (Course course : this.courses.values()) {
			for (Student student : course.getStudents())
				if (student.courses.equals(courses))
					size++;
			break;
		}
		return size;
	}

}
