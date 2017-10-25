/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lxrtest.commonlib.genutil;

import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author som
 */
public class LXRCsvReader {
    private static final Log LOGGER = LogFactory.getLog(LXRCsvReader.class);
    
    private final List<String[]> allRows;
    
    public LXRCsvReader(String filePath) throws FileNotFoundException, IOException {
            //Build reader instance
            File file = new File(filePath);
            CSVReader reader = new CSVReader(new FileReader(file), ',', '"');
            
            //Read all rows at once
            allRows = reader.readAll();      
    }
    
    public JSONObject getFormatInJson() {
        String jsonString = null;
        String[] header = getHeader();
        if(allRows != null && !allRows.isEmpty()){
            jsonString = "[" + allRows.stream().skip(1)
                    .map(row -> {
                        return convertToJsonString(row, header);
            }).collect( Collectors.joining( "," )) + "]";
        }
        JSONObject jsonObject = null;
        try{
            jsonObject = new JSONObject(jsonString);
        }catch(JSONException e){
            LOGGER.error("Error in converting following string to JSONObject" + jsonString);
        }
        return jsonObject;
    }
    
    /**
     * Provides header as a String array
     * @return Header as String Array
     */
    public String[] getHeader() {
        String[] header = null;
        if(allRows != null && !allRows.isEmpty()){
            header = allRows.get(0);
        }
        return header;
    }

    public int getRecordCount(){
        int size = 0;
        if(allRows != null && !allRows.isEmpty()){
            size = allRows.size();
        }
        return size;
    }

    public JSONObject getRowInJson(int rowNo){
        String[] row = null;
        if(allRows != null && !allRows.isEmpty() && rowNo < allRows.size()){
            row = allRows.get(rowNo);
        }
        String[] header = getHeader();
        String jsonString = convertToJsonString(row, header);
        JSONObject jsonObject = null;
        try{
            jsonObject = new JSONObject(jsonString);
        }catch(JSONException e){
            LOGGER.error("Error in converting following string to JSONObject" + jsonString);
        }
        return jsonObject;
    }
    
    public String[] getRow(int rowNo) {
        String[] row = null;
        if(allRows != null && !allRows.isEmpty() && rowNo < allRows.size()){
            row = allRows.get(rowNo);
        }
        return row;
    }

    public String getCellValue(int rowNo, String columnName) throws UnsupportedOperationException {
        String[] header = getHeader();
        int colNo = Arrays.asList(header).indexOf(columnName);
        String value = null;
        if(colNo >= 0){
            value = getCellValue(rowNo, colNo);
        }
        return value;
    }

    public String getCellValue(int rowNo, int colNo) {
        String value = null;
        if(allRows != null && !allRows.isEmpty() && rowNo < allRows.size()){
            String [] row = allRows.get(rowNo);
            if(row.length <= colNo){
                value = row[colNo - 1];
            }
        }
        return value;
    }
    
    private String convertToJsonString(String[] row, String[] header){
        StringBuilder temprowString = new StringBuilder("{");
        for(int i = 0; i < row.length; i++){
            temprowString.append("\"").append(header[i]).append("\":\"").append(row[i]).append("\", ");
        }
        String rowString;
        if(row.length > 0){
            rowString = temprowString.substring(0, temprowString.length() - 2) + "}";
        }else{
            rowString = temprowString + "}";
        }
        return rowString;
    }
}
