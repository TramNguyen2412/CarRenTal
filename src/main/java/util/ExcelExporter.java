package util;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.util.CellRangeAddress;
import model.XeDoanhThu;
import model.KhachHangDoanhThu;
public class ExcelExporter {
   
    public static void exportDoanhThuTheoThang(Map<Integer, Double> doanhThuThang, int year, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            // Tạo sheet
            Sheet sheet = workbook.createSheet("Doanh Thu Theo Tháng");
            
            // Tạo tiêu đề và định dạng
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            
            // Tạo style cho dữ liệu
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            
            // Tạo style định dạng tiền
            CellStyle moneyStyle = workbook.createCellStyle();
            moneyStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
            moneyStyle.setBorderBottom(BorderStyle.THIN);
            moneyStyle.setBorderTop(BorderStyle.THIN);
            moneyStyle.setBorderLeft(BorderStyle.THIN);
            moneyStyle.setBorderRight(BorderStyle.THIN);
            
            // Tạo hàng tiêu đề báo cáo
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("BÁO CÁO DOANH THU THEO THÁNG NĂM " + year);
            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleCell.setCellStyle(titleStyle);
            
            // Gộp ô tiêu đề
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
            
            // Tạo thông tin báo cáo
            Row infoRow = sheet.createRow(1);
            infoRow.createCell(0).setCellValue("Ngày xuất báo cáo: " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));
            
            // Tạo hàng tiêu đề cột
            Row headerRow = sheet.createRow(3);
            String[] columns = {"STT", "Tháng", "Doanh thu (VNĐ)"};
            
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Tạo dữ liệu
            int rowNum = 4;
            double totalRevenue = 0;
            
            for (int month = 1; month <= 12; month++) {
                Row row = sheet.createRow(rowNum++);
                
                // STT
                Cell sttCell = row.createCell(0);
                sttCell.setCellValue(month);
                sttCell.setCellStyle(dataStyle);
                
                // Tháng
                Cell monthCell = row.createCell(1);
                monthCell.setCellValue("Tháng " + month);
                monthCell.setCellStyle(dataStyle);
                
                // Doanh thu
                double revenue = doanhThuThang.getOrDefault(month, 0.0);
                totalRevenue += revenue;
                
                Cell revenueCell = row.createCell(2);
                revenueCell.setCellValue(revenue);
                revenueCell.setCellStyle(moneyStyle);
            }
            
            // Tạo hàng tổng cộng
            Row totalRow = sheet.createRow(rowNum);
            Cell totalLabelCell = totalRow.createCell(0);
            totalLabelCell.setCellValue("Tổng cộng:");
            totalLabelCell.setCellStyle(headerStyle);
            
            Cell totalMergeCell = totalRow.createCell(1);
            totalMergeCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 1));
            
            Cell totalValueCell = totalRow.createCell(2);
            totalValueCell.setCellValue(totalRevenue);
            totalValueCell.setCellStyle(moneyStyle);
            
            // Điều chỉnh độ rộng cột
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Ghi vào file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
                System.out.println("Xuất báo cáo doanh thu theo tháng thành công: " + filePath);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
   
    public static void exportDoanhThuTheoKhachHang(List<KhachHangDoanhThu> khachHangList, int year, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            // Tạo sheet
            Sheet sheet = workbook.createSheet("Doanh Thu Theo Khách Hàng");
            
            // Tạo tiêu đề và định dạng
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            
            // Tạo style cho dữ liệu
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            
            // Tạo style định dạng tiền
            CellStyle moneyStyle = workbook.createCellStyle();
            moneyStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
            moneyStyle.setBorderBottom(BorderStyle.THIN);
            moneyStyle.setBorderTop(BorderStyle.THIN);
            moneyStyle.setBorderLeft(BorderStyle.THIN);
            moneyStyle.setBorderRight(BorderStyle.THIN);
            moneyStyle.setAlignment(HorizontalAlignment.RIGHT);
            
            // Tạo hàng tiêu đề báo cáo
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("BÁO CÁO DOANH THU THEO KHÁCH HÀNG NĂM " + year);
            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleCell.setCellStyle(titleStyle);
            
            // Gộp ô tiêu đề
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
            
            // Tạo thông tin báo cáo
            Row infoRow = sheet.createRow(1);
            infoRow.createCell(0).setCellValue("Ngày xuất báo cáo: " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));
            
            Row usernameRow = sheet.createRow(2);
            usernameRow.createCell(0).setCellValue("Người xuất báo cáo: NgocTram2412");
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 4));
            
            // Tạo hàng tiêu đề cột
            Row headerRow = sheet.createRow(4);
            String[] columns = {"STT", "Mã KH", "Họ tên", "Số hợp đồng", "Doanh thu (VNĐ)"};
            
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Tạo dữ liệu
            int rowNum = 5;
            double totalRevenue = 0;
            
            for (int i = 0; i < khachHangList.size(); i++) {
                KhachHangDoanhThu kh = khachHangList.get(i);
                Row row = sheet.createRow(rowNum++);
                
                // STT
                Cell sttCell = row.createCell(0);
                sttCell.setCellValue(i + 1);
                sttCell.setCellStyle(dataStyle);
                
                // Mã KH
                Cell maKHCell = row.createCell(1);
                maKHCell.setCellValue(kh.getMaKH());
                maKHCell.setCellStyle(dataStyle);
                
                // Họ tên
                Cell hoTenCell = row.createCell(2);
                hoTenCell.setCellValue(kh.getHoTen());
                hoTenCell.setCellStyle(dataStyle);
                
                // Số hợp đồng
                Cell soHDCell = row.createCell(3);
                soHDCell.setCellValue(kh.getSoHopDong());
                soHDCell.setCellStyle(dataStyle);
                
                // Doanh thu
                double revenue = kh.getDoanhThu();
                totalRevenue += revenue;
                
                Cell revenueCell = row.createCell(4);
                revenueCell.setCellValue(revenue);
                revenueCell.setCellStyle(moneyStyle);
            }
            
            // Tạo hàng tổng cộng
            Row totalRow = sheet.createRow(rowNum);
            Cell totalLabelCell = totalRow.createCell(0);
            totalLabelCell.setCellValue("Tổng cộng:");
            totalLabelCell.setCellStyle(headerStyle);
            
            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 3));
            
            Cell totalValueCell = totalRow.createCell(4);
            totalValueCell.setCellValue(totalRevenue);
            totalValueCell.setCellStyle(moneyStyle);
            
            // Điều chỉnh độ rộng cột
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Ghi vào file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
                System.out.println("Xuất báo cáo doanh thu theo khách hàng thành công: " + filePath);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
   
    public static void exportDoanhThuTheoXe(List<XeDoanhThu> xeList, int year, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            // Tạo sheet
            Sheet sheet = workbook.createSheet("Doanh Thu Theo Xe");
            
            // Tạo tiêu đề và định dạng
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            
            // Tạo style cho dữ liệu
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            
            // Tạo style định dạng tiền
            CellStyle moneyStyle = workbook.createCellStyle();
            moneyStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
            moneyStyle.setBorderBottom(BorderStyle.THIN);
            moneyStyle.setBorderTop(BorderStyle.THIN);
            moneyStyle.setBorderLeft(BorderStyle.THIN);
            moneyStyle.setBorderRight(BorderStyle.THIN);
            moneyStyle.setAlignment(HorizontalAlignment.RIGHT);
            
            // Tạo hàng tiêu đề báo cáo
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("BÁO CÁO DOANH THU THEO XE NĂM " + year);
            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleCell.setCellStyle(titleStyle);
            
            // Gộp ô tiêu đề
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
            
            // Tạo thông tin báo cáo
            Row infoRow = sheet.createRow(1);
            infoRow.createCell(0).setCellValue("Ngày xuất báo cáo: " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
            
            Row usernameRow = sheet.createRow(2);
            usernameRow.createCell(0).setCellValue("Người xuất báo cáo: NgocTram2412");
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 5));
            
            // Tạo hàng tiêu đề cột
            Row headerRow = sheet.createRow(4);
            String[] columns = {"STT", "Mã xe", "Tên xe", "Biển số", "Số lượt thuê", "Doanh thu (VNĐ)"};
            
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Tạo dữ liệu
            int rowNum = 5;
            double totalRevenue = 0;
            
            for (int i = 0; i < xeList.size(); i++) {
                XeDoanhThu xe = xeList.get(i);
                Row row = sheet.createRow(rowNum++);
                
                // STT
                Cell sttCell = row.createCell(0);
                sttCell.setCellValue(i + 1);
                sttCell.setCellStyle(dataStyle);
                
                // Mã xe
                Cell maXeCell = row.createCell(1);
                maXeCell.setCellValue(xe.getMaXe());
                maXeCell.setCellStyle(dataStyle);
                
                // Tên xe
                Cell tenXeCell = row.createCell(2);
                tenXeCell.setCellValue(xe.getTenXe());
                tenXeCell.setCellStyle(dataStyle);
                
                // Biển số
                Cell bienSoCell = row.createCell(3);
                bienSoCell.setCellValue(xe.getBienSo());
                bienSoCell.setCellStyle(dataStyle);
                
                // Số lượt thuê
                Cell luotThueCell = row.createCell(4);
                luotThueCell.setCellValue(xe.getSoLuotThue());
                luotThueCell.setCellStyle(dataStyle);
                
                // Doanh thu
                double revenue = xe.getDoanhThu();
                totalRevenue += revenue;
                
                Cell revenueCell = row.createCell(5);
                revenueCell.setCellValue(revenue);
                revenueCell.setCellStyle(moneyStyle);
            }
            
            // Tạo hàng tổng cộng
            Row totalRow = sheet.createRow(rowNum);
            Cell totalLabelCell = totalRow.createCell(0);
            totalLabelCell.setCellValue("Tổng cộng:");
            totalLabelCell.setCellStyle(headerStyle);
            
            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 4));
            
            Cell totalValueCell = totalRow.createCell(5);
            totalValueCell.setCellValue(totalRevenue);
            totalValueCell.setCellStyle(moneyStyle);
            
            // Điều chỉnh độ rộng cột
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Ghi vào file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
                System.out.println("Xuất báo cáo doanh thu theo xe thành công: " + filePath);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}