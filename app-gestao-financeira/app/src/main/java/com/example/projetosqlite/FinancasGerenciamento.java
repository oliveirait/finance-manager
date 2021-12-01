package com.example.projetosqlite;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class FinancasGerenciamento extends Activity {
    private EditText edtcod, edtdescricao, edtdata, edtvalor;
    private ImageView limpar, salvar, excluir, alterar;
    private Button datahoje;
    private ListView lista;
    private RadioGroup rg;
    private RadioButton r0, r1;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    private InputMethodManager imm;
    private BancoDados db = new BancoDados(this);
    private String tp;
    private TextView txttotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.financasgerenciamento);
        rg = (RadioGroup) findViewById(R.id.radioGroup1);
        edtcod = (EditText) findViewById(R.id.edtCod);
        edtdescricao = (EditText) findViewById(R.id.edtdesc);
        edtdata = (EditText) findViewById(R.id.edtData);
        edtvalor = (EditText) findViewById(R.id.edtValor);
        limpar = (ImageView) findViewById(R.id.btnLimpar);
        salvar = (ImageView) findViewById(R.id.btnSalvar);
        alterar = (ImageView) findViewById(R.id.btnAlterar);
        excluir = (ImageView) findViewById(R.id.btnExcluir);
        lista = (ListView) findViewById(R.id.listViewFinancas);
        r0 = (RadioButton) findViewById(R.id.radio0);
        r1 = (RadioButton) findViewById(R.id.radio1);
        txttotal = (TextView) findViewById(R.id.txttotal);
        datahoje = (Button) findViewById(R.id.btnData);
        Date agora = new Date();
        SimpleDateFormat dataBr = new SimpleDateFormat("dd/MM/yyyy");
        String dataformatada = dataBr.format(agora);
        edtdata.setText(dataformatada);
        txttotal.setText("" + calcTotal());
// esconde o teclado
        imm = (InputMethodManager)
                this.getSystemService(Service.INPUT_METHOD_SERVICE);
// lista os campos
        listarTipos();
// botão limpar
        limpar.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                limparCampos();
            }
        });
// botão de hoje
        datahoje.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Date agora = new Date();
                SimpleDateFormat dataBr = new SimpleDateFormat("dd/MM/yyyy");
                String dataformatada = dataBr.format(agora);
                edtdata.setText(dataformatada);
            }
        });
// botão alterar
        alterar.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String c = edtcod.getText().toString();
                    String des = edtdescricao.getText().toString();
                    String dat = edtdata.getText().toString();
                    switch (rg.getCheckedRadioButtonId()) {
                        case R.id.radio0:
                            tp = "Despesa";
                            break;
                        case R.id.radio1:
                            tp = "Receita";
                            break;
                        default:
                            break;
                    }
                    String v1 = edtvalor.getText().toString();
                    db.atualizarTipo(new Financas(Integer.parseInt(c), des,
                            dat, tp, Double.parseDouble(v1)));
                    Toast.makeText(v.getContext(), "Alterado com Sucesso",
                            Toast.LENGTH_LONG).show();
                    listarTipos();
                    limparCampos();
                    escondeTeclado();
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), "Valores inválidos",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
// botão salvar
        salvar.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String des = edtdescricao.getText().toString();
                    String dat = edtdata.getText().toString();
                    double v1=Double.parseDouble(edtvalor.getText().toString());
                    switch (rg.getCheckedRadioButtonId()) {
                        case R.id.radio0:
                            tp = "Despesa";
                            v1 = v1 * -1;
                            break;
                        case R.id.radio1:
                            tp = "Receita";
                            break;
                        default:
                            break;
                    }
                    // inserir
                    db.addTipo(new Financas(des, dat, tp, v1));
                    Toast.makeText(v.getContext(), "Adicionado com Sucesso",
                            Toast.LENGTH_LONG).show();
                    listarTipos();
                    limparCampos();
                    escondeTeclado();
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), "Valores inválidos",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
// botão excluir
        excluir.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c = edtcod.getText().toString();
                if (c.isEmpty()) {
                    Toast.makeText(v.getContext(),"Nenhum tipo foi selecionado", Toast.LENGTH_LONG).show();
                } else {
                    Financas t = new Financas();
                    t.setCod_tipo(Integer.parseInt(edtcod.getText().toString()));
                    db.apagarTipo(t);
                    Toast.makeText(v.getContext(), "Excluído com sucesso",
                            Toast.LENGTH_LONG).show();
                    listarTipos();
                    limparCampos();
                }
            }
        });
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String conteudo = (String) lista.getItemAtPosition(position);
                String codigo = conteudo.substring(0, conteudo.indexOf("-"));
                Financas t = db.selecionarTipo(Integer.parseInt(codigo.replace(" ",
                        "")));
                edtcod.setText("" + t.getCod_tipo());
                edtdescricao.setText(t.getDescricao());
                edtdata.setText(t.getData());
                if (t.getTipo().equals("Despesa")) {
                    r0.setChecked(true);
                    r1.setChecked(false);
                } else if (t.getTipo().equals("Receita")) {
                    r0.setChecked(false);
                    r1.setChecked(true);
                }
                edtvalor.setText("" + t.getValor());
            }
        });
    }
    public void escondeTeclado() {
        imm.hideSoftInputFromWindow(edtdescricao.getWindowToken(), 0);
    }
    public void limparCampos() {
        edtcod.setText("");
        edtdescricao.setText("");
        edtdata.setText("");
        edtvalor.setText("");
        edtdescricao.requestFocus();
    }
    public void listarTipos() {
        List<Financas> tipos = db.listaTipos();
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(FinancasGerenciamento.this,
                android.R.layout.simple_list_item_1, arrayList);
        lista.setAdapter(adapter);
        DecimalFormat df = new DecimalFormat("###,##0.00");
        for (Financas t : tipos) {
            arrayList.add(t.getCod_tipo() + " - " + t.getDescricao() +
                            " - " +
                    " - " + t.getData() + " - " + t.getTipo() + " R$ " +
                    df.format(t.getValor()));
            adapter.notifyDataSetChanged();
        }
        double total = 0;
        for (Iterator<Financas> iterator = tipos.iterator();
             iterator.hasNext();) {
            Financas item = iterator.next(); // pega o item da lista
            total += item.getValor(); // metodo do POJO
            txttotal.setText("Total R$" + df.format(total)); }
    }
    public double calcTotal() {
        double count = 0;
        for (int i = 0; i <= lista.getCount() - 1; i++) {
            count +=
                    Double.parseDouble(lista.getItemAtPosition(4).toString());
        }
        return count;
    }
}