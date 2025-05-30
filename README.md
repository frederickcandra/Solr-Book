===============================
  Solr Book Service API
===============================

API sederhana untuk mengelola data buku menggunakan Apache Solr dan Spring Boot.

Base URL:
http://localhost:8080

--------------------------------
1. Get All Books
--------------------------------
Endpoint:
GET /books

Deskripsi:
Mengambil semua data buku dari Solr.

Contoh request curl:
curl -X GET http://localhost:8080/books

Contoh response:
[
  {
    "id": "1",
    "title": "Solr Com",
    "author": "Candra"
  },
  {
    "id": "2",
    "title": "Spring Boot Dasar",
    "author": "Budi"
  }
]

--------------------------------
2. Search Books by Keyword
--------------------------------
Endpoint:
GET /books/search?q={keyword}

Deskripsi:
Mencari buku berdasarkan kata kunci di field 'title' atau 'author'.

Contoh request curl:
curl -X GET "http://localhost:8080/books/search?q=Spring"

Contoh response:
[
  {
    "id": "2",
    "title": "Spring Boot Dasar",
    "author": "Budi"
  }
]

--------------------------------
3. Add New Book
--------------------------------
Endpoint:
POST /books

Deskripsi:
Menambahkan data buku baru ke Solr.

Headers:
Content-Type: application/json

Contoh request body JSON:
{
  "id": "3",
  "title": "Java Programming",
  "author": "Andi"
}

Contoh request curl:
curl -X POST http://localhost:8080/books -H "Content-Type: application/json" -d "{\"id\":\"3\",\"title\":\"Java Programming\",\"author\":\"Andi\"}"

Contoh response:
{
  "message": "Book added successfully"
}

--------------------------------
Catatan Penting:
- Pastikan Apache Solr berjalan di http://localhost:8983/solr dengan core 'mycore'.
- Data yang dimasukkan harus sesuai format schema Solr.
- Endpoint POST otomatis melakukan commit ke Solr agar data langsung bisa dicari.
- Gunakan tanda kutip yang sesuai di terminal/cmd saat menggunakan curl ("" atau '').

--------------------------------
Selesai
