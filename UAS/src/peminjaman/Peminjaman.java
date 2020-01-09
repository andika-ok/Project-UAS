/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peminjaman;
import bagianabstract.Denda;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import peminjaman.Anggota;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import koneksi.MySQLConnection;
import peminjaman.Book;
/**
 *
 * @author Andika
 */
public class Peminjaman extends Denda{
    int idPeminjaman;
    String bukuPinjam;
    String tglPinjam;
    String tglKembali;
    int denda;
    
    
    
    public String getBukuPinjam(){
        Book a1 = new Book();
        return this.bukuPinjam = a1.getNamaBuku();
    }
    
    public String getTglPinjam(){
        System.out.print("Masukkan tanggal pinjam buku : ");
        Scanner p = new Scanner(System.in);
        return this.tglPinjam = p.nextLine();
    } 
    
    public String getTglKembali(){
        System.out.print("Masukkan tanggal kembali buku : ");
        Scanner k = new Scanner(System.in);
        return this.tglPinjam = k.nextLine();
    } 
    
    @Override
    public void setDendaAnggota(){
        String stglAwal = this.tglPinjam;
        String stglAkhir = this.tglKembali;
        DateFormat dateAwal = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat dateAkhir = new SimpleDateFormat("dd/MM/yyyy");
         
        try {
            Date tglAwal = dateAwal.parse(stglAwal);
            Date tglAkhir = dateAkhir.parse(stglAkhir);
             
            Date TGLAwal = tglAwal;
            Date TGLAkhir = tglAkhir;
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(TGLAwal);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(TGLAkhir);
             
            String hasil = String.valueOf(daysBetween(cal1, cal2));
             
            System.out.println("Tanggal Awal  = " +stglAwal);
            System.out.println("Tanggal Akhir = " +stglAkhir);
            System.out.println("Selisih: " +hasil+ " hari");
            
            if (Integer.parseInt(hasil) > 7 ){
                this.denda = (Integer.parseInt(hasil) - 7) * 5000;
                System.out.println("Denda : " + denda);
            }
            else{
                System.out.println("Denda " + 0);
            }
             
        } catch (ParseException e) {
        }
    }
     
    private static long daysBetween(Calendar tanggalAwal, Calendar tanggalAkhir) {
        long lama = 0;
        Calendar tanggal = (Calendar) tanggalAwal.clone();
        while (tanggal.before(tanggalAkhir)) {
            tanggal.add(Calendar.DAY_OF_MONTH, 1);
            lama++;
        }
        return lama;
    }
    
    // insert data buku
    public void insert(MySQLConnection m, String bukuPinjam, String tglPinjam, String tglKembali, Integer denda){
        // lakukan koneksi ke mysql
        Connection koneksi = m.conn;
        
        // query sql untuk insert data buku
        String sql = "INSERT INTO peminjam (bukuPinjam, tglPinjam, tglKembali, denda) VALUES (?, ?, ?, ?)";
 
        try {
            PreparedStatement statement = koneksi.prepareStatement(sql);
            
            // mapping nilai parameter dari query sql nya (sesuai urutan)
            statement.setString(1, bukuPinjam);
            statement.setString(2, tglPinjam);
            statement.setString(3, tglKembali);
            statement.setString(4, denda.toString());

            // jalankan query (baca jumlah row affectednya)
            int rowsInserted = statement.executeUpdate();
            // jika ada row affected nya, maka status sukses
            if (rowsInserted > 0) {
                System.out.println("Insert data buku sukses");
            }

        } catch (SQLException ex) {
            // jika query gagal
            System.out.println("Insert data buku gagal");
        }
    }
    
    // delete data buku berdasarkan idbook
    public void delete(MySQLConnection m, Integer idBook){
        
        // query sql untuk hapus data buku berdasarkan idbook
        String sql = "DELETE FROM books WHERE idBook=?";
        // lakukan koneksi ke mysql
        Connection koneksi = m.conn;
        
        try {
            PreparedStatement statement;
            statement = koneksi.prepareStatement(sql);
            
            // mapping nilai parameter dari query sql nya
            statement.setString(1, idBook.toString());
            
            // jalankan query, dan lihat jumlah row affected nya
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Buku sudah berhasil dihapus");
            }
        } catch (SQLException ex) {
            System.out.println("Hapus data buku gagal");
        }
        
    }
    
    // update data buku berdasarkan idbook
    public void update(MySQLConnection m, Integer idBook, String title, String author, String publisher, Integer year){
        
        // query sql untuk update data buku berdasarkan idbook
        String sql = "UPDATE books SET bookTitle=?, bookAuthor=?, bookPublisher=?, yearPublished=? WHERE idBook=?";
        // lakukan koneksi ke mysql
        Connection koneksi = m.conn;
        
        try {
            PreparedStatement statement = koneksi.prepareStatement(sql);
            // mapping nilai parameter ke query sqlnya
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setString(3, publisher);
            statement.setString(4, year.toString());
            statement.setString(5, idBook.toString());

            // jalankan query, dan baca jumlah row affectednya
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Update data buku sukses");
            }
        } catch (SQLException ex) {
             System.out.println("Update data buku gagal");
        }
    }
    
    // tampilkan semua data buku
    public void select(MySQLConnection m){
        
        // query sql untuk select all data buku
        String sql = "SELECT * FROM books";
        
        // lakukan koneksi ke mysql
        Connection koneksi = m.conn;
        
        try {
            Statement statement = koneksi.createStatement();
            // jalankan query
            ResultSet result = statement.executeQuery(sql);

            // membuat header table untuk output
            System.out.println("==============================================================================");
            String header = "%3s %20s %20s %20s %4s";
            System.out.println(String.format(header, "ID", "JUDUL", "PENGARANG", "PENERBIT", "THN TERBIT"));
            System.out.println("------------------------------------------------------------------------------");
            
            // looping untuk baca data per record
            while (result.next()){
                // baca data buku per record
                String idBook = result.getString("idBook");
                String title = result.getString("bookTitle");
                String author = result.getString("bookAuthor");
                String publisher = result.getString("bookPublisher");
                String year = result.getString("yearPublished");
                // tampilkan data buku per record
                String output = "%3s %20s %20s %20s %4s";
                System.out.println(String.format(output, idBook, title, author, publisher, year));
            }
            
            System.out.println("==============================================================================");
            
        } catch (SQLException ex){
            System.out.println("Tampil data buku gagal");
        }
        
    }
    
    public static void main(String[] args) {
        MySQLConnection db1 = new MySQLConnection("localhost", "dbpeminjaman", "root", "");
        Peminjaman p1 = new Peminjaman();
        p1.getDenda();
        p1.insert(db1, p1.getBukuPinjam(), p1.getTglPinjam(), p1.getTglKembali(), p1.getDenda());
    }
}
