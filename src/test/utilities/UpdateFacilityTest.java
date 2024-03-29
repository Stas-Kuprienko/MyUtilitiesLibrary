package utilities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stas.utilities.UpdateFacility;

import java.time.LocalDate;

public class UpdateFacilityTest {

    private UpdateFacility<Person> updateFacility;
    private Person person;

    @BeforeEach
    public void setUp() {
        updateFacility = new UpdateFacility<>();
        person = new Person("John", 25);
    }

    @Test
    public void testInit() throws ReflectiveOperationException {
        updateFacility.init(person, "name");
        Assertions.assertEquals("John", person.getName());
    }

    @Test
    public void testSetNewPrimitiveValue() throws ReflectiveOperationException {
        updateFacility.init(person, "age");
        updateFacility.setNewValue(30);
        Assertions.assertEquals(30, person.getAge());
    }

    @Test
    public void testSetNewObjectValue() throws ReflectiveOperationException {
        person.setBirthdate(LocalDate.now());
        updateFacility.init(person, "birthdate");
        String date = "1999-01-01";
        updateFacility.setNewValue(date);
        Assertions.assertEquals(LocalDate.parse(date), person.getBirthdate());
    }

    @Test
    public void testRevert() throws ReflectiveOperationException {
        updateFacility.init(person, "name");
        updateFacility.setNewValue("Mike");
        updateFacility.revert();
        Assertions.assertEquals("John", person.getName());
    }


    public static class Person {

        private String name;
        private int age;
        private LocalDate birthdate;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public LocalDate getBirthdate() {
            return birthdate;
        }

        public void setBirthdate(LocalDate birthdate) {
            this.birthdate = birthdate;
        }

        public void setBirthdate(String birthdate) {
            this.birthdate = LocalDate.parse(birthdate);
        }
    }
}