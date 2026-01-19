public class Reservasi {
    private Pelanggan pelanggan;
    private Event event;
    private int jumlahTiket;
    private double hargaTiket = 100000;

    public Reservasi(Pelanggan pelanggan, Event event, int jumlahTiket, double hargaPerTiket) {
        this.pelanggan = pelanggan;
        this.event = event;
        this.jumlahTiket = jumlahTiket;
    }

    public String prosesReservasi() {
        if (event.cekKetersediaan(jumlahTiket)) {
            event.kurangiKapasitas(jumlahTiket);

            double total = jumlahTiket * hargaTiket;
            double diskon = total * pelanggan.getDiskon();
            double bayar = total - diskon;

            return "Reservasi Berhasil!\n" +
                    "Nama        : " + pelanggan.getNama() + "\n" +
                    "Event       : " + event.getNamaEvent() + "\n" +
                    "Jumlah Tiket: " + jumlahTiket + "\n" +
                    "Total       : Rp " + total + "\n" +
                    "Diskon      : Rp " + diskon + "\n" +
                    "Bayar       : Rp " + bayar;
        } else {
            return "Reservasi Gagal!\nTiket tidak mencukupi.";
        }
    }
}
