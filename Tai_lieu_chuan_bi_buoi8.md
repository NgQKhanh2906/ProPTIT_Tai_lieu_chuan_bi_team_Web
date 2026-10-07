# BUỔI 8: TỐI ƯU JPA, VALIDATION VÀ EXCEPTION HANDLING
*Tài liệu chuẩn bị buổi 8*

---

**Mục lục**
- [BUỔI 8: TỐI ƯU JPA, VALIDATION VÀ EXCEPTION HANDLING](#buổi-8-tối-ưu-jpa-validation-và-exception-handling)
  - [Phần 1: Các phương pháp tối ưu JPA](#phần-1-các-phương-pháp-tối-ưu-jpa)
    - [I. Lazy / Eager initialization](#i-lazy--eager-initialization)
      - [1. Bản chất kỹ thuật:](#1-bản-chất-kỹ-thuật)
      - [2. Giải quyết bài toán gì?](#2-giải-quyết-bài-toán-gì)
      - [3. Lưu ý \& Best Practices:](#3-lưu-ý--best-practices)
      - [4. Demo:](#4-demo)
    - [II. Pagination](#ii-pagination)
    - [III. Caching](#iii-caching)
    - [IV. Manual / Native query](#iv-manual--native-query)
    - [V. Dynamic query](#v-dynamic-query)
    - [VI. Phòng tránh N + 1 query](#vi-phòng-tránh-n--1-query)
  - [Phần 2: Tìm hiểu về Validation](#phần-2-tìm-hiểu-về-validation)
    - [I. Cơ chế Validation trong Spring Boot](#i-cơ-chế-validation-trong-spring-boot)
  - [Phần 3: Tìm hiểu về Exception Handling](#phần-3-tìm-hiểu-về-exception-handling)
    - [I. Kiến trúc xử lý lỗi tập trung](#i-kiến-trúc-xử-lý-lỗi-tập-trung)
  - [Phần 4: Override default fetch plan với JOIN FETCH / Entity Graph](#phần-4-override-default-fetch-plan-với-join-fetch--entity-graph)
    - [I. Sử dụng JOIN FETCH trong JPQL](#i-sử-dụng-join-fetch-trong-jpql)
    - [II. Sử dụng @EntityGraph](#ii-sử-dụng-entitygraph)
  - [Phần 5: Bài tập chuẩn bị (ứng dụng vào Project Quản lý Tour)](#phần-5-bài-tập-chuẩn-bị-ứng-dụng-vào-project-quản-lý-tour)
    - [I. Yêu cầu 1 (Áp dụng Pagination)](#i-yêu-cầu-1-áp-dụng-pagination)
    - [II. Yêu cầu 2 (Áp dụng Validation)](#ii-yêu-cầu-2-áp-dụng-validation)
    - [III. Yêu cầu 3 (Áp dụng Exception Handling)](#iii-yêu-cầu-3-áp-dụng-exception-handling)

---

## Phần 1: Các phương pháp tối ưu JPA

### I. Lazy / Eager initialization

Cách JPA quản lý quan hệ 1-N bằng `@OneToMany`/`@ManyToOne`, và quyết định quan trọng nhất đi kèm nó - dữ liệu liên quan nên được tải ngay lập tức hay chỉ tải khi thật sự cần.

#### 1. Bản chất kỹ thuật:

- **A.** `FetchType.EAGER`: dữ liệu quan hệ được tải **ngay lập tức**, cùng lúc hoặc ngay sau khi Entity chính được tải.
- **B.** `FetchType.LAZY`: dữ liệu quan hệ **chưa được tải**, chỉ được tải tại đúng thời điểm có đoạn code gọi đến getter của thuộc tính đó.
- **C.** Cơ chế đứng sau Lazy là **Proxy Object**: khi Hibernate trả về một Entity có trường Lazy, trường đó không chứa object thật, mà chứa một object giả do Hibernate tự sinh ra lúc chạy, trông giống hệt object thật nhưng bên trong rỗng, chỉ giữ lại giá trị khóa chính.
- **D.** Proxy Object chỉ thức dậy và tự động bắn một câu `SELECT` xuống database **đúng lần đầu tiên** có phương thức nào đó được gọi trên nó (ví dụ `tour.getLichTrinhs().size()`).
- **E.** Việc thức dậy này bắt buộc phải diễn ra trong khi **Hibernate Session** vẫn còn mở. Tức là còn trong phạm vi một `@Transactional`. Ra khỏi phạm vi đó, Proxy không còn cách nào tải dữ liệu, dẫn đến lỗi `LazyInitializationException`.

#### 2. Giải quyết bài toán gì?

- Nếu mọi quan hệ đều tải kiểu Eager, mỗi lần lấy một `Tour` để chỉ hiển thị tên, hệ thống vẫn phải tải kèm toàn bộ danh sách `LichTrinh` của tour đó dẫn đến dữ liệu không dùng đến nhưng vẫn tốn băng thông, bộ nhớ, thời gian truy vấn.

#### 3. Lưu ý & Best Practices:

- Mặc định của JPA **khác nhau** theo loại quan hệ:

| Loại quan hệ  | Mặc định |
| ------------- | -------- |
| `@ManyToOne`  | `EAGER`  |
| `@OneToOne`   | `EAGER`  |
| `@OneToMany`  | `LAZY`   |
| `@ManyToMany` | `LAZY`   |

- Lỗi `LazyInitializationException` thường xảy ra khi Controller trả thẳng Entity ra ngoài, và Jackson cố đọc một trường Lazy **sau khi** transaction của Service đã đóng lại.
- Không nên đổi toàn bộ quan hệ sang `EAGER`. Cách này chỉ che giấu vấn đề và trực tiếp dẫn tới N + 1 query.
- Cách xử lý đúng khi cần dữ liệu Lazy ngoài phạm vi transaction không phải là đổi sang Eager, mà là chủ động yêu cầu tải sớm cho đúng truy vấn đó.

#### 4. Demo:

Entity `Tour` sau khi bổ sung quan hệ đối tượng:

```java
package com.example.quanlytour.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;

@Entity
@Table(name = "tours")
@Data
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tour_name", nullable = false, length = 200)
    private String tourName;

    @Column(name = "departure_location", length = 100)
    private String departureLocation;

    @Column(name = "destination", length = 100)
    private String destination;

    @Column(name = "description", length = 1000)
    private String description;

    @OneToMany(mappedBy = "tour", fetch = FetchType.LAZY)
    private List<LichTrinh> lichTrinhs;

}
```

Entity `LichTrinh` sau khi đổi `Long maTour` thành quan hệ đối tượng:

```java
package com.example.quanlytour.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Data;

@Entity
@Table(name = "lich_trinh")
@Data
public class LichTrinh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_tour", nullable = false)
    private Tour tour;

    @Column(name = "ngay_xuat_phat", nullable = false)
    private LocalDate departureDate;

    @Column(name = "gia_co_ban", nullable = false)
    private Double basePrice;

    @Column(name = "so_cho_toi_da", nullable = false)
    private Integer totalSlots;

    @Column(name = "so_cho_da_dat")
    private Integer bookedSlots;

}
```

Giải thích:

- **A.** `@OneToMany(mappedBy = "tour", fetch = FetchType.LAZY)` trên `Tour`: khai báo một `Tour` có nhiều `LichTrinh`. `mappedBy = "tour"` chỉ rõ phía `LichTrinh` mới là nơi thật sự giữ khóa ngoại (tên đúng bằng tên thuộc tính `tour` bên `LichTrinh`), còn `Tour` chỉ là phía "được tham chiếu tới".
- **B.** `@ManyToOne(fetch = FetchType.LAZY)` trên `LichTrinh`: khai báo nhiều `LichTrinh` cùng trỏ về một `Tour`. Cố tình ghi rõ `FetchType.LAZY` để ghi đè mặc định `EAGER` của `@ManyToOne`.
- **C.** `@JoinColumn(name = "ma_tour", nullable = false)`: xác nhận cột khóa ngoại thật trong bảng `lich_trinh` tên là `ma_tour`, đúng với cột đã có từ Buổi 7.
- **D.** Từ nay, `lichTrinh.getTour().getTourName()` lấy được tên tour trực tiếp, không cần tự gọi `tourService.getTourById(lichTrinh.getMaTour())` như cách làm tạm ở Buổi 7.

Input/Output minh họa `LazyInitializationException`:

```java
@Transactional
public List<LichTrinh> getLichTrinhsInsideTransaction(Long tourId) {
    Tour tour = tourRepository.findById(tourId).orElseThrow();
    return tour.getLichTrinhs();
}
```

- Input: gọi phương thức trên khi đang còn `@Transactional` bao quanh.
- Output: trả về danh sách `LichTrinh` bình thường, vì `tour.getLichTrinhs()` được gọi **trong khi** Session còn mở.

```java
public List<LichTrinh> getLichTrinhsOutsideTransaction(Long tourId) {
    Tour tour = tourRepository.findById(tourId).orElseThrow();
    return tour.getLichTrinhs();
}
```

- Input: gọi phương thức trên — phương thức này **không có** `@Transactional`.
- Output: ứng dụng ném `LazyInitializationException: could not initialize proxy - no Session`, vì tại thời điểm `getLichTrinhs()` được gọi, Session phục vụ cho `findById` đã đóng ngay sau khi phương thức Repository trả về.

### II. Pagination

Danh sách `Tour` hôm nay có vài chục dòng, trả hết về một lần không ảnh hưởng gì. Khi hệ thống vận hành thật với hàng chục nghìn tour, trả hết toàn bộ bảng trong một Response JSON là điều không một hệ thống sản xuất nào chấp nhận được, kể cả khi kết quả được dùng để hiển thị theo trang trên giao diện.

**1. Bản chất kỹ thuật:**

- **A.** `Pageable` (`org.springframework.data.domain.Pageable`) là một interface mô tả "tôi muốn trang số mấy, mỗi trang bao nhiêu dòng, sắp xếp theo gì".
- **B.** `PageRequest.of(page, size)` là cách tạo một `Pageable` cụ thể, trong đó `page` được đánh số **bắt đầu từ 0**.
- **C.** Khi một phương thức Repository nhận `Pageable` làm tham số, Spring Data JPA tự dịch nó thành hai mệnh đề SQL: `LIMIT size OFFSET (page * size)`.
- **D.** `JpaRepository` đã có sẵn `Page<T> findAll(Pageable pageable)`, không cần viết gì thêm để có phân trang cơ bản.
- **E.** Kiểu trả về `Page<T>` không chỉ chứa dữ liệu của trang hiện tại, mà còn kèm theo tổng số dòng, tổng số trang — những số liệu này đến từ một câu `SELECT COUNT(*)` chạy riêng, **thêm** vào câu `SELECT` dữ liệu chính.

**2. Giải quyết bài toán gì?:**

- Không có Pagination, một API trả toàn bộ bảng sẽ kéo theo: thời gian phản hồi tăng theo cấp số nhân khi dữ liệu lớn dần, bộ nhớ Server phải giữ toàn bộ kết quả cùng lúc, và Client (web, mobile) phải tải về một khối dữ liệu khổng lồ chỉ để hiển thị vài chục dòng đầu tiên trên màn hình.
- Pagination giới hạn chính xác lượng dữ liệu cần xử lý và truyền tải cho mỗi lần gọi, bất kể bảng có bao nhiêu dòng.

**3. Lưu ý & Best Practices:**

- `page` đánh số từ 0, không phải từ 1 — `page = 0` là trang đầu tiên. Đây là lỗi sai rất phổ biến khi mới dùng.
- Không nên để Client tự do truyền `size` tùy ý (ví dụ `size = 100000`) — nên giới hạn giá trị tối đa cho phép ở tầng Service.
- `Page<T>` chạy 2 câu truy vấn (dữ liệu và đếm tổng). Nếu không cần biết tổng số trang (chỉ cần nút "xem thêm"), có thể dùng `Slice<T>` để tiết kiệm câu đếm.

**4. Demo Thực chiến:**

```java
public Page<Tour> getAllTours(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return tourRepository.findAll(pageable);
}
```

Giải thích:

- **A.** `PageRequest.of(page, size)` dựng một đối tượng `Pageable` từ hai số nguyên nhận vào.
- **B.** `tourRepository.findAll(pageable)`: gọi đúng phương thức có sẵn của `JpaRepository`, không cần khai báo gì thêm ở `TourRepository`.

Input: gọi `getAllTours(0, 2)` khi bảng `tours` có 5 dòng.

Output — câu lệnh SQL sinh ra trong Console:

```text
Hibernate: 
    select t1_0.id, t1_0.departure_location, t1_0.description, t1_0.destination, t1_0.tour_name 
    from tours t1_0 
    limit ? offset ?
Hibernate: 
    select count(t1_0.id) 
    from tours t1_0
```

Output — dữ liệu `Page<Tour>` trả về, chỉ gồm 2 trong 5 dòng, kèm metadata:

```json
{
    "content": [
        {"id": 1, "tourName": "Da Lat 3N2D"},
        {"id": 2, "tourName": "Sa Pa 2N1D"}
    ],
    "totalElements": 5,
    "totalPages": 3,
    "number": 0,
    "size": 2
}
```
### III. Caching

Giả sử trong một phương thức Service, `tourRepository.findById(5L)` được gọi hai lần liên tiếp — có thể do hai nhánh code khác nhau cùng cần đến dữ liệu tour đó. Trực giác thông thường sẽ nghĩ hệ thống bắn hai câu `SELECT` xuống database. Thực tế, Hibernate đã tự lo việc này từ trước, không cần cấu hình gì thêm.

**1. Bản chất kỹ thuật:**

- **A.** Mỗi khi một transaction bắt đầu, Hibernate mở một **Persistence Context** (còn gọi là Session), đóng vai trò như một vùng nhớ tạm theo dõi mọi Entity đã được tải trong transaction đó.
- **B.** Vùng nhớ này chính là **First-level Cache**, hoạt động dạng một Map, khóa là cặp (loại Entity, giá trị khóa chính).
- **C.** Khi `findById(5L)` được gọi, Hibernate tra Map này trước. Nếu đã có sẵn Entity `Tour` ứng với `id = 5` (do đã tải ở một lời gọi trước đó trong cùng transaction), nó trả về **thẳng tham chiếu đó**, không bắn thêm câu `SELECT` nào.
- **D.** First-level Cache được bật mặc định, không thể tắt, và chỉ tồn tại trong đúng phạm vi một transaction — transaction kết thúc, cache bị hủy theo.
- **E.** Khác với First-level Cache, **Second-level Cache** là một cơ chế riêng (không bật mặc định, cần thư viện ngoài như Ehcache), có phạm vi dùng chung giữa nhiều transaction khác nhau — nằm ngoài phạm vi buổi học này.

**2. Giải quyết bài toán gì?:**

- Không có First-level Cache, mỗi lần gọi `findById` với cùng một `id`, dù trong cùng một luồng xử lý, cũng sẽ bắn thêm một câu `SELECT` dư thừa xuống database.
- Với một nghiệp vụ gọi đi gọi lại cùng một Entity ở nhiều bước xử lý khác nhau (ví dụ vừa kiểm tra tồn tại, vừa cập nhật), First-level Cache giúp giảm số lần truy vấn xuống database xuống mức tối thiểu một cách hoàn toàn tự động.

**3. Lưu ý & Best Practices:**

- First-level Cache chỉ có hiệu lực **trong cùng một transaction**. Hai Request HTTP khác nhau luôn chạy trong hai transaction khác nhau, nên không bao giờ dùng chung cache này.
- Không nên nhầm lẫn First-level Cache với việc dữ liệu luôn "mới nhất" — nếu một tiến trình khác thay đổi dữ liệu ở database ngay trong lúc transaction này đang chạy, Entity trong cache vẫn giữ giá trị cũ.
- Không cần (và không thể) tự viết code để bật tính năng này — nó là một phần không thể tách rời của cách Hibernate Session hoạt động.

**4. Demo Thực chiến:**

```java
@Transactional
public void demoFirstLevelCache(Long tourId) {
    Tour tourLanDau = tourRepository.findById(tourId).orElseThrow();
    Tour tourLanHai = tourRepository.findById(tourId).orElseThrow();
    boolean cungMotObject = (tourLanDau == tourLanHai);
}
```

Giải thích:

- **A.** Hai lời gọi `findById(tourId)` dùng chung một giá trị `tourId`, cùng nằm trong phạm vi của `@Transactional`.
- **B.** `tourLanDau == tourLanHai` so sánh **địa chỉ tham chiếu** (không phải nội dung), kết quả là `true`, vì Hibernate trả về đúng cùng một object đã nằm sẵn trong Persistence Context.

Input: gọi `demoFirstLevelCache(1L)`.

Output — Console chỉ in **đúng một** câu lệnh SQL, dù trong code có hai lời gọi `findById`:

```text
Hibernate: 
    select t1_0.id, t1_0.departure_location, t1_0.description, t1_0.destination, t1_0.tour_name 
    from tours t1_0 
    where t1_0.id=?
```

### IV. Manual / Native query

Phần 3, mục 2 của Buổi 7 đã giới thiệu Native Query như một lựa chọn song song với JPQL. Mục này đi sâu hơn vào câu hỏi cốt lõi: khi nào JPQL — vốn đã đủ dùng cho hầu hết nghiệp vụ — thật sự "bó tay" và bắt buộc phải hạ xuống viết SQL thuần.

**1. Bản chất kỹ thuật:**

- **A.** Khi `nativeQuery = true`, Hibernate **không hề dịch** câu lệnh — chuỗi SQL được gửi thẳng, nguyên văn, xuống trình điều khiển MySQL.
- **B.** Câu lệnh phải dùng đúng **tên bảng, tên cột vật lý** (ví dụ `tour_name`), không phải tên class hay tên thuộc tính Java như JPQL.
- **C.** Vì không qua lớp dịch của JPA, Native Query có thể dùng **mọi hàm, mọi cú pháp đặc thù của MySQL** mà JPQL hoàn toàn không hỗ trợ.
- **D.** Kết quả trả về được Hibernate cố gắng ánh xạ lại thành Entity nếu các cột `SELECT` khớp đủ với Entity; nếu chỉ lấy một phần cột hoặc dữ liệu đã tổng hợp (như `COUNT`, `GROUP BY`), kiểu trả về phải là `Object[]` hoặc một interface chiếu (projection), không thể là Entity đầy đủ.

**2. Giải quyết bài toán gì?:**

- JPQL chỉ hiểu các phép toán, hàm được đặc tả chuẩn trong JPA — không có khái niệm về hàm xử lý ngày tháng đặc thù của MySQL, không có các chỉ thị tối ưu hóa dành riêng cho một engine cụ thể.
- Với các báo cáo cần nhóm dữ liệu theo tháng, định dạng chuỗi theo kiểu riêng của MySQL, hay các câu truy vấn đã được đội DBA tối ưu sẵn bằng tay, JPQL không đủ biểu đạt — đây chính là lúc cần Native Query.
- Buổi 7 cũng đã gặp một trường hợp buộc dùng Native Query: tìm kiếm xuyên hai bảng không có quan hệ đối tượng JPA.

**3. Lưu ý & Best Practices:**

- Mất khả năng chuyển đổi cơ sở dữ liệu: câu lệnh viết cho MySQL có thể không chạy được nếu công ty đổi sang PostgreSQL hay Oracle.
- Luôn dùng tham số có tên (`:tenThamSo` cùng `@Param`), **tuyệt đối không** tự nối chuỗi giá trị người dùng nhập vào ngay trong câu SQL — đây là nguyên nhân trực tiếp dẫn đến lỗ hổng SQL Injection.
- Vì là một chuỗi văn bản thuần, lỗi cú pháp trong Native Query **chỉ lộ ra khi chạy thực tế**, không được kiểm tra trước ở thời điểm biên dịch.

**4. Demo Thực chiến:**

```java
@Query(value = "SELECT DATE_FORMAT(ngay_xuat_phat, '%Y-%m') AS thang, COUNT(*) AS so_luong FROM lich_trinh GROUP BY DATE_FORMAT(ngay_xuat_phat, '%Y-%m')", nativeQuery = true)
List<Object[]> countSchedulesByMonth();
```

Giải thích:

- **A.** `DATE_FORMAT(ngay_xuat_phat, '%Y-%m')` là một hàm **đặc thù của MySQL**, dùng để quy một ngày cụ thể về dạng "năm-tháng" — JPQL không có hàm tương đương.
- **B.** `GROUP BY` kết hợp `COUNT(*)` tạo ra dữ liệu tổng hợp (thống kê số lịch trình theo từng tháng), không còn khớp với cấu trúc một `LichTrinh` đầy đủ.
- **C.** Vì kết quả không còn là một `LichTrinh` nguyên vẹn, kiểu trả về bắt buộc phải là `List<Object[]>` — mỗi phần tử `Object[]` ứng với một dòng kết quả, phần tử `[0]` là tháng, phần tử `[1]` là số lượng.

Input: gọi `countSchedulesByMonth()` khi bảng `lich_trinh` có các dòng vào tháng 3 và tháng 4 năm 2026.

Output:

```text
["2026-03", 2]
["2026-04", 1]
```

### V. Dynamic query

Tính năng tìm kiếm tour trong thực tế hiếm khi cố định: có lúc người dùng chỉ gõ tên tour, có lúc chỉ chọn nơi đến, có lúc nhập cả hai, có lúc không nhập gì. Một phương thức `@Query` với điều kiện `WHERE` cố định không thể phục vụ được tất cả các tổ hợp đó cùng lúc.

**1. Bản chất kỹ thuật:**

- **A.** Bài toán cốt lõi: mỗi điều kiện lọc là **tùy chọn** — có thể có, có thể không, và không biết trước tổ hợp nào sẽ xảy ra.
- **B.** Kỹ thuật được khuyến nghị cho các trường hợp phức tạp là **JPA Specification** (thông qua `JpaSpecificationExecutor<T>`) — xây dựng điều kiện `WHERE` bằng code Java tại thời điểm chạy, thay vì một chuỗi JPQL cố định viết sẵn.
- **C.** Với các trường hợp không quá phức tạp (vài điều kiện lọc), có một kỹ thuật gọn hơn, dùng ngay trong JPQL: mẫu `(:thamSo IS NULL OR cot = :thamSo)`.
- **D.** Cơ chế của mẫu trên: nếu `thamSo` được truyền vào là `null`, vế `:thamSo IS NULL` cho kết quả đúng, khiến toàn bộ điều kiện `OR` luôn đúng — tức điều kiện lọc đó bị "vô hiệu hóa" một cách có chủ đích.

**2. Giải quyết bài toán gì?:**

- Không có kỹ thuật này, mỗi tổ hợp điều kiện lọc sẽ cần một phương thức Repository riêng (`findByTourName`, `findByDestination`, `findByTourNameAndDestination`...) — số lượng phương thức tăng theo cấp số nhân khi số điều kiện lọc tăng lên, nhanh chóng trở nên không thể quản lý.
- Dynamic query cho phép **một** phương thức duy nhất xử lý được mọi tổ hợp điều kiện.

**3. Lưu ý & Best Practices:**

- Mẫu `(:thamSo IS NULL OR ...)` dễ viết, dễ hiểu, phù hợp với số lượng điều kiện lọc nhỏ — đây là lựa chọn phù hợp với trình độ hiện tại của khóa học.
- JPA Specification mạnh hơn, type-safe hơn, nhưng đòi hỏi làm quen thêm với `CriteriaBuilder`, `Predicate`, `Root` — một chủ đề riêng, xứng đáng có buổi học của chính nó khi hệ thống lọc phức tạp hơn nhiều điều kiện.
- Luôn kiểm tra, làm sạch dữ liệu lọc nhận từ Client trước khi đưa vào truy vấn, dù dùng kỹ thuật nào.

**4. Demo Thực chiến:**

```java
@Query("SELECT t FROM Tour t WHERE (:tourName IS NULL OR t.tourName LIKE %:tourName%) AND (:destination IS NULL OR t.destination = :destination)")
List<Tour> searchTours(@Param("tourName") String tourName, @Param("destination") String destination);
```

Giải thích:

- **A.** Hai điều kiện được nối bằng `AND`, nhưng mỗi điều kiện tự nó lại là một cặp `OR` cho phép "bỏ qua" chính nó khi tham số tương ứng là `null`.
- **B.** `t.tourName LIKE %:tourName%` tìm theo tên tour có chứa chuỗi nhập vào (đã học cú pháp `LIKE` ở Buổi 3).
- **C.** `t.destination = :destination` so khớp chính xác nơi đến.

Input 1: gọi `searchTours("Da Lat", null)`.

Output 1 — chỉ lọc theo tên, điều kiện `destination` tự vô hiệu hóa:

```text
Hibernate: 
    ... where (? is null or t1_0.tour_name like ?) and (? is null or t1_0.destination=?)
```

Trả về mọi tour có tên chứa "Da Lat", bất kể nơi đến.

Input 2: gọi `searchTours(null, null)`.

Output 2 — cả hai điều kiện cùng vô hiệu hóa, tương đương lấy toàn bộ danh sách tour.

### VI. Phòng tránh N + 1 query

Đây là lỗi hiệu năng phổ biến nhất trong các hệ thống dùng JPA, và là hệ quả trực tiếp của cơ chế Lazy đã học ở mục I. Ví dụ cụ thể: lấy danh sách 10 `Tour`, sau đó với mỗi `Tour`, đọc danh sách `LichTrinh` của nó để hiển thị.

**1. Bản chất kỹ thuật:**

- **A.** Bước 1: gọi `tourRepository.findAll()` — sinh ra **1 câu** `SELECT` lấy 10 dòng `Tour`.
- **B.** Bước 2: lặp qua 10 `Tour` đó, với mỗi `Tour` gọi `tour.getLichTrinhs()` — vì quan hệ này là `LAZY`, **mỗi lần gọi** trên một `Tour` khác nhau sẽ kích hoạt Proxy của chính `Tour` đó, bắn ra **1 câu** `SELECT` riêng.
- **C.** Tổng cộng: 1 câu lấy danh sách Tour, cộng thêm 10 câu lấy LichTrinh tương ứng từng Tour — đúng 11 câu, công thức tổng quát là **1 + N** câu lệnh cho N dòng dữ liệu cha.
- **D.** First-level Cache (mục III) **không** giúp được ở đây, vì mỗi `Tour` có `id` khác nhau — không có Entity nào bị gọi lặp lại trùng khóa chính để cache phát huy tác dụng.

**2. Giải quyết bài toán gì?:**

- Với 10 Tour, 11 câu lệnh vẫn chạy được, chỉ chậm hơn bình thường một chút.
- Với 10.000 Tour trong một hệ thống thật, con số trở thành 10.001 lượt gọi xuống database — mỗi lượt gọi đều tốn thời gian do độ trễ mạng (network latency) cộng dồn, dù bản thân mỗi câu truy vấn đơn lẻ rất nhanh.
- Số lượng kết nối (connection) trong HikariCP Pool (đã học ở Buổi 7) là có hạn — hàng nghìn câu truy vấn dồn dập cùng lúc có thể khiến các Request khác phải xếp hàng chờ kết nối rảnh, kéo sập hiệu năng toàn hệ thống, không chỉ riêng chức năng đang bị N + 1.

**3. Lưu ý & Best Practices:**

- Cách phát hiện: bật `show-sql=true` (đã cấu hình từ Buổi 7), nếu thấy **cùng một mẫu câu** `SELECT ... FROM lich_trinh WHERE ma_tour=?` lặp lại nhiều lần liên tiếp với giá trị `?` khác nhau ngay sau một `findAll()`, đó chính là dấu hiệu của N + 1.
- Chuyển quan hệ sang `EAGER` **không phải** cách sửa đúng — nó chỉ gộp vấn đề vào ngay câu truy vấn đầu tiên, dễ kéo theo trùng lặp dữ liệu dạng tích Descartes khi quan hệ là danh sách.
- Cách sửa đúng là chủ động yêu cầu Hibernate tải sẵn dữ liệu liên quan trong **cùng một** câu truy vấn, ngay tại truy vấn cần đến nó — chính là nội dung của Phần 4.

**4. Demo Thực chiến:**

```java
public void printAllToursWithSchedules() {
    List<Tour> tours = tourRepository.findAll();
    for (Tour tour : tours) {
        tour.getLichTrinhs().size();
    }
}
```

Giải thích:

- **A.** `tourRepository.findAll()`: đúng 1 câu truy vấn, lấy toàn bộ `Tour`.
- **B.** Vòng lặp `for` chạy qua từng `Tour`. `tour.getLichTrinhs().size()` chỉ nhằm mục đích chạm vào Proxy để minh họa, buộc nó phải tải dữ liệu thật.
- **C.** Vì `lichTrinhs` là `LAZY` (mục I), mỗi lần "chạm" vào một `Tour` khác nhau trong vòng lặp kích hoạt một câu `SELECT` độc lập cho đúng `Tour` đó.

Input: gọi `printAllToursWithSchedules()` khi bảng `tours` có 3 dòng.

Output — Console in ra đúng mẫu "1 + N", ở đây là 1 + 3 = 4 câu:

```text
Hibernate: select t1_0.id, ... from tours t1_0
Hibernate: select l1_0.ma_tour, ... from lich_trinh l1_0 where l1_0.ma_tour=?
Hibernate: select l1_0.ma_tour, ... from lich_trinh l1_0 where l1_0.ma_tour=?
Hibernate: select l1_0.ma_tour, ... from lich_trinh l1_0 where l1_0.ma_tour=?
```

Ba câu cuối giống hệt nhau về cấu trúc, chỉ khác giá trị tham số — đây chính là dấu hiệu nhận diện N + 1 đã nêu ở mục Lưu ý.

---

## Phần 2: Tìm hiểu về Validation

### I. Cơ chế Validation trong Spring Boot

Entity `Tour` hiện tại không có bất kỳ ràng buộc nào ở tầng Java: Client hoàn toàn có thể gửi lên một `tourName` rỗng, và `createTour` ở Service vẫn vô tư lưu xuống database. Vấn đề chỉ lộ ra khi dữ liệu rác đã nằm sẵn trong bảng.

**1. Bản chất kỹ thuật:**

- **A.** Cơ chế Validation của Spring Boot dựa trên chuẩn **Bean Validation** (đặc tả JSR 380), được hiện thực bởi thư viện **Hibernate Validator**.
- **B.** Các ràng buộc được khai báo bằng annotation ngay trên thuộc tính của Entity hoặc DTO: `@NotNull`, `@NotBlank`, `@Size`, `@Min`, `@Max`...
- **C.** `@Valid` đặt trước tham số nhận dữ liệu ở Controller (ví dụ `@Valid @RequestBody Tour tour`) là nơi **kích hoạt** việc kiểm tra — thiếu `@Valid`, mọi annotation ràng buộc phía trên hoàn toàn bị bỏ qua, không có tác dụng gì.
- **D.** Việc kiểm tra diễn ra **trước khi** thân phương thức Controller được thực thi, ngay tại bước Spring chuẩn bị tham số cho phương thức (đúng công đoạn `DispatcherServlet` xử lý Request đã học ở Buổi 6).
- **E.** Nếu phát hiện vi phạm, Spring tự động ném `MethodArgumentNotValidException`, phương thức Controller **không bao giờ được gọi tới**.

**2. Giải quyết bài toán gì?:**

- Không có Validation khai báo, Service hoặc Controller phải tự viết hàng loạt câu lệnh `if` kiểm tra thủ công cho từng trường dữ liệu, dễ thiếu sót, dễ trùng lặp logic ở nhiều nơi khác nhau khi cùng một Entity được dùng ở nhiều API.
- Validation gom toàn bộ quy tắc ràng buộc vào đúng một nơi — ngay trên định nghĩa dữ liệu — giúp quy tắc luôn nhất quán dù được gọi từ API nào.

**3. Lưu ý & Best Practices:**

- Spring Boot 3 đã **tách riêng** Validation ra khỏi `spring-boot-starter-web` — bắt buộc phải khai báo thêm dependency `spring-boot-starter-validation` trong `pom.xml`, nếu không mọi annotation `@NotBlank`... sẽ không hoạt động, dù có `@Valid` hay không.
- Phân biệt dễ nhầm giữa ba annotation kiểm tra chuỗi rỗng:

| Annotation  | Chặn `null` | Chặn chuỗi rỗng `""` | Chặn chuỗi chỉ có khoảng trắng `"   "` |
| ----------- | ----------- | -------------------- | -------------------------------------- |
| `@NotNull`  | Có          | Không                | Không                                  |
| `@NotEmpty` | Có          | Có                   | Không                                  |
| `@NotBlank` | Có          | Có                   | Có                                     |

- Ràng buộc `nullable = false` trên `@Column` (đã học ở Buổi 7) vẫn nên giữ song song với `@NotBlank` — `@Column` là lớp bảo vệ cuối cùng ở chính database, còn Validation là lớp bảo vệ sớm, phản hồi lỗi thân thiện ngay tại tầng ứng dụng, trước khi kịp chạm tới database.
- Quên gắn `@Valid` ở Controller là lỗi cực kỳ phổ biến: toàn bộ annotation ràng buộc phía Entity/DTO lúc đó chỉ còn là trang trí.

**4. Demo Thực chiến:**

Thêm dependency trong `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

Entity `Tour` bổ sung ràng buộc:

```java
package com.example.quanlytour.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "tours")
@Data
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ten tour khong duoc de trong")
    @Column(name = "tour_name", nullable = false, length = 200)
    private String tourName;

    @Column(name = "departure_location", length = 100)
    private String departureLocation;

    @Column(name = "destination", length = 100)
    private String destination;

    @Column(name = "description", length = 1000)
    private String description;

}
```

Controller bổ sung `@Valid`:

```java
@PostMapping
public Tour createTour(@Valid @RequestBody Tour tour) {
    return tourService.createTour(tour);
}
```

Giải thích:

- **A.** `@NotBlank(message = "...")` gắn trực tiếp lên `tourName`: nếu giá trị là `null`, rỗng, hoặc chỉ có khoảng trắng, ràng buộc này bị vi phạm. `message` là nội dung sẽ xuất hiện trong lỗi trả về.
- **B.** `@Valid` trên tham số `tour` của `createTour`: ra lệnh cho Spring kiểm tra toàn bộ ràng buộc khai báo trên object `tour` **trước khi** dòng `return tourService.createTour(tour);` có cơ hội chạy.

Input: Postman gửi `POST /api/tours` với Body:

```json
{
    "tourName": "",
    "destination": "Da Lat"
}
```

Output — mã trạng thái `400 Bad Request`. Thân phương thức `createTour` **không hề chạy** — `tourService.createTour` không được gọi, không có dòng nào được lưu xuống database. Nội dung lỗi mặc định của Spring khi chưa có Phần 3 sẽ rất dài dòng và lộ nhiều chi tiết kỹ thuật nội bộ không cần thiết, đây chính là động lực dẫn sang Phần 3.

---

## Phần 3: Tìm hiểu về Exception Handling

### I. Kiến trúc xử lý lỗi tập trung

Hiện tại, bất kỳ Exception nào bị ném ra từ Controller — dù là lỗi Validation ở Phần 2, lỗi "không tìm thấy" từ `getTourById` (Buổi 7), hay bất kỳ lỗi không lường trước nào — đều rơi vào cùng một trang lỗi mặc định xấu xí của Spring Boot, không thống nhất, đôi khi lộ cả chi tiết nội bộ hệ thống.

**1. Bản chất kỹ thuật:**

- **A.** `@RestControllerAdvice` (kết hợp `@ControllerAdvice` và `@ResponseBody`) đánh dấu một class đóng vai trò **lưới lọc toàn cục**, đứng chắn phía ngoài **mọi** `@RestController` trong ứng dụng.
- **B.** Bên trong class đó, mỗi phương thức đánh dấu `@ExceptionHandler(LoaiException.class)` là một "trạm xử lý" dành riêng cho đúng một loại Exception.
- **C.** Khi bất kỳ Controller nào ném ra một Exception khớp loại đã đăng ký, Spring **tự động chuyển hướng** luồng xử lý sang đúng phương thức `@ExceptionHandler` tương ứng, thay vì để Exception rơi thẳng xuống Tomcat.
- **D.** Cơ chế đứng sau là cùng một nguyên lý "bọc proxy" đã gặp ở `@Transactional` (Buổi 7): Spring chạy mọi phương thức Controller bên trong một lớp bọc có khả năng bắt Exception và định tuyến nó.

**2. Giải quyết bài toán gì?:**

- Không có lưới lọc tập trung, mỗi phương thức Controller phải tự viết `try-catch` riêng, lặp lại cùng một đoạn code định dạng JSON lỗi ở hàng chục nơi khác nhau.
- Tệ hơn, bất kỳ Exception nào bị bỏ sót không bắt sẽ lộ thẳng ra ngoài dưới dạng trang lỗi mặc định của Spring, định dạng không thống nhất với các lỗi khác, đôi khi kèm theo thông tin nội bộ không nên công khai cho Client.
- `@RestControllerAdvice` giải quyết cả hai vấn đề: một nơi duy nhất định nghĩa toàn bộ cách xử lý lỗi, áp dụng tự động cho mọi Controller.

**3. Lưu ý & Best Practices:**

- Khi một Exception khớp nhiều `@ExceptionHandler` (do quan hệ kế thừa giữa các lớp Exception), Spring luôn ưu tiên phương thức khai báo **loại cụ thể nhất**.
- Luôn có một `@ExceptionHandler(Exception.class)` làm lưới bắt cuối cùng — đảm bảo tuyệt đối không có Exception nào lọt ra ngoài dưới dạng lỗi mặc định xấu xí của Spring.
- Trả đúng mã trạng thái HTTP theo từng loại lỗi (`400` cho dữ liệu sai, `404` cho không tìm thấy, `500` cho lỗi không lường trước) — không nên trả `200 OK` kèm một object lỗi, và cũng không nên trả tất cả về `500`.
- Giữ cấu trúc JSON trả về **thống nhất** giữa mọi `@ExceptionHandler` (cùng tên trường `status`, `message`), để code phía Client xử lý lỗi được đơn giản, nhất quán.

**4. Demo Thực chiến:**

```java
package com.example.quanlytour.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", message);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException exception) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("message", exception.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception exception) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("message", exception.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
```

Giải thích:

- **A.** `handleValidationException` bắt đúng `MethodArgumentNotValidException` — loại Exception được Spring tự ném ra ở Phần 2 khi `@Valid` phát hiện vi phạm. `exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage()` lấy ra đúng nội dung `message` đã khai báo trong `@NotBlank`.
- **B.** `handleIllegalArgumentException` bắt đúng loại Exception mà `getTourById` ở Buổi 7 đã ném khi không tìm thấy tour, trả về mã `404` thay vì mã `500` mặc định trước đây.
- **C.** `handleGeneralException` là lưới bắt cuối cùng, dành cho mọi Exception không khớp hai phương thức phía trên.
- **D.** Cả ba phương thức đều trả về cùng cấu trúc `Map` với hai khóa `status` và `message`, đảm bảo định dạng JSON lỗi thống nhất.

Input: gửi lại đúng Request ở Phần 2 (Body có `tourName` rỗng).

Output — mã trạng thái vẫn là `400 Bad Request`, nhưng nội dung JSON giờ gọn gàng, thống nhất:

```json
{
    "status": 400,
    "message": "Ten tour khong duoc de trong"
}
```

---

## Phần 4: Override default fetch plan với JOIN FETCH / Entity Graph

### I. Sử dụng JOIN FETCH trong JPQL

Mục VI của Phần 1 đã chỉ ra chính xác vấn đề: lấy danh sách `Tour` rồi lặp qua để đọc `LichTrinh` của từng `Tour` sinh ra 1 + N câu truy vấn. Mục này là lời giải đầu tiên cho đúng vấn đề đó.

**1. Bản chất kỹ thuật:**

- **A.** `JOIN` thường trong JPQL (không có `FETCH`) chỉ dùng để **lọc điều kiện** — nó không ép Hibernate tải sẵn dữ liệu của bảng được nối, các trường Lazy liên quan vẫn là Proxy rỗng như chưa từng có `JOIN`.
- **B.** `JOIN FETCH` bổ sung thêm chỉ thị: "tải luôn cả dữ liệu thật của bảng được nối, gộp chung vào **cùng một** câu `SELECT`".
- **C.** Kết quả là chỉ **một** câu lệnh SQL duy nhất, có `JOIN` thật sự, trả về đủ cột của cả hai bảng trong cùng một lượt gọi xuống database.
- **D.** Cách viết này chỉ ghi đè kiểu tải dữ liệu cho **đúng một câu truy vấn cụ thể**, không làm thay đổi `FetchType` mặc định đã khai báo trên Entity (`LAZY` vẫn là `LAZY` ở mọi nơi khác).

**2. Giải quyết bài toán gì?:**

- Thay vì 1 câu lấy `Tour` cộng thêm N câu lấy `LichTrinh` tương ứng từng `Tour`, `JOIN FETCH` gộp tất cả thành đúng 1 câu lệnh — giải quyết triệt để N + 1 cho đúng trường hợp cần dữ liệu liên quan ngay lập tức.

**3. Lưu ý & Best Practices:**

- Kết hợp `JOIN FETCH` trên một quan hệ dạng danh sách (`to-many`) cùng lúc với `Pageable` là một tổ hợp **có vấn đề đã biết**: Hibernate không thể áp dụng đúng `LIMIT`/`OFFSET` trên kết quả đã bị nhân bản dòng do `JOIN`, nên tránh dùng chung hai kỹ thuật này trên cùng một truy vấn.
- Với quan hệ `to-many`, kết quả SQL thô sẽ có **nhiều dòng trùng lặp phía bảng cha** (một dòng cho mỗi bản ghi con) — Hibernate tự loại trùng khi ánh xạ lại thành danh sách Entity, không cần tự xử lý tay.
- Chỉ nên `JOIN FETCH` đúng những quan hệ thật sự cần dùng ngay ở truy vấn đó — lạm dụng cho mọi truy vấn triệt tiêu hoàn toàn lợi ích của Lazy.

**4. Demo Thực chiến:**

```java
@Query("SELECT t FROM Tour t JOIN FETCH t.lichTrinhs WHERE t.id = :id")
Tour findTourWithSchedules(@Param("id") Long id);
```

Giải thích:

- **A.** `JOIN FETCH t.lichTrinhs`: nối sang quan hệ `lichTrinhs` của `Tour`, đồng thời ép tải dữ liệu thật của nó ngay trong câu lệnh này.
- **B.** `WHERE t.id = :id` vẫn hoạt động như một điều kiện JPQL bình thường, lọc đúng một `Tour`.

Input: gọi `findTourWithSchedules(1L)`, tour này có 2 `LichTrinh`.

Output — đúng **một** câu SQL duy nhất:

```text
Hibernate: 
    select t1_0.id, t1_0.tour_name, l1_0.ma_tour, l1_0.id, l1_0.gia_co_ban, l1_0.ngay_xuat_phat 
    from tours t1_0 
    join lich_trinh l1_0 on t1_0.id=l1_0.ma_tour 
    where t1_0.id=?
```

So với mục VI (1 + N = 4 câu cho 3 Tour và các LichTrinh liên quan), ở đây dù `Tour` có bao nhiêu `LichTrinh` đi nữa, số câu lệnh luôn cố định là 1.

### II. Sử dụng @EntityGraph

`JOIN FETCH` đòi hỏi tự viết một câu JPQL hoàn chỉnh. `@EntityGraph` là cách đạt được cùng mục tiêu mà không cần viết JPQL, áp dụng trực tiếp lên các phương thức Repository, kể cả những phương thức đã có sẵn từ `JpaRepository`.

**1. Bản chất kỹ thuật:**

- **A.** `@EntityGraph(attributePaths = {"tenThuocTinh"})` đặt trên một phương thức Repository, khai báo thuộc tính Lazy nào cần được tải Eager **chỉ riêng cho lần gọi phương thức đó**.
- **B.** Có thể dùng để "ghi đè" ngay cả phương thức đã được `JpaRepository` cung cấp sẵn (`findAll`, `findById`), bằng cách khai báo lại chữ ký phương thức đó trong interface Repository.
- **C.** Về bản chất thực thi, Spring Data JPA chuyển `attributePaths` thành một gợi ý (hint) cho Hibernate, và Hibernate dịch gợi ý đó thành một câu SQL có `JOIN` — cùng một kết quả cuối cùng như `JOIN FETCH`, chỉ khác cách khai báo.

**2. Giải quyết bài toán gì?:**

- Với các trường hợp chỉ cần "tải Eager thêm một quan hệ cho đúng một truy vấn", không có điều kiện lọc phức tạp nào khác, `@EntityGraph` ngắn gọn hơn hẳn so với việc viết lại nguyên một câu JPQL chỉ để thêm `JOIN FETCH`.

**3. Lưu ý & Best Practices:**

- Có thể khai báo nhiều thuộc tính cùng lúc, cách nhau bằng dấu phẩy trong `attributePaths`, để tải Eager nhiều quan hệ trong một lần.
- Phù hợp nhất cho các trường hợp đơn giản: tải kèm quan hệ cho `findAll()`/`findById()`. Khi cần thêm điều kiện lọc phức tạp đi kèm, `JOIN FETCH` (mục I) vẫn là lựa chọn linh hoạt hơn.
- Không nên gắn `@EntityGraph` cho mọi phương thức một cách mặc định — cùng nguyên tắc với `JOIN FETCH`, chỉ áp dụng đúng nơi thật sự cần dữ liệu liên quan ngay lập tức.

**4. Demo Thực chiến:**

```java
@EntityGraph(attributePaths = {"lichTrinhs"})
List<Tour> findAll();
```

Giải thích:

- **A.** Khai báo lại `findAll()` — vốn đã có sẵn từ `JpaRepository` — ngay trong `TourRepository`, kèm `@EntityGraph`.
- **B.** Từ thời điểm này, mọi lời gọi `tourRepository.findAll()` trong toàn bộ ứng dụng sẽ tự động tải kèm `lichTrinhs`, không cần sửa bất kỳ nơi nào khác đang gọi `findAll()`.

Input: gọi `tourRepository.findAll()` khi có 3 `Tour`, mỗi `Tour` có từ 1 đến 2 `LichTrinh`.

Output — một câu SQL duy nhất có `JOIN`, thay vì 1 + 3 = 4 câu như minh họa N + 1 ở Phần 1, mục VI:

```text
Hibernate: 
    select t1_0.id, t1_0.tour_name, l1_0.ma_tour, l1_0.id, l1_0.gia_co_ban 
    from tours t1_0 
    left join lich_trinh l1_0 on t1_0.id=l1_0.ma_tour
```

---

## Phần 5: Bài tập chuẩn bị (ứng dụng vào Project Quản lý Tour)

Một điều chỉnh cần nói rõ trước khi bắt đầu: đề bài gốc yêu cầu ràng buộc "Giá phải > 0" ngay trên `Tour`. Theo đúng sơ đồ E-R đã chốt lại ở Buổi 7, `Tour` không còn thuộc tính giá — giá (`basePrice`) nay thuộc về `LichTrinh`. Bài tập dưới đây áp dụng ràng buộc tên tour lên `Tour`, và áp dụng ràng buộc giá lên đúng nơi dữ liệu giá thực sự tồn tại là `LichTrinh`, để không phá vỡ lại thiết kế đã thống nhất.

### I. Yêu cầu 1 (Áp dụng Pagination)

**Mục tiêu:** `GET /api/tours` hỗ trợ phân trang qua `page` và `size`.

- **A.** Mở `TourService.java`, sửa phương thức `getAllTours`:

```java
public Page<Tour> getAllTours(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return tourRepository.findAll(pageable);
}
```

- **B.** Thêm hai dòng `import` cần thiết ở đầu `TourService.java`:

```java
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
```

- **C.** Mở `TourController.java`, sửa phương thức `getAllTours`:

```java
@GetMapping
public Page<Tour> getAllTours(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
    return tourService.getAllTours(page, size);
}
```

- **D.** Giải thích: `@RequestParam(defaultValue = "0")` nghĩa là nếu Client không truyền `page`, mặc định lấy trang đầu tiên (`0`). Tương tự, không truyền `size` thì mặc định mỗi trang 5 dòng.
- **E.** Lưu lại cả hai file, dừng ứng dụng (nút Stop) rồi chạy lại (nút tam giác xanh), vì code vừa thay đổi.

**Kiểm thử trên Postman:**

- **A.** Tạo Request mới, Method `GET`, địa chỉ: `http://localhost:8080/api/tours?page=0&size=5`.
- **B.** Không cần vào tab Body — đây là `GET`.
- **C.** Bấm **Send**.

**Output** — JSON trả về không còn là một mảng đơn thuần, mà là một object chứa cả dữ liệu lẫn thông tin phân trang:

```json
{
    "content": [
        {"id": 1, "tourName": "Da Lat 3N2D", "destination": "Da Lat"},
        {"id": 2, "tourName": "Sa Pa 2N1D", "destination": "Sa Pa"}
    ],
    "totalElements": 2,
    "totalPages": 1,
    "number": 0,
    "size": 5,
    "numberOfElements": 2,
    "first": true,
    "last": true
}
```

- `content`: danh sách tour của đúng trang hiện tại.
- `totalElements`: tổng số tour trong toàn bộ bảng, không chỉ trang hiện tại.
- `totalPages`: tổng số trang, tính từ `totalElements` chia cho `size`.
- `number`: số thứ tự trang hiện tại (bắt đầu từ `0`).
- `first`, `last`: cho biết trang hiện tại có phải trang đầu/trang cuối hay không.

### II. Yêu cầu 2 (Áp dụng Validation)

**Mục tiêu:** tên tour bắt buộc có khi tạo `Tour`; giá cơ bản bắt buộc lớn hơn 0 khi tạo `LichTrinh`.

- **A.** Mở `pom.xml`, thêm dependency sau vào bên trong thẻ `<dependencies>`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

- **B.** Bấm biểu tượng **Load Maven Changes** (chữ `m` có mũi tên xoay, góc trên bên phải file `pom.xml`) để tải dependency mới, đợi thanh tiến trình ở góc dưới bên phải chạy xong.
- **C.** Mở `Tour.java` (package `entity`), thêm `@NotBlank` lên `tourName` và thêm dòng `import`:

```java
import jakarta.validation.constraints.NotBlank;
```

```java
@NotBlank(message = "Ten tour khong duoc de trong")
@Column(name = "tour_name", nullable = false, length = 200)
private String tourName;
```

- **D.** Mở `LichTrinh.java` (package `entity`), thêm ràng buộc lên `basePrice` và thêm dòng `import`:

```java
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
```

```java
@NotNull(message = "Gia co ban khong duoc de trong")
@Positive(message = "Gia co ban phai lon hon 0")
@Column(name = "gia_co_ban", nullable = false)
private Double basePrice;
```

- **E.** Giải thích: `@Positive` chặn mọi giá trị nhỏ hơn hoặc bằng `0`, đúng yêu cầu "giá phải > 0". Dùng chung với `@NotNull` vì bản thân `@Positive` không chặn được giá trị `null`.
- **F.** Mở `TourController.java`, thêm `@Valid` vào phương thức `createTour`:

```java
@PostMapping
public Tour createTour(@Valid @RequestBody Tour tour) {
    return tourService.createTour(tour);
}
```

- **G.** Mở `LichTrinhController.java`, thêm `@Valid` vào phương thức `createLichTrinh`:

```java
@PostMapping
public LichTrinh createLichTrinh(@Valid @RequestBody LichTrinh lichTrinh) {
    return lichTrinhService.createLichTrinh(lichTrinh);
}
```

- **H.** Thêm dòng `import jakarta.validation.Valid;` vào đầu cả hai file Controller.
- **I.** Lưu toàn bộ file, Stop rồi Run lại ứng dụng.

**Kiểm thử trên Postman — trường hợp sai dữ liệu:**

- **A.** Request mới, Method `POST`, địa chỉ `http://localhost:8080/api/tours`.
- **B.** Tab **Body**, chọn **raw**, chọn **JSON**, dán:

```json
{
    "tourName": "",
    "destination": "Da Lat"
}
```

- **C.** Bấm **Send**.

**Output:** `400 Bad Request`. Vì chưa làm Phần 3 nên nội dung lỗi lúc này vẫn còn dài dòng theo định dạng mặc định của Spring — đây là trạng thái trung gian, sẽ được xử lý tiếp ở Yêu cầu 3.

**Kiểm thử trường hợp giá không hợp lệ:**

- **A.** Request mới, Method `POST`, địa chỉ `http://localhost:8080/api/lich-trinh`.
- **B.** Body:

```json
{
    "maTour": 1,
    "departureDate": "2026-06-10",
    "basePrice": -500000,
    "totalSlots": 10,
    "bookedSlots": 0
}
```

- **C.** Bấm **Send**.

**Output:** `400 Bad Request`, vi phạm ràng buộc `@Positive` trên `basePrice`.

### III. Yêu cầu 3 (Áp dụng Exception Handling)

**Mục tiêu:** chuẩn hóa toàn bộ lỗi ở Yêu cầu 2 về đúng định dạng `{"status": 400, "message": "..."}`.

- **A.** Trong khung Project, chuột phải vào package gốc `com.example.quanlytour`, chọn **New**, chọn **Package**, gõ `exception`, nhấn Enter.
- **B.** Chuột phải vào package `exception` vừa tạo, chọn **New**, chọn **Java Class**, gõ `GlobalExceptionHandler`, nhấn Enter.
- **C.** Xóa nội dung mặc định, dán nguyên văn đoạn code sau:

```java
package com.example.quanlytour.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", message);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException exception) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("message", exception.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalStateException(IllegalStateException exception) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", exception.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception exception) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("message", exception.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
```

- **D.** Giải thích bổ sung so với bản minh họa ở Phần 3: có thêm `handleIllegalStateException`, dùng để bắt đúng lỗi "không đủ chỗ trống" mà `bookSlots` ở Buổi 7 đã khai báo bằng `IllegalStateException`, trả về `400` thay vì rơi vào lưới bắt chung `500`.
- **E.** File này **không cần** khai báo ở bất kỳ đâu khác. `@RestControllerAdvice` tự khiến Spring nhận diện và áp dụng cho toàn bộ Controller ngay khi ứng dụng khởi động, nhờ cơ chế quét Component đã học ở Buổi 6.
- **F.** Lưu file, Stop rồi Run lại ứng dụng.

**Kiểm thử lại trên Postman:**

- **A.** Lặp lại đúng Request ở Yêu cầu 2 (`POST /api/tours` với `tourName` rỗng).
- **B.** Bấm **Send**.

**Output:**

```json
{
    "status": 400,
    "message": "Ten tour khong duoc de trong"
}
```

- **C.** Lặp lại Request `POST /api/lich-trinh` với `basePrice` âm.

**Output:**

```json
{
    "status": 400,
    "message": "Gia co ban phai lon hon 0"
}
```

- **D.** Thử lại cả API lấy chi tiết một tour không tồn tại từ Buổi 7 (`GET /api/tours/999`), xác nhận giờ trả về đúng `404` thay vì `500` như trước khi có `GlobalExceptionHandler`:

```json
{
    "status": 404,
    "message": "Khong tim thay tour co id: 999"
}
```

Hoàn thành ba yêu cầu trên, API `GET /api/tours` đã sẵn sàng phục vụ dữ liệu lớn qua phân trang, dữ liệu đầu vào đã được chặn ngay từ cổng vào trước khi chạm tới tầng nghiệp vụ, và mọi lỗi của hệ thống — dù phát sinh từ Validation, từ nghiệp vụ, hay từ bất kỳ đâu không lường trước — đều trả về đúng một định dạng JSON thống nhất, chuyên nghiệp.