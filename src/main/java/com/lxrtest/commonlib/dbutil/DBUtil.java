/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lxrtest.commonlib.dbutil;

import java.util.List;

/**
 *
 * @author som
 */
public interface DBUtil {
    String [] getQueryResultArray(String sql);
    List getQueryResultList(String sql); 
    boolean insertData(String query);
    boolean loadData(String filePath, String tableName);
    boolean updateData(String query);
    boolean deleteData(String query);
    boolean runProcedure(String procedure);
}
