package com.example.projetosqlite;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BancoDados extends SQLiteOpenHelper {
    private static int VERSAO_BANCO = 1;
    private static String BANCO_TIPO = "controle_financeiro1";
    private static String TBL_financa = "financa";
    private static String COL_COD = "cod_financa";
    private static String COL_DESC = "descricao";
    private static String COL_DATA = "data";
    private static String COL_TIPO = "tipo";
    private static String COL_VALOR = "valor";
    public BancoDados(Context context) {
        super(context, BANCO_TIPO, null, VERSAO_BANCO);
        // TODO Auto-generated constructor stub
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String query = "CREATE TABLE " + TBL_financa + "(" + COL_COD
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_DESC
                + " TEXT UNIQUE NOT NULL, " + COL_DATA + " TEXT, " + COL_TIPO
                + " TEXT, " + COL_VALOR + " real not null );";
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }
    // CRUD
    void addTipo(Financas t) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valor = new ContentValues();
        valor.put(COL_DESC, t.getDescricao());
        valor.put(COL_DATA, t.getData());
        valor.put(COL_TIPO, t.getTipo());
        valor.put(COL_VALOR, t.getValor());
        db.insert(TBL_financa, null, valor);
        db.close();
    }
    void apagarTipo(Financas t) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_financa, COL_COD + " = ?",
                new String[] { String.valueOf(t.getCod_tipo()) });
        db.close();
    }
    Financas selecionarTipo(int codigo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TBL_financa, new String[] { COL_COD,
                        COL_DESC, COL_DATA,COL_TIPO, COL_VALOR }, COL_COD + " = ? ",
                new String[] { String.valueOf(codigo) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Financas t1 = new Financas(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                Double.parseDouble(cursor.getString(4)));
        return t1;
    }
    void atualizarTipo(Financas t) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valor = new ContentValues();
        valor.put(COL_DESC, t.getDescricao());
        valor.put(COL_DATA, t.getData());
        valor.put(COL_TIPO, t.getTipo());
        valor.put(COL_VALOR, t.getValor());
        db.update(TBL_financa, valor, COL_COD + " = ?",
                new String[] { String.valueOf(t.getCod_tipo()) });
        // db.close();
    }
    public List<Financas> listaTipos() {
        List<Financas> tipos = new ArrayList<Financas>();
        String query = "SELECT * FROM " + TBL_financa;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                Financas t = new Financas();
                t.setCod_tipo(Integer.parseInt(c.getString(0)));
                t.setDescricao(c.getString(1));
                t.setTipo(c.getString(2));
                t.setData(c.getString(3));
                t.setValor(Double.parseDouble(c.getString(4)));
                tipos.add(t);
            } while (c.moveToNext());
        }
        return tipos;
    }
}