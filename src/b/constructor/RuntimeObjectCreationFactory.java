package src.b.constructor;

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
