package src.a.gateway;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ReflectionGateway {
    public static void main(String[] args) throws ClassNotFoundException {
        Map<String, Integer> map = new HashMap<>();

        // 3가지 방법으로 Class 객체 얻기
        Class<String> stringClass = String.class;
        Class<?> hashMapClass = map.getClass();
        Class<?> squareClass = Class.forName("src.ReflectionGateway$Square");

        // 클래스 정보 뽑기
        printClassInfo(stringClass, hashMapClass, squareClass);

        // Drawable을 실행하는 익명 객체 생성
        var circleObject = new Drawable() {
            @Override
            public int getNumberOfCorners() {
                return 0;
            }
        };

        // 배열,기본타입,Enum,인터페이스,익명 여부 확인
        printClassInfo(Collection.class, boolean.class, int[][].class, Color.class, circleObject.getClass());
    }

    // 클래스 정보를 출력하는 함수
    // ... 표기법으로 가변인수를 가져오는데 가변인수는 클래스 객체임
    // Class<?> 는 모든 Class<T>의 상위 타입이기 떄문에 어떤 타입이든 가져올 수 있다
    private static void printClassInfo(Class<?> ... classes) {
        // 각 객체마다 정보 가져오기
        for (Class<?> clazz : classes) {
            // 클래스명, 패키지명 출력
            System.out.println(String.format("클래스명 : %s / 패키지명 : %s", clazz.getSimpleName(), clazz.getPackageName()));

            // 클래스가 실행하는 인터페이스 정보
            Class<?>[] implementedInterfaces = clazz.getInterfaces();
            for (Class<?> implementedInterface : implementedInterfaces) {
                System.out.println(String.format("클래스 %s의 인터페이스 : %s", clazz.getSimpleName(), implementedInterface.getSimpleName()));
            }

            System.out.println("배열 여부 : " + clazz.isArray());
            System.out.println("기본 타입 여부 : " + clazz.isPrimitive());
            System.out.println("Enum 여부 " + clazz.isEnum());
            System.out.println("인터페이스 여부 : " + clazz.isInterface());
            System.out.println("익명 여부 : " + clazz.isAnonymousClass());

            System.out.println();
            System.out.println("==========");
            System.out.println();
        }
    }

    // 사용자 정의 클래스
    private static class Square implements Drawable {
        @Override
        public int getNumberOfCorners() {
            return 4;
        }
    }

    // 임의의 UI Interface
    private static interface Drawable {
        int getNumberOfCorners();
    }

    // 색깔 Enum
    private enum Color {
        BLUE, RED, GREEN
    }
}
