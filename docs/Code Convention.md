# `코드 컨벤션`

<aside>
☝ 명확한 의미 전달을 위해 축약형 사용을 지양합니다.

</aside>

<aside>
☝ 작성한 코드를 팀원도 이해할 수 있도록 주석을 달아줍니다. → 선택

</aside>

<aside>
☝ 패키지 이름은 소문자로 생성하고, 역할이나 기능에 따라 명확하게 묶어서 명명합니다. 언더스코어 ‘_’나 대문자를 섞지 않습니다.

```java
com.example.project.controller
com.example.project.service
com.example.project.repository
com.example.project.model
```

</aside>

<aside>
☝ 상수는 대문자와 언더스코어(”_“)로, 변수와 메서드는 CamelCase 형식으로 작성합니다.

```java
// 상수
static final int MAX_COUNT = 100;

// 변수
int itemCount;

// 메서드
public String printCount() { ... }
```

</aside>

<aside>
☝ 변수명을 짓기 어려울 때에는 아래 사이트의 도움을 받아봅시다!
영어로 선택 후 원하는 단어를 검색하면 됩니다.

[Curioustore](https://curioustore.com/#!/)

</aside>

<aside>
☝ Boolean 타입의 변수는 접두사로 is를 사용해 변수명을 작성합니다.

```java
boolean isExist;
boolean isTrue;
```

</aside>

<aside>
☝ long 타입의 값의 마지막에는 대문자 ‘L’을 붙여줍시다.

```java
long base = 54423234211L;
```

</aside>

<aside>
☝ 컬렉션 이름은 복수형을 사용하거나 컬렉션임을 명시해줍니다.

```java
List ids;
Map<User, int> userMap;
```

</aside>

<aside>
☝ 클래스명은 명사로 작성하고 UpperCamelCase를 사용합니다.

```java
private class Address { ... }
public class UserEmail { ... }
```

</aside>

<aside>
☝ 메서드명은 소문자로 시작하고 동사로 네이밍합니다.

대표적인 메서드들의 네이밍 규칙은 아래를 따릅니다.

```java
// 조회(상세)
getXXX()
getXXXDetail()
getXXXInfo()

// 조회(리스트)
getXXXList()

// 조회(카운트)
getXXXCount()

// 등록
createXXX()
addXXX()
registXXX()

// 수정
updateXXX()
modifyXXX()

// 삭제
removeXXX()
deleteXXX()
```

</aside>

<aside>
☝ Enum 변수의 이름은 대문자로 작성합니다.

```java
// 상태 - XXX_STATUS
public enum MemberStatus {
    WAITING_STATUS,    // 수락 대기 상태
    ACCEPT_STATUS,     // 수락 상태
    WITHDRAW_STATUS    // 탈퇴 상태
}

// 유형 - XXX_TYPE
public enum UserType {
    ADMIN_TYPE,
    CUSTOMER_TYPE,
    GUEST_TYPE;
}
```

</aside>

<aside>
☝ builder를 호출하는 static 메서드는 다음 규칙을 따릅니다.

1. 파라미터가 1개인 경우: **xxxFrom**

```java
public static User createUserFromUsername(String username) {
    // username을 사용해 builder 호출
    return new User(username);
}
```

1. 파라미터가 2개 이상인 경우: **xxxOf**

```java
public static User createUserOf(String username, String email) {
    // username과 email을 사용해 builder 호출
    return new User(username, email);
}
```

</aside>

<aside>
☝ 다른 객체로 변환하는 메서드의 이름은 toEntity 형식으로 선언합니다.

```java
@Getter
public class ProductCreateRequest {
    private ProductType type;
    private ProductSellingStatus sellingStatus;
    private String name;
    private int price;

		// AllArgumentConstructor는 private으로 직접 사용을 막아주고, 
		// @Builder 선언해서 직접 사용 막기
    @Builder
    private ProductCreateRequest(ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

		// toEntity
    public Product toEntity(String productNumber) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }
}
```

</aside>

<aside>
✅

DTO
____Response.java
___Request.java

</aside>