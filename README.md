## 🐱 Java Reflection-API 학습 노트

<details>

<summary><strong>내용보기</strong></summary>

### 1. Reflection 개요

- [Reflection API Entrypoint & WildCard](./doc/2-Reflection-API-Entrypoint.md)
- [Class Analyzer 구현](./src/a/analyzer)
- [Reflection & Recursion을 이용한 Interface Analyze 구현](./src/a/analyzer)

<br>

### 2. 객체와 생성자

<br>

### 3. 필드 검사와 배열 감사

<br>

### 4. 필드 조정 및 배열 생성

<br>

### 5. 메서드 탐색과 호출

<br>

### 6. 제어자 탐색 및 분석

<br>

### 7. Reflection과 Annotation

<br>

### 8. Dynamic Proxy

<br>

### 9. 성능 및 보안 모범 사례

</details>

---

## 📚 Reflection이란?

<details>
<summary><strong>내용보기</strong></summary>

<br>

Java Reflection이란 짧게 요약하면 **런타임 중, 어플리케이션의 클래스 및 객체에 관련된 정보에 액세스 할 수 있게 해주는 언어이자 JVM의 기능** 입니다.

보통의 프로그램은 실행 시 Input을 받아 Output을 반환합니다.

반대로 Reflection으로 프로그램 작성 시 Input과 내부 소스 로직을 모두 입력값으로 간주해 그걸 분석하고 Output을 반환합니다.

위 이유로 인해 Reflection을 이용하면 강력한 라이브러리,프레임워크,소프트웨어를 설계 할 수 있습니다.

- Java Reflection은 언어이자 JVM의 기능 중 하나이며, 런타임 시 `classes`와 `objects`를 추출할 수 있습니다.
- Reflection API를 통해 다양한 소프트웨어를 Flexible하게 컴포넌트를 연결하고, 소스코드를 수정하지 않고 새로운 프로그램 흐름을 만들 수 있습니다.
- 또, Reflection을 이용해 다목적 알고리즘을 작성할 수 있습니다. 실행하고 있는 클래스와 객체에 따라 이 알고리즘을 쉽게 조정하거나 변경할 수 있습니다.

---

## 📚 Use Cases

### Spring

- Spring의 `@Configuration, @Bean, Dependency Injection` 등이 있습니다.
- 예를 들어 `@Configuration`을 정의한 Config 클래스의 메서드에 `@Bean`을 붙이면 런타임 시 해당 Bean을 객체로 만들어 다른 객체의 생성자에 해당 Bean 객체가 필요할 떄 주입해주는 역할을 합니다.
- Google Guice

<br>

### Json Serialization/Deserialization Library

- 라이브러리에 사용해 프로토콜 간 변환을 실행할 때에도 Reflection이 사용됩니다.
- Jackson Library
- Gson Library
- 입력값으로 Json이 들어오면 위 라이브러리들은 Reflection을 사용해 클래스를 확인하고 필드를 전부 분석후 간단히 객체로 변환 해줍니다. (반대의 경우도 마찬가지)
- 그래서 우리는 아주 흔하게 사용하는 `ObjectMapper.readValue(json, Person.class)`와 `ObjectMapper.writeValueAsString(person)`과 같이 간단하게 1줄의 메서드로 직렬화/역직렬화를 사용하고 있는 것입니다.

<br>

### JUnit

Java Reflection으로 동작하는 대표적인 Use Cases는 유닛 테스트에 자주 사용하는 `JUnit`이 있습니다.

이해하기 쉽게 아래와 같은 Reflection으로 동작하는 테스트 클래스가 있다고 가정해 봅시다.

```java
public class CarTest {
    @Before
    public void setUp(){}
    
    @Test
    public void testDrive(){}
    
    @Test
    public void testBrake(){}
}
```

위 클래스를 Reflection을 사용하지 않고 만들면 클래스에 main() 메서드를 먼들어 수동으로 클래스를 인스턴스화 하고,

메서드 각각을 수동으로 설정하고 호출해야 합니다.

```java
public class CarTest {
    public void setUp(){}
    public void testDrive(){}
    public void testBrake(){}
    
    ...
    
    public static void main(String[] args) {
        CarTest carTest = new CarTest();
        carTet.setUp();
        carTest.testDrive();
        carTest.testBrake();
    }
}
```

Reflection은 위 Use Cases들뿐 아니라 아주 다양하게(Logging Frameworks, ORM, Web Frameworks 등등) 사용되고 있습니다.

---

## 📚 Reflection 문제점

위에서 알아본것과 같이 Reflection은 매우 강력한 기능이지만 아래와 같은 단점들도 있습니다.

<br>

### 성능 문제

Reflection은 런타임에 메서드나 필드에 접근하기 위해 추가적인 처리 단계를 거칩니다. 따라서 일반적인 코드 호출보다 느립니다.

이러한 성능 저하로 인해, Reflection은 빈번히 호출되는 코드에서 사용하기 부적합합니다.

<br>

### 안정성 감소

Reflection은 컴파일 타임이 아니라 런타임에 코드 구조에 접근하기 때문에, 컴파일 시점에 에러를 잡을 수 없습니다.

만약 접근하려는 메서드나 필드의 이름이 변경되거나 삭제되면, 런타임 에러가 발생할 가능성이 높아집니다.

<br>

### 캡슐화 위반

Reflection을 사용하면 private 필드나 메서드에 접근할 수 있습니다. 이는 객체지향 프로그래밍의 중요한 원칙인 **캡슐화(encapsulation)** 를 위반합니다.

잘못 사용하면 클래스의 내부 구현에 의존하게 되어 유지보수가 어려워질 수 있습니다.

<br>

### 보안 문제

Reflection은 보안 관리자(Security Manager)가 설정된 환경에서는 제한될 수 있습니다. 잘못된 접근으로 인해 민감한 데이터가 노출될 위험이 있습니다.

예를 들어, 악의적인 코드가 Reflection을 통해 private 데이터를 조작하거나 읽을 수 있습니다.

<br>

### 가독성과 유지보수성 저하

Reflection으로 작성된 코드는 일반 코드에 비해 이해하기 어렵고, 디버깅이 복잡합니다.

특히 팀 작업에서 Reflection을 남용하면 코드의 유지보수가 매우 어려워질 수 있습니다.

<br>

### 동일성 문제

Reflection은 클래스나 객체의 구조를 기반으로 동작하기 때문에, 특정 JVM 구현이나 클래스 로더에 따라 동작이 달라질 수 있습니다.

이는 코드의 플랫폼 독립성을 저해할 수 있습니다.

</details>


---
