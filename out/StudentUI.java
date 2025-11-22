
import java.io.*;
import java.util.*;
import java.text.Collator;
import java.util.Locale;

public class StudentUI {
    private HashMap<String, Student> danhSachSinhVien;
    private ArrayList<Student> danhSachSapXep;
    private HashMap<String, ArrayList<Student>> indexHocLuc;
    private Scanner scanner;

    public StudentUI() {
        danhSachSinhVien = new HashMap<>();
        danhSachSapXep = new ArrayList<>();
        indexHocLuc = new HashMap<>();
        scanner = new Scanner(System.in, "UTF-8");
        taoSinhVienMau();
    }

    public void hienThiMenu() {
        System.out.println("\n===== QUẢN LÍ SINH VIÊN =====");
        System.out.println("1. Thêm sinh viên");
        System.out.println("2. Xóa sinh viên");
        System.out.println("3. Tìm kiếm theo MSV");
        System.out.println("4. Hiển thị tất cả sinh viên");
        System.out.println("5. Cập nhật điểm");
        System.out.println("6. Sinh viên đạt học bổng");
        System.out.println("7. Tìm theo kết quả học tập");
        System.out.println("0. Thoát");
        System.out.print("Chọn chức năng: ");
    }

    public void themSinhVien() throws IOException {
        System.out.println("\n--- THÊM SINH VIÊN ---");
        String maSo = nhapMaSoSinhVien();
        if (danhSachSinhVien.containsKey(maSo)) {
            System.out.println("Mã số đã tồn tại");
            return;
        }

        String hoTen = nhapHoTenSinhVien();
        double diemTB = nhapDiemTrungBinh();
        Student svMoi = new Student(maSo, hoTen, diemTB);
        danhSachSinhVien.put(maSo, svMoi);
        themVaoIndexHocLuc(svMoi);
        System.out.println("Thêm thành công");
    }

    public void xoaSinhVien() throws IOException {
        System.out.println("\n--- XÓA SINH VIÊN ---");
        String maSo = nhapMaSoSinhVien();

        if (danhSachSinhVien.containsKey(maSo)) {
            Student sv = danhSachSinhVien.get(maSo);

            String hocLuc = sv.getKetQuaHocTap();

            xoaKhoiIndexHocLuc(sv, hocLuc);

            danhSachSinhVien.remove(maSo);

            System.out.println("Xóa thành công");
        } else {
            System.out.println("Không tìm thấy sinh viên");
        }
    }

    public void timKiemTheoMSV() throws IOException {
        System.out.println("\n--- TÌM KIẾM SINH VIÊN ---");
        String maSo = nhapMaSoSinhVien();

        Student sv = danhSachSinhVien.get(maSo);
        if (sv != null) {
            int thuTu = timThuTuTrongDanhSach(sv);
            System.out.println("Thứ tự trong danh sách: " + thuTu);
            System.out.printf("%-5s %-12s %-25s %-8s %-15s %-12s\n",
                    "STT", "MSSV", "Họ Tên", "Điểm", "Kết Quả", "Học Bổng");
            System.out.printf("%-5d %-12s %-25s %-8.2f %-15s %-12s\n",
                    thuTu, sv.getMaSoSinhVien(), sv.getHoTen(),
                    sv.getDiemTrungBinh(), sv.getKetQuaHocTap(),
                    sv.coDatHocBong() ? "CÓ" : "KHÔNG");
        } else {
            System.out.println("Không tìm thấy sinh viên");
        }
    }

    public void hienThiTatCaSinhVien() {
        System.out.println("\n--- DANH SÁCH SINH VIÊN ---");

        if (danhSachSinhVien.isEmpty()) {
            System.out.println("Danh sách trống");
            return;
        }

        danhSachSapXep.clear();
        danhSachSapXep.addAll(danhSachSinhVien.values());

        Collator collator = Collator.getInstance(new Locale("vi", "VN"));
        collator.setStrength(Collator.PRIMARY);

        Collections.sort(danhSachSapXep, (sv1, sv2) -> {
            String ten1 = sv1.getHoTen().trim();
            String ten2 = sv2.getHoTen().trim();
            String[] parts1 = ten1.split("\\s+");
            String[] parts2 = ten2.split("\\s+");
            String lastName1 = parts1.length > 0 ? parts1[parts1.length - 1] : ten1;
            String lastName2 = parts2.length > 0 ? parts2[parts2.length - 1] : ten2;

            int cmp = collator.compare(lastName1, lastName2);
            if (cmp != 0)
                return cmp;
            return collator.compare(ten1, ten2);
        });

        System.out.printf("%-5s %-12s %-25s %-8s %-15s %-12s\n",
                "STT", "MSSV", "Họ Tên", "Điểm", "Kết Quả", "Học Bổng");
        int stt = 1;
        for (Student sv : danhSachSapXep) {
            System.out.printf("%-5d %-12s %-25s %-8.2f %-15s %-12s\n",
                    stt++, sv.getMaSoSinhVien(), sv.getHoTen(),
                    sv.getDiemTrungBinh(), sv.getKetQuaHocTap(),
                    sv.coDatHocBong() ? "CÓ" : "KHÔNG");
        }
        System.out.println("Tổng: " + danhSachSinhVien.size() + " sinh viên");
    }

    public void capNhatDiem() throws IOException {
        System.out.println("\n--- CẬP NHẬT ĐIỂM ---");
        String maSo = nhapMaSoSinhVien();

        Student sv = danhSachSinhVien.get(maSo);
        if (sv != null) {
            String hocLucCu = sv.getKetQuaHocTap();
            double diemMoi = nhapDiemTrungBinh();
            sv.setDiemTrungBinh(diemMoi);

            String hocLucMoi = sv.getKetQuaHocTap();
            if (!hocLucCu.equals(hocLucMoi)) {
                xoaKhoiIndexHocLuc(sv, hocLucCu);
                themVaoIndexHocLuc(sv);
            }

            System.out.println("Cập nhật thành công");
        } else {
            System.out.println("Không tìm thấy sinh viên");
        }
    }

    public void sinhVienHocBong() {
        System.out.println("\n--- SINH VIÊN HỌC BỔNG ---");

        ArrayList<Student> svHocBong = new ArrayList<>();
        for (Student sv : danhSachSinhVien.values()) {
            if (sv.coDatHocBong()) {
                svHocBong.add(sv);
            }
        }

        if (svHocBong.isEmpty()) {
            System.out.println("Không có sinh viên nào đạt học bổng");
            return;
        }

        Collections.sort(svHocBong, (sv1, sv2) -> Double.compare(sv2.getDiemTrungBinh(), sv1.getDiemTrungBinh()));

        System.out.printf("%-5s %-12s %-25s %-8s\n", "STT", "MSSV", "Họ Tên", "Điểm");
        int stt = 1;
        for (Student sv : svHocBong) {
            System.out.printf("%-5d %-12s %-25s %-8.2f\n",
                    stt++, sv.getMaSoSinhVien(), sv.getHoTen(), sv.getDiemTrungBinh());
        }
    }

    public void timTheoKetQuaHocTap() throws IOException {
        System.out.println("\n--- TÌM THEO KẾT QUẢ HỌC TẬP ---");
        System.out.println("1. XUẤT SẮC  2. GIỎI  3. KHÁ  4. TRUNG BÌNH  5. YẾU  6. KÉM");
        System.out.print("Chọn kết quả: ");

        int luaChon = Integer.parseInt(scanner.nextLine());
        String[] ketQua = { "", "XUẤT SẮC", "GIỎI", "KHÁ", "TRUNG BÌNH", "YẾU", "KÉM" };

        if (luaChon < 1 || luaChon > 6) {
            System.out.println("Chọn không hợp lệ");
            return;
        }

        String ketQuaTim = ketQua[luaChon];

        ArrayList<Student> ketQuaList = indexHocLuc.getOrDefault(ketQuaTim, new ArrayList<>());

        if (ketQuaList.isEmpty()) {
            System.out.println("Không có sinh viên nào");
            return;
        }

        System.out.println("\nKết quả: " + ketQuaTim);
        System.out.printf("%-5s %-12s %-25s %-8s\n", "STT", "MSSV", "Họ Tên", "Điểm");
        int stt = 1;
        for (Student sv : ketQuaList) {
            System.out.printf("%-5d %-12s %-25s %-8.2f\n",
                    stt++, sv.getMaSoSinhVien(), sv.getHoTen(), sv.getDiemTrungBinh());
        }
    }

    private int timThuTuTrongDanhSach(Student sv) {
        ArrayList<Student> danhSachSapXepTemp = new ArrayList<>();
        danhSachSapXepTemp.addAll(danhSachSinhVien.values());

        Collator collator = Collator.getInstance(new Locale("vi", "VN"));
        collator.setStrength(Collator.PRIMARY);

        Collections.sort(danhSachSapXepTemp, (sv1, sv2) -> {
            String ten1 = sv1.getHoTen().trim();
            String ten2 = sv2.getHoTen().trim();
            String[] parts1 = ten1.split("\\s+");
            String[] parts2 = ten2.split("\\s+");
            String lastName1 = parts1.length > 0 ? parts1[parts1.length - 1] : ten1;
            String lastName2 = parts2.length > 0 ? parts2[parts2.length - 1] : ten2;

            int cmp = collator.compare(lastName1, lastName2);
            if (cmp != 0)
                return cmp;
            return collator.compare(ten1, ten2);
        });

        return danhSachSapXepTemp.indexOf(sv) + 1;
    }

    private String nhapMaSoSinhVien() throws IOException {
        while (true) {
            System.out.print("Nhập mã số SV: ");
            String maSo = scanner.nextLine().trim();
            if (maSo.matches("\\d{10,}")) {
                return maSo;
            }
            System.out.println("Mã số phải gồm ít nhất 10 chữ số và không chứa ký tự khác");
        }
    }

    private double nhapDiemTrungBinh() throws IOException {
        while (true) {
            System.out.print("Nhập điểm trung bình: ");
            try {
                double diem = Double.parseDouble(scanner.nextLine());
                if (diem >= 0 && diem <= 10)
                    return diem;
                System.out.println("Điểm phải từ 0 đến 10");
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số");
            }
        }
    }

    private String nhapHoTenSinhVien() {
        while (true) {
            System.out.print("Nhập họ tên: ");
            String hoTen = scanner.nextLine().trim();
            if (!hoTen.isEmpty() && hoTen.matches("^[\\p{L}\\s]+$")) {
                return hoTen;
            }
            System.out.println("Họ tên không được để trống, chỉ được chứa chữ cái và khoảng trắng, không được chứa số hoặc ký tự đặc biệt");
        }
    }

    private void taoSinhVienMau() {
        String[][] data = {
                { "2411062411", "Nguyễn Đăng Cảnh", "6.3" },
                { "2411062558", "Nguyễn Bá Mạnh Cường", "4.2" },
                { "2411062467", "Nguyễn Cao Cường", "8.1" },
                { "2411062493", "Lê Công Dũng", "7.5" },
                { "2411062179", "Nguyễn Thành Hải Dương", "9.4" },
                { "2411062550", "Lê Minh Đức", "8.3" },
                { "2411062406", "Lâm Chí Hào", "7.1" },
                { "2411062192", "Trần Thị Hằng", "4.8" },
                { "2411062353", "Hồ Thu Hiền", "8.7" },
                { "2411062159", "Trần Trung Hiếu", "9.5" },
                { "2411062166", "Vy Thị Thanh Hòa", "9.0" },
                { "2411062615", "Đặng Quốc Huy", "5.2" },
                { "2411062304", "Phan Quang Huy", "7.9" },
                { "2411062548", "Nguyễn Thị Ngọc Huyền", "8.0" },
                { "2411062483", "Nguyễn Thế Bảo An", "8.5" },
                { "2411062645", "Lê Công Anh", "7.2" },
                { "2411062620", "Nguyễn Mai Anh", "6.8" },
                { "2411062650", "Nguyễn Ngọc Hải Anh", "9.1" },
                { "2411062526", "Phạm Bảo Hoàng Anh", "5.5" },
                { "2411062637", "Vũ Tuấn Anh", "8.9" },
                { "2411062326", "Bùi Thị Thanh Bình", "7.8" },
                { "2411062269", "Phạm Thái Dương", "5.9" },
                { "2411062490", "Vũ Đại Dương", "6.7" },
               
        };

        for (String[] svData : data) {
            Student sv = new Student(svData[0], svData[1], Double.parseDouble(svData[2]));
            danhSachSinhVien.put(sv.getMaSoSinhVien(), sv);
            themVaoIndexHocLuc(sv);
        }
    }

    private void themVaoIndexHocLuc(Student sv) {
        String hocLuc = sv.getKetQuaHocTap();
        indexHocLuc.putIfAbsent(hocLuc, new ArrayList<>());
        indexHocLuc.get(hocLuc).add(sv);
    }

    private void xoaKhoiIndexHocLuc(Student sv, String hocLuc) {
        ArrayList<Student> danhSach = indexHocLuc.get(hocLuc);
        if (danhSach != null) {
            danhSach.remove(sv);
            if (danhSach.isEmpty()) {
                indexHocLuc.remove(hocLuc);
            }
        }
    }

    public void chayChuongTrinh() {
        System.out.println("CHƯƠNG TRÌNH QUẢN LÝ SINH VIÊN");

        while (true) {
            hienThiMenu();
            try {
                int luaChon = Integer.parseInt(scanner.nextLine());

                switch (luaChon) {
                    case 1:
                        themSinhVien();
                        break;
                    case 2:
                        xoaSinhVien();
                        break;
                    case 3:
                        timKiemTheoMSV();
                        break;
                    case 4:
                        hienThiTatCaSinhVien();
                        break;
                    case 5:
                        capNhatDiem();
                        break;
                    case 6:
                        sinhVienHocBong();
                        break;
                    case 7:
                        timTheoKetQuaHocTap();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Lựa chọn không hợp lệ");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
