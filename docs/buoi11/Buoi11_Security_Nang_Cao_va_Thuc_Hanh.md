# [BUỔI 11] SECURITY NÂNG CAO VÀ THỰC HÀNH TỔNG HỢP

## Phần 1: PHÂN QUYỀN THEO DỮ LIỆU (DATA-BASED AUTHORIZATION)

### I. Phân biệt Phân quyền theo Vai trò (RBAC) và Theo Dữ liệu

- **1. Câu hỏi cốt lõi mà mỗi mô hình trả lời:**
  - **A.** RBAC trả lời câu hỏi "Bạn là ai?" — hệ thống chỉ quan tâm tài khoản đang đăng nhập mang vai trò gì.
  - **B.** Data-based authorization trả lời câu hỏi "Bạn có quan hệ gì với bản ghi cụ thể này?" — hệ thống quan tâm tài khoản đó có sở hữu, có là thành viên, có được giao xử lý bản ghi hay không.
- **2. Đơn vị kiểm tra:**
  - **A.** RBAC kiểm tra trên danh từ chung: `ROLE_USER` được gọi `/api/hoa-don`, `ROLE_ADMIN` được gọi `/api/admin/**`.
  - **B.** Data-based kiểm tra trên từng instance: hóa đơn số 17, nhóm chat số 4, hợp đồng số 902.
- **3. Hệ quả khi chỉ có RBAC:**
  - **A.** Mọi người dùng đều mang `ROLE_USER`, nên mọi người dùng đều vượt qua lớp kiểm tra đầu tiên.
  - **B.** Khi client gửi `GET /api/hoa-don/17`, hệ thống trả về hóa đơn 17 cho bất kỳ ai — đây chính là lỗ hổng IDOR (Insecure Direct Object Reference).
  - **C.** Kẻ tấn công chỉ cần thay đổi số trên URL, không cần bẻ khóa gì cả.
- **4. Nguyên tắc thiết kế bắt buộc:**
  - **A.** RBAC là lớp ngoài cùng, chặn theo URL và HTTP method.
  - **B.** Data-based authorization là lớp trong cùng, chạy trên từng bản ghi trước khi dữ liệu rời khỏi server.
  - **C.** Bất kỳ endpoint nào nhận định danh bản ghi từ client (`{id}`, `{maDon}`, `{conversationId}`) đều phải đi qua lớp B.
- **5. Ví dụ mốc để so sánh:** TV1 và TV2 cùng mang `ROLE_USER`. Hóa đơn `HD-0001` thuộc về TV1. Với RBAC thuần, cả TV1 và TV2 đều đọc được `HD-0001`. Với data-based authorization, TV1 nhận 200 kèm dữ liệu, TV2 nhận 404 như thể hóa đơn không tồn tại.

### II. Các cách kiểm tra quyền trên dữ liệu

**1. Cách thứ nhất: dùng `@PreAuthorize` gọi Bean tùy chỉnh**

- **A.** Quyền được kiểm tra trước khi phương thức Controller được thực thi, dựa trên tham số truyền vào.
- **B.** Bean kiểm tra là một `@Component` đặt tên, được gọi từ biểu thức SpEL của `@PreAuthorize`.
- **C.** Ưu điểm: tách logic phân quyền khỏi logic nghiệp vụ, tái sử dụng được ở nhiều endpoint, đọc code rất rõ ý đồ.
- **D.** Nhược điểm: bản ghi được truy vấn hai lần (một lần để kiểm tra quyền, một lần để lấy dữ liệu trả về), và có nguy cơ lệch trạng thái nếu dữ liệu thay đổi giữa hai lần.

```java
package com.example.tour.security;

import com.example.tour.repository.HoaDonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("hoaDonAccess")
@RequiredArgsConstructor
public class HoaDonAccessService {

    private final HoaDonRepository hoaDonRepository;

    public boolean isOwner(Long hoaDonId, String username) {
        if (hoaDonId == null || username == null) {
            return false;
        }
        return hoaDonRepository.existsByIdAndKhachHangTaiKhoanUsername(hoaDonId, username);
    }
}
```

```java
@GetMapping("/{id}")
@PreAuthorize("hasRole('ADMIN') or @hoaDonAccess.isOwner(#id, authentication.name)")
public HoaDonResponse findById(@PathVariable Long id) {
    return hoaDonService.findById(id);
}
```

Bean `hoaDonAccess` nhận hai tham số: `#id` lấy từ biến đường dẫn, `authentication.name` lấy từ SecurityContext của request hiện tại. Câu truy vấn `existsByIdAndKhachHangTaiKhoanUsername` dịch thành phép đếm có điều kiện `hd.id = ? AND tk.username = ?` sau khi join ba bảng. Nếu trả về `false` và người gọi không mang quyền ADMIN, Spring Security ném `AccessDeniedException` trước khi thân phương thức kịp chạy, nên Service không bao giờ nhìn thấy request trái phép.

**2. Cách thứ hai: đưa quyền vào điều kiện `WHERE` của Spring Data JPA**

- **A.** Quyền sở hữu trở thành một phần của câu truy vấn, không phải một bước kiểm tra riêng.
- **B.** Bản ghi không thuộc về người gọi đơn giản là "không tìm thấy", nên trả 404 tự nhiên.
- **C.** Ưu điểm: chỉ một câu truy vấn, không có khoảng trống giữa lúc kiểm tra và lúc đọc dữ liệu.
- **D.** Nhược điểm: mỗi endpoint cần một phương thức Repository riêng; không diễn đạt được quy tắc phức tạp (ví dụ "được xem nếu là trưởng nhóm hoặc thành viên").

```java
package com.example.tour.repository;

import com.example.tour.entity.HoaDon;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {

    List<HoaDon> findByKhachHangTaiKhoanUsernameOrderByNgayTaoDesc(String username);

    Optional<HoaDon> findByIdAndKhachHangTaiKhoanUsername(Long id, String username);
}
```

Tên phương thức được Spring Data phân rã thành điều kiện `hd.khachHang.taiKhoan.username = ?`. Với TV2 gọi `GET /api/hoa-don/1`, câu truy vấn chạy với `id = 1` và `username = 'tv2'`, không có dòng nào khớp, `Optional` rỗng, Service ném `ResourceNotFoundException`, Controller trả 404. Hệ thống không phân biệt "hóa đơn không tồn tại" với "hóa đơn tồn tại nhưng không phải của bạn", nhờ vậy kẻ tấn công không dò được id nào là thật.

**3. Chọn cách nào:**

- **A.** Dùng `WHERE` cho các endpoint đọc/ghi trên bản ghi đơn giản thuộc sở hữu một người.
- **B.** Dùng `@PreAuthorize` khi quy tắc phức tạp, cần đọc nhiều bảng, hoặc cần chặn trước cả khi gọi Service.
- **C.** Kết hợp cả hai ở hệ thống lớn: `@PreAuthorize` chặn thô theo vai trò, `WHERE` siết chặt theo dữ liệu.

## Phần 2: LƯU MẬT KHẨU, MÃ HÓA VÀ GIẢI MÃ

### I. Phân biệt Hashing, Encryption và Encoding

- **1. Hashing (băm):**
  - **A.** Là hàm một chiều: đưa chuỗi vào, nhận chuỗi rút gọn có độ dài cố định, không có thao tác ngược.
  - **B.** Không cần khóa, nhưng cần salt để mỗi lần băm cho kết quả khác nhau.
  - **C.** Không thể dịch ngược. Muốn kiểm tra mật khẩu, hệ thống băm lại chuỗi người dùng nhập rồi so sánh hai giá trị băm.
  - **D.** Dùng cho: mật khẩu, mã PIN, refresh token, chữ ký dữ liệu.
- **2. Encryption (mã hóa):**
  - **A.** Là phép thuận nghịch: có khóa thì giải mã được về nguyên bản.
  - **B.** Bắt buộc có khóa, khóa mất thì dữ liệu mất vĩnh viễn.
  - **C.** Dùng cho: số điện thoại, số CCCD, số thẻ, thông tin cần hiển thị lại cho người dùng.
- **3. Encoding (mã hóa biểu diễn):**
  - **A.** Chỉ đổi cách biểu diễn dữ liệu để truyền hoặc lưu trữ an toàn về mặt ký tự, ví dụ Base64, Hex, URL-encode.
  - **B.** Không cần khóa, ai cũng giải mã được bằng thuật toán công khai.
  - **C.** Không phải biện pháp bảo mật. Base64 của mật khẩu vẫn là mật khẩu.
- **4. Hệ quả thực chiến:**
  - **A.** Mật khẩu đã băm thì không thể "gửi lại mật khẩu cũ" cho người dùng; chức năng quên mật khẩu là cấp lại, không phải khôi phục.
  - **B.** Không bao giờ ghi log giá trị mật khẩu, không trả trường `password` ra DTO.
  - **C.** Dữ liệu mã hóa bằng AES-GCM thì không tìm kiếm mờ được; cần tìm kiếm thì thêm cột băm xác định (HMAC) bên cạnh.

### II. Lưu mật khẩu đúng cách với BCrypt

**1. Cấu trúc một chuỗi BCrypt:**

```text
$2a$12$R9h/cIPz0gi.URNNX3kh2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUW
 |  |  |                     |
 |  |  +-- 22 ky tu salt -----+ 31 ky tu digest
 |  +----- cost factor (12)
 +-------- phien ban thuat toan
```

- **A.** Thuật toán, cost và salt đều nằm ngay trong chuỗi kết quả, nên không cần cột riêng để lưu salt.
- **B.** `$2a$`, `$2b$`, `$2y$` là các biến thể BCrypt; Spring dùng `$2a$` và đọc được cả ba.
- **C.** Độ dài chuỗi luôn là 60 ký tự, cộng thêm tiền tố `{bcrypt}` khi dùng `DelegatingPasswordEncoder` thành 68.

**2. Salt hoạt động ngầm ra sao:**

- **A.** Mỗi lần băm, thư viện sinh một salt ngẫu nhiên 16 byte và trộn vào quá trình tính.
- **B.** Cùng một mật khẩu `123456` của TV1 và TV2 cho ra hai chuỗi băm hoàn toàn khác nhau.
- **C.** Salt triệt tiêu rainbow table: kẻ tấn công không thể tra sẵn bảng băm cho từng mật khẩu phổ biến.
- **D.** Salt không bí mật, nó nằm công khai trong chuỗi băm; giá trị bảo mật của nó nằm ở tính duy nhất, không phải tính bí mật.

**3. Work factor hoạt động ngầm ra sao:**

- **A.** Cost factor `c` khiến thuật toán chạy khoảng `2^c` vòng lặp nội bộ. `c = 10` là khoảng 1024 vòng, `c = 12` là khoảng 4096 vòng.
- **B.** Tăng cost thêm 1 đơn vị làm thời gian băm gấp đôi. Đây là nút điều chỉnh duy nhất giữa "người dùng chờ lâu" và "kẻ tấn công tốn tiền".
- **C.** Cost được ghi vào chính chuỗi băm, nên có thể nâng cost mà không phá vỡ mật khẩu cũ: hệ thống đọc cost trong chuỗi để kiểm tra, và băm lại theo cost mới ở lần đăng nhập kế tiếp.
- **D.** Chọn cost theo phần cứng thực tế: đo thời gian một lần `matches`, mục tiêu 100–250 ms trên máy chủ đang chạy production. Với CPU phổ thông hiện nay, cost 12 là điểm khởi đầu hợp lý.
- **E.** BCrypt cắt mật khẩu ở 72 byte; mật khẩu dài hơn cần băm trước bằng SHA-256 rồi mới đưa vào BCrypt.

**4. Vì sao MD5/SHA-1/SHA-256 thuần không dùng cho mật khẩu:**

- **A.** Chúng được thiết kế để chạy nhanh, một GPU có thể thử hàng tỉ tổ hợp mỗi giây.
- **B.** Không có salt mặc định, cùng mật khẩu cho cùng kết quả.
- **C.** BCrypt/Argon2/PBKDF2 được thiết kế để chậm có kiểm soát, đó chính là mục tiêu bảo mật.

```java
package com.example.tour.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
```

`BCryptPasswordEncoder(12)` tạo encoder với cost 12. Lời gọi `encode("123456")` sinh salt ngẫu nhiên, chạy khoảng 4096 vòng và trả chuỗi 60 ký tự. Lời gọi `matches("123456", chuoiBam)` đọc salt và cost từ chính `chuoiBam`, chạy lại quá trình băm rồi so sánh. Không có bước giải mã nào xảy ra.

### III. Mã hóa đối xứng AES-GCM

**1. Cách hoạt động:**

- **A.** AES là mã hóa khối 128 bit, nhận khóa 128/192/256 bit. Cùng một khóa vừa mã hóa vừa giải mã, nên gọi là đối xứng.
- **B.** GCM là chế độ vận hành biến AES thành mã hóa có xác thực (AEAD): ngoài bản mã, nó sinh thêm một tag 16 byte để chống sửa đổi.
- **C.** Khi giải mã, thư viện tính lại tag từ bản mã và IV; nếu tag không khớp, nó ném `AEADBadTagException` thay vì trả dữ liệu rác. Nhờ vậy kẻ tấn công không thể lật một bit trong DB rồi chờ hệ thống xử lý sai.

**2. Vai trò của IV (Initialization Vector):**

- **A.** IV là 12 byte ngẫu nhiên, sinh mới cho mỗi lần mã hóa, không bí mật và được lưu kèm bản mã.
- **B.** IV bảo đảm cùng một số điện thoại mã hóa hai lần cho ra hai bản mã khác nhau, nên kẻ có quyền đọc DB không thấy được sự trùng lặp.
- **C.** Tuyệt đối không tái sử dụng IV với cùng một khóa. Tái sử dụng IV trong GCM làm lộ quan hệ XOR giữa hai bản rõ và phá hủy hoàn toàn tính xác thực.
- **D.** Không dùng IV cố định, không dùng bộ đếm dùng chung giữa các tiến trình mà không đồng bộ.

**3. Bố cục dữ liệu lưu xuống DB:**

- **A.** Ghép theo thứ tự `IV (12 byte) || bản mã || tag (16 byte)`.
- **B.** Mã hóa Base64 toàn bộ mảng byte để lưu vào cột `VARCHAR`.
- **C.** Khi đọc lên: Base64-decode, tách 12 byte đầu làm IV, phần còn lại là bản mã kèm tag, đưa vào `Cipher` ở chế độ giải mã.
- **D.** Với số điện thoại 10 ký tự: 12 + 10 + 16 = 38 byte, Base64 thành 52 ký tự. Cột `VARCHAR(255)` là đủ dư.

**4. Giới hạn cần biết trước khi dùng:**

- **A.** Không truy vấn được `LIKE`, không `ORDER BY` theo nội dung đã mã hóa, không so sánh bằng trực tiếp.
- **B.** Cần tìm chính xác theo số điện thoại thì thêm cột `so_dien_thoai_hash` chứa HMAC-SHA256 với khóa phụ, đánh index lên cột đó.
- **C.** Khóa AES phải nằm ngoài mã nguồn và ngoài DB: biến môi trường, secret manager hoặc KMS. Xoay khóa cần thiết kế phiên bản khóa trong bản mã.

```java
package com.example.tour.security.crypto;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AesGcmCipher {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH_BIT = 128;

    private final SecretKey secretKey;
    private final SecureRandom secureRandom = new SecureRandom();

    public AesGcmCipher(@Value("${app.security.aes-key}") String base64Key) {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        this.secretKey = new SecretKeySpec(keyBytes, "AES");
    }

    public String encrypt(String plainText) {
        try {
            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            ByteBuffer buffer = ByteBuffer.allocate(iv.length + encrypted.length);
            buffer.put(iv);
            buffer.put(encrypted);
            return Base64.getEncoder().encodeToString(buffer.array());
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Không thể mã hóa dữ liệu", exception);
        }
    }

    public String decrypt(String base64Payload) {
        try {
            byte[] payload = Base64.getDecoder().decode(base64Payload);
            ByteBuffer buffer = ByteBuffer.wrap(payload);
            byte[] iv = new byte[IV_LENGTH];
            buffer.get(iv);
            byte[] encrypted = new byte[buffer.remaining()];
            buffer.get(encrypted);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
            return new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Không thể giải mã dữ liệu", exception);
        }
    }
}
```

Hàm dựng nhận khóa Base64 từ cấu hình và tạo `SecretKeySpec`. Phương thức `encrypt` sinh IV mới, khởi tạo `Cipher` ở chế độ mã hóa với `GCMParameterSpec` chỉ định tag 128 bit, lấy đầu ra `doFinal` (đã bao gồm tag ở cuối), rồi ghi IV lên trước bản mã trong một `ByteBuffer` và Base64 toàn bộ. Phương thức `decrypt` làm ngược lại: giải Base64, đọc đúng 12 byte đầu làm IV, phần còn lại đưa vào `Cipher` chế độ giải mã; nếu tag sai, `doFinal` ném ngoại lệ và dữ liệu không bao giờ được trả ra.

## Phần 3: CÁC KIỂU TẤN CÔNG PHỔ BIẾN & PHÒNG CHỐNG (OWASP TOP 10)

### I. SQL Injection (Dùng Bind Tham số)

- **1. Bản chất:** dữ liệu do người dùng nhập bị ghép thẳng vào câu lệnh, biến dữ liệu thành cú pháp. Chuỗi `' OR '1'='1` không còn là tên tour mà trở thành điều kiện luôn đúng.
- **2. Vì sao Spring Data JPA an toàn mặc định:**
  - **A.** Truy vấn dẫn xuất theo tên phương thức luôn sinh `PreparedStatement` với dấu `?`.
  - **B.** `@Query` dùng tham số đặt tên `:keyword` được bind qua `setParameter`, giá trị không bao giờ đi vào phần cú pháp.
  - **C.** Giá trị tham số được truyền xuống driver ở kênh riêng, không qua bộ phân tích cú pháp SQL.
- **3. Nơi JPA không cứu được:**
  - **A.** Ghép chuỗi bằng tay trong `createNativeQuery`.
  - **B.** Tên cột, tên bảng, `ORDER BY`, `LIMIT` — các thành phần cú pháp không bind được. Phải kiểm tra theo danh sách trắng.
  - **C.** Gọi stored procedure nhận chuỗi SQL động.

```java
package com.example.tour.repository;

import com.example.tour.entity.Tour;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TourRepository extends JpaRepository<Tour, Long> {

    List<Tour> findByTenTourContainingIgnoreCase(String keyword);

    @Query("select t from Tour t where lower(t.tenTour) like lower(concat('%', :keyword, '%'))")
    List<Tour> searchByKeyword(@Param("keyword") String keyword);
}
```

```java
package com.example.tour.service;

import com.example.tour.entity.Tour;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TourSearchService {

    private final EntityManager entityManager;

    public List<Tour> searchUnsafe(String keyword) {
        String sql = "select * from tour where ten_tour like '%" + keyword + "%'";
        return entityManager.createNativeQuery(sql, Tour.class).getResultList();
    }

    public List<Tour> searchSafe(String keyword) {
        return entityManager.createNativeQuery("select * from tour where ten_tour like :keyword", Tour.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();
    }
}
```

Phương thức `searchUnsafe` minh họa chính lỗ hổng: nếu `keyword` là `' union select * from tai_khoan --`, câu lệnh sau khi ghép sẽ trả về bảng tài khoản. Phương thức `searchSafe` giữ nguyên cấu trúc câu lệnh, chỉ thay giá trị qua `setParameter`, nên ký tự đặc biệt trong `keyword` bị driver coi là dữ liệu thuần và được escape đúng chuẩn. Truy vấn dẫn xuất `findByTenTourContainingIgnoreCase` và `@Query` có tham số đặt tên đều an toàn theo cùng cơ chế. Trong toàn bộ dự án thực hành bên dưới, không có một câu lệnh SQL nào được ghép chuỗi.

**4. Chống tấn công thứ cấp:** `ORDER BY` nhận từ client phải đối chiếu với tập cột cho phép trước khi đưa vào truy vấn, tuyệt đối không nối chuỗi trực tiếp.

### II. Broken Access Control & IDOR

- **1. Bản chất:** người dùng đã xác thực thành công nhưng thực hiện được hành động vượt quá quyền của mình, thường bằng cách sửa id trên URL hoặc trong body.
- **2. Ba lỗi thường gặp:**
  - **A.** Chỉ kiểm tra đăng nhập mà không kiểm tra quyền sở hữu bản ghi.
  - **B.** Tin tưởng id do client gửi lên thay vì lấy chủ thể từ SecurityContext.
  - **C.** Trả 403 cho bản ghi tồn tại và 404 cho bản ghi không tồn tại, giúp kẻ tấn công liệt kê được id hợp lệ.
- **3. Cách Spring Boot phòng chống:**
  - **A.** Cấu hình đóng theo mặc định: `anyRequest().authenticated()`, mọi đường dẫn mở phải được khai báo tường minh.
  - **B.** `@EnableMethodSecurity` kết hợp `@PreAuthorize` để chặn ở tầng phương thức.
  - **C.** Đưa điều kiện sở hữu vào câu truy vấn để bản ghi không thuộc quyền trở thành "không tồn tại".
  - **D.** Trả 404 thay vì 403 cho tài nguyên riêng tư, không tiết lộ sự tồn tại của bản ghi.

```java
package com.example.tour.service;

import com.example.tour.dto.HoaDonResponse;
import com.example.tour.entity.HoaDon;
import com.example.tour.exception.ResourceNotFoundException;
import com.example.tour.repository.HoaDonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HoaDonReadService {

    private final HoaDonRepository hoaDonRepository;

    public HoaDonResponse findUnsafe(Long id) {
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn"));
        return HoaDonResponse.from(hoaDon);
    }

    public HoaDonResponse findSafe(Long id, String username) {
        HoaDon hoaDon = hoaDonRepository.findByIdAndKhachHangTaiKhoanUsername(id, username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn"));
        return HoaDonResponse.from(hoaDon);
    }
}
```

`findUnsafe` lấy chủ thể từ tham số `id` do client kiểm soát và không đối chiếu người gọi, nên TV2 đọc được hóa đơn của TV1. `findSafe` biến `username` lấy từ thông tin đăng nhập thành một phần của mệnh đề `WHERE`, nên TV2 chỉ có thể nhận về hóa đơn của chính mình hoặc nhận 404.

### III. XSS (Tầm quan trọng của CSP)

- **1. Bản chất:** nội dung do kẻ tấn công kiểm soát được trình duyệt hiểu là mã và thực thi, chạy trong origin của ứng dụng và có quyền đọc cookie, gọi API thay người dùng.
- **2. Ba dạng:**
  - **A.** Stored XSS: payload nằm trong DB, phát tán tới mọi người xem trang (bình luận, tên tour, tin nhắn chat).
  - **B.** Reflected XSS: payload nằm trong tham số URL và được in lại ngay trong response.
  - **C.** DOM XSS: JavaScript phía client nhận dữ liệu không tin cậy rồi gán vào `innerHTML`.
- **3. Phòng chống ở tầng render:**
  - **A.** Thymeleaf escape mặc định khi dùng `th:text`; `th:utext` tắt escape và là nguồn lỗi phổ biến nhất.
  - **B.** Với API JSON, việc escape thuộc về client; không trả về HTML dựng sẵn từ dữ liệu người dùng.
  - **C.** Nếu buộc phải nhận HTML từ người dùng, lọc bằng OWASP Java HTML Sanitizer với danh sách thẻ cho phép.
- **4. Content Security Policy là lớp phòng thủ cuối:**
  - **A.** CSP là header trình duyệt dùng để quyết định nguồn nào được phép tải script, style, ảnh, frame.
  - **B.** Một CSP không có `unsafe-inline` vô hiệu hóa phần lớn payload XSS vì trình duyệt từ chối chạy script nội tuyến.
  - **C.** `frame-ancestors 'none'` chặn clickjacking; `form-action 'self'` chặn form gửi dữ liệu ra domain lạ; `base-uri 'self'` chặn đổi base URL để chiếm đường dẫn tương đối.
  - **D.** Bật CSP sẽ làm hỏng các trang có script hoặc style viết trực tiếp trong HTML; cần chuyển sang file tĩnh hoặc dùng nonce.

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .headers(headers -> headers
                    .contentSecurityPolicy(csp -> csp.policyDirectives(
                            "default-src 'self'; script-src 'self'; style-src 'self'; "
                                    + "img-src 'self' data:; connect-src 'self'; object-src 'none'; "
                                    + "frame-ancestors 'none'; base-uri 'self'; form-action 'self'")))
            .frameOptions(frame -> frame.deny());
    return http.build();
}
```

`contentSecurityPolicy` gắn header `Content-Security-Policy` vào mọi response. Trình duyệt đọc `default-src 'self'` và từ chối mọi tài nguyên từ domain khác; `script-src 'self'` không có `'unsafe-inline'` khiến thẻ `script` chèn vào nội dung bị chặn thực thi. `frameOptions().deny()` bổ sung header cũ `X-Frame-Options: DENY` cho các trình duyệt chưa hỗ trợ `frame-ancestors`.

### IV. Brute-force & Dò mật khẩu

- **1. Bản chất:** kẻ tấn công thử liên tục tổ hợp mật khẩu vào một hoặc nhiều tài khoản. Vì BCrypt làm mỗi lần thử tốn hàng chục đến hàng trăm mili giây phía server, đòn tấn công còn là đòn làm cạn CPU.
- **2. Credential stuffing:** dùng danh sách tài khoản/mật khẩu rò rỉ từ hệ thống khác để thử hàng loạt. Vì người dùng hay dùng lại mật khẩu, tỉ lệ thành công cao dù mật khẩu đã được băm đúng cách.
- **3. Các lớp phòng chống:**
  - **A.** Đếm số lần sai theo tài khoản và tạm khóa có thời hạn, không khóa vĩnh viễn.
  - **B.** Đếm cả theo địa chỉ IP để chặn dò rải rác nhiều tài khoản.
  - **C.** Trả thông điệp lỗi giống nhau cho "sai mật khẩu" và "không tồn tại tài khoản" để tránh lộ danh sách tài khoản.
  - **D.** Ghi log và cảnh báo khi một tài khoản hoặc một IP có tần suất thất bại bất thường.
  - **E.** Bật MFA cho tài khoản quản trị; MFA vô hiệu hóa giá trị của mật khẩu bị lộ.
- **4. Cách Spring Security hỗ trợ:**
  - **A.** `AuthenticationFailureBadCredentialsEvent` và `AuthenticationSuccessEvent` được `ProviderManager` phát ra, dùng để đếm mà không cần sửa filter.
  - **B.** `User.accountNonLocked(false)` khiến `DaoAuthenticationProvider` ném `LockedException` ở bước kiểm tra trước khi so mật khẩu.
  - **C.** Bộ đếm trong bộ nhớ chỉ đúng cho một instance; hệ thống nhiều node cần Redis hoặc một bảng có TTL.

## Phần 4: THỰC HÀNH TỔNG HỢP (DỰ ÁN QUẢN LÝ TOUR)

Toàn bộ mã nguồn dưới đây tạo thành một ứng dụng Spring Boot chạy được ngay: một cổng 8080, một database MySQL, xác thực HTTP Basic để dễ kiểm thử bằng Postman. Cơ chế phân quyền, mã hóa, chống dò mật khẩu và xử lý lỗi hoàn toàn độc lập với cách xác thực, nên khi chuyển sang JWT chỉ cần thay bộ lọc, không phải sửa các lớp bên dưới.

Dữ liệu mẫu cố định dùng trong toàn bộ phần thực hành: tài khoản `admin` (vai trò ADMIN), tài khoản `tv1` (vai trò USER, sở hữu hóa đơn `HD-0001` và là thành viên nhóm chat của tour), tài khoản `tv2` (vai trò USER, không sở hữu hóa đơn nào và không thuộc nhóm chat). Mật khẩu của `admin` là `admin123`, của `tv1` và `tv2` là `123456`.

**1. Cấu trúc package:**

- **A.** `com.example.tour.entity` — các lớp JPA.
- **B.** `com.example.tour.repository` — Spring Data JPA.
- **C.** `com.example.tour.service` — nghiệp vụ.
- **D.** `com.example.tour.controller` — REST endpoint.
- **E.** `com.example.tour.dto` — request/response.
- **F.** `com.example.tour.security` — cấu hình bảo mật, UserDetailsService, Bean phân quyền.
- **G.** `com.example.tour.security.crypto` — AES-GCM và converter.
- **H.** `com.example.tour.exception` — ngoại lệ nghiệp vụ và xử lý tập trung.
- **I.** `com.example.tour.config` — cấu hình chung và seed dữ liệu.

**2. Phụ thuộc bắt buộc trong `pom.xml`:**

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

**3. Cấu hình `src/main/resources/application.yml`:**

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tour_security?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Ho_Chi_Minh
    username: root
    password: 123456
  jpa:
    hibernate:
      ddl-auto: update
    open-in-view: false
    show-sql: true
    properties:
      hibernate:
        format_sql: true

app:
  security:
    aes-key: ${AES_KEY}
```

`ddl-auto: update` để Hibernate tự tạo và cập nhật bảng từ Entity, không cần chạy script SQL nào. `open-in-view: false` buộc mọi truy cập lazy nằm trong transaction của Service, tránh lỗi `LazyInitializationException` ngẫu nhiên trên production. Khóa AES đọc từ biến môi trường `AES_KEY`, khai báo trong Run Configuration của IDE hoặc trong biến môi trường của hệ điều hành trước khi chạy ứng dụng.

**4. Sinh khóa AES 256 bit:**

```java
package com.example.tour.security.crypto;

import java.security.SecureRandom;
import java.util.Base64;

public class AesKeyGenerator {

    public static void main(String[] args) {
        byte[] key = new byte[32];
        new SecureRandom().nextBytes(key);
        System.out.println(Base64.getEncoder().encodeToString(key));
    }
}
```

Chạy lớp này một lần, gán giá trị in ra vào biến môi trường `AES_KEY` của máy chạy ứng dụng. Khóa không được commit vào Git và không được ghi vào file cấu hình chia sẻ.

### I. Phân quyền API & Xem hóa đơn của chính mình (Chống IDOR)

**1. Nội dung tính năng:**

- **A.** `GET /api/hoa-don/me` trả về danh sách hóa đơn của chính người đang đăng nhập.
- **B.** `GET /api/hoa-don/{id}` chỉ trả về khi người đang đăng nhập sở hữu hóa đơn đó; trường hợp còn lại trả 404.
- **C.** ADMIN xem được mọi hóa đơn qua cùng endpoint `GET /api/hoa-don/{id}`.
- **D.** Danh tính người gọi luôn lấy từ `Authentication` do Spring Security dựng, không bao giờ lấy từ tham số client.

**2. Triển khai Code:**

Tạo `com.example.tour.entity.VaiTro`:

```java
package com.example.tour.entity;

public enum VaiTro {
    ADMIN,
    USER
}
```

Tạo `com.example.tour.entity.TaiKhoan`:

```java
package com.example.tour.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tai_khoan")
@Getter
@Setter
@NoArgsConstructor
public class TaiKhoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 120)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VaiTro vaiTro;

    @Column(nullable = false)
    private boolean enabled = true;
}
```

Tạo `com.example.tour.entity.KhachHang`:

```java
package com.example.tour.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "khach_hang")
@Getter
@Setter
@NoArgsConstructor
public class KhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String hoTen;

    @Column(name = "so_dien_thoai", length = 20)
    private String soDienThoai;

    @Column(length = 100)
    private String email;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tai_khoan_id", unique = true)
    private TaiKhoan taiKhoan;
}
```

Tạo `com.example.tour.entity.Tour`:

```java
package com.example.tour.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tour")
@Getter
@Setter
@NoArgsConstructor
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String tenTour;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal gia;

    @Column(nullable = false)
    private int soCho;

    @Column(nullable = false)
    private LocalDate ngayKhoiHanh;
}
```

Tạo `com.example.tour.entity.DatTour`:

```java
package com.example.tour.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dat_tour")
@Getter
@Setter
@NoArgsConstructor
public class DatTour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "khach_hang_id")
    private KhachHang khachHang;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @Column(nullable = false)
    private int soLuong;

    @Column(nullable = false)
    private LocalDateTime ngayDat;
}
```

Tạo `com.example.tour.entity.HoaDon`:

```java
package com.example.tour.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hoa_don")
@Getter
@Setter
@NoArgsConstructor
public class HoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String maHoaDon;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dat_tour_id", unique = true)
    private DatTour datTour;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "khach_hang_id")
    private KhachHang khachHang;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal tongTien;

    @Column(nullable = false)
    private LocalDateTime ngayTao;
}
```

Tạo các Repository trong package `com.example.tour.repository`:

```java
package com.example.tour.repository;

import com.example.tour.entity.TaiKhoan;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Long> {

    Optional<TaiKhoan> findByUsername(String username);

    boolean existsByUsername(String username);
}
```

```java
package com.example.tour.repository;

import com.example.tour.entity.KhachHang;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KhachHangRepository extends JpaRepository<KhachHang, Long> {

    Optional<KhachHang> findByTaiKhoanUsername(String username);
}
```

```java
package com.example.tour.repository;

import com.example.tour.entity.HoaDon;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {

    List<HoaDon> findByKhachHangTaiKhoanUsernameOrderByNgayTaoDesc(String username);

    Optional<HoaDon> findByIdAndKhachHangTaiKhoanUsername(Long id, String username);

    boolean existsByIdAndKhachHangTaiKhoanUsername(Long id, String username);
}
```

```java
package com.example.tour.repository;

import com.example.tour.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<Tour, Long> {
}
```

```java
package com.example.tour.repository;

import com.example.tour.entity.DatTour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatTourRepository extends JpaRepository<DatTour, Long> {
}
```

Tạo `com.example.tour.exception.ResourceNotFoundException`:

```java
package com.example.tour.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

Tạo `com.example.tour.dto.HoaDonResponse`:

```java
package com.example.tour.dto;

import com.example.tour.entity.HoaDon;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record HoaDonResponse(
        Long id,
        String maHoaDon,
        String tenTour,
        BigDecimal tongTien,
        LocalDateTime ngayTao) {

    public static HoaDonResponse from(HoaDon hoaDon) {
        return new HoaDonResponse(
                hoaDon.getId(),
                hoaDon.getMaHoaDon(),
                hoaDon.getDatTour().getTour().getTenTour(),
                hoaDon.getTongTien(),
                hoaDon.getNgayTao());
    }
}
```

Tạo `com.example.tour.service.HoaDonService`:

```java
package com.example.tour.service;

import com.example.tour.dto.HoaDonResponse;
import com.example.tour.entity.HoaDon;
import com.example.tour.exception.ResourceNotFoundException;
import com.example.tour.repository.HoaDonRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HoaDonService {

    private final HoaDonRepository hoaDonRepository;

    @Transactional(readOnly = true)
    public List<HoaDonResponse> findMyInvoices(String username) {
        return hoaDonRepository.findByKhachHangTaiKhoanUsernameOrderByNgayTaoDesc(username)
                .stream()
                .map(HoaDonResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public HoaDonResponse findByIdForUser(Long id, String username, boolean admin) {
        HoaDon hoaDon = admin
                ? hoaDonRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn"))
                : hoaDonRepository.findByIdAndKhachHangTaiKhoanUsername(id, username)
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn"));
        return HoaDonResponse.from(hoaDon);
    }
}
```

Tạo `com.example.tour.controller.HoaDonController`:

```java
package com.example.tour.controller;

import com.example.tour.dto.HoaDonResponse;
import com.example.tour.service.HoaDonService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hoa-don")
@RequiredArgsConstructor
public class HoaDonController {

    private final HoaDonService hoaDonService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<HoaDonResponse> findMyInvoices(Authentication authentication) {
        return hoaDonService.findMyInvoices(authentication.getName());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public HoaDonResponse findById(@PathVariable Long id, Authentication authentication) {
        boolean admin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        return hoaDonService.findByIdForUser(id, authentication.getName(), admin);
    }
}
```

Tạo `com.example.tour.security.CustomUserDetailsService`:

```java
package com.example.tour.security;

import com.example.tour.entity.TaiKhoan;
import com.example.tour.repository.TaiKhoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final TaiKhoanRepository taiKhoanRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tồn tại tài khoản " + username));
        return User.withUsername(taiKhoan.getUsername())
                .password(taiKhoan.getPassword())
                .roles(taiKhoan.getVaiTro().name())
                .disabled(!taiKhoan.isEnabled())
                .build();
    }
}
```

Tạo `com.example.tour.config.PasswordEncoderConfig`:

```java
package com.example.tour.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
```

Tạo `com.example.tour.config.SecurityConfig`:

```java
package com.example.tour.config;

import com.example.tour.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated())
                .userDetailsService(customUserDetailsService)
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
```

Tạo `com.example.tour.config.DataInitializer`:

```java
package com.example.tour.config;

import com.example.tour.entity.DatTour;
import com.example.tour.entity.HoaDon;
import com.example.tour.entity.KhachHang;
import com.example.tour.entity.TaiKhoan;
import com.example.tour.entity.Tour;
import com.example.tour.entity.VaiTro;
import com.example.tour.repository.DatTourRepository;
import com.example.tour.repository.HoaDonRepository;
import com.example.tour.repository.KhachHangRepository;
import com.example.tour.repository.TaiKhoanRepository;
import com.example.tour.repository.TourRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Order(1)
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TaiKhoanRepository taiKhoanRepository;
    private final KhachHangRepository khachHangRepository;
    private final TourRepository tourRepository;
    private final DatTourRepository datTourRepository;
    private final HoaDonRepository hoaDonRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (taiKhoanRepository.count() > 0) {
            return;
        }

        TaiKhoan admin = createAccount("admin", passwordEncoder.encode("admin123"), VaiTro.ADMIN);
        TaiKhoan tv1 = createAccount("tv1", passwordEncoder.encode("123456"), VaiTro.USER);
        createAccount("tv2", "{noop}123456", VaiTro.USER);

        KhachHang khachHang1 = createCustomer("Khách hàng TV1", "0901234567", "tv1@example.com", tv1);
        createCustomer("Khách hàng TV2", "0907654321", "tv2@example.com", null);

        Tour tour = new Tour();
        tour.setTenTour("Đà Lạt 3 ngày 2 đêm");
        tour.setGia(new BigDecimal("2500000"));
        tour.setSoCho(20);
        tour.setNgayKhoiHanh(LocalDate.now().plusDays(20));
        tourRepository.save(tour);

        DatTour datTour = new DatTour();
        datTour.setKhachHang(khachHang1);
        datTour.setTour(tour);
        datTour.setSoLuong(2);
        datTour.setNgayDat(LocalDateTime.now());
        datTourRepository.save(datTour);

        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHoaDon("HD-0001");
        hoaDon.setDatTour(datTour);
        hoaDon.setKhachHang(khachHang1);
        hoaDon.setTongTien(new BigDecimal("5000000"));
        hoaDon.setNgayTao(LocalDateTime.now());
        hoaDonRepository.save(hoaDon);
    }

    private TaiKhoan createAccount(String username, String encodedPassword, VaiTro vaiTro) {
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setUsername(username);
        taiKhoan.setPassword(encodedPassword);
        taiKhoan.setVaiTro(vaiTro);
        taiKhoan.setEnabled(true);
        return taiKhoanRepository.save(taiKhoan);
    }

    private KhachHang createCustomer(String hoTen, String soDienThoai, String email, TaiKhoan taiKhoan) {
        KhachHang khachHang = new KhachHang();
        khachHang.setHoTen(hoTen);
        khachHang.setSoDienThoai(soDienThoai);
        khachHang.setEmail(email);
        khachHang.setTaiKhoan(taiKhoan);
        return khachHangRepository.save(khachHang);
    }
}
```

Luồng chạy: khi ứng dụng khởi động, `DataInitializer` được gọi sau khi Hibernate tạo bảng. Nếu bảng `tai_khoan` rỗng, nó tạo ba tài khoản, hai khách hàng, một tour, một lượt đặt và một hóa đơn. Mật khẩu `admin` và `tv1` được băm bằng encoder đang cấu hình nên DB lưu dạng `{bcrypt}$2a$10$...`. Tài khoản `tv2` được gán thẳng chuỗi `{noop}123456`, mô phỏng một bản ghi mật khẩu cũ chưa được băm; encoder dạng delegating vẫn so khớp được định dạng này nên `tv2` đăng nhập bình thường. Trường `soDienThoai` lúc này còn lưu dạng thô.

Khi có request `GET /api/hoa-don/1` với Basic auth của `tv2`, chuỗi xử lý diễn ra: filter Basic giải mã header, `CustomUserDetailsService` nạp `tv2`, `DaoAuthenticationProvider` so khớp mật khẩu, SecurityContext được gắn vào request. `HoaDonController.findById` lấy tên `tv2` từ `Authentication`, Service gọi `findByIdAndKhachHangTaiKhoanUsername(1, "tv2")`, câu truy vấn không trả về dòng nào, `ResourceNotFoundException` được ném và ánh xạ thành HTTP 404.

**3. Demo trực quan (Postman, thẻ Authorization chọn Basic Auth, user `tv1`, password `123456`):**

Yêu cầu:

```http
GET http://localhost:8080/api/hoa-don/me
Authorization: Basic tv1:123456
```

Phản hồi `200 OK`:

```json
[
  {
    "id": 1,
    "maHoaDon": "HD-0001",
    "tenTour": "Đà Lạt 3 ngày 2 đêm",
    "tongTien": 5000000.00,
    "ngayTao": "2026-11-06T09:15:30.482991"
  }
]
```

Đổi sang user `tv2`, password `123456`, gọi `GET /api/hoa-don/1`:

```json
{
  "timestamp": "2026-11-06T09:20:11.482+00:00",
  "status": 404,
  "error": "Not Found",
  "path": "/api/hoa-don/1"
}
```

Đổi sang user `admin`, password `admin123`, gọi `GET /api/hoa-don/1`:

```json
{
  "id": 1,
  "maHoaDon": "HD-0001",
  "tenTour": "Đà Lạt 3 ngày 2 đêm",
  "tongTien": 5000000.00,
  "ngayTao": "2026-11-06T09:15:30.482991"
}
```

Thân lỗi ở trường hợp 404 lúc này do Spring MVC sinh mặc định; mục VI thay thế bằng cấu trúc JSON thống nhất của dự án.

### II. Lịch sử hội thoại nhóm khách đi Tour

**1. Nội dung tính năng:**

- **A.** Mỗi tour có một nhóm hội thoại; chỉ khách hàng đã đặt tour đó mới là thành viên nhóm.
- **B.** `GET /api/hoi-thoai/{conversationId}/tin-nhan` trả lịch sử tin nhắn theo thứ tự thời gian.
- **C.** `POST /api/hoi-thoai/{conversationId}/tin-nhan` gửi tin nhắn mới, người gửi lấy từ SecurityContext.
- **D.** Người không thuộc nhóm bị chặn ở tầng phương thức trước khi Service được gọi và nhận 403, nên TV2 không đọc được tin nhắn của TV1.

**2. Triển khai Code:**

Tạo `com.example.tour.entity.Conversation`:

```java
package com.example.tour.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "conversation")
@Getter
@Setter
@NoArgsConstructor
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @Column(nullable = false, length = 150)
    private String tenNhom;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
```

Tạo `com.example.tour.entity.ConversationMember`:

```java
package com.example.tour.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "conversation_member",
        uniqueConstraints = @UniqueConstraint(columnNames = {"conversation_id", "khach_hang_id"}))
@Getter
@Setter
@NoArgsConstructor
public class ConversationMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "khach_hang_id")
    private KhachHang khachHang;

    @Column(nullable = false)
    private LocalDateTime joinedAt;
}
```

Tạo `com.example.tour.entity.Message`:

```java
package com.example.tour.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "message")
@Getter
@Setter
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id")
    private KhachHang sender;

    @Column(nullable = false, length = 2000)
    private String noiDung;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
```

Tạo các Repository còn thiếu trong package `com.example.tour.repository`:

```java
package com.example.tour.repository;

import com.example.tour.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}
```

```java
package com.example.tour.repository;

import com.example.tour.entity.ConversationMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationMemberRepository extends JpaRepository<ConversationMember, Long> {

    boolean existsByConversationIdAndKhachHangTaiKhoanUsername(Long conversationId, String username);
}
```

```java
package com.example.tour.repository;

import com.example.tour.entity.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByConversationIdOrderByCreatedAtAsc(Long conversationId);
}
```

Tạo `com.example.tour.dto.MessageRequest`:

```java
package com.example.tour.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MessageRequest(
        @NotBlank(message = "Nội dung tin nhắn không được để trống")
        @Size(max = 2000, message = "Nội dung tin nhắn tối đa 2000 ký tự")
        String noiDung) {
}
```

Tạo `com.example.tour.dto.MessageResponse`:

```java
package com.example.tour.dto;

import com.example.tour.entity.Message;
import java.time.LocalDateTime;

public record MessageResponse(
        Long id,
        String nguoiGui,
        String noiDung,
        LocalDateTime thoiGian) {

    public static MessageResponse from(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getSender().getHoTen(),
                message.getNoiDung(),
                message.getCreatedAt());
    }
}
```

Tạo `com.example.tour.security.ConversationAccessService`:

```java
package com.example.tour.security;

import com.example.tour.repository.ConversationMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("conversationAccess")
@RequiredArgsConstructor
public class ConversationAccessService {

    private final ConversationMemberRepository conversationMemberRepository;

    public boolean isMember(Long conversationId, String username) {
        if (conversationId == null || username == null) {
            return false;
        }
        return conversationMemberRepository
                .existsByConversationIdAndKhachHangTaiKhoanUsername(conversationId, username);
    }
}
```

Tạo `com.example.tour.service.MessageService`:

```java
package com.example.tour.service;

import com.example.tour.dto.MessageRequest;
import com.example.tour.dto.MessageResponse;
import com.example.tour.entity.Conversation;
import com.example.tour.entity.KhachHang;
import com.example.tour.entity.Message;
import com.example.tour.exception.ResourceNotFoundException;
import com.example.tour.repository.ConversationRepository;
import com.example.tour.repository.KhachHangRepository;
import com.example.tour.repository.MessageRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final KhachHangRepository khachHangRepository;

    @Transactional(readOnly = true)
    public List<MessageResponse> findMessages(Long conversationId) {
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId)
                .stream()
                .map(MessageResponse::from)
                .toList();
    }

    @Transactional
    public MessageResponse sendMessage(Long conversationId, String username, MessageRequest request) {
        KhachHang sender = khachHangRepository.findByTaiKhoanUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hồ sơ khách hàng"));
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhóm hội thoại"));
        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setNoiDung(request.noiDung());
        message.setCreatedAt(LocalDateTime.now());
        return MessageResponse.from(messageRepository.save(message));
    }
}
```

Tạo `com.example.tour.controller.ConversationController`:

```java
package com.example.tour.controller;

import com.example.tour.dto.MessageRequest;
import com.example.tour.dto.MessageResponse;
import com.example.tour.service.MessageService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hoi-thoai")
@RequiredArgsConstructor
public class ConversationController {

    private final MessageService messageService;

    @GetMapping("/{conversationId}/tin-nhan")
    @PreAuthorize("@conversationAccess.isMember(#conversationId, authentication.name)")
    public List<MessageResponse> findMessages(@PathVariable Long conversationId) {
        return messageService.findMessages(conversationId);
    }

    @PostMapping("/{conversationId}/tin-nhan")
    @PreAuthorize("@conversationAccess.isMember(#conversationId, authentication.name)")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse sendMessage(@PathVariable Long conversationId,
                                       @Valid @RequestBody MessageRequest request,
                                       Authentication authentication) {
        return messageService.sendMessage(conversationId, authentication.getName(), request);
    }
}
```

Tạo `com.example.tour.config.ChatDataInitializer`:

```java
package com.example.tour.config;

import com.example.tour.entity.Conversation;
import com.example.tour.entity.ConversationMember;
import com.example.tour.entity.KhachHang;
import com.example.tour.entity.Message;
import com.example.tour.entity.Tour;
import com.example.tour.repository.ConversationMemberRepository;
import com.example.tour.repository.ConversationRepository;
import com.example.tour.repository.KhachHangRepository;
import com.example.tour.repository.MessageRepository;
import com.example.tour.repository.TourRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Order(2)
@RequiredArgsConstructor
public class ChatDataInitializer implements CommandLineRunner {

    private final ConversationRepository conversationRepository;
    private final ConversationMemberRepository conversationMemberRepository;
    private final MessageRepository messageRepository;
    private final KhachHangRepository khachHangRepository;
    private final TourRepository tourRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (conversationRepository.count() > 0) {
            return;
        }
        KhachHang khachHang1 = khachHangRepository.findByTaiKhoanUsername("tv1")
                .orElseThrow(() -> new IllegalStateException("Chưa có dữ liệu khách hàng tv1"));
        Tour tour = tourRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Chưa có dữ liệu tour"));

        Conversation conversation = new Conversation();
        conversation.setTour(tour);
        conversation.setTenNhom("Nhóm khách tour Đà Lạt");
        conversation.setCreatedAt(LocalDateTime.now());
        conversationRepository.save(conversation);

        ConversationMember member = new ConversationMember();
        member.setConversation(conversation);
        member.setKhachHang(khachHang1);
        member.setJoinedAt(LocalDateTime.now());
        conversationMemberRepository.save(member);

        Message first = new Message();
        first.setConversation(conversation);
        first.setSender(khachHang1);
        first.setNoiDung("Cả nhóm đã nhận được hóa đơn HD-0001 chưa?");
        first.setCreatedAt(LocalDateTime.now().minusMinutes(10));
        messageRepository.save(first);

        Message second = new Message();
        second.setConversation(conversation);
        second.setSender(khachHang1);
        second.setNoiDung("Xe khởi hành lúc 6 giờ tại điểm hẹn trung tâm.");
        second.setCreatedAt(LocalDateTime.now().minusMinutes(5));
        messageRepository.save(second);
    }
}
```

Luồng chạy: `ChatDataInitializer` có `@Order(2)` nên chạy sau `DataInitializer` (đã đánh `@Order(1)`), tìm khách hàng gắn với tài khoản `tv1` và tour đầu tiên, tạo một nhóm hội thoại, thêm đúng một thành viên là TV1 và hai tin nhắn mẫu. Tài khoản `tv2` cố ý không được thêm vào `conversation_member`.

Khi TV1 gọi `GET /api/hoi-thoai/1/tin-nhan`, Spring Security đánh giá biểu thức `@conversationAccess.isMember(#conversationId, authentication.name)`: bean nhận `1` và `tv1`, chạy truy vấn đếm trên bảng `conversation_member` join sang `khach_hang` và `tai_khoan`, nhận `true`, phương thức Controller mới được gọi. Khi TV2 gọi cùng URL, bean nhận `1` và `tv2`, truy vấn trả `false`, `AuthorizationDeniedException` được ném ra trước khi Controller chạy, request không bao giờ chạm tới `MessageService`.

**3. Demo trực quan:**

TV1 đọc lịch sử nhóm, Basic Auth `tv1` / `123456`:

```http
GET http://localhost:8080/api/hoi-thoai/1/tin-nhan
Authorization: Basic tv1:123456
```

Phản hồi `200 OK`:

```json
[
  {
    "id": 1,
    "nguoiGui": "Khách hàng TV1",
    "noiDung": "Cả nhóm đã nhận được hóa đơn HD-0001 chưa?",
    "thoiGian": "2026-11-06T09:05:30.112"
  },
  {
    "id": 2,
    "nguoiGui": "Khách hàng TV1",
    "noiDung": "Xe khởi hành lúc 6 giờ tại điểm hẹn trung tâm.",
    "thoiGian": "2026-11-06T09:10:30.112"
  }
]
```

TV1 gửi tin nhắn mới:

```http
POST http://localhost:8080/api/hoi-thoai/1/tin-nhan
Authorization: Basic tv1:123456
Content-Type: application/json

{
  "noiDung": "Mình đã chuyển khoản, nhờ ban tổ chức kiểm tra giúp."
}
```

Phản hồi `201 Created`:

```json
{
  "id": 3,
  "nguoiGui": "Khách hàng TV1",
  "noiDung": "Mình đã chuyển khoản, nhờ ban tổ chức kiểm tra giúp.",
  "thoiGian": "2026-11-06T09:22:04.771"
}
```

TV2 đọc trộm cùng nhóm, Basic Auth `tv2` / `123456`, gọi `GET /api/hoi-thoai/1/tin-nhan`: phản hồi `403 Forbidden`, không có dữ liệu tin nhắn nào trong body. Ở thời điểm này body do Spring Security sinh mặc định; mục VI thay bằng JSON thống nhất.

### III. Nâng cấp lưu mật khẩu với DelegatingPasswordEncoder

**1. Nội dung tính năng:**

- **A.** Mật khẩu mới luôn được ghi ở dạng `{bcrypt}$2a$12$...`, tiền tố cho biết thuật toán đã dùng.
- **B.** Bản ghi cũ dạng `{noop}...`, `{sha256}...` vẫn đăng nhập được bình thường.
- **C.** Ngay sau lần đăng nhập thành công đầu tiên, hệ thống tự băm lại theo cost 12 và ghi đè bản ghi cũ, người dùng không phải đổi mật khẩu.
- **D.** Cơ chế gồm hai mảnh: `DelegatingPasswordEncoder` quyết định encoder theo tiền tố, `UserDetailsPasswordService` nhận tín hiệu nâng cấp từ `DaoAuthenticationProvider`.

**2. Triển khai Code:**

Thay thế toàn bộ `com.example.tour.config.PasswordEncoderConfig`:

```java
package com.example.tour.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    private static final String DEFAULT_ENCODER_ID = "bcrypt";
    private static final int BCRYPT_STRENGTH = 12;

    @Bean
    public PasswordEncoder passwordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder(BCRYPT_STRENGTH));
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        return new DelegatingPasswordEncoder(DEFAULT_ENCODER_ID, encoders);
    }
}
```

Tạo `com.example.tour.security.AccountPasswordService`:

```java
package com.example.tour.security;

import com.example.tour.entity.TaiKhoan;
import com.example.tour.repository.TaiKhoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountPasswordService implements UserDetailsPasswordService {

    private final TaiKhoanRepository taiKhoanRepository;

    @Override
    @Transactional
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Không tồn tại tài khoản " + user.getUsername()));
        taiKhoan.setPassword(newPassword);
        return User.withUsername(taiKhoan.getUsername())
                .password(newPassword)
                .roles(taiKhoan.getVaiTro().name())
                .disabled(!taiKhoan.isEnabled())
                .build();
    }
}
```

Bổ sung vào `com.example.tour.config.SecurityConfig`: thêm hai trường và một Bean, rồi truyền provider vào filter chain.

```java
    private final UserDetailsPasswordService userDetailsPasswordService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsPasswordService(userDetailsPasswordService);
        return provider;
    }
```

```java
    public SecurityFilterChain securityFilterChain(HttpSecurity http, DaoAuthenticationProvider provider)
            throws Exception {
        http
                .authenticationProvider(provider)
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
```

Cần import `org.springframework.security.authentication.dao.DaoAuthenticationProvider`, `org.springframework.security.core.userdetails.UserDetailsPasswordService` và `org.springframework.security.crypto.password.PasswordEncoder`. Toàn bộ nội dung hoàn chỉnh của `SecurityConfig` nằm ở mục VI.

Luồng chạy: khi đăng nhập, `DaoAuthenticationProvider` gọi `PasswordEncoder.matches` với chuỗi lưu trong DB. Với `{noop}123456`, `DelegatingPasswordEncoder` tách tiền tố `noop`, tra bảng encoder và dùng `NoOpPasswordEncoder` để so sánh chuỗi thuần, nên đăng nhập thành công. Ngay sau đó provider kiểm tra `upgradeEncoding`: id `noop` khác id mặc định `bcrypt` nên kết quả là `true`; provider băm lại mật khẩu vừa nhập bằng bcrypt cost 12 rồi gọi `updatePassword`. `AccountPasswordService` nạp entity trong transaction, gán mật khẩu mới, dirty checking của Hibernate phát ra câu `UPDATE` khi transaction kết thúc, không cần gọi `save` thủ công. Từ lần đăng nhập sau, chuỗi trong DB có tiền tố `{bcrypt}` và encoder bcrypt đọc cost 12 từ chính chuỗi đó để kiểm tra.

**3. Demo trực quan:**

Trước khi đăng nhập, bản ghi `tv2` trong bảng `tai_khoan` có giá trị cột `password` là `{noop}123456`. Thực hiện một request bất kỳ với Basic Auth `tv2` / `123456`:

```http
GET http://localhost:8080/api/hoa-don/me
Authorization: Basic tv2:123456
```

Phản hồi `200 OK`, body là mảng rỗng vì TV2 chưa có hóa đơn:

```json
[]
```

Trong log Hibernate xuất hiện câu cập nhật do dirty checking sinh ra, với tham số mật khẩu đã đổi định dạng:

```text
Hibernate: update tai_khoan set enabled=?, password=?, username=?, vai_tro=? where id=?
```

Giá trị tham số `password` sau lần đăng nhập đó có dạng:

```text
{bcrypt}$2a$12$Fh3sVQ9p1yLk8rT0uWm2XeY6cN4bZ7aQd1hJ5iK9lM3nO6pR8sT
```

Đăng nhập lại bằng đúng mật khẩu `123456` vẫn thành công, nhưng lần này không còn câu `UPDATE` nào được phát ra, vì `upgradeEncoding` trả `false` khi bản ghi đã ở đúng id `bcrypt` và đúng cost 12.

### IV. Mã hóa số điện thoại khách hàng bằng AES-GCM

**1. Nội dung tính năng:**

- **A.** Số điện thoại được mã hóa trước khi ghi xuống DB và giải mã khi đọc lên, hoàn toàn trong suốt với tầng nghiệp vụ.
- **B.** Mỗi lần ghi sinh một IV mới, nên hai khách hàng cùng số điện thoại cho ra hai chuỗi Base64 khác nhau.
- **C.** Cột `so_dien_thoai` chứa `Base64(IV || bản mã || tag)`, không chứa số thật.
- **D.** Không dùng cột này để tìm kiếm gần đúng; cần tìm chính xác thì thêm cột băm xác định riêng.

**2. Triển khai Code:**

Lớp `com.example.tour.security.crypto.AesGcmCipher` dùng nguyên khối code AES-GCM đã trình bày ở Phần 2 mục III, không cần sửa gì. Tạo tiếp `com.example.tour.security.crypto.PhoneEncryptionConverter`:

```java
package com.example.tour.security.crypto;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Converter
public class PhoneEncryptionConverter implements AttributeConverter<String, String> {

    @Autowired
    private AesGcmCipher aesGcmCipher;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null || attribute.isBlank()) {
            return attribute;
        }
        return aesGcmCipher.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return dbData;
        }
        return aesGcmCipher.decrypt(dbData);
    }
}
```

Cập nhật trường `soDienThoai` trong `com.example.tour.entity.KhachHang`:

```java
    @Convert(converter = PhoneEncryptionConverter.class)
    @Column(name = "so_dien_thoai", length = 255)
    private String soDienThoai;
```

Tạo `com.example.tour.dto.KhachHangResponse`:

```java
package com.example.tour.dto;

import com.example.tour.entity.KhachHang;

public record KhachHangResponse(
        Long id,
        String hoTen,
        String soDienThoai,
        String email) {

    public static KhachHangResponse from(KhachHang khachHang) {
        return new KhachHangResponse(
                khachHang.getId(),
                khachHang.getHoTen(),
                khachHang.getSoDienThoai(),
                khachHang.getEmail());
    }
}
```

Tạo `com.example.tour.service.KhachHangService`:

```java
package com.example.tour.service;

import com.example.tour.dto.KhachHangResponse;
import com.example.tour.entity.KhachHang;
import com.example.tour.exception.ResourceNotFoundException;
import com.example.tour.repository.KhachHangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KhachHangService {

    private final KhachHangRepository khachHangRepository;

    @Transactional(readOnly = true)
    public KhachHangResponse getMyProfile(String username) {
        return KhachHangResponse.from(findByUsername(username));
    }

    KhachHang findByUsername(String username) {
        return khachHangRepository.findByTaiKhoanUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hồ sơ khách hàng"));
    }
}
```

Tạo `com.example.tour.controller.KhachHangController`:

```java
package com.example.tour.controller;

import com.example.tour.dto.KhachHangResponse;
import com.example.tour.service.KhachHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/khach-hang")
@RequiredArgsConstructor
public class KhachHangController {

    private final KhachHangService khachHangService;

    @GetMapping("/me")
    public KhachHangResponse getMyProfile(Authentication authentication) {
        return khachHangService.getMyProfile(authentication.getName());
    }
}
```

Luồng chạy: `AttributeConverter` được Hibernate gọi ở hai thời điểm. Khi persist hoặc update entity, Hibernate lấy giá trị `String` từ `soDienThoai` và gọi `convertToDatabaseColumn`, phương thức này chuyển cho `AesGcmCipher.encrypt` sinh IV, mã hóa và trả chuỗi Base64 để ghi vào cột. Khi đọc entity từ kết quả truy vấn, Hibernate gọi `convertToEntityAttribute`, giải mã và trả lại số điện thoại dạng thuần cho tầng nghiệp vụ. Converter phải là Spring Bean để được tiêm `AesGcmCipher`; Spring Boot cấu hình sẵn `SpringBeanContainer` cho Hibernate nên việc tiêm này hoạt động mà không cần cấu hình thêm.

Lưu ý khi bật converter trên database đã có dữ liệu thô: cột đang chứa số điện thoại dạng `0901234567` sẽ không giải mã được và mọi lần đọc đều ném `IllegalStateException`. Phải xử lý dữ liệu cũ trước khi triển khai, bằng một runner đọc entity trong transaction rồi ghi lại để converter mã hóa, hoặc bằng cách để trống cột và yêu cầu khách hàng cập nhật lại. Trong dự án thực hành, xóa bảng `khach_hang` và để `DataInitializer` seed lại là đủ.

**3. Demo trực quan:**

Sau khi khởi động lại ứng dụng và seed lại dữ liệu, đọc hồ sơ của mình:

```http
GET http://localhost:8080/api/khach-hang/me
Authorization: Basic tv1:123456
```

Phản hồi `200 OK` trả về số điện thoại đã được giải mã:

```json
{
  "id": 1,
  "hoTen": "Khách hàng TV1",
  "soDienThoai": "0901234567",
  "email": "tv1@example.com"
}
```

Trong khi đó, log Hibernate của câu insert ghi tham số đã mã hóa:

```text
Hibernate: insert into khach_hang (email, ho_ten, so_dien_thoai, tai_khoan_id) values (?, ?, ?, ?)
binding parameter [1] as [VARCHAR] - [tv1@example.com]
binding parameter [2] as [VARCHAR] - [Khách hàng TV1]
binding parameter [3] as [VARCHAR] - [k7Qm2Xr9TbV4LpZ1sC8nEw==3fA9dK2mQ7vXbT1pR8sUwY6c]
```

Chuỗi tham số thứ ba là Base64 của `IV || bản mã || tag`, không phải số điện thoại. Gọi lại `GET /api/khach-hang/me` nhiều lần cho kết quả giống nhau vì dữ liệu đã nằm trong DB; nếu cập nhật lại cùng số điện thoại đó, chuỗi Base64 mới sẽ khác chuỗi cũ do IV mới được sinh ra.

### V. Chống dò mật khẩu (Brute-force)

**1. Nội dung tính năng:**

- **A.** Đếm số lần đăng nhập sai theo từng tài khoản, lưu trong `ConcurrentHashMap` an toàn với đa luồng.
- **B.** Sai đủ 5 lần thì tài khoản bị khóa 15 phút, kể cả khi lần thứ sáu nhập đúng mật khẩu.
- **C.** Trạng thái khóa được biểu diễn qua `User.accountNonLocked(false)`, khiến `DaoAuthenticationProvider` ném `LockedException` trước khi so mật khẩu.
- **D.** Đăng nhập thành công xóa bộ đếm của tài khoản đó.
- **E.** Bộ đếm nằm trong bộ nhớ nên chỉ đúng cho một instance; hệ thống nhiều node phải dùng Redis hoặc bảng có TTL.

**2. Triển khai Code:**

Tạo `com.example.tour.security.LoginAttemptService`:

```java
package com.example.tour.security;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCK_DURATION_MINUTES = 15;

    private final Map<String, Attempt> attempts = new ConcurrentHashMap<>();

    public void loginSucceeded(String username) {
        attempts.remove(username);
    }

    public void loginFailed(String username) {
        long now = System.currentTimeMillis();
        attempts.compute(username, (key, current) -> {
            Attempt attempt = (current == null || current.isExpired(now)) ? Attempt.empty() : current;
            int failures = attempt.failures() + 1;
            long lockedUntil = failures >= MAX_ATTEMPTS
                    ? now + Duration.ofMinutes(LOCK_DURATION_MINUTES).toMillis()
                    : 0L;
            return new Attempt(failures, lockedUntil);
        });
    }

    public boolean isBlocked(String username) {
        Attempt attempt = attempts.get(username);
        if (attempt == null || !attempt.isLocked()) {
            return false;
        }
        if (attempt.isExpired(System.currentTimeMillis())) {
            attempts.remove(username);
            return false;
        }
        return true;
    }

    private record Attempt(int failures, long lockedUntil) {

        static Attempt empty() {
            return new Attempt(0, 0L);
        }

        boolean isLocked() {
            return lockedUntil > 0L;
        }

        boolean isExpired(long now) {
            return isLocked() && now >= lockedUntil;
        }
    }
}
```

Thay thế `com.example.tour.security.CustomUserDetailsService`:

```java
package com.example.tour.security;

import com.example.tour.entity.TaiKhoan;
import com.example.tour.repository.TaiKhoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final TaiKhoanRepository taiKhoanRepository;
    private final LoginAttemptService loginAttemptService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tồn tại tài khoản " + username));
        return User.withUsername(taiKhoan.getUsername())
                .password(taiKhoan.getPassword())
                .roles(taiKhoan.getVaiTro().name())
                .disabled(!taiKhoan.isEnabled())
                .accountNonLocked(!loginAttemptService.isBlocked(username))
                .build();
    }
}
```

Tạo `com.example.tour.security.AuthenticationEventListener`:

```java
package com.example.tour.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationEventListener {

    private final LoginAttemptService loginAttemptService;

    @EventListener
    public void onFailure(AuthenticationFailureBadCredentialsEvent event) {
        loginAttemptService.loginFailed(event.getAuthentication().getName());
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        loginAttemptService.loginSucceeded(event.getAuthentication().getName());
    }
}
```

Tạo `com.example.tour.dto.ErrorResponse`:

```java
package com.example.tour.dto;

public record ErrorResponse(int status, String message) {
}
```

Tạo `com.example.tour.security.RestAuthenticationEntryPoint`:

```java
package com.example.tour.security;

import com.example.tour.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String INVALID_CREDENTIALS_MESSAGE = "Sai tên đăng nhập hoặc mật khẩu";
    private static final String LOCKED_MESSAGE =
            "Tài khoản đang bị tạm khóa do đăng nhập sai quá số lần cho phép, vui lòng thử lại sau 15 phút";

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String message = authException instanceof LockedException
                ? LOCKED_MESSAGE
                : INVALID_CREDENTIALS_MESSAGE;
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        objectMapper.writeValue(response.getOutputStream(),
                new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), message));
    }
}
```

Bổ sung vào `com.example.tour.config.SecurityConfig`: thêm trường `restAuthenticationEntryPoint` và cấu hình `exceptionHandling` trong filter chain.

```java
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
```

```java
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(restAuthenticationEntryPoint))
```

Luồng chạy: mỗi lần xác thực thất bại vì sai mật khẩu, `ProviderManager` phát ra `AuthenticationFailureBadCredentialsEvent`; listener đếm vào map theo username. Khi bộ đếm chạm 5, `lockedUntil` được gán mốc thời gian hiện tại cộng 15 phút. Ở request kế tiếp, `CustomUserDetailsService` gọi `loginAttemptService.isBlocked(username)`, nhận `true`, dựng `User` với `accountNonLocked(false)`; `DaoAuthenticationProvider` kiểm tra `preAuthenticationChecks` và ném `LockedException` trước khi chạm tới bước so mật khẩu. Ngoại lệ này đi tới `RestAuthenticationEntryPoint`, nơi phản hồi JSON 401 với thông điệp khóa tài khoản. Sau 15 phút, `isBlocked` thấy mốc đã qua, xóa bản ghi và tài khoản đăng nhập lại bình thường.

**3. Demo trực quan:**

Gọi liên tiếp 5 lần với mật khẩu sai, Basic Auth `tv2` / `sai-mat-khau`:

```http
GET http://localhost:8080/api/hoa-don/me
Authorization: Basic tv2:sai-mat-khau
```

Cả 5 lần đều trả `401 Unauthorized`:

```json
{
  "status": 401,
  "message": "Sai tên đăng nhập hoặc mật khẩu"
}
```

Lần thứ 6, dùng đúng mật khẩu `123456`:

```http
GET http://localhost:8080/api/hoa-don/me
Authorization: Basic tv2:123456
```

Phản hồi vẫn là `401 Unauthorized`, nhưng thông điệp đã khác:

```json
{
  "status": 401,
  "message": "Tài khoản đang bị tạm khóa do đăng nhập sai quá số lần cho phép, vui lòng thử lại sau 15 phút"
}
```

Tài khoản `tv1` không bị ảnh hưởng vì bộ đếm tách riêng theo username.

### VI. Chặn Mass Assignment & Xử lý lỗi tập trung

**1. Nội dung tính năng:**

- **A.** Mọi phương thức POST/PUT nhận DTO có danh sách trường cố định, không bind trực tiếp vào Entity.
- **B.** DTO được kiểm tra bằng Bean Validation qua `@Valid`, thông điệp lỗi bằng tiếng Việt.
- **C.** `GlobalExceptionHandler` trả về đúng một cấu trúc `{"status": ..., "message": "..."}` cho mọi loại lỗi, không lộ stack trace hay mã nội bộ.
- **D.** Header CSP được gắn vào toàn bộ response để chặn XSS ở tầng trình duyệt.

**2. Triển khai Code:**

Tạo `com.example.tour.dto.TourRequest`:

```java
package com.example.tour.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TourRequest(
        @NotBlank(message = "Tên tour không được để trống")
        @Size(max = 200, message = "Tên tour tối đa 200 ký tự")
        String tenTour,

        @NotNull(message = "Giá tour không được để trống")
        @DecimalMin(value = "0", inclusive = false, message = "Giá tour phải lớn hơn 0")
        BigDecimal gia,

        @Min(value = 1, message = "Số chỗ phải lớn hơn 0")
        int soCho,

        @NotNull(message = "Ngày khởi hành không được để trống")
        @Future(message = "Ngày khởi hành phải ở tương lai")
        LocalDate ngayKhoiHanh) {
}
```

Tạo `com.example.tour.dto.TourResponse`:

```java
package com.example.tour.dto;

import com.example.tour.entity.Tour;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TourResponse(
        Long id,
        String tenTour,
        BigDecimal gia,
        int soCho,
        LocalDate ngayKhoiHanh) {

    public static TourResponse from(Tour tour) {
        return new TourResponse(
                tour.getId(),
                tour.getTenTour(),
                tour.getGia(),
                tour.getSoCho(),
                tour.getNgayKhoiHanh());
    }
}
```

Tạo `com.example.tour.dto.KhachHangRequest`:

```java
package com.example.tour.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record KhachHangRequest(
        @NotBlank(message = "Họ tên không được để trống")
        @Size(max = 100, message = "Họ tên tối đa 100 ký tự")
        String hoTen,

        @NotBlank(message = "Số điện thoại không được để trống")
        @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0")
        String soDienThoai,

        @Email(message = "Email không hợp lệ")
        @Size(max = 100, message = "Email tối đa 100 ký tự")
        String email) {
}
```

Tạo `com.example.tour.service.TourService`:

```java
package com.example.tour.service;

import com.example.tour.dto.TourRequest;
import com.example.tour.dto.TourResponse;
import com.example.tour.entity.Tour;
import com.example.tour.exception.ResourceNotFoundException;
import com.example.tour.repository.TourRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;

    @Transactional(readOnly = true)
    public List<TourResponse> findAll() {
        return tourRepository.findAll().stream()
                .map(TourResponse::from)
                .toList();
    }

    @Transactional
    public TourResponse create(TourRequest request) {
        Tour tour = new Tour();
        apply(tour, request);
        return TourResponse.from(tourRepository.save(tour));
    }

    @Transactional
    public TourResponse update(Long id, TourRequest request) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tour " + id));
        apply(tour, request);
        return TourResponse.from(tour);
    }

    private void apply(Tour tour, TourRequest request) {
        tour.setTenTour(request.tenTour());
        tour.setGia(request.gia());
        tour.setSoCho(request.soCho());
        tour.setNgayKhoiHanh(request.ngayKhoiHanh());
    }
}
```

Tạo `com.example.tour.controller.TourController`:

```java
package com.example.tour.controller;

import com.example.tour.dto.TourRequest;
import com.example.tour.dto.TourResponse;
import com.example.tour.service.TourService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tour")
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;

    @GetMapping
    public List<TourResponse> findAll() {
        return tourService.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public TourResponse create(@Valid @RequestBody TourRequest request) {
        return tourService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public TourResponse update(@PathVariable Long id, @Valid @RequestBody TourRequest request) {
        return tourService.update(id, request);
    }
}
```

Cập nhật toàn bộ `com.example.tour.service.KhachHangService` để nhận thêm luồng cập nhật hồ sơ:

```java
package com.example.tour.service;

import com.example.tour.dto.KhachHangRequest;
import com.example.tour.dto.KhachHangResponse;
import com.example.tour.entity.KhachHang;
import com.example.tour.exception.ResourceNotFoundException;
import com.example.tour.repository.KhachHangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KhachHangService {

    private final KhachHangRepository khachHangRepository;

    @Transactional(readOnly = true)
    public KhachHangResponse getMyProfile(String username) {
        return KhachHangResponse.from(findByUsername(username));
    }

    @Transactional
    public KhachHangResponse updateMyProfile(String username, KhachHangRequest request) {
        KhachHang khachHang = findByUsername(username);
        khachHang.setHoTen(request.hoTen());
        khachHang.setSoDienThoai(request.soDienThoai());
        khachHang.setEmail(request.email());
        return KhachHangResponse.from(khachHang);
    }

    KhachHang findByUsername(String username) {
        return khachHangRepository.findByTaiKhoanUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hồ sơ khách hàng"));
    }
}
```

Cập nhật toàn bộ `com.example.tour.controller.KhachHangController`:

```java
package com.example.tour.controller;

import com.example.tour.dto.KhachHangRequest;
import com.example.tour.dto.KhachHangResponse;
import com.example.tour.service.KhachHangService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/khach-hang")
@RequiredArgsConstructor
public class KhachHangController {

    private final KhachHangService khachHangService;

    @GetMapping("/me")
    public KhachHangResponse getMyProfile(Authentication authentication) {
        return khachHangService.getMyProfile(authentication.getName());
    }

    @PutMapping("/me")
    public KhachHangResponse updateMyProfile(Authentication authentication,
                                             @Valid @RequestBody KhachHangRequest request) {
        return khachHangService.updateMyProfile(authentication.getName(), request);
    }
}
```

Tạo `com.example.tour.security.RestAccessDeniedHandler`:

```java
package com.example.tour.security;

import com.example.tour.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        objectMapper.writeValue(response.getOutputStream(),
                new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền thực hiện thao tác này"));
    }
}
```

Tạo `com.example.tour.exception.GlobalExceptionHandler`:

```java
package com.example.tour.exception;

import com.example.tour.dto.ErrorResponse;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleConflict(DataIntegrityViolationException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(HttpStatus.CONFLICT.value(),
                        "Dữ liệu vi phạm ràng buộc của hệ thống"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleDenied(AccessDeniedException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền thực hiện thao tác này"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception exception) {
        log.error("Lỗi không mong đợi", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Hệ thống đang gặp sự cố, vui lòng thử lại sau"));
    }
}
```

Thay thế toàn bộ `com.example.tour.config.SecurityConfig` bằng phiên bản hoàn chỉnh:

```java
package com.example.tour.config;

import com.example.tour.security.CustomUserDetailsService;
import com.example.tour.security.RestAccessDeniedHandler;
import com.example.tour.security.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String CONTENT_SECURITY_POLICY =
            "default-src 'self'; script-src 'self'; style-src 'self'; img-src 'self' data:; "
                    + "font-src 'self'; connect-src 'self'; object-src 'none'; "
                    + "frame-ancestors 'none'; base-uri 'self'; form-action 'self'";

    private final CustomUserDetailsService customUserDetailsService;
    private final UserDetailsPasswordService userDetailsPasswordService;
    private final PasswordEncoder passwordEncoder;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final RestAccessDeniedHandler restAccessDeniedHandler;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsPasswordService(userDetailsPasswordService);
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   DaoAuthenticationProvider provider) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated())
                .authenticationProvider(provider)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                        .accessDeniedHandler(restAccessDeniedHandler))
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp.policyDirectives(CONTENT_SECURITY_POLICY))
                        .frameOptions(frame -> frame.deny())
                        .referrerPolicy(referrer -> referrer
                                .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER))
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000)))
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
```

Luồng chạy: request đi vào Controller, `@Valid` kiểm tra DTO trước khi thân phương thức chạy; nếu vi phạm, `MethodArgumentNotValidException` được ném và `GlobalExceptionHandler` gom toàn bộ lỗi trường thành một chuỗi, trả 400 với đúng hai khóa `status` và `message`. DTO chỉ có các trường được phép, nên client gửi thêm `id`, `tongTien` hay bất kỳ trường lạ nào cũng bị bỏ qua thay vì ghi đè Entity. `ResourceNotFoundException` đi qua handler riêng và trả 404. Lỗi ràng buộc DB trả 409. Mọi ngoại lệ còn lại rơi vào handler cuối, được ghi log đầy đủ ở server nhưng chỉ trả về câu thông báo chung cho client. Riêng các lỗi phát sinh ở tầng filter — chưa xác thực hoặc thiếu quyền ở mức URL — được `RestAuthenticationEntryPoint` và `RestAccessDeniedHandler` trả JSON cùng cấu trúc. Header `Content-Security-Policy` và các header phụ được gắn vào mọi response ngay trong filter chain.

**3. Demo trực quan:**

ADMIN tạo tour với dữ liệu không hợp lệ, cố tình gửi kèm trường `id` để thử mass assignment:

```http
POST http://localhost:8080/api/tour
Authorization: Basic admin:admin123
Content-Type: application/json

{
  "id": 999,
  "tenTour": "",
  "gia": 0,
  "soCho": 0,
  "ngayKhoiHanh": "2020-01-01"
}
```

Phản hồi `400 Bad Request`:

```json
{
  "status": 400,
  "message": "gia: Giá tour phải lớn hơn 0; ngayKhoiHanh: Ngày khởi hành phải ở tương lai; soCho: Số chỗ phải lớn hơn 0; tenTour: Tên tour không được để trống"
}
```

ADMIN gửi lại dữ liệu hợp lệ, vẫn kèm `id` và một trường lạ:

```http
POST http://localhost:8080/api/tour
Authorization: Basic admin:admin123
Content-Type: application/json

{
  "id": 999,
  "tenTour": "Phú Quốc 4 ngày 3 đêm",
  "gia": 6500000,
  "soCho": 15,
  "ngayKhoiHanh": "2026-12-20",
  "noiBat": true
}
```

Phản hồi `201 Created`, bản ghi nhận id do database sinh và hoàn toàn bỏ qua `id` cũng như `noiBat`:

```json
{
  "id": 2,
  "tenTour": "Phú Quốc 4 ngày 3 đêm",
  "gia": 6500000.00,
  "soCho": 15,
  "ngayKhoiHanh": "2026-12-20"
}
```

TV1 gọi endpoint chỉ dành cho ADMIN:

```http
POST http://localhost:8080/api/tour
Authorization: Basic tv1:123456
Content-Type: application/json

{
  "tenTour": "Tour thử quyền",
  "gia": 1000000,
  "soCho": 5,
  "ngayKhoiHanh": "2026-12-25"
}
```

Phản hồi `403 Forbidden`:

```json
{
  "status": 403,
  "message": "Bạn không có quyền thực hiện thao tác này"
}
```

Gọi hóa đơn không tồn tại bằng TV1:

```http
GET http://localhost:8080/api/hoa-don/999
Authorization: Basic tv1:123456
```

Phản hồi `404 Not Found`, không còn khối `timestamp`, `path` hay `error` mặc định:

```json
{
  "status": 404,
  "message": "Không tìm thấy hóa đơn"
}
```

Gọi API mà không gửi thông tin đăng nhập:

```http
GET http://localhost:8080/api/tour
```

Phản hồi `401 Unauthorized`:

```json
{
  "status": 401,
  "message": "Sai tên đăng nhập hoặc mật khẩu"
}
```

Header của mọi response đều chứa cấu hình CSP và các header phòng thủ phụ:

```text
Content-Security-Policy: default-src 'self'; script-src 'self'; style-src 'self'; img-src 'self' data:; font-src 'self'; connect-src 'self'; object-src 'none'; frame-ancestors 'none'; base-uri 'self'; form-action 'self'
X-Frame-Options: DENY
Referrer-Policy: no-referrer
X-Content-Type-Options: nosniff
```

TV1 cập nhật hồ sơ với số điện thoại sai định dạng:

```http
PUT http://localhost:8080/api/khach-hang/me
Authorization: Basic tv1:123456
Content-Type: application/json

{
  "hoTen": "Khách hàng TV1",
  "soDienThoai": "091234",
  "email": "tv1@example.com"
}
```

Phản hồi `400 Bad Request`:

```json
{
  "status": 400,
  "message": "soDienThoai: Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0"
}
```

TV1 gửi lại dữ liệu hợp lệ, số điện thoại được mã hóa AES-GCM trước khi ghi DB và giải mã lại trong response:

```json
{
  "id": 1,
  "hoTen": "Khách hàng TV1",
  "soDienThoai": "0912345678",
  "email": "tv1@example.com"
}
```
