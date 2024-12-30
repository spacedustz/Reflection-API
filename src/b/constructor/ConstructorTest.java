package src.b.constructor;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

public class ConstructorTest {
    public static void main(String[] args) {
//        printConstructorData(Person.class);
        printConstructorData(Address.class);
    }

    public static void printConstructorData(Class<?> clazz) {
        // 생성자 개수
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        System.out.println(String.format("%s 클래스의 생성자 개수 %d", clazz.getSimpleName(), constructors.length));

        // 각 생성자 파라미터 출력
        for (int i = 0; i < constructors.length; i++) {
            Class<?>[] parameterTypes = constructors[i].getParameterTypes();

            List<String> parameterTypeNames = Arrays.stream(parameterTypes)
                    .map(Class::getSimpleName)
                    .toList();

            System.out.println(parameterTypeNames);
        }
    }

    // Person Class
    public static class Person {
        private final Address address;
        private final String name;
        private final int age;

        public Person() {
            this.name = "anonymous";
            this.age = 0;
            this.address = null;
        }

        public Person(String name) {
            this.name = name;
            this.age = 0;
            this.address = null;
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
            this.address = null;
        }

        public Person(String name, int age, Address address) {
            this.name = name;
            this.age = age;
            this.address = address;
        }
    }

    // Address Class
    public static class Address {
        private String street;
        private int number;

        public Address(String street, int number) {
            this.street = street;
            this.number = number;
        }
    }
}
