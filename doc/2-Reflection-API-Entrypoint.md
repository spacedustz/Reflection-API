## 📚 Reflection API Entrypoint

이번엔 Java Reflection API의 Entrypoint를 학습 하였습니다.

어플리케이션의 클래스와 객체를 확인할 수 있도록 Reflection 로직을 작성할 수 있는 `Class<?>` 객체입니다.

Class 타입의 객체는, 객체의 런타임에 관한 정보가 있거나 앱에 있는 특정한 클래스가 존재합니다.

그리고 어떤 메서드와 필드를 포함하는지, 어떤 클래스를 확장하는지, 어떤 인터페이스를 실행하는지도 알 수 있습니다.

이 Class 객체를 얻을 수 있는 방법과, Java의 Wildcard를 복습해 보겠습니다.

---

## 📚 Class<?> 객체를 얻는 3가지 방법

### Object.getClass()

첫번쨰 방법은 객체 인스턴스의 getClass() 메서드로 얻을 수 있습니다.

아래 코드에서는 Java의 Class 객체를 사용하여 런타임에 특정 객체의 클래스 타입을 가져옵니다. 

이를 통해 각 객체의 런타임 타입(runtime type) 정보를 얻을 수 있습니다.

```java
String str = "some-string";
Car car = new Car();
Map<String, Integer> sortedMap = new TreeMap<>();

Class<String> strClass = str.getClass();
Class<Car> carClass = car.getClass();
Class<?> mapClass = sortedMap.getClass();
```

위 코드에서 특이사항은 `sortedMap` 변수입니다.

Map<String, Integer> 타입으로 선언되었지만 실제 구현체는 TreeMap 이므로 `mapClass`는 TreeMap 클래스 타입을 참조합니다.

즉, **제네릭 타입 정보인 <String, Integer>는 런타임 시 삭제**되므로 `Class<?>`로 표현됩니다.

런타임 결과 : mapClass.getName() -> "java.util.TreeMap"

<br>

또 1가지 유의해야할 점은 Primitive Type은 객체 클래스에서 상속되지 않으므로, getClass()를 호출할 수 없으며 따라서 변수 인스턴스에서 타입 정보를 수 없습니다.

<br>

### 타입명에 ".class" 추가


`.class`를 사용하여 Class 객체를 얻는 방법은 Java에서 정적(static)으로 특정 타입의 런타임 클래스 정보를 가져오는 가장 간단하고 직관적인 방법입니다..

이 메서드는 클래스의 인스턴스가 없을떄 주로 사용됩니다. 이 말은 Primitive Type의 타입 정보도 가져올 수 있다는 의미입니다.

```java
Class<String> strClass = String.class;
Class<Car> carClass = Car.class;
Class<?> mapClass = TreeMap.class;

// Primitive Type
Class<Integer> intClass = int.class;
Class<Void> voidClass = void.class;

// 익명, 중첩 Class
Class<?> anonymousClass = new Object() {}.getClass(); 
Class<OuterClass.InnerClass> innerClass = OuterClass.InnerClass.class;
```

<br>

### Class.forName()

Class.forName() 메서드는 Java에서 런타임에 클래스 이름을 문자열로 제공하여 해당 클래스의 Class 객체를 로드하고 반환하는 데 사용됩니다. 

이 메서드는 **리플렉션(reflection)** 과 함께 **동적 클래스 로딩**에 자주 사용됩니다.

즉, forName()을 사용해 패키지명을 포함한 클래스 경로에서 동적으로 클래스를 찾을 수 있습니다.

<br>

아래 코드처럼 패키지명을 포함한 클래스 경로를 forName의 파라미터로 넣어주어 클래스 정보를 알 수 있고,

내부 클래스의 정보에 접근하려면 `$` 기호를 사용해 아래 `$Engine` 과 같이 상위 클래스와 하위 클래스 사이에 기호를 사용하면 됩니다.

```java
Class<?> strType = Class.forName("java.lang.String");
Class<?> catType = Class.forName("vehicles.Car");
Class<?> engineType = Class.forName("vehicles.Car$Engine");
```

이 방법에서도 Primitive Type에 대한 Class 객체를 얻을 수 없으며, 만약 시도한다면 Runtime에서 ClassNotFoundException이 나게 됩니다.

그리고 3가지 방법중 이 방법에 Class 객체를 얻는 방법 중 가장 위험한 방법일 수 있습니다.

<br>

보통 이 방법을 사용하는 경우는, 인스턴스를 확인하거나 만들려는 타입이 별도의 config 파일이나 custom 파일에서 전달될 떄 이 방법을 사용합니다.

그래서 이걸 사용하면 소스코드를 변경하거나 리컴파일 없이 yml이나 xml 파일만 수정해도 어플리케이션의 동작을 변경할 수 있습니다.

<br>

또 다른 경우는, 확인하려는 클래스가 프로젝트가 없고, 코드를 컴파일할 떄 해당 클래스가 없을떄 사용합니다.

예를 들면 실행중인 앱의 ClassPath에 외부에 있는 클래스를 불러와서 사용하는 앱과 분리해 별도의 라이브러리를 구축하는 등의 경우입니다.



---

## 📚 클래스 타입(Class<T>)과 와일드카드(Class<?>)의 차이

### 제네릭 클래스의 상위/하위 관계

**Java 제네릭은 다음과 같은 규칙을 따릅니다**

- 제네릭 클래스 간에는 상하위 관계가 없다
- Class<String>와 Class<Object>는 상하위 관계가 아님.
- 하지만, Class<String>은 Class<?>의 하위 타입으로 간주됨.

<br>

**와일드카드의 역할**

- Class<?>는 모든 제네릭 클래스 타입의 상위 타입으로 작동.
- 따라서, Class<String>, Class<Integer> 등 모든 클래스 타입이 Class<?>로 대입 가능.

```java
Class<String> stringClass = String.class;
Class<?> anyClass = stringClass; // 가능: Class<String>은 Class<?>의 하위 타입

// 반대는 불가
Class<?> wildcardClass = String.class;
// Class<String> specificClass = wildcardClass; // 컴파일 에러
```

<br>

### `Class<T>`

**기본 개념**

- Class<T>는 제네릭 타입 T를 명시하여 특정 타입의 클래스 객체를 나타냅니다.
- 컴파일러는 제네릭 타입을 통해 **타입 안전성(type safety)**을 보장하며, 타입이 정확히 T임을 보장합니다.

<br>

**타입 안전성의 의미**

- Class<T>를 사용하면 컴파일러가 잘못된 타입 사용을 방지합니다.
- 예를 들어, Class<String>은 String 타입에만 한정되므로 다른 타입(예: Integer)과의 혼동이 없습니다.

<br>

### `Class<?>`

**기본 개념**

- Class<?>는 모든 클래스 타입을 허용하는 와일드카드 제네릭 타입입니다.
- 어떤 타입이든 허용되지만, 타입 정보는 불확실하므로 제네릭의 상속 관계를 명확히 구분하지 못합니다.

<br>

**예시**

```java
Class<?> anyClass = String.class;       // String 클래스
anyClass = Integer.class;              // Integer 클래스
anyClass = TreeMap.class;              // TreeMap 클래스
```

<br>

**제한점**

- Class<?>는 타입을 알 수 없는 상태로 선언되므로, 컴파일러는 타입 안정성을 보장하지 않습니다.
- 예를 들어, Class<?> 타입에서는 특정 타입으로의 안전한 캐스팅을 컴파일러가 허용하지 않습니다.

<br>

### 와일드카드를 사용해 클래스를 메서드로 전달

매개변수가 특정한 클래스와 인터페이스만 실행하도록 제한하는 방법입니다.

```java
public List<String> findAllMethods(Class<? extends Collection> clazz) {}
```