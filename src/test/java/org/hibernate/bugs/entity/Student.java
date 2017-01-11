package org.hibernate.bugs.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@NamedEntityGraph(
	name = "Student.Course",
	attributeNodes = {
		@NamedAttributeNode("courses")
	}
)
public class Student {
	@Id
	@GeneratedValue
	private int id;
	
	@ManyToMany
	@JoinTable(
		name="STUDENT_COURSES",
		joinColumns=@JoinColumn(referencedColumnName="ID", name="STUDENT_ID"),
		inverseJoinColumns=@JoinColumn(referencedColumnName="ID", name="COURSE_ID"),
		uniqueConstraints={@UniqueConstraint(columnNames={"STUDENT_ID", "COURSE_ID"})}
	)
	private Set<Course> courses;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}
}
