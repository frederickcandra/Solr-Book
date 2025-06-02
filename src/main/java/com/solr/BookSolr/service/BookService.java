package com.solr.BookSolr.service;

import com.solr.BookSolr.model.Book;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    public void saveAll(List<Book> books) throws Exception {
        List<SolrInputDocument> documents = new ArrayList<>();
        for (Book book : books) {
            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("id", book.getId());
            doc.addField("title", book.getTitle());
            doc.addField("author", book.getAuthor());
            documents.add(doc);
        }

        int batchSize = 500;
        for (int i = 0; i < documents.size(); i += batchSize) {
            int end = Math.min(i + batchSize, documents.size());
            solrClient.add(documents.subList(i, end));
        }

        solrClient.commit();
    }

    public List<Book> findAll() {
        try {
            SolrQuery query = new SolrQuery("*:*");
            QueryResponse response = solrClient.query(query);
            return convertToBooks(response.getResults());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saat mengambil data dari Solr", e);
        }
    }

    public List<Book> searchByKeyword(String keyword) {
        try {
            String queryStr = String.format("title:%s OR author:%s", keyword, keyword);
            SolrQuery query = new SolrQuery(queryStr);
            QueryResponse response = solrClient.query(query);
            return convertToBooks(response.getResults());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saat mencari data di Solr", e);
        }
    }

    private List<Book> convertToBooks(SolrDocumentList docs) {
        List<Book> books = new ArrayList<>();
        for (SolrDocument doc : docs) {
            String id = (String) doc.getFieldValue("id");

            String title = getFirstFieldValue(doc.getFieldValue("title"));
            String author = getFirstFieldValue(doc.getFieldValue("author"));

            books.add(new Book(id, title, author));
        }
        return books;
    }

    private String getFirstFieldValue(Object fieldValue) {
        if (fieldValue instanceof List) {
            List<?> list = (List<?>) fieldValue;
            return list.isEmpty() ? null : String.valueOf(list.get(0));
        }
        return fieldValue != null ? String.valueOf(fieldValue) : null;
    }
}
