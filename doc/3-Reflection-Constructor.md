## 📚 Constructor<?>

Java의 `Constructor<?>`는 Java 리플렉션 API에서 제공하는 클래스 중 하나로, 클래스의 생성자(constructor)를 나타내는 객체입니다. 

Constructor<?>는 java.lang.reflect 패키지의 일부이며, 특정 클래스의 생성자에 접근하거나 실행할 수 있도록 도와줍니다.

<br>

### 클래스 로드 및 생성자 조회

Class 객체를 사용하여 특정 클래스의 모든 Constructor<?> 객체를 가져올 수 있고, 해당 생성자 파라미터를 알고 있다면 파라미터에 parameterTypes를 넣으면 특정 생성자를 가져올 수 있습니다.

만약 가져온 Class에 생성자가 없다면 자동으로 기본 생성자를 포함한 단일 요소 배열로 반환됩니다.

- Public, Private 모든 생성자 로드
  - `getDeclaredConstructors()` 또는 `getDeclaredConstructor(Class<?>... parameterTypes)`  
- Public 생성자만 로드
  - `getConstructor()` 또는 `getConstructor(Class<?>... parameterTypes)`

```java
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
```

---

## 📚 Dynamic Object Creation using Reflection

Reflection을 사용하면 런타임에 클래스 이름, 생성자 정보, 그리고 파라미터를 기반으로 객체를 동적으로 생성할 수 있습니다.

특히 아래와 같은 경우에 유용합니다.

- **클래스 타입이 런타임에만 결정되는 경우**: 실행 시점에만 어떤 클래스가 사용될지 알 수 있을 때.
- **다형성 객체의 동적인 생성**: 여러 서브클래스 중 조건에 맞는 클래스를 동적으로 생성해야 할 때.
- **제네릭 메서드 활용**: 타입 안정성을 유지하면서도 다양한 타입의 객체를 생성해야 할 때.
- **유연성 확보**: 특정 로직에 의존하지 않고, 런타임에 객체를 유연하게 생성하는 시스템을 구성해야 할 때.

<br>

### Reflection 없이 동적인 객체를 생성할 떄의 문제점

Reflection 없이 동적으로 객체를 생성하려면 아래 예시 코드처럼 switch 문이나 if-else 블록을 사용해 각 클래스의 생성자를 호출해야 합니다.

이는 다음과 같은 문제점이 발생합니다.

- **어려운 유지보수**: 클래스가 추가되거나 변경될 때마다 switch 문을 수정해야 하며, 길고 복잡한 코드가 됩니다.
- **부족한 확장성**: 새로운 클래스 타입을 추가할 때 기존 코드를 반복적으로 수정해야 합니다.
- **캡슐화 위반**: 코드가 특정 클래스의 세부사항에 지나치게 의존하게 됩니다.
- 예를 들어, 아래 코드는 특정 클래스 타입에 따라 객체를 생성하지만, 비효율적이고 확장성이 부족합니다.

```java
import java.lang.reflect.Type;

public Object createObj(Type type, Object arg) {
    switch (type) {
      case Type.Employee: return new Eployee(arg);
      case Type.Contractor: return new Contractor(arg);
    }
}
```

<br>

### Reflection을 활용한 동적인 객체 생성

동적으로 객체를 생성할 때 `Constructor.newInstance(Object... args)`를 이용해 객체를 생성하고,

`Class.newInstance()`는 Java 9 이후부터 권장되지 않으므로 `Constructor.newInstance()`를 사용해 주는 것이 좋습니다.

<br>

**isInstance()와 instanceof()의 차이점**

|특징|	instanceof|	Class.isInstance()|
|---|---|---|
|검사 시점|	컴파일 타임 검사|	런타임 검사|
|유형 정보|	코드에 명시된 클래스만 확인 가능|	Reflection으로 동적 클래스 확인 가능|
|적용 대상|	정적 클래스, 인터페이스|	동적 클래스, 런타임 타입|

<br>

**Primitive Type 처리**

Reflection이 int와 Integer를 구분하기 때문에, Primitive 타입과 Wrapper 클래스 간의 호환성을 처리하는 추가 로직을 포함하였습니다.

```java
import java.lang.reflect.Constructor;

public class RuntimeObjectCreationFactory {
  public static void main(String[] args) {
    try {
      // Employee 객체 생성
      Object employee = createObj("src.b.constructor.RuntimeObjectCreationFactory$Employee", "사람1", 25);
      System.out.println(employee);

      // Contractor 객체 생성
      Object contractor = createObj("src.b.constructor.RuntimeObjectCreationFactory$Contractor", "사람2", 30);
      System.out.println(contractor);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // 클래스 이름과 생성자 파라미터를 기반으로 객체 생성
  public static Object createObj(String className, Object... args) throws Exception {
    // 1. 클래스 로드
    Class<?> clazz = Class.forName(className);

    // 2. 적합한 생성자 탐색
    for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
      if (constructor.getParameterCount() == args.length) {
        boolean isMatch = true;
        Class<?>[] paramTypes = constructor.getParameterTypes();

        // 3. 파라미터 타입 비교
        for (int i = 0; i < args.length; i++) {
          if (!isCompatibleType(paramTypes[i], args[i])) {
//                    if (!paramTypes[i].isInstance(args[i]) && !(args[i] == null && !paramTypes[i].isPrimitive())) {
            isMatch = false;
            break;
          }
        }

        if (isMatch) {
          constructor.setAccessible(true); // private 생성자도 호출 가능
          return constructor.newInstance(args);
        }
      }
    }

    throw new IllegalArgumentException("적합한 생성자를 찾을 수 없습니다.");
  }

  // Employee 클래스
  public static class Employee {
    private String name;
    private int age;

    // 생성자
    public Employee(String name, int age) {
      this.name = name;
      this.age = age;
    }

    @Override
    public String toString() {
      return "Employee{name='" + name + "', age=" + age + "}";
    }
  }

  // Contractor 클래스
  public static class Contractor {
    private String name;
    private int hoursWorked;

    // 생성자
    public Contractor(String name, int hoursWorked) {
      this.name = name;
      this.hoursWorked = hoursWorked;
    }

    @Override
    public String toString() {
      return "Contractor{name='" + name + "', hoursWorked=" + hoursWorked + "}";
    }
  }

  // 타입 호환성 체크 메서드
  private static boolean isCompatibleType(Class<?> paramType, Object arg) {
    if (arg == null) {
      // null 값은 primitive 타입에 매핑 불가
      return !paramType.isPrimitive();
    }
    // Primitive 타입과 Wrapper 타입 매칭
    if (paramType.isPrimitive()) {
      if (paramType == int.class && arg instanceof Integer) return true;
      if (paramType == double.class && arg instanceof Double) return true;
      if (paramType == long.class && arg instanceof Long) return true;
      if (paramType == boolean.class && arg instanceof Boolean) return true;
      if (paramType == char.class && arg instanceof Character) return true;
      if (paramType == byte.class && arg instanceof Byte) return true;
      if (paramType == short.class && arg instanceof Short) return true;
      if (paramType == float.class && arg instanceof Float) return true;
      return false;
    }
    // 일반 객체 타입 매칭
    return paramType.isInstance(arg);
  }
}
```

<br>

**생성된 객체**

```text
Employee{name='사람1', age=25}
Contractor{name='사람2', hoursWorked=30}
```