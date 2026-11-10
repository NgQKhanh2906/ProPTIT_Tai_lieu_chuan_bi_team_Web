**Nguyễn Quốc Khánh**
**MENU**
- [Buổi 3: Thao tác Database, tối ưu JPA, Validation và Exception Handling](#buổi-3-thao-tác-database-tối-ưu-jpa-validation-và-exception-handling)
  - [I. Các mối quan hệ trong JPA](#i-các-mối-quan-hệ-trong-jpa)
    - [A. Quan hệ One-To-Many và Many-To-One (1..N)](#a-quan-hệ-one-to-many-và-many-to-one-1n)
    - [B. Quan hệ Many-To-Many (N..N)](#b-quan-hệ-many-to-many-nn)
  - [II. Các mức độ truy vấn trong Spring Data JPA](#ii-các-mức-độ-truy-vấn-trong-spring-data-jpa)
    - [A. Derived Query Methods (Truy vấn theo tên hàm)](#a-derived-query-methods-truy-vấn-theo-tên-hàm)
    - [B. JPQL với @Query](#b-jpql-với-query)
    - [C. Native Query (SQL thuần)](#c-native-query-sql-thuần)
    - [D. Dynamic Query và EntityManager](#d-dynamic-query-và-entitymanager)
  - [III. Các phương pháp tối ưu JPA](#iii-các-phương-pháp-tối-ưu-jpa)
    - [A. Lazy / Eager initialization](#a-lazy--eager-initialization)
    - [B. Pagination](#b-pagination)
    - [C. Caching](#c-caching)
    - [D. Vấn đề N + 1 Query và Cách phòng tránh](#d-vấn-đề-n--1-query-và-cách-phòng-tránh)
  - [IV. Validation và Exception Handling](#iv-validation-và-exception-handling)
    - [A. Cơ chế Validation trong Spring Boot](#a-cơ-chế-validation-trong-spring-boot)
    - [B. Kiến trúc xử lý lỗi tập trung (Exception Handling)](#b-kiến-trúc-xử-lý-lỗi-tập-trung-exception-handling)
  - [V. Bài tập thực hành tổng hợp (Dự án Quản lý Tour)](#v-bài-tập-thực-hành-tổng-hợp-dự-án-quản-lý-tour)
    - [A. Yêu cầu 1 (Thiết lập quan hệ)](#a-yêu-cầu-1-thiết-lập-quan-hệ)
      - [Bước 1: Xác định Owning side trước khi code](#bước-1-xác-định-owning-side-trước-khi-code)
      - [Bước 2: Cập nhật Entity `Tour`](#bước-2-cập-nhật-entity-tour)
      - [Bước 3: Cập nhật Entity `HoaDon`](#bước-3-cập-nhật-entity-hoadon)
      - [Bước 4: Cập nhật Entity `KhachHang`](#bước-4-cập-nhật-entity-khachhang)
      - [Bước 5: Viết nghiệp vụ mua vé để kiểm chứng hai quan hệ](#bước-5-viết-nghiệp-vụ-mua-vé-để-kiểm-chứng-hai-quan-hệ)
      - [Bước 6: Dữ liệu mẫu và kiểm tra kết quả](#bước-6-dữ-liệu-mẫu-và-kiểm-tra-kết-quả)
    - [B. Yêu cầu 2 (Truy vấn Cơ bản & Nâng cao)](#b-yêu-cầu-2-truy-vấn-cơ-bản--nâng-cao)
      - [Bước 1: Tạo các DTO Response](#bước-1-tạo-các-dto-response)
      - [Bước 2: Derived Query: tìm khách hàng theo tên](#bước-2-derived-query-tìm-khách-hàng-theo-tên)
      - [Bước 3: JPQL: tìm hóa đơn có tổng tiền lớn hơn X](#bước-3-jpql-tìm-hóa-đơn-có-tổng-tiền-lớn-hơn-x)
      - [Bước 4: Native Query: thống kê Tour bằng JOIN](#bước-4-native-query-thống-kê-tour-bằng-join)
    - [C. Yêu cầu 3 (Tối ưu & Phân trang)](#c-yêu-cầu-3-tối-ưu--phân-trang)
      - [Bước 1: Chuẩn bị DTO cho danh sách và chi tiết](#bước-1-chuẩn-bị-dto-cho-danh-sách-và-chi-tiết)
      - [Bước 2: Sửa API danh sách Tour thành phân trang](#bước-2-sửa-api-danh-sách-tour-thành-phân-trang)
      - [Bước 3: API chi tiết Tour kèm Hóa đơn, chống N+1](#bước-3-api-chi-tiết-tour-kèm-hóa-đơn-chống-n1)
      - [Bước 4: Đo lường trước và sau tối ưu](#bước-4-đo-lường-trước-và-sau-tối-ưu)
    - [D. Yêu cầu 4 (Validation & Exception)](#d-yêu-cầu-4-validation--exception)
      - [Bước 1: Tạo Request DTO có Validation](#bước-1-tạo-request-dto-có-validation)
      - [Bước 2: Viết Service tạo Tour](#bước-2-viết-service-tạo-tour)
      - [Bước 3: Thêm endpoint tạo Tour vào Controller](#bước-3-thêm-endpoint-tạo-tour-vào-controller)
      - [Bước 4: Hoàn thiện `GlobalExceptionHandler`](#bước-4-hoàn-thiện-globalexceptionhandler)
      - [Bước 5: Kịch bản kiểm thử bắt buộc](#bước-5-kịch-bản-kiểm-thử-bắt-buộc)
    - [E. Tiêu chí nghiệm thu bài tập](#e-tiêu-chí-nghiệm-thu-bài-tập)

---

# Buổi 3: Thao tác Database, tối ưu JPA, Validation và Exception Handling

> Tài liệu Wiki nội bộ dành cho Fresher - Khóa Spring Boot cơ bản.
> Môi trường áp dụng: Spring Boot 3.x, Spring Data JPA, Hibernate 6.x, Jakarta Persistence, MySQL.

**Lộ trình đọc tài liệu:**

* Mảng Cơ bản (Mục I, Mục II): học cách mô tả quan hệ giữa các bảng và cách lấy dữ liệu ra.
* Mảng Nâng cao (Mục III, Mục IV): học cách lấy dữ liệu nhanh, an toàn, và cách chặn dữ liệu rác, xử lý lỗi gọn gàng.
* Mục V: áp dụng toàn bộ vào dự án "Phần mềm quản lý đặt tour du lịch".

**Chuẩn bị trước khi đọc:**

* Bật log SQL để tự quan sát những gì Hibernate làm "phía sau" trong suốt quá trình học.

```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false
logging.level.org.hibernate.orm.jdbc.bind=TRACE
```

* `show-sql` và `format_sql`: in câu SQL thật mà Hibernate gửi xuống DB, có xuống dòng dễ đọc.
* `open-in-view=false`: tắt cơ chế giữ kết nối DB suốt cả request (sẽ giải thích ở Mục III).
* `logging.level.org.hibernate.orm.jdbc.bind=TRACE`: in giá trị thật của các tham số `?` trong câu SQL.

---

## I. Các mối quan hệ trong JPA

**Bài toán mở đầu:**

* Trong Database, các bảng liên kết với nhau bằng **khóa ngoại** (*Foreign Key*).
* Trong Java, các object liên kết với nhau bằng **tham chiếu** (*Reference*) và **Collection** (`List`, `Set`).
* Hai thế giới này nói hai "ngôn ngữ" khác nhau. JPA là "phiên dịch viên" giúp ta viết `tour.getReviews()` thay vì tự viết `SELECT * FROM review WHERE tour_id = ?`.

### A. Quan hệ One-To-Many và Many-To-One (1..N)

#### 1. Bản chất kỹ thuật

* **Định nghĩa quan hệ 1..N**:
  * Một bản ghi ở bảng A liên kết với nhiều bản ghi ở bảng B.
  * Ví dụ: một `Tour` có nhiều `Review` (đánh giá), nhưng mỗi `Review` chỉ thuộc về đúng một `Tour`.
* **Ở tầng Database, chỉ có MỘT cột khóa ngoại**:
  * Cột `tour_id` nằm ở bảng `review` (phía N).
  * Bảng `tour` (phía 1) hoàn toàn không có cột nào trỏ sang `review`.
* **Owning side (phía sở hữu quan hệ)**:
  * Là Entity "nắm giữ" cột khóa ngoại, trong ví dụ là `Review`.
  * Được đánh dấu bằng `@ManyToOne` kết hợp `@JoinColumn`.
  * Hibernate CHỈ đọc phía này để quyết định giá trị cột `tour_id` khi `INSERT` hoặc `UPDATE`.
* **Inverse side (phía bị tham chiếu / phía nghịch đảo)**:
  * Là Entity không giữ khóa ngoại, trong ví dụ là `Tour`.
  * Được đánh dấu bằng `@OneToMany(mappedBy = "tour")`.
  * `mappedBy` có nghĩa: "Quan hệ này đã được quản lý bởi thuộc tính `tour` bên class `Review`, đừng tạo thêm gì cả".
  * Hibernate bỏ qua phía này khi ghi dữ liệu. Nó chỉ dùng phía này để ĐỌC (load danh sách review của tour).
* **Cơ chế ngầm bên dưới**:
  * Khi khởi động, Hibernate đọc toàn bộ annotation, xây dựng "metamodel" mô tả bảng, cột, quan hệ.
  * Khi `save(review)`, Hibernate lấy `review.getTour().getId()` để điền vào cột `tour_id`.
  * Khi gọi `tour.getReviews()`, Hibernate sinh câu `SELECT ... FROM review WHERE tour_id = ?`.

![Owning side và Inverse side trong quan hệ 1..N](images/01-owning-inverse.png)

#### 2. Giải quyết bài toán gì?

* **Xóa bỏ việc tự quản lý khóa ngoại bằng tay**:
  * Không có JPA: lập trình viên phải tự lưu `Long tourId` trong `Review`, rồi tự viết thêm câu query để lấy `Tour`.
  * Có JPA: chỉ cần `review.getTour().getTenTour()`, Hibernate tự sinh câu `JOIN` hoặc `SELECT` tương ứng.
* **Tránh tạo bảng thừa**:
  * Nếu chỉ khai báo `@OneToMany` mà KHÔNG có `mappedBy`, Hibernate hiểu đây là quan hệ một chiều.
  * Khi đó nó tự sinh thêm một bảng trung gian `tour_reviews` không cần thiết, gây thừa dữ liệu và chậm.
* **Tránh xung đột dữ liệu**:
  * Nếu cả hai phía đều tự nhận là Owning side, Hibernate sẽ cập nhật khóa ngoại hai lần, sinh ra câu `UPDATE` thừa.

#### 3. Lưu ý & Best Practices

* Luôn khai báo `mappedBy` ở phía `@OneToMany` khi làm quan hệ hai chiều (*bidirectional*).
* **Hiểu đúng "chỉ Owning side mới được ghi"**:
  * Sai lầm kinh điển: chỉ gọi `tour.getReviews().add(review)` rồi `save(tour)`.
  * Kết quả: cột `tour_id` trong DB vẫn là `NULL`, vì phía `Review` chưa hề được gán `tour`.
  * Cách đúng: viết hàm tiện ích (*helper method*) đồng bộ CẢ HAI phía, ví dụ `tour.addReview(review)`.
* **Luôn đặt `fetch = FetchType.LAZY` cho `@ManyToOne`**:
  * Mặc định JPA để `@ManyToOne` là `EAGER`, rất dễ gây N+1 Query (xem Mục III).
* **Không dùng Lombok `@Data` trên Entity có quan hệ hai chiều**:
  * `@Data` sinh `toString()`, `hashCode()` gọi qua lại giữa `Tour` và `Review`, gây `StackOverflowError`.
  * Nên dùng `@Getter`, `@Setter` riêng lẻ.
* **Không trả Entity hai chiều trực tiếp ra JSON**:
  * Jackson sẽ serialize `Tour -> reviews -> tour -> reviews...` vô hạn.
  * Giải pháp đúng: dùng DTO cho tầng Controller.
* **Cẩn thận với `CascadeType.ALL` và `orphanRemoval = true`**:
  * Chỉ dùng khi phía N thực sự là "con" của phía 1 (con không thể tồn tại nếu thiếu cha).

#### 4. Demo Thực chiến

```java
@Entity
@Table(name = "tour")
@Getter
@Setter
@NoArgsConstructor
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_tour", nullable = false, length = 255)
    private String tenTour;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    public void addReview(Review review) {
        reviews.add(review);
        review.setTour(this);
    }

    public void removeReview(Review review) {
        reviews.remove(review);
        review.setTour(null);
    }
}
```

```java
@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "noi_dung", length = 1000)
    private String noiDung;

    @Column(name = "so_sao")
    private Integer soSao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;
}
```

```java
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final TourRepository tourRepository;

    @Transactional
    public void addReviewToTour(Long tourId, String noiDung, Integer soSao) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tour id = " + tourId));
        Review review = new Review();
        review.setNoiDung(noiDung);
        review.setSoSao(soSao);
        tour.addReview(review);
    }
}
```

**Giải thích chi tiết:**

* **Class `Tour` (*Inverse side*)**:
  * `@Entity` và `@Table(name = "tour")`: đánh dấu class là một Entity, ánh xạ tới bảng `tour`.
  * `@GeneratedValue(strategy = GenerationType.IDENTITY)`: để DB tự tăng khóa chính (`AUTO_INCREMENT` của MySQL).
  * `@OneToMany(mappedBy = "tour")`: chuỗi `"tour"` là TÊN THUỘC TÍNH Java bên class `Review`, không phải tên cột.
  * `cascade = CascadeType.ALL`: khi lưu hoặc xóa `Tour`, Hibernate tự lưu hoặc xóa các `Review` đi kèm.
  * `orphanRemoval = true`: khi một `Review` bị gỡ khỏi list `reviews`, Hibernate tự sinh câu `DELETE` cho nó.
  * `= new ArrayList<>()`: khởi tạo sẵn để tránh `NullPointerException` khi gọi `addReview()` trên tour mới.
  * `addReview()` và `removeReview()`: helper method đồng bộ hai phía. Dòng `review.setTour(this)` là dòng QUAN TRỌNG NHẤT vì nó cập nhật Owning side.
* **Class `Review` (*Owning side*)**:
  * `@ManyToOne(fetch = FetchType.LAZY)`: nhiều review thuộc một tour, chỉ load tour khi thực sự cần.
  * `@JoinColumn(name = "tour_id", nullable = false)`: khai báo tên cột khóa ngoại trong bảng `review` và bắt buộc không null.
* **Class `ReviewService`**:
  * `@Transactional`: mở một transaction, mọi thay đổi trên Entity được Hibernate tự theo dõi (*Dirty Checking*).
  * `ResourceNotFoundException`: exception nghiệp vụ tự định nghĩa, sẽ được xây dựng chi tiết ở Mục IV.
  * `tour.addReview(review)`: không cần gọi `save()`. Khi method kết thúc, transaction commit và nhờ `cascade` Hibernate tự sinh câu `INSERT INTO review (noi_dung, so_sao, tour_id) VALUES (?, ?, ?)`.

### B. Quan hệ Many-To-Many (N..N)

#### 1. Bản chất kỹ thuật

* **Định nghĩa quan hệ N..N**:
  * Một bản ghi bảng A liên kết nhiều bản ghi bảng B, và ngược lại.
  * Ví dụ: một `Tour` thuộc nhiều `Category` (Biển, Nghỉ dưỡng, Gia đình). Một `Category` chứa nhiều `Tour`.
* **Database quan hệ KHÔNG hỗ trợ trực tiếp N..N**:
  * Không thể nhét nhiều khóa ngoại vào một ô dữ liệu (vi phạm chuẩn 1NF).
  * Giải pháp duy nhất: tách thành bảng trung gian (*Join Table*) chứa hai cột khóa ngoại.
* **JPA tự động sinh bảng trung gian**:
  * Khi gặp `@ManyToMany` + `@JoinTable`, Hibernate tự tạo bảng `tour_category (tour_id, category_id)`.
  * Lập trình viên KHÔNG cần viết Entity cho bảng trung gian này.
* **Owning side và Inverse side trong N..N**:
  * Owning side: phía khai báo `@JoinTable`. Hibernate chỉ đọc phía này để `INSERT`/`DELETE` vào bảng trung gian.
  * Inverse side: phía khai báo `mappedBy`.
  * Có thể chọn bất kỳ phía nào làm Owning side, thường chọn phía hay thao tác thêm/bớt nhất.
* **Cơ chế ghi ngầm**:
  * Thêm một category vào `tour.getCategories()`: Hibernate sinh `INSERT INTO tour_category ...`.
  * Gỡ một category: Hibernate sinh `DELETE FROM tour_category WHERE tour_id = ? AND category_id = ?`.

![Bảng trung gian của quan hệ Many-To-Many](images/02-join-table.png)

#### 2. Giải quyết bài toán gì?

* Mô hình hóa đúng nghiệp vụ "nhiều - nhiều" mà không phải viết thêm Entity trung gian và Repository trung gian.
* **Thao tác thuần hướng đối tượng**:
  * Không có JPA: tự viết `INSERT INTO tour_category`, tự `JOIN` 3 bảng mỗi khi cần lấy danh sách.
  * Có JPA: chỉ cần `tour.getCategories().add(category)`.

#### 3. Lưu ý & Best Practices

* **Dùng `Set` thay vì `List` cho `@ManyToMany`**:
  * Với `List`, khi gỡ 1 phần tử, Hibernate XÓA TOÀN BỘ dòng của tour trong bảng trung gian rồi `INSERT` lại các dòng còn lại.
  * Với `Set`, Hibernate chỉ xóa đúng 1 dòng.
* **TUYỆT ĐỐI không dùng `CascadeType.REMOVE` hoặc `CascadeType.ALL` cho `@ManyToMany`**:
  * Xóa một Tour sẽ kéo theo xóa luôn Category "Biển", làm mất dữ liệu của các Tour khác.
  * Chỉ nên dùng `CascadeType.PERSIST` và `CascadeType.MERGE` nếu thực sự cần.
* **Khi bảng trung gian cần thêm cột (ngày mua, số lượng, giá), KHÔNG dùng `@ManyToMany` nữa**:
  * Phải tạo Entity trung gian riêng, rồi tách thành hai quan hệ `@ManyToOne`.
  * Ví dụ thực tế: `HoaDon` chính là Entity trung gian giữa `KhachHang` và `Tour`.
* **Khi dùng `Set`, phải cài đặt `equals()` và `hashCode()` cẩn thận**:
  * Không dùng `@EqualsAndHashCode` của Lombok trên toàn bộ field.
  * Cách an toàn: so sánh theo `id` và trả về `hashCode` cố định theo class.

#### 4. Demo Thực chiến

```java
@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_danh_muc", nullable = false, unique = true)
    private String tenDanhMuc;

    @ManyToMany(mappedBy = "categories")
    private Set<Tour> tours = new HashSet<>();

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Category category)) {
            return false;
        }
        return id != null && id.equals(category.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
```

```java
@Entity
@Table(name = "tour")
@Getter
@Setter
@NoArgsConstructor
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_tour", nullable = false)
    private String tenTour;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tour_category",
            joinColumns = @JoinColumn(name = "tour_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    public void addCategory(Category category) {
        categories.add(category);
        category.getTours().add(this);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
        category.getTours().remove(this);
    }
}
```

**Giải thích chi tiết:**

* **Class `Tour` (*Owning side*)**:
  * `@ManyToMany`: khai báo quan hệ nhiều - nhiều. Mặc định `fetch = LAZY`.
  * `cascade = {PERSIST, MERGE}`: khi lưu tour mới kèm category mới, category cũng được lưu. Không có `REMOVE`.
  * `@JoinTable(name = "tour_category")`: tên bảng trung gian Hibernate sẽ tạo.
  * `joinColumns = @JoinColumn(name = "tour_id")`: cột trỏ về bảng của CHÍNH class này (`tour`).
  * `inverseJoinColumns = @JoinColumn(name = "category_id")`: cột trỏ về bảng của phía bên kia (`category`).
  * `addCategory()`, `removeCategory()`: đồng bộ cả hai `Set` để dữ liệu trong bộ nhớ luôn nhất quán với DB.
* **Class `Category` (*Inverse side*)**:
  * `@ManyToMany(mappedBy = "categories")`: trỏ về thuộc tính `categories` bên `Tour`.
  * `equals()`: hai category bằng nhau khi cùng `id` khác `null`. Cú pháp `instanceof Category category` là pattern matching của Java 16+.
  * `hashCode()` trả về giá trị cố định theo class: đảm bảo hash không đổi trước và sau khi `persist` (lúc đầu `id` là `null`, sau khi lưu mới có giá trị).
* **Bảng sinh ra ở DB**:
  * `tour (id, ten_tour)`, `category (id, ten_danh_muc)`, `tour_category (tour_id, category_id)`.

**Chuyển tiếp sang mục II:**

* Đến đây ta đã "vẽ" xong bản đồ quan hệ giữa các Entity.
* Câu hỏi tiếp theo: làm sao lấy dữ liệu ra theo đủ loại điều kiện mà nghiệp vụ yêu cầu?
* Spring Data JPA cung cấp 4 mức độ truy vấn, từ đơn giản (tự sinh) đến linh hoạt nhất (tự dựng câu query lúc runtime).

---

## II. Các mức độ truy vấn trong Spring Data JPA

![4 mức độ truy vấn trong Spring Data JPA](images/03-query-levels.png)

### A. Derived Query Methods (Truy vấn theo tên hàm)

#### 1. Bản chất kỹ thuật

* **Định nghĩa**:
  * Chỉ cần KHAI BÁO tên method trong interface Repository theo đúng quy ước, Spring tự sinh câu query.
  * Không cần viết bất kỳ dòng SQL hay JPQL nào.
* **Cơ chế Parser (`PartTree`)**:
  * Khi ứng dụng khởi động, Spring Data quét mọi interface kế thừa `JpaRepository`.
  * Với mỗi method không có `@Query`, class `PartTree` tách tên method thành 2 phần: Subject (`find`, `exists`, `count`, `delete`) và Predicate (sau chữ `By`).
  * Phần Predicate tiếp tục được tách theo từ khóa `And`, `Or`, rồi mỗi phần nhỏ được ánh xạ tới thuộc tính Entity và toán tử.
  * Kết quả cuối cùng được dịch thành JPQL, rồi Hibernate dịch tiếp ra SQL theo dialect của DB.
* Ví dụ quá trình dịch tên hàm `findByNoiDenAndGiaTourLessThan(String noiDen, BigDecimal gia)`:
  * Subject: `find` nghĩa là `SELECT`.
  * Predicate 1: `NoiDen` khớp thuộc tính `noiDen`, toán tử mặc định là `=`.
  * Predicate 2: `GiaTour` + `LessThan` nghĩa là `giaTour < ?`.
  * SQL cuối cùng: `SELECT ... FROM tour WHERE noi_den = ? AND gia_tour < ?`.
* **Các từ khóa hay dùng**:
  * So sánh: `Equals`, `Not`, `LessThan`, `GreaterThan`, `Between`, `IsNull`, `In`.
  * Chuỗi: `Like`, `Containing`, `StartingWith`, `EndingWith`, `IgnoreCase`.
  * Sắp xếp và giới hạn: `OrderBy...Asc/Desc`, `Top10`, `First`.
  * Subject đặc biệt: `existsBy` trả về `boolean`, `countBy` trả về `long`.

![Parser dịch tên method thành SQL](images/04-derived-parser.png)

#### 2. Giải quyết bài toán gì?

* Loại bỏ code lặp đi lặp lại cho 80% các truy vấn đơn giản (tìm theo 1 - 2 điều kiện).
* **Kiểm tra lỗi ngay khi khởi động (*Fail-fast*)**:
  * Nếu gõ sai tên thuộc tính (ví dụ `findByTenTuor`), ứng dụng KHÔNG khởi động được và báo `PropertyReferenceException`.
  * Lỗi bị phát hiện từ máy lập trình viên, không phải lúc chạy thật trên production.

#### 3. Lưu ý & Best Practices

* Tên method phải khớp TÊN THUỘC TÍNH Java, không phải tên cột DB (`tenTour`, không phải `ten_tour`).
* **Không lạm dụng**: tên method dài quá 3 điều kiện (ví dụ `findByAAndBAndCOrDAndEOrderByF`) là dấu hiệu cần chuyển sang `@Query`.
* **Hiểu rõ `Containing` so với `Like`**:
  * `Containing` tự bọc `%` hai đầu tham số.
  * `Like` yêu cầu tự truyền ký tự `%` vào tham số.
* Dùng `existsBy` thay vì `findBy(...) != null` để kiểm tra tồn tại, vì `existsBy` chỉ cần lấy 1 dòng, nhanh hơn.
* Dùng `Optional<T>` cho kết quả có thể không tồn tại, tránh `NullPointerException`.

#### 4. Demo Thực chiến

```java
public interface TourRepository extends JpaRepository<Tour, Long> {

    List<Tour> findByNoiDen(String noiDen);

    List<Tour> findByTenTourContainingIgnoreCase(String keyword);

    List<Tour> findByGiaTourBetweenOrderByGiaTourAsc(BigDecimal giaTu, BigDecimal giaDen);

    Optional<Tour> findFirstByNoiDenOrderByGiaTourAsc(String noiDen);

    boolean existsByTenTour(String tenTour);

    long countByNoiXuatPhat(String noiXuatPhat);
}
```

**Giải thích chi tiết:**

* **`extends JpaRepository<Tour, Long>`**: kế thừa sẵn các hàm `save`, `findById`, `findAll`, `deleteById`... `Long` là kiểu của khóa chính.
* **`findByNoiDen`**: sinh `SELECT ... FROM tour WHERE noi_den = ?`.
* **`findByTenTourContainingIgnoreCase`**: sinh `WHERE UPPER(ten_tour) LIKE UPPER(?)` với tham số đã được bọc thành `%keyword%`.
* **`findByGiaTourBetweenOrderByGiaTourAsc`**: sinh `WHERE gia_tour BETWEEN ? AND ? ORDER BY gia_tour ASC`.
* **`findFirstByNoiDenOrderByGiaTourAsc`**: lấy đúng 1 tour rẻ nhất tới điểm đến, sinh thêm `LIMIT 1`.
* **`existsByTenTour`**: dùng để kiểm tra trùng tên trước khi tạo tour mới, chỉ trả `true`/`false`.
* **`countByNoiXuatPhat`**: sinh `SELECT COUNT(...) ... WHERE noi_xuat_phat = ?`.

### B. JPQL với @Query

#### 1. Bản chất kỹ thuật

* **JPQL (*Jakarta Persistence Query Language*) là ngôn ngữ truy vấn hướng đối tượng**:
  * Cú pháp gần giống SQL nhưng làm việc trên ENTITY và THUỘC TÍNH Java.
  * SQL làm việc trên BẢNG và CỘT.
* **So sánh JPQL và SQL thuần**:
  * SQL: `SELECT * FROM hoa_don h WHERE h.tong_tien > 1000000`.
  * JPQL: `SELECT h FROM HoaDon h WHERE h.tongTien > 1000000`.
  * JPQL dùng tên class `HoaDon` (*PascalCase*) và tên field `tongTien` (*camelCase*).
  * JPQL duyệt quan hệ bằng dấu chấm: `h.khachHang.tenKhachHang` (Hibernate tự sinh `JOIN`).
* **Cơ chế dịch**:
  * Hibernate phân tích câu JPQL thành cây cú pháp (*AST*), đối chiếu với metamodel của Entity.
  * Sau đó sinh SQL theo `Dialect` của từng DB (MySQL, PostgreSQL, Oracle...).
  * Vì vậy JPQL độc lập với DB: đổi từ MySQL sang PostgreSQL không cần sửa câu query.
* **Truyền tham số**:
  * Named parameter: `:mucTien` trong câu query, kết hợp `@Param("mucTien")` ở tham số method.
  * Positional parameter: `?1`, `?2` theo thứ tự tham số (khó đọc, không khuyến khích).
  * Tham số luôn được bind qua `PreparedStatement`, nên an toàn trước SQL Injection.

#### 2. Giải quyết bài toán gì?

* Truy vấn phức tạp mà tên hàm Derived Query trở nên quá dài hoặc không biểu diễn được (`JOIN`, `GROUP BY`, hàm tổng hợp, subquery).
* Đặt tên method theo nghiệp vụ dễ hiểu (`findHoaDonGiaTriCao`) thay vì tên máy móc.
* **Kiểm tra cú pháp lúc khởi động**: JPQL sai tên Entity hoặc thuộc tính, ứng dụng sẽ không chạy.

#### 3. Lưu ý & Best Practices

* **Sai lầm phổ biến nhất**: viết tên BẢNG thay vì tên ENTITY trong JPQL (`FROM hoa_don` thay vì `FROM HoaDon`), gây lỗi `UnknownEntityException`.
* Tên trong `@Param("...")` phải khớp chính xác với `:tenThamSo` trong câu query.
* **Với câu `UPDATE` hoặc `DELETE`**:
  * Bắt buộc thêm `@Modifying` và chạy trong `@Transactional`.
  * Nên thêm `clearAutomatically = true` để xóa cache cũ trong Persistence Context sau khi update hàng loạt.
* Không bao giờ nối chuỗi tham số vào câu query (`"... WHERE ten = '" + ten + "'"`), luôn dùng `:param`.
* **Có thể dùng "Constructor Expression" để trả thẳng về DTO**: `SELECT new com.example.dto.XxxDto(...) FROM ...`.

#### 4. Demo Thực chiến

```java
public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {

    @Query("SELECT h FROM HoaDon h WHERE h.tongTien > :mucTien ORDER BY h.tongTien DESC")
    List<HoaDon> findHoaDonCoTongTienLonHon(@Param("mucTien") BigDecimal mucTien);

    @Query("""
            SELECT h FROM HoaDon h
            WHERE h.khachHang.id = :khachHangId
              AND h.ngayTao BETWEEN :tuNgay AND :denNgay
            """)
    List<HoaDon> findByKhachHangTrongKhoangThoiGian(
            @Param("khachHangId") Long khachHangId,
            @Param("tuNgay") LocalDateTime tuNgay,
            @Param("denNgay") LocalDateTime denNgay);

    @Query("SELECT COALESCE(SUM(h.tongTien), 0) FROM HoaDon h WHERE h.tour.id = :tourId")
    BigDecimal sumDoanhThuTheoTour(@Param("tourId") Long tourId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE HoaDon h SET h.trangThai = :trangThaiMoi WHERE h.ngayTao < :thoiDiem")
    int updateTrangThaiHoaDonCu(
            @Param("trangThaiMoi") String trangThaiMoi,
            @Param("thoiDiem") LocalDateTime thoiDiem);
}
```

**Giải thích chi tiết:**

* **`findHoaDonCoTongTienLonHon`**:
  * `SELECT h FROM HoaDon h`: chọn toàn bộ Entity `HoaDon`, alias là `h`.
  * `h.tongTien > :mucTien`: so sánh trên thuộc tính Java `tongTien`. Hibernate dịch thành cột `tong_tien`.
  * `@Param("mucTien")`: gắn giá trị tham số Java vào placeholder `:mucTien`.
* **`findByKhachHangTrongKhoangThoiGian`**:
  * Dùng Text Block `"""` (Java 15+) để viết query nhiều dòng dễ đọc.
  * `h.khachHang.id`: duyệt qua quan hệ `@ManyToOne`. Vì chỉ lấy `id`, Hibernate đủ thông minh để dùng thẳng cột `khach_hang_id` mà không cần `JOIN`.
* **`sumDoanhThuTheoTour`**:
  * Dùng hàm tổng hợp `SUM`, kết hợp `COALESCE` để trả `0` thay vì `null` khi tour chưa có hóa đơn nào.
* **`updateTrangThaiHoaDonCu`**:
  * `@Modifying`: báo cho Spring đây là câu lệnh thay đổi dữ liệu, gọi `executeUpdate()` thay vì `getResultList()`.
  * `clearAutomatically = true`: dọn Persistence Context để các lần đọc sau lấy dữ liệu mới.
  * Kiểu trả về `int`: số dòng bị ảnh hưởng.

### C. Native Query (SQL thuần)

#### 1. Bản chất kỹ thuật

* Native Query là câu SQL viết đúng cú pháp của DB đang dùng, đặt trong `@Query(value = "...", nativeQuery = true)`.
* **Cơ chế hoạt động**:
  * Hibernate KHÔNG phân tích hay dịch câu lệnh, mà gửi nguyên văn xuống JDBC Driver.
  * Do đó dùng tên BẢNG và tên CỘT thật (`hoa_don`, `tong_tien`).
* **Kết quả trả về**:
  * Nếu `SELECT *` đủ cột của một Entity: có thể map thẳng về Entity đó.
  * Nếu là kết quả thống kê (cột tự đặt alias): map vào Interface Projection hoặc `Object[]`.
* **Interface Projection**:
  * Khai báo interface có các getter trùng với alias trong câu SQL.
  * Spring tạo proxy tự động implement interface này, đọc giá trị theo tên alias.

#### 2. Giải quyết bài toán gì?

* **Dùng các tính năng đặc thù của DB mà JPQL không hỗ trợ**: Window Function (`ROW_NUMBER() OVER`), `WITH` (*CTE*), hàm JSON, Full-text search.
* Báo cáo thống kê phức tạp cần tối ưu bằng tay từng câu SQL.
* Tái sử dụng câu SQL đã được DBA tối ưu sẵn.

#### 3. Lưu ý & Best Practices

* **Mất tính độc lập DB**: đổi MySQL sang PostgreSQL có thể phải viết lại câu query.
* **Không được kiểm tra lúc khởi động**: sai tên cột chỉ lộ ra khi API được gọi.
* Alias trong SQL phải khớp chính xác tên getter của Projection (`getTongDoanhThu()` tương ứng alias `tongDoanhThu`).
  * Một số DB như PostgreSQL tự chuyển alias không có nháy kép về chữ thường, khi đó cần bọc alias trong `"..."`.
* Khi Native Query kết hợp phân trang (`Pageable`), phải khai báo thêm `countQuery`.
* Chỉ dùng Native Query khi JPQL không làm được. Ưu tiên theo thứ tự: Derived Query, JPQL, Native Query.

#### 4. Demo Thực chiến

```java
public interface DoanhThuNoiDen {

    String getNoiDen();

    Long getSoHoaDon();

    BigDecimal getTongDoanhThu();
}
```

```java
public interface TourRepository extends JpaRepository<Tour, Long> {

    @Query(value = """
            SELECT t.noi_den AS noiDen,
                   COUNT(h.id) AS soHoaDon,
                   COALESCE(SUM(h.tong_tien), 0) AS tongDoanhThu
            FROM tour t
            LEFT JOIN hoa_don h ON h.tour_id = t.id AND YEAR(h.ngay_tao) = :nam
            GROUP BY t.noi_den
            ORDER BY tongDoanhThu DESC
            """, nativeQuery = true)
    List<DoanhThuNoiDen> thongKeDoanhThuTheoNoiDen(@Param("nam") int nam);
}
```

**Giải thích chi tiết:**

* **Interface `DoanhThuNoiDen`**:
  * Là Projection, không phải Entity. Không cần `@Entity`, không cần bảng.
  * Mỗi getter ứng với một alias trong câu SQL: `getNoiDen()` ứng với `noiDen`.
* **`nativeQuery = true`**: báo Spring gửi câu lệnh nguyên văn xuống MySQL.
* **`FROM tour t LEFT JOIN hoa_don h ON h.tour_id = t.id`**: dùng tên bảng, tên cột thật trong DB.
* **`AND YEAR(h.ngay_tao) = :nam` đặt trong mệnh đề `ON`, không đặt ở `WHERE`**:
  * Nếu đặt ở `WHERE`, các nơi đến không có hóa đơn (cột `h` toàn `NULL`) bị loại bỏ, `LEFT JOIN` vô tình biến thành `INNER JOIN`.
  * `YEAR()` là hàm riêng của MySQL, là ví dụ cho việc phụ thuộc DB.
* `COUNT(h.id)` trả về kiểu số nguyên lớn nên map sang `Long`, `SUM` trên cột `DECIMAL` map sang `BigDecimal`.
* **`@Param("nam")`**: Native Query vẫn hỗ trợ named parameter như JPQL.

### D. Dynamic Query và EntityManager

#### 1. Bản chất kỹ thuật

* `EntityManager` là gì?
  * Là interface trung tâm của JPA, mọi thao tác với DB đều đi qua nó.
  * Các hàm chính: `persist()` (thêm), `find()` (tìm theo id), `merge()` (cập nhật), `remove()` (xóa), `createQuery()` (truy vấn).
  * Thực ra `JpaRepository` của Spring chỉ là lớp vỏ bọc bên ngoài. Hàm `save()` bên trong gọi `entityManager.persist()` hoặc `entityManager.merge()`.
* **Persistence Context**:
  * Mỗi `EntityManager` quản lý một "vùng nhớ" gọi là Persistence Context, chứa các Entity đang được theo dõi.
  * Đây chính là First-level Cache sẽ học ở Mục III.
* `EntityManager` được inject vào Spring như thế nào?
  * Spring không inject `EntityManager` thật, mà inject một Shared Proxy (an toàn khi dùng đa luồng).
  * Mỗi khi gọi hàm trên proxy, nó tìm `EntityManager` thật đang gắn với transaction hiện tại của luồng đó để thực thi.
  * Nhờ vậy có thể inject `EntityManager` như một singleton bean thông thường.
* **Dynamic Query**:
  * Là câu query được "lắp ráp" lúc runtime, tùy theo người dùng nhập bộ lọc nào.
  * Công cụ chuẩn của JPA: Criteria API (`CriteriaBuilder`, `CriteriaQuery`, `Root`, `Predicate`).
  * Mỗi điều kiện lọc là một `Predicate`. Chỉ thêm `Predicate` khi người dùng có nhập giá trị cho bộ lọc đó.

![EntityManager và Persistence Context](images/05-entitymanager.png)

#### 2. Giải quyết bài toán gì?

* **Bài toán thực tế**: màn hình tìm kiếm tour có 5 ô lọc (tên, nơi đến, nơi xuất phát, giá từ, giá đến). Người dùng có thể nhập 0, 1, 2 hoặc cả 5 ô.
* **Nếu dùng Derived Query hoặc `@Query` cố định**:
  * Cần viết 2^5 = 32 method cho mọi tổ hợp, không khả thi.
  * Hoặc dùng mẹo `WHERE (:ten IS NULL OR t.tenTour = :ten)` khiến DB khó dùng Index, query chậm.
* **Dynamic Query giải quyết triệt để**: chỉ sinh đúng những điều kiện người dùng thực sự nhập.

#### 3. Lưu ý & Best Practices

* TUYỆT ĐỐI không nối chuỗi giá trị người dùng nhập vào câu JPQL, vì gây SQL Injection. Criteria API tự động bind tham số an toàn.
* Đặt logic dynamic query vào Custom Repository Fragment, không viết trong Controller hay Service:
  * Tạo interface `TourRepositoryCustom` và class `TourRepositoryCustomImpl` (bắt buộc hậu tố `Impl`).
  * Cho `TourRepository` kế thừa thêm `TourRepositoryCustom`.
* Kiểm tra chuỗi rỗng bằng `StringUtils.hasText()` thay vì chỉ `!= null`, vì người dùng có thể gửi `""` hoặc `"   "`.
* **Phương án thay thế đáng học sau**: `JpaSpecificationExecutor` của Spring Data (cũng xây dựng trên Criteria API).

#### 4. Demo Thực chiến

```java
public record TourSearchCriteria(
        String tenTour,
        String noiXuatPhat,
        String noiDen,
        BigDecimal giaTu,
        BigDecimal giaDen) {
}
```

```java
public interface TourRepositoryCustom {

    List<Tour> searchTours(TourSearchCriteria criteria);
}
```

```java
@Repository
public class TourRepositoryCustomImpl implements TourRepositoryCustom {

    private static final String FIELD_TEN_TOUR = "tenTour";
    private static final String FIELD_NOI_XUAT_PHAT = "noiXuatPhat";
    private static final String FIELD_NOI_DEN = "noiDen";
    private static final String FIELD_GIA_TOUR = "giaTour";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Tour> searchTours(TourSearchCriteria criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tour> criteriaQuery = criteriaBuilder.createQuery(Tour.class);
        Root<Tour> tourRoot = criteriaQuery.from(Tour.class);

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.hasText(criteria.tenTour())) {
            String pattern = "%" + criteria.tenTour().trim().toLowerCase() + "%";
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(tourRoot.get(FIELD_TEN_TOUR)), pattern));
        }
        if (StringUtils.hasText(criteria.noiXuatPhat())) {
            predicates.add(criteriaBuilder.equal(tourRoot.get(FIELD_NOI_XUAT_PHAT), criteria.noiXuatPhat().trim()));
        }
        if (StringUtils.hasText(criteria.noiDen())) {
            predicates.add(criteriaBuilder.equal(tourRoot.get(FIELD_NOI_DEN), criteria.noiDen().trim()));
        }
        if (criteria.giaTu() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(tourRoot.get(FIELD_GIA_TOUR), criteria.giaTu()));
        }
        if (criteria.giaDen() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(tourRoot.get(FIELD_GIA_TOUR), criteria.giaDen()));
        }

        criteriaQuery.select(tourRoot)
                .where(predicates.toArray(new Predicate[0]))
                .orderBy(criteriaBuilder.asc(tourRoot.get(FIELD_GIA_TOUR)));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
```

```java
public interface TourRepository extends JpaRepository<Tour, Long>, TourRepositoryCustom {
}
```

**Giải thích chi tiết:**

* **Record `TourSearchCriteria`**: gom 5 bộ lọc thành 1 object bất biến. Bộ lọc nào người dùng không nhập sẽ là `null`.
* **Hằng số `FIELD_TEN_TOUR`, `FIELD_GIA_TOUR`...**: đặt tên theo chuẩn `UPPER_SNAKE_CASE`, tránh gõ sai chuỗi tên thuộc tính ở nhiều chỗ.
* **`@PersistenceContext private EntityManager entityManager`**: Spring inject Shared Proxy của `EntityManager`.
* **Ba dòng khởi tạo Criteria**:
  * `getCriteriaBuilder()`: "nhà máy" sinh ra các điều kiện (`like`, `equal`, `greaterThan`...).
  * `createQuery(Tour.class)`: khai báo kết quả trả về là `Tour`.
  * `criteriaQuery.from(Tour.class)`: tương đương mệnh đề `FROM Tour`, biến `tourRoot` dùng để truy cập thuộc tính.
* **`List<Predicate> predicates`**: danh sách điều kiện, ban đầu rỗng.
* **Từng khối `if`**:
  * Chỉ khi người dùng có nhập giá trị, điều kiện tương ứng mới được thêm vào list.
  * `criteriaBuilder.lower(...)` kết hợp `toLowerCase()`: tìm kiếm không phân biệt hoa thường.
* **`.where(predicates.toArray(new Predicate[0]))`**: các `Predicate` được nối với nhau bằng `AND`. Nếu list rỗng, không sinh mệnh đề `WHERE`, tức là trả về tất cả.
* **`entityManager.createQuery(criteriaQuery).getResultList()`**: Hibernate dịch Criteria thành SQL và thực thi.
* **Kết quả với input `noiDen = "Đà Nẵng"`, `giaDen = 5000000`, các ô còn lại để trống**:
  * SQL sinh ra: `SELECT ... FROM tour WHERE noi_den = ? AND gia_tour <= ? ORDER BY gia_tour ASC`.
* `TourRepository extends JpaRepository<Tour, Long>, TourRepositoryCustom`: Spring tự động tìm class `TourRepositoryCustomImpl` và ghép vào, nên Service chỉ cần gọi `tourRepository.searchTours(criteria)`.

**Chuyển tiếp từ mảng Cơ bản sang mảng Nâng cao:**

* Ở Mục I và Mục II, ta đã biết mô tả quan hệ và lấy được dữ liệu ra. Code "chạy được" trên máy cá nhân với 20 dòng dữ liệu.
* Nhưng khi lên production với 100.000 tour và 10 triệu hóa đơn, chính những dòng code đó có thể:
  * Sinh ra hàng nghìn câu SQL cho một request (N+1 Query).
  * Kéo cả triệu dòng lên RAM, làm sập ứng dụng (thiếu phân trang).
  * Nhận dữ liệu rác như giá tour âm, tên rỗng, rồi trả về lỗi 500 khó hiểu cho Frontend.
* Mảng Nâng cao (Mục III, Mục IV) giải quyết đúng ba nhóm vấn đề này: Hiệu năng, Tính toàn vẹn dữ liệu, Trải nghiệm lỗi.

---

## III. Các phương pháp tối ưu JPA

**Dẫn dắt:**

* Khi đã biết truy vấn, bước tiếp theo là truy vấn ĐÚNG LÚC, ĐÚNG LƯỢNG, ĐÚNG SỐ LẦN.
* Bốn kỹ thuật dưới đây trả lời lần lượt: Khi nào mới load quan hệ? Load bao nhiêu dòng? Có cần hỏi lại DB không? Gom bao nhiêu câu SQL thành một?

### A. Lazy / Eager initialization

#### 1. Bản chất kỹ thuật

* **Fetch Type quy định THỜI ĐIỂM Hibernate load dữ liệu của Entity liên quan**:
  * `EAGER` (háo hức): load NGAY LẬP TỨC cùng Entity chính, dù có dùng đến hay không.
  * `LAZY` (lười biếng): CHƯA load, chỉ load khi code thực sự truy cập vào dữ liệu đó.
* **Giá trị mặc định của JPA (cần thuộc lòng)**:
  * `@ManyToOne`, `@OneToOne`: mặc định `EAGER`.
  * `@OneToMany`, `@ManyToMany`: mặc định `LAZY`.
  * Quy tắc dễ nhớ: kết thúc bằng `One` là `EAGER`, kết thúc bằng `Many` là `LAZY`.
* **Proxy Object - "hình nộm" trong cơ chế Lazy Load**:
  * Với `@ManyToOne(fetch = LAZY)`, Hibernate không gán `Tour` thật vào `review.tour`.
  * Thay vào đó, nó dùng thư viện ByteBuddy sinh lúc runtime một class con của `Tour` (tên dạng `Tour$HibernateProxy$Ab12Cd`).
  * Object proxy này CHỈ chứa sẵn `id` (lấy từ cột khóa ngoại `tour_id`), mọi field khác đang trống.
  * Khi gọi một getter khác `getId()` (ví dụ `getTenTour()`), proxy chặn lời gọi, chạy câu `SELECT` lấy dữ liệu thật, rồi mới trả kết quả.
* **Với Collection (`@OneToMany`, `@ManyToMany`)**:
  * Hibernate không dùng `ArrayList` hay `HashSet` thường, mà thay bằng `PersistentBag` hoặc `PersistentSet`.
  * Các wrapper này chỉ chạy câu `SELECT` khi gọi `size()`, `iterator()`, `get()`...
* **So sánh thời điểm query DB**:

| Tiêu chí | EAGER | LAZY |
|---|---|---|
| Thời điểm query | Ngay khi load Entity chính | Khi lần đầu truy cập dữ liệu liên quan |
| Số câu SQL khi chỉ cần Entity chính | Nhiều hơn mức cần (thường có `JOIN` hoặc câu phụ) | Đúng 1 câu |
| Giá trị trong field | Object thật | Proxy hoặc PersistentCollection |
| Rủi ro chính | Load thừa dữ liệu, dễ gây N+1 | `LazyInitializationException` |

* **`LazyInitializationException`**:
  * Proxy cần `EntityManager` (*Session*) còn mở để chạy câu `SELECT`.
  * Nếu truy cập proxy SAU KHI transaction đã đóng, Hibernate ném lỗi `could not initialize proxy - no Session`.

![Lazy Load và Proxy Object](images/06-lazy-proxy.png)

#### 2. Giải quyết bài toán gì?

* **Bài toán**: màn hình danh sách Review chỉ cần hiển thị nội dung và số sao.
  * Với `EAGER`, mỗi lần load Review, Hibernate kéo theo cả `Tour`, rồi `Tour` lại kéo theo những quan hệ `EAGER` khác của nó.
  * Một câu query đơn giản có thể "phình" thành câu `JOIN` 5 - 6 bảng, tốn RAM và băng thông.
* `LAZY` đảm bảo nguyên tắc "cần gì lấy nấy", giúp truy vấn mặc định luôn nhẹ nhất.
* Khi thực sự cần dữ liệu liên quan, ta chủ động lấy kèm bằng `JOIN FETCH` hoặc `@EntityGraph` (mục III.D).

#### 3. Lưu ý & Best Practices

* **Quy tắc vàng**: đặt `LAZY` cho TẤT CẢ quan hệ, kể cả `@ManyToOne` và `@OneToOne`. Không có cách nào "tắt" `EAGER` ở từng câu query, nhưng luôn có cách "bật" fetch khi cần.
* Không sửa `LazyInitializationException` bằng cách đổi sang `EAGER`. Đó là chữa triệu chứng, gây bệnh nặng hơn.
* **Tắt Open Session In View (`spring.jpa.open-in-view=false`)**:
  * Mặc định Spring Boot giữ `EntityManager` mở suốt request, nên Lazy Load "chạy được" cả trong Controller hay lúc Jackson serialize JSON.
  * Hệ quả: giữ kết nối DB lâu, và câu SQL bị bắn ra ở những chỗ không ngờ tới. Spring Boot cũng in cảnh báo về cấu hình này khi khởi động.
* Mọi việc truy cập dữ liệu Lazy phải nằm trong method có `@Transactional`, và nên chuyển Entity sang DTO ngay trong Service.
* `getId()` trên proxy KHÔNG kích hoạt câu SQL, vì `id` đã có sẵn từ khóa ngoại.

#### 4. Demo Thực chiến

```java
public record ReviewDetailResponse(Long reviewId, String noiDung, Long tourId, String tenTour) {
}
```

```java
@Service
@RequiredArgsConstructor
public class ReviewQueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewQueryService.class);

    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public ReviewDetailResponse getReviewDetail(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy review id = " + reviewId));

        Tour tour = review.getTour();
        LOGGER.info("Class thực tế của tour: {}", tour.getClass().getName());

        Long tourId = tour.getId();
        String tenTour = tour.getTenTour();

        return new ReviewDetailResponse(review.getId(), review.getNoiDung(), tourId, tenTour);
    }

    public String getTenTourOutsideTransaction(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy review id = " + reviewId));
        return review.getTour().getTenTour();
    }
}
```

**Giải thích chi tiết:**

* **`LOGGER`**: hằng số logger của SLF4J, đặt tên theo chuẩn `UPPER_SNAKE_CASE` vì là `static final`.
* **`@Transactional(readOnly = true)`**: mở transaction chỉ đọc, giữ `EntityManager` sống suốt method. `readOnly` giúp Hibernate bỏ qua bước Dirty Checking, tăng tốc độ.
* **`reviewRepository.findById(reviewId)`**: sinh câu SQL số 1, chỉ `SELECT` bảng `review`. Không có `JOIN` sang `tour`.
* **`review.getTour()`**: chưa sinh SQL. Biến `tour` lúc này là proxy.
* **`tour.getClass().getName()`**: log ra dạng `com.example.entity.Tour$HibernateProxy$Xyz`, chứng minh đây là "hình nộm".
* **`tour.getId()`**: KHÔNG sinh SQL, vì proxy đã giữ sẵn `id`.
* **`tour.getTenTour()`**: proxy được "đánh thức", sinh câu SQL số 2: `SELECT ... FROM tour WHERE id = ?`.
* Method `getTenTourOutsideTransaction` (KHÔNG có `@Transactional`, và `open-in-view=false`):
  * `findById` chạy trong transaction riêng của Repository, transaction đóng ngay khi `findById` trả về.
  * Dòng `review.getTour().getTenTour()` truy cập proxy khi Session đã đóng, ném `LazyInitializationException`.
* **Nếu đổi `Review.tour` sang `EAGER`**: câu `findById` sẽ luôn `JOIN` sang bảng `tour`, kể cả ở những màn hình không cần `tenTour`.

### B. Pagination

#### 1. Bản chất kỹ thuật

* Pagination (phân trang) là chia tập kết quả lớn thành các "trang" nhỏ, mỗi lần chỉ lấy một trang.
* **Các thành phần của Spring Data**:
  * `Pageable`: interface mô tả yêu cầu phân trang (trang số mấy, mỗi trang bao nhiêu dòng, sắp xếp thế nào).
  * `PageRequest.of(page, size, sort)`: class cài đặt `Pageable`, cách tạo phổ biến nhất.
  * `Page<T>`: kết quả trả về, gồm dữ liệu trang hiện tại + tổng số phần tử + tổng số trang.
  * `Slice<T>`: giống `Page` nhưng KHÔNG đếm tổng, chỉ biết "còn trang sau hay không".
* **Cơ chế biến đổi `Pageable` thành SQL**:
  * Spring tính `offset = page * size` (`page` bắt đầu từ 0).
  * Hibernate gọi `setFirstResult(offset)` và `setMaxResults(size)` trên câu query.
  * Dialect MySQL dịch thành `LIMIT ? OFFSET ?`. Ví dụ `PageRequest.of(2, 10)` thành `LIMIT 10 OFFSET 20` (bỏ qua 20 dòng đầu, lấy 10 dòng).
  * `Sort` được dịch thành mệnh đề `ORDER BY`.
* **Câu COUNT phụ**:
  * Khi kiểu trả về là `Page<T>`, Spring tự sinh thêm câu `SELECT COUNT(...)` để tính `totalElements`, `totalPages`.
  * Khi kiểu trả về là `Slice<T>`, Spring lấy `size + 1` dòng để biết còn trang sau không, không cần câu COUNT.

![Pageable được dịch thành LIMIT và OFFSET](images/07-pagination.png)

#### 2. Giải quyết bài toán gì?

* **Bài toán**: API `GET /api/tours` gọi `findAll()` trên bảng 100.000 tour.
  * Hibernate tạo 100.000 object trên RAM, Jackson tạo chuỗi JSON hàng chục MB.
  * Hậu quả: `OutOfMemoryError`, API mất vài chục giây, trình duyệt treo.
* Phân trang giới hạn lượng dữ liệu mỗi request ở mức cố định (ví dụ 20 dòng), bất kể bảng lớn đến đâu.
* Trả thêm metadata (`totalPages`, `totalElements`) để Frontend vẽ thanh chuyển trang.

#### 3. Lưu ý & Best Practices

* `page` trong Spring Data bắt đầu từ 0, không phải 1. Cần thống nhất với Frontend ngay từ đầu.
* Luôn giới hạn `size` tối đa (ví dụ `MAX_PAGE_SIZE = 100`). Nếu không, người dùng truyền `size=1000000` là quay lại bài toán cũ.
* **Luôn có `Sort` ổn định khi phân trang**: không có `ORDER BY`, DB không đảm bảo thứ tự, dữ liệu có thể bị lặp hoặc mất giữa các trang.
* **Câu COUNT có thể rất chậm trên bảng lớn**: nếu giao diện dạng "cuộn vô hạn", dùng `Slice` thay cho `Page`.
* `OFFSET` rất lớn (trang 50.000) vẫn chậm vì DB phải quét và bỏ qua toàn bộ dòng phía trước. Bài toán này cần Keyset Pagination (`WHERE id > :lastId`), sẽ học ở khóa nâng cao.
* **KHÔNG kết hợp `JOIN FETCH` một Collection với `Pageable`**:
  * Hibernate không thể `LIMIT` đúng khi một tour bị nhân thành nhiều dòng do `JOIN`.
  * Nó sẽ tải TOÀN BỘ dữ liệu lên RAM rồi mới cắt trang, kèm cảnh báo `HHH90003004: firstResult/maxResults specified with collection fetch; applying in memory`.
* **Không trả thẳng `Page<Entity>` ra JSON**: cấu trúc JSON của `PageImpl` không ổn định giữa các phiên bản, và Entity lộ quan hệ Lazy. Hãy map sang DTO và tự định nghĩa object phân trang.

#### 4. Demo Thực chiến

```java
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last) {

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast());
    }
}
```

```java
public record TourSummaryResponse(Long id, String tenTour, String noiDen, BigDecimal giaTour) {

    public static TourSummaryResponse fromEntity(Tour tour) {
        return new TourSummaryResponse(tour.getId(), tour.getTenTour(), tour.getNoiDen(), tour.getGiaTour());
    }
}
```

```java
public interface TourRepository extends JpaRepository<Tour, Long> {

    Page<Tour> findByNoiDen(String noiDen, Pageable pageable);
}
```

```java
@RestController
@RequestMapping("/api/tours")
@RequiredArgsConstructor
public class TourController {

    private static final int MAX_PAGE_SIZE = 100;

    private final TourRepository tourRepository;

    @GetMapping("/search")
    public PageResponse<TourSummaryResponse> searchByNoiDen(
            @RequestParam String noiDen,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by("giaTour").ascending().and(Sort.by("id")));

        Page<TourSummaryResponse> result = tourRepository.findByNoiDen(noiDen, pageable)
                .map(TourSummaryResponse::fromEntity);

        return PageResponse.from(result);
    }
}
```

**Giải thích chi tiết:**

* **Record `PageResponse<T>`**: định dạng JSON phân trang do team tự kiểm soát. Hàm `from()` chuyển `Page<T>` của Spring sang record này.
* **Record `TourSummaryResponse`**: DTO rút gọn, hàm `fromEntity()` chuyển Entity sang DTO, chỉ đọc các field cơ bản nên không kích hoạt Lazy Load.
* **`Page<Tour> findByNoiDen(String noiDen, Pageable pageable)`**: chỉ cần thêm tham số `Pageable` cuối cùng, Derived Query tự hỗ trợ phân trang.
* **`MAX_PAGE_SIZE = 100`**: hằng số `UPPER_SNAKE_CASE`, chặn trần kích thước trang.
* **`safePage`, `safeSize`**: chuẩn hóa input, chống trang âm hoặc kích thước vô lý.
* **`PageRequest.of(safePage, safeSize, Sort...)`**:
  * Sắp theo `giaTour` tăng dần, nếu trùng giá thì theo `id` để thứ tự luôn ổn định.
* **`.map(TourSummaryResponse::fromEntity)`**: `Page` có sẵn hàm `map` để đổi Entity sang DTO mà vẫn giữ nguyên metadata phân trang.
* **Hai câu SQL thực tế khi gọi `GET /api/tours/search?noiDen=Đà Nẵng&page=2&size=10`**:
  * `SELECT ... FROM tour WHERE noi_den = ? ORDER BY gia_tour ASC, id ASC LIMIT 10 OFFSET 20`.
  * `SELECT COUNT(t.id) FROM tour t WHERE t.noi_den = ?`.

### C. Caching

#### 1. Bản chất kỹ thuật

* **First-level Cache (Bộ nhớ đệm cấp 1) chính là Persistence Context của `EntityManager`**:
  * Bên trong là một `Map` với khóa là cặp (Kiểu Entity, Id), giá trị là object Entity đang được quản lý.
  * Luôn bật sẵn, không thể tắt, không cần cấu hình.
* **Phạm vi sống (*Scope*)**:
  * Gắn với một `EntityManager`, trong Spring thường là một `@Transactional`.
  * Transaction kết thúc, Persistence Context bị đóng, cache biến mất.
  * KHÔNG chia sẻ giữa các request hay giữa các user.
* Luồng xử lý của `entityManager.find(Tour.class, 1L)` (cũng là thứ `findById` gọi bên dưới):
  * Bước 1: Tra trong Persistence Context xem đã có `Tour#1` chưa.
  * Bước 2: Nếu có, trả về NGAY object đó, không gửi SQL.
  * Bước 3: Nếu chưa, chạy `SELECT`, đưa object vào Persistence Context, rồi trả về.
* Vì sao query 2 lần cùng 1 ID trong 1 transaction chỉ sinh 1 câu SQL?
  * Lần 1: cache trống, Hibernate chạy `SELECT` và lưu `Tour#1` vào cache.
  * Lần 2: tìm thấy `Tour#1` trong cache, trả lại CHÍNH object đó. Thậm chí `tour1 == tour2` cho kết quả `true`.
* **Vai trò phụ nhưng quan trọng**:
  * Identity guarantee: trong một transaction, một dòng DB tương ứng đúng một object Java.
  * Dirty Checking: Hibernate lưu một "bản chụp" (*snapshot*) lúc load. Khi commit, so sánh object hiện tại với snapshot để tự sinh `UPDATE`.
* **Phân biệt với Second-level Cache**:
  * Second-level Cache dùng chung cho toàn ứng dụng (cần cấu hình Ehcache, Redis...).
  * Không bật mặc định, sẽ học ở khóa nâng cao.

![First-level Cache](images/08-first-level-cache.png)

#### 2. Giải quyết bài toán gì?

* **Bài toán**: trong một nghiệp vụ đặt tour, nhiều hàm con cùng cần `Tour#1` (kiểm tra còn chỗ, tính giá, ghi hóa đơn).
  * Không có cache: mỗi hàm `findById` một lần, 3 câu SQL giống hệt nhau.
  * Có First-level Cache: chỉ 1 câu SQL đầu tiên, các lần sau lấy từ bộ nhớ.
* **Đảm bảo nhất quán**: mọi chỗ trong transaction cùng sửa một object, tránh tình trạng hai bản sao "giẫm chân" nhau.

#### 3. Lưu ý & Best Practices

* Chỉ `find()` hoặc `findById()` mới tra cache TRƯỚC khi gọi DB.
  * Câu JPQL như `findByTenTour(...)` luôn gửi SQL xuống DB, sau đó mới đối chiếu kết quả với cache để trả về đúng object đang quản lý.
* **Thiếu `@Transactional` ở Service**:
  * Mỗi lời gọi Repository chạy trong transaction riêng, nên cache bị bỏ đi sau mỗi lời gọi.
  * Khi đó gọi `findById` 2 lần sẽ ra 2 câu SQL (nếu đã tắt Open Session In View).
* Sau câu `UPDATE` hoặc `DELETE` hàng loạt bằng `@Modifying`, cache có thể giữ dữ liệu cũ. Dùng `clearAutomatically = true`.
* Khi xử lý batch hàng chục nghìn Entity trong một transaction, cache phình to gây tốn RAM. Gọi `entityManager.flush()` và `entityManager.clear()` định kỳ.
* Không nhầm First-level Cache với `@Cacheable` của Spring Cache: hai cơ chế độc lập.

#### 4. Demo Thực chiến

```java
@Service
@RequiredArgsConstructor
public class TourCacheDemoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TourCacheDemoService.class);

    private final TourRepository tourRepository;

    @Transactional(readOnly = true)
    public void demoFirstLevelCache(Long tourId) {
        LOGGER.info("Lần gọi thứ nhất");
        Tour firstLoad = tourRepository.findById(tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tour id = " + tourId));

        LOGGER.info("Lần gọi thứ hai");
        Tour secondLoad = tourRepository.findById(tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tour id = " + tourId));

        LOGGER.info("Cùng một object trong bộ nhớ: {}", firstLoad == secondLoad);
    }

    @Transactional
    public void demoDirtyChecking(Long tourId, String tenTourMoi) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tour id = " + tourId));
        tour.setTenTour(tenTourMoi);
    }
}
```

**Giải thích chi tiết:**

* **`demoFirstLevelCache`**:
  * Log "Lần gọi thứ nhất" xuất hiện, ngay sau đó là câu `SELECT ... FROM tour WHERE id = ?`.
  * Log "Lần gọi thứ hai" xuất hiện, KHÔNG có câu SQL nào theo sau.
  * Log cuối in ra `true`: `firstLoad` và `secondLoad` trỏ cùng một địa chỉ trong bộ nhớ.
* **`demoDirtyChecking`**:
  * `findById` load tour và lưu snapshot vào Persistence Context.
  * `tour.setTenTour(tenTourMoi)`: chỉ thay đổi object Java, chưa có gì xảy ra ở DB.
  * Khi method kết thúc, transaction commit, Hibernate so sánh với snapshot, phát hiện `tenTour` thay đổi và tự sinh `UPDATE tour SET ten_tour = ? WHERE id = ?`.
  * Không cần gọi `save()`. Đây là hệ quả trực tiếp của First-level Cache.

### D. Vấn đề N + 1 Query và Cách phòng tránh

#### 1. Bản chất kỹ thuật

* **Định nghĩa**:
  * Chạy 1 câu query để lấy danh sách N bản ghi cha.
  * Sau đó, với MỖI bản ghi cha, chạy thêm 1 câu query để lấy dữ liệu con.
  * Tổng cộng: 1 + N câu SQL, thay vì 1 hoặc 2 câu.
* **Ví dụ thực tế**: lấy 10 Tour kèm Review.
  * Câu 1: `SELECT * FROM tour` trả về 10 tour.
  * Vòng lặp gọi `tour.getReviews()` trên từng tour, mỗi lần đánh thức một `PersistentBag` Lazy.
  * Câu 2 đến câu 11: `SELECT * FROM review WHERE tour_id = ?`, lặp 10 lần với 10 id khác nhau.
  * Tổng: 11 câu SQL. Với 1.000 tour: 1.001 câu SQL.
* **Nguyên nhân gốc rễ**:
  * Lazy Load không sai. Sai ở chỗ ta BIẾT trước sẽ cần dữ liệu con, nhưng lại không nói với Hibernate từ đầu.
  * `EAGER` cũng không cứu được: với `findAll()`, Hibernate vẫn chạy câu `SELECT tour` trước, rồi bắn từng câu phụ để nạp quan hệ `EAGER`.
* **Hai giải pháp "nói trước với Hibernate"**:
  * Giải pháp A - `JOIN FETCH` trong JPQL: viết rõ trong câu query rằng cần lấy kèm Collection.
  * Giải pháp B - `@EntityGraph`: khai báo bằng annotation, ghi đè (*override*) fetch plan mặc định của câu query đó.
  * Cả hai đều sinh ra 1 câu SQL duy nhất có `LEFT JOIN`, lấy cha và con trong một lần.

![Vấn đề N + 1 Query và cách khắc phục](images/09-n-plus-1.png)

#### 2. Giải quyết bài toán gì?

* Mỗi câu SQL tốn một vòng mạng (*network round trip*) giữa App và DB, khoảng 1 - 5 ms.
  * 1.001 câu SQL có thể mất vài giây, trong khi 1 câu `JOIN` chỉ mất vài chục ms.
* **N+1 là lỗi hiệu năng "ẩn"**: trên máy dev với 5 bản ghi không ai nhận ra, lên production mới làm sập Connection Pool.
* `JOIN FETCH` và `@EntityGraph` giúp số câu SQL KHÔNG tăng theo số bản ghi.

#### 3. Lưu ý & Best Practices

* Luôn bật `show-sql` khi phát triển, và ĐẾM số câu SQL của mỗi API. Thấy câu lệnh lặp lại giống hệt nhau chỉ khác tham số là N+1.
* `JOIN FETCH` một Collection làm số dòng kết quả SQL bị nhân lên (1 tour có 5 review thành 5 dòng):
  * Hibernate 6 tự loại trùng Entity cha trong kết quả.
  * Với Hibernate 5 (Spring Boot 2), cần thêm `DISTINCT` vào câu JPQL.
* **Không `JOIN FETCH` hai Collection kiểu `List` cùng lúc**: gây `MultipleBagFetchException`. Có thể đổi sang `Set` hoặc tách thành 2 câu query.
* Không kết hợp `JOIN FETCH` Collection với `Pageable` (đã giải thích ở mục III.B).
* **Giải pháp bổ trợ khi buộc phải phân trang**: cấu hình `spring.jpa.properties.hibernate.default_batch_fetch_size=50`.
  * Hibernate gom các câu phụ thành `WHERE tour_id IN (?, ?, ..., ?)`, giảm từ 1 + N xuống 1 + N/50 câu.
* **Chọn giữa hai cách**:
  * `JOIN FETCH`: khi đã cần viết `@Query` (có điều kiện `WHERE` phức tạp).
  * `@EntityGraph`: khi muốn giữ Derived Query hoặc tái sử dụng một method có sẵn, chỉ thêm annotation.

#### 4. Demo Thực chiến

**Code gây ra N+1 (Phiên bản sai):**

```java
public record TourWithReviewsResponse(Long id, String tenTour, List<String> reviews) {
}
```

```java
@Service
@RequiredArgsConstructor
public class TourReviewService {

    private final TourRepository tourRepository;

    @Transactional(readOnly = true)
    public List<TourWithReviewsResponse> getAllToursWithReviewsBad() {
        List<Tour> tours = tourRepository.findAll();
        return tours.stream()
                .map(tour -> new TourWithReviewsResponse(
                        tour.getId(),
                        tour.getTenTour(),
                        tour.getReviews().stream().map(Review::getNoiDung).toList()))
                .toList();
    }
}
```

* **`tourRepository.findAll()`**: câu SQL số 1, lấy 10 tour.
* **`tour.getReviews().stream()`**: mỗi lần lặp đánh thức một Collection Lazy, sinh 1 câu `SELECT ... FROM review WHERE tour_id = ?`.
* **Kết quả log**: 11 câu SQL cho 10 tour.

**Giải pháp A - JOIN FETCH:**

```java
public interface TourRepository extends JpaRepository<Tour, Long> {

    @Query("SELECT t FROM Tour t LEFT JOIN FETCH t.reviews")
    List<Tour> findAllWithReviews();

    @Query("SELECT t FROM Tour t LEFT JOIN FETCH t.reviews WHERE t.noiDen = :noiDen")
    List<Tour> findByNoiDenWithReviews(@Param("noiDen") String noiDen);
}
```

* **`LEFT JOIN FETCH t.reviews`**:
  * `JOIN` thường chỉ dùng để lọc, KHÔNG nạp dữ liệu vào Collection.
  * `JOIN FETCH` vừa `JOIN` vừa ra lệnh "đổ dữ liệu review vào `tour.reviews` ngay".
  * `LEFT` để giữ lại cả những tour chưa có review nào.
* **SQL sinh ra**: `SELECT t.*, r.* FROM tour t LEFT JOIN review r ON r.tour_id = t.id`. Chỉ 1 câu.
* Sau câu query, `tour.getReviews()` là Collection đã được khởi tạo, gọi bao nhiêu lần cũng không sinh SQL.

**Giải pháp B - @EntityGraph:**

```java
public interface TourRepository extends JpaRepository<Tour, Long> {

    @EntityGraph(attributePaths = {"reviews"})
    @Query("SELECT t FROM Tour t")
    List<Tour> findAllWithReviewsGraph();

    @EntityGraph(attributePaths = {"reviews"})
    List<Tour> findByNoiXuatPhat(String noiXuatPhat);
}
```

* **`@EntityGraph(attributePaths = {"reviews"})`**:
  * Khai báo danh sách thuộc tính cần nạp kèm trong câu query này.
  * Ghi đè (*override*) fetch plan mặc định: `reviews` vốn là `LAZY`, riêng ở method này được nạp ngay bằng `LEFT JOIN`.
  * Các thuộc tính KHÔNG liệt kê vẫn giữ nguyên fetch type đã khai báo trên Entity.
* **`findAllWithReviewsGraph`**: kết hợp `@Query` đơn giản với `@EntityGraph`, câu JPQL gọn, không cần viết `JOIN FETCH`.
* **`findByNoiXuatPhat`**: áp `@EntityGraph` lên Derived Query, giữ nguyên lợi ích "khai báo tên hàm" mà vẫn tránh N+1.
* **Có thể nạp quan hệ nhiều cấp bằng dấu chấm**: `attributePaths = {"hoaDons", "hoaDons.khachHang"}`.

**Service sau khi sửa:**

```java
@Transactional(readOnly = true)
public List<TourWithReviewsResponse> getAllToursWithReviews() {
    return tourRepository.findAllWithReviews().stream()
            .map(tour -> new TourWithReviewsResponse(
                    tour.getId(),
                    tour.getTenTour(),
                    tour.getReviews().stream().map(Review::getNoiDung).toList()))
            .toList();
}
```

* Chỉ thay `findAll()` bằng `findAllWithReviews()` (hoặc `findAllWithReviewsGraph()`), toàn bộ phần còn lại giữ nguyên.
* **Kết quả log**: 1 câu SQL duy nhất, dù có 10 hay 10.000 tour.

---

## IV. Validation và Exception Handling

**Dẫn dắt:**

* Mục III giúp hệ thống CHẠY NHANH. Mục IV giúp hệ thống CHẠY ĐÚNG và BÁO LỖI RÕ RÀNG.
* Nguyên tắc: "Không bao giờ tin dữ liệu từ client". Mọi dữ liệu phải được kiểm tra ngay tại cổng vào (*Controller*).

### A. Cơ chế Validation trong Spring Boot

#### 1. Bản chất kỹ thuật

* **Bean Validation (*Jakarta Validation*) là một ĐẶC TẢ (*specification*)**:
  * Quy định bộ annotation chuẩn như `@NotNull`, `@NotBlank`, `@Min`, `@Size`, `@Email`.
  * Bản cài đặt (*implementation*) phổ biến nhất là Hibernate Validator, được kéo về bởi `spring-boot-starter-validation`.
* **Luồng chạy ngầm khi request đi vào Controller**:
  * Bước 1: `DispatcherServlet` nhận request, tìm method Controller phù hợp.
  * Bước 2: Jackson chuyển chuỗi JSON thành object DTO (`@RequestBody`).
  * Bước 3: Thấy tham số có `@Valid`, Spring gọi `Validator` duyệt qua từng annotation trên các field của DTO.
  * Bước 4: Nếu có vi phạm, Spring ném `MethodArgumentNotValidException` (chứa danh sách lỗi từng field). Code trong method Controller KHÔNG được chạy.
  * Bước 5: Nếu hợp lệ, method Controller mới được gọi.
* **Phân biệt các annotation hay nhầm**:

| Annotation | `null` | `""` | `"   "` | Áp dụng cho |
|---|---|---|---|---|
| `@NotNull` | Lỗi | Hợp lệ | Hợp lệ | Mọi kiểu |
| `@NotEmpty` | Lỗi | Lỗi | Hợp lệ | String, Collection |
| `@NotBlank` | Lỗi | Lỗi | Lỗi | String |

* **Các annotation số và chuỗi khác**:
  * `@Min(value)`, `@Max(value)`: giới hạn số nguyên.
  * `@Positive`, `@PositiveOrZero`, `@DecimalMin(value, inclusive)`: phù hợp cho `BigDecimal` (tiền).
  * `@Size(min, max)`: độ dài chuỗi hoặc kích thước Collection.
  * `@Email`, `@Pattern(regexp)`, `@Future`, `@Past`.
* Các annotation số (`@Min`, `@Positive`...) đều coi `null` là HỢP LỆ. Muốn bắt buộc nhập phải thêm `@NotNull`.

![Luồng Validation](images/10-validation-flow.png)

#### 2. Giải quyết bài toán gì?

* **Bài toán**: client gửi `{"tenTour": "", "giaTour": -500000}`.
  * Không validation: dữ liệu rác được lưu thẳng vào DB, gây sai báo cáo doanh thu.
  * Hoặc DB từ chối do ràng buộc `NOT NULL`, ném lỗi SQL thô, API trả về 500 khó hiểu.
* Thay vì viết hàng chục dòng `if (tenTour == null || tenTour.isBlank()) throw ...`, chỉ cần một annotation.
* Quy tắc kiểm tra nằm ngay trên field, đọc DTO là biết luật nghiệp vụ.

#### 3. Lưu ý & Best Practices

* Từ Spring Boot 2.3, `spring-boot-starter-web` KHÔNG còn kèm validation. Quên thêm `spring-boot-starter-validation` thì mọi annotation bị bỏ qua mà không báo lỗi.
* **Quên `@Valid` trước `@RequestBody`**: annotation trên DTO có đủ nhưng không bao giờ được kiểm tra.
* **Validate trên DTO (*Request object*), không đặt Entity trực tiếp làm `@RequestBody`**:
  * Entity chứa cả các field client không được phép gửi (`id`, `ngayTao`).
  * Mỗi API có luật riêng (tạo mới khác cập nhật), DTO tách biệt sẽ linh hoạt hơn.
* Annotation trên Entity vẫn có tác dụng như "lưới an toàn cuối": Hibernate kiểm tra trước khi `INSERT`/`UPDATE`, nếu vi phạm ném `ConstraintViolationException`.
* Object lồng nhau (ví dụ `List<ChiTietRequest>`) phải đặt thêm `@Valid` trên field đó thì mới được kiểm tra đệ quy.
* Validation chỉ kiểm tra ĐỊNH DẠNG. Luật cần truy vấn DB (tên tour đã tồn tại chưa) phải kiểm tra ở tầng Service.
* Với `@RequestParam` hoặc `@PathVariable` có annotation ràng buộc, Spring 6.1 trở lên tự kiểm tra và ném `HandlerMethodValidationException`.

#### 4. Demo Thực chiến

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

```java
public record ReviewCreateRequest(

        @NotNull(message = "Mã tour không được để trống")
        Long tourId,

        @NotBlank(message = "Nội dung đánh giá không được để trống")
        @Size(max = 1000, message = "Nội dung đánh giá tối đa 1000 ký tự")
        String noiDung,

        @NotNull(message = "Số sao không được để trống")
        @Min(value = 1, message = "Số sao tối thiểu là 1")
        @Max(value = 5, message = "Số sao tối đa là 5")
        Integer soSao,

        @Email(message = "Email không đúng định dạng")
        String emailNguoiDanhGia) {
}
```

```java
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Void> createReview(@Valid @RequestBody ReviewCreateRequest request) {
        reviewService.addReviewToTour(request.tourId(), request.noiDung(), request.soSao());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
```

**Giải thích chi tiết:**

* **Dependency `spring-boot-starter-validation`**: kéo Hibernate Validator vào classpath, Spring Boot tự cấu hình bean `Validator`.
* **Record `ReviewCreateRequest`**:
  * `@NotNull` trên `tourId`: bắt buộc gửi lên.
  * `@NotBlank` + `@Size(max = 1000)` trên `noiDung`: không rỗng, không toàn khoảng trắng, không quá dài.
  * `@NotNull` + `@Min(1)` + `@Max(5)` trên `soSao`: bắt buộc và nằm trong khoảng 1 đến 5.
  * `@Email` trên `emailNguoiDanhGia`: không bắt buộc (cho phép `null`), nhưng nếu gửi thì phải đúng định dạng.
  * Thuộc tính `message`: nội dung lỗi trả về cho người dùng, nên viết bằng ngôn ngữ nghiệp vụ.
* **`@Valid @RequestBody ReviewCreateRequest request`**:
  * `@RequestBody`: Jackson chuyển JSON thành record.
  * `@Valid`: kích hoạt kiểm tra. Nếu lỗi, dòng `reviewService.addReviewToTour(...)` không bao giờ được chạy.
* Gửi thử `{"tourId": 1, "noiDung": "  ", "soSao": 9}` khi CHƯA có xử lý lỗi tập trung, Spring Boot trả về:

```json
{
  "timestamp": "2026-10-12T15:30:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/reviews"
}
```

* Response trên đúng mã 400 nhưng KHÔNG nói field nào sai, sai vì sao. Frontend không thể hiển thị lỗi cho người dùng. Đây là động lực cho mục IV.B.

### B. Kiến trúc xử lý lỗi tập trung (Exception Handling)

#### 1. Bản chất kỹ thuật

* **Luồng xử lý exception trong Spring MVC**:
  * Exception được ném ra ở bất kỳ tầng nào (Repository, Service, Controller) sẽ "nổi" ngược lên `DispatcherServlet`.
  * `DispatcherServlet` duyệt chuỗi `HandlerExceptionResolver` để tìm "người xử lý".
  * `ExceptionHandlerExceptionResolver` tìm method có `@ExceptionHandler` phù hợp: trước tiên trong chính Controller đó, sau đó trong các bean `@ControllerAdvice`.
  * Nếu nhiều handler cùng khớp, Spring chọn handler có kiểu exception GẦN NHẤT trong cây kế thừa.
* **`@RestControllerAdvice`**:
  * Bằng `@ControllerAdvice` + `@ResponseBody`.
  * Là một bean "bao bọc" toàn bộ Controller, giá trị trả về của mỗi handler được serialize thành JSON.
* **`@ExceptionHandler(XxxException.class)`**:
  * Đánh dấu method sẽ được gọi khi exception kiểu `XxxException` (hoặc lớp con) xảy ra.
  * Method có thể nhận tham số là exception và `HttpServletRequest` để lấy thông tin bổ sung.
* **JSON Error Response thống nhất**:
  * Một record chung (ví dụ `ApiErrorResponse`) dùng cho MỌI lỗi: thời điểm, mã HTTP, mã lỗi, thông điệp, đường dẫn, chi tiết từng field.

![Xử lý lỗi tập trung](images/11-exception-flow.png)

#### 2. Giải quyết bài toán gì?

* **Bài toán**: 30 method Controller, mỗi method đều có khối `try-catch` giống nhau.
  * Code lặp lại, dễ quên, mỗi người viết một kiểu response lỗi khác nhau.
  * Frontend phải viết 5 - 6 cách đọc lỗi cho cùng một hệ thống.
* Lỗi không được bắt sẽ trả về 500 kèm thông tin nội bộ, có thể lộ cấu trúc hệ thống.
* **Xử lý tập trung giúp**:
  * Controller và Service chỉ tập trung vào nghiệp vụ, cứ `throw` khi gặp lỗi.
  * Một nơi duy nhất quyết định mã HTTP và định dạng JSON cho mọi loại lỗi.

#### 3. Lưu ý & Best Practices

* Tạo exception nghiệp vụ riêng (`ResourceNotFoundException`, `BusinessException`), kế thừa `RuntimeException`, thay vì ném `RuntimeException` chung chung.
* **Ánh xạ đúng mã HTTP**:
  * 400: dữ liệu không hợp lệ. 404: không tìm thấy tài nguyên. 409: xung đột (trùng dữ liệu). 500: lỗi không lường trước.
* **Luôn có một handler "chốt chặn" cho `Exception.class`**:
  * Ghi log đầy đủ stack trace phía server.
  * Trả cho client thông điệp chung chung, KHÔNG trả `ex.getMessage()` hay stack trace của lỗi hệ thống.
* Không `catch` exception rồi im lặng bỏ qua (nuốt lỗi) trong Service. Hoặc xử lý thật, hoặc ném tiếp.
* Đặt tên class lỗi là `ApiErrorResponse`, tránh trùng với interface `org.springframework.web.ErrorResponse` có sẵn trong Spring 6.
* **Tìm hiểu thêm**: Spring 6 hỗ trợ sẵn `ProblemDetail` theo chuẩn RFC 9457, có thể dùng thay record tự định nghĩa nếu team thống nhất.

#### 4. Demo Thực chiến

```java
public record ApiErrorResponse(
        LocalDateTime timestamp,
        int status,
        String errorCode,
        String message,
        String path,
        Map<String, String> fieldErrors) {

    public static ApiErrorResponse of(HttpStatus status, String errorCode, String message, String path) {
        return new ApiErrorResponse(LocalDateTime.now(), status.value(), errorCode, message, path, Map.of());
    }

    public static ApiErrorResponse withFieldErrors(
            HttpStatus status, String errorCode, String message, String path, Map<String, String> fieldErrors) {
        return new ApiErrorResponse(LocalDateTime.now(), status.value(), errorCode, message, path, fieldErrors);
    }
}
```

```java
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

```java
public class BusinessException extends RuntimeException {

    private final String errorCode;

    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
```

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    private static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    private static final String MALFORMED_REQUEST = "MALFORMED_REQUEST";
    private static final String INTERNAL_ERROR = "INTERNAL_ERROR";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> fieldErrors.putIfAbsent(error.getField(), error.getDefaultMessage()));

        ApiErrorResponse body = ApiErrorResponse.withFieldErrors(
                HttpStatus.BAD_REQUEST, VALIDATION_ERROR, "Dữ liệu đầu vào không hợp lệ",
                request.getRequestURI(), fieldErrors);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {
        ApiErrorResponse body = ApiErrorResponse.of(
                HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest request) {
        ApiErrorResponse body = ApiErrorResponse.of(
                HttpStatus.CONFLICT, ex.getErrorCode(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleMalformedJson(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        ApiErrorResponse body = ApiErrorResponse.of(
                HttpStatus.BAD_REQUEST, MALFORMED_REQUEST, "Body JSON sai cú pháp hoặc sai kiểu dữ liệu",
                request.getRequestURI());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
        LOGGER.error("Lỗi không mong muốn tại {}", request.getRequestURI(), ex);
        ApiErrorResponse body = ApiErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_ERROR, "Hệ thống đang gặp sự cố, vui lòng thử lại sau",
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
```

**Giải thích chi tiết:**

* **Record `ApiErrorResponse`**:
  * Định dạng JSON lỗi DUY NHẤT của toàn hệ thống.
  * `errorCode`: mã lỗi dạng chuỗi cố định để Frontend dùng `switch`, không phụ thuộc vào câu chữ của `message`.
  * `fieldErrors`: chỉ có dữ liệu với lỗi validation, các lỗi khác là `Map` rỗng.
  * Hai static factory `of()` và `withFieldErrors()`: tạo object gọn, tự gán `timestamp`.
* **`ResourceNotFoundException`, `BusinessException`**: exception nghiệp vụ kế thừa `RuntimeException` (*unchecked*), Service chỉ việc `throw` mà không cần khai báo `throws`.
* **`@RestControllerAdvice` trên `GlobalExceptionHandler`**: áp dụng cho MỌI Controller trong ứng dụng.
* **Các hằng số `VALIDATION_ERROR`, `RESOURCE_NOT_FOUND`...**: tuân thủ `UPPER_SNAKE_CASE`, tránh lặp chuỗi.
* **`handleValidation`**:
  * Bắt `MethodArgumentNotValidException` (lỗi do `@Valid`).
  * `ex.getBindingResult().getFieldErrors()`: danh sách lỗi từng field.
  * `putIfAbsent`: một field vi phạm nhiều luật chỉ giữ lỗi đầu tiên, tránh ghi đè. `LinkedHashMap` giữ đúng thứ tự.
  * Trả về HTTP 400 kèm `fieldErrors`.
* **`handleNotFound`**: trả HTTP 404, `message` lấy từ exception vì đây là thông điệp nghiệp vụ do ta tự viết.
* **`handleBusiness`**: trả HTTP 409, dùng `errorCode` riêng của từng tình huống nghiệp vụ.
* **`handleMalformedJson`**: bắt lỗi JSON sai cú pháp, hoặc gửi chữ vào field số (`"giaTour": "abc"`).
* **`handleUnexpected`**:
  * "Chốt chặn" cuối cho mọi exception chưa có handler riêng.
  * `LOGGER.error(..., ex)`: truyền `ex` là tham số cuối để log ghi đầy đủ stack trace.
  * Thông điệp trả về chung chung, không lộ chi tiết kỹ thuật.
* **Gửi lại request lỗi ở mục IV.A, giờ nhận được**:

```json
{
  "timestamp": "2026-10-12T22:30:00.123",
  "status": 400,
  "errorCode": "VALIDATION_ERROR",
  "message": "Dữ liệu đầu vào không hợp lệ",
  "path": "/api/reviews",
  "fieldErrors": {
    "noiDung": "Nội dung đánh giá không được để trống",
    "soSao": "Số sao tối đa là 5"
  }
}
```

---

## V. Bài tập thực hành tổng hợp (Dự án Quản lý Tour)

**Bối cảnh dự án:**

* Project "Phần mềm quản lý đặt tour du lịch" đã có các Entity cơ bản: `Tour`, `KhachHang`, `HoaDon` (theo thiết kế CSDL ở Bài tập Buổi 2).
* Nhiệm vụ: hoàn thiện quan hệ, viết các API truy vấn, tối ưu hiệu năng, và bổ sung validation, xử lý lỗi.

**Ghi chú về thiết kế:**

* Trong lược đồ E-R đầy đủ, `HoaDon` liên kết với `LichTrinh`, và `LichTrinh` mới liên kết với `Tour`.
* Bài tập này ĐƠN GIẢN HÓA: `HoaDon` liên kết trực tiếp với `Tour`, để tập trung vào kỹ thuật JPA.
* Sau khi làm xong, học viên có thể tự nâng cấp lên mô hình có `LichTrinh` như một bài tập mở rộng.

![Mô hình Entity của bài tập](images/12-erd-baitap.png)

**Cấu trúc package đề xuất:**

```text
com.example.tourbooking
    controller
        TourController
        KhachHangController
        HoaDonController
    service
        TourService
        KhachHangService
        HoaDonService
    repository
        TourRepository
        KhachHangRepository
        HoaDonRepository
        projection
            TourThongKe
    entity
        Tour
        KhachHang
        HoaDon
    dto
        request
            TourCreateRequest
            MuaVeRequest
        response
            TourSummaryResponse
            TourDetailResponse
            HoaDonResponse
            KhachHangResponse
            PageResponse
            ApiErrorResponse
    exception
        ResourceNotFoundException
        BusinessException
        GlobalExceptionHandler
```

**Cấu hình `application.properties`:**

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tour_booking?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=123456
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false
```

* `ddl-auto=update`: Hibernate tự tạo và cập nhật bảng theo Entity. Chỉ dùng khi học tập, production phải dùng Flyway hoặc Liquibase.
* `show-sql=true`: BẮT BUỘC bật trong suốt bài tập để đếm số câu SQL ở Yêu cầu 3.

### A. Yêu cầu 1 (Thiết lập quan hệ)

**Mục tiêu:**

* Quan hệ 1-N giữa `Tour` và `HoaDon`: một tour có nhiều hóa đơn, mỗi hóa đơn thuộc một tour.
* Quan hệ N-N giữa `KhachHang` và `Tour` qua bảng trung gian `lich_su_mua_ve`: ghi nhận khách hàng nào đã từng mua tour nào.

#### Bước 1: Xác định Owning side trước khi code

* **Quan hệ `Tour` - `HoaDon` (1-N)**:
  * Khóa ngoại `tour_id` nằm ở bảng `hoa_don`, nên `HoaDon` là Owning side (`@ManyToOne` + `@JoinColumn`).
  * `Tour` là Inverse side (`@OneToMany(mappedBy = "tour")`).
* **Quan hệ `KhachHang` - `HoaDon` (1-N, đã có trong thiết kế Buổi 2)**:
  * Tương tự, `HoaDon` giữ khóa ngoại `khach_hang_id`.
* **Quan hệ `KhachHang` - `Tour` (*N-N*)**:
  * Chọn `KhachHang` làm Owning side, vì nghiệp vụ "ghi lịch sử mua" xuất phát từ phía khách hàng.
  * `KhachHang` khai báo `@JoinTable(name = "lich_su_mua_ve")`, `Tour` khai báo `mappedBy`.

#### Bước 2: Cập nhật Entity `Tour`

```java
@Entity
@Table(name = "tour")
@Getter
@Setter
@NoArgsConstructor
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_tour", nullable = false, unique = true, length = 255)
    private String tenTour;

    @Column(name = "noi_xuat_phat", nullable = false, length = 100)
    private String noiXuatPhat;

    @Column(name = "noi_den", nullable = false, length = 100)
    private String noiDen;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "gia_tour", nullable = false, precision = 15, scale = 2)
    private BigDecimal giaTour;

    @OneToMany(mappedBy = "tour")
    private List<HoaDon> hoaDons = new ArrayList<>();

    @ManyToMany(mappedBy = "toursDaMua")
    private Set<KhachHang> khachHangs = new HashSet<>();

    public void addHoaDon(HoaDon hoaDon) {
        hoaDons.add(hoaDon);
        hoaDon.setTour(this);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Tour tour)) {
            return false;
        }
        return id != null && id.equals(tour.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
```

* **`giaTour` dùng `BigDecimal` với `precision = 15, scale = 2`**: KHÔNG dùng `double` cho tiền vì sai số dấu phẩy động.
* **`@OneToMany(mappedBy = "tour")` KHÔNG có `cascade`**:
  * Hóa đơn là chứng từ tài chính, không được tự động xóa khi xóa tour.
  * Tour đã có hóa đơn thì nên chặn xóa ở tầng Service, hoặc chỉ "ngừng kinh doanh" (*soft delete*).
* **`@ManyToMany(mappedBy = "toursDaMua")`**: Inverse side, trỏ về thuộc tính `toursDaMua` của `KhachHang`.
* **`equals()` và `hashCode()` theo `id`**: bắt buộc vì `Tour` được đặt trong `Set<Tour>` bên `KhachHang`.

#### Bước 3: Cập nhật Entity `HoaDon`

```java
@Entity
@Table(name = "hoa_don")
@Getter
@Setter
@NoArgsConstructor
public class HoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "so_luong_khach", nullable = false)
    private Integer soLuongKhach;

    @Column(name = "gia_ve", nullable = false, precision = 15, scale = 2)
    private BigDecimal giaVe;

    @Column(name = "tong_tien", nullable = false, precision = 15, scale = 2)
    private BigDecimal tongTien;

    @Column(name = "ngay_tao", nullable = false, updatable = false)
    private LocalDateTime ngayTao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khach_hang_id", nullable = false)
    private KhachHang khachHang;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
        tongTien = giaVe.multiply(BigDecimal.valueOf(soLuongKhach));
    }
}
```

* **`giaVe`**: lưu giá TẠI THỜI ĐIỂM MUA, đúng nguyên tắc 3NF đã phân tích ở Buổi 2. Đổi giá tour sau này không làm sai hóa đơn cũ.
* `@ManyToOne(fetch = FetchType.LAZY)` trên cả `tour` và `khachHang`: ghi đè mặc định `EAGER`, tránh N+1 ngay từ thiết kế.
* **`@JoinColumn(name = "tour_id", nullable = false)`**: hóa đơn bắt buộc thuộc về một tour.
* **`@PrePersist`**: JPA Lifecycle Callback, được gọi tự động ngay trước câu `INSERT`.
  * Gán `ngayTao` bằng thời điểm hiện tại.
  * Tính `tongTien = giaVe * soLuongKhach` (thuộc tính dẫn xuất trong lược đồ E-R).
* **`updatable = false` trên `ngayTao`**: Hibernate không bao giờ đưa cột này vào câu `UPDATE`.

#### Bước 4: Cập nhật Entity `KhachHang`

```java
@Entity
@Table(name = "khach_hang")
@Getter
@Setter
@NoArgsConstructor
public class KhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_khach_hang", nullable = false, length = 150)
    private String tenKhachHang;

    @Column(name = "so_id", nullable = false, unique = true, length = 20)
    private String soId;

    @Column(name = "loai_the_id", nullable = false, length = 20)
    private String loaiTheId;

    @Column(name = "so_dien_thoai", length = 15)
    private String soDienThoai;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "dia_chi", length = 255)
    private String diaChi;

    @OneToMany(mappedBy = "khachHang")
    private List<HoaDon> hoaDons = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "lich_su_mua_ve",
            joinColumns = @JoinColumn(name = "khach_hang_id"),
            inverseJoinColumns = @JoinColumn(name = "tour_id")
    )
    private Set<Tour> toursDaMua = new HashSet<>();

    public void ghiNhanLichSuMua(Tour tour) {
        toursDaMua.add(tour);
        tour.getKhachHangs().add(this);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof KhachHang khachHang)) {
            return false;
        }
        return id != null && id.equals(khachHang.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
```

* **`@ManyToMany` + `@JoinTable(name = "lich_su_mua_ve")`**: `KhachHang` là Owning side. Hibernate tự tạo bảng `lich_su_mua_ve (khach_hang_id, tour_id)`.
* **Không khai báo `cascade` cho `toursDaMua`**: xóa khách hàng không được kéo theo xóa tour.
* **`Set<Tour>`**: một khách mua cùng một tour nhiều lần, bảng lịch sử chỉ ghi nhận một dòng, đúng ngữ nghĩa "đã từng mua". Chi tiết từng lần mua đã nằm trong `HoaDon`.
* **`ghiNhanLichSuMua()`**: helper method đồng bộ hai phía của quan hệ N-N.
* **Lưu ý tư duy thiết kế**:
  * Nếu cần lưu thêm "ngày mua", "số lần mua" vào bảng lịch sử, phải bỏ `@ManyToMany` và tạo Entity trung gian (xem mục I.B).
  * Bài tập dùng `@ManyToMany` với mục đích luyện tập annotation.

#### Bước 5: Viết nghiệp vụ mua vé để kiểm chứng hai quan hệ

```java
public record MuaVeRequest(

        @NotNull(message = "Mã khách hàng không được để trống")
        Long khachHangId,

        @NotNull(message = "Mã tour không được để trống")
        Long tourId,

        @NotNull(message = "Số lượng khách không được để trống")
        @Min(value = 1, message = "Số lượng khách tối thiểu là 1")
        Integer soLuongKhach) {
}
```

```java
@Service
@RequiredArgsConstructor
public class HoaDonService {

    private final HoaDonRepository hoaDonRepository;
    private final TourRepository tourRepository;
    private final KhachHangRepository khachHangRepository;

    @Transactional
    public HoaDonResponse muaVe(MuaVeRequest request) {
        Tour tour = tourRepository.findById(request.tourId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tour id = " + request.tourId()));
        KhachHang khachHang = khachHangRepository.findById(request.khachHangId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy khách hàng id = " + request.khachHangId()));

        HoaDon hoaDon = new HoaDon();
        hoaDon.setSoLuongKhach(request.soLuongKhach());
        hoaDon.setGiaVe(tour.getGiaTour());
        hoaDon.setKhachHang(khachHang);
        tour.addHoaDon(hoaDon);

        khachHang.ghiNhanLichSuMua(tour);

        HoaDon savedHoaDon = hoaDonRepository.save(hoaDon);
        return HoaDonResponse.fromEntity(savedHoaDon);
    }
}
```

* **Hai câu `findById`**: kiểm tra tour và khách hàng tồn tại, nếu không ném `ResourceNotFoundException` (HTTP 404).
* **`hoaDon.setGiaVe(tour.getGiaTour())`**: chụp lại giá tour tại thời điểm mua.
* **`tour.addHoaDon(hoaDon)`**: đồng bộ hai phía quan hệ 1-N, quan trọng nhất là dòng `hoaDon.setTour(this)` bên trong.
* **`khachHang.ghiNhanLichSuMua(tour)`**: thêm tour vào `Set`. Khi commit, Hibernate sinh `INSERT INTO lich_su_mua_ve` (nếu cặp này chưa có).
* **`hoaDonRepository.save(hoaDon)`**: sinh `INSERT INTO hoa_don`, `@PrePersist` tự tính `tongTien`.
* **`HoaDonResponse.fromEntity(...)`**: định nghĩa ở Yêu cầu 2. Đổi Entity sang DTO ngay trong transaction.
* **Lưu ý hiệu năng của helper method trên Collection Lazy**:
  * Thêm phần tử vào `Set` Lazy buộc Hibernate load toàn bộ `Set` để kiểm tra trùng lặp.
  * Với dữ liệu bài tập thì chấp nhận được. Với hệ thống lớn, có thể chỉ cập nhật Owning side, sẽ học kỹ ở khóa nâng cao.

#### Bước 6: Dữ liệu mẫu và kiểm tra kết quả

```sql
INSERT INTO tour (ten_tour, noi_xuat_phat, noi_den, mo_ta, gia_tour) VALUES
('Đà Nẵng - Hội An 3N2Đ', 'Hà Nội', 'Đà Nẵng', 'Tham quan Bà Nà, phố cổ Hội An', 4500000),
('Phú Quốc 4N3Đ', 'TP.HCM', 'Phú Quốc', 'Nghỉ dưỡng biển', 6200000),
('Sapa 2N1Đ', 'Hà Nội', 'Lào Cai', 'Chinh phục Fansipan', 2800000);

INSERT INTO khach_hang (ten_khach_hang, so_id, loai_the_id, so_dien_thoai, email, dia_chi) VALUES
('Nguyễn Văn An', '001099000001', 'CCCD', '0901000001', 'an@gmail.com', 'Hà Nội'),
('Trần Thị Bình', '001099000002', 'CCCD', '0901000002', 'binh@gmail.com', 'TP.HCM'),
('Lê Văn Nam', 'B1234567', 'PASSPORT', '0901000003', 'nam@gmail.com', 'Đà Nẵng');
```

* **Khởi động ứng dụng, quan sát log**: Hibernate sinh `create table hoa_don`, `create table lich_su_mua_ve` cùng các khóa ngoại.
* Chạy script dữ liệu mẫu ở trên.
* Gọi API mua vé (Controller viết ở Yêu cầu 2) và kiểm tra bảng `hoa_don` và `lich_su_mua_ve` đều có dữ liệu.

### B. Yêu cầu 2 (Truy vấn Cơ bản & Nâng cao)

**Mục tiêu:**

* API tìm khách hàng theo tên (*Derived Query*).
* API tìm hóa đơn có tổng tiền lớn hơn X (*JPQL*).
* API thống kê Tour bằng lệnh JOIN (*Native Query*).

#### Bước 1: Tạo các DTO Response

```java
public record KhachHangResponse(Long id, String tenKhachHang, String soDienThoai, String email) {

    public static KhachHangResponse fromEntity(KhachHang khachHang) {
        return new KhachHangResponse(
                khachHang.getId(),
                khachHang.getTenKhachHang(),
                khachHang.getSoDienThoai(),
                khachHang.getEmail());
    }
}
```

```java
public record HoaDonResponse(
        Long id,
        String tenTour,
        String tenKhachHang,
        Integer soLuongKhach,
        BigDecimal giaVe,
        BigDecimal tongTien,
        LocalDateTime ngayTao) {

    public static HoaDonResponse fromEntity(HoaDon hoaDon) {
        return new HoaDonResponse(
                hoaDon.getId(),
                hoaDon.getTour().getTenTour(),
                hoaDon.getKhachHang().getTenKhachHang(),
                hoaDon.getSoLuongKhach(),
                hoaDon.getGiaVe(),
                hoaDon.getTongTien(),
                hoaDon.getNgayTao());
    }
}
```

* Không trả Entity ra JSON, tránh vòng lặp vô hạn `Tour -> hoaDons -> tour` và tránh lộ dữ liệu nhạy cảm (`soId`).
* **CẢNH BÁO trong `HoaDonResponse.fromEntity()`**:
  * `hoaDon.getTour().getTenTour()` và `hoaDon.getKhachHang().getTenKhachHang()` đều đánh thức proxy Lazy.
  * Nếu map một danh sách 50 hóa đơn mà không fetch trước, sẽ sinh tới 1 + 50 + 50 câu SQL. Bước 3 sẽ xử lý điều này.

#### Bước 2: Derived Query: tìm khách hàng theo tên

```java
public interface KhachHangRepository extends JpaRepository<KhachHang, Long> {

    List<KhachHang> findByTenKhachHangContainingIgnoreCaseOrderByTenKhachHangAsc(String tenKhachHang);
}
```

```java
@Service
@RequiredArgsConstructor
public class KhachHangService {

    private final KhachHangRepository khachHangRepository;

    @Transactional(readOnly = true)
    public List<KhachHangResponse> timTheoTen(String tenKhachHang) {
        return khachHangRepository.findByTenKhachHangContainingIgnoreCaseOrderByTenKhachHangAsc(tenKhachHang.trim())
                .stream()
                .map(KhachHangResponse::fromEntity)
                .toList();
    }
}
```

```java
@RestController
@RequestMapping("/api/khach-hang")
@RequiredArgsConstructor
public class KhachHangController {

    private final KhachHangService khachHangService;

    @GetMapping("/search")
    public List<KhachHangResponse> timTheoTen(@RequestParam("ten") String tenKhachHang) {
        return khachHangService.timTheoTen(tenKhachHang);
    }
}
```

* **Tên method được Parser tách thành**:
  * `find...By`: câu `SELECT`.
  * `TenKhachHang` + `Containing` + `IgnoreCase`: `UPPER(ten_khach_hang) LIKE UPPER('%giá_trị%')`.
  * `OrderByTenKhachHangAsc`: `ORDER BY ten_khach_hang ASC`.
* **`tenKhachHang.trim()`**: bỏ khoảng trắng thừa do người dùng gõ.
* **`@RequestParam("ten")`**: URL dùng tham số ngắn gọn `ten`, ánh xạ vào biến `tenKhachHang`.
* **Kiểm thử**: `GET /api/khach-hang/search?ten=van` trả về "Lê Văn Nam" và "Nguyễn Văn An".
  * Lưu ý: tìm "van" khớp "Văn" hay không phụ thuộc collation của MySQL (ví dụ `utf8mb4_unicode_ci` bỏ qua dấu).

#### Bước 3: JPQL: tìm hóa đơn có tổng tiền lớn hơn X

```java
public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {

    @Query("""
            SELECT h FROM HoaDon h
            JOIN FETCH h.tour
            JOIN FETCH h.khachHang
            WHERE h.tongTien > :mucTien
            ORDER BY h.tongTien DESC
            """)
    List<HoaDon> findHoaDonCoTongTienLonHon(@Param("mucTien") BigDecimal mucTien);
}
```

```java
@Transactional(readOnly = true)
public List<HoaDonResponse> timHoaDonTheoTongTien(BigDecimal mucTien) {
    if (mucTien.signum() < 0) {
        throw new BusinessException("MUC_TIEN_KHONG_HOP_LE", "Mức tiền phải lớn hơn hoặc bằng 0");
    }
    return hoaDonRepository.findHoaDonCoTongTienLonHon(mucTien).stream()
            .map(HoaDonResponse::fromEntity)
            .toList();
}
```

```java
@RestController
@RequestMapping("/api/hoa-don")
@RequiredArgsConstructor
public class HoaDonController {

    private final HoaDonService hoaDonService;

    @GetMapping
    public List<HoaDonResponse> timTheoTongTien(@RequestParam("tongTienToiThieu") BigDecimal mucTien) {
        return hoaDonService.timHoaDonTheoTongTien(mucTien);
    }

    @PostMapping
    public ResponseEntity<HoaDonResponse> muaVe(@Valid @RequestBody MuaVeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hoaDonService.muaVe(request));
    }
}
```

* Câu JPQL dùng tên Entity `HoaDon` và thuộc tính `tongTien`, không dùng `hoa_don` hay `tong_tien`.
* **`JOIN FETCH h.tour` và `JOIN FETCH h.khachHang`**:
  * Áp dụng ngay kiến thức Mục III: nạp sẵn hai quan hệ `@ManyToOne` mà `HoaDonResponse` sẽ dùng.
  * Kết quả: chỉ 1 câu SQL dù có bao nhiêu hóa đơn.
* **`:mucTien` và `@Param("mucTien")`**: named parameter, an toàn trước SQL Injection.
* Service kiểm tra `mucTien` âm và ném `BusinessException`. Đây là luật nghiệp vụ, không phải lỗi định dạng.
* Controller có thêm endpoint `POST /api/hoa-don` để gọi nghiệp vụ mua vé của Yêu cầu 1.
* **Kiểm thử**: `GET /api/hoa-don?tongTienToiThieu=10000000`.

#### Bước 4: Native Query: thống kê Tour bằng JOIN

```java
public interface TourThongKe {

    Long getTourId();

    String getTenTour();

    String getNoiDen();

    Long getSoHoaDon();

    BigDecimal getTongDoanhThu();
}
```

```java
public interface TourRepository extends JpaRepository<Tour, Long> {

    boolean existsByTenTour(String tenTour);

    @Query(value = """
            SELECT t.id AS tourId,
                   t.ten_tour AS tenTour,
                   t.noi_den AS noiDen,
                   COUNT(h.id) AS soHoaDon,
                   COALESCE(SUM(h.tong_tien), 0) AS tongDoanhThu
            FROM tour t
            LEFT JOIN hoa_don h ON h.tour_id = t.id
            GROUP BY t.id, t.ten_tour, t.noi_den
            ORDER BY tongDoanhThu DESC
            """, nativeQuery = true)
    List<TourThongKe> thongKeDoanhThuTheoTour();
}
```

```java
@Transactional(readOnly = true)
public List<TourThongKe> thongKeDoanhThu() {
    return tourRepository.thongKeDoanhThuTheoTour();
}
```

```java
@GetMapping("/thong-ke")
public List<TourThongKe> thongKeTour() {
    return tourService.thongKeDoanhThu();
}
```

* **Interface Projection `TourThongKe`**: 5 getter tương ứng 5 alias trong câu SQL (`tourId`, `tenTour`, `noiDen`, `soHoaDon`, `tongDoanhThu`).
* **`nativeQuery = true`**: câu lệnh gửi nguyên văn xuống MySQL, dùng tên bảng `tour`, `hoa_don` và tên cột thật.
* **`LEFT JOIN`**: tour chưa bán được hóa đơn nào vẫn xuất hiện với `soHoaDon = 0`.
* **`COALESCE(SUM(...), 0)`**: tour chưa có doanh thu trả về `0` thay vì `null`.
* **`GROUP BY t.id, t.ten_tour, t.noi_den`**: liệt kê đủ các cột không nằm trong hàm tổng hợp (yêu cầu của chế độ `ONLY_FULL_GROUP_BY` của MySQL).
* Projection là interface chỉ đọc, an toàn để trả thẳng ra JSON. `TourService.thongKeDoanhThu()` chỉ cần gọi lại Repository.
* **Kiểm thử**: `GET /api/tours/thong-ke`.

### C. Yêu cầu 3 (Tối ưu & Phân trang)

**Mục tiêu:**

* API danh sách Tour trả về dạng phân trang theo `page`, `size`.
* API chi tiết Tour kèm danh sách Hóa đơn, chỉ dùng 1 câu SQL (không N+1).

#### Bước 1: Chuẩn bị DTO cho danh sách và chi tiết

```java
public record TourSummaryResponse(
        Long id,
        String tenTour,
        String noiXuatPhat,
        String noiDen,
        BigDecimal giaTour) {

    public static TourSummaryResponse fromEntity(Tour tour) {
        return new TourSummaryResponse(
                tour.getId(),
                tour.getTenTour(),
                tour.getNoiXuatPhat(),
                tour.getNoiDen(),
                tour.getGiaTour());
    }
}
```

```java
public record TourDetailResponse(
        Long id,
        String tenTour,
        String noiXuatPhat,
        String noiDen,
        String moTa,
        BigDecimal giaTour,
        List<HoaDonResponse> hoaDons) {

    public static TourDetailResponse fromEntity(Tour tour) {
        List<HoaDonResponse> hoaDonResponses = tour.getHoaDons().stream()
                .map(HoaDonResponse::fromEntity)
                .toList();
        return new TourDetailResponse(
                tour.getId(),
                tour.getTenTour(),
                tour.getNoiXuatPhat(),
                tour.getNoiDen(),
                tour.getMoTa(),
                tour.getGiaTour(),
                hoaDonResponses);
    }
}
```

* **`TourSummaryResponse`**: chỉ chứa field cơ bản, không đụng tới quan hệ, nên không kích hoạt Lazy Load.
* **`TourDetailResponse.fromEntity()`**:
  * Duyệt `tour.getHoaDons()` (*Collection Lazy*).
  * Với mỗi hóa đơn lại gọi `HoaDonResponse.fromEntity()`, chạm vào `hoaDon.getTour()` và `hoaDon.getKhachHang()`.
  * Đây chính là "mỏ" N+1 hai tầng cần được xử lý ở Bước 3.
* **`PageResponse<T>`**: dùng lại record đã viết ở mục III.B.

#### Bước 2: Sửa API danh sách Tour thành phân trang

```java
@Service
@RequiredArgsConstructor
public class TourService {

    private static final int MAX_PAGE_SIZE = 50;

    private final TourRepository tourRepository;

    @Transactional(readOnly = true)
    public PageResponse<TourSummaryResponse> layDanhSachTour(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<TourSummaryResponse> result = tourRepository.findAll(pageable)
                .map(TourSummaryResponse::fromEntity);
        return PageResponse.from(result);
    }
}
```

```java
@RestController
@RequestMapping("/api/tours")
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;

    @GetMapping
    public PageResponse<TourSummaryResponse> layDanhSachTour(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return tourService.layDanhSachTour(page, size);
    }
}
```

* **`findAll(Pageable)`**: có sẵn trong `JpaRepository` (kế thừa từ `PagingAndSortingRepository`), không cần khai báo thêm.
* **`MAX_PAGE_SIZE = 50`**: chặn client xin quá nhiều dữ liệu.
* **`Sort.by(Sort.Direction.DESC, "id")`**: tour mới nhất lên đầu, thứ tự luôn ổn định giữa các trang.
* **`@RequestParam(defaultValue = "0")`**: client không truyền thì mặc định trang đầu tiên.
* **SQL kỳ vọng khi gọi `GET /api/tours?page=1&size=2`**:
  * `SELECT ... FROM tour ORDER BY id DESC LIMIT 2 OFFSET 2`.
  * `SELECT COUNT(t.id) FROM tour t`.
* **Response mẫu**:

```json
{
  "content": [
    { "id": 1, "tenTour": "Đà Nẵng - Hội An 3N2Đ", "noiXuatPhat": "Hà Nội", "noiDen": "Đà Nẵng", "giaTour": 4500000.00 }
  ],
  "page": 1,
  "size": 2,
  "totalElements": 3,
  "totalPages": 2,
  "last": true
}
```

#### Bước 3: API chi tiết Tour kèm Hóa đơn, chống N+1

**Cách 1 - JOIN FETCH:**

```java
@Query("""
        SELECT t FROM Tour t
        LEFT JOIN FETCH t.hoaDons h
        LEFT JOIN FETCH h.khachHang
        WHERE t.id = :tourId
        """)
Optional<Tour> findDetailById(@Param("tourId") Long tourId);
```

**Cách 2 - @EntityGraph:**

```java
@EntityGraph(attributePaths = {"hoaDons", "hoaDons.khachHang"})
Optional<Tour> findWithHoaDonsById(Long id);
```

**Service và Controller:**

```java
@Transactional(readOnly = true)
public TourDetailResponse layChiTietTour(Long tourId) {
    Tour tour = tourRepository.findDetailById(tourId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tour id = " + tourId));
    return TourDetailResponse.fromEntity(tour);
}
```

```java
@GetMapping("/{tourId}")
public TourDetailResponse layChiTietTour(@PathVariable Long tourId) {
    return tourService.layChiTietTour(tourId);
}
```

* **Cách 1, `LEFT JOIN FETCH t.hoaDons h`**:
  * Nạp toàn bộ hóa đơn của tour vào `tour.hoaDons` ngay trong câu query.
  * `LEFT` để tour chưa có hóa đơn vẫn được trả về (danh sách rỗng), thay vì báo 404.
  * Alias `h` trên fetch join là mở rộng của Hibernate, cho phép fetch tiếp tầng sâu hơn.
* **Cách 1, `LEFT JOIN FETCH h.khachHang`**:
  * Nạp tiếp khách hàng của từng hóa đơn, xử lý N+1 tầng thứ hai.
  * `hoaDon.getTour()` KHÔNG cần fetch thêm: hóa đơn trỏ về đúng tour vừa load, Hibernate lấy từ First-level Cache.
* **Cách 2, `@EntityGraph(attributePaths = {"hoaDons", "hoaDons.khachHang"})`**:
  * Cùng hiệu quả, khai báo bằng annotation trên Derived Query `findWithHoaDonsById`.
  * `"hoaDons.khachHang"`: dấu chấm để chỉ quan hệ lồng nhau.
* Chỉ chọn MỘT trong hai cách cho Service. Code mẫu dùng Cách 1.
* Không phân trang ở API này, nên không gặp cảnh báo `HHH90003004`. Nếu một tour có hàng chục nghìn hóa đơn, cần tách thành API riêng `GET /api/tours/{tourId}/hoa-don?page=&size=`.

#### Bước 4: Đo lường trước và sau tối ưu

* Tạm thời đổi Service sang `tourRepository.findById(tourId)` rồi gọi `GET /api/tours/1` với tour có 3 hóa đơn của 3 khách hàng khác nhau:
  * 1 câu `SELECT` bảng `tour`.
  * 1 câu `SELECT` bảng `hoa_don WHERE tour_id = ?`.
  * 3 câu `SELECT` bảng `khach_hang WHERE id = ?`.
  * Tổng: 5 câu SQL, tăng tuyến tính theo số khách hàng.
* **Đổi lại `findDetailById(tourId)` và gọi lại**:
  * Đúng 1 câu `SELECT ... FROM tour t LEFT JOIN hoa_don h ON ... LEFT JOIN khach_hang k ON ... WHERE t.id = ?`.
* Ghi lại số câu SQL trước và sau vào báo cáo bài tập.

### D. Yêu cầu 4 (Validation & Exception)

**Mục tiêu:**

* Validation khi tạo mới Tour: tên không trống, giá lớn hơn 0.
* `GlobalExceptionHandler` hứng lỗi Validation và trả về JSON chuẩn.

#### Bước 1: Tạo Request DTO có Validation

```java
public record TourCreateRequest(

        @NotBlank(message = "Tên tour không được để trống")
        @Size(max = 255, message = "Tên tour tối đa 255 ký tự")
        String tenTour,

        @NotBlank(message = "Nơi xuất phát không được để trống")
        String noiXuatPhat,

        @NotBlank(message = "Nơi đến không được để trống")
        String noiDen,

        String moTa,

        @NotNull(message = "Giá tour không được để trống")
        @Positive(message = "Giá tour phải lớn hơn 0")
        @Digits(integer = 13, fraction = 2, message = "Giá tour tối đa 13 chữ số phần nguyên và 2 chữ số thập phân")
        BigDecimal giaTour) {
}
```

* **`@NotBlank` trên `tenTour`**: chặn `null`, `""` và `"   "`.
* **`@Size(max = 255)`**: khớp độ dài cột `ten_tour`, tránh lỗi `Data too long` từ MySQL.
* **`@NotNull` + `@Positive` trên `giaTour`**:
  * `@Positive` coi `null` là hợp lệ, nên bắt buộc phải có thêm `@NotNull`.
  * `@Positive` nghĩa là lớn hơn 0 (không tính 0). Tương đương `@DecimalMin(value = "0", inclusive = false)`.
* **`@Digits(integer = 13, fraction = 2)`**: khớp `precision = 15, scale = 2` của cột `gia_tour`.
* **`moTa` không có annotation**: trường không bắt buộc.

#### Bước 2: Viết Service tạo Tour

```java
@Transactional
public TourSummaryResponse taoTour(TourCreateRequest request) {
    String tenTour = request.tenTour().trim();
    if (tourRepository.existsByTenTour(tenTour)) {
        throw new BusinessException("TOUR_DA_TON_TAI", "Tour với tên '" + tenTour + "' đã tồn tại");
    }

    Tour tour = new Tour();
    tour.setTenTour(tenTour);
    tour.setNoiXuatPhat(request.noiXuatPhat().trim());
    tour.setNoiDen(request.noiDen().trim());
    tour.setMoTa(request.moTa());
    tour.setGiaTour(request.giaTour());

    Tour savedTour = tourRepository.save(tour);
    return TourSummaryResponse.fromEntity(savedTour);
}
```

* Validation annotation chỉ kiểm tra ĐỊNH DẠNG. Luật "tên tour không trùng" cần truy vấn DB nên phải nằm ở Service.
* **`existsByTenTour`**: Derived Query trả về `boolean`, chỉ kiểm tra tồn tại nên rất nhẹ.
* Ném `BusinessException` với `errorCode = "TOUR_DA_TON_TAI"`, `GlobalExceptionHandler` chuyển thành HTTP 409.
* Ràng buộc `unique = true` ở cột `ten_tour` vẫn giữ làm lưới an toàn cuối, phòng hai request tạo cùng lúc.

#### Bước 3: Thêm endpoint tạo Tour vào Controller

```java
@PostMapping
public ResponseEntity<TourSummaryResponse> taoTour(@Valid @RequestBody TourCreateRequest request) {
    TourSummaryResponse created = tourService.taoTour(request);
    URI location = URI.create("/api/tours/" + created.id());
    return ResponseEntity.created(location).body(created);
}
```

* **`@Valid @RequestBody`**: kích hoạt validation. Thiếu `@Valid`, mọi annotation ở Bước 1 vô tác dụng.
* **`ResponseEntity.created(location)`**: trả HTTP 201 kèm header `Location` trỏ tới tài nguyên vừa tạo, đúng chuẩn REST.

#### Bước 4: Hoàn thiện `GlobalExceptionHandler`

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    private static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    private static final String MALFORMED_REQUEST = "MALFORMED_REQUEST";
    private static final String INVALID_PARAMETER = "INVALID_PARAMETER";
    private static final String INTERNAL_ERROR = "INTERNAL_ERROR";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> fieldErrors.putIfAbsent(error.getField(), error.getDefaultMessage()));
        ApiErrorResponse body = ApiErrorResponse.withFieldErrors(
                HttpStatus.BAD_REQUEST, VALIDATION_ERROR, "Dữ liệu đầu vào không hợp lệ",
                request.getRequestURI(), fieldErrors);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {
        ApiErrorResponse body = ApiErrorResponse.of(
                HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest request) {
        ApiErrorResponse body = ApiErrorResponse.of(
                HttpStatus.CONFLICT, ex.getErrorCode(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleMalformedJson(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        ApiErrorResponse body = ApiErrorResponse.of(
                HttpStatus.BAD_REQUEST, MALFORMED_REQUEST, "Body JSON sai cú pháp hoặc sai kiểu dữ liệu",
                request.getRequestURI());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = "Tham số '" + ex.getName() + "' có giá trị không hợp lệ: " + ex.getValue();
        ApiErrorResponse body = ApiErrorResponse.of(
                HttpStatus.BAD_REQUEST, INVALID_PARAMETER, message, request.getRequestURI());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
        LOGGER.error("Lỗi không mong muốn tại {}", request.getRequestURI(), ex);
        ApiErrorResponse body = ApiErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_ERROR, "Hệ thống đang gặp sự cố, vui lòng thử lại sau",
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
```

* Dùng lại `ApiErrorResponse`, `ResourceNotFoundException`, `BusinessException` đã viết ở mục IV.B.
* **So với Mục IV, bổ sung `handleTypeMismatch`**:
  * Bắt `MethodArgumentTypeMismatchException` khi client gọi `GET /api/tours/abc` hoặc `GET /api/hoa-don?tongTienToiThieu=xyz`.
  * Không có handler này, lỗi rơi xuống `handleUnexpected` và trả nhầm HTTP 500 cho một lỗi do client.
* **Các handler còn lại giữ nguyên ý nghĩa**: 400 cho Validation và JSON sai, 404 cho không tìm thấy, 409 cho vi phạm nghiệp vụ, 500 cho lỗi không lường trước.

#### Bước 5: Kịch bản kiểm thử bắt buộc

* **Tạo tour hợp lệ**:
  * Request: `POST /api/tours` với `{"tenTour": "Hạ Long 2N1Đ", "noiXuatPhat": "Hà Nội", "noiDen": "Quảng Ninh", "giaTour": 3200000}`.
  * Kỳ vọng: HTTP 201, header `Location: /api/tours/{id}`.
* **Tên trống và giá âm**:
  * Request: `{"tenTour": "  ", "noiXuatPhat": "Hà Nội", "noiDen": "Huế", "giaTour": -100}`.
  * Kỳ vọng: HTTP 400, JSON như sau:

```json
{
  "timestamp": "2026-10-12T22:45:10.512",
  "status": 400,
  "errorCode": "VALIDATION_ERROR",
  "message": "Dữ liệu đầu vào không hợp lệ",
  "path": "/api/tours",
  "fieldErrors": {
    "tenTour": "Tên tour không được để trống",
    "giaTour": "Giá tour phải lớn hơn 0"
  }
}
```

* **Giá bằng 0**: kỳ vọng HTTP 400 với `"giaTour": "Giá tour phải lớn hơn 0"`.
* **Thiếu hẳn field `giaTour`**: kỳ vọng HTTP 400 với `"giaTour": "Giá tour không được để trống"`.
* Gửi `"giaTour": "abc"`: kỳ vọng HTTP 400 với `errorCode` là `MALFORMED_REQUEST`.
* **Tạo trùng tên tour ở kịch bản A**: kỳ vọng HTTP 409 với `errorCode` là `TOUR_DA_TON_TAI`.
* **`GET /api/tours/9999`**: kỳ vọng HTTP 404 với `errorCode` là `RESOURCE_NOT_FOUND`.
* **`GET /api/tours/abc`**: kỳ vọng HTTP 400 với `errorCode` là `INVALID_PARAMETER`.

### E. Tiêu chí nghiệm thu bài tập

* **Ứng dụng khởi động thành công, sinh đủ 4 bảng**: `tour`, `khach_hang`, `hoa_don`, `lich_su_mua_ve`.
* Toàn bộ `@ManyToOne` đã đặt `FetchType.LAZY`. Không có quan hệ nào dùng `EAGER`.
* Không có API nào trả Entity trực tiếp ra JSON. Tất cả đều qua DTO.
* API chi tiết Tour chỉ sinh đúng 1 câu SQL (có ảnh chụp log làm minh chứng).
* API danh sách Tour có giới hạn `size` tối đa và thứ tự sắp xếp ổn định.
* Toàn bộ 8 kịch bản kiểm thử ở Yêu cầu 4 trả đúng mã HTTP và đúng định dạng `ApiErrorResponse`.
* **Code tuân thủ naming convention**: class `PascalCase`, method và biến `camelCase`, hằng số `UPPER_SNAKE_CASE`.
