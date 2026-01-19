public class Pelanggan {
    protected String nama;
    protected String email;

    public Pelanggan(String nama, String email) {
        this.nama = nama;
        this.email = email;
    }

    public String getNama() {
        return nama;
    }

    public double getDiskon() {
        return 0.0;
    }
}
