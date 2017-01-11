package org.hibernate.bugs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.bugs.entity.Course;
import org.hibernate.bugs.entity.Student;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private static EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void hhh123Test() throws Exception {
		
		// Initialize DB
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		Course one = new Course();
		one.setName("One");
		Course two = new Course();
		two.setName("two");
		entityManager.persist(one);
		entityManager.persist(two);
		Student student = new Student();
		Set<Course> courses = new HashSet<Course>();
		courses.add(one);
		courses.add(two);
		student.setCourses(courses);
		entityManager.persist(student);
		entityManager.getTransaction().commit();
		entityManager.close();
		
		// Load student object so that cache gets populated
		entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		EntityGraph<?> graph = entityManager.getEntityGraph("Student.Course");
		Map<String, Object> props = new HashMap<>();
		props.put("javax.persistence.fetchgraph", graph);		
		Student loadedStudent = entityManager.find(Student.class, student.getId(), props);
		loadedStudent.getCourses().size();
		entityManager.getTransaction().commit();
		entityManager.close();
		
		// Load same entity again, note that it is getting loaded from cache
		entityManager = entityManagerFactory.createEntityManager();
		EntityGraph<?> graph2 = entityManager.getEntityGraph("Student.Course");
		Map<String, Object> props2 = new HashMap<>();
		props2.put("javax.persistence.fetchgraph", graph2);		
		Student loadedStudent2 = entityManager.find(Student.class, student.getId(), props2);
		// Everything ok if
		// loadedStudent2.getCourses().size();
		entityManager.close();
		
		
		
		assert(loadedStudent2.getCourses().containsAll(courses));
	}
}
