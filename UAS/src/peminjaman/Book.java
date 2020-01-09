/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peminjaman;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import koneksi.MySQLConnection;
import java.util.Scanner;
/**
 *
 * @author Andika
 */
public class Book {
    // atribut buku
    
    public int idBuku;
    public String judulBuku;
    public String namaPenulis;
    public String penerbit;
    public Integer tahunTerbit;
    
    // constructor
    
    public String getNamaBuku(){
        System.out.print("Masukkan judul buku : ");
        Scanner b = new Scanner(System.in);
        return this.judulBuku = b.nextLine();
    }
    
    public String getNamaPenulis(){
        System.out.println("Masukkan nama penulis buku : ");
        Scanner p = new Scanner(System.in);
        return this.namaPenulis = p.nextLine();
    }
    
    public String getPenerbit(){
        System.out.println("Masukkan nama penerbit buku : ");
        Scanner p = new Scanner(System.in);
        return this.penerbit = p.nextLine();
    }
    
    public Integer getTahunTerbit(){
        System.out.println("Masukkan tahun terbit buku : ");
        Scanner t = new Scanner(System.in);
        return this.tahunTerbit = t.nextInt();
    }
    
    // insert data buku
    public void insert(MySQLConnection m, String judulBuku, String penulisBuku, String penerbit, Integer tahunTerbit){
        // lakukan koneksi ke mysql
        Connection koneksi = m.conn;
        
        // query sql untuk insert data buku
        String sql = "INSERT INTO buku (judulBuku, penulisBuku, penerbit, tahunTerbit) VALUES (?, ?, ?, ?)";
 
        try {
            PreparedStatement statement = koneksi.prepareStatement(sql);
            
            // mapping nilai parameter dari query sql nya (sesuai urutan)
            statement.setString(1, judulBuku);
            statement.setString(2, penulisBuku);
            statement.setString(3, penerbit);
            statement.setString(4, tahunTerbit.toString());

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
    public void delete(MySQLConnection m, Integer idBuku){
        
        // query sql untuk hapus data buku berdasarkan idbook
        String sql = "DELETE FROM buku WHERE idBuku=?";
        // lakukan koneksi ke mysql
        Connection koneksi = m.conn;
        
        try {
            PreparedStatement statement;
            statement = koneksi.prepareStatement(sql);
            
            // mapping nilai parameter dari query sql nya
            statement.setString(1, idBuku.toString());
            
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
    public void update(MySQLConnection m, Integer idBuku, String judulBuku, String namaPenulis, String penerbit, Integer tahunTerbit){
        
        // query sql untuk update data buku berdasarkan idbook
        String sql = "UPDATE buku SET idBuku=?, judulBuku =?, namaPenulis=?, penerbit=?, tahunTerbit=? WHERE idBuku=?";
        // lakukan koneksi ke mysql
        Connection koneksi = m.conn;
        
        try {
            PreparedStatement statement = koneksi.prepareStatement(sql);
            // mapping nilai parameter ke query sqlnya
            statement.setString(1, judulBuku);
            statement.setString(2, namaPenulis);
            statement.setString(3, penerbit);
            statement.setString(4, tahunTerbit.toString());

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
        String sql = "SELECT * FROM buku";
        
        // lakukan koneksi ke mysql
        Connection koneksi = m.conn;
        
        try {
            Statement statement = koneksi.createStatement();
            // jalankan query
            ResultSet result = statement.executeQuery(sql);

            // membuat header table untuk output
            System.out.println("==============================================================================");
            String header = "%3s %20s %20s %20s %4s";
            System.out.println(String.format(header, "ID", "JUDUL", "PENULIS", "PENERBIT", "THN TERBIT"));
            System.out.println("------------------------------------------------------------------------------");
            
            // looping untuk baca data per record
            while (result.next()){
                // baca data buku per record
                String idBuku = result.getString("idBuku");
                String judulBuku = result.getString("judulBuku");
                String namaPenulis = result.getString("penulisBuku");
                String penerbit = result.getString("penerbit");
                String tahunTerbit = result.getString("tahunTerbit");
                // tampilkan data buku per record
                String output = "%3s %20s %20s %20s %4s";
                System.out.println(String.format(output, idBuku, judulBuku, namaPenulis, penerbit, tahunTerbit));
            }
            
            System.out.println("==============================================================================");
            
        } catch (SQLException ex){
            System.out.println("Tampil data buku gagal");
        }
        
    }
    
    public static void main(String[] args) {
        MySQLConnection db1 = new MySQLConnection("localhost", "dbpeminjaman", "root", "");
        Book b1 = new Book();
        b1.insert(db1, b1.getNamaBuku(), b1.getNamaPenulis(), b1.getPenerbit(), b1.getTahunTerbit());
    }
}

