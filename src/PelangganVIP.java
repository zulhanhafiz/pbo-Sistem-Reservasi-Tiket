public class PelangganVIP extends Pelanggan {

    public PelangganVIP(String nama, String email) {
        super(nama, email);
    }

    @Override
    public double getDiskon() {
        return 0.2;
    }
}
