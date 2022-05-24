package com.rain;

import java.io.File;
import java.io.FileFilter;

public class TextFileFilter implements FileFilter{

    /**
     * Define el tipo de archivo a indexar. archivo.txt
     * @param pathname 
     * @return True si es archivo .txt. False en otro caso
     */
    @Override
    public boolean accept(File pathname) {
        
        return pathname.getName().toLowerCase().endsWith(".txt");
    }
    
}
