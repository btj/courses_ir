package courses_ir;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import logicalcollections.LogicalMap;

public class Student {
	
	/**
	 * Returns this student's state as a map from property names to property values.
	 * 
	 * @post | result != null
	 * @post | result.equals(Map.of("username", getUsernameInternal(), "courses", Set.copyOf(getCoursesInternal().values())))
	 */
	Map<String, Object> getStateInternal() {
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
		return getStateInternal();
	}
	
	/**
	 * Returns a map that maps each course and each student related directly or
	 * indirectly to this student to its current state, represented as a map from
	 * property names to property values.
	 * 
	 * @post | result != null
	 * @post | result.equals(Course.getPeerGroupState(null, this))
	 * @peerObjects | result.keySet()
	 */
	public Map<Object, Map<String, Object>> getPeerGroupState() {
		return Course.getPeerGroupStateInternal(null, this);
	}
	
	/**
	 * @invar | username != null
	 * @invar | courses != null
	 * @invar | !courses.keySet().contains(null)
	 * @invar | !courses.values().contains(null)
	 */
	private String username;
	/** @representationObject */
	private Map<String, Course> courses =
			new HashMap<String, Course>();
	
	/**
	 * @invar | Course.peerGroupState(null, this) != null
	 * 
	 * @post | result != null
	 */
	String getUsernameInternal() { return username; }
	
	/**
	 * @peerObjects | result.values()
	 * 
	 * @creates | result
	 * @post | result != null
	 * @post | result.keySet().stream().allMatch(k -> k != null)
	 * @post | result.values().stream().allMatch(c -> c != null)
	 */
	Map<String, Course> getCoursesInternal() {
		return Map.copyOf(courses);
	}
	
	/**
	 * Registers the given course as one of the courses taken by this
	 * student.
	 * 
	 * @pre | course != null
	 * @inspects | this, course
	 * @post | getCoursesInternal().get(course.getNameInternal()) == course
	 * @post | LogicalMap.equalsExcept(getCoursesInternal(), old(getCoursesInternal()), course.getNameInternal())
	 */
	void putCourseInternal(Course course) {
		courses.put(course.getNameInternal(), course);
	}
	
	/** @post | result != null */
	public String getUsername() { return username; }
	
	/**
	 * @post | result != null
	 * @post | result.stream().allMatch(c -> c != null)
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
		for (Course course : this.courses.values()) {
			for (Student student : course.getStudents())
				if (student.courses.equals(courses))
					size++;
			break;
		}
		return size;
	}

}
