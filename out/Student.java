
public class Student {
    private String maSoSinhVien;
    private String hoTen;
    private double diemTrungBinh;
    private String ketQuaHocTap;
    
    public Student(String maSoSinhVien, String hoTen, double diemTrungBinh) {
        this.maSoSinhVien = maSoSinhVien;
        this.hoTen = hoTen;
        this.diemTrungBinh = diemTrungBinh;
        this.ketQuaHocTap = tinhKetQuaHocTap(diemTrungBinh);
    }
    
    private String tinhKetQuaHocTap(double diem) {
        if (diem >= 9.0) return "XUẤT SẮC";
        else if (diem >= 8.0) return "GIỎI";
        else if (diem >= 7.0) return "KHÁ";
        else if (diem >= 5.0) return "TRUNG BÌNH";
        else if (diem >= 3.5) return "YẾU";
        else return "KÉM";
    }
    
    public String getMaSoSinhVien() { 
        return maSoSinhVien; 
    }
    public String getHoTen() {
         return hoTen; 
    }
    public double getDiemTrungBinh() {
         return diemTrungBinh; }
    public String getKetQuaHocTap() { 
        return ketQuaHocTap; 
    }
    
    public void setDiemTrungBinh(double diemTrungBinh) {
        this.diemTrungBinh = diemTrungBinh;
        this.ketQuaHocTap = tinhKetQuaHocTap(diemTrungBinh);
    }   
    
    public boolean coDatHocBong() {
        return diemTrungBinh >= 9.0;
    }
}