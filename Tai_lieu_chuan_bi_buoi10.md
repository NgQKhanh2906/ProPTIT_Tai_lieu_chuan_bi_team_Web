# Nguyễn Quốc Khánh
# BUỔI 10: SECURITY BASIC IMPLEMENT

*Tài liệu chuẩn bị buổi 10*

---

**Mục lục**

- [Nguyễn Quốc Khánh](#nguyễn-quốc-khánh)
- [BUỔI 10: SECURITY BASIC IMPLEMENT](#buổi-10-security-basic-implement)
  - [Phần 1: Authentication và Authorization](#phần-1-authentication-và-authorization)
    - [I. Authentication là gì](#i-authentication-là-gì)
      - [1. Bản chất kỹ thuật](#1-bản-chất-kỹ-thuật)
      - [2. Giải quyết bài toán gì?](#2-giải-quyết-bài-toán-gì)
      - [3. Lưu ý \& Best Practices](#3-lưu-ý--best-practices)
      - [4. Demo](#4-demo)
    - [II. Authorization là gì](#ii-authorization-là-gì)
      - [1. Bản chất kỹ thuật](#1-bản-chất-kỹ-thuật-1)
      - [2. Giải quyết bài toán gì?](#2-giải-quyết-bài-toán-gì-1)
      - [3. Lưu ý \& Best Practices](#3-lưu-ý--best-practices-1)
      - [4. Demo](#4-demo-1)
    - [III. So sánh Authentication và Authorization](#iii-so-sánh-authentication-và-authorization)
      - [1. Bản chất kỹ thuật](#1-bản-chất-kỹ-thuật-2)
      - [2. Giải quyết bài toán gì?](#2-giải-quyết-bài-toán-gì-2)
      - [3. Lưu ý \& Best Practices](#3-lưu-ý--best-practices-2)
      - [4. Demo](#4-demo-2)
  - [Phần 2: Spring Security](#phần-2-spring-security)
    - [I. Spring Security là gì và vị trí của nó](#i-spring-security-là-gì-và-vị-trí-của-nó)
      - [1. Bản chất kỹ thuật](#1-bản-chất-kỹ-thuật-3)
      - [2. Giải quyết bài toán gì?](#2-giải-quyết-bài-toán-gì-3)
      - [3. Lưu ý \& Best Practices](#3-lưu-ý--best-practices-3)
      - [4. Demo](#4-demo-3)
    - [II. Security Filter Chain](#ii-security-filter-chain)
      - [1. Bản chất kỹ thuật](#1-bản-chất-kỹ-thuật-4)
      - [2. Giải quyết bài toán gì?](#2-giải-quyết-bài-toán-gì-4)
      - [3. Lưu ý \& Best Practices](#3-lưu-ý--best-practices-4)
      - [4. Demo](#4-demo-4)
    - [III. Các thành phần xác thực](#iii-các-thành-phần-xác-thực)
      - [1. Bản chất kỹ thuật](#1-bản-chất-kỹ-thuật-5)
      - [2. Giải quyết bài toán gì?](#2-giải-quyết-bài-toán-gì-5)
      - [3. Lưu ý \& Best Practices](#3-lưu-ý--best-practices-5)
      - [4. Demo](#4-demo-5)
    - [IV. PasswordEncoder và BCrypt](#iv-passwordencoder-và-bcrypt)
      - [1. Bản chất kỹ thuật](#1-bản-chất-kỹ-thuật-6)
      - [2. Giải quyết bài toán gì?](#2-giải-quyết-bài-toán-gì-6)
      - [3. Lưu ý \& Best Practices](#3-lưu-ý--best-practices-6)
      - [4. Demo](#4-demo-6)
    - [V. Phân quyền trong Spring Security](#v-phân-quyền-trong-spring-security)
      - [1. Bản chất kỹ thuật](#1-bản-chất-kỹ-thuật-7)
      - [2. Giải quyết bài toán gì?](#2-giải-quyết-bài-toán-gì-7)
      - [3. Lưu ý \& Best Practices](#3-lưu-ý--best-practices-7)
      - [4. Demo](#4-demo-7)
  - [Phần 3: JWT (JSON Web Token)](#phần-3-jwt-json-web-token)
    - [I. JWT là gì](#i-jwt-là-gì)
      - [1. Bản chất kỹ thuật](#1-bản-chất-kỹ-thuật-8)
      - [2. Giải quyết bài toán gì?](#2-giải-quyết-bài-toán-gì-8)
      - [3. Lưu ý \& Best Practices](#3-lưu-ý--best-practices-8)
      - [4. Demo](#4-demo-8)
    - [II. Chữ ký và tính toàn vẹn](#ii-chữ-ký-và-tính-toàn-vẹn)
      - [1. Bản chất kỹ thuật](#1-bản-chất-kỹ-thuật-9)
      - [2. Giải quyết bài toán gì?](#2-giải-quyết-bài-toán-gì-9)
      - [3. Lưu ý \& Best Practices](#3-lưu-ý--best-practices-9)
      - [4. Demo](#4-demo-9)
    - [III. Session-based và Token-based (Stateless)](#iii-session-based-và-token-based-stateless)
      - [1. Bản chất kỹ thuật](#1-bản-chất-kỹ-thuật-10)
      - [2. Giải quyết bài toán gì?](#2-giải-quyết-bài-toán-gì-10)
      - [3. Lưu ý \& Best Practices](#3-lưu-ý--best-practices-10)
      - [4. Demo](#4-demo-10)
    - [IV. Luồng JWT trong Spring Security](#iv-luồng-jwt-trong-spring-security)
      - [1. Bản chất kỹ thuật](#1-bản-chất-kỹ-thuật-11)
      - [2. Giải quyết bài toán gì?](#2-giải-quyết-bài-toán-gì-11)
      - [3. Lưu ý \& Best Practices](#3-lưu-ý--best-practices-11)
      - [4. Demo](#4-demo-11)
    - [V. Lưu ý bảo mật khi dùng JWT](#v-lưu-ý-bảo-mật-khi-dùng-jwt)
      - [1. Bản chất kỹ thuật](#1-bản-chất-kỹ-thuật-12)
      - [2. Giải quyết bài toán gì?](#2-giải-quyết-bài-toán-gì-12)
      - [3. Lưu ý \& Best Practices](#3-lưu-ý--best-practices-12)
      - [4. Demo](#4-demo-12)
  - [Phần 4: Bài tập chuẩn bị (Buổi 10)](#phần-4-bài-tập-chuẩn-bị-buổi-10)
    - [I. Yêu cầu 1 - Cập nhật thiết kế CSDL](#i-yêu-cầu-1---cập-nhật-thiết-kế-csdl)
      - [1. Bước 1: Khảo sát yêu cầu bổ sung](#1-bước-1-khảo-sát-yêu-cầu-bổ-sung)
      - [2. Bước 2: Xác định thực thể và thuộc tính](#2-bước-2-xác-định-thực-thể-và-thuộc-tính)
      - [3. Bước 3: Xác định mối quan hệ](#3-bước-3-xác-định-mối-quan-hệ)
      - [4. Bước 4: Vẽ lược đồ E-R](#4-bước-4-vẽ-lược-đồ-e-r)
      - [5. Bước 5: Chuyển lược đồ E-R sang mô hình quan hệ](#5-bước-5-chuyển-lược-đồ-e-r-sang-mô-hình-quan-hệ)
      - [6. Bước 6: Chuẩn hóa dữ liệu (1NF, 2NF, 3NF)](#6-bước-6-chuẩn-hóa-dữ-liệu-1nf-2nf-3nf)
    - [II. Yêu cầu 2 - Cấu hình nền tảng](#ii-yêu-cầu-2---cấu-hình-nền-tảng)
      - [1. Thêm dependency vào `pom.xml`](#1-thêm-dependency-vào-pomxml)
      - [2. Cấu hình `application.properties`](#2-cấu-hình-applicationproperties)
      - [3. Tạo `Role` và `User`](#3-tạo-role-và-user)
      - [4. Tạo `UserRepository`](#4-tạo-userrepository)
      - [5. Tạo `DataInitializer`](#5-tạo-datainitializer)
    - [III. Yêu cầu 3 - JWT và Security](#iii-yêu-cầu-3---jwt-và-security)
      - [1. `JwtService`](#1-jwtservice)
      - [2. `CustomUserDetailsService`](#2-customuserdetailsservice)
      - [3. `JwtAuthenticationFilter`](#3-jwtauthenticationfilter)
      - [4. `RestAuthenticationEntryPoint` và `RestAccessDeniedHandler`](#4-restauthenticationentrypoint-và-restaccessdeniedhandler)
      - [5. `SecurityConfig`](#5-securityconfig)
    - [IV. Yêu cầu 4 - Đăng ký, đăng nhập](#iv-yêu-cầu-4---đăng-ký-đăng-nhập)
      - [1. Các DTO](#1-các-dto)
      - [2. `AuthService`](#2-authservice)
      - [3. `AuthController`](#3-authcontroller)
      - [4. Bổ sung `GlobalExceptionHandler`](#4-bổ-sung-globalexceptionhandler)
    - [V. Yêu cầu 5 - API thông tin của chính mình](#v-yêu-cầu-5---api-thông-tin-của-chính-mình)
      - [1. Vì sao không được nhận `id`](#1-vì-sao-không-được-nhận-id)
      - [2. `UserService`](#2-userservice)
      - [3. `UserController`](#3-usercontroller)
      - [4. Giải thích](#4-giải-thích)
    - [VI. Yêu cầu 6 - API danh sách user cho ADMIN](#vi-yêu-cầu-6---api-danh-sách-user-cho-admin)
      - [1. Thêm vào `UserService`](#1-thêm-vào-userservice)
      - [2. Thêm vào `UserController`](#2-thêm-vào-usercontroller)
      - [3. Hai lớp chặn](#3-hai-lớp-chặn)
      - [4. Bổ sung `GlobalExceptionHandler`](#4-bổ-sung-globalexceptionhandler-1)
      - [5. Tránh N + 1](#5-tránh-n--1)
    - [VII. Yêu cầu 7 - Áp phân quyền cho API cũ](#vii-yêu-cầu-7---áp-phân-quyền-cho-api-cũ)
      - [1. Ma trận phân quyền](#1-ma-trận-phân-quyền)
      - [2. Cập nhật `SecurityConfig`](#2-cập-nhật-securityconfig)
      - [3. Sửa `BookTicketRequest`](#3-sửa-bookticketrequest)
      - [4. Sửa `HoaDonService`](#4-sửa-hoadonservice)
      - [5. Sửa `HoaDonController`](#5-sửa-hoadoncontroller)
      - [6. `GlobalExceptionHandler` hoàn chỉnh](#6-globalexceptionhandler-hoàn-chỉnh)
    - [VIII. Kiểm thử trên Postman](#viii-kiểm-thử-trên-postman)
      - [1. Chuẩn bị Environment và lưu token tự động](#1-chuẩn-bị-environment-và-lưu-token-tự-động)
      - [2. Đăng ký](#2-đăng-ký)
      - [3. Đăng nhập](#3-đăng-nhập)
      - [4. Gọi API có và không có token](#4-gọi-api-có-và-không-có-token)
      - [5. API thông tin cá nhân và danh sách user](#5-api-thông-tin-cá-nhân-và-danh-sách-user)
      - [6. Phân quyền API cũ](#6-phân-quyền-api-cũ)
      - [7. Đặt vé theo user đăng nhập](#7-đặt-vé-theo-user-đăng-nhập)
      - [8. Token bị sửa và token hết hạn](#8-token-bị-sửa-và-token-hết-hạn)
    - [IX. Tiêu chí hoàn thành](#ix-tiêu-chí-hoàn-thành)
    - [X. Hướng mở rộng](#x-hướng-mở-rộng)

---

## Phần 1: Authentication và Authorization

Hai câu hỏi mà mọi hệ thống có đăng nhập đều phải trả lời, theo đúng thứ tự: "Bạn là ai?" rồi mới đến "Bạn được làm gì?". Ví dụ đời thường ngay trong đề tài: khách đến quầy công ty du lịch.

![Authentication và Authorization tại công ty du lịch](buoi10-1-authn-vs-authz.png)

### I. Authentication là gì

#### 1. Bản chất kỹ thuật

- Authentication (xác thực) là quá trình **chứng minh danh tính**: client đưa ra một bằng chứng, server đối chiếu với dữ liệu đã lưu để trả lời câu hỏi "Bạn là ai?".
- Ba cách xác thực phổ biến:
  - **Mật khẩu**: thứ bạn biết. Dùng một lần lúc đăng nhập.
  - **Token**: thứ server cấp cho bạn sau khi đăng nhập thành công. Gửi kèm mọi request sau đó.
  - **OTP**: mã dùng một lần gửi qua điện thoại, email. Thường dùng làm lớp xác thực thứ hai.
- Kết quả thành công: server biết chắc request này là của user nào, gọi là **principal** (ví dụ user `an`).
- Thiếu bằng chứng, bằng chứng sai, hoặc token hết hạn: server trả mã **401 Unauthorized**. Tên chuẩn HTTP là "Unauthorized" nhưng ý nghĩa thực tế là "chưa xác thực".
- Trong dự án tour: đăng nhập bằng `username` + `password` đúng **một lần**, nhận về một JWT; mọi request sau gửi JWT thay cho mật khẩu.

#### 2. Giải quyết bài toán gì?

- Hiện tại API đặt vé nhận `khachHangId` trong Body. Bất kỳ ai cũng gửi được `{"khachHangId": 5, ...}` để đặt vé đứng tên khách hàng số 5.
- Server không biết người đang gọi là ai, nên không thể ghi nhận "ai đã làm gì", cũng không thể giới hạn mỗi người chỉ thao tác trên dữ liệu của chính mình.
- Authentication gắn mỗi request với đúng một người dùng đã được kiểm chứng. Đây là nền móng cho mọi bước phân quyền phía sau.

#### 3. Lưu ý & Best Practices

| Cách xác thực | Bằng chứng              | Ưu điểm                                            | Nhược điểm                                       | Trong dự án tour                  |
| ------------- | ----------------------- | -------------------------------------------------- | ------------------------------------------------ | --------------------------------- |
| Mật khẩu      | `username` + `password` | Quen thuộc, dễ làm                                 | Lộ là mất tài khoản, không nên gửi ở mọi request | Chỉ dùng ở `POST /api/auth/login` |
| Token (JWT)   | Chuỗi do server ký      | Không gửi lại mật khẩu, server không cần lưu phiên | Khó thu hồi trước hạn                            | Gửi ở mọi API còn lại             |
| OTP           | Mã 6 số dùng một lần    | Chống lộ mật khẩu                                  | Cần dịch vụ SMS, email                           |                                   |

- Khi đăng nhập sai, chỉ trả **một** thông điệp chung `"Sai ten dang nhap hoac mat khau"`. Không tách thành "sai username" và "sai password", vì kẻ xấu sẽ dựa vào đó để dò ra username nào có tồn tại.
- Không bao giờ lưu mật khẩu dạng thô trong database.
- Mật khẩu và token đều đi trong request, nên môi trường thật bắt buộc dùng HTTPS.

#### 4. Demo

Đoạn code xác thực `username` + `password` bằng Spring Security:

```java
Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.username(), request.password()));
User user = (User) authentication.getPrincipal();
```

Giải thích:

- `new UsernamePasswordAuthenticationToken(username, password)`: gói bằng chứng người dùng gửi lên, lúc này **chưa** được xác thực.
- `authenticationManager.authenticate(...)`: đối chiếu với bảng `users`. Đúng thì trả về một `Authentication` **đã** xác thực, sai thì ném `BadCredentialsException`.
- `getPrincipal()`: lấy ra chính user vừa đăng nhập, dùng để tạo token.

Input 1: `POST /api/auth/login`, Body `{"username": "an", "password": "123456"}`.

Output 1 mong đợi: `200 OK`, nhận về `accessToken`.

Input 2: cùng URL, Body `{"username": "an", "password": "sai-mat-khau"}`.

Output 2 mong đợi: `401 Unauthorized`:

```json
{
    "message": "Sai ten dang nhap hoac mat khau",
    "status": 401
}
```

### II. Authorization là gì

#### 1. Bản chất kỹ thuật

- Authorization (phân quyền) trả lời câu hỏi "Bạn được làm gì?". Nó chỉ diễn ra **sau** khi đã biết bạn là ai.
- **Authority** là một quyền, biểu diễn bằng một chuỗi, trong Spring Security là interface `GrantedAuthority`. Ví dụ `"ROLE_ADMIN"`, `"tour:write"`.
- **Role** là một Authority đặc biệt có tiền tố `ROLE_`, dùng để gom nhiều quyền theo vai trò. Dự án có 2 role: `USER` (khách hàng) và `ADMIN` (quản lý).
- Server so sánh authority của người dùng với yêu cầu của API. Thiếu quyền thì trả **403 Forbidden**.
- Có hai nơi khai báo quy tắc phân quyền:
  - Theo **URL** trong `SecurityConfig`: `requestMatchers("/api/khach-hang/**").hasRole("ADMIN")`.
  - Theo **phương thức** bằng annotation: `@PreAuthorize("hasRole('ADMIN')")`.

#### 2. Giải quyết bài toán gì?

- Đăng nhập rồi chưa đủ: khách hàng `an` đã xác thực nhưng không được xem doanh thu (`GET /api/tours/revenue`), không được sửa, xóa tour.
- Ngược lại, tài khoản ADMIN không gắn với khách hàng nào nên không được đặt vé.
- Authorization biến các quy định nghiệp vụ "ai được làm gì" thành quy tắc mà server tự kiểm tra ở mọi request.

#### 3. Lưu ý & Best Practices

- **Mặc định từ chối:** quy tắc cuối cùng luôn là `anyRequest().authenticated()`. API mới thêm sau này mà quên khai báo vẫn được bảo vệ.
- **Quyền tối thiểu:** chỉ cấp đúng quyền cần dùng. USER không cần quyền ghi trên `/api/tours`.
- **Kiểm tra ở server:** ẩn nút "Xóa tour" trên giao diện không phải là phân quyền. Kẻ xấu gọi thẳng API bằng Postman vẫn qua nếu server không chặn.
- **Không tin dữ liệu định danh từ client:** "đặt vé cho chính mình" phải lấy khách hàng từ token, không lấy `khachHangId` trong Body.

#### 4. Demo

Một quy tắc phân quyền theo URL trong `SecurityConfig`:

```java
.requestMatchers(HttpMethod.GET, "/api/tours/revenue").hasRole("ADMIN")
```

Giải thích:

- `requestMatchers(HttpMethod.GET, "/api/tours/revenue")`: áp dụng cho đúng request `GET` tới URL này.
- `hasRole("ADMIN")`: chỉ user có authority `ROLE_ADMIN` mới qua.

Input: gọi `GET /api/tours/revenue` lần lượt bằng token của `an` (USER) và của `admin` (ADMIN).

Output mong đợi:

- Token USER: `403 Forbidden`, Body `{"message": "Khong co quyen truy cap", "status": 403}`.
- Token ADMIN: `200 OK`, danh sách doanh thu từng tour như Buổi 9.

### III. So sánh Authentication và Authorization

#### 1. Bản chất kỹ thuật

| Tiêu chí                   | Authentication                                     | Authorization                          |
| -------------------------- | -------------------------------------------------- | -------------------------------------- |
| Câu hỏi trả lời            | Bạn là ai?                                         | Bạn được làm gì?                       |
| Thời điểm                  | Trước                                              | Sau, chỉ khi đã xác thực               |
| Dữ liệu dùng               | `username`, `password`, token                      | Role, Authority của user               |
| Thành phần Spring Security | `AuthenticationManager`, `JwtAuthenticationFilter` | `AuthorizationFilter`, `@PreAuthorize` |
| Lỗi trả về                 | `401 Unauthorized`                                 | `403 Forbidden`                        |
| Ví dụ dự án tour           | Gọi `GET /api/tours` không kèm token               | USER gọi `GET /api/users`              |

Ví dụ dễ nhớ: Authentication là xuất trình CCCD ở quầy, Authorization là soát thẻ đoàn ở từng cửa.

#### 2. Giải quyết bài toán gì?

- Phân biệt đúng giúp trả đúng mã lỗi, và client xử lý đúng:
  - Nhận `401`: token thiếu hoặc hết hạn, ứng dụng chuyển người dùng sang màn hình đăng nhập.
  - Nhận `403`: đăng nhập lại cũng vô ích, chỉ cần báo "Bạn không có quyền".

#### 3. Lưu ý & Best Practices

- Nhầm lẫn hay gặp nhất: trả `403` cho người chưa đăng nhập. Spring Security sẽ làm đúng điều này nếu cấu hình JWT mà **quên** khai báo `authenticationEntryPoint`, vì khi không có form login hay HTTP Basic, nó dùng `Http403ForbiddenEntryPoint` mặc định.
- Thứ tự kiểm tra luôn là xác thực trước, phân quyền sau. Request không có token tới một API chỉ dành cho ADMIN nhận `401` chứ không phải `403`.

#### 4. Demo

Input: cùng gọi `GET /api/users` (chỉ ADMIN) theo ba cách.

Output mong đợi:

| Request                          | Mã trả về | Lý do                                              |
| -------------------------------- | --------- | -------------------------------------------------- |
| Không gửi header `Authorization` | `401`     | Chưa biết bạn là ai                                |
| Gửi token của `an` (USER)        | `403`     | Biết bạn là `an`, nhưng `an` không có `ROLE_ADMIN` |
| Gửi token của `admin`            | `200`     | Đã xác thực và đủ quyền                            |

**Ghi nhớ nhanh:**

- Authentication hỏi "Bạn là ai?", sai hoặc thiếu thì 401. Authorization hỏi "Bạn được làm gì?", thiếu quyền thì 403.
- Xác thực luôn đứng trước phân quyền: chưa xác thực thì chưa bàn tới quyền.
- Role trong Spring Security là Authority có tiền tố `ROLE_`.
- Ẩn nút trên giao diện không phải phân quyền; server phải tự chặn.

---

## Phần 2: Spring Security

### I. Spring Security là gì và vị trí của nó

![Vị trí của Spring Security trong một request](buoi10-2-vi-tri-spring-security.png)

*Spring Security là một lớp Filter đứng trước DispatcherServlet: request bị chặn ở đây thì Controller không bao giờ được gọi.*

#### 1. Bản chất kỹ thuật

- **Spring Security** là framework bảo mật chuẩn của hệ sinh thái Spring, lo ba việc chính: xác thực, phân quyền và chống các kiểu tấn công phổ biến (CSRF, giả mạo header...).
- Về kỹ thuật, nó là một chuỗi **Servlet Filter**. Filter chạy **trước** `DispatcherServlet`, tức là trước mọi Controller của dự án.
- Mỗi request đi qua các filter theo thứ tự. Filter nào phát hiện request không hợp lệ thì trả lỗi ngay, request không đi tiếp tới Spring MVC.
- Ngay khi thêm dependency `spring-boot-starter-security`, Spring Boot tự cấu hình:
  - **Mọi** API đều bị khóa, kể cả `GET /api/tours`.
  - Một trang đăng nhập mặc định tại `/login` (khi mở bằng trình duyệt) và hỗ trợ HTTP Basic.
  - Một tài khoản mặc định tên `user`, mật khẩu sinh ngẫu nhiên và in ra Console mỗi lần khởi động.

#### 2. Giải quyết bài toán gì?

- Không có Spring Security, mỗi phương thức Controller phải tự viết `if` kiểm tra token, kiểm tra quyền. Hàng chục API là hàng chục chỗ lặp lại, quên một chỗ là lộ một lỗ hổng.
- Spring Security gom toàn bộ việc kiểm tra về **một** nơi (`SecurityConfig`), chạy trước Controller nên Controller chỉ còn lo nghiệp vụ.
- Kèm sẵn các thành phần đã được kiểm chứng: mã hóa mật khẩu BCrypt, quản lý `SecurityContext`, chuẩn hóa lỗi 401/403.

#### 3. Lưu ý & Best Practices

- Mật khẩu sinh ngẫu nhiên in ra Console chỉ dùng thử. Khi dự án khai báo bean `UserDetailsService` của riêng mình, tài khoản `user` mặc định tự biến mất.
- Khi tự khai báo bean `SecurityFilterChain`, toàn bộ cấu hình mặc định (form login, HTTP Basic) không còn. Dự án chỉ có đúng những gì được khai báo.
- Từ Spring Security 6, class `WebSecurityConfigurerAdapter` **đã bị xóa**. Mọi hướng dẫn trên mạng còn dùng `extends WebSecurityConfigurerAdapter` là đã lỗi thời, chép vào sẽ không biên dịch được.

#### 4. Demo

Thêm vào `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

Giải thích:

- Không ghi `version`: phiên bản do `spring-boot-starter-parent` quản lý, luôn khớp với Spring Boot đang dùng.

Input: khởi động lại ứng dụng, chưa viết thêm dòng code nào.

Output mong đợi trong Console (chuỗi mật khẩu thay đổi mỗi lần chạy):

```text
Using generated security password: 3f1c8a52-7d4e-4c61-9b0a-2e5f8d7c1a90

This generated password is for development use only. Your security configuration must be updated before running your application in production.
```

- Postman gọi `GET http://localhost:8080/api/tours` : nhận `401 Unauthorized`.
- Vào tab **Authorization**, chọn **Basic Auth**, nhập `user` và mật khẩu ở Console, gọi lại: nhận `200 OK` như cũ.

### II. Security Filter Chain

![Security Filter Chain](buoi10-3-security-filter-chain.png)

*Tomcat chỉ biết một Filter là DelegatingFilterProxy; phía sau nó là cả một chuỗi filter do Spring quản lý, chạy đúng thứ tự.*

#### 1. Bản chất kỹ thuật

- **`DelegatingFilterProxy`**: Tomcat chỉ làm việc với Servlet Filter, không biết bean Spring là gì. `DelegatingFilterProxy` là một Servlet Filter đóng vai cầu nối: nhận request từ Tomcat rồi chuyển cho một bean Spring xử lý.
- **`FilterChainProxy`**: chính là bean tên `springSecurityFilterChain` mà `DelegatingFilterProxy` chuyển việc tới. Nó giữ danh sách các `SecurityFilterChain` và chọn chain **đầu tiên** khớp với URL của request.
- **`SecurityFilterChain`**: một danh sách filter có thứ tự cố định. Dự án khai báo đúng một chain bằng `@Bean SecurityFilterChain` trong `SecurityConfig`.
- Các filter đáng nhớ, theo đúng thứ tự chạy:
  - `SecurityContextHolderFilter`: chuẩn bị `SecurityContext` cho request.
  - `UsernamePasswordAuthenticationFilter`: xử lý form đăng nhập. Dự án không bật form login nên filter này không có trong chain, nhưng vị trí của nó vẫn được dùng làm **mốc**.
  - `AnonymousAuthenticationFilter`: nếu tới đây vẫn chưa có ai đăng nhập, gán một user "ẩn danh".
  - `ExceptionTranslationFilter`: bắt lỗi bảo mật của các filter phía sau, đổi thành 401 hoặc 403.
  - `AuthorizationFilter`: filter gần cuối, so request với các quy tắc `requestMatchers` để quyết định cho qua hay chặn.
- `addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)` chèn filter JWT của dự án ngay trước mốc trên, tức là trước `AnonymousAuthenticationFilter` và `AuthorizationFilter`. Nhờ vậy khi tới bước phân quyền, user đã được nạp từ token.

#### 2. Giải quyết bài toán gì?

- Chia việc bảo mật thành nhiều filter nhỏ, mỗi filter một việc. Muốn thêm cách xác thực mới (JWT) chỉ cần chèn thêm một filter, không sửa filter có sẵn.
- `DelegatingFilterProxy` cho phép filter bảo mật là bean Spring bình thường: được inject `JwtService`, `UserRepository` như mọi Service khác.

#### 3. Lưu ý & Best Practices

- **Exception ném ra trong Filter không đi qua `@RestControllerAdvice`.** Filter chạy trước `DispatcherServlet`, còn `@RestControllerAdvice` chỉ bọc quanh Controller. Vì vậy:
  - Lỗi bảo mật phải được trả về bằng `AuthenticationEntryPoint` (401) và `AccessDeniedHandler` (403) riêng
  - Trong `JwtAuthenticationFilter`, mọi lỗi giải mã token đều phải được `try-catch` ngay trong filter.
- Filter JWT nên kế thừa `OncePerRequestFilter`: đảm bảo chỉ chạy **một lần** cho mỗi request, kể cả khi request bị chuyển tiếp nội bộ.
- Vì `JwtAuthenticationFilter` có `@Component`, Spring Boot còn tự đăng ký nó vào chuỗi filter của Tomcat. `OncePerRequestFilter` giúp lần chạy thứ hai bị bỏ qua, nên không có lỗi.
- Muốn xem chain thật của dự án, bật log `DEBUG` cho package `org.springframework.security`, xem Demo.

#### 4. Demo

Thêm tạm vào `application.properties` (xóa sau khi xem xong vì log rất dài):

```properties
logging.level.org.springframework.security=DEBUG
```

Giải thích:

- Ở mức `DEBUG`, Spring Security in danh sách filter của từng `SecurityFilterChain` khi ứng dụng khởi động.

Input: khởi động ứng dụng sau khi làm xong Phần 4.III.

Output mong đợi (tên rút gọn, có thể khác đôi chút giữa các bản vá):

```text
DEBUG o.s.s.web.DefaultSecurityFilterChain : Will secure any request with filters: DisableEncodeUrlFilter, WebAsyncManagerIntegrationFilter, SecurityContextHolderFilter, HeaderWriterFilter, LogoutFilter, JwtAuthenticationFilter, RequestCacheAwareFilter, SecurityContextHolderAwareRequestFilter, AnonymousAuthenticationFilter, SessionManagementFilter, ExceptionTranslationFilter, AuthorizationFilter
```

- `JwtAuthenticationFilter` đứng trước `AnonymousAuthenticationFilter`, đúng vị trí đã chèn.
- Không có `CsrfFilter` (đã tắt CSRF), không có `UsernamePasswordAuthenticationFilter` (không bật form login).

### III. Các thành phần xác thực

![Các thành phần xác thực](buoi10-4-thanh-phan-xac-thuc.png)

*AuthenticationManager điều phối, UserDetailsService tìm user, PasswordEncoder so mật khẩu; kết quả là một Authentication nằm trong SecurityContextHolder.*

#### 1. Bản chất kỹ thuật

- `SecurityContextHolder` giữ thông tin đăng nhập của request **hiện tại**. Mặc định nó lưu trong `ThreadLocal`: mỗi luồng xử lý request có một bản riêng, không lẫn sang request khác.
- Bên trong là **`SecurityContext`**, bên trong nữa là **`Authentication`**, gồm 3 phần chính:
  - `principal`: người dùng. Trong dự án là object `User`.
  - `credentials`: bằng chứng (mật khẩu). Bị xóa thành `null` ngay sau khi xác thực xong.
  - `authorities`: danh sách quyền, ví dụ `[ROLE_USER]`.
- **`AuthenticationManager`** là interface điều phối việc xác thực. Bản cài đặt mặc định `ProviderManager` hỏi lần lượt các `AuthenticationProvider`.
- **`DaoAuthenticationProvider`** xác thực bằng dữ liệu trong database, nhờ hai trợ thủ:
  - **`UserDetailsService`**: `loadUserByUsername(username)` tìm user. Dự án viết `CustomUserDetailsService` đọc bảng `users`.
  - **`PasswordEncoder`**: `matches(raw, hash)` so mật khẩu vừa nhập với chuỗi hash đã lưu.
- **E.** **`UserDetails`** là interface mô tả "một user theo cách Spring Security hiểu": có `getUsername()`, `getPassword()`, `getAuthorities()`.

#### 2. Giải quyết bài toán gì?

- Tách ba việc khác nhau ra ba thành phần: "tìm user ở đâu", "so mật khẩu thế nào", "ai điều phối". Đổi từ MySQL sang LDAP chỉ cần thay `UserDetailsService`, đổi thuật toán mã hóa chỉ cần thay `PasswordEncoder`.
- `SecurityContextHolder` cho phép mọi tầng (Controller, Service) lấy được user hiện tại mà không cần truyền tham số qua từng phương thức.

#### 3. Lưu ý & Best Practices

- `ThreadLocal` nghĩa là code chạy ở luồng khác (`@Async`, `CompletableFuture`) **không** thấy user đang đăng nhập.
- Khi username không tồn tại, `DaoAuthenticationProvider` vẫn ném `BadCredentialsException` giống hệt trường hợp sai mật khẩu, để kẻ xấu không dò được username nào có thật.
- Lựa chọn thiết kế: Entity `User` tự implements `UserDetails`, hay tách class `CustomUserDetails` riêng?

| Tiêu chí                          | `User implements UserDetails`                           | Tách `CustomUserDetails`                   |
| --------------------------------- | ------------------------------------------------------- | ------------------------------------------ |
| Số class                          | Ít hơn                                                  | Thêm 1 class bọc                           |
| Principal trong `SecurityContext` | Chính Entity `User` (đã rời Persistence Context)        | Object nhẹ, chỉ có dữ liệu cần cho bảo mật |
| Rủi ro                            | Dễ vô tình đọc quan hệ Lazy, dễ trả nhầm Entity ra JSON | Thấp hơn                                   |
| Tách biệt trách nhiệm             | Entity phụ thuộc Spring Security                        | Entity độc lập                             |

#### 4. Demo

Hai cách lấy user đang đăng nhập trong Controller:

```java
@GetMapping("/me")
public UserResponse getCurrentUser(@AuthenticationPrincipal User currentUser) {
    return userService.getCurrentUser(currentUser.getUsername());
}
```

```java
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
User currentUser = (User) authentication.getPrincipal();
```

Giải thích:

- Cách 1, `@AuthenticationPrincipal`: Spring tự lấy `principal` trong `SecurityContextHolder` và truyền vào tham số. Gọn, dễ test, nên dùng trong Controller.
- Cách 2, gọi thẳng `SecurityContextHolder`: dùng được ở bất kỳ đâu, kể cả Service. Phải tự ép kiểu `principal` về `User`.

Input: gọi `GET /api/users/me` kèm token của user `an`.

Output mong đợi: `currentUser.getUsername()` là `"an"`, `currentUser.getAuthorities()` là `[ROLE_USER]`.

### IV. PasswordEncoder và BCrypt

![BCrypt với salt](buoi10-5-bcrypt.png)

*Cùng mật khẩu 123456 nhưng mỗi lần encode có salt khác nhau nên ra hash khác nhau; matches() vẫn kiểm tra đúng vì salt nằm sẵn trong hash.*

#### 1. Bản chất kỹ thuật

- **Hash** là hàm **một chiều**: từ mật khẩu tính ra chuỗi hash rất dễ, từ hash tính ngược ra mật khẩu là không khả thi. Khác với mã hóa (encryption) có thể giải mã khi có khóa.
- **Salt** là chuỗi ngẫu nhiên trộn thêm vào mật khẩu trước khi băm. Mỗi lần `encode` sinh salt mới, nên cùng `123456` băm hai lần ra hai hash khác nhau.
- **BCrypt** là thuật toán băm được thiết kế **chậm có chủ đích**, độ chậm điều chỉnh bằng tham số **cost** (mặc định 10, tức 2^10 vòng lặp).
- Chuỗi BCrypt dài 60 ký tự, tự chứa mọi thứ cần để kiểm tra: `$2a$` (phiên bản), `10$` (cost), 22 ký tự salt, 31 ký tự kết quả băm.
- `matches(raw, hash)` không giải mã: nó đọc cost và salt ngay trong `hash`, băm lại `raw` bằng đúng cost và salt đó rồi so sánh với `hash`.

#### 2. Giải quyết bài toán gì?

- Lưu mật khẩu thô: chỉ cần lộ bảng `users` (backup rò rỉ, SQL Injection, nhân viên tò mò) là lộ mật khẩu của mọi khách hàng. Nhiều người dùng chung mật khẩu cho email, ngân hàng.
- Băm không có salt: hai người cùng mật khẩu có cùng hash, kẻ xấu tra bảng hash dựng sẵn là ra ngay.
- Băm nhanh (MD5, SHA-256): máy tính thử được hàng tỷ mật khẩu mỗi giây. BCrypt chậm khiến việc dò thử tốn thời gian hơn rất nhiều lần.

#### 3. Lưu ý & Best Practices

| Tiêu chí                     | Lưu thô   | MD5 / SHA-256       | BCrypt                  |
| ---------------------------- | --------- | ------------------- | ----------------------- |
| Lộ database thì lộ mật khẩu? | Lộ ngay   | Dò được nhanh       | Rất tốn thời gian để dò |
| Cùng mật khẩu ra cùng chuỗi? | Có        | Có (nếu không salt) | Không, nhờ salt         |
| Tốc độ băm                   | Không băm | Rất nhanh           | Chậm, chỉnh bằng cost   |
| Dùng cho mật khẩu?           | Không     | Không               | Có                      |

- Cột `password` phải chứa được ít nhất 60 ký tự.
- Không so sánh mật khẩu bằng `equals`. Luôn dùng `passwordEncoder.matches(raw, hash)`.
- Không `encode` hai lần: `encode(encode("123456"))` tạo ra hash của một chuỗi hash, người dùng sẽ không bao giờ đăng nhập được.
- BCrypt chỉ xử lý tối đa **72 byte** đầu của mật khẩu.
- Ở Spring Security 6.5, `encode()` ném `IllegalArgumentException` nếu mật khẩu dài hơn 72 byte. Vì vậy dự án giới hạn `@Size(max = 72)` và kiểm tra thêm số byte trong `AuthService`: `@Size` đếm ký tự, còn ký tự tiếng Việt có dấu chiếm 2 đến 3 byte trong UTF-8.

#### 4. Demo

```java
package com.example.quanlytour;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BCryptDemo {

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hash1 = passwordEncoder.encode("123456");
        String hash2 = passwordEncoder.encode("123456");
        System.out.println(hash1);
        System.out.println(hash2);
        System.out.println(hash1.equals(hash2));
        System.out.println(passwordEncoder.matches("123456", hash1));
        System.out.println(passwordEncoder.matches("123456", hash2));
        System.out.println(passwordEncoder.matches("1234567", hash1));
    }

}
```

Giải thích:

- `new BCryptPasswordEncoder()`: cost mặc định 10, phiên bản `$2a$`.
- Hai lần `encode` cùng một mật khẩu, so sánh hai chuỗi bằng `equals`, rồi kiểm tra bằng `matches`.

Output mong đợi (hai dòng hash là hash BCrypt thật của `123456`, sinh thử bằng thư viện bcrypt cost 10; khi tự chạy sẽ ra chuỗi khác vì salt ngẫu nhiên):

```text
$2a$10$SJU5Hq2fvn9ZmtK5FUSYqusP68ayQodYUYVrUt/nOuLau8kykXIhq
$2a$10$brQMQHG77/Tw9KjahXbhg.Q2CE8wN.AYKNWNjUKEvYjuH0J7X5AwG
false
true
true
false
```

- Dòng 3 `false`: hai hash khác nhau. Dòng 4, 5 `true`: cả hai đều khớp `123456`. Dòng 6 `false`: sai mật khẩu.

### V. Phân quyền trong Spring Security

#### 1. Bản chất kỹ thuật

- **A.** **Phân quyền theo URL** khai báo trong `authorizeHttpRequests(...)` của `SecurityConfig`. Mỗi dòng `requestMatchers(...)` ghép một mẫu URL (có thể kèm HTTP method) với một yêu cầu:
  - `permitAll()`: ai cũng gọi được, kể cả chưa đăng nhập.
  - `authenticated()`: chỉ cần đã đăng nhập.
  - `hasRole("ADMIN")`: phải có authority `ROLE_ADMIN`.
- **B.** `AuthorizationFilter` duyệt các quy tắc **từ trên xuống**, gặp quy tắc **đầu tiên** khớp thì áp dụng và dừng. Các quy tắc phía dưới không còn được xét.
- **C.** **Phân quyền theo phương thức**: bật `@EnableMethodSecurity` trên class cấu hình, rồi đặt `@PreAuthorize("hasRole('ADMIN')")` lên phương thức. Spring bọc Bean bằng proxy và kiểm tra quyền **trước khi** phương thức chạy. Không đủ quyền thì ném `AccessDeniedException`.
- **D.** `hasRole("ADMIN")` tự thêm tiền tố `ROLE_`, nên authority của user phải là `"ROLE_ADMIN"`. Đó là lý do `User.getAuthorities()` trả về `"ROLE_" + role.name()`.

#### 2. Giải quyết bài toán gì?

- Phân quyền theo URL: nhìn một chỗ là thấy toàn bộ "bản đồ quyền" của hệ thống, khớp với ma trận phân quyền.
- Phân quyền theo phương thức: quy tắc nằm ngay cạnh code nghiệp vụ, vẫn có hiệu lực dù URL thay đổi, và kiểm tra được các điều kiện mà URL không diễn tả được.

#### 3. Lưu ý & Best Practices

- **Thứ tự `requestMatchers`: cụ thể đặt trước, tổng quát đặt sau.** Nếu đặt `GET /api/tours/**` (đã đăng nhập) **trước** `GET /api/tours/revenue` (ADMIN), thì `/api/tours/revenue` khớp quy tắc đầu tiên, USER cũng xem được doanh thu.
- `anyRequest()` luôn là dòng cuối. Đặt quy tắc nào sau `anyRequest()`, ứng dụng báo lỗi ngay khi khởi động.

| Cách viết                    | Authority cần có | Ghi chú                                                   |
| ---------------------------- | ---------------- | --------------------------------------------------------- |
| `hasRole("ADMIN")`           | `ROLE_ADMIN`     | Tự thêm `ROLE_`, dùng cho Role                            |
| `hasAuthority("ROLE_ADMIN")` | `ROLE_ADMIN`     | Viết đầy đủ, tương đương dòng trên                        |
| `hasAuthority("ADMIN")`      | `ADMIN`          | Không khớp với user có `ROLE_ADMIN`, dù nhìn giống        |
| `hasRole("ROLE_ADMIN")`      | Không hợp lệ     | Ứng dụng báo lỗi khi khởi động vì `ROLE_` đã được tự thêm |

- **`AccessDeniedException` do `@PreAuthorize` ném ra nằm trong Controller**, nên bị `@RestControllerAdvice` bắt **trước** `AccessDeniedHandler`. `GlobalExceptionHandler` của Buổi 8 có `@ExceptionHandler(Exception.class)`, nên nếu không thêm handler riêng cho `AccessDeniedException`, client nhận `500` thay vì `403`.
- Luôn `permitAll()` cho `/error`. Khi có lỗi chưa được xử lý, Tomcat chuyển tiếp nội bộ tới `/error`; nếu `/error` bị khóa, client nhận `401` thay vì lỗi thật.

#### 4. Demo

Hai cách chặn `GET /api/users` cho riêng ADMIN:

```java
.requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
```

```java
@GetMapping
@PreAuthorize("hasRole('ADMIN')")
public List<UserResponse> getAllUsers() {
    return userService.getAllUsers();
}
```

Giải thích:

- Dòng thứ nhất nằm trong `SecurityConfig`, chặn ngay ở `AuthorizationFilter`, lỗi trả về qua `RestAccessDeniedHandler`.
- `@PreAuthorize` nằm trên phương thức của `UserController`, chặn khi phương thức sắp chạy, lỗi đi qua `GlobalExceptionHandler`.

Input: gọi `GET /api/users` bằng token USER, lần lượt trong ba cấu hình.

Output mong đợi:

| Cấu hình                                                        | Kết quả                            | Đi qua đâu                |
| --------------------------------------------------------------- | ---------------------------------- | ------------------------- |
| Có dòng `requestMatchers`                                       | `403`, `"Khong co quyen truy cap"` | `RestAccessDeniedHandler` |
| Chỉ có `@PreAuthorize`, đã thêm handler `AccessDeniedException` | `403`, `"Khong co quyen truy cap"` | `GlobalExceptionHandler`  |
| Chỉ có `@PreAuthorize`, chưa thêm handler                       | `500`                              | `handleGeneralException`  |

**Ghi nhớ nhanh:**

- Spring Security là chuỗi Filter đứng trước Controller: chặn ở cửa, Controller không biết gì.
- Lỗi trong Filter không tới được `@RestControllerAdvice`; lỗi của `@PreAuthorize` thì tới được.
- `requestMatchers` giống xếp hàng qua cổng: quy tắc đầu tiên khớp quyết định tất cả.
- BCrypt: cùng mật khẩu, mỗi lần một salt nên mỗi lần một hash; salt nằm sẵn trong hash nên `matches()` vẫn kiểm tra được.
- `hasRole("ADMIN")` tìm `ROLE_ADMIN`, không phải `ADMIN`.

---

## Phần 3: JWT (JSON Web Token)

### I. JWT là gì

![Cấu trúc JWT](buoi10-6-cau-truc-jwt.png)

*Một JWT gồm 3 phần nối bằng dấu chấm; header và payload chỉ là JSON được mã hóa Base64URL, ai cũng đọc được.*

#### 1. Bản chất kỹ thuật

- JWT (chuẩn RFC 7519) là một chuỗi văn bản gọn, mang thông tin về người dùng và được server **ký** để chống sửa đổi.
- Cấu trúc gồm 3 phần nối bằng dấu chấm: `header.payload.signature`.
  - **Header**: thuật toán ký, ví dụ `{"alg": "HS256"}`.
  - **Payload**: các **claim**, tức là các cặp khóa và giá trị mô tả người dùng.
  - **Signature**: chữ ký tính từ header, payload và một khóa bí mật (secret) chỉ server biết.
- Header và payload được mã hóa **Base64URL**: giống Base64 nhưng thay `+` bằng `-`, `/` bằng `_` và bỏ dấu `=` cuối, để token đặt được vào URL, header HTTP mà không bị lỗi ký tự.
- Claim chuẩn hay dùng:
  - `sub` (subject): định danh người dùng. Dự án đặt là `username`.
  - `iat` (issued at): thời điểm phát hành, tính bằng giây kể từ 01/01/1970 UTC.
  - `exp` (expiration): thời điểm hết hạn, cùng đơn vị với `iat`.
- Claim tự thêm: dự án thêm `role` để client biết vai trò người dùng mà không cần gọi thêm API.

#### 2. Giải quyết bài toán gì?

- Sau khi đăng nhập, client cần một "giấy thông hành" để chứng minh danh tính ở các request sau, thay vì gửi lại mật khẩu mỗi lần.
- JWT tự chứa đủ thông tin (ai, vai trò gì, hết hạn lúc nào) và tự kiểm tra được tính toàn vẹn bằng chữ ký. Server không phải lưu danh sách phiên đăng nhập.

#### 3. Lưu ý & Best Practices

- Base64URL là **mã hóa để truyền tải**, không phải mã hóa bảo mật. Ai có token cũng giải mã đọc được payload.
- Chỉ đặt vào payload những thông tin không nhạy cảm: `username`, `role`. Không đặt mật khẩu, số CCCD, số điện thoại.
- Token càng nhiều claim càng dài, mà token được gửi kèm **mọi** request. Giữ payload gọn.
- `iat`, `exp` tính theo **giây** (RFC 7519), trong khi `Date.getTime()` của Java tính theo **mili giây**. Thư viện jjwt tự chuyển đổi khi dùng `issuedAt(Date)`, `expiration(Date)`.

#### 4. Demo

Một token thật do `JwtService` sinh ra cho user `an`, sau khi đăng nhập:

```text
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbiIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzkyOTgzNjAwLCJleHAiOjE3OTI5ODcyMDB9.ZYtXbgC8io75K3Lo3t9IduMtQ7u08k9Bs11SabQI1As
```

Giải thích:

- Đoạn trước dấu chấm thứ nhất là header, đoạn giữa là payload, đoạn cuối là chữ ký.
- `eyJ` ở đầu header và payload là dấu hiệu nhận biết: đó là Base64URL của `{"`.

Input: mở trang `https://jwt.io`, dán token trên vào ô **Encoded**.

Output: phần **Decoded** hiển thị ngay, không cần secret:

```json
{
    "alg": "HS256"
}
```

```json
{
    "sub": "an",
    "role": "USER",
    "iat": 1792983600,
    "exp": 1792987200
}
```

- `iat = 1792983600` là 10:00 ngày 26/10/2026 giờ Việt Nam, `exp` muộn hơn đúng 3600 giây (1 giờ).
- Token trên được sinh thử theo đúng định dạng jjwt tạo ra, với secret mẫu ở Phần 4.II. Token do máy bạn sinh sẽ khác vì `iat`, `exp` và secret khác.

### II. Chữ ký và tính toàn vẹn

![Chữ ký JWT chống sửa đổi](buoi10-7-chu-ky-jwt.png)

*Kẻ gian sửa "USER" thành "ADMIN" thì chữ ký server tính lại không còn khớp, token bị từ chối với 401.*

#### 1. Bản chất kỹ thuật

-   Với thuật toán HS256, chữ ký được tính bằng HMAC-SHA256:

```text
signature = HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), secret)
```

- Khi nhận token, server **tính lại** chữ ký từ header, payload trong token và secret của mình, rồi so với chữ ký đi kèm. Khớp thì token nguyên vẹn, không khớp thì từ chối.
- Chỉ cần sửa **một ký tự** trong payload, chữ ký tính lại sẽ khác hoàn toàn. Không có secret thì kẻ gian không thể tính ra chữ ký đúng cho payload mới.
- HS256 là thuật toán **đối xứng**: cùng một secret dùng để ký và để kiểm tra. Ai có secret là tạo được token hợp lệ, nên secret phải được giữ kín tuyệt đối.

#### 2. Giải quyết bài toán gì?

- Payload công khai cho ai cũng đọc, nhưng chữ ký đảm bảo **không ai sửa được** mà không bị phát hiện.
- Nhờ vậy server tin được `sub` và `role` trong token mà không phải tra database để kiểm tra lại mỗi lần.

#### 3. Lưu ý & Best Practices

- **Kết luận quan trọng:** chữ ký chống **sửa**, không chống **đọc**. Không đưa password hay dữ liệu nhạy cảm vào payload.
- Secret phải đủ dài: HS256 yêu cầu tối thiểu 256 bit (32 byte). `Keys.hmacShaKeyFor(...)` của jjwt ném `WeakKeyException` nếu secret ngắn hơn, nên lỗi lộ ra ngay khi khởi động.
- Luôn kiểm tra chữ ký bằng `verifyWith(secretKey)`. Chỉ giải mã Base64 rồi đọc payload mà không kiểm tra chữ ký là lỗ hổng nghiêm trọng.

#### 4. Demo

Đoạn kiểm tra chữ ký trong `JwtService`:

```java
Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
```

Giải thích:

- `verifyWith(secretKey)`: dùng secret của server để tính lại và so chữ ký.
- `parseSignedClaims(token)`: chữ ký sai thì ném `SignatureException`, token hết hạn thì ném `ExpiredJwtException`, token sai định dạng thì ném `MalformedJwtException`. Cả ba đều là lớp con của `JwtException`.

Input: lấy token của `an` ở Mục I, sửa payload thành `"role": "ADMIN"` (đoạn giữa thành `eyJzdWIiOiJhbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc5Mjk4MzYwMCwiZXhwIjoxNzkyOTg3MjAwfQ`), giữ nguyên chữ ký, gọi `GET /api/users`.

Output mong đợi: `401 Unauthorized`:

```json
{
    "message": "Token khong hop le",
    "status": 401
}
```

- Chữ ký đúng cho payload mới phải là `RQJ_iRbOyVj-h14JYFT5GqaFS2KWaTuYepzZwYtR9YY`, khác hẳn chữ ký `ZYtXbgC8io75K3Lo3t9IduMtQ7u08k9Bs11SabQI1As` đang có trong token.

### III. Session-based và Token-based (Stateless)

![Session và JWT](buoi10-8-session-vs-jwt.png)

*Session: server nhớ ai đã đăng nhập. JWT: server không nhớ gì, client mang theo token ở mọi request.*

#### 1. Bản chất kỹ thuật

-   **Session-based (stateful):** đăng nhập xong, server tạo một phiên, lưu trong bộ nhớ, rồi gửi về cookie `JSESSIONID`. Trình duyệt tự gửi lại cookie ở mọi request, server tra phiên để biết là ai.
- **B.** **Token-based (stateless):** đăng nhập xong, server ký một JWT và gửi về trong Body, **không lưu gì**. Client tự đính kèm header `Authorization: Bearer <token>`, server kiểm tra chữ ký là biết là ai.
- **C.** Trong Spring Security, cấu hình `SessionCreationPolicy.STATELESS` báo rằng không tạo và không dùng `HttpSession` để lưu `SecurityContext`. Mỗi request phải tự mang token.

#### 2. Giải quyết bài toán gì?

- Ứng dụng Mobile, SPA (React, Vue) và các hệ thống chạy nhiều server: phiên lưu trên server A thì server B không biết. JWT kiểm tra được ở bất kỳ server nào có secret.
- Server không phải giữ hàng nghìn phiên trong bộ nhớ.

#### 3. Lưu ý & Best Practices

| Tiêu chí             | Session-based                    | Token-based (JWT)               |
| -------------------- | -------------------------------- | ------------------------------- |
| Nơi lưu trạng thái   | Server (session store)           | Client (giữ token)              |
| Mở rộng nhiều server | Cần chia sẻ session (Redis...)   | Dễ, chỉ cần chung secret        |
| Thu hồi trước hạn    | Dễ: xóa session                  | Khó: token còn hạn vẫn hợp lệ   |
| CSRF                 | Có nguy cơ, vì cookie tự gửi kèm | Không, nếu token gửi qua header |
| Phù hợp              | Web render phía server           | REST API cho Mobile, SPA        |

- **Vì sao tắt CSRF là an toàn ở dự án này:** tấn công CSRF lợi dụng việc trình duyệt **tự động** gửi cookie khi người dùng bấm vào một trang độc hại. Token JWT nằm trong header `Authorization`, trình duyệt không tự gửi header này, trang độc hại cũng không đọc được token. Không có thứ gì "tự gửi" để lợi dụng.
- Điều kiện để lập luận trên đúng: token **không** được lưu và gửi bằng cookie. Riêng `STATELESS` chưa đủ để an toàn trước CSRF; lý do quyết định là token đi qua header. Nếu sau này chuyển sang lưu JWT trong cookie, phải bật lại CSRF.

#### 4. Demo

Hai dòng cấu hình stateless trong `SecurityConfig` (Phần 4.III):

```java
.csrf(csrf -> csrf.disable())
.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
```

Giải thích:

- `csrf.disable()`: bỏ `CsrfFilter`. Nếu không tắt, mọi request `POST`, `PUT`, `DELETE` từ Postman bị chặn `403` vì thiếu CSRF token.
- `SessionCreationPolicy.STATELESS`: Spring Security không tạo `HttpSession`, không gửi cookie `JSESSIONID`.

Input: gọi `POST /api/auth/login` đúng mật khẩu, mở tab **Headers** của response trong Postman.

Output mong đợi: không có header `Set-Cookie: JSESSIONID=...`. Toàn bộ thông tin đăng nhập nằm trong `accessToken` của Body.

### IV. Luồng JWT trong Spring Security

![Luồng xử lý request có token](buoi10-10-luong-goi-api-co-token.png)

*Mỗi request đi qua 3 câu hỏi: có Bearer token không, token có hợp lệ không, có đủ quyền không; trả lời "Không" ở đâu thì nhận 401 hoặc 403 ở đó.*

#### 1. Bản chất kỹ thuật

-   **Đăng nhập:** `POST /api/auth/login` với `username`, `password`. `AuthenticationManager` xác thực, `JwtService` ký token, trả về `{"accessToken": "...", "tokenType": "Bearer", "expiresIn": 3600}`.
- **Gọi API:** client gửi header `Authorization: Bearer <accessToken>`.
- **`JwtAuthenticationFilter`** chạy ở mọi request:
  - Không có header hoặc không bắt đầu bằng `Bearer `: cho đi tiếp, không làm gì.
  - Có token: kiểm tra chữ ký và hạn dùng, lấy `username` trong `sub`, nạp `User` từ database.
  - Tạo `Authentication` chứa `User` và authorities, đặt vào `SecurityContextHolder`.
  - Token lỗi: ghi lý do vào request attribute, cho đi tiếp mà **không** có `Authentication`.
- **`AuthorizationFilter`** so request với `requestMatchers`. Chưa có `Authentication` mà API cần đăng nhập thì `RestAuthenticationEntryPoint` trả `401`. Có nhưng thiếu quyền thì `RestAccessDeniedHandler` trả `403`.
- **Controller** lấy user đang đăng nhập bằng `@AuthenticationPrincipal User currentUser`, hoặc `SecurityContextHolder.getContext().getAuthentication()`.

#### 2. Giải quyết bài toán gì?

- Biến JWT, một chuỗi văn bản, thành một `Authentication` mà toàn bộ Spring Security hiểu được. Từ đó `hasRole`, `@PreAuthorize`, `@AuthenticationPrincipal` đều hoạt động như với đăng nhập bằng form.
- Controller và Service không cần biết JWT là gì, chỉ làm việc với object `User`.

#### 3. Lưu ý & Best Practices

- Filter **không tự trả 401** khi token lỗi, mà để `AuthorizationFilter` quyết định. Nhờ vậy gửi token hết hạn tới `POST /api/auth/login` (API công khai) vẫn đăng nhập được bình thường.
- Header phải đúng dạng `Bearer <token>`: chữ `Bearer`, **một** dấu cách, rồi tới token. Copy thừa dấu ngoặc kép hay xuống dòng là token không hợp lệ.
- Mỗi request có token sinh thêm 1 câu `SELECT` vào bảng `users`. Đổi lại, user bị xóa hoặc đổi role có hiệu lực ngay ở request kế tiếp, không phải chờ token hết hạn.

#### 4. Demo

Phần lõi của `JwtAuthenticationFilter`:

```java
String username = jwtService.extractUsername(token);
UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
SecurityContext context = SecurityContextHolder.createEmptyContext();
context.setAuthentication(authentication);
SecurityContextHolder.setContext(context);
```

Giải thích:

- `extractUsername(token)`: kiểm tra chữ ký, hạn dùng và lấy `sub`. Token lỗi thì ném `JwtException` ngay tại dòng này.
- `loadUserByUsername(username)`: nạp `User` mới nhất từ bảng `users`.
- Constructor 3 tham số của `UsernamePasswordAuthenticationToken` tạo ra một `Authentication` **đã xác thực**; `credentials` là `null` vì không cần mật khẩu nữa.
- Tạo `SecurityContext` mới bằng `createEmptyContext()` rồi mới đặt vào holder, theo khuyến nghị của Spring Security để tránh xung đột giữa các luồng.

Input: `GET /api/users/me`, header `Authorization: Bearer <token của an>`.

Output mong đợi: Console có đúng 2 câu `SELECT` vào `users`: một của filter (nạp `User`), một của `/me` (nạp `User` kèm `KhachHang`). Kết quả JSON ở Phần 4.V.

### V. Lưu ý bảo mật khi dùng JWT

#### 1. Bản chất kỹ thuật

- Token đã phát ra thì có hiệu lực tới `exp`, **server không thu hồi được** vì không lưu gì. Bị lộ token là bị dùng trộm tới khi hết hạn.
- Toàn bộ an toàn của HS256 nằm ở secret. Lộ secret là ai cũng tự ký được token ADMIN.
- Token đi trong header HTTP. Không có HTTPS thì ai nghe lén đường truyền (Wi-Fi công cộng) cũng chép được token.

#### 2. Giải quyết bài toán gì?

- Ba rủi ro trên không thể loại bỏ hoàn toàn, chỉ có thể **giảm thiểu**: thời hạn ngắn giới hạn thời gian bị dùng trộm; secret mạnh và giữ kín chống làm giả; HTTPS chống nghe lén.

#### 3. Lưu ý & Best Practices

| Rủi ro             | Biện pháp trong bài                                   | Hướng nâng cao (chỉ nêu tên)                                |
| ------------------ | ----------------------------------------------------- | ----------------------------------------------------------- |
| Token bị lộ        | `jwt.expiration-ms` ngắn (1 giờ)                      | Access Token rất ngắn + Refresh Token                       |
| Không thu hồi được | Chấp nhận trong phạm vi bài tập                       | Danh sách đen token (blacklist) khi Logout, lưu trong Redis |
| Lộ secret          | Secret Base64 tối thiểu 256 bit, không commit lên Git | Biến môi trường, Secret Manager, ký bất đối xứng RS256      |
| Nghe lén           | Dùng HTTPS ở môi trường thật                          | HSTS                                                        |

- `application.properties` có secret thì **không đẩy lên GitHub công khai**. Cách đơn giản: đọc từ biến môi trường như Demo dưới đây.
- Mỗi môi trường (máy cá nhân, server thật) dùng một secret khác nhau.
- Đổi secret thì mọi token cũ lập tức hết hiệu lực. Đây cũng là cách "đăng xuất tất cả" trong tình huống khẩn cấp.

#### 4. Demo

Đọc secret từ biến môi trường, chỉ dùng giá trị mặc định khi chạy trên máy cá nhân:

```properties
jwt.secret=${JWT_SECRET:HQXnI7c9xi5ASIr/NeLuSilwlljzc1h0VeioMTO8rWo=}
jwt.expiration-ms=3600000
```

Giải thích:

- `${JWT_SECRET:...}`: Spring đọc biến môi trường `JWT_SECRET`; không có thì dùng giá trị sau dấu hai chấm.
- Tạo secret mới ngẫu nhiên 256 bit bằng lệnh `openssl rand -base64 32` (Git Bash, Linux, macOS).
- `3600000` mili giây là 1 giờ.

Input: tạm đặt `jwt.expiration-ms=10000`, đăng nhập lấy token, đợi hơn 10 giây rồi gọi `GET /api/tours`.

Output mong đợi: `401 Unauthorized`, Body `{"message": "Token da het han", "status": 401}`.

**Ghi nhớ nhanh:**

- JWT giống thẻ đoàn có đóng dấu: ai cũng đọc được chữ trên thẻ, nhưng không ai làm giả được con dấu.
- Payload là Base64URL, không phải bí mật: không bao giờ đặt password vào token.
- Session: server nhớ bạn. JWT: bạn tự mang theo giấy tờ, server chỉ kiểm tra con dấu.
- Token đã phát thì không thu hồi được: thời hạn ngắn và giữ kín secret là hai lớp bảo vệ chính.

---

## Phần 4: Bài tập chuẩn bị (Buổi 10)

![Lược đồ E-R sau Buổi 10](buoi10-11-erd-users.png)


![Cấu trúc package sau Buổi 10](buoi10-13-cau-truc-package.png)

**Lưu ý trước khi bắt đầu:**

- Mở `pom.xml`, xem phiên bản `spring-boot-starter-parent`. Tài liệu viết và tra cứu cho **Spring Boot 3.5.16** (Spring Security 6.5.11), bản vá cuối cùng của dòng 3.x. Nếu dự án đang ở 3.x cũ hơn, nên nâng lên bản này.
  - Ở Spring Security 6.5, bốn phương thức `isAccountNonExpired()`, `isAccountNonLocked()`, `isCredentialsNonExpired()`, `isEnabled()` của `UserDetails` đã là `default`, không cần viết. Ở các bản 6.x cũ (ví dụ 6.0), chúng vẫn là `abstract`: `User.java` sẽ báo lỗi "is not abstract and does not override abstract method", khi đó thêm 4 phương thức này vào `User`, mỗi phương thức `return true;`.
  - Hỗ trợ miễn phí (OSS) cho Spring Boot 3.5 đã kết thúc ngày 30/06/2026; dòng hiện hành là Spring Boot 4.x đi kèm Spring Security 7.x. Tài liệu giữ Spring Boot 3 để khớp dự án. Cách cấu hình trong bài (Lambda DSL, `authorizeHttpRequests`, mẫu URL dạng chuỗi) vẫn dùng được ở Spring Security 7, vì bản 7 chỉ xóa các kiểu viết cũ như `.and()`, `authorizeRequests`, `AntPathRequestMatcher`.
- Ngay khi thêm dependency bảo mật, **mọi** request Postman cũ sẽ nhận `401`. Đây là đúng thiết kế; Mục VIII hướng dẫn gắn token.
- `spring.jpa.hibernate.ddl-auto=update` tự tạo bảng `users` khi khởi động, không đụng dữ liệu các bảng cũ.
- Tên bảng là `users` chứ không phải `user`: `user` là từ khóa trong nhiều hệ quản trị (PostgreSQL, H2), dễ lỗi khi đổi database.
- JSON lỗi dùng `HashMap`, nên Jackson in khóa `message` trước `status`. Nội dung không đổi.
- **Trạng thái kiểm thử:** code Java trong tài liệu **chưa chạy thực tế** (môi trường soạn tài liệu không tải được thư viện từ Maven Central). Mọi Output của API là kết quả **mong đợi**, suy ra từ code. Các phần đã chạy thật: câu lệnh tạo bảng `users` (chạy trên MariaDB 10.11, tương thích MySQL), chuỗi hash BCrypt và token JWT mẫu (sinh bằng thư viện bcrypt, PyJWT).

### I. Yêu cầu 1 - Cập nhật thiết kế CSDL

**Mục tiêu:** bổ sung thực thể `User` vào lược đồ, trình bày theo đúng các bước thiết kế đã học.

#### 1. Bước 1: Khảo sát yêu cầu bổ sung

- Người dùng phải đăng ký, đăng nhập mới dùng được hệ thống.
- Có 2 vai trò: khách hàng (`USER`) tự đăng ký; quản lý (`ADMIN`) được tạo sẵn, không đăng ký qua API.
- Khách hàng chỉ đặt vé cho chính mình; hệ thống phải biết tài khoản đang đăng nhập là khách hàng nào.

#### 2. Bước 2: Xác định thực thể và thuộc tính

- `User`: lưu thông tin **đăng nhập**, gồm `MaUser`, `Username` (duy nhất), `Password` (chuỗi hash BCrypt), `Role` (`USER` hoặc `ADMIN`).
- Không lặp lại tên, số điện thoại, email trong `User`: những thông tin này thuộc về con người, đã có trong `KhachHang`.

#### 3. Bước 3: Xác định mối quan hệ

- `User` và `KhachHang`: quan hệ **1-1**, đặt tên `Co_Tai_Khoan`.
- Phân tích hai chiều:
  - Một `User` vai trò `USER` gắn với **đúng một** `KhachHang`. `User` vai trò `ADMIN` **không** gắn với khách hàng nào.
  - Một `KhachHang` có **không hoặc một** `User`: khách tạo tại quầy ở Buổi 9 chưa có tài khoản, khách tự đăng ký thì có đúng một tài khoản.

#### 4. Bước 4: Vẽ lược đồ E-R

- Lược đồ cập nhật nằm ở hình đầu Phần 4, dùng cùng ký hiệu với Bài tập Buổi 2: hình chữ nhật là thực thể, hình thoi là quan hệ, hình elip là thuộc tính, thuộc tính khóa gạch chân.
- Quan hệ `Co_Tai_Khoan` có số `1` ở cả hai đầu, không có ký hiệu chân gà như quan hệ 1-N `Mua`.

#### 5. Bước 5: Chuyển lược đồ E-R sang mô hình quan hệ

- **Quy tắc cho quan hệ 1-1:** đặt khóa ngoại ở **một** trong hai bảng, kèm ràng buộc `UNIQUE`. Thiếu `UNIQUE`, nhiều `User` có thể trỏ cùng một `KhachHang`, quan hệ biến thành N-1.
- **Chọn đặt khóa ngoại `ma_khach_hang` ở bảng `users`**, vì:
  - Không phải sửa bảng `khach_hang` đang có dữ liệu từ Buổi 9.
  - Tài khoản phụ thuộc vào khách hàng chứ không ngược lại: module đăng nhập được thêm sau, bám vào dữ liệu nghiệp vụ có sẵn.
  - Tài khoản ADMIN không có khách hàng: cột chỉ cần cho phép `NULL`. MySQL cho phép nhiều giá trị `NULL` trong một cột `UNIQUE`, nên tạo bao nhiêu ADMIN cũng được.
- Ở tầng Java, `User` giữ khóa ngoại nên là **Owning side**: `@OneToOne` + `@JoinColumn(name = "ma_khach_hang", unique = true)`.
- Mô hình quan hệ: `users (id, username, password, role, ma_khach_hang)`.
  - Khóa chính `id`. `username` là `UNIQUE`, `NOT NULL`.
  - `ma_khach_hang` là khóa ngoại tới `khach_hang(id)`, `UNIQUE`, cho phép `NULL`.

Câu lệnh tạo bảng tương đương với những gì Hibernate sinh ra (tên constraint do Hibernate tự đặt sẽ khác):

```sql
CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    ma_khach_hang BIGINT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_users_username UNIQUE (username),
    CONSTRAINT uk_users_ma_khach_hang UNIQUE (ma_khach_hang),
    CONSTRAINT fk_users_khach_hang FOREIGN KEY (ma_khach_hang) REFERENCES khach_hang (id)
) ENGINE = InnoDB;
```

#### 6. Bước 6: Chuẩn hóa dữ liệu (1NF, 2NF, 3NF)

- **1NF:** mọi cột của `users` đều giữ giá trị đơn. `role` là một giá trị duy nhất, không lưu danh sách role trong một ô.
- **2NF:** khóa chính đơn (`id`), không có khóa phức hợp nên tự động đạt 2NF.
- **3NF:** các cột không khóa (`password`, `role`) chỉ phụ thuộc vào khóa. Không lưu `email` hay `customerName` vào `users`: nếu lưu, `email` sẽ phụ thuộc bắc cầu `id -> ma_khach_hang -> email`, và hai nơi cùng giữ email sẽ có lúc lệch nhau.

### II. Yêu cầu 2 - Cấu hình nền tảng

**Mục tiêu:** thêm thư viện, cấu hình, Entity `User`, Repository và tài khoản ADMIN tạo sẵn khi khởi động.

#### 1. Thêm dependency vào `pom.xml`

- **A.** Thêm vào khối `<properties>` (khối có sẵn dòng `<java.version>`):

```xml
<jjwt.version>0.13.0</jjwt.version>
```

- **B.** Thêm vào khối `<dependencies>`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>${jjwt.version}</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>${jjwt.version}</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>${jjwt.version}</version>
    <scope>runtime</scope>
</dependency>
```

#### 2. Cấu hình `application.properties`

Thêm vào cuối file:

```properties
jwt.secret=HQXnI7c9xi5ASIr/NeLuSilwlljzc1h0VeioMTO8rWo=
jwt.expiration-ms=3600000
app.admin.username=admin
app.admin.password=Admin@123
```

Giải thích:

- `jwt.secret`: chuỗi Base64 của 32 byte ngẫu nhiên (256 bit), đủ độ dài cho HS256. Giá trị trên chỉ là mẫu, hãy tự tạo secret của mình bằng `openssl rand -base64 32` và không đưa lên GitHub công khai.
- `jwt.expiration-ms`: thời hạn token tính bằng mili giây. `3600000` là 1 giờ.
- `app.admin.*`: tài khoản ADMIN mà `DataInitializer` tạo sẵn ở lần khởi động đầu tiên.

#### 3. Tạo `Role` và `User`

- **A.** Trong package `entity`, tạo `Role.java`:

```java
package com.example.quanlytour.entity;

public enum Role {

    USER,
    ADMIN

}
```

- **B.** Tạo `User.java`:

```java
package com.example.quanlytour.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_khach_hang", unique = true)
    private KhachHang khachHang;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

}
```

- **C.** Giải thích:
  - `@Enumerated(EnumType.STRING)`: lưu chữ `"USER"`, `"ADMIN"` thay vì số thứ tự `0`, `1`. Nếu sau này chèn thêm giá trị vào giữa `enum`, dữ liệu cũ không bị đọc sai.
  - `@OneToOne(fetch = FetchType.LAZY)`: `@OneToOne` mặc định là `EAGER`, cố tình ghi đè thành `LAZY` theo quy ước của dự án.
  - `@JoinColumn(name = "ma_khach_hang", unique = true)`: cột khóa ngoại, `UNIQUE` để đúng 1-1. Không có `nullable = false` vì ADMIN không có khách hàng.
  - `implements UserDetails`: Spring Security dùng thẳng `User` làm principal. `@Getter` của Lombok sinh sẵn `getUsername()`, `getPassword()`, đúng hai phương thức mà `UserDetails` yêu cầu.
  - `getAuthorities()`: trả về `ROLE_USER` hoặc `ROLE_ADMIN`. Tiền tố `ROLE_` bắt buộc để `hasRole("ADMIN")` khớp (Phần 2.V).
  - Không có `@Data`, không có `@JsonIgnore` trên `password`: Entity `User` **không bao giờ** được trả ra JSON, mọi API đều trả `UserResponse`.

#### 4. Tạo `UserRepository`

```java
package com.example.quanlytour.repository;

import com.example.quanlytour.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @EntityGraph(attributePaths = {"khachHang"})
    Optional<User> findWithKhachHangByUsername(String username);

    @EntityGraph(attributePaths = {"khachHang"})
    @Query("SELECT u FROM User u ORDER BY u.id")
    List<User> findAllWithKhachHang();

}
```

Giải thích:

- `findByUsername`: dùng cho đăng nhập và cho `JwtAuthenticationFilter`. Chỉ nạp bảng `users`, `khachHang` vẫn là Proxy Lazy.
- `existsByUsername`: kiểm tra trùng khi đăng ký, chỉ cần trả `true`/`false`.
- `findWithKhachHangByUsername`: phần `WithKhachHang` nằm giữa `find` và `By` chỉ để đặt tên cho dễ đọc, Spring Data bỏ qua nó khi dịch. `@EntityGraph(attributePaths = {"khachHang"})` nạp luôn `KhachHang` trong cùng câu SQL.
- `findAllWithKhachHang`: danh sách user kèm khách hàng bằng **một** câu `LEFT JOIN`, tránh N + 1 khi map sang `UserResponse`.

#### 5. Tạo `DataInitializer`

Trong package mới `config`, tạo `DataInitializer.java`:

```java
package com.example.quanlytour.config;

import com.example.quanlytour.entity.Role;
import com.example.quanlytour.entity.User;
import com.example.quanlytour.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminUsername;
    private final String adminPassword;

    public DataInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           @Value("${app.admin.username}") String adminUsername,
                           @Value("${app.admin.password}") String adminPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(String... args) {
        if (userRepository.existsByUsername(adminUsername)) {
            return;
        }
        User admin = new User();
        admin.setUsername(adminUsername);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);
        LOGGER.info("Da tao tai khoan ADMIN mac dinh: {}", adminUsername);
    }

}
```

Giải thích:

- `CommandLineRunner`: Spring Boot gọi `run(...)` một lần, ngay sau khi ứng dụng khởi động xong.
- `@Value("${app.admin.username}")`: đọc giá trị trong `application.properties`, truyền vào constructor.
- `existsByUsername(adminUsername)`: đã có thì thoát. Nhờ vậy khởi động lại bao nhiêu lần cũng chỉ có một tài khoản `admin`.
- `passwordEncoder.encode(adminPassword)`: mật khẩu ADMIN cũng phải lưu dạng hash. Bean `PasswordEncoder` được khai báo trong `SecurityConfig` (Mục III), nên phải làm xong Mục III ứng dụng mới khởi động được.
- Không gán `khachHang`: cột `ma_khach_hang` của ADMIN là `NULL`.

Output mong đợi trong Console ở lần khởi động đầu tiên (sau khi làm xong Mục III):

```text
Hibernate: create table users (id bigint not null auto_increment, password varchar(100) not null, role ..., username varchar(50) not null, ma_khach_hang bigint, primary key (id)) engine=InnoDB
...
INFO ... c.e.quanlytour.config.DataInitializer : Da tao tai khoan ADMIN mac dinh: admin
```

Kiểm tra trong MySQL Workbench:

```sql
SELECT id, username, LEFT(password, 7) AS password_prefix, role, ma_khach_hang FROM users;
```

Output mong đợi: một dòng `1 | admin | $2a$10$ | ADMIN | NULL`.

### III. Yêu cầu 3 - JWT và Security

**Mục tiêu:** tạo package `security` gồm 6 class: ký và kiểm tra token, nạp user, filter đọc token, hai class trả lỗi 401/403, và cấu hình bảo mật.

#### 1. `JwtService`

```java
package com.example.quanlytour.security;

import com.example.quanlytour.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtService {

    private final SecretKey secretKey;
    private final long expirationMs;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration-ms}") long expirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expirationMs = expirationMs;
    }

    public String generateToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
```

Giải thích:

- Constructor nhận `jwt.secret`, `jwt.expiration-ms` qua `@Value`:
  - `Decoders.BASE64.decode(secret)`: đổi chuỗi Base64 thành mảng byte.
  - `Keys.hmacShaKeyFor(...)`: tạo `SecretKey` dùng cho HMAC-SHA. Secret dưới 256 bit thì ném `WeakKeyException` ngay khi khởi động.
- `generateToken(user)`, theo đúng thứ tự các claim trong payload:
  - `subject(...)`: claim `sub` là `username`.
  - `claim("role", ...)`: claim tự thêm, giá trị `"USER"` hoặc `"ADMIN"`.
  - `issuedAt(now)`, `expiration(...)`: claim `iat`, `exp`.
  - `signWith(secretKey)`: ký. Khóa 256 bit nên jjwt tự chọn thuật toán `HS256`.
  - `compact()`: ghép thành chuỗi `header.payload.signature`.
- `parseClaims(token)`: kiểm tra chữ ký và hạn dùng. Token lỗi thì ném lớp con của `JwtException`; chuỗi rỗng thì ném `IllegalArgumentException`.
- Dùng API mới của jjwt: `subject()`, `verifyWith()`, `parseSignedClaims()`. Các tên cũ `setSubject()`, `parserBuilder()`, `setSigningKey()`, `parseClaimsJws()` đã deprecated, không dùng.

#### 2. `CustomUserDetailsService`

```java
package com.example.quanlytour.security;

import com.example.quanlytour.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Khong tim thay user: " + username));
    }

}
```

Giải thích:

- `implements UserDetailsService`: Spring Boot thấy bean này thì tự dùng nó cho `DaoAuthenticationProvider`, và **không** tạo tài khoản `user` mặc định nữa.
- Trả về chính `User` vì `User` đã `implements UserDetails`.
- Không tìm thấy thì ném `UsernameNotFoundException`. Khi đăng nhập, Spring Security tự đổi lỗi này thành `BadCredentialsException` để không lộ username nào có tồn tại.

#### 3. `JwtAuthenticationFilter`

```java
package com.example.quanlytour.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String JWT_ERROR_ATTRIBUTE = "jwtError";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(BEARER_PREFIX.length());
        try {
            String username = jwtService.extractUsername(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        } catch (ExpiredJwtException exception) {
            request.setAttribute(JWT_ERROR_ATTRIBUTE, "Token da het han");
        } catch (JwtException | IllegalArgumentException | UsernameNotFoundException exception) {
            request.setAttribute(JWT_ERROR_ATTRIBUTE, "Token khong hop le");
        }

        filterChain.doFilter(request, response);
    }

}
```

Giải thích:

- `extends OncePerRequestFilter`: chạy đúng một lần mỗi request, chỉ cần viết `doFilterInternal`.
- `JWT_ERROR_ATTRIBUTE`: tên request attribute để báo cho `RestAuthenticationEntryPoint` biết lý do token lỗi. Để `public static` cho class kia dùng chung.
- Khối kiểm tra header: không có `Authorization` hoặc không bắt đầu bằng `"Bearer "` thì gọi `filterChain.doFilter(...)` cho đi tiếp và `return`. Request đi tiếp mà không có `Authentication`; API công khai vẫn chạy, API cần đăng nhập sẽ bị `AuthorizationFilter` chặn.
- `substring(BEARER_PREFIX.length())`: cắt bỏ 7 ký tự `"Bearer "`, còn lại là token.
- Khối `try` (giải thích chi tiết ở Phần 3.IV): kiểm tra token, nạp user, đặt `Authentication` vào `SecurityContextHolder`.
- Hai khối `catch`:
  - **Bắt buộc phải có**, vì exception ném ra trong Filter không đi qua `GlobalExceptionHandler`. Thiếu `catch`, client nhận lỗi `500` hoặc trang lỗi mặc định.
  - `ExpiredJwtException` đứng trước vì nó là lớp con của `JwtException`.
  - Không trả lỗi ngay mà chỉ ghi lý do vào request attribute rồi cho đi tiếp, để API công khai như `/api/auth/login` vẫn hoạt động khi client lỡ gửi kèm token cũ.

#### 4. `RestAuthenticationEntryPoint` và `RestAccessDeniedHandler`

- **A.** `RestAuthenticationEntryPoint.java`:

```java
package com.example.quanlytour.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        Object jwtError = request.getAttribute(JwtAuthenticationFilter.JWT_ERROR_ATTRIBUTE);
        String message = jwtError != null ? jwtError.toString() : "Chua xac thuc";

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        body.put("message", message);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        objectMapper.writeValue(response.getOutputStream(), body);
    }

}
```

- **B.** `RestAccessDeniedHandler.java`:

```java
package com.example.quanlytour.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.FORBIDDEN.value());
        body.put("message", "Khong co quyen truy cap");

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        objectMapper.writeValue(response.getOutputStream(), body);
    }

}
```

- **C.** Giải thích:
  - `AuthenticationEntryPoint` được `ExceptionTranslationFilter` gọi khi request **chưa xác thực** mà API yêu cầu đăng nhập: trả `401`.
  - `AccessDeniedHandler` được gọi khi **đã xác thực** nhưng thiếu quyền: trả `403`.
  - `ObjectMapper`: bean Jackson có sẵn của Spring Boot, dùng để tự ghi JSON vào response. Ở tầng Filter không có `@RestControllerAdvice` hay `ResponseEntity`, phải tự ghi.
  - `request.getAttribute(JWT_ERROR_ATTRIBUTE)`: có lý do từ filter thì dùng (`"Token da het han"`, `"Token khong hop le"`), không có thì dùng `"Chua xac thuc"`.
  - Cả hai trả đúng cấu trúc `{"status": ..., "message": "..."}` như `GlobalExceptionHandler`, client chỉ cần một cách đọc lỗi.

#### 5. `SecurityConfig`

Phiên bản đầu tiên, chỉ phân quyền cho các API mới. Mục VII sẽ bổ sung quy tắc cho các API cũ.

```java
package com.example.quanlytour.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final RestAccessDeniedHandler restAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                        .accessDeniedHandler(restAccessDeniedHandler))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
```

Giải thích:

- `@EnableWebSecurity`: bật cấu hình bảo mật web. `@EnableMethodSecurity`: bật `@PreAuthorize` (dùng ở Mục VI).
- `@Bean SecurityFilterChain`: cách cấu hình của Spring Security 6, viết bằng Lambda DSL (mỗi phần cấu hình là một lambda). Không còn `extends WebSecurityConfigurerAdapter`.
- `csrf(csrf -> csrf.disable())`: an toàn vì token gửi qua header, không qua cookie (Phần 3.III).
- `SessionCreationPolicy.STATELESS`: không tạo `HttpSession`, mỗi request tự mang token.
- `authorizeHttpRequests(...)`, theo thứ tự từ trên xuống:
  - `/error` và `/api/auth/**` mở cho tất cả (`permitAll`).
  - `/api/users/me` chỉ cần đăng nhập; `GET /api/users` chỉ ADMIN.
  - `anyRequest().authenticated()`: mọi API còn lại phải đăng nhập.
- `exceptionHandling(...)`: đăng ký hai class trả lỗi 401, 403 vừa viết. Quên dòng này thì người chưa đăng nhập nhận `403` thay vì `401` (Phần 1.III).
- `addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)`: chèn filter JWT vào đúng vị trí (Phần 2.II).
- `PasswordEncoder`: bean `BCryptPasswordEncoder`, dùng chung cho `AuthService`, `DataInitializer` và `DaoAuthenticationProvider`.
- `AuthenticationManager`: lấy từ `AuthenticationConfiguration` mà Spring Boot đã dựng sẵn (bên trong là `DaoAuthenticationProvider` dùng `CustomUserDetailsService` và `PasswordEncoder` ở trên), để `AuthService` inject được.

**Kiểm tra nhanh:** Stop rồi Run lại ứng dụng.

- Console **không còn** dòng `Using generated security password`, và có dòng `Da tao tai khoan ADMIN mac dinh: admin`.
- `GET http://localhost:8080/api/tours` không kèm token: `401`, Body `{"message": "Chua xac thuc", "status": 401}`.

### IV. Yêu cầu 4 - Đăng ký, đăng nhập

**Mục tiêu:** `POST /api/auth/register` tạo đồng thời `KhachHang` và `User` role `USER`; `POST /api/auth/login` trả về JWT.

![Luồng đăng ký và đăng nhập](buoi10-9-luong-dang-ky-dang-nhap.png)

*Đăng ký: kiểm tra trùng, mã hóa mật khẩu, lưu. Đăng nhập: AuthenticationManager xác thực, JwtService ký token.*

#### 1. Các DTO

- **A.** Trong package `dto`, tạo `RegisterRequest.java`:

```java
package com.example.quanlytour.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "Ten dang nhap khong duoc de trong")
        @Size(min = 4, max = 50, message = "Ten dang nhap tu 4 den 50 ky tu")
        @Pattern(regexp = "^[a-zA-Z0-9._]+$", message = "Ten dang nhap chi gom chu khong dau, so, dau cham, gach duoi")
        String username,

        @NotBlank(message = "Mat khau khong duoc de trong")
        @Size(min = 6, max = 72, message = "Mat khau tu 6 den 72 ky tu")
        String password,

        @NotBlank(message = "Ten khach hang khong duoc de trong")
        String customerName,

        @Pattern(regexp = "^0\\d{9}$", message = "So dien thoai gom 10 chu so va bat dau bang 0")
        String phone,

        @NotBlank(message = "Email khong duoc de trong")
        @Email(message = "Email khong dung dinh dang")
        String email) {
}
```

- **B.** `LoginRequest.java`:

```java
package com.example.quanlytour.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "Ten dang nhap khong duoc de trong")
        String username,

        @NotBlank(message = "Mat khau khong duoc de trong")
        String password) {
}
```

- **C.** `AuthResponse.java`:

```java
package com.example.quanlytour.dto;

public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresIn) {
}
```

- **D.** `UserResponse.java`:

```java
package com.example.quanlytour.dto;

import com.example.quanlytour.entity.KhachHang;
import com.example.quanlytour.entity.Role;
import com.example.quanlytour.entity.User;

public record UserResponse(
        Long id,
        String username,
        Role role,
        String customerName,
        String phone,
        String email) {

    public static UserResponse from(User user) {
        KhachHang khachHang = user.getKhachHang();
        if (khachHang == null) {
            return new UserResponse(user.getId(), user.getUsername(), user.getRole(), null, null, null);
        }
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                khachHang.getCustomerName(),
                khachHang.getPhone(),
                khachHang.getEmail());
    }

}
```

- **E.** Giải thích:
  - `RegisterRequest` áp dụng Validation của Buổi 8:
    - `username`: `@Pattern` chỉ cho chữ không dấu, số, `.` và `_`, nên không có khoảng trắng hai đầu và không cần `trim()`.
    - `password`: `@Size(max = 72)` vì giới hạn 72 byte của BCrypt (Phần 2.IV).
    - `phone` không bắt buộc, nhưng nếu gửi thì phải đúng 10 chữ số bắt đầu bằng `0`.
  - `AuthResponse`: `tokenType` luôn là `"Bearer"`, `expiresIn` tính bằng **giây** theo thông lệ của OAuth2.
  - `UserResponse` **không có** trường `password`. `from(user)` xử lý cả trường hợp ADMIN không có khách hàng: các trường khách hàng là `null`.

#### 2. `AuthService`

```java
package com.example.quanlytour.service;

import com.example.quanlytour.dto.AuthResponse;
import com.example.quanlytour.dto.LoginRequest;
import com.example.quanlytour.dto.RegisterRequest;
import com.example.quanlytour.dto.UserResponse;
import com.example.quanlytour.entity.KhachHang;
import com.example.quanlytour.entity.Role;
import com.example.quanlytour.entity.User;
import com.example.quanlytour.repository.KhachHangRepository;
import com.example.quanlytour.repository.UserRepository;
import com.example.quanlytour.security.JwtService;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String TOKEN_TYPE = "Bearer";
    private static final int MAX_PASSWORD_BYTES = 72;

    private final UserRepository userRepository;
    private final KhachHangRepository khachHangRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        String username = request.username();
        if (userRepository.existsByUsername(username)) {
            throw new IllegalStateException("Ten dang nhap da ton tai");
        }
        if (request.password().getBytes(StandardCharsets.UTF_8).length > MAX_PASSWORD_BYTES) {
            throw new IllegalStateException("Mat khau vuot qua 72 byte");
        }

        KhachHang khachHang = new KhachHang();
        khachHang.setCustomerName(request.customerName().trim());
        khachHang.setPhone(request.phone());
        khachHang.setEmail(request.email());
        khachHangRepository.save(khachHang);

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        user.setKhachHang(khachHang);
        return UserResponse.from(userRepository.save(user));
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        User user = (User) authentication.getPrincipal();
        String accessToken = jwtService.generateToken(user);
        return new AuthResponse(accessToken, TOKEN_TYPE, jwtService.getExpirationMs() / 1000);
    }

}
```

Giải thích:

- `register` có `@Transactional`: lưu `KhachHang` và `User` cùng thành công hoặc cùng bị hủy. Nếu lưu `User` lỗi, không còn sót lại một `KhachHang` mồ côi.
  - Trùng username: ném `IllegalStateException`, `GlobalExceptionHandler` có sẵn trả `400`.
  - Mật khẩu quá 72 byte (ví dụ 72 ký tự có dấu): chặn trước bằng `IllegalStateException` (`400`). Nếu để `encode` tự xử lý, Spring Security 6.5 ném `IllegalArgumentException`, và `GlobalExceptionHandler` của Buổi 8 sẽ đổi nhầm thành `404`.
  - `passwordEncoder.encode(...)`: chỉ chuỗi hash được lưu xuống database.
  - `user.setKhachHang(khachHang)`: gán Owning side, Hibernate điền cột `ma_khach_hang`.
  - Ràng buộc `unique = true` của cột `username` vẫn là lớp bảo vệ cuối khi hai request đăng ký cùng username chạy đồng thời.
- `login` **không** tự kiểm tra mật khẩu mà giao cho `authenticationManager.authenticate(...)`:
  - Đúng: nhận về `Authentication` có `principal` là `User`, ép kiểu rồi ký token.
  - Sai username hoặc password: ném `BadCredentialsException`. Lỗi này ném ra **trong** Controller nên đi qua `GlobalExceptionHandler` (bước 3 dưới đây).
  - `getExpirationMs() / 1000`: đổi mili giây sang giây cho `expiresIn`.

#### 3. `AuthController`

Trong package `controller`, tạo `AuthController.java`:

```java
package com.example.quanlytour.controller;

import com.example.quanlytour.dto.AuthResponse;
import com.example.quanlytour.dto.LoginRequest;
import com.example.quanlytour.dto.RegisterRequest;
import com.example.quanlytour.dto.UserResponse;
import com.example.quanlytour.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

}
```

Giải thích:

- Đường dẫn gốc `/api/auth` đã được `permitAll()` trong `SecurityConfig`, gọi không cần token.
- `@Valid`: dữ liệu sai định dạng bị chặn trước khi vào Service, trả `400` qua `handleValidationException` có sẵn.

#### 4. Bổ sung `GlobalExceptionHandler`

Thêm vào `GlobalExceptionHandler`, đặt **trước** phương thức `handleGeneralException`:

```java
@ExceptionHandler(BadCredentialsException.class)
public ResponseEntity<Map<String, Object>> handleBadCredentialsException(BadCredentialsException exception) {
    Map<String, Object> body = new HashMap<>();
    body.put("status", HttpStatus.UNAUTHORIZED.value());
    body.put("message", "Sai ten dang nhap hoac mat khau");
    return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
}
```

Thêm `import`:

```java
import org.springframework.security.authentication.BadCredentialsException;
```

Giải thích:

- Thiếu handler này, `BadCredentialsException` rơi vào `handleGeneralException` và client nhận `500` cho một lỗi do nhập sai mật khẩu.
- Message cố định, không dùng `exception.getMessage()`, để không lộ chi tiết nội bộ.

### V. Yêu cầu 5 - API thông tin của chính mình

**Mục tiêu:** `GET /api/users/me` trả về thông tin của người đang đăng nhập, **không** nhận bất kỳ tham số nào.

#### 1. Vì sao không được nhận `id`

- Cách viết sai thường gặp:

```java
@GetMapping("/{id}")
public UserResponse getUser(@PathVariable Long id) {
    return userService.getUserById(id);
}
```

- User `an` đăng nhập, gọi `GET /api/users/2` thấy thông tin của mình. Đổi thành `GET /api/users/3` là xem được của người khác. Lỗ hổng này gọi là **IDOR** (Insecure Direct Object Reference): server tin vào định danh do client gửi lên mà không kiểm tra người gọi có quyền với đối tượng đó hay không.
- Cách đúng: danh tính lấy từ **token**, thứ client không sửa được. Client không gửi gì, nên không có gì để sửa.

#### 2. `UserService`

Trong package `service`, tạo `UserService.java`:

```java
package com.example.quanlytour.service;

import com.example.quanlytour.dto.UserResponse;
import com.example.quanlytour.entity.User;
import com.example.quanlytour.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(String username) {
        User user = userRepository.findWithKhachHangByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay user: " + username));
        return UserResponse.from(user);
    }

}
```

#### 3. `UserController`

Trong package `controller`, tạo `UserController.java`:

```java
package com.example.quanlytour.controller;

import com.example.quanlytour.dto.UserResponse;
import com.example.quanlytour.entity.User;
import com.example.quanlytour.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserResponse getCurrentUser(@AuthenticationPrincipal User currentUser) {
        return userService.getCurrentUser(currentUser.getUsername());
    }

}
```

#### 4. Giải thích

- `@AuthenticationPrincipal User currentUser`: Spring lấy `principal` mà `JwtAuthenticationFilter` đã đặt vào `SecurityContextHolder`. Phương thức không có `@PathVariable`, `@RequestParam` nào.
- Controller chỉ lấy `currentUser.getUsername()` rồi giao cho Service nạp lại dữ liệu.
- **Bẫy `LazyInitializationException`:** `currentUser` do filter nạp bằng `findByUsername`, ở một Persistence Context đã đóng ngay sau câu `SELECT` (nối lại Phần 1.I Buổi 8). Trường `khachHang` của nó chỉ là Proxy Lazy.
  - Nếu viết tắt `return UserResponse.from(currentUser);`, dòng `khachHang.getCustomerName()` bên trong `from` sẽ ném `LazyInitializationException: could not initialize proxy [...KhachHang#...] - no Session`, client nhận `500`.
  - Cách sửa trong bài: `getCurrentUser` chạy trong `@Transactional(readOnly = true)` và dùng `findWithKhachHangByUsername` có `@EntityGraph`, nạp `User` kèm `KhachHang` bằng một câu `LEFT JOIN`.

Output mong đợi (Console) khi gọi `/me` bằng token của `an`, câu thứ hai là của `getCurrentUser`:

```text
Hibernate: select u1_0.id, u1_0.ma_khach_hang, u1_0.password, u1_0.role, u1_0.username from users u1_0 where u1_0.username=?
Hibernate: select u1_0.id, k1_0.id, k1_0.email, ..., u1_0.username from users u1_0 left join khach_hang k1_0 on k1_0.id=u1_0.ma_khach_hang where u1_0.username=?
```

### VI. Yêu cầu 6 - API danh sách user cho ADMIN

**Mục tiêu:** `GET /api/users` trả về toàn bộ user, chỉ ADMIN dùng được; chặn ở cả `SecurityConfig` và `@PreAuthorize` để thấy rõ hai cách.

#### 1. Thêm vào `UserService`

```java
@Transactional(readOnly = true)
public List<UserResponse> getAllUsers() {
    return userRepository.findAllWithKhachHang()
            .stream()
            .map(UserResponse::from)
            .toList();
}
```

Thêm `import java.util.List;`.

#### 2. Thêm vào `UserController`

```java
@GetMapping
@PreAuthorize("hasRole('ADMIN')")
public List<UserResponse> getAllUsers() {
    return userService.getAllUsers();
}
```

Thêm `import`:

```java
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
```

#### 3. Hai lớp chặn

- **Lớp 1, theo URL:** dòng `.requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")` đã có trong `SecurityConfig` ở Mục III. USER bị chặn ngay ở `AuthorizationFilter`, lỗi trả qua `RestAccessDeniedHandler`.
- **Lớp 2, theo phương thức:** `@PreAuthorize("hasRole('ADMIN')")` trên `getAllUsers`. Có hiệu lực nhờ `@EnableMethodSecurity` trong `SecurityConfig`.
- Bình thường lớp 1 chặn trước nên lớp 2 không bao giờ được chạm tới. Để thấy lớp 2 hoạt động, **tạm xóa** dòng `requestMatchers` của `/api/users` rồi chạy lại, gọi bằng token USER:
  - Chưa làm bước 4 dưới đây: nhận `500`. `@PreAuthorize` ném `AccessDeniedException` bên trong Controller, `@RestControllerAdvice` bắt được trước Spring Security và rơi vào `handleGeneralException`.
  - Đã làm bước 4: nhận `403` với đúng JSON lỗi.
- Thử xong nhớ thêm lại dòng `requestMatchers` đã xóa.

#### 4. Bổ sung `GlobalExceptionHandler`

Thêm vào `GlobalExceptionHandler`, đặt **trước** `handleGeneralException`:

```java
@ExceptionHandler(AccessDeniedException.class)
public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException exception) {
    Map<String, Object> body = new HashMap<>();
    body.put("status", HttpStatus.FORBIDDEN.value());
    body.put("message", "Khong co quyen truy cap");
    return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
}
```

Thêm `import`:

```java
import org.springframework.security.access.AccessDeniedException;
```

Giải thích:

- Chú ý import đúng `org.springframework.security.access.AccessDeniedException`. Java còn một class trùng tên là `java.nio.file.AccessDeniedException`, chọn nhầm thì handler không bao giờ chạy.
- Handler này bắt cả lớp con, gồm `AuthorizationDeniedException` mà `@PreAuthorize` ném ra ở các bản Spring Security mới.

#### 5. Tránh N + 1

- `getAllUsers` dùng `findAllWithKhachHang()` có `@EntityGraph`. Dù có bao nhiêu user, Console chỉ có **một** câu SQL:

```text
Hibernate: select u1_0.id, k1_0.id, k1_0.email, k1_0.so_dien_thoai, k1_0.ten_khach_hang, ... from users u1_0 left join khach_hang k1_0 on k1_0.id=u1_0.ma_khach_hang order by u1_0.id
```

- Nếu dùng `userRepository.findAll()`, mỗi lần `UserResponse.from(user)` đọc `getCustomerName()` lại sinh một câu `select ... from khach_hang k1_0 where k1_0.id=?`, đúng mẫu 1 + N.
- `LEFT JOIN` (không phải `JOIN`): giữ lại cả ADMIN không có khách hàng.

### VII. Yêu cầu 7 - Áp phân quyền cho API cũ

**Mục tiêu:** toàn bộ API của Buổi 8 + 9 được phân quyền theo ma trận; đặt vé lấy khách hàng từ token thay vì `khachHangId`.

#### 1. Ma trận phân quyền

| API                                                                | Quyền                        |
| ------------------------------------------------------------------ | ---------------------------- |
| `POST /api/auth/register`, `POST /api/auth/login`                  | Công khai                    |
| `GET /api/users/me`                                                | Đã đăng nhập (USER, ADMIN)   |
| `GET /api/users`                                                   | ADMIN                        |
| `GET /api/tours/revenue`                                           | ADMIN                        |
| `GET /api/tours/**`, `GET /api/lich-trinh/**`                      | Đã đăng nhập                 |
| `POST`, `PUT`, `DELETE` trên `/api/tours/**`, `/api/lich-trinh/**` | ADMIN                        |
| `POST /api/hoa-don`                                                | USER (đặt vé cho chính mình) |
| `GET /api/hoa-don/search`                                          | ADMIN                        |
| `/api/khach-hang/**`                                               | ADMIN                        |
| Mọi API còn lại                                                    | Đã đăng nhập                 |

![Ma trận phân quyền](buoi10-12-ma-tran-phan-quyen.png)

*Mỗi ô là kết quả khi gọi API đó: khách vãng lai luôn 401 trừ đăng ký, đăng nhập; USER và ADMIN bị 403 ở đúng các ô ngoài quyền của mình.*

#### 2. Cập nhật `SecurityConfig`

Trong `SecurityConfig`, thay **toàn bộ** khối `.authorizeHttpRequests(...)` bằng:

```java
.authorizeHttpRequests(auth -> auth
        .requestMatchers("/error").permitAll()
        .requestMatchers("/api/auth/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/users/me").authenticated()
        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
        .requestMatchers(HttpMethod.GET, "/api/tours/revenue").hasRole("ADMIN")
        .requestMatchers(HttpMethod.GET, "/api/tours/**", "/api/lich-trinh/**").authenticated()
        .requestMatchers("/api/tours/**", "/api/lich-trinh/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.POST, "/api/hoa-don").hasRole("USER")
        .requestMatchers(HttpMethod.GET, "/api/hoa-don/search").hasRole("ADMIN")
        .requestMatchers("/api/khach-hang/**").hasRole("ADMIN")
        .anyRequest().authenticated())
```

Giải thích, theo đúng thứ tự (quy tắc đầu tiên khớp sẽ được áp dụng):

- `GET /api/tours/revenue` (ADMIN) đứng **trước** `GET /api/tours/**` (đã đăng nhập). Đảo ngược thì USER xem được doanh thu.
- `GET /api/tours/**` (đã đăng nhập) đứng **trước** dòng không ghi method `"/api/tours/**"` (ADMIN). Dòng không ghi method khớp mọi method, nhưng `GET` đã được dòng trên "nhận" trước, nên dòng này thực chất chỉ còn áp dụng cho `POST`, `PUT`, `DELETE`.
- `/api/tours/**` khớp cả `/api/tours` lẫn `/api/tours/5`: `**` khớp không hoặc nhiều đoạn đường dẫn.
- `POST /api/hoa-don` dùng `hasRole("USER")`: ADMIN không có khách hàng nên không được đặt vé.
- `anyRequest().authenticated()` vẫn đứng cuối.

#### 3. Sửa `BookTicketRequest`

Thay toàn bộ nội dung `BookTicketRequest.java`:

```java
package com.example.quanlytour.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record BookTicketRequest(

        @NotNull(message = "Ma lich trinh khong duoc de trong")
        Long lichTrinhId,

        @NotNull(message = "So luong khach khong duoc de trong")
        @Min(value = 1, message = "So luong khach toi thieu la 1")
        Integer guestCount) {
}
```

- Bỏ hẳn `khachHangId`: client không còn cách nào chỉ định đặt vé cho người khác.

#### 4. Sửa `HoaDonService`

Thay toàn bộ nội dung `HoaDonService.java`:

```java
package com.example.quanlytour.service;

import com.example.quanlytour.dto.BookTicketRequest;
import com.example.quanlytour.dto.HoaDonResponse;
import com.example.quanlytour.entity.HoaDon;
import com.example.quanlytour.entity.KhachHang;
import com.example.quanlytour.entity.LichTrinh;
import com.example.quanlytour.entity.User;
import com.example.quanlytour.repository.HoaDonRepository;
import com.example.quanlytour.repository.LichTrinhRepository;
import com.example.quanlytour.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HoaDonService {

    private final HoaDonRepository hoaDonRepository;
    private final LichTrinhRepository lichTrinhRepository;
    private final UserRepository userRepository;

    @Transactional
    public HoaDonResponse bookTicket(BookTicketRequest request, String username) {
        User user = userRepository.findWithKhachHangByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay user: " + username));
        KhachHang khachHang = user.getKhachHang();
        if (khachHang == null) {
            throw new IllegalStateException("Tai khoan chua gan voi khach hang");
        }

        LichTrinh lichTrinh = lichTrinhRepository.findById(request.lichTrinhId())
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay lich trinh id = " + request.lichTrinhId()));

        int bookedSlots = lichTrinh.getBookedSlots() == null ? 0 : lichTrinh.getBookedSlots();
        if (bookedSlots + request.guestCount() > lichTrinh.getTotalSlots()) {
            throw new IllegalStateException("Khong du cho trong");
        }
        lichTrinh.setBookedSlots(bookedSlots + request.guestCount());

        HoaDon hoaDon = new HoaDon();
        hoaDon.setKhachHang(khachHang);
        hoaDon.setLichTrinh(lichTrinh);
        hoaDon.setGuestCount(request.guestCount());
        hoaDon.setTicketPrice(lichTrinh.getBasePrice());

        return HoaDonResponse.from(hoaDonRepository.save(hoaDon));
    }

    @Transactional(readOnly = true)
    public List<HoaDonResponse> searchByMinTotal(Double minTotal) {
        return hoaDonRepository.findInvoicesWithTotalGreaterThan(minTotal)
                .stream()
                .map(HoaDonResponse::from)
                .toList();
    }

}
```

Giải thích:

- Thay `KhachHangRepository` bằng `UserRepository`: khách hàng giờ được suy ra từ tài khoản.
- `bookTicket(request, username)`:
  - `findWithKhachHangByUsername(username)`: nạp `User` kèm `KhachHang` **trong** transaction này. Không dùng `khachHang` của principal vì đó là Proxy đã rời Persistence Context (Mục V).
  - Tài khoản không gắn khách hàng: ném `IllegalStateException`, trả `400`. Với cấu hình ở bước 2, trường hợp này chỉ xảy ra khi ai đó nới quyền cho ADMIN đặt vé.
  - Phần còn lại giữ nguyên logic Buổi 9: kiểm tra chỗ trống, tăng `bookedSlots`, tạo `HoaDon`, chụp giá vé.
- `searchByMinTotal` giữ nguyên như Buổi 9.

#### 5. Sửa `HoaDonController`

Thay toàn bộ nội dung `HoaDonController.java`:

```java
package com.example.quanlytour.controller;

import com.example.quanlytour.dto.BookTicketRequest;
import com.example.quanlytour.dto.HoaDonResponse;
import com.example.quanlytour.entity.User;
import com.example.quanlytour.service.HoaDonService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hoa-don")
@RequiredArgsConstructor
public class HoaDonController {

    private final HoaDonService hoaDonService;

    @PostMapping
    public HoaDonResponse bookTicket(@Valid @RequestBody BookTicketRequest request,
                                     @AuthenticationPrincipal User currentUser) {
        return hoaDonService.bookTicket(request, currentUser.getUsername());
    }

    @GetMapping("/search")
    public List<HoaDonResponse> searchByMinTotal(@RequestParam Double minTotal) {
        return hoaDonService.searchByMinTotal(minTotal);
    }

}
```

Giải thích:

- Thêm tham số `@AuthenticationPrincipal User currentUser` và truyền `currentUser.getUsername()` xuống Service.
- Chú ý import `com.example.quanlytour.entity.User`, không phải `org.springframework.security.core.userdetails.User`. Chọn nhầm thì `currentUser` luôn là `null`.

#### 6. `GlobalExceptionHandler` hoàn chỉnh

 Đối chiếu với bản dưới đây:

```java
package com.example.quanlytour.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
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

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentialsException(BadCredentialsException exception) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        body.put("message", "Sai ten dang nhap hoac mat khau");
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException exception) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.FORBIDDEN.value());
        body.put("message", "Khong co quyen truy cap");
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
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

- Hai handler mới: `BadCredentialsException` trả `401` (sai đăng nhập), `AccessDeniedException` trả `403` (thiếu quyền ở `@PreAuthorize`).
- Bốn handler cũ giữ nguyên. `handleGeneralException` vẫn là lưới bắt cuối cùng.
- Lỗi 401, 403 phát sinh trong Filter không đi qua class này mà đi qua `RestAuthenticationEntryPoint`, `RestAccessDeniedHandler`; cả hai nơi trả cùng một cấu trúc JSON.
- Giới hạn có từ Buổi 8, ngoài phạm vi buổi này: `handleGeneralException` cũng bắt các lỗi của Spring MVC như Body JSON sai cú pháp, thiếu tham số, URL không tồn tại, và trả `500` thay vì `400`, `404`. Khi kiểm thử ở Mục VIII, luôn gửi JSON đúng cú pháp.

### VIII. Kiểm thử trên Postman

Dữ liệu giả định có sẵn từ Buổi 9: tour id `1` "Da Lat 3N2D", lịch trình id `1` của tour đó (10 chỗ, đã đặt 3). Mọi Output dưới đây là kết quả mong đợi.

#### 1. Chuẩn bị Environment và lưu token tự động

- **A.** Góc trên bên phải Postman, tạo Environment tên `quanlytour-local` gồm 3 biến: `baseUrl` = `http://localhost:8080`, `token` để trống, `adminToken` để trống. Chọn Environment này làm Environment đang dùng.
- **B.** Trong request đăng nhập (bước 3), mở tab **Scripts**, chọn **Post-response**, dán:

```javascript
const body = pm.response.json();
if (pm.response.code === 200) {
    pm.environment.set("token", body.accessToken);
}
```

- **C.** Với request đăng nhập bằng `admin`, dùng cùng script nhưng đổi `"token"` thành `"adminToken"`.
- **D.** Với mọi request cần đăng nhập: tab **Authorization**, mục **Auth Type** chọn **Bearer Token**, ô **Token** nhập `{{token}}` (hoặc `{{adminToken}}`). Postman tự thêm header `Authorization: Bearer <token>`.
- **E.** Giải thích: `pm.response.json()` đọc Body vừa nhận, `pm.environment.set(...)` ghi vào biến của Environment. Đăng nhập lại là token mới tự thay token cũ, không phải copy tay. Nếu chưa chọn Environment nào, lệnh `pm.environment.set` không có chỗ để ghi.

#### 2. Đăng ký

**Case 2.1: Đăng ký thành công.** `POST {{baseUrl}}/api/auth/register`, không cần Authorization, Body:

```json
{
    "username": "an",
    "password": "123456",
    "customerName": "Nguyen Van An",
    "phone": "0901000001",
    "email": "an@gmail.com"
}
```

Output mong đợi: `200 OK`

```json
{
    "id": 2,
    "username": "an",
    "role": "USER",
    "customerName": "Nguyen Van An",
    "phone": "0901000001",
    "email": "an@gmail.com"
}
```

- `id` là `2` vì `admin` đã chiếm `1`. Không có trường `password`.
- Kiểm tra database: bảng `khach_hang` có thêm một dòng, bảng `users` có dòng `an`, cột `password` bắt đầu bằng `$2a$10$`, cột `ma_khach_hang` trỏ đúng dòng khách hàng mới.

**Case 2.2: Trùng username.** Gửi lại đúng Body trên.

Output mong đợi: `400 Bad Request`, Body `{"message": "Ten dang nhap da ton tai", "status": 400}`.

**Case 2.3: Thiếu trường.** Body không có `password`:

```json
{
    "username": "binh",
    "customerName": "Tran Thi Binh",
    "email": "binh@gmail.com"
}
```

Output mong đợi: `400 Bad Request`, Body `{"message": "Mat khau khong duoc de trong", "status": 400}`.

#### 3. Đăng nhập

**Case 3.1: Đăng nhập đúng.** `POST {{baseUrl}}/api/auth/login`, Body `{"username": "an", "password": "123456"}`, có script ở bước 1.B.

Output mong đợi: `200 OK`, biến `token` được cập nhật:

```json
{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbiIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzkyOTgzNjAwLCJleHAiOjE3OTI5ODcyMDB9.ZYtXbgC8io75K3Lo3t9IduMtQ7u08k9Bs11SabQI1As",
    "tokenType": "Bearer",
    "expiresIn": 3600
}
```

**Case 3.2: Sai mật khẩu.** Body `{"username": "an", "password": "654321"}`.

Output mong đợi: `401 Unauthorized`, Body `{"message": "Sai ten dang nhap hoac mat khau", "status": 401}`. Biến `token` không bị ghi đè nhờ điều kiện `code === 200` trong script.

**Case 3.3: Đăng nhập ADMIN.** Body `{"username": "admin", "password": "Admin@123"}`, script lưu vào `adminToken`.

Output mong đợi: `200 OK`, payload của token có `"sub": "admin"`, `"role": "ADMIN"`.

#### 4. Gọi API có và không có token

| Case | Request                             | Authorization      | Output mong đợi                                                  |
| ---- | ----------------------------------- | ------------------ | ---------------------------------------------------------------- |
| 4.1  | `GET {{baseUrl}}/api/tours`         | No Auth            | `401`, `{"message": "Chua xac thuc", "status": 401}`             |
| 4.2  | `GET {{baseUrl}}/api/tours`         | Bearer `{{token}}` | `200`, trang dữ liệu tour như Buổi 8                             |
| 4.3  | `GET {{baseUrl}}/api/khong-ton-tai` | No Auth            | `401`: API không có trong ma trận vẫn bị khóa nhờ `anyRequest()` |

#### 5. API thông tin cá nhân và danh sách user

**Case 5.1: `/me` bằng token USER.** `GET {{baseUrl}}/api/users/me`, Bearer `{{token}}`, không Params, không Body.

Output mong đợi: `200 OK`

```json
{
    "id": 2,
    "username": "an",
    "role": "USER",
    "customerName": "Nguyen Van An",
    "phone": "0901000001",
    "email": "an@gmail.com"
}
```

**Case 5.2: `/me` bằng token ADMIN.** Cùng request, đổi sang Bearer `{{adminToken}}`.

Output mong đợi: `200 OK`, mỗi người chỉ thấy thông tin của chính mình:

```json
{
    "id": 1,
    "username": "admin",
    "role": "ADMIN",
    "customerName": null,
    "phone": null,
    "email": null
}
```

**Case 5.3: USER xem danh sách user.** `GET {{baseUrl}}/api/users`, Bearer `{{token}}`.

Output mong đợi: `403 Forbidden`, Body `{"message": "Khong co quyen truy cap", "status": 403}`.

**Case 5.4: ADMIN xem danh sách user.** Cùng request, Bearer `{{adminToken}}`.

Output mong đợi: `200 OK`, mảng gồm `admin` và `an` theo thứ tự `id`, không phần tử nào có `password`. Console chỉ có một câu `select ... from users u1_0 left join khach_hang ...` (Mục VI.5).

#### 6. Phân quyền API cũ

Mỗi dòng trong ma trận có ít nhất một case. `USER` là Bearer `{{token}}`, `ADMIN` là Bearer `{{adminToken}}`.

| Case | Request                                                                             | Token | Output mong đợi                |
| ---- | ----------------------------------------------------------------------------------- | ----- | ------------------------------ |
| 6.1  | `GET /api/tours/revenue`                                                            | USER  | `403`                          |
| 6.2  | `GET /api/tours/revenue`                                                            | ADMIN | `200`, doanh thu từng tour     |
| 6.3  | `GET /api/lich-trinh/tour/1/upcoming`                                               | USER  | `200`                          |
| 6.4  | `POST /api/tours`, Body `{"tourName": "Ha Long 2N1D", "destination": "Quang Ninh"}` | USER  | `403`                          |
| 6.5  | `POST /api/tours`, cùng Body                                                        | ADMIN | `200`, tour mới                |
| 6.6  | `DELETE /api/lich-trinh/1`                                                          | USER  | `403`, lịch trình không bị xóa |
| 6.7  | `GET /api/hoa-don/search?minTotal=1000000`                                          | USER  | `403`                          |
| 6.8  | `GET /api/hoa-don/search?minTotal=1000000`                                          | ADMIN | `200`                          |
| 6.9  | `GET /api/khach-hang/search?name=an`                                                | USER  | `403`                          |
| 6.10 | `GET /api/khach-hang/search?name=an`                                                | ADMIN | `200`                          |

Tất cả các case `403` có cùng Body `{"message": "Khong co quyen truy cap", "status": 403}`.

#### 7. Đặt vé theo user đăng nhập

**Case 7.1: USER đặt vé, không gửi `khachHangId`.** `POST {{baseUrl}}/api/hoa-don`, Bearer `{{token}}`, Body:

```json
{
    "lichTrinhId": 1,
    "guestCount": 2
}
```

Output mong đợi: `200 OK`, hóa đơn đứng tên chính user `an`:

```json
{
    "id": 2,
    "customerName": "Nguyen Van An",
    "tourName": "Da Lat 3N2D",
    "departureDate": "2026-12-10",
    "guestCount": 2,
    "ticketPrice": 2500000.0,
    "totalAmount": 5000000.0,
    "createdAt": "2026-10-26T10:15:30.512"
}
```

- `id`, `createdAt` phụ thuộc dữ liệu và thời điểm gọi. Cột `ma_khach_hang` của hóa đơn mới trỏ đúng khách hàng tạo ở Case 2.1.

**Case 7.2: ADMIN đặt vé.** Cùng request, Bearer `{{adminToken}}`.

Output mong đợi: `403 Forbidden`.

**Case 7.3: Cố tình gửi `khachHangId` của người khác.** Body `{"khachHangId": 1, "lichTrinhId": 1, "guestCount": 1}`, Bearer `{{token}}`.

Output mong đợi: `200 OK`, hóa đơn **vẫn** đứng tên `an`. Jackson bỏ qua trường lạ `khachHangId` vì `BookTicketRequest` không còn trường này.

#### 8. Token bị sửa và token hết hạn

**Case 8.1: Sửa 1 ký tự trong token.** Copy giá trị biến `token`, đổi một ký tự ở **giữa** phần chữ ký (đoạn sau dấu chấm thứ hai), dán vào ô Token thay cho `{{token}}`, gọi `GET {{baseUrl}}/api/users/me`.

- Không sửa ký tự **cuối cùng**: ký tự cuối của chuỗi Base64URL chứa vài bit đệm, đổi sang một số ký tự gần nó có thể không làm thay đổi chữ ký sau khi giải mã.

Output mong đợi: `401 Unauthorized`, Body `{"message": "Token khong hop le", "status": 401}`.

**Case 8.2: Token hết hạn.**

- **A.** Tạm sửa `application.properties`: `jwt.expiration-ms=10000` (10 giây). Stop rồi Run lại.
- **B.** Đăng nhập lại bằng `an` để lấy token mới (lúc này `expiresIn` là `10`).
- **C.** Đợi hơn 10 giây, gọi `GET {{baseUrl}}/api/users/me` bằng Bearer `{{token}}`.

Output mong đợi: `401 Unauthorized`, Body `{"message": "Token da het han", "status": 401}`.

- **D.** Trả `jwt.expiration-ms` về `3600000`, chạy lại ứng dụng.

### IX. Tiêu chí hoàn thành

- **A.** Ứng dụng khởi động thành công, Console có dòng `Da tao tai khoan ADMIN mac dinh: admin` ở lần chạy đầu và **không còn** dòng `Using generated security password`.
- **B.** Bảng `users` có đúng 5 cột `id`, `username`, `password`, `role`, `ma_khach_hang`; `username` và `ma_khach_hang` có ràng buộc `UNIQUE`; `ma_khach_hang` có khóa ngoại tới `khach_hang(id)`.
- **C.** Mọi giá trị cột `password` dài 60 ký tự và bắt đầu bằng `$2a$10$`. Dòng `admin` có `ma_khach_hang` là `NULL`, `role` là `ADMIN`.
- **D.** Đủ 4 mã trạng thái với đúng JSON `{"status": ..., "message": "..."}`:
  - `200`: Case 3.1 (có `accessToken`), Case 7.1.
  - `400`: Case 2.2, Case 2.3.
  - `401`: Case 3.2, Case 4.1, Case 8.1, Case 8.2, mỗi case đúng message riêng.
  - `403`: Case 5.3, Case 7.2.
- **E.** Không API nào trả về trường `password` hoặc Entity `User`.
- **F.** `GET /api/users/me` không nhận tham số nào; token USER và token ADMIN cho ra hai kết quả khác nhau.
- **G.** `GET /api/users` bằng token ADMIN chỉ sinh **một** câu SQL có `left join khach_hang`.
- **H.** Ảnh chụp màn hình làm minh chứng:
  - Postman: Case 3.1, 3.2, 4.1, 5.1, 5.2, 5.3, 7.1, 8.1, 8.2 (thấy rõ URL, tab Authorization, mã trạng thái, Body).
  - Tab Scripts của request đăng nhập và Environment có biến `token` đã được điền.
  - MySQL Workbench: kết quả `SELECT * FROM users`.
  - Console: câu SQL của Case 5.4.

### X. Hướng mở rộng

Chỉ liệt kê để tự tìm hiểu, không thuộc phạm vi bài tập:

- **Refresh Token:** Access Token sống rất ngắn (5 đến 15 phút), kèm một Refresh Token sống lâu hơn, lưu ở server, dùng để xin Access Token mới.
- **Logout bằng blacklist:** khi đăng xuất, lưu token (hoặc mã định danh `jti` của token) vào danh sách đen tới khi hết hạn, `JwtAuthenticationFilter` kiểm tra thêm danh sách này.
- **Đổi mật khẩu:** API `PUT /api/users/me/password`, kiểm tra mật khẩu cũ bằng `passwordEncoder.matches`.
- **Tour yêu thích theo `/me`:** đổi `POST /api/khach-hang/{khachHangId}/tour-yeu-thich/{tourId}` thành `POST /api/users/me/tour-yeu-thich/{tourId}`, lấy khách hàng từ token như cách làm với đặt vé, để mở API này cho USER mà không bị IDOR.
- **Liên kết khách hàng có sẵn:** khi đăng ký, nếu email đã tồn tại trong `khach_hang` (khách tạo tại quầy), gắn tài khoản vào khách hàng đó thay vì tạo bản ghi mới.
- **Các hướng khác:** OAuth2, đăng nhập Google, quên mật khẩu qua email, giao diện Frontend.
