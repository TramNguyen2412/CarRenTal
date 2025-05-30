package ui.admin.BaoDuong;

import model.ChiTietBaoDuong;
import model.DichVuBD;
import controller.BaoDuongController;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ChiTietBaoDuongTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Mã DV", "Tên dịch vụ", "Số lượng", "Đơn giá", "Thành tiền"};
    private List<ChiTietBaoDuong> data;
    private BaoDuongController baoDuongController = new BaoDuongController();

    public ChiTietBaoDuongTableModel(List<ChiTietBaoDuong> data) {
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ChiTietBaoDuong ct = data.get(rowIndex);
        DichVuBD dv = baoDuongController.getDichVuBDById(ct.getMaDV());
        switch (columnIndex) {
            case 0: return ct.getMaDV();
            case 1: return dv != null ? dv.getTenDV() : "";
            case 2: return ct.getSoLuong();
            case 3: return dv != null ? dv.getGiaDV() : 0;
            case 4: return dv != null ? dv.getGiaDV() * ct.getSoLuong() : 0;
            default: return "";
        }
    }

    public void setData(List<ChiTietBaoDuong> data) {
        this.data = data;
        fireTableDataChanged();
    }
}