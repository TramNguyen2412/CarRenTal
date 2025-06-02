-- BEGIN
--    -- Drop all tables
--    FOR c IN (SELECT table_name FROM user_tables WHERE table_name IN (
--       'CHITIETBAODUONG', 'DICHVUBD', 'PHIEUBAODUONG', 'DANHGIA', 
--       'LICHSUCONGNO', 'GIAONHANXE', 'CTHD', 'HOPDONG', 'NHANVIEN',
--       'KHACHHANG', 'VAITRO_QUYENHAN', 'TAIKHOAN', 'QUYENHAN', 'VAITRO', 'XE', 'GIOHANG'
--    )) LOOP
--       EXECUTE IMMEDIATE 'DROP TABLE ' || c.table_name || ' CASCADE CONSTRAINTS';
--    END LOOP;

--    -- Drop all sequences
--    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_khachhang';
--    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_taikhoan';
--    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_nhanvien';
--    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_xe';
--    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_hopdong';
--    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_giaonhanxe';
--    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_lichsucongno';
--    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_danhgia';
--    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_phieubaoduong';
--    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_dichvubd';
--    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_vaitro';
--    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_quyenhan';
--       EXECUTE IMMEDIATE 'DROP SEQUENCE seq_giohang';
      
-- END;

-- 2. Bảng VAITRO (Vai trò)
CREATE TABLE VAITRO (
    MaVaiTro VARCHAR2(10) PRIMARY KEY,
    TenVaiTro VARCHAR2(50) UNIQUE
);

-- 3. Bảng QUYENHAN (Quyền hạn)
CREATE TABLE QUYENHAN (
    MaQuyen VARCHAR2(10) PRIMARY KEY,
    TenQuyen VARCHAR2(100) UNIQUE,
    TrangThai VARCHAR2(50) CHECK (TrangThai IN ('Hoạt động', 'Không hoạt động'))
);

-- 1. Bảng TAIKHOAN (TAI KHOẢN)
CREATE TABLE TAIKHOAN (
    MaTK VARCHAR2(10) PRIMARY KEY,
    MaVaiTro VARCHAR2(10),
    TenDangNhap VARCHAR2(50) UNIQUE,
    MatKhau VARCHAR2(255),
    TrangThai VARCHAR2(50) CHECK (TrangThai IN ('Hoạt động', 'Không hoạt động')),
    FOREIGN KEY (MaVaiTro) REFERENCES VAITRO(MaVaiTro)
);

-- 4. Bảng VAITRO_QUYENHAN (Liên kết Vai trò và Quyền hạn)
CREATE TABLE VAITRO_QUYENHAN (
    MaVaiTro VARCHAR2(10),
    MaQuyen VARCHAR2(10),
    PRIMARY KEY (MaVaiTro, MaQuyen),
    FOREIGN KEY (MaVaiTro) REFERENCES VAITRO(MaVaiTro),
    FOREIGN KEY (MaQuyen) REFERENCES QUYENHAN(MaQuyen)
);

-- 5. Bảng KHACHHANG
CREATE TABLE KHACHHANG (
    MaKH VARCHAR2(10) PRIMARY KEY,
    MaTK VARCHAR2(10),
    TongTienNo NUMBER(10,2) DEFAULT 0,
    HoTen VARCHAR2(100),
    SDT VARCHAR2(15),
    Email VARCHAR2(100),
    CCCD VARCHAR2(20),
    DiaChi VARCHAR2(255),
    FOREIGN KEY (MaTK) REFERENCES TAIKHOAN(MaTK)
);


-- 6. Bảng XE
CREATE TABLE XE (
    MaXe VARCHAR2(10) PRIMARY KEY,
    TenXe VARCHAR2(50),
    BienSo VARCHAR2(20),
    SoCho NUMBER,
    HangXe VARCHAR2(50),
    NamSX NUMBER(4),
    TrangThai VARCHAR2(50),
    GiaThueNgay NUMBER(10,2)
);
ALTER TABLE XE ADD HinhAnh VARCHAR2(255);

-- 7. Bảng NHANVIEN
CREATE TABLE NHANVIEN (
    MaNV VARCHAR2(10) PRIMARY KEY,
    MaTK VARCHAR2(10),
    HoTen VARCHAR2(100),
    SDT VARCHAR2(15),
    Email VARCHAR2(100),
    ChucVu VARCHAR2(50),
    FOREIGN KEY (MaTK) REFERENCES TAIKHOAN(MaTK)
);

-- 8. Bảng HOPDONG
CREATE TABLE HOPDONG (
    MaHD VARCHAR2(10) PRIMARY KEY,
    MaKH VARCHAR2(10),
    MaNV VARCHAR2(10),
    NgayLap DATE,
    DiaChiGiao VARCHAR2(255),
    TongTien NUMBER(15,2),
    TrangThai VARCHAR2(50),
    FOREIGN KEY (MaKH) REFERENCES KHACHHANG(MaKH),
    FOREIGN KEY (MaNV) REFERENCES NHANVIEN(MaNV)
);

-- 9. Bảng CTHD (Chi tiết hợp đồng)
CREATE TABLE CTHD (
    MaHD VARCHAR2(10),
    MaXe VARCHAR2(10),
    NgayBatDau DATE,
    NgayKetThuc DATE,
    PRIMARY KEY (MaHD, MaXe),
    FOREIGN KEY (MaHD) REFERENCES HOPDONG(MaHD),
    FOREIGN KEY (MaXe) REFERENCES XE(MaXe)
);

-- 10. Bảng GIAONHANXE
CREATE TABLE GIAONHANXE (
    MaGiaoNhan VARCHAR2(10) PRIMARY KEY,
    MaHD VARCHAR2(10),
    MaXe VARCHAR2(10),
    MaNV VARCHAR2(10),
    TrangThaiXe VARCHAR2(100),
    GhiChu VARCHAR2(255),
    FOREIGN KEY (MaHD) REFERENCES HOPDONG(MaHD),
    FOREIGN KEY (MaXe) REFERENCES XE(MaXe),
    FOREIGN KEY (MaNV) REFERENCES NHANVIEN(MaNV)
);
ALTER TABLE GIAONHANXE ADD TrangThaiGN VARCHAR2(255) CHECK (TrangThaiGN IN ('Đã giao', 'Đã nhận về'));

-- 11. Bảng LICHSUCONGNO
CREATE TABLE LICHSUCONGNO (
    MaLichSu VARCHAR2(10) PRIMARY KEY,
    MaKH VARCHAR2(10),
    NgayGiaoDich DATE,
    LoaiGiaoDich VARCHAR2(15) CHECK (LoaiGiaoDich IN ('PHAT SINH', 'THANH TOAN')),
    SoTien NUMBER(10,2),
    GhiChu VARCHAR2(100),
    FOREIGN KEY (MaKH) REFERENCES KHACHHANG(MaKH)
);

-- 12. Bảng DANHGIA
CREATE TABLE DANHGIA (
    MaDG VARCHAR2(10) PRIMARY KEY,
    MaHD VARCHAR2(10),
    DiemSo NUMBER(1) CHECK (DiemSo BETWEEN 1 AND 5),
    BinhLuan VARCHAR2(500),
    NgayDanhGia DATE,
    FOREIGN KEY (MaHD) REFERENCES HOPDONG(MaHD)
);

-- 13. Bảng PHIEUBAODUONG
CREATE TABLE PHIEUBAODUONG (
    MaBD VARCHAR2(10) PRIMARY KEY,
    MaXe VARCHAR2(10),
    MaKH VARCHAR2(10) NULL, -- Cho phép NULL nếu công ty chịu chi phí
    NgayBD DATE,
    MaNV VARCHAR2(10),
    LoaiBD VARCHAR2(50) CHECK (LoaiBD IN ('Định Kỳ', 'Khách gây hư hại')),
    TongTienBD NUMBER(10,2),
    FOREIGN KEY (MaXe) REFERENCES XE(MaXe),
    FOREIGN KEY (MaKH) REFERENCES KHACHHANG(MaKH),
    FOREIGN KEY (MaNV) REFERENCES NHANVIEN(MaNV)
);


-- 15. Bảng DICHVUBD
CREATE TABLE DICHVUBD (
    MaDV VARCHAR2(10) PRIMARY KEY,
    TenDV VARCHAR2(50),
    GiaDV NUMBER(10,2)
);

-- 14. Bảng CHITIETBAODUONG
CREATE TABLE CHITIETBAODUONG (
    MaBD VARCHAR2(10),
    MaDV VARCHAR2(10),
    SoLuong NUMBER(10),
    PRIMARY KEY (MaBD, MaDV),
    FOREIGN KEY (MaBD) REFERENCES PHIEUBAODUONG(MaBD),
    FOREIGN KEY (MaDV) REFERENCES DICHVUBD(MaDV)
);

CREATE TABLE GIOHANG (
    MaGH VARCHAR2(10) PRIMARY KEY,
    MaKH VARCHAR2(10) NOT NULL,
    MaXe VARCHAR2(10) NOT NULL,
    NgayBatDau DATE NOT NULL,
    NgayKetThuc DATE NOT NULL,
    NgayThem DATE DEFAULT SYSDATE,
    FOREIGN KEY (MaKH) REFERENCES KHACHHANG(MaKH),
    FOREIGN KEY (MaXe) REFERENCES XE(MaXe)
);

ALTER TABLE KHACHHANG MODIFY TongTienNo NUMBER(15,2);
ALTER TABLE XE MODIFY GiaThueNgay NUMBER(15,2);
ALTER TABLE DICHVUBD MODIFY GiaDV NUMBER(15,2);
ALTER TABLE PHIEUBAODUONG MODIFY TongTienBD NUMBER(15,2);
ALTER TABLE LICHSUCONGNO MODIFY SoTien NUMBER(15,2);

-- 1. Tạo sequences cho các bảng
CREATE SEQUENCE seq_khachhang START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_taikhoan START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_nhanvien START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_xe START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_hopdong START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_giaonhanxe START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_lichsucongno START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_danhgia START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_phieubaoduong START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_dichvubd START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_vaitro START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_quyenhan START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE SEQ_GIOHANG  START WITH 1  INCREMENT BY 1  NOCACHE NOCYCLE;

-- 2. Tạo các trigger tương ứng để tạo mã
-- Trigger cho KHACHHANG
CREATE OR REPLACE TRIGGER trigger_taoma_khachhang 
BEFORE INSERT ON KHACHHANG 
FOR EACH ROW 
BEGIN
    IF :NEW.MaKH IS NULL THEN
        :NEW.MaKH := 'KH' || LPAD(seq_khachhang.NEXTVAL, 3, '0');
    END IF;
END;
/
-- Trigger cho TAIKHOAN
CREATE OR REPLACE TRIGGER trigger_taoma_taikhoan
BEFORE INSERT ON TAIKHOAN 
FOR EACH ROW 
BEGIN
    IF :NEW.MaTK IS NULL THEN
        :NEW.MaTK := 'TK' || LPAD(seq_taikhoan.NEXTVAL, 3, '0');
    END IF;
END;
/

-- Trigger cho NHANVIEN  
CREATE OR REPLACE TRIGGER trigger_taoma_nhanvien
BEFORE INSERT ON NHANVIEN 
FOR EACH ROW 
BEGIN
    IF :NEW.MaNV IS NULL THEN
        :NEW.MaNV := 'NV' || LPAD(seq_nhanvien.NEXTVAL, 3, '0');
    END IF;
END;
/

-- Trigger cho XE
CREATE OR REPLACE TRIGGER trigger_taoma_xe
BEFORE INSERT ON XE 
FOR EACH ROW 
BEGIN
    IF :NEW.MaXe IS NULL THEN
        :NEW.MaXe := 'XE' || LPAD(seq_xe.NEXTVAL, 3, '0');
    END IF;
END;
/

-- Trigger cho HOPDONG
CREATE OR REPLACE TRIGGER trigger_taoma_hopdong
BEFORE INSERT ON HOPDONG 
FOR EACH ROW 
BEGIN
    IF :NEW.MaHD IS NULL THEN
        :NEW.MaHD := 'HD' || LPAD(seq_hopdong.NEXTVAL, 3, '0');
    END IF;
END;
/
-- Trigger cho GIAONHANXE
CREATE OR REPLACE TRIGGER trigger_taoma_giaonhanxe
BEFORE INSERT ON GIAONHANXE 
FOR EACH ROW 
BEGIN
    IF :NEW.MaGiaoNhan IS NULL THEN
        :NEW.MaGiaoNhan := 'GN' || LPAD(seq_giaonhanxe.NEXTVAL, 3, '0');
    END IF;
END;
/

-- Trigger cho LICHSUCONGNO
CREATE OR REPLACE TRIGGER trigger_taoma_lichsucongno
BEFORE INSERT ON LICHSUCONGNO 
FOR EACH ROW 
BEGIN
    IF :NEW.MaLichSu IS NULL THEN
        :NEW.MaLichSu := 'LS' || LPAD(seq_lichsucongno.NEXTVAL, 3, '0');
    END IF;
END;
/

-- Trigger cho DANHGIA
CREATE OR REPLACE TRIGGER trigger_taoma_danhgia
BEFORE INSERT ON DANHGIA 
FOR EACH ROW 
BEGIN
    IF :NEW.MaDG IS NULL THEN
        :NEW.MaDG := 'DG' || LPAD(seq_danhgia.NEXTVAL, 3, '0');
    END IF;
END;
/

-- Trigger cho PHIEUBAODUONG
CREATE OR REPLACE TRIGGER trigger_taoma_phieubaoduong
BEFORE INSERT ON PHIEUBAODUONG 
FOR EACH ROW 
BEGIN
    IF :NEW.MaBD IS NULL THEN
        :NEW.MaBD := 'BD' || LPAD(seq_phieubaoduong.NEXTVAL, 3, '0');
    END IF;
END;
/

-- Trigger cho DICHVUBD
CREATE OR REPLACE TRIGGER trigger_taoma_dichvubd
BEFORE INSERT ON DICHVUBD 
FOR EACH ROW 
BEGIN
    IF :NEW.MaDV IS NULL THEN
        :NEW.MaDV := 'DV' || LPAD(seq_dichvubd.NEXTVAL, 3, '0');
    END IF;
END;
/

-- Trigger cho VAITRO
CREATE OR REPLACE TRIGGER trigger_taoma_vaitro
BEFORE INSERT ON VAITRO 
FOR EACH ROW 
BEGIN
    IF :NEW.MaVaiTro IS NULL THEN
        :NEW.MaVaiTro := 'VT' || LPAD(seq_vaitro.NEXTVAL, 3, '0');
    END IF;
END;
/

-- Trigger cho QUYENHAN
CREATE OR REPLACE TRIGGER trigger_taoma_quyenhan
BEFORE INSERT ON QUYENHAN 
FOR EACH ROW 
BEGIN
    IF :NEW.MaQuyen IS NULL THEN
        :NEW.MaQuyen := 'QH' || LPAD(seq_quyenhan.NEXTVAL, 3, '0');
    END IF;
END;
/
CREATE OR REPLACE TRIGGER trigger_taoma_giohang
BEFORE INSERT ON GIOHANG
FOR EACH ROW
BEGIN
    IF :NEW.MaGH IS NULL THEN
        SELECT 'GH' || LPAD(SEQ_GIOHANG.NEXTVAL, 3, '0')
        INTO :NEW.MaGH
        FROM DUAL;
    END IF;
END;
/

----------------------------------------------------------------------------------------
--- FUNCTION: HÀM TÍNH SỐ NGÀY THUÊ XE
    CREATE OR REPLACE FUNCTION TinhSoNgayThue(
        p_NgayBatDau IN DATE,
        p_NgayKetThuc IN DATE
    ) RETURN NUMBER IS
    BEGIN
        -- Tính số ngày thuê (làm tròn xuống để tránh số lẻ)
        RETURN TRUNC(p_NgayKetThuc) - TRUNC(p_NgayBatDau) + 1;
    END;
    /
--------------------
--------------------------------------------
--Trigger 1:	Trị giá của một hợp đồng bằng tổng thành tiền (Giá Xe*Số ngày thuê) của các xe thuộc chi tiết hợp đồng đó
--1. Khi thêm hoặc cập nhật HOPDONG

CREATE OR REPLACE TRIGGER tg_HOPDONG_insOrupd1
BEFORE INSERT  ON HOPDONG
FOR EACH ROW
DECLARE
    v_tongtien NUMBER(15,2);
BEGIN
    -- Kiểm tra khi thêm hợp đồng mới
    IF INSERTING AND :NEW.TongTien <> 0 THEN
        RAISE_APPLICATION_ERROR(-20002, 'KHÔNG ĐƯỢC THÊM HOPDONG VỚI TỔNG TIỀN KHÁC 0');
    END IF;
END;
/
------------------------------------------
--2. TRIGGER xử lý sửa CTHD thì TONGTIEN sẽ thay đổi theo.(GHI TÊN KHÁC ĐỂ THỂ HIỆN TƯỜNG MINH HƠN)
    CREATE OR REPLACE TRIGGER trg_CTHD_UpdateTongTien
    BEFORE INSERT OR DELETE OR UPDATE ON CTHD
    FOR EACH ROW
    DECLARE
        v_TienXe_Old NUMBER := 0;
        v_TienXe_New NUMBER := 0;
    BEGIN
        -- Xử lý khi xóa hoặc cập nhật (tính tiền cũ)
        IF DELETING OR (UPDATING AND :OLD.MaXe <> :NEW.MaXe OR   
                :OLD.NgayBatDau <> :NEW.NgayBatDau OR :OLD.NgayKetThuc <> :NEW.NgayKetThuc) THEN
            SELECT NVL(x.GiaThueNgay * TinhSoNgayThue(:OLD.NgayBatDau, :OLD.NgayKetThuc), 0)
            INTO v_TienXe_Old
            FROM XE x
            WHERE x.MaXe = :OLD.MaXe;
        
            -- Trừ tiền xe cũ khỏi hợp đồng
            UPDATE HOPDONG
            SET TongTien = NVL(TongTien, 0) - v_TienXe_Old
            WHERE MaHD = :OLD.MaHD;
        END IF;
        
        -- Xử lý khi thêm mới hoặc cập nhật (tính tiền mới)
        IF INSERTING OR (UPDATING AND 
        (:OLD.MaXe <> :NEW.MaXe OR 
        :OLD.NgayBatDau <> :NEW.NgayBatDau OR 
       :OLD.NgayKetThuc <> :NEW.NgayKetThuc)
        ) THEN
            SELECT NVL(x.GiaThueNgay * TinhSoNgayThue(:NEW.NgayBatDau, :NEW.NgayKetThuc), 0)
            INTO v_TienXe_New
            FROM XE x
            WHERE x.MaXe = :NEW.MaXe;
        
            -- Cộng tiền xe mới vào hợp đồng
            UPDATE HOPDONG
            SET TongTien = NVL(TongTien, 0) + v_TienXe_New
            WHERE MaHD = :NEW.MaHD;
        END IF;
    END;
    /
-------3. Trigger sửa GiaThueNgay ==> Xử lý TongTien hopDong sau khi sửa

CREATE OR REPLACE TRIGGER trg_XE_UpdateGiaThueNgay
BEFORE UPDATE OF GiaThueNgay ON XE
FOR EACH ROW
DECLARE
    v_TienXeCu NUMBER;
    v_TienXeMoi NUMBER;
BEGIN
    -- Duyệt qua tất cả hợp đồng có thuê xe này
    FOR contract IN (
        SELECT c.MaHD, c.NgayBatDau, c.NgayKetThuc
        FROM CTHD c
        WHERE c.MaXe = :OLD.MaXe
    ) LOOP
        -- Tính tiền thuê cũ và mới dựa trên ngày trong CTHD
        v_TienXeCu := :OLD.GiaThueNgay * TinhSoNgayThue(contract.NgayBatDau, contract.NgayKetThuc);
        v_TienXeMoi := :NEW.GiaThueNgay * TinhSoNgayThue(contract.NgayBatDau, contract.NgayKetThuc);
        
        -- Cập nhật tổng tiền hợp đồng
        UPDATE HOPDONG
        SET TongTien = NVL(TongTien, 0) - v_TienXeCu + v_TienXeMoi
        WHERE MaHD = contract.MaHD;
    END LOOP;
END;
/
--TRIGGER2: Tổng tiền bảo dưỡng bằng tổng thành tiền của GiaDV liên quan tới phiếu bảo dưỡng đó
--1.PHIEUBAODUONG
    CREATE OR REPLACE TRIGGER tg_PHIEUBAODUONG_insOrupd
    BEFORE INSERT ON PHIEUBAODUONG
    FOR EACH ROW
    BEGIN
        IF INSERTING AND :NEW.TongTienBD <> 0 THEN
            RAISE_APPLICATION_ERROR(-20003, 'KHÔNG ĐƯỢC THÊM PHIEUBAODUONG VỚI TỔNG TIỀN KHÁC 0');
        END IF;
    END;
    /
--2.CTBD
    --Thêm sửa xóa CTBD
    CREATE OR REPLACE TRIGGER trg_CHITIETBAODUONG_ins_upd_Del
    BEFORE INSERT OR DELETE OR UPDATE ON CHITIETBAODUONG
    FOR EACH ROW
    DECLARE
        v_TienDV_Old NUMBER := 0;
        v_TienDV_New NUMBER := 0;
    BEGIN
        -- Nếu DELETE hoặc UPDATE co thay doi dich vu hoac so luong
        IF DELETING OR (UPDATING AND :OLD.MaDV != :NEW.MaDV OR :OLD.SoLuong != :NEW.SoLuong)
         THEN
            SELECT NVL(GiaDV, 0)
            INTO v_TienDV_Old
            FROM DICHVUBD
            WHERE MaDV = :OLD.MaDV;
            
            -- Trừ tiền trong phiếu bảo dưỡng cũ (nhân với số lượng)
            UPDATE PHIEUBAODUONG
            SET TongTienBD = NVL(TongTienBD, 0) - (v_TienDV_Old * NVL(:OLD.SoLuong, 1))
            WHERE MaBD = :OLD.MaBD;
        END IF;

        -- Nếu INSERT hoặc UPDATE 
        IF INSERTING OR (UPDATING AND :OLD.MaDV != :NEW.MaDV OR :OLD.SoLuong != :NEW.SoLuong)
         THEN
            SELECT NVL(GiaDV, 0)
            INTO v_TienDV_New
            FROM DICHVUBD
            WHERE MaDV = :NEW.MaDV;
            
            -- Cộng tiền vào phiếu bảo dưỡng mới (nhân với số lượng)
            UPDATE PHIEUBAODUONG
            SET TongTienBD = NVL(TongTienBD, 0) + (v_TienDV_New * NVL(:NEW.SoLuong, 1))
            WHERE MaBD = :NEW.MaBD;
        END IF;
    END;
    /
--3.DICHVUBD
        --sửa giá DV
    CREATE OR REPLACE TRIGGER trg_DICHVUBD_UpdateGiaDV
    BEFORE UPDATE OF GiaDV ON DICHVUBD
    FOR EACH ROW
    BEGIN
        -- Cập nhật tất cả phiếu bảo dưỡng liên quan đến dịch vụ này
        FOR phieu IN (
            SELECT CTBD.MaBD, NVL(CTBD.SoLuong, 1) AS SoLuong
            FROM CHITIETBAODUONG CTBD
            WHERE CTBD.MaDV = :OLD.MaDV
        ) LOOP
            -- Cập nhật tổng tiền (tính cả số lượng)
            UPDATE PHIEUBAODUONG
            SET TongTienBD = NVL(TongTienBD, 0) - (:OLD.GiaDV * phieu.SoLuong) + (:NEW.GiaDV * phieu.SoLuong)
            WHERE MaBD = phieu.MaBD;
        END LOOP;
    END;
    /
    
---Trigger 3: TongCongNo của khách hàng = TongTienBD(Loại BD là khách gây hư hại) + SoTien(Phát sinh trong LSCN) - SoTien(Thanh toán trong LSCN)

---1. LICHSUCONGNO
CREATE OR REPLACE TRIGGER trg_Update_ins_del_LSCN
    AFTER INSERT OR DELETE OR UPDATE ON LICHSUCONGNO
    FOR EACH ROW
    DECLARE
    v_TongNo_KHCu NUMBER;
    v_TongNo_KHMoi NUMBER;
    v_NoSauCapNhat NUMBER;
    BEGIN
        -- Xử lý khi INSERT
        IF INSERTING THEN
            -- Kiểm tra trước khi cập nhật
            SELECT NVL(TongTienNo, 0) INTO v_TongNo_KHMoi
            FROM KHACHHANG
            WHERE MaKH = :NEW.MaKH;
            
            v_NoSauCapNhat := v_TongNo_KHMoi + 
                CASE WHEN :NEW.LoaiGiaoDich = 'PHAT SINH' THEN :NEW.SoTien 
                        WHEN :NEW.LoaiGiaoDich = 'THANH TOAN' THEN -:NEW.SoTien 
                        ELSE 0 END;
            
            IF v_NoSauCapNhat < 0 THEN
                RAISE_APPLICATION_ERROR(-20010, 
                    'Không thể thêm giao dịch: Công nợ sau sẽ âm (' || v_NoSauCapNhat || 
                    '). Tổng nợ hiện tại: ' || v_TongNo_KHMoi);
            ELSE
                UPDATE KHACHHANG
                SET TongTienNo = v_NoSauCapNhat
                WHERE MaKH = :NEW.MaKH;
            END IF;
        END IF;
        
        -- Xử lý khi DELETE
        IF DELETING THEN
            -- Kiểm tra trước khi cập nhật
            SELECT NVL(TongTienNo, 0) INTO v_TongNo_KHCu
            FROM KHACHHANG
            WHERE MaKH = :OLD.MaKH;
            
            v_NoSauCapNhat := v_TongNo_KHCu - 
                CASE WHEN :OLD.LoaiGiaoDich = 'PHAT SINH' THEN :OLD.SoTien 
                        WHEN :OLD.LoaiGiaoDich = 'THANH TOAN' THEN -:OLD.SoTien 
                        ELSE 0 END;
            
            IF v_NoSauCapNhat < 0 THEN
                RAISE_APPLICATION_ERROR(-20011, 
                    'Không thể xóa giao dịch: Công nợ sau sẽ âm (' || v_NoSauCapNhat || 
                    '). Tổng nợ hiện tại: ' || v_TongNo_KHCu);
            ELSE
                UPDATE KHACHHANG
                SET TongTienNo = v_NoSauCapNhat
                WHERE MaKH = :OLD.MaKH;
            END IF;
        END IF;

        -- Xử lý khi UPDATE và có thay đổi thực sự trên các trường quan trọng
        IF UPDATING AND ((:OLD.MaKH <> :NEW.MaKH) OR (:OLD.LoaiGiaoDich <> :NEW.LoaiGiaoDich) OR (:OLD.SoTien <> :NEW.SoTien)) THEN
            -- Xử lý khi UPDATE SoTien hoặc LoaiGiaoDich nhưng không thay đổi MaKH
            IF :OLD.MaKH = :NEW.MaKH THEN
                -- Kiểm tra trước khi cập nhật
                SELECT NVL(TongTienNo, 0) INTO v_TongNo_KHMoi
                FROM KHACHHANG
                WHERE MaKH = :NEW.MaKH;
                
                v_NoSauCapNhat := v_TongNo_KHMoi
                    - CASE WHEN :OLD.LoaiGiaoDich = 'PHAT SINH' THEN :OLD.SoTien 
                           WHEN :OLD.LoaiGiaoDich = 'THANH TOAN' THEN -:OLD.SoTien 
                           ELSE 0 END
                    + CASE WHEN :NEW.LoaiGiaoDich = 'PHAT SINH' THEN :NEW.SoTien 
                           WHEN :NEW.LoaiGiaoDich = 'THANH TOAN' THEN -:NEW.SoTien 
                           ELSE 0 END;
               
                IF v_NoSauCapNhat < 0 THEN
                    RAISE_APPLICATION_ERROR(-20012, 
                        'Không thể cập nhật: Công nợ sau sẽ âm (' || v_NoSauCapNhat || 
                        '). Tổng nợ hiện tại: ' || v_TongNo_KHMoi);
                ELSE
                    UPDATE KHACHHANG
                    SET TongTienNo = v_NoSauCapNhat
                    WHERE MaKH = :NEW.MaKH;
                END IF;
            ELSE
                -- Trường hợp thay đổi MaKH (chuyển công nợ)
                -- Kiểm tra KH cũ
                SELECT NVL(TongTienNo, 0) INTO v_TongNo_KHCu
                FROM KHACHHANG
                WHERE MaKH = :OLD.MaKH;
                
                -- Kiểm tra KH mới
                SELECT NVL(TongTienNo, 0) INTO v_TongNo_KHMoi
                FROM KHACHHANG
                WHERE MaKH = :NEW.MaKH;
                
                -- Tính toán công nợ mới cho cả 2 KH
                DECLARE
                    v_NoSauCapNhat_KHCu NUMBER := v_TongNo_KHCu - 
                        CASE WHEN :OLD.LoaiGiaoDich = 'PHAT SINH' THEN :OLD.SoTien 
                             WHEN :OLD.LoaiGiaoDich = 'THANH TOAN' THEN -:OLD.SoTien 
                             ELSE 0 END;
                    
                    v_NoSauCapNhat_KHMoi NUMBER := v_TongNo_KHMoi + 
                        CASE WHEN :NEW.LoaiGiaoDich = 'PHAT SINH' THEN :NEW.SoTien 
                             WHEN :NEW.LoaiGiaoDich = 'THANH TOAN' THEN -:NEW.SoTien 
                             ELSE 0 END;
                BEGIN
                    -- Kiểm tra công nợ âm cho cả 2 KH
                    IF v_NoSauCapNhat_KHCu < 0 OR v_NoSauCapNhat_KHMoi < 0 THEN
                        RAISE_APPLICATION_ERROR(-20013, 
                            'Không thể chuyển giao dịch: Công nợ sau sẽ âm. ' ||
                            'KH cũ: ' || v_NoSauCapNhat_KHCu || ', KH mới: ' || v_NoSauCapNhat_KHMoi);
                    ELSE
                        -- Cập nhật KH cũ
                        UPDATE KHACHHANG
                        SET TongTienNo = v_NoSauCapNhat_KHCu
                        WHERE MaKH = :OLD.MaKH;
                        
                        -- Cập nhật KH mới
                        UPDATE KHACHHANG
                        SET TongTienNo = v_NoSauCapNhat_KHMoi
                        WHERE MaKH = :NEW.MaKH;
                    END IF;
                END;
            END IF;
        END IF;
    END;
    /

--Thêm, xóa, sửa Phieubaoduong(LoaiBD, TongTienBD, MaKH) 

    CREATE OR REPLACE TRIGGER trg_Upd_Ins_Del_From_PBD
    AFTER INSERT OR DELETE OR UPDATE OF LoaiBD, TongTienBD, MaKH ON PHIEUBAODUONG
    FOR EACH ROW
    BEGIN
        IF INSERTING OR UPDATING THEN
            --Kiểm tra ràng buộc MaKH dựa trên LoaiBD
            IF :NEW.LoaiBD = 'Định Kỳ' AND :NEW.MaKH IS NOT NULL THEN
                RAISE_APPLICATION_ERROR(-20001, 'Phiếu bảo dưỡng Định kỳ không được gán MaKH');
            END IF;
            
            IF :NEW.LoaiBD = 'Khách gây hư hại' AND :NEW.MaKH IS NULL THEN
                RAISE_APPLICATION_ERROR(-20002, 'Phiếu bảo dưỡng của khách hàng phải có MaKH');
            END IF;
        END IF;
        -- UPDATE từ loại khác thành 'Khách gây hư hại' => Cộng tiền
        IF (UPDATING AND :OLD.LoaiBD <> 'Khách gây hư hại' AND :NEW.LoaiBD = 'Khách gây hư hại') THEN
            UPDATE KHACHHANG
            SET TongTienNo = NVL(TongTienNo, 0) + :NEW.TongTienBD
            WHERE MaKH = :NEW.MaKH;
        END IF;
    
        -- DELETE và UPDATE từ 'Khách gây hư hại' sang loại khác => Trừ tiền
        IF DELETING OR (UPDATING AND :OLD.LoaiBD = 'Khách gây hư hại' AND :NEW.LoaiBD <> 'Khách gây hư hại') THEN
            UPDATE KHACHHANG
            SET TongTienNo = NVL(TongTienNo, 0) - :OLD.TongTienBD
            WHERE MaKH = :OLD.MaKH;
        END IF;
    
        -- UPDATE số tiền bảo dưỡng nhưng vẫn là 'Khách gây hư hại' => Cập nhật tiền
        IF UPDATING AND :OLD.LoaiBD = 'Khách gây hư hại' AND :NEW.LoaiBD = 'Khách gây hư hại' 
           AND :OLD.TongTienBD <> :NEW.TongTienBD THEN
            UPDATE KHACHHANG
            SET TongTienNo = NVL(TongTienNo, 0) - :OLD.TongTienBD + :NEW.TongTienBD
            WHERE MaKH = :NEW.MaKH;
        END IF;
    
        -- UPDATE MaKH (chuyển nợ từ khách này sang khách khác)
        IF UPDATING AND :OLD.MaKH <> :NEW.MaKH THEN
            -- Trừ tiền ở khách cũ
            IF :OLD.LoaiBD = 'Khách gây hư hại' THEN
                UPDATE KHACHHANG
                SET TongTienNo = NVL(TongTienNo, 0) - :OLD.TongTienBD
                WHERE MaKH = :OLD.MaKH;
            END IF;
            -- Cộng tiền vào khách mới
            IF :NEW.LoaiBD = 'Khách gây hư hại' THEN
                UPDATE KHACHHANG
                SET TongTienNo = NVL(TongTienNo, 0) + :NEW.TongTienBD
                WHERE MaKH = :NEW.MaKH;
            END IF;
        END IF;
    END;
    /
---TRIGGER 4: KHÔNG ĐƯỢC THANH TOÁN SỐ TIỀN VƯỢT QUÁ CÔNG NỢ
    CREATE OR REPLACE TRIGGER trg_kiem_tra_thanhtoan
    BEFORE INSERT OR UPDATE OF SoTien ON LICHSUCONGNO
    FOR EACH ROW
    DECLARE
    v_TongTienNo NUMBER;
    BEGIN
        -- Lấy tổng tiền nợ hiện tại của khách hàng
        SELECT TongTienNo INTO v_TongTienNo
        FROM KHACHHANG
        WHERE MaKH = :NEW.MaKH;
    
        -- Kiểm tra nếu là giao dịch THANH TOAN thì số tiền thanh toán không được vượt quá nợ
        IF :NEW.LoaiGiaoDich = 'THANH TOAN' AND :NEW.SoTien > v_TongTienNo THEN
            RAISE_APPLICATION_ERROR(-20003, 'Số tiền thanh toán vượt quá tổng công nợ của khách hàng!');
        END IF;
    END;
   /

CREATE OR REPLACE TRIGGER trg_update_congno_khachhang
AFTER INSERT OR UPDATE OR DELETE ON HOPDONG
FOR EACH ROW
BEGIN
    -- Khi thêm mới hoặc cập nhật hợp đồng
    IF INSERTING THEN
        UPDATE KHACHHANG
        SET TongTienNo = TongTienNo + :NEW.TongTien
        WHERE MaKH = :NEW.MaKH;
    ELSIF UPDATING THEN
        -- Kiểm tra nếu khách hàng thay đổi
        IF :NEW.MaKH <> :OLD.MaKH THEN
            -- Giảm công nợ của khách hàng cũ
            UPDATE KHACHHANG
            SET TongTienNo = TongTienNo - :OLD.TongTien
            WHERE MaKH = :OLD.MaKH;
            
            -- Tăng công nợ của khách hàng mới
            UPDATE KHACHHANG
            SET TongTienNo = TongTienNo + :NEW.TongTien
            WHERE MaKH = :NEW.MaKH;
        ELSE
            -- Trường hợp chỉ thay đổi tổng tiền (code gốc của bạn)
            UPDATE KHACHHANG
            SET TongTienNo = TongTienNo + (:NEW.TongTien - :OLD.TongTien)
            WHERE MaKH = :NEW.MaKH;
        END IF;
    ELSIF DELETING THEN
        UPDATE KHACHHANG
        SET TongTienNo = TongTienNo - :OLD.TongTien
        WHERE MaKH = :OLD.MaKH;
    END IF;
END;
/

--/PROCEDURE
-- Tạo procedure đăng ký khách hàng
CREATE OR REPLACE PROCEDURE sp_DangKyKhachHang(
    p_HoTen IN VARCHAR2,
    p_SDT IN VARCHAR2, 
    p_Email IN VARCHAR2,
    p_CCCD IN VARCHAR2,
    p_DiaChi IN VARCHAR2,
    p_TenDangNhap IN VARCHAR2,
    p_MatKhau IN VARCHAR2,
    p_Message OUT VARCHAR2
)
IS
    v_count NUMBER;
    v_MaTK VARCHAR2(10);
    v_MaKH VARCHAR2(10);
    v_VaiTro VARCHAR2(10);
    --QUY ƯỚC EMAIL, SDT, VÀ CCCD NHƯ SAU:
    v_email_pattern VARCHAR2(100) := '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$';
    v_phone_pattern VARCHAR2(20) := '^0[0-9]{9}$';
    v_cccd_pattern VARCHAR2(20) := '^[0-9]{12}$';

BEGIN
    -- [1] VALIDATE DỮ LIỆU ĐẦU VÀO
    -- Kiểm tra định dạng email
    IF NOT REGEXP_LIKE(p_Email, v_email_pattern) THEN
        p_Message := 'Email không hợp lệ';
        RETURN;
    END IF;
    
    -- Kiểm tra email tồn tại
    SELECT COUNT(*) INTO v_count 
    FROM KHACHHANG 
    WHERE Email = p_Email;
    
    IF v_count > 0 THEN
        p_Message := 'Email đã được đăng ký';
        RETURN;
    END IF;
    
    -- Kiểm tra CCCD
    IF NOT REGEXP_LIKE(p_CCCD, v_cccd_pattern) THEN
        p_Message := 'CCCD không hợp lệ (phải đủ 12 số)';
        RETURN;
    END IF;
    
    -- Kiểm tra CCCD tồn tại
    SELECT COUNT(*) INTO v_count 
    FROM KHACHHANG 
    WHERE CCCD = p_CCCD;
    IF v_count > 0 THEN
        p_Message := 'CCCD đã được đăng ký';
        RETURN;
    END IF;
    
    -- Kiểm tra SĐT
    IF NOT REGEXP_LIKE(p_SDT, v_phone_pattern) THEN
        p_Message := 'Số điện thoại không hợp lệ (phải bắt đầu bằng 0 và đủ 10 số)';
        RETURN;
    END IF;
    
    -- Kiểm tra mật khẩu
    IF LENGTH(p_MatKhau) < 8 
        OR NOT REGEXP_LIKE(p_MatKhau, '[A-Z]') 
        OR NOT REGEXP_LIKE(p_MatKhau, '[a-z]')
        OR NOT REGEXP_LIKE(p_MatKhau, '[0-9]') THEN
        p_Message := 'Mật khẩu phải có ít nhất 8 ký tự, chứa chữ hoa, chữ thường và số';
        RETURN;
    END IF;
    
    -- Kiểm tra tên đăng nhập tồn tại
    SELECT COUNT(*) INTO v_count 
    FROM TAIKHOAN 
    WHERE TenDangNhap = p_TenDangNhap;
    IF v_count > 0 THEN
        p_Message := 'Tên đăng nhập đã tồn tại';
        RETURN;
    END IF;
    --Lấy mã vai trò của khách hàng
    SELECT MaVaiTro INTO v_VaiTro
    FROM VAITRO
    WHERE TenVaiTro = 'Khách hàng';
   
    -- [2] TẠO TÀI KHOẢN MỚI
    -- Thêm vào bảng TAIKHOAN (trigger sẽ tự tạo MaTK)
    INSERT INTO TAIKHOAN(MaVaiTro, TenDangNhap, MatKhau, TrangThai)
    VALUES (v_VaiTro, p_TenDangNhap, p_MatKhau, 'Hoạt động')
    RETURNING MaTK INTO v_MaTK; -- Lấy mã TK vừa được tạo bởi trigger
    
    -- Thêm vào bảng KHACHHANG (trigger sẽ tự tạo MaKH)
    INSERT INTO KHACHHANG(MaTK, HoTen, SDT, Email, CCCD, DiaChi, TongTienNo)
    VALUES (v_MaTK, p_HoTen, p_SDT, p_Email, p_CCCD, p_DiaChi, 0)
    RETURNING MaKH INTO v_MaKH; -- Lấy mã KH vừa được tạo bởi trigger
    
    COMMIT;

    p_Message := 'Đăng ký thành công! Mã khách hàng của bạn là: ' || v_MaKH || ', Mã tài khoản: ' || v_MaTK;

EXCEPTION
     WHEN NO_DATA_FOUND THEN
        p_Message := 'Không tìm thấy vai trò khách hàng';
        ROLLBACK;
    WHEN OTHERS THEN
        ROLLBACK;
        p_Message := 'Lỗi: ' || SQLERRM;
END;
/

---==============================

CREATE OR REPLACE PROCEDURE sp_BaoCaoDoanhThuNam(
    p_Nam IN NUMBER,
    p_Result OUT SYS_REFCURSOR
)
IS
    v_NamHienTai NUMBER := EXTRACT(YEAR FROM SYSDATE);
BEGIN
    IF p_Nam IS NULL OR p_Nam < 1900 OR p_Nam > v_NamHienTai THEN
        RAISE_APPLICATION_ERROR(-20001, 'Năm nhập vào không hợp lệ.');
    END IF;
    
    -- Mở cursor để trả kết quả về
    OPEN p_Result FOR
        SELECT 
            t.Thang,
            NVL(h.TongDoanhThu, 0) as TongDoanhThu
        FROM 
            (SELECT LEVEL as Thang FROM DUAL CONNECT BY LEVEL <= 12) t
        LEFT JOIN 
            (SELECT 
                EXTRACT(MONTH FROM NgayLap) as Thang,
                SUM(TongTien) as TongDoanhThu
             FROM HOPDONG
             WHERE EXTRACT(YEAR FROM NgayLap) = p_Nam
             AND TrangThai <> 'Chờ xác nhận'
             GROUP BY EXTRACT(MONTH FROM NgayLap)) h
        ON t.Thang = h.Thang
        ORDER BY t.Thang;
EXCEPTION
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20003, 'Lỗi: ' || SQLERRM);
END;
/

-- 2. Báo cáo doanh thu theo khách hàng - điều chỉnh để trả về cursor
CREATE OR REPLACE PROCEDURE sp_BaoCaoDoanhThuKhachHang(
    p_Nam IN NUMBER,
    p_Result OUT SYS_REFCURSOR
)
IS
    v_NamHienTai NUMBER := EXTRACT(YEAR FROM SYSDATE);
BEGIN
    IF p_Nam IS NULL OR p_Nam < 1900 OR p_Nam > v_NamHienTai THEN
        RAISE_APPLICATION_ERROR(-20001, 'Năm nhập vào không hợp lệ.');
    END IF;
    
    -- Mở cursor để trả kết quả về
    OPEN p_Result FOR
        SELECT 
            kh.MaKH,
            kh.HoTen,
            COUNT(hd.MaHD) as SoHopDong,
            SUM(hd.TongTien) as TongDoanhThu
        FROM KHACHHANG kh
        JOIN HOPDONG hd ON kh.MaKH = hd.MaKH
        WHERE EXTRACT(YEAR FROM hd.NgayLap) = p_Nam
          AND hd.TrangThai <> 'Chờ xác nhận'
        GROUP BY kh.MaKH, kh.HoTen
        ORDER BY TongDoanhThu DESC;
EXCEPTION
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20003, 'Lỗi: ' || SQLERRM);
END;
/
-- 3. Báo cáo doanh thu theo xe
CREATE OR REPLACE PROCEDURE sp_BaoCaoDoanhThuXe(
    p_Nam IN NUMBER,
    p_Result OUT SYS_REFCURSOR
)
IS
    v_NamHienTai NUMBER := EXTRACT(YEAR FROM SYSDATE);
BEGIN
    IF p_Nam IS NULL OR p_Nam < 1900 OR p_Nam > v_NamHienTai THEN
        RAISE_APPLICATION_ERROR(-20001, 'Năm nhập vào không hợp lệ.');
    END IF;
    
    -- Mở cursor để trả kết quả về
    OPEN p_Result FOR
        SELECT 
            x.MaXe,
            x.TenXe,
            x.BienSo,
            COUNT(ct.MaXe) as SoLuotThue,
            SUM((TRUNC(ct.NgayKetThuc) - TRUNC(ct.NgayBatDau) + 1) * x.GiaThueNgay) as DoanhThu
        FROM XE x
        JOIN CTHD ct ON x.MaXe = ct.MaXe
        JOIN HOPDONG hd ON ct.MaHD = hd.MaHD
        WHERE EXTRACT(YEAR FROM hd.NgayLap) = p_Nam
          AND hd.TrangThai <> 'Chờ xác nhận'
        GROUP BY x.MaXe, x.TenXe, x.BienSo
        ORDER BY DoanhThu DESC;
EXCEPTION
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20003, 'Lỗi: ' || SQLERRM);
END;
/


-- 4. Procedure tổng quan hệ thống
CREATE OR REPLACE PROCEDURE sp_ThongKeTongQuan(
    p_TongSoXe OUT NUMBER,
    p_TongSoKhachHang OUT NUMBER,
    p_TongSoHopDong OUT NUMBER,
    p_TongDoanhThu OUT NUMBER
)
IS
BEGIN
    -- Tổng số xe
    SELECT COUNT(*) INTO p_TongSoXe FROM XE;
    
    -- Tổng số khách hàng
    SELECT COUNT(*) INTO p_TongSoKhachHang FROM KHACHHANG;
    
    -- Tổng số hợp đồng
    SELECT COUNT(*) INTO p_TongSoHopDong FROM HOPDONG;
    
    -- Tổng doanh thu
    SELECT NVL(SUM(TongTien), 0) INTO p_TongDoanhThu 
    FROM HOPDONG 
    WHERE TrangThai = 'Hoàn thành';
EXCEPTION
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20003, 'Lỗi: ' || SQLERRM);
END;
/

CREATE OR REPLACE FUNCTION IS_CAR_IN_CONTRACT(p_MaXe IN VARCHAR2) 
RETURN BOOLEAN IS
    v_count NUMBER := 0;
    v_current_date DATE := SYSDATE;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM CTHD ct
    WHERE ct.MaXe = p_MaXe
    AND TRUNC(SYSDATE) BETWEEN TRUNC(ct.NgayBatDau) AND TRUNC(ct.NgayKetThuc);
    
    RETURN (v_count > 0);
END;
/

-- Hàm kiểm tra xem xe có đang bảo dưỡng không
CREATE OR REPLACE FUNCTION IS_CAR_IN_MAINTENANCE(p_MaXe IN VARCHAR2) 
RETURN BOOLEAN IS
    v_count NUMBER := 0;
    v_current_date DATE := SYSDATE;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM PHIEUBAODUONG bd
    WHERE bd.MaXe = p_MaXe
    AND TRUNC(bd.NgayBD) = TRUNC(v_current_date);
    
    RETURN (v_count > 0);
END;
/
--===========================================


--TRIGGER: 6.Ngày bắt đầu thuê không thể sau ngày kết thúc. 
CREATE OR REPLACE TRIGGER trg_check_bd_kt
BEFORE INSERT OR UPDATE ON CTHD
FOR EACH ROW
BEGIN
    IF :NEW.NgayBatDau > :NEW.NgayKetThuc THEN
        RAISE_APPLICATION_ERROR(-20050, 'Ngày bắt đầu thuê xe không thể sau ngày kết thúc.');
    END IF;
END;
/
---/===============TRIGGER XỬ  LÝ TRẠNG THÁI XE========================/
-- COMPOUND TRIGGER cho XE (Gộp RBTV1 và RBTV2) 
--Trigger: Xe chỉ có thể "Sẵn sàng" khi không trong hợp đồng và không bảo dưỡng 
--Xe phải có trạng thái "Đang thuê" khi đang trong hợp đồng
CREATE OR REPLACE TRIGGER TRG_XE_UPDATE_COMPOUND
FOR UPDATE OF TrangThai ON XE
COMPOUND TRIGGER
    -- Biến cho toàn bộ trigger
    v_in_contract BOOLEAN;
    v_in_maintenance BOOLEAN;

    -- Phần BEFORE EACH ROW
    BEFORE EACH ROW IS
    BEGIN
        -- RBTV1: Xe chỉ có thể "Sẵn sàng" khi không trong hợp đồng và không bảo dưỡng
        IF :NEW.TrangThai = 'Sẵn sàng' THEN
            v_in_contract := IS_CAR_IN_CONTRACT(:NEW.MaXe);
            v_in_maintenance := IS_CAR_IN_MAINTENANCE(:NEW.MaXe);
            
            IF v_in_contract OR v_in_maintenance THEN
                RAISE_APPLICATION_ERROR(-20001, 'Xe chỉ có thể "Sẵn sàng" khi không trong hợp đồng và không bảo dưỡng');
            END IF;
        END IF;
        
        -- RBTV2: Xe phải có trạng thái "Đang thuê" khi đang trong hợp đồng
        -- Nếu trạng thái mới không phải "Đang thuê"
        IF :NEW.TrangThai <> 'Đang thuê' THEN
            v_in_contract := IS_CAR_IN_CONTRACT(:NEW.MaXe);
            
            -- Kiểm tra xe có đang trong hợp đồng không
            IF v_in_contract THEN
                RAISE_APPLICATION_ERROR(-20002, 'Vi phạm RBTV2: Xe phải có trạng thái "Đang thuê" khi đang trong hợp đồng');
            END IF;
        END IF;
    END BEFORE EACH ROW;
END TRG_XE_UPDATE_COMPOUND;
/

--Cập nhật trạng thái cho xe tương ứng khi xóa PHIEUBAODUONG
-- COMPOUND TRIGGER cho XÓA PHIEUBAODUONG
CREATE OR REPLACE TRIGGER TRG_PHIEUBAODUONG_DELETE_COMPOUND
FOR DELETE ON PHIEUBAODUONG
COMPOUND TRIGGER
    -- Khai báo biến cho trigger
    TYPE car_check_rec IS RECORD (
        MaXe XE.MaXe%TYPE,
        maintenance_date DATE
    );
    --danh sách các xe có phiếu bảo dưỡng bị xóa hôm nay.
    TYPE car_check_list IS TABLE OF car_check_rec INDEX BY BINARY_INTEGER;
    cars_to_check car_check_list;
    idx INTEGER := 0;
    v_today DATE;
    
    -- BEFORE STATEMENT
    BEFORE STATEMENT IS
    BEGIN
        v_today := TRUNC(SYSDATE);
    END BEFORE STATEMENT;
    
    -- AFTER EACH ROW
    AFTER EACH ROW IS
        v_old_maintenance_date DATE := TRUNC(:OLD.NgayBD);
    BEGIN
        -- Chỉ quan tâm đến việc xóa phiếu bảo dưỡng của ngày hôm nay
        IF v_old_maintenance_date = v_today THEN
            idx := idx + 1;
            cars_to_check(idx).MaXe := :OLD.MaXe;
            cars_to_check(idx).maintenance_date := v_old_maintenance_date;
        END IF;
    END AFTER EACH ROW;
    
    -- AFTER STATEMENT
    AFTER STATEMENT IS
        v_car_status VARCHAR2(50);
        v_in_contract BOOLEAN;
        v_other_maintenance_count NUMBER;
    BEGIN
        -- Xử lý từng xe cần kiểm tra
        FOR i IN 1..idx LOOP
            -- Lấy trạng thái hiện tại của xe
            SELECT TrangThai INTO v_car_status
            FROM XE
            WHERE MaXe = cars_to_check(i).MaXe;
            
            -- Nếu xe đang ở trạng thái "Bảo dưỡng", cần kiểm tra thêm
            IF v_car_status = 'Bảo dưỡng' THEN
                -- Kiểm tra xem còn phiếu bảo dưỡng nào khác không
                SELECT COUNT(*) INTO v_other_maintenance_count
                FROM PHIEUBAODUONG
                WHERE MaXe = cars_to_check(i).MaXe
                AND TRUNC(NgayBD) = v_today;
                
                -- Nếu không còn phiếu bảo dưỡng nào khác
                IF v_other_maintenance_count = 0 THEN
                    -- Kiểm tra xe có đang trong hợp đồng thuê không
                    v_in_contract := IS_CAR_IN_CONTRACT(cars_to_check(i).MaXe);
                    
                    -- Cập nhật trạng thái xe dựa vào điều kiện hợp đồng
                    IF v_in_contract THEN
                        UPDATE XE
                        SET TrangThai = 'Đang thuê'
                        WHERE MaXe = cars_to_check(i).MaXe;
                    ELSE
                        UPDATE XE
                        SET TrangThai = 'Sẵn sàng'
                        WHERE MaXe = cars_to_check(i).MaXe;
                    END IF;
                END IF;
            END IF;
        END LOOP;
    END AFTER STATEMENT;
END TRG_PHIEUBAODUONG_DELETE_COMPOUND;
/

--TRIGGER UPDATE PHIEUBAODUONG
--Ngăn chặn 2 PHIEUBAODUONG cho 1 xe cùng 1 ngày
CREATE OR REPLACE TRIGGER TRG_PHIEUBAODUONG_CHECK_DUPLICATE
FOR INSERT OR UPDATE OF MaXe, NgayBD ON PHIEUBAODUONG
COMPOUND TRIGGER
    -- Cấu trúc lưu thông tin kiểm tra
    TYPE check_rec IS RECORD (
        MaXe VARCHAR2(10),
        NgayBD DATE
    );
    
    TYPE check_list IS TABLE OF check_rec INDEX BY BINARY_INTEGER;
    cars_to_check check_list;
    idx INTEGER := 0;
    
    -- BEFORE STATEMENT
    BEFORE STATEMENT IS
    BEGIN
        -- Reset các biến
        idx := 0;
    END BEFORE STATEMENT;
    
    -- BEFORE EACH ROW
    BEFORE EACH ROW IS
    BEGIN
        -- Chỉ lưu thông tin, không truy vấn gì cả
        idx := idx + 1;
        cars_to_check(idx).MaXe := :NEW.MaXe;
        cars_to_check(idx).NgayBD := TRUNC(:NEW.NgayBD);
    END BEFORE EACH ROW;
    
    -- AFTER STATEMENT
    AFTER STATEMENT IS
        TYPE count_rec IS RECORD (
            MaXe VARCHAR2(10),
            NgayBD DATE,
            cnt NUMBER
        );
        
        TYPE count_list IS TABLE OF count_rec INDEX BY BINARY_INTEGER;
        duplicate_check count_list;
        dup_idx INTEGER := 0;
        
        v_found BOOLEAN;
        v_count NUMBER;
    BEGIN
        -- Kiểm tra xem có xe nào được lên lịch bảo dưỡng nhiều lần trong cùng ngày không
        FOR i IN 1..idx LOOP
            -- Đếm số lần xuất hiện của cặp (MaXe, NgayBD)
            v_found := FALSE;
            
            -- Kiểm tra trong mảng tạm đã có cặp này chưa
            FOR j IN 1..dup_idx LOOP
                IF duplicate_check(j).MaXe = cars_to_check(i).MaXe AND
                   duplicate_check(j).NgayBD = cars_to_check(i).NgayBD THEN
                   
                    duplicate_check(j).cnt := duplicate_check(j).cnt + 1;
                    v_found := TRUE;
                    EXIT;
                END IF;
            END LOOP;
            
            -- Nếu chưa có, thêm mới vào mảng
            IF NOT v_found THEN
                dup_idx := dup_idx + 1;
                duplicate_check(dup_idx).MaXe := cars_to_check(i).MaXe;
                duplicate_check(dup_idx).NgayBD := cars_to_check(i).NgayBD;
                duplicate_check(dup_idx).cnt := 1;
            END IF;
        END LOOP;
        
        -- Kiểm tra từng xe trong database
        FOR i IN 1..dup_idx LOOP
            -- Đếm số phiếu trong database
            SELECT COUNT(*)
            INTO v_count
            FROM PHIEUBAODUONG
            WHERE MaXe = duplicate_check(i).MaXe
            AND TRUNC(NgayBD) = duplicate_check(i).NgayBD;
            
            -- Nếu có nhiều hơn 1 phiếu cho cùng xe trong cùng ngày, báo lỗi
            IF v_count > 1 THEN
                RAISE_APPLICATION_ERROR(-20050, 'Xe ' || duplicate_check(i).MaXe || 
                                      ' đã có phiếu bảo dưỡng khác trong ngày ' || 
                                      TO_CHAR(duplicate_check(i).NgayBD, 'DD/MM/YYYY') || 
                                      '. Vui lòng gộp các công việc vào cùng phiếu.');
            END IF;
        END LOOP;
    END AFTER STATEMENT;
END TRG_PHIEUBAODUONG_CHECK_DUPLICATE;
/


--=TRIGGER xử lý khi insert PHIEUBAODUONG
CREATE OR REPLACE TRIGGER TRG_PHIEUBAODUONG_INSERT_COMPOUND
FOR INSERT ON PHIEUBAODUONG
COMPOUND TRIGGER
    -- Biến lưu trữ
    v_today DATE;
    
    TYPE check_rec IS RECORD (
        MaXe VARCHAR2(10),
        NgayBD DATE
    );
    
    TYPE check_list IS TABLE OF check_rec INDEX BY BINARY_INTEGER;
    cars_to_check check_list;
    idx INTEGER := 0;
    
    -- BEFORE STATEMENT
    BEFORE STATEMENT IS
    BEGIN
        v_today := TRUNC(SYSDATE);
    END BEFORE STATEMENT;
    
    -- BEFORE EACH ROW
    BEFORE EACH ROW IS
        v_car_status VARCHAR2(50);
        v_contract_count NUMBER;
    BEGIN
        -- Lưu thông tin để xử lý sau
        idx := idx + 1;
        cars_to_check(idx).MaXe := :NEW.MaXe;
        cars_to_check(idx).NgayBD := TRUNC(:NEW.NgayBD);
        
        -- QUAN TRỌNG: Kiểm tra xem xe có đang được thuê vào ngày bảo dưỡng không
        -- Thay vì chỉ kiểm tra trạng thái hiện tại
        SELECT COUNT(*) INTO v_contract_count
        FROM CTHD
        WHERE MaXe = :NEW.MaXe
        AND TRUNC(:NEW.NgayBD) BETWEEN NgayBatDau AND NgayKetThuc;
        
        IF v_contract_count > 0 THEN
            RAISE_APPLICATION_ERROR(-20017, 'Không thể bảo dưỡng xe ' || :NEW.MaXe || 
                                  ' vào ngày ' || TO_CHAR(TRUNC(:NEW.NgayBD), 'DD/MM/YYYY') || 
                                  ' vì xe đã được thuê trong thời gian này.');
        END IF;
    END BEFORE EACH ROW;
    
    -- AFTER EACH ROW
    AFTER EACH ROW IS
    BEGIN
        -- Nếu phiếu bảo dưỡng cho ngày hôm nay, cập nhật trạng thái xe thành "Bảo dưỡng"
        IF TRUNC(:NEW.NgayBD) = v_today THEN
            UPDATE XE
            SET TrangThai = 'Bảo dưỡng'
            WHERE MaXe = :NEW.MaXe;
            
        END IF;
    END AFTER EACH ROW;
END TRG_PHIEUBAODUONG_INSERT_COMPOUND;
/


CREATE OR REPLACE TRIGGER TRG_PHIEUBAODUONG_UPDATE_COMPOUND
FOR UPDATE OF MaXe, NgayBD ON PHIEUBAODUONG
COMPOUND TRIGGER
    -- Biến lưu trữ
    v_today DATE;
    
    TYPE update_rec IS RECORD (
        new_MaXe XE.MaXe%TYPE,
        old_MaXe XE.MaXe%TYPE,
        old_NgayBD DATE,
        new_NgayBD DATE,
        MaBD PHIEUBAODUONG.MaBD%TYPE
    );
    
    TYPE update_list IS TABLE OF update_rec INDEX BY BINARY_INTEGER;
    updates_to_check update_list;
    idx INTEGER := 0;
    
    -- BEFORE STATEMENT
    BEFORE STATEMENT IS
    BEGIN
        v_today := TRUNC(SYSDATE);
    END BEFORE STATEMENT;
    
    -- BEFORE EACH ROW
    BEFORE EACH ROW IS
        v_contract_count NUMBER;
    BEGIN
        -- QUAN TRỌNG: Kiểm tra xe mới KHÔNG được thuê vào ngày bảo dưỡng mới
        IF :NEW.MaXe <> :OLD.MaXe OR TRUNC(:NEW.NgayBD) <> TRUNC(:OLD.NgayBD) THEN
            -- Kiểm tra xem xe mới có được thuê vào ngày mới không
            SELECT COUNT(*) INTO v_contract_count
            FROM CTHD
            WHERE MaXe = :NEW.MaXe
            AND TRUNC(:NEW.NgayBD) BETWEEN NgayBatDau AND NgayKetThuc;
            
            IF v_contract_count > 0 THEN
                RAISE_APPLICATION_ERROR(-20017, 'Không thể bảo dưỡng xe ' || :NEW.MaXe || 
                                      ' vào ngày ' || TO_CHAR(TRUNC(:NEW.NgayBD), 'DD/MM/YYYY') || 
                                      ' vì xe đã được thuê trong thời gian này.');
            END IF;
        END IF;
    END BEFORE EACH ROW;
    
    -- AFTER EACH ROW
    AFTER EACH ROW IS
    BEGIN
        -- Chỉ lưu thông tin, không thực hiện truy vấn để tránh mutating table
        idx := idx + 1;
        updates_to_check(idx).new_MaXe := :NEW.MaXe;
        updates_to_check(idx).old_MaXe := :OLD.MaXe;
        updates_to_check(idx).old_NgayBD := TRUNC(:OLD.NgayBD);
        updates_to_check(idx).new_NgayBD := TRUNC(:NEW.NgayBD);
        updates_to_check(idx).MaBD := :OLD.MaBD;
    END AFTER EACH ROW;
    
    -- AFTER STATEMENT
    AFTER STATEMENT IS
        TYPE car_rec IS RECORD (
            MaXe XE.MaXe%TYPE,
            new_status VARCHAR2(50)
        );
        TYPE car_list IS TABLE OF car_rec INDEX BY BINARY_INTEGER;
        cars_to_update car_list;
        cars_idx INTEGER := 0;
        
        v_maintenance_count NUMBER;
        v_old_date_is_today BOOLEAN;
        v_new_date_is_today BOOLEAN;
    BEGIN
        -- Xử lý từng bản ghi cập nhật
        FOR i IN 1..idx LOOP
            v_old_date_is_today := (updates_to_check(i).old_NgayBD = v_today);
            v_new_date_is_today := (updates_to_check(i).new_NgayBD = v_today);
            
            -- Trường hợp 1: Đổi xe
            IF updates_to_check(i).new_MaXe <> updates_to_check(i).old_MaXe THEN
                -- Nếu ngày cũ là hôm nay, kiểm tra xe cũ còn phiếu bảo dưỡng nào khác không
                IF v_old_date_is_today THEN
                    SELECT COUNT(*) INTO v_maintenance_count
                    FROM PHIEUBAODUONG
                    WHERE MaXe = updates_to_check(i).old_MaXe
                    AND TRUNC(NgayBD) = v_today
                    AND MaBD <> updates_to_check(i).MaBD;
                    
                    -- Nếu không còn phiếu bảo dưỡng nào khác
                    IF v_maintenance_count = 0 THEN
                        cars_idx := cars_idx + 1;
                        cars_to_update(cars_idx).MaXe := updates_to_check(i).old_MaXe;
                        cars_to_update(cars_idx).new_status := 'Sẵn sàng';
                    END IF;
                END IF;
                
                -- Nếu ngày mới là hôm nay, xử lý xe mới
                IF v_new_date_is_today THEN
                    cars_idx := cars_idx + 1;
                    cars_to_update(cars_idx).MaXe := updates_to_check(i).new_MaXe;
                    cars_to_update(cars_idx).new_status := 'Bảo dưỡng';
                END IF;
                
            -- Trường hợp 2: Chỉ đổi ngày - cùng một xe
            ELSIF updates_to_check(i).new_NgayBD <> updates_to_check(i).old_NgayBD THEN
                -- Nếu chuyển từ hôm nay sang ngày khác = hoàn thành bảo dưỡng
                IF v_old_date_is_today AND NOT v_new_date_is_today THEN
                    SELECT COUNT(*) INTO v_maintenance_count
                    FROM PHIEUBAODUONG
                    WHERE MaXe = updates_to_check(i).old_MaXe
                    AND TRUNC(NgayBD) = v_today
                    AND MaBD <> updates_to_check(i).MaBD;
                    
                    -- Nếu không còn phiếu bảo dưỡng nào khác
                    IF v_maintenance_count = 0 THEN
                        cars_idx := cars_idx + 1;
                        cars_to_update(cars_idx).MaXe := updates_to_check(i).new_MaXe;
                        cars_to_update(cars_idx).new_status := 'Sẵn sàng';
                    END IF;
                -- Nếu chuyển từ ngày khác sang hôm nay = bắt đầu bảo dưỡng
                ELSIF NOT v_old_date_is_today AND v_new_date_is_today THEN
                    cars_idx := cars_idx + 1;
                    cars_to_update(cars_idx).MaXe := updates_to_check(i).new_MaXe;
                    cars_to_update(cars_idx).new_status := 'Bảo dưỡng';
                END IF;
            END IF;
        END LOOP;
        
        -- Cập nhật trạng thái xe
        FOR i IN 1..cars_idx LOOP
            UPDATE XE
            SET TrangThai = cars_to_update(i).new_status
            WHERE MaXe = cars_to_update(i).MaXe;
           
        END LOOP;
        
    END AFTER STATEMENT;
END TRG_PHIEUBAODUONG_UPDATE_COMPOUND;
/

-- COMPOUND TRIGGER cho CTHD DELETE (RBTV4)
--Cập nhật trạng thái cho xe tương ứng khi Delete CTHD
CREATE OR REPLACE TRIGGER TRG_CTHD_DELETE_COMPOUND
FOR DELETE ON CTHD
COMPOUND TRIGGER
    -- Khai báo biến cho toàn bộ TRIGGER
    TYPE car_list_type IS TABLE OF XE.MaXe%TYPE INDEX BY BINARY_INTEGER;
    deleted_car_list car_list_type;
    idx INTEGER := 0;

    -- AFTER EACH ROW
    AFTER EACH ROW IS
    BEGIN
        -- Lưu xe đã xóa khỏi hợp đồng để xử lý sau
        idx := idx + 1;
        deleted_car_list(idx) := :OLD.MaXe;
    END AFTER EACH ROW;

    -- AFTER STATEMENT 
    AFTER STATEMENT IS
        v_in_contract BOOLEAN;
        v_in_maintenance BOOLEAN;
    BEGIN
        -- Kiểm tra và cập nhật trạng thái từng xe
        FOR i IN 1..idx LOOP
            -- Kiểm tra xem xe có còn thuộc hợp đồng nào khác không
            v_in_contract := IS_CAR_IN_CONTRACT(deleted_car_list(i));
            v_in_maintenance := IS_CAR_IN_MAINTENANCE(deleted_car_list(i));
            
            -- Nếu không còn hợp đồng và không đang bảo dưỡng, cập nhật về "Sẵn sàng"
            IF NOT v_in_contract AND NOT v_in_maintenance THEN
                UPDATE XE
                SET TrangThai = 'Sẵn sàng'
                WHERE MaXe = deleted_car_list(i);
            END IF;
        END LOOP;
    END AFTER STATEMENT;
END TRG_CTHD_DELETE_COMPOUND;
/


-- COMPOUND TRIGGER cho CTHD INSERT (Gộp RBTV3 và RBTV5)
-- COMPOUND TRIGGER kiểm tra trùng lặp thời gian hợp đồng khi INSERT vào CTHD
-- COMPOUND TRIGGER cho CTHD INSERT 
CREATE OR REPLACE TRIGGER TRG_CTHD_INSERT_COMPOUND
FOR INSERT ON CTHD
COMPOUND TRIGGER
    -- Khai báo biến
    TYPE car_rec_type IS RECORD (
        MaXe XE.MaXe%TYPE,
        TrangThai XE.TrangThai%TYPE
    );
    
    TYPE car_list_type IS TABLE OF car_rec_type INDEX BY BINARY_INTEGER;
    affected_cars car_list_type;
    idx INTEGER := 0;
    v_current_date DATE;

    -- BEFORE STATEMENT
    BEFORE STATEMENT IS
    BEGIN
        v_current_date := TRUNC(SYSDATE);
    END BEFORE STATEMENT;

    -- BEFORE EACH ROW
    BEFORE EACH ROW IS
        v_trang_thai VARCHAR2(50);
        v_overlap_count NUMBER;
        v_maintenance_count NUMBER;
    BEGIN
        -- Lấy trạng thái hiện tại của xe
        SELECT TrangThai INTO v_trang_thai
        FROM XE
        WHERE MaXe = :NEW.MaXe;
        
        -- KIỂM TRA TRÙNG THỜI GIAN: Kiểm tra xem xe đã có trong hợp đồng nào khác trùng thời gian không
        SELECT COUNT(*) INTO v_overlap_count
        FROM CTHD
        WHERE MaXe = :NEW.MaXe
          AND (:NEW.NgayBatDau <= NgayKetThuc AND :NEW.NgayKetThuc >= NgayBatDau)
          AND MaHD <> :NEW.MaHD; -- Loại trừ trường hợp cùng hợp đồng
        
        IF v_overlap_count > 0 THEN
            RAISE_APPLICATION_ERROR(-20006, 'Vi phạm ràng buộc: Xe ' || :NEW.MaXe || 
                                            ' đã có trong hợp đồng khác trùng thời gian từ ' || 
                                            TO_CHAR(:NEW.NgayBatDau, 'DD/MM/YYYY') || ' đến ' || 
                                            TO_CHAR(:NEW.NgayKetThuc, 'DD/MM/YYYY'));
        END IF;
        
        -- QUAN TRỌNG: Kiểm tra xe có lịch bảo dưỡng nào trong thời gian thuê không
        SELECT COUNT(*) INTO v_maintenance_count
        FROM PHIEUBAODUONG
        WHERE MaXe = :NEW.MaXe
          AND TRUNC(NgayBD) BETWEEN TRUNC(:NEW.NgayBatDau) AND TRUNC(:NEW.NgayKetThuc);
        
        IF v_maintenance_count > 0 THEN
            RAISE_APPLICATION_ERROR(-20018, 'Xe ' || :NEW.MaXe || 
                                  ' có lịch bảo dưỡng trong khoảng thời gian thuê từ ' ||
                                  TO_CHAR(:NEW.NgayBatDau, 'DD/MM/YYYY') || ' đến ' ||
                                  TO_CHAR(:NEW.NgayKetThuc, 'DD/MM/YYYY') || '. ' ||
                                  'Vui lòng chọn xe khác hoặc thay đổi thời gian thuê.');
        END IF;
        
        -- RBTV5: Chỉ kiểm tra trạng thái "Sẵn sàng" nếu hợp đồng bắt đầu ngay (từ ngày hiện tại)
        IF :NEW.NgayBatDau = v_current_date THEN
            -- Nếu hợp đồng bắt đầu ngay hôm nay, xe phải ở trạng thái "Sẵn sàng"
            IF v_trang_thai <> 'Sẵn sàng' THEN
                RAISE_APPLICATION_ERROR(-20005, 'Chỉ xe có trạng thái "Sẵn sàng" mới có thể thêm vào hợp đồng bắt đầu ngay');
            END IF;
        END IF;
        
        -- RBTV3: Kiểm tra xe có cần chuyển sang "Đang thuê" không
        IF v_current_date BETWEEN :NEW.NgayBatDau AND :NEW.NgayKetThuc THEN
            -- Lưu vào danh sách xe cần cập nhật sau khi statement hoàn thành
            idx := idx + 1;
            affected_cars(idx).MaXe := :NEW.MaXe;
            affected_cars(idx).TrangThai := 'Đang thuê';
        END IF;
    END BEFORE EACH ROW;

    -- AFTER STATEMENT 
    AFTER STATEMENT IS
    BEGIN
        -- Cập nhật trạng thái tất cả các xe cần thiết
        FOR i IN 1..idx LOOP
            UPDATE XE
            SET TrangThai = affected_cars(i).TrangThai
            WHERE MaXe = affected_cars(i).MaXe;
        END LOOP;
    END AFTER STATEMENT;
END TRG_CTHD_INSERT_COMPOUND;
/
--TRIGGER: UPDATE CTHD
-- COMPOUND TRIGGER cho CTHD UPDATE (Đã sửa để kiểm tra lịch bảo dưỡng)
CREATE OR REPLACE TRIGGER TRG_CTHD_UPDATE_COMPOUND
FOR UPDATE OF MaXe, NgayBatDau, NgayKetThuc ON CTHD
COMPOUND TRIGGER
    -- Các biến lưu trữ
    TYPE car_rec IS RECORD (
        MaXe XE.MaXe%TYPE,
        TrangThai XE.TrangThai%TYPE,
        MaHD CTHD.MaHD%TYPE
    );
    
    TYPE car_list IS TABLE OF car_rec INDEX BY BINARY_INTEGER;
    
    -- Danh sách xe mới cần xử lý 
    new_cars car_list;
    new_idx INTEGER := 0;
    
    -- Danh sách xe cũ cần xử lý
    old_cars car_list;
    old_idx INTEGER := 0;
    
    -- Biến thời gian hiện tại
    v_current_date DATE;
    
    -- Cấu trúc để lưu thông tin kiểm tra trùng lặp
    TYPE overlap_check_rec IS RECORD (
        MaXe XE.MaXe%TYPE,
        start_date DATE,
        end_date DATE,
        MaHD CTHD.MaHD%TYPE,
        row_id ROWID
    );
    
    TYPE overlap_list IS TABLE OF overlap_check_rec INDEX BY BINARY_INTEGER;
    overlap_checks overlap_list;
    overlap_idx INTEGER := 0;

    -- BEFORE STATEMENT
    BEFORE STATEMENT IS
    BEGIN
        v_current_date := TRUNC(SYSDATE);
    END BEFORE STATEMENT;
    
    -- BEFORE EACH ROW
    BEFORE EACH ROW IS
        v_maintenance_count NUMBER;
        v_trang_thai_new_car VARCHAR2(50);
    BEGIN
        -- Lưu thông tin cần kiểm tra trùng lặp
        IF :NEW.MaXe <> :OLD.MaXe OR 
           :NEW.NgayBatDau <> :OLD.NgayBatDau OR 
           :NEW.NgayKetThuc <> :OLD.NgayKetThuc THEN
           
            overlap_idx := overlap_idx + 1;
            overlap_checks(overlap_idx).MaXe := :NEW.MaXe;
            overlap_checks(overlap_idx).start_date := :NEW.NgayBatDau;
            overlap_checks(overlap_idx).end_date := :NEW.NgayKetThuc;
            overlap_checks(overlap_idx).MaHD := :NEW.MaHD;
            overlap_checks(overlap_idx).row_id := :OLD.ROWID;
            
            
            -- QUAN TRỌNG: Kiểm tra xe có lịch bảo dưỡng nào trong thời gian thuê không
            SELECT COUNT(*) INTO v_maintenance_count
            FROM PHIEUBAODUONG
            WHERE MaXe = :NEW.MaXe
              AND TRUNC(NgayBD) BETWEEN TRUNC(:NEW.NgayBatDau) AND TRUNC(:NEW.NgayKetThuc);
            
            IF v_maintenance_count > 0 THEN
                RAISE_APPLICATION_ERROR(-20018, 'Xe ' || :NEW.MaXe || 
                                      ' có lịch bảo dưỡng trong khoảng thời gian thuê từ ' ||
                                      TO_CHAR(:NEW.NgayBatDau, 'DD/MM/YYYY') || ' đến ' ||
                                      TO_CHAR(:NEW.NgayKetThuc, 'DD/MM/YYYY') || '. ' ||
                                      'Vui lòng chọn xe khác hoặc thay đổi thời gian thuê.');
            END IF;
            
            -- Nếu thay đổi xe và hợp đồng bắt đầu từ hôm nay hoặc đã bắt đầu
            IF :NEW.MaXe <> :OLD.MaXe AND :NEW.NgayBatDau <= v_current_date THEN
                -- Kiểm tra xe mới có trạng thái "Sẵn sàng" không
                SELECT TrangThai INTO v_trang_thai_new_car
                FROM XE
                WHERE MaXe = :NEW.MaXe;
                
                IF v_trang_thai_new_car <> 'Sẵn sàng' THEN
                    RAISE_APPLICATION_ERROR(-20005, 'Chỉ xe có trạng thái "Sẵn sàng" mới có thể thêm vào hợp đồng đã bắt đầu');
                END IF;
            END IF;
        END IF;
        
        -- Nếu thay đổi MaXe
        IF :NEW.MaXe <> :OLD.MaXe THEN
            -- Lưu xe MỚI để xử lý sau
            new_idx := new_idx + 1;
            new_cars(new_idx).MaXe := :NEW.MaXe;
            new_cars(new_idx).MaHD := :NEW.MaHD;
            
            -- Lưu xe CŨ để xử lý sau  
            old_idx := old_idx + 1;
            old_cars(old_idx).MaXe := :OLD.MaXe;
            old_cars(old_idx).MaHD := :OLD.MaHD;
            
        ELSIF :NEW.NgayBatDau <> :OLD.NgayBatDau OR :NEW.NgayKetThuc <> :OLD.NgayKetThuc THEN
            -- Nếu chỉ thay đổi ngày, xem như xe mới cần cập nhật
            new_idx := new_idx + 1;
            new_cars(new_idx).MaXe := :NEW.MaXe;
            new_cars(new_idx).MaHD := :NEW.MaHD;
            
        END IF;
    END BEFORE EACH ROW;

    -- AFTER STATEMENT
    AFTER STATEMENT IS
        v_overlap_count NUMBER;
        v_contract_count NUMBER;
        v_maintenance_count NUMBER;
        v_current_status VARCHAR2(50);
    BEGIN
        -- 1. Kiểm tra trùng lặp thời gian TRƯỚC
        FOR i IN 1..overlap_idx LOOP
            SELECT COUNT(*) INTO v_overlap_count
            FROM CTHD
            WHERE MaXe = overlap_checks(i).MaXe
              AND (overlap_checks(i).start_date <= NgayKetThuc 
                  AND overlap_checks(i).end_date >= NgayBatDau)
              AND MaHD <> overlap_checks(i).MaHD
              AND ROWID <> overlap_checks(i).row_id;
            
            IF v_overlap_count > 0 THEN
                RAISE_APPLICATION_ERROR(-20006, 'Vi phạm ràng buộc: Xe ' || overlap_checks(i).MaXe || 
                                              ' đã có trong hợp đồng khác trùng thời gian.');
            END IF;
        END LOOP;
        
        -- 2. XỬ LÝ CÁC XE MỚI
        DBMS_OUTPUT.PUT_LINE('Xử lý ' || new_idx || ' xe mới');
        FOR i IN 1..new_idx LOOP
            -- Kiểm tra xe có đang trong thời gian thuê hay không
            SELECT COUNT(*) INTO v_contract_count
            FROM CTHD
            WHERE MaXe = new_cars(i).MaXe
              AND v_current_date BETWEEN NgayBatDau AND NgayKetThuc;
            
            SELECT COUNT(*) INTO v_maintenance_count
            FROM PHIEUBAODUONG
            WHERE MaXe = new_cars(i).MaXe
              AND TRUNC(NgayBD) = v_current_date;
            
            -- Quyết định trạng thái xe mới
            IF v_maintenance_count > 0 THEN
                new_cars(i).TrangThai := 'Bảo dưỡng';
            ELSIF v_contract_count > 0 THEN
                new_cars(i).TrangThai := 'Đang thuê';
            ELSE 
                new_cars(i).TrangThai := 'Sẵn sàng';
            END IF;
            
            UPDATE XE
            SET TrangThai = new_cars(i).TrangThai
            WHERE MaXe = new_cars(i).MaXe;
        END LOOP;
        
        -- 3. XỬ LÝ CÁC XE CŨ
        DBMS_OUTPUT.PUT_LINE('Xử lý ' || old_idx || ' xe cũ');
        FOR i IN 1..old_idx LOOP
            -- Kiểm tra xe có đang trong thời gian thuê hay không trong HĐ khác
            SELECT COUNT(*) INTO v_contract_count
            FROM CTHD
            WHERE MaXe = old_cars(i).MaXe
              AND v_current_date BETWEEN NgayBatDau AND NgayKetThuc;
            
            SELECT COUNT(*) INTO v_maintenance_count
            FROM PHIEUBAODUONG
            WHERE MaXe = old_cars(i).MaXe
              AND TRUNC(NgayBD) = v_current_date;
            
            -- Quyết định trạng thái xe cũ
            IF v_maintenance_count > 0 THEN
                old_cars(i).TrangThai := 'Bảo dưỡng';
            ELSIF v_contract_count > 0 THEN
                old_cars(i).TrangThai := 'Đang thuê';
            ELSE 
                old_cars(i).TrangThai := 'Sẵn sàng';
            END IF;
            
            UPDATE XE
            SET TrangThai = old_cars(i).TrangThai
            WHERE MaXe = old_cars(i).MaXe;
        END LOOP;
        
    END AFTER STATEMENT;
END TRG_CTHD_UPDATE_COMPOUND;
/

-- 2. Thêm dữ liệu mẫu (không chỉ định mã cho các bảng có trigger)

INSERT INTO VAITRO (TenVaiTro) VALUES ('Khách hàng');
INSERT INTO VAITRO (TenVaiTro) VALUES ('Quản trị viên');

-- Thêm dữ liệu vào bảng QUYENHAN
INSERT INTO QUYENHAN (TenQuyen, TrangThai) VALUES ('Quản lý tài khoản', 'Hoạt động');
INSERT INTO QUYENHAN (TenQuyen, TrangThai) VALUES ('Xem báo cáo', 'Hoạt động');
INSERT INTO QUYENHAN (TenQuyen, TrangThai) VALUES ('Đặt xe', 'Hoạt động');
INSERT INTO QUYENHAN (TenQuyen, TrangThai) VALUES ('Giao nhận xe', 'Hoạt động');

-- Thêm dữ liệu vào bảng VAITRO_QUYENHAN
INSERT INTO VAITRO_QUYENHAN VALUES ('VT002', 'QH001');
INSERT INTO VAITRO_QUYENHAN VALUES ('VT002', 'QH002');
INSERT INTO VAITRO_QUYENHAN VALUES ('VT001', 'QH003');
INSERT INTO VAITRO_QUYENHAN VALUES ('VT001', 'QH004');

-- Thêm dữ liệu vào bảng TAIKHOAN
INSERT INTO TAIKHOAN (MaVaiTro, TenDangNhap, MatKhau, TrangThai) VALUES ('VT001', 'khach1', '123456', 'Hoạt động');
INSERT INTO TAIKHOAN (MaVaiTro, TenDangNhap, MatKhau, TrangThai) VALUES ('VT001', 'khach2', '123456', 'Hoạt động');
INSERT INTO TAIKHOAN (MaVaiTro, TenDangNhap, MatKhau, TrangThai) VALUES ('VT002', 'admin', '123456', 'Hoạt động');
INSERT INTO TAIKHOAN (MaVaiTro, TenDangNhap, MatKhau, TrangThai) VALUES ('VT002', 'admin1', '123456', 'Hoạt động');
INSERT INTO TAIKHOAN (MaVaiTro, TenDangNhap, MatKhau, TrangThai) VALUES ('VT002', 'admin2', '123456', 'Hoạt động');
INSERT INTO TAIKHOAN (MaVaiTro, TenDangNhap, MatKhau, TrangThai) VALUES ('VT002', 'admin3', '123456', 'Hoạt động');
INSERT INTO TAIKHOAN (MaVaiTro, TenDangNhap, MatKhau, TrangThai) VALUES ('VT001', 'lehoainam', '123456', 'Hoạt động');
INSERT INTO TAIKHOAN (MaVaiTro, TenDangNhap, MatKhau, TrangThai) VALUES ('VT001', 'ptha', '123456', 'Hoạt động');
INSERT INTO TAIKHOAN (MaVaiTro, TenDangNhap, MatKhau, TrangThai) VALUES ('VT001', 'tvbinh', '123456', 'Hoạt động');
-- Thêm dữ liệu vào bảng NHANVIEN
INSERT INTO NHANVIEN (MaTK, HoTen, SDT, Email, ChucVu) VALUES ('TK003', 'Nguyễn Quản Lý', '0901234567', 'quanly@carental.com', 'Quản lý');
INSERT INTO NHANVIEN (MaTK, HoTen, SDT, Email, ChucVu) VALUES ('TK004', 'Trần Văn Bán', '0912345678', 'banle@carental.com', 'Nhân viên bán hàng');
INSERT INTO NHANVIEN (MaTK, HoTen, SDT, Email, ChucVu) VALUES ('TK005', 'Lê Kỹ Thuật', '0923456789', 'kythuat@carental.com', 'Kỹ thuật viên');
INSERT INTO NHANVIEN (MaTK, HoTen, SDT, Email, ChucVu) VALUES ('TK006', 'Phạm Giao Nhận', '0934567890', 'giaonhan@carental.com', 'Nhân viên giao nhận');

-- Thêm dữ liệu vào bảng KHACHHANG
INSERT INTO KHACHHANG (MaTK, TongTienNo, HoTen, SDT, Email, CCCD, DiaChi) VALUES ('TK001', 0, 'Nguyễn Văn Khách', '0945678901', 'khach1@gmail.com', '001234567890', 'Quận 1, TP.HCM');
INSERT INTO KHACHHANG (MaTK, TongTienNo, HoTen, SDT, Email, CCCD, DiaChi) VALUES ('TK002', 0, 'Trần Thị Khách', '0956789012', 'khach2@gmail.com', '001234567891', 'Quận 2, TP.HCM');
INSERT INTO KHACHHANG (MaTK, TongTienNo, HoTen, SDT, Email, CCCD, DiaChi) VALUES ('TK007', 0, 'Lê Hoài Nam', '0967890123', 'nam@gmail.com', '001234567892', 'Quận 3, TP.HCM');
INSERT INTO KHACHHANG (MaTK, TongTienNo, HoTen, SDT, Email, CCCD, DiaChi) VALUES ('TK008', 0, 'Phạm Thị Hà', '0978901234', 'ha@gmail.com', '001234567893', 'Quận 4, TP.HCM');
INSERT INTO KHACHHANG (MaTK, TongTienNo, HoTen, SDT, Email, CCCD, DiaChi) VALUES ('TK009', 0, 'Trương Văn Bình', '0989012345', 'binh@gmail.com', '001234567894', 'Quận 5, TP.HCM');
INSERT INTO KHACHHANG (MaTK, TongTienNo, HoTen, SDT, Email, CCCD, DiaChi) VALUES (NULL, 0, 'Trâm Nguyễn', '0989012345', 'binh@gmail.com', '001234567894', 'Quận 5, TP.HCM');

-- Thêm dữ liệu vào bảng XE
INSERT INTO XE (TenXe, BienSo, SoCho, HangXe, NamSX, TrangThai, GiaThueNgay, HinhAnh) VALUES ('Toyota Camry', '51A-12345', 5, 'Toyota', 2020, 'Sẵn sàng', 800000, 'xe_XE001.png');
INSERT INTO XE (TenXe, BienSo, SoCho, HangXe, NamSX, TrangThai, GiaThueNgay, HinhAnh) VALUES ('Honda Civic', '51A-23456', 5, 'Honda', 2021, 'Sẵn sàng', 750000, 'xe_XE002.png');
INSERT INTO XE (TenXe, BienSo, SoCho, HangXe, NamSX, TrangThai, GiaThueNgay, HinhAnh) VALUES ('Ford Everest', '51A-34567', 7, 'Ford', 2022, 'Sẵn sàng', 950000, 'xe_XE003.png');
INSERT INTO XE (TenXe, BienSo, SoCho, HangXe, NamSX, TrangThai, GiaThueNgay, HinhAnh) VALUES ('Mazda CX-5', '51A-45678', 5, 'Mazda', 2021, 'Sẵn sàng', 850000, 'xe_XE004.png');
INSERT INTO XE (TenXe, BienSo, SoCho, HangXe, NamSX, TrangThai, GiaThueNgay, HinhAnh) VALUES ('Kia Sportage', '51A-56789', 5, 'Kia', 2022, 'Sẵn sàng', 820000, 'xe_XE005.png');
INSERT INTO XE (TenXe, BienSo, SoCho, HangXe, NamSX, TrangThai, GiaThueNgay, HinhAnh) VALUES ('Toyota Fortuner', '51A-67890', 7, 'Toyota', 2020, 'Sẵn sàng', 950000, 'xe_XE006.png');
INSERT INTO XE (TenXe, BienSo, SoCho, HangXe, NamSX, TrangThai, GiaThueNgay, HinhAnh) VALUES ('Mercedes-Benz C300', '51A-78901', 5, 'Mercedes', 2022, 'Sẵn sàng', 1500000, 'xe_XE007.png');
INSERT INTO XE (TenXe, BienSo, SoCho, HangXe, NamSX, TrangThai, GiaThueNgay, HinhAnh) VALUES ('BMW X5', '51A-89012', 5, 'BMW', 2021, 'Sẵn sàng', 1600000, 'xe_XE008.png');
INSERT INTO XE (TenXe, BienSo, SoCho, HangXe, NamSX, TrangThai, GiaThueNgay, HinhAnh) VALUES ('Audi Q5', '51A-90123', 5, 'Audi', 2022, 'Sẵn sàng', 1450000, 'xe_XE009.png');
INSERT INTO XE (TenXe, BienSo, SoCho, HangXe, NamSX, TrangThai, GiaThueNgay, HinhAnh) VALUES ('Hyundai Santa Fe', '51A-01234', 7, 'Hyundai', 2021, 'Sẵn sàng', 900000, 'xe_XE010.png');

-- Thêm dữ liệu vào bảng DICHVUBD
INSERT INTO DICHVUBD (TenDV, GiaDV) VALUES ('Thay nhớt', 350000);
INSERT INTO DICHVUBD (TenDV, GiaDV) VALUES ('Thay lọc gió', 150000);
INSERT INTO DICHVUBD (TenDV, GiaDV) VALUES ('Thay lọc nhiên liệu', 200000);
INSERT INTO DICHVUBD (TenDV, GiaDV) VALUES ('Bảo dưỡng hệ thống phanh', 450000);
INSERT INTO DICHVUBD (TenDV, GiaDV) VALUES ('Cân chỉnh thước lái', 300000);
INSERT INTO DICHVUBD (TenDV, GiaDV) VALUES ('Thay dầu hộp số', 500000);
INSERT INTO DICHVUBD (TenDV, GiaDV) VALUES ('Thay bugi', 250000);
INSERT INTO DICHVUBD (TenDV, GiaDV) VALUES ('Kiểm tra và sửa điều hòa', 600000);

-- -- Thêm dữ liệu vào bảng HOPDONG (với TongTien khác 0)
-- INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai) VALUES ('KH001', 'NV002', SYSDATE-10, 'Quận 1, TP.HCM', 0, 'Chờ xác nhận');
-- INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai) VALUES ('KH002', 'NV002', SYSDATE-7, 'Quận 2, TP.HCM', 0, 'Chờ xác nhận');
-- INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai) VALUES ('KH003', 'NV002', SYSDATE-5, 'Quận 3, TP.HCM', 0, 'Đang thuê');
-- INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai) VALUES ('KH001', 'NV002', SYSDATE-3, 'Quận 1, TP.HCM', 0, 'Đang thuê');
-- INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai) VALUES ('KH002', 'NV002', SYSDATE-1, 'Quận 2, TP.HCM', 0, 'Chờ xác nhận');


-- -- Thêm dữ liệu vào bảng CTHD
-- INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) VALUES ('HD001', 'XE001', SYSDATE-10, SYSDATE-7);
-- INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) VALUES ('HD002', 'XE002', SYSDATE-7, SYSDATE+3);
-- INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) VALUES ('HD002', 'XE003', SYSDATE-7, SYSDATE+3);
-- INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) VALUES ('HD003', 'XE004', SYSDATE-5, SYSDATE);
-- INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) VALUES ('HD004', 'XE005', SYSDATE-3, SYSDATE+4);
-- INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) VALUES ('HD004', 'XE006', SYSDATE-3, SYSDATE+4);
-- INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) VALUES ('HD005', 'XE007', SYSDATE+1, SYSDATE+5);
INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai) 
VALUES ('KH001', 'NV002', TO_DATE('15-05-2024', 'DD-MM-YYYY'), 'Quận 1, TP.HCM', 0, 'Hoàn thành');

INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai) 
VALUES ('KH002', 'NV003', TO_DATE('22-06-2024', 'DD-MM-YYYY'), 'Quận 2, TP.HCM', 0, 'Hoàn thành');

INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai) 
VALUES ('KH003', 'NV001', TO_DATE('10-07-2024', 'DD-MM-YYYY'), 'Quận 3, TP.HCM', 0, 'Hoàn thành');

INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai) 
VALUES ('KH001', 'NV002', TO_DATE('18-08-2024', 'DD-MM-YYYY'), 'Quận 1, TP.HCM', 0, 'Hoàn thành');

INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai) 
VALUES ('KH004', 'NV004', TO_DATE('05-09-2024', 'DD-MM-YYYY'), 'Quận 5, TP.HCM', 0, 'Hoàn thành');

INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai) 
VALUES ('KH002', 'NV003', TO_DATE('12-10-2024', 'DD-MM-YYYY'), 'Quận 2, TP.HCM', 0, 'Hoàn thành');

INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai) 
VALUES ('KH005', 'NV002', TO_DATE('28-11-2024', 'DD-MM-YYYY'), 'Quận 7, TP.HCM', 0, 'Hoàn thành');

INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai) 
VALUES ('KH003', 'NV001', TO_DATE('15-12-2024', 'DD-MM-YYYY'), 'Quận 3, TP.HCM', 0, 'Hoàn thành');

-- Các hợp đồng từ đầu năm nay
INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai) 
VALUES ('KH001', 'NV004', TO_DATE('05-01-2025', 'DD-MM-YYYY'), 'Quận 1, TP.HCM', 0, 'Hoàn thành');

INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai) 
VALUES ('KH006', 'NV002', TO_DATE('18-01-2025', 'DD-MM-YYYY'), 'Quận 6, TP.HCM', 0, 'Hoàn thành');

INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai) 
VALUES ('KH002', 'NV003', TO_DATE('02-02-2025', 'DD-MM-YYYY'), 'Quận 2, TP.HCM', 0, 'Hoàn thành');

INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai) 
VALUES ('KH004', 'NV001', TO_DATE('20-02-2025', 'DD-MM-YYYY'), 'Quận 5, TP.HCM', 0, 'Hoàn thành');

INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai) 
VALUES ('KH003', 'NV002', TO_DATE('10-03-2025', 'DD-MM-YYYY'), 'Quận 3, TP.HCM', 0, 'Hoàn thành');

INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai) 
VALUES ('KH005', 'NV004', TO_DATE('25-03-2025', 'DD-MM-YYYY'), 'Quận 7, TP.HCM', 0, 'Hoàn thành');

-- Các hợp đồng gần đây (tháng 4-5/2025)
INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai) 
VALUES ('KH002', 'NV003', TO_DATE('08-04-2025', 'DD-MM-YYYY'), 'Quận 2, TP.HCM', 0, 'Đang thuê');

INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai) 
VALUES ('KH001', 'NV001', TO_DATE('22-04-2025', 'DD-MM-YYYY'), 'Quận 1, TP.HCM', 0, 'Đang thuê');

INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai) 
VALUES ('KH006', 'NV002', TO_DATE('01-05-2025', 'DD-MM-YYYY'), 'Quận 6, TP.HCM', 0, 'Đang thuê');

INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai) 
VALUES ('KH003', 'NV004', TO_DATE('12-05-2025', 'DD-MM-YYYY'), 'Quận 3, TP.HCM', 0, 'Chờ xác nhận');

INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai) 
VALUES ('KH004', 'NV003', TO_DATE('25-05-2025', 'DD-MM-YYYY'), 'Quận 5, TP.HCM', 0, 'Chờ xác nhận');

-- Thêm dữ liệu vào bảng CTHD với thời gian tương ứng
-- Các hợp đồng cũ từ năm trước
INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) 
VALUES ('HD001', 'XE001', TO_DATE('16-05-2024', 'DD-MM-YYYY'), TO_DATE('19-05-2024', 'DD-MM-YYYY'));

INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) 
VALUES ('HD002', 'XE002', TO_DATE('23-06-2024', 'DD-MM-YYYY'), TO_DATE('28-06-2024', 'DD-MM-YYYY'));

INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) 
VALUES ('HD002', 'XE003', TO_DATE('23-06-2024', 'DD-MM-YYYY'), TO_DATE('28-06-2024', 'DD-MM-YYYY'));

INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) 
VALUES ('HD003', 'XE004', TO_DATE('12-07-2024', 'DD-MM-YYYY'), TO_DATE('13-07-2024', 'DD-MM-YYYY'));

INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) 
VALUES ('HD004', 'XE001', TO_DATE('20-08-2024', 'DD-MM-YYYY'), TO_DATE('23-08-2024', 'DD-MM-YYYY'));

INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) 
VALUES ('HD005', 'XE005', TO_DATE('06-09-2024', 'DD-MM-YYYY'), TO_DATE('07-09-2024', 'DD-MM-YYYY'));

INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) 
VALUES ('HD006', 'XE002', TO_DATE('14-10-2024', 'DD-MM-YYYY'), TO_DATE('15-10-2024', 'DD-MM-YYYY'));

INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) 
VALUES ('HD007', 'XE006', TO_DATE('29-11-2024', 'DD-MM-YYYY'), TO_DATE('01-12-2024', 'DD-MM-YYYY'));

INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) 
VALUES ('HD008', 'XE003', TO_DATE('16-12-2024', 'DD-MM-YYYY'), TO_DATE('17-12-2024', 'DD-MM-YYYY'));

-- Các hợp đồng từ đầu năm nay
INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) 
VALUES ('HD009', 'XE004', TO_DATE('06-01-2025', 'DD-MM-YYYY'), TO_DATE('07-01-2025', 'DD-MM-YYYY'));

INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) 
VALUES ('HD010', 'XE007', TO_DATE('19-01-2025', 'DD-MM-YYYY'), TO_DATE('20-01-2025', 'DD-MM-YYYY'));

INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) 
VALUES ('HD011', 'XE001', TO_DATE('03-02-2025', 'DD-MM-YYYY'), TO_DATE('04-02-2025', 'DD-MM-YYYY'));

INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) 
VALUES ('HD012', 'XE005', TO_DATE('21-02-2025', 'DD-MM-YYYY'), TO_DATE('23-02-2025', 'DD-MM-YYYY'));

INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) 
VALUES ('HD013', 'XE002', TO_DATE('11-03-2025', 'DD-MM-YYYY'), TO_DATE('14-03-2025', 'DD-MM-YYYY'));

INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) 
VALUES ('HD013', 'XE003', TO_DATE('11-03-2025', 'DD-MM-YYYY'), TO_DATE('14-03-2025', 'DD-MM-YYYY'));

INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) 
VALUES ('HD014', 'XE006', TO_DATE('26-03-2025', 'DD-MM-YYYY'), TO_DATE('28-03-2025', 'DD-MM-YYYY'));

-- Các hợp đồng đang thuê (tháng 4-5/2025)
INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) 
VALUES ('HD015', 'XE004', TO_DATE('09-04-2025', 'DD-MM-YYYY'), TO_DATE('13-04-2025', 'DD-MM-YYYY'));

INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) 
VALUES ('HD016', 'XE001', TO_DATE('23-04-2025', 'DD-MM-YYYY'), TO_DATE('26-04-2025', 'DD-MM-YYYY'));

INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) 
VALUES ('HD016', 'XE007', TO_DATE('10-05-2025', 'DD-MM-YYYY'), TO_DATE('14-05-2025', 'DD-MM-YYYY'));

INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) 
VALUES ('HD017', 'XE003', TO_DATE('13-06-2025', 'DD-MM-YYYY'), TO_DATE('15-06-2025', 'DD-MM-YYYY'));

-- Các hợp đồng chờ xác nhận (tháng 5/2025 với ngày bắt đầu trong tương lai)
INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) 
VALUES ('HD018', 'XE005', TO_DATE('15-06-2025', 'DD-MM-YYYY'), TO_DATE('20-06-2025', 'DD-MM-YYYY'));

INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc) 
VALUES ('HD019', 'XE002', TO_DATE('10-06-2025', 'DD-MM-YYYY'), TO_DATE('17-06-2025', 'DD-MM-YYYY'));


-- Thêm dữ liệu vào bảng GIAONHANXE
INSERT INTO GIAONHANXE (MaHD, MaXe, MaNV, TrangThaiXe, GhiChu, TrangThaiGN) VALUES ('HD001', 'XE001', 'NV004', 'Tốt', 'Giao xe đúng hẹn', 'Đã giao');
INSERT INTO GIAONHANXE (MaHD, MaXe, MaNV, TrangThaiXe, GhiChu, TrangThaiGN) VALUES ('HD002', 'XE002', 'NV004', 'Tốt', 'Giao xe đúng hẹn', 'Đã giao');
INSERT INTO GIAONHANXE (MaHD, MaXe, MaNV, TrangThaiXe, GhiChu, TrangThaiGN) VALUES ('HD002', 'XE003', 'NV004', 'Tốt', 'Xe mới','Đã giao');
INSERT INTO GIAONHANXE (MaHD, MaXe, MaNV, TrangThaiXe, GhiChu, TrangThaiGN) VALUES ('HD003', 'XE004', 'NV004', 'Tốt', 'Đã kiểm tra trước khi giao', 'Đã giao');

-- Thêm dữ liệu vào bảng PHIEUBAODUONG (với TongTienBD khác 0)
INSERT INTO PHIEUBAODUONG (MaXe, MaKH, NgayBD, MaNV, LoaiBD, TongTienBD) VALUES ('XE001', NULL, SYSDATE-15, 'NV003', 'Định Kỳ', 0);
INSERT INTO PHIEUBAODUONG (MaXe, MaKH, NgayBD, MaNV, LoaiBD, TongTienBD) VALUES ('XE002', NULL, SYSDATE-15, 'NV003', 'Định Kỳ', 0);
INSERT INTO PHIEUBAODUONG (MaXe, MaKH, NgayBD, MaNV, LoaiBD, TongTienBD) VALUES ('XE003', 'KH001', SYSDATE+30, 'NV003', 'Khách gây hư hại', 0);
INSERT INTO PHIEUBAODUONG (MaXe, MaKH, NgayBD, MaNV, LoaiBD, TongTienBD) VALUES ('XE004', NULL, SYSDATE+31, 'NV003', 'Định Kỳ', 0);
INSERT INTO PHIEUBAODUONG (MaXe, MaKH, NgayBD, MaNV, LoaiBD, TongTienBD) VALUES ('XE005', NULL, SYSDATE+31, 'NV003', 'Định Kỳ', 0);
-- Thêm dữ liệu vào bảng CHITIETBAODUONG
INSERT INTO CHITIETBAODUONG (MaBD, MaDV, SoLuong) VALUES ('BD001', 'DV001', 1);
INSERT INTO CHITIETBAODUONG (MaBD, MaDV, SoLuong) VALUES ('BD001', 'DV002', 1);
INSERT INTO CHITIETBAODUONG (MaBD, MaDV, SoLuong) VALUES ('BD001', 'DV003', 1);
INSERT INTO CHITIETBAODUONG (MaBD, MaDV, SoLuong) VALUES ('BD002', 'DV001', 1);
INSERT INTO CHITIETBAODUONG (MaBD, MaDV, SoLuong) VALUES ('BD002', 'DV007', 4);
INSERT INTO CHITIETBAODUONG (MaBD, MaDV, SoLuong) VALUES ('BD003', 'DV004', 1);
INSERT INTO CHITIETBAODUONG (MaBD, MaDV, SoLuong) VALUES ('BD003', 'DV005', 1);
INSERT INTO CHITIETBAODUONG (MaBD, MaDV, SoLuong) VALUES ('BD003', 'DV008', 1);
INSERT INTO CHITIETBAODUONG (MaBD, MaDV, SoLuong) VALUES ('BD004', 'DV001', 1);
INSERT INTO CHITIETBAODUONG (MaBD, MaDV, SoLuong) VALUES ('BD004', 'DV002', 1);
INSERT INTO CHITIETBAODUONG (MaBD, MaDV, SoLuong) VALUES ('BD004', 'DV006', 1);

-- Thêm dữ liệu vào bảng LICHSUCONGNO
INSERT INTO LICHSUCONGNO (MaKH, NgayGiaoDich, LoaiGiaoDich, SoTien, GhiChu) VALUES ('KH001', SYSDATE-10, 'PHAT SINH', 2400000, 'Phát sinh từ hợp đồng thuê xe');
INSERT INTO LICHSUCONGNO (MaKH, NgayGiaoDich, LoaiGiaoDich, SoTien, GhiChu) VALUES ('KH001', SYSDATE-9, 'THANH TOAN', 2400000, 'Thanh toán hợp đồng thuê xe');
INSERT INTO LICHSUCONGNO (MaKH, NgayGiaoDich, LoaiGiaoDich, SoTien, GhiChu) VALUES ('KH002', SYSDATE-7, 'PHAT SINH', 4750000, 'Phát sinh từ hợp đồng thuê xe');
INSERT INTO LICHSUCONGNO (MaKH, NgayGiaoDich, LoaiGiaoDich, SoTien, GhiChu) VALUES ('KH002', SYSDATE-6, 'THANH TOAN', 2000000, 'Thanh toán một phần hợp đồng');
INSERT INTO LICHSUCONGNO (MaKH, NgayGiaoDich, LoaiGiaoDich, SoTien, GhiChu) VALUES ('KH001', SYSDATE-5, 'PHAT SINH', 1200000, 'Chi phí sửa chữa xe');
INSERT INTO LICHSUCONGNO (MaKH, NgayGiaoDich, LoaiGiaoDich, SoTien, GhiChu) VALUES ('KH001', SYSDATE-3, 'PHAT SINH', 3200000, 'Phát sinh từ hợp đồng thuê xe');

INSERT INTO LICHSUCONGNO (MaKH, NgayGiaoDich, LoaiGiaoDich, SoTien, GhiChu) VALUES ('KH003', SYSDATE-5, 'PHAT SINH', 1200000, 'Chi phí sửa chữa xe');
INSERT INTO LICHSUCONGNO (MaKH, NgayGiaoDich, LoaiGiaoDich, SoTien, GhiChu) VALUES ('KH004', SYSDATE-3, 'PHAT SINH', 3200000, 'Phát sinh từ hợp đồng thuê xe');

-- Thêm dữ liệu vào bảng DANHGIA
INSERT INTO DANHGIA (MaHD, DiemSo, BinhLuan, NgayDanhGia) VALUES ('HD001', 5, 'Dịch vụ rất tốt, xe sạch sẽ và chạy êm', SYSDATE-7);
INSERT INTO DANHGIA (MaHD, DiemSo, BinhLuan, NgayDanhGia) VALUES ('HD002', 4, 'Xe tốt nhưng giá hơi cao', SYSDATE-6);

CREATE OR REPLACE TRIGGER trg_khachhang_delete
BEFORE DELETE ON KHACHHANG
FOR EACH ROW
BEGIN
    -- Nếu khách hàng có tài khoản
    IF :OLD.MaTK IS NOT NULL THEN
        -- Kiểm tra xem tài khoản có liên kết với nhân viên nào không
        DECLARE
            v_count NUMBER;
        BEGIN
            SELECT COUNT(*) INTO v_count FROM NHANVIEN 
            WHERE MaTK = :OLD.MaTK;
            
            -- Nếu không có liên kết với nhân viên nào, xóa tài khoản
            IF v_count = 0 THEN
                DELETE FROM TAIKHOAN WHERE MaTK = :OLD.MaTK;
            END IF;
        END;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_nhanvien_delete
BEFORE DELETE ON NHANVIEN
FOR EACH ROW
BEGIN
    -- Nếu nhân viên có tài khoản
    IF :OLD.MaTK IS NOT NULL THEN
        -- Kiểm tra xem tài khoản có liên kết với khách hàng nào không
        DECLARE
            v_count NUMBER;
        BEGIN
            SELECT COUNT(*) INTO v_count FROM KHACHHANG 
            WHERE MaTK = :OLD.MaTK;
            
            -- Nếu không có liên kết với khách hàng nào, xóa tài khoản
            IF v_count = 0 THEN
                DELETE FROM TAIKHOAN WHERE MaTK = :OLD.MaTK;
            END IF;
        END;
    END IF;
END;
/
CREATE OR REPLACE PROCEDURE sp_TaoKhachHang(
    p_HoTen IN KHACHHANG.HoTen%TYPE,
    p_SDT IN KHACHHANG.SDT%TYPE,
    p_Email IN KHACHHANG.Email%TYPE,
    p_CCCD IN KHACHHANG.CCCD%TYPE,
    p_DiaChi IN KHACHHANG.DiaChi%TYPE,
    p_MaKH OUT KHACHHANG.MaKH%TYPE
)
IS
    v_Count NUMBER; -- Khai báo biến chung cho toàn bộ procedure
BEGIN
    -- Kiểm tra dữ liệu đầu vào
    IF p_HoTen IS NULL OR p_SDT IS NULL OR p_CCCD IS NULL THEN
        RAISE_APPLICATION_ERROR(-20001, 'Họ tên, SĐT và CCCD không được để trống.');
    END IF;
    
    -- Kiểm tra SĐT đã tồn tại chưa
    SELECT COUNT(*) INTO v_Count FROM KHACHHANG WHERE SDT = p_SDT;
    IF v_Count > 0 THEN
        RAISE_APPLICATION_ERROR(-20002, 'Số điện thoại đã được sử dụng.');
    END IF;
    
    -- Kiểm tra CCCD đã tồn tại chưa
    SELECT COUNT(*) INTO v_Count FROM KHACHHANG WHERE CCCD = p_CCCD;
    IF v_Count > 0 THEN
        RAISE_APPLICATION_ERROR(-20003, 'CCCD đã được sử dụng.');
    END IF;
    
    -- Thêm khách hàng mới với MaTK = NULL, trigger sẽ tạo mã tự động
    INSERT INTO KHACHHANG (MaTK, TongTienNo, HoTen, SDT, Email, CCCD, DiaChi)
    VALUES (NULL, 0, p_HoTen, p_SDT, p_Email, p_CCCD, p_DiaChi)
    RETURNING MaKH INTO p_MaKH;
    
    DBMS_OUTPUT.PUT_LINE('Đã tạo khách hàng: ' || p_HoTen || ' - Mã: ' || p_MaKH);
END;
/

CREATE OR REPLACE PROCEDURE sp_TaoHopDong(
    p_MaKH IN KHACHHANG.MaKH%TYPE,
    p_MaNV IN NHANVIEN.MaNV%TYPE,
    p_DiaChiGiao IN HOPDONG.DiaChiGiao%TYPE,
    p_MaHD OUT HOPDONG.MaHD%TYPE
)
IS
    v_TongTienNo KHACHHANG.TongTienNo%TYPE;
    v_Count NUMBER; 
BEGIN
    -- Kiểm tra khách hàng tồn tại
    SELECT COUNT(*) INTO v_Count FROM KHACHHANG WHERE MaKH = p_MaKH;
    IF v_Count = 0 THEN
        RAISE_APPLICATION_ERROR(-20010, 'Khách hàng không tồn tại.');
    END IF;
    
    -- Kiểm tra nhân viên tồn tại
    SELECT COUNT(*) INTO v_Count FROM NHANVIEN WHERE MaNV = p_MaNV;
    IF v_Count = 0 THEN
        RAISE_APPLICATION_ERROR(-20011, 'Nhân viên không tồn tại.');
    END IF;
    
    -- Kiểm tra công nợ của khách hàng
    SELECT TongTienNo INTO v_TongTienNo
    FROM KHACHHANG
    WHERE MaKH = p_MaKH;
    
    IF v_TongTienNo > 5000000 THEN
        RAISE_APPLICATION_ERROR(-20012, 'Khách hàng có công nợ vượt quá 5 triệu đồng, không thể tạo hợp đồng mới.');
    END IF;
    
    -- Thêm hợp đồng mới, trigger sẽ tạo mã tự động
    INSERT INTO HOPDONG (MaKH, MaNV, NgayLap, DiaChiGiao, TongTien, TrangThai)
    VALUES (p_MaKH, p_MaNV, SYSDATE, p_DiaChiGiao, 0, 'Đang thuê')
    RETURNING MaHD INTO p_MaHD;
    
    DBMS_OUTPUT.PUT_LINE('Đã tạo hợp đồng mới - Mã: ' || p_MaHD);
    DBMS_OUTPUT.PUT_LINE('Khách hàng: ' || p_MaKH || ' - Công nợ hiện tại: ' || v_TongTienNo || ' VNĐ');
END;
/

CREATE OR REPLACE PROCEDURE sp_ThemXeVaoHopDong(
    p_MaHD IN HOPDONG.MaHD%TYPE,
    p_MaXe IN XE.MaXe%TYPE,
    p_NgayBatDau IN CTHD.NgayBatDau%TYPE,
    p_NgayKetThuc IN CTHD.NgayKetThuc%TYPE
)
IS
    v_TrangThaiXe XE.TrangThai%TYPE;
    v_TrangThaiHD HOPDONG.TrangThai%TYPE;
    v_SoXeTrongHD NUMBER;
    v_Count NUMBER;
BEGIN
    -- Kiểm tra hợp đồng tồn tại và chưa hoàn thành
    SELECT TrangThai INTO v_TrangThaiHD
    FROM HOPDONG
    WHERE MaHD = p_MaHD;
    
    IF v_TrangThaiHD = 'Hoàn thành' THEN
        RAISE_APPLICATION_ERROR(-20030, 'Hợp đồng đã được hoàn thành, không thể thêm xe.');
    END IF;
    
    -- Kiểm tra số xe đã có trong hợp đồng này (tối đa 3 xe)
    SELECT COUNT(*) INTO v_SoXeTrongHD
    FROM CTHD
    WHERE MaHD = p_MaHD;
    
    -- Kiểm tra giới hạn 3 xe trong 1 hợp đồng
    IF v_SoXeTrongHD >= 3 THEN
        RAISE_APPLICATION_ERROR(-20031, 'Hợp đồng chỉ được thuê tối đa 3 xe.');
    END IF;
    
    -- Kiểm tra thời gian thuê hợp lý
    IF p_NgayBatDau < TRUNC(SYSDATE) THEN
        RAISE_APPLICATION_ERROR(-20032, 'Ngày bắt đầu phải từ hôm nay trở đi.');
    END IF;
    
    IF p_NgayKetThuc <= p_NgayBatDau THEN
        RAISE_APPLICATION_ERROR(-20033, 'Ngày kết thúc phải sau ngày bắt đầu.');
    END IF;
    
    SELECT COUNT(*)
    INTO v_Count
    FROM CTHD
    WHERE MaXe = p_MaXe
    AND (
        (p_NgayBatDau BETWEEN NgayBatDau AND NgayKetThuc)
        OR (p_NgayKetThuc BETWEEN NgayBatDau AND NgayKetThuc)
        OR (p_NgayBatDau <= NgayBatDau AND p_NgayKetThuc >= NgayKetThuc)
    );
    
    IF v_Count > 0 THEN
        RAISE_APPLICATION_ERROR(-20035, 'Xe đã được đặt trong khoảng thời gian này.');
    END IF;
    
    -- Thêm xe vào hợp đồng
    INSERT INTO CTHD (MaHD, MaXe, NgayBatDau, NgayKetThuc)
    VALUES (p_MaHD, p_MaXe, p_NgayBatDau, p_NgayKetThuc);
    
    
    DBMS_OUTPUT.PUT_LINE('Đã thêm xe ' || p_MaXe || ' vào hợp đồng ' || p_MaHD);
    DBMS_OUTPUT.PUT_LINE('Thời gian thuê: ' || TO_CHAR(p_NgayBatDau, 'DD/MM/YYYY') || ' đến ' || TO_CHAR(p_NgayKetThuc, 'DD/MM/YYYY'));
END;
/


COMMIT