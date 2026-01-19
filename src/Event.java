public class Event {
    public String namaEvent;
    public String lokasiEvent;
    public double hargaEvent;
    public int kapasitasAwal;
    public int sisaTiket;

    public Event(String nama, String lokasi, double harga, int kapasitas) {
        this.namaEvent = nama;
        this.lokasiEvent = lokasi;
        this.hargaEvent = harga;
        this.kapasitasAwal = kapasitas;
        this.sisaTiket = kapasitas;
    }

    public boolean cekKetersediaan(int jml) {
        return sisaTiket >= jml;
    }

    public void kurangiKapasitas(int jml) {
        this.sisaTiket -= jml;
    }

    // Getter sederhana
    public String getNamaEvent() { return namaEvent; }
    public int getSisa() { return sisaTiket; }
}