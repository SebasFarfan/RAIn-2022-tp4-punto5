package com.rain;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;


public class AppTester 
{
    String indexDir = "/home/sebas/Rain/Indices/";
    String dataDir = "/home/sebas/Rain/Data/";
    Indexer indexer;
    Searcher searcher;

    public static void main(String[] args) {
        AppTester tester;
        try {
            tester = new AppTester();
            System.out.println(LuceneConstants.BG_BLUE
                    + "----------------- Iniciando Proceso de indexación -----------------" + LuceneConstants.RESET);
            tester.createIndex();

            BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
            String terminoBuscar = "";
            boolean opcion = true;
            while (opcion) {
                System.out.println(LuceneConstants.CYAN
                        + "---------------------- Indexador con Lucene ----------------------" + LuceneConstants.RESET);
                System.out.print("Ingrese Palabra (o una frase entre comillas \" \"): ");
                terminoBuscar = entrada.readLine();

                // tester.search("InCo");
                tester.search(terminoBuscar);
                System.out
                        .print(LuceneConstants.YELLOW + "Desea Realizar otra consulta ?: y/n " + LuceneConstants.RESET);
                String resp = entrada.readLine().toLowerCase();
                if (!resp.equals("y")) {
                    opcion = false;
                }
            }

            entrada.close();
        } catch (IOException e) {
            e.printStackTrace();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void createIndex() throws IOException {
        deleteFiles(indexDir);

        indexer = new Indexer(indexDir);
        int numIndexed;
        long startTime = System.currentTimeMillis();
        numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
        long endTime = System.currentTimeMillis();
        indexer.close();
        System.out.println(LuceneConstants.YELLOW +
                numIndexed + " Archivos Indexados, Tiempo empleado: " + (endTime - startTime) + " ms"
                + LuceneConstants.RESET);
    }

    private void search(String searchQuery) throws IOException, ParseException {
        searcher = new Searcher(indexDir);
        long startTime = System.currentTimeMillis();
        TopDocs hits = searcher.search(searchQuery);
        long endTime = System.currentTimeMillis();

        System.out.println(LuceneConstants.YELLOW + hits.totalHits + LuceneConstants.RESET
                + " (documentos encontrados). Tiempo:" + (endTime - startTime) + " ms.");
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.getDocument(scoreDoc);
            System.out.println(LuceneConstants.GREEN + "Archivo: " + LuceneConstants.RESET
                    + doc.get(LuceneConstants.FILE_PATH));
        }
    }

    private void deleteFiles(String directorio) {

        File directorioContenedor = new File(directorio);
        File[] archivos = directorioContenedor.listFiles();
        for (File file : archivos) {
            file.delete();

        }
    }
}
