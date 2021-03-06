package com.uphyca;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.CancellationSignal;

public class LazyLoadingSQLiteQueryBuilder extends SQLiteQueryBuilder {

    private static final int BLOCK_SIZE = 100;
    
    private final List<Operations.Operation> op = new ArrayList<Operations.Operation>();
    private final Context context;
    private final Uri uri;

    public LazyLoadingSQLiteQueryBuilder(Context context,
                                         Uri uri) {
        this.context = context;
        this.uri = uri;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.database.sqlite.SQLiteQueryBuilder#getTables()
     */
    @Override
    public String getTables() {
        return super.getTables();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.database.sqlite.SQLiteQueryBuilder#appendWhere(java.lang.CharSequence
     * )
     */
    @Override
    public void appendWhere(CharSequence inWhere) {
        super.appendWhere(inWhere);
        op.add(new Operations.AppendWhere(inWhere));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.database.sqlite.SQLiteQueryBuilder#appendWhereEscapeString(java
     * .lang.String)
     */
    @Override
    public void appendWhereEscapeString(String inWhere) {
        super.appendWhereEscapeString(inWhere);
        op.add(new Operations.AppendWhereEscapeString(inWhere));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.database.sqlite.SQLiteQueryBuilder#buildQuery(java.lang.String[],
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public String buildQuery(String[] projectionIn,
                             String selection,
                             String groupBy,
                             String having,
                             String sortOrder,
                             String limit) {
        return super.buildQuery(projectionIn, selection, groupBy, having, sortOrder, limit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.database.sqlite.SQLiteQueryBuilder#buildQuery(java.lang.String[],
     * java.lang.String, java.lang.String[], java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    @Deprecated
    public String buildQuery(String[] projectionIn,
                             String selection,
                             String[] selectionArgs,
                             String groupBy,
                             String having,
                             String sortOrder,
                             String limit) {
        return super.buildQuery(projectionIn, selection, selectionArgs, groupBy, having, sortOrder, limit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.database.sqlite.SQLiteQueryBuilder#buildUnionSubQuery(java.lang
     * .String, java.lang.String[], java.util.Set, int, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String buildUnionSubQuery(String typeDiscriminatorColumn,
                                     String[] unionColumns,
                                     Set<String> columnsPresentInTable,
                                     int computedColumnsOffset,
                                     String typeDiscriminatorValue,
                                     String selection,
                                     String groupBy,
                                     String having) {
        return super.buildUnionSubQuery(typeDiscriminatorColumn, unionColumns, columnsPresentInTable, computedColumnsOffset, typeDiscriminatorValue, selection, groupBy, having);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.database.sqlite.SQLiteQueryBuilder#buildUnionSubQuery(java.lang
     * .String, java.lang.String[], java.util.Set, int, java.lang.String,
     * java.lang.String, java.lang.String[], java.lang.String, java.lang.String)
     */
    @Override
    @Deprecated
    public String buildUnionSubQuery(String typeDiscriminatorColumn,
                                     String[] unionColumns,
                                     Set<String> columnsPresentInTable,
                                     int computedColumnsOffset,
                                     String typeDiscriminatorValue,
                                     String selection,
                                     String[] selectionArgs,
                                     String groupBy,
                                     String having) {
        return super.buildUnionSubQuery(typeDiscriminatorColumn, unionColumns, columnsPresentInTable, computedColumnsOffset, typeDiscriminatorValue, selection, selectionArgs, groupBy, having);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.database.sqlite.SQLiteQueryBuilder#buildUnionQuery(java.lang.
     * String[], java.lang.String, java.lang.String)
     */
    @Override
    public String buildUnionQuery(String[] subQueries,
                                  String sortOrder,
                                  String limit) {
        return super.buildUnionQuery(subQueries, sortOrder, limit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.database.sqlite.SQLiteQueryBuilder#query(android.database.sqlite
     * .SQLiteDatabase, java.lang.String[], java.lang.String,
     * java.lang.String[], java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, android.os.CancellationSignal)
     */
    @Override
    public Cursor query(SQLiteDatabase db,
                        String[] projectionIn,
                        String selection,
                        String[] selectionArgs,
                        String groupBy,
                        String having,
                        String sortOrder,
                        String limit,
                        CancellationSignal cancellationSignal) {
        return new LazyLoadingCursor(context, uri, db, op, projectionIn, selection, selectionArgs, groupBy, having, sortOrder, cancellationSignal, BLOCK_SIZE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.database.sqlite.SQLiteQueryBuilder#query(android.database.sqlite
     * .SQLiteDatabase, java.lang.String[], java.lang.String,
     * java.lang.String[], java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public Cursor query(SQLiteDatabase db,
                        String[] projectionIn,
                        String selection,
                        String[] selectionArgs,
                        String groupBy,
                        String having,
                        String sortOrder,
                        String limit) {
        return query(db, projectionIn, selection, selectionArgs, groupBy, having, sortOrder, limit, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.database.sqlite.SQLiteQueryBuilder#query(android.database.sqlite
     * .SQLiteDatabase, java.lang.String[], java.lang.String,
     * java.lang.String[], java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Cursor query(SQLiteDatabase db,
                        String[] projectionIn,
                        String selection,
                        String[] selectionArgs,
                        String groupBy,
                        String having,
                        String sortOrder) {
        return query(db, projectionIn, selection, selectionArgs, groupBy, having, sortOrder, null, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.database.sqlite.SQLiteQueryBuilder#setCursorFactory(android.database
     * .sqlite.SQLiteDatabase.CursorFactory)
     */
    @Override
    public void setCursorFactory(CursorFactory factory) {
        super.setCursorFactory(factory);
        op.add(new Operations.SetCursorFactory(factory));
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.database.sqlite.SQLiteQueryBuilder#setDistinct(boolean)
     */
    @Override
    public void setDistinct(boolean distinct) {
        super.setDistinct(distinct);
        op.add(new Operations.SetDistinct(distinct));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.database.sqlite.SQLiteQueryBuilder#setTables(java.lang.String)
     */
    @Override
    public void setTables(String inTables) {
        super.setTables(inTables);
        op.add(new Operations.SetTables(inTables));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.database.sqlite.SQLiteQueryBuilder#setProjectionMap(java.util
     * .Map)
     */
    @Override
    public void setProjectionMap(Map<String, String> columnMap) {
        super.setProjectionMap(columnMap);
        op.add(new Operations.SetProjectionMap(columnMap));
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.database.sqlite.SQLiteQueryBuilder#setStrict(boolean)
     */
    @Override
    public void setStrict(boolean flag) {
        super.setStrict(flag);
        op.add(new Operations.SetStrict(flag));
    }
}
