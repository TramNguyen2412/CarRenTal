# CarRental - Hệ thống Quản lý Thuê Xe

## Giới thiệu

CarRental là hệ thống quản lý thuê xe được phát triển bằng Java, giúp khách hàng có thể thuê xe một cách thuận tiện và giúp quản trị viên dễ dàng quản lý quy trình cho thuê xe.

## Tính năng chính

- **Đối với khách hàng**:
  - Xem danh sách xe có sẵn
  - Đặt xe và quản lý giỏ xe
  - Đánh giá dịch vụ
 
- **Đối với quản trị viên**:
  - Quản lý danh sách xe
  - Quản lý khách hàng
  - Quản lý nhân viên
  - Quản lý hợp đồng thuê xe
  - Quản lý dịch vụ bảo dưỡng
  - Quản lý bảo dưỡng
  - Quản lý công nợ khách hàng
  - Quản lý giao nhận xe
  - Báo cáo doanh thu và thống kê

## Cài đặt

### Yêu cầu hệ thống
- JDK 11 trở lên
- Maven
- Oracle

### Thiết lập cơ sở dữ liệu
1. Mở Oracle và tạo database, connection mới
2. Chạy script SQL từ file trong `src/main/resources/database/Script_Database.sql`

### Cấu hình kết nối cơ sở dữ liệu
Mở file `DatabaseConfig.java` trong package `util` để cập nhật thông tin kết nối:

#### Kết nối với Oracle 18c
- private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
- private static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl"; // Hoặc SID phù hợp
- private static final String USER = "username";
- private static final String PASSWORD = "password";
#### Kết nối với Oracle 21c
- private static final String DRIVER = "oracle.jdbc.OracleDriver";
- private static final String URL = "jdbc:oracle:thin:@//localhost:1521/XEPDB1"; // Sử dụng service name
- private static final String USER = "username";
- private static final String PASSWORD = "password";
