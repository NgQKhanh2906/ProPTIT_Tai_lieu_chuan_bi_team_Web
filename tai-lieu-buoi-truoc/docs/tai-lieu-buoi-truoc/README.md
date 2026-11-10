# Tài liệu các buổi trước

Thư mục này chứa tài liệu chuẩn bị các buổi đã học, dùng làm nguồn đối chiếu cho Claude Code khi rà soát dự án và viết tài liệu Buổi 11 (theo `Prompt_ClaudeCode_RaSoat_va_Buoi11.md`, Phần 1).

## Phần 1: Nội dung

| Thư mục | File | Nội dung | Ảnh |
| --- | --- | --- | --- |
| `buoi2/` | `BaiTapBuoi2.md` | Thiết kế CSDL quản lý đặt tour | Đủ: `image-1.png` (lược đồ E-R) |
| `buoi3/` | `Buoi3_SpringBoot_JPA_Validation_Exception.md` | Quan hệ JPA, truy vấn, tối ưu, Validation, Exception | Thiếu 12 ảnh, xem Phần 2 |
| `buoi8_9/` | `Tai_lieu_chuan_bi_buoi8_9.md` | Tối ưu JPA, Validation, Exception, thao tác Database | Thiếu 14 ảnh, xem Phần 2 |
| `buoi10/` | `Tai_lieu_chuan_bi_buoi10.md` | Security 1: Spring Security, BCrypt, JWT, phân quyền | Đủ 13 ảnh `buoi10-*.png` |
| `ban-nhap-buoi11/` | `Tai_lieu_chuan_bi_buoi11_ban_nhap.md` | Bản nháp Buổi 11 do Claude Web soạn | Đủ 10 ảnh `buoi11-*.png` |

Ghi chú:

- Buổi 10 là bản cập nhật ngày 30/10/2026 (đã sửa lỗi `ObjectMapper` trên Spring Boot 4). Bản nháp Buổi 11 là bản ngày 02/11/2026.
- Tài liệu được chia thư mục con vì Buổi 2 và Buổi 8 + 9 cùng dùng tên `image-1.png` cho hai ảnh khác nhau. Prompt tìm tài liệu bằng Glob `**/...` nên vẫn tìm thấy.

## Phần 2: Ảnh còn thiếu

Các ảnh dưới đây không có trong Claude Project nên không đóng gói được. Chép từ máy của bạn vào đúng vị trí:

- **A.** `buoi3/images/`:
  - `01-owning-inverse.png`, `02-join-table.png`, `03-query-levels.png`, `04-derived-parser.png`
  - `05-entitymanager.png`, `06-lazy-proxy.png`, `07-pagination.png`, `08-first-level-cache.png`
  - `09-n-plus-1.png`, `10-validation-flow.png`, `11-exception-flow.png`, `12-erd-baitap.png`
- **B.** `buoi8_9/`:
  - Ảnh chụp màn hình: `image.png`, `image-1.png` đến `image-6.png`
  - Sơ đồ: `buoi9-1-owning-side.png`, `buoi9-2-many-to-many.png`, `buoi9-3-entity-trung-gian.png`, `buoi9-4-query-levels.png`, `buoi9-5-derived-parser.png`, `buoi9-6-criteria.png`, `buoi9-7-erd-baitap.png`

Thiếu ảnh chỉ ảnh hưởng đến hiển thị khi mở file Markdown, không ảnh hưởng đến việc Claude Code đọc nội dung chữ.

## Phần 3: Cách đặt vào repo

- **A.** Giải nén file zip tại **thư mục gốc repo**, sẽ có `docs/tai-lieu-buoi-truoc/` và `CLAUDE.md`.
- **B.** `CLAUDE.md` là nội dung "Hướng dẫn làm việc cho Claude" lấy từ Claude Project. Repo đã có `CLAUDE.md` thì không ghi đè, chỉ đối chiếu và bổ sung phần còn thiếu.
- **C.** Kiểm tra lại bằng lệnh:

```bash
find docs/tai-lieu-buoi-truoc -type f | sort
```
