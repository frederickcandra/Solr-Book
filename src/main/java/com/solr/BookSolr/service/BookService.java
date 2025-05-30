package com.solr.BookSolr.service;

import com.solr.BookSolr.model.Book;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final SolrClient solrClient;

    public BookService(SolrClient solrClient) {
        this.solrClient = solrClient;
    }

    public void save(Book book) throws Exception {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", book.getId());
        doc.addField("title", book.getTitle());
        doc.addField("author", book.getAuthor());

        solrClient.add(doc);
        solrClient.commit();
    }

    public List<Book> findAll() {
        try {
            SolrQuery query = new SolrQuery("*:*");
            QueryResponse response = solrClient.query(query);
            SolrDocumentList docs = response.getResults();

            List<Book> books = new ArrayList<>();
            for (SolrDocument doc : docs) {
                String id = (String) doc.getFieldValue("id");

                List<String> titleList = (List<String>) doc.getFieldValue("title");
                String title = titleList != null && !titleList.isEmpty() ? titleList.get(0) : null;

                List<String> authorList = (List<String>) doc.getFieldValue("author");
                String author = authorList != null && !authorList.isEmpty() ? authorList.get(0) : null;

                books.add(new Book(id, title, author));
            }
            return books;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saat mengambil data dari Solr", e);
        }
    }

    public List<Book> searchByTitle(String title) throws Exception {
        SolrQuery query = new SolrQuery("title:" + title);
        QueryResponse response = solrClient.query(query);
        SolrDocumentList docs = response.getResults();

        List<Book> books = new ArrayList<>();
        for (SolrDocument doc : docs) {
            books.add(new Book(
                    (String) doc.getFieldValue("id"),
                    (String) doc.getFieldValue("title"),
                    (String) doc.getFieldValue("author")
            ));
        }
        return books;
    }

    public List<Book> searchByKeyword(String keyword) {
        try {
            String queryStr = String.format("title:%s OR author:%s", keyword, keyword);
            SolrQuery query = new SolrQuery(queryStr);

            QueryResponse response = solrClient.query(query);
            SolrDocumentList docs = response.getResults();

            List<Book> books = new ArrayList<>();
            for (SolrDocument doc : docs) {
                String id = (String) doc.getFieldValue("id");

                List<String> titleList = (List<String>) doc.getFieldValue("title");
                String title = titleList != null && !titleList.isEmpty() ? titleList.get(0) : null;

                List<String> authorList = (List<String>) doc.getFieldValue("author");
                String author = authorList != null && !authorList.isEmpty() ? authorList.get(0) : null;

                books.add(new Book(id, title, author));
            }
            return books;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saat mencari data di Solr", e);
        }
    }
}
