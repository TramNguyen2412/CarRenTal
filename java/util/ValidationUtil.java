package util;

import java.util.regex.Pattern;

public class ValidationUtil {
    
    // Kiểm tra biển số xe hợp lệ (VD: 51A-12345)
    public static boolean isValidLicensePlate(String licensePlate) {
        String regex = "^\\d{2}[A-Z]-\\d{4,5}$";
        return Pattern.matches(regex, licensePlate);
    }
    
    // Kiểm tra năm sản xuất hợp lệ
    public static boolean isValidProductionYear(int year) {
        int currentYear = java.time.Year.now().getValue();
        return year > 1900 && year <= currentYear;
    }
    
    // Kiểm tra giá tiền hợp lệ
    public static boolean isValidPrice(double price) {
        return price > 0;
    }
    
    // Kiểm tra số chỗ hợp lệ
    public static boolean isValidSeatCount(int seats) {
        return seats > 0 && seats <= 50; // Giả sử xe có tối đa 50 chỗ ngồi
    }
    
    // Kiểm tra chuỗi rỗng
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}