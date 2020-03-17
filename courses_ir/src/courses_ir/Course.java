package courses_ir;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import logicalcollections.LogicalList;
import logicalcollections.LogicalMap;
import logicalcollections.LogicalSet;

/**
 * @invar | getPeerGroupState() != null
 */
public class Course {

	/**
	 * Returns this course's state as a map from property names to property values.
	 * 
	 * @post | result != null
	 * @post | result.equals(Map.of("name", getNameInternal(), "students", Set.copyOf(getStudentsInternal().values())))
	 */
	Map<String, Object> getStateInternal() {
		return Map.of(
				"name", getNameInternal(),
				"students", Set.copyOf(getStudentsInternal().values()));
	}
	
	/**
	 * Returns this course's state as a map from property names to property values.
	 * 
	 * @post | result != null
	 * @post | result.equals(Map.of("name", getName(), "students", getStudents()))
	 */
	public Map<String, Object> getState() {
		return getStateInternal();
	}
	
	/**
	 * @inspects | this, *FSC4J.getPackageLevelPeerGroup(this)
	 * @post
	 *    | result.equals(LogicalMap.matching(map ->
     *    |     LogicalSet.<Course>matching(courses ->
	 *    |         LogicalSet.<Student>matching(students ->
	 *    |         	(course0 == null || courses.contains(course0)) &&
	 *    |             (student0 == null || students.contains(student0)) &&
     *    |             courses.allMatch(course ->
	 *    |    		        course.getStudentsInternal().values().stream().allMatch(s -> students.contains(s))
	 *    |    	        ) &&
	 *    |    	        students.allMatch(student ->
	 *    |     		    student.getCoursesInternal().values().stream().allMatch(c -> courses.contains(c))
     *    |     	    ) &&
	 *    |             courses.allMatch(course ->
     *    |                 course.getStudentsInternal().keySet().stream().allMatch(username ->
     *    |                     course.getStudentsInternal().get(username).getUsernameInternal().equals(username) &&
     *    |                     course.getStudentsInternal().get(username).getCoursesInternal().get(course.getNameInternal()) == course
     *    |                 ) &&
     *    |                 map.containsEntry(course, course.getStateInternal())
     *    |             ) &&
     *    |             students.allMatch(student ->
     *    |                 student.getCoursesInternal().keySet().stream().allMatch(name ->
     *    |                     student.getCoursesInternal().get(name).getNameInternal().equals(name) &&
     *    |                     student.getCoursesInternal().get(name).getStudentsInternal().get(student.getUsernameInternal()) == student
     *    |                 ) &&
     *    |                 map.containsEntry(student, student.getStateInternal())
     *    |             )
     *    |         ) != null
     *    |     ) != null
     *    | ))
	 */
	static Map<Object, Map<String, Object>> getPeerGroupStateInternal(Course course0, Student student0) {
		return LogicalMap.matching(map ->
			LogicalSet.<Course>matching(courses ->
			    LogicalSet.<Student>matching(students ->
			    	(course0 == null || courses.contains(course0)) &&
			    	(student0 == null || students.contains(student0)) &&
			    	courses.allMatch(course ->
			    		course.students.values().stream().allMatch(s -> students.contains(s))
			    	) &&
			    	students.allMatch(student ->
			    		student.getCoursesInternal().values().stream().allMatch(c -> courses.contains(c))
			    	) &&
			    	courses.allMatch(course ->
			    	    course.students.keySet().stream().allMatch(username ->
			    	        course.students.get(username).getUsernameInternal().equals(username) &&
			    	        course.students.get(username).getCoursesInternal().get(course.name) == course
			    	    ) &&
			    	    map.containsEntry(course, course.getStateInternal())
			    	) &&
			    	students.allMatch(student ->
			    		student.getCoursesInternal().keySet().stream().allMatch(name ->
			    			student.getCoursesInternal().get(name).name.equals(name) &&
			    			student.getCoursesInternal().get(name).students.get(student.getUsernameInternal()) == student
			    		) &&
			    		map.containsEntry(student, student.getStateInternal())
			    	)
			    ) != null
			) != null
		);
	}
	
	/**
	 * Returns a map that maps each course and each student related directly or
	 * indirectly to the given course or the given student to its current state,
	 * represented as a map from property names to property values.
	 * 
	 * @post | result != null
	 * @post
	 *    | result.equals(LogicalMap.matching(map ->
	 *    |     LogicalSet.<Course>matching(courses ->
	 *    |         LogicalSet.<Student>matching(students ->
	 *    |
	 *    |             // The given course, if any, is related to itself.
	 *    |             (course0 == null || courses.contains(course0)) &&
	 *    |
	 *    |             // The given student, if any, is related to itself.
	 *    |             (student0 == null || students.contains(student0)) &&
	 *    |
	 *    |             courses.allMatch(course ->
	 *    |
	 *    |                 // Each related course's students have distinct usernames.
	 *    |                 LogicalList.distinct(
	 *    |                     course.getStudents().stream()
	 *    |                         .map(s -> s.getUsername())
	 *    |                         .collect(Collectors.toList())) &&
	 *    |
	 *    |                 // Each related course's students are related.
	 *    |                 students.containsAll(course.getStudents())
	 *    |
	 *    |             ) &&
	 *    |             students.allMatch(student ->
	 *    |
	 *    |                 // Each related student's courses have distinct names.
	 *    |                 LogicalList.distinct(
	 *    |                     student.getCourses().stream()
	 *    |                         .map(c -> c.getName())
	 *    |                         .collect(Collectors.toList())) &&
	 *    |
	 *    |                 // Each related student's courses are related.
	 *    |                 courses.containsAll(student.getCourses())
	 *    |
	 *    |             ) &&
	 *    |             courses.allMatch(course ->
	 *    |
	 *    |                 // Each related course C's students have C in their program. 
	 *    |                 course.getStudents().stream().allMatch(student ->
	 *    |                     student.getCourses().contains(course)) &&
	 *    |
	 *    |                 map.containsEntry(course, course.getState())
	 *    |
	 *    |             ) &&
	 *    |             students.allMatch(student ->
	 *    |
	 *    |                 // Each related student S's courses have S among their students.
	 *    |                 student.getCourses().stream().allMatch(course ->
	 *    |                     course.getStudents().contains(student)) &&
	 *    |
	 *    |                 map.containsEntry(student, student.getState())
	 *    |             )
	 *    |         ) != null
	 *    |     ) != null
	 *    | ))
	 */
	public static Map<Object, Map<String, Object>> getPeerGroupState(Course course0, Student student0) {
		return getPeerGroupStateInternal(course0, student0);
	}

	/**
	 * Returns a map that maps each course and each student related directly or
	 * indirectly to this course to its current state, represented as a map from
	 * property names to property values.
	 * 
	 * @post | result != null
	 * @post | result.equals(getPeerGroupState(this, null))
	 * @peerObjects | result.keySet()
	 */
	public Map<Object, Map<String, Object>> getPeerGroupState() {
		return getPeerGroupStateInternal(this, null);
	}

	/**
	 * @invar | name != null
	 * @invar | students != null
	 * @invar | !students.keySet().contains(null)
	 * @invar | !students.values().contains(null)
	 */
	private String name;
	/** @representationObject */
	private Map<String, Student> students = new HashMap<String, Student>();
	
	/**
	 * @invar | getPeerGroupStateInternal(this, null) != null
	 * 
	 * @post | result != null
	 */
	String getNameInternal() { return name; }
	
	/**
	 * @post | result != null
	 * @post | result.keySet().stream().allMatch(k -> k != null)
	 * @post | result.values().stream().allMatch(v -> v != null)
	 * 
	 * @peerObjects | result.values()
	 */
	Map<String, Student> getStudentsInternal() {
		return Map.copyOf(students);
	}
	
	/**
	 * Registers the given student as one of the students of this course.
	 * 
	 * @pre | student != null
	 * @inspects | this, student
	 * @post | getStudentsInternal().get(student.getUsername()) == student
	 * @post | LogicalMap.equalsExcept(getStudentsInternal(), old(getStudentsInternal()), student.getUsername())
	 */
	void putStudentInternal(Student student) {
		students.put(student.getUsernameInternal(), student);
	}

	/** @post | result != null */
	public String getName() { return name; }
	
	/**
	 * @post | result != null
	 * @post | result.stream().allMatch(s -> s != null)
	 * @creates | result
	 */
	public Set<Student> getStudents() {
		return Set.copyOf(getStudentsInternal().values());
	}
	
	/**
	 * Returns the student that takes this course and whose username is the given username,
	 * or null if there is no such student.
	 * 
	 * @pre | username != null
	 * @post
	 *    | getStudents().stream().anyMatch(s -> s.getUsername().equals(username)) ?
	 *    |     result != null && getStudents().contains(result) &&
	 *    |     result.getUsername().equals(username)
	 *    | :
	 *    |     result == null   
	 */
	public Student getStudent(String username) {
		return students.get(username);
	}
	
	/**
	 * Initializes this course with the given name and an empty set of students.
	 * 
	 * @pre | name != null
	 * @post This course's name equals the given name.
	 *    | getName().equals(name)
	 * @post This course's set of students is empty.
	 *    | getStudents().isEmpty()
	 */
	public Course(String name) {
		this.name = name;
	}
	
	/**
	 * Enrolls the given student into this course.
	 * 
	 * @pre | student != null
	 * @pre If a student with the same username is already in this course, it is the given student.
	 *    | getStudent(student.getUsername()) == null || getStudent(student.getUsername()) == student
	 * @pre If a course with the same name is already in the given student's program, it is this course.
	 *    | student.getCourse(getName()) == null || student.getCourse(getName()) == this
	 * 
	 * @mutates | this, student
	 * 
	 * @post This course's set of students equals its old set of students plus the given student.
	 *    | getStudents().equals(LogicalSet.plus(old(getStudents()), student))
	 * @post This course's name has remained unchanged.
	 *    | getName().equals(old(getName()))
	 * @post The given student's set of courses equals its old set of courses plus this course.
	 *    | student.getCourses().equals(LogicalSet.plus(old(student.getCourses()), this))
	 * @post The given student's username has remained unchanged.
	 *    | student.getUsername().equals(old(student.getUsername()))
	 * @post All existing objects in this course's peer group, except for this course and the given student,
	 *       have remained unchanged.
	 *    | LogicalMap.extendsExcept(getPeerGroupState(), old(getPeerGroupState()), this, student)
	 * @post All existing objects in the given student's peer group, except for this course and the given student,
	 *       have remained unchanged.
	 *    | LogicalMap.extendsExcept(student.getPeerGroupState(), old(student.getPeerGroupState()), this, student)
	 */
	public void enroll(Student student) {
		students.put(student.getUsernameInternal(), student);
		student.putCourseInternal(this);
	}

}
