# MYSQL의 공간함수와 삼각 함수 기반 거리 계산 성능 비교 실험

## 배경 및 목적

- 공간 데이터 처리에 있어서 MYSQL에서 제공하는 공간 함수(ST_Distance_Sphere)와 삼각 함수 기반 거리 계산(Haversine 공식)의 장단점을 분석
- 공간 데이터에서 두 방법의 성능을 측정하고, 쿼리 실행 속도를 비교

## 실험 방법

- 삼각 함수 거리 계산 : Haversine 공식을 사용하여 위도와 경도를 기준으로 거리를 계산
- 공간 함수 기반 거리 계산 : MySQL의 ST_Distance_Sphere 함수를 사용하여 두 지점 간의 구형 거리를 계산

### 실험 코드
```sql
    @Override
    public List<TestPosition> findPositionByLocation(
            Double latitude,
            Double longitude,
            Double distance) {
        QTestPosition testPosition = QTestPosition.testPosition;

        NumberTemplate<Double> haversineDistance = calculateHaversineDistance(
                latitude,
                longitude,
                testPosition);

        return fetchPositionsByLocation(
                haversineDistance,
                distance);
    }

    private NumberTemplate<Double> calculateHaversineDistance(
            Double latitude,
            Double longitude,
            QTestPosition testPosition) {
        return Expressions.numberTemplate(Double.class,
                "(6371 *" +
                        " acos(cos(radians({0})) *" +
                        " cos(radians({1})) *" +
                        " cos(radians({2}) -" +
                        " radians({3})) +" +
                        " sin(radians({0})) *" +
                        " sin(radians({1}))))",
                latitude,
                testPosition.position.latitude,
                testPosition.position.longitude,
                longitude);
    }

    private List<TestPosition> fetchPositionsByLocation(
            NumberTemplate<Double> haversineDistance,
            Double distance) {
        QTestPosition testPosition = QTestPosition.testPosition;
        return queryFactory
                .selectFrom(testPosition)
                .where(haversineDistance.loe(distance)
                        .and(testPosition.isDeleted.eq(IsDeleted.FALSE)))
                .fetch();
    }
```

```sql
    @Query(value = "SELECT * FROM test_point WHERE ST_Distance_Sphere(position," +
            " ST_GeomFromText(:point, 4326)) <= :distance * 1000", nativeQuery = true)
    List<TestPoint> findStadiumsByLocation(@Param("point") String point,
                                           @Param("distance") Double distance);
```

### 시간 측정

```sql
    public void StartTest(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String point = "POINT(37.7749 -122.4194)";
        testPointRepository.findStadiumsByLocation(point, 10.0);

        stopWatch.stop();
        log.info("PointTest ST_Distance_Sphere executed in {} ms",
                stopWatch.getTotalTimeMillis());
    }
```

```sql
    public void StartTest(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        testPositionRepository.findPositionByLocation(37.7749, -122.4194, 10.0);

        stopWatch.stop();
        log.info("PositionTest executed in {} ms", stopWatch.getTotalTimeMillis());

    }
```

## 실험 결과
- 삼각 함수 거리 계산 : 375ms
- 삼각 함수 거리 계산 (캐싱) : 7ms
- 공간 함수 기반 거리 계산 : 1587 ms
- 공간 함수 기반 거리 계산 (캐싱) : 1540ms

## 분석

### - 삼각 함수 방식
    - 저비용 연산 : MySQL에서 제공하는 cos, sin, acos를 사용한 비교적 단순한 수학 연산
    - 성능 : 공간 인덱스를 사용하지 않고, 수치 계산 방식이므로 더 나은 성능
    - 정확도 : 구형 모델을 사용하지만, 완전한 구면 거리 계산이 아니기에 낮음
    - 장점 : 상대적으로 계산 비용이 적고, 대량의 데이터를 처리할 때 성능이 더 나음
### - 공간 함수 기반 거리 계산 방식
    - 고비용 연산 : 공간 데이터 전용 함수로, 지구 구면 거리를 정확하게 계산하는 복잡한 연산
    - 성능 : 인덱스를 사용하더라도 모든 좌표에 대해 거리를 계산해야 하므로 사용 불가
    - 정확도 : 지리적 구면 거리를 매우 정확하게 계산함
    - 장점 : 매우 정확한 거리 계산을 제공하지만, 계산 비용이 큼

### 정리
공간 데이터 처리 성능을 최적화 하기 위해서는 상황에 따라 공간 함수 대신 삼각 함수 방식이 유리할 수 있다. 다만 정확도가 중요한 경우에는 공감 함수를 사용하는 것이 필요하다.

# `더 나은 방법은 없을까?`


## 공간 함수 ST_Buffer, ST_Contains

```sql
    -- M 사용시
    @Query(value = "SELECT * FROM test_point WHERE ST_Contains(ST_Buffer(ST_GeomFromText(:point, 4326), :distance), position)", nativeQuery = true)
    List<TestPoint> findStadiumByLocationWithBuffer(@Param("point") String point, @Param("distance") Double distance);
    
    -- KM 사용시
    @Query(value = "SELECT * FROM test_point WHERE ST_Contains(ST_Buffer(ST_GeomFromText(:point, 4326), :distance / 111.32), position)", nativeQuery = true)
    List<TestPoint> findStadiumByLocationWithBuffer(@Param("point") String point, @Param("distance") Double distance)
```

## 실험 결과

- ST_Buffer : 2469 ms
- ST_Buffer(공간 인덱스 사용 시) : 50ms

## 공간 인덱스

```sql
ALTER TABLE test_point MODIFY position POINT NOT NULL SRID 4326;

ALTER TABLE test_point ADD SPATIAL INDEX(position);
```

## 분석

- ST_Buffer는 해당 점을 기준으로 버퍼를 생성하고, 버퍼에 속하는 점을 찾는데, 공간 인덱스를 활용하여 버퍼에 속하는 점을 빠르게 찾을 수 있다
- 다만 ST_Distance_Sphere 가 더 정확한 결과를 보인다.

## 결론

### - 삼각 함수 방식
    - 위도와 경도로 주어지는 두 지점 사이의 구형 표면에서의 최단거리를 계산
    - 단순 수학적 연산으로 공간 데이터 없이 거리 계산이 가능
    - 계산이 가벼운 수학적 함수를 사용하여 공간 데이터 처리에 비해 비용이 적음
    - 인덱스 없이도 쉽게 적용 가능
    - 지구는 완전한 구체가 아니기에 일부 상황에서 (근거리, 고위도, 저위도) 오차가 크게 발생할 수 있음

### - ST_Distance_Sphere
    - MySQL에서 제공하는 공간 함수로, 지구 표면의 구형 모델을 사용하여 두 지점 사이의 거리를 계산
    - 두 좌표가 지구 표면상에서 얼마나 떨어져 있는지를 구하며, 미터 단위의 거리를 반환
    - 정확한 지구형 거리 계산이 가능하다.
    - 공간 함수이기 때문에 계산 비용이 일반적인 수학 함수보다 높다.
    - SRID 좌표계가 일치해야 한다.

### - ST_Buffer
    - 주어진 지점으로부터 반경을 기반으로 버퍼 영역(원형 영역)을 생성한다.
    - 버퍼 내에 포함된 공간 데이터를 검색할 수 있는 함수. 지정된 좌표로부터 일정 반경 내에 있는 지리 데이터의 포함 여부를 필터링 하는 것이 가능
    - 단순한 점 대 점 거리 계산이 아닌 복잡한 공간 데이터 검색이 가능.
    - 특정 좌표를 중심으로 일정 반경 내에 있는 데이터를 효율적으로 찾을 수 있음
    - 공간 인덱스가 설정되지 않으면 성능 저하 발생
    - 거리를 위도나 경도 단위로 변환하는 추가 작업의 필요