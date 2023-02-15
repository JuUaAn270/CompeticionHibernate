package model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;

@Entity
@Table(name="Competicion")
public class Atleta {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int Id;
	@Column
	private String FullName;
	@Column
	private int Age;
	@Column
	private String Genre;
	@Column
	private int BenchPress;
	@Column
	private int Deadlift;
	@Column
	private int Squat;
	@Column
	private int Total;
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getFullName() {
		return FullName;
	}
	public void setFullName(String fullName) {
		FullName = fullName;
	}
	public int getAge() {
		return Age;
	}
	public void setAge(int age) {
		Age = age;
	}
	public String getGenre() {
		return Genre;
	}
	public void setGenre(String genre) {
		Genre = genre;
	}
	public int getBenchPress() {
		return BenchPress;
	}
	public void setBenchPress(int benchPress) {
		BenchPress = benchPress;
	}
	public int getDeadlift() {
		return Deadlift;
	}
	public void setDeadlift(int deadlift) {
		Deadlift = deadlift;
	}
	public int getSquat() {
		return Squat;
	}
	public void setSquat(int squat) {
		Squat = squat;
	}
	public int getTotal() {
		return Total;
	}
	public void setTotal(int total) {
		Total = total;
	}

}