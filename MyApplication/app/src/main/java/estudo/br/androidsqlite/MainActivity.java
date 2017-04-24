package estudo.br.androidsqlite;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {
    EditText editRa, editNome, editCurso;
    Button btnAdd, btnDelete, btnModify, btnView, btnViewAll, btnShowInfo;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editRa = (EditText) findViewById(R.id.editRa);
        editNome = (EditText) findViewById(R.id.editNome);
        editCurso = (EditText) findViewById(R.id.editCurso);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnModify = (Button) findViewById(R.id.btnModify);
        btnView = (Button) findViewById(R.id.btnView);
        btnViewAll = (Button) findViewById(R.id.btnViewAll);
        btnShowInfo = (Button) findViewById(R.id.btnShowInfo);

        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnModify.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnViewAll.setOnClickListener(this);
        btnShowInfo.setOnClickListener(this);
        db = openOrCreateDatabase("AlunoDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS aluno(ra VARCHAR, nome VARCHAR, curso VARCHAR);");

    }

    @Override
    public void onClick(View view) {

        if (view == btnAdd) {
            if (editRa.getText().toString().trim().length() == 0 || editNome.getText().toString().trim().length() == 0
                    || editCurso.getText().toString().trim().length() == 0) {
                showMessage("Erro", "Entre com valores válidos!");
                return;
            }
            db.execSQL("INSERT INTO aluno VALUES('" + editRa.getText() + "','" + editNome.getText() + "','"
                    + editCurso.getText() + "');");
            showMessage("Successo", "Aluno Incluído!");
            clearText();
        }

        if (view == btnDelete) {
            if (editRa.getText().toString().trim().length() == 0) {
                showMessage("Error", "Digite o RA!");
                return;
            }
            Cursor c = db.rawQuery("SELECT * FROM aluno WHERE ra='" + editRa.getText() + "'", null);
            if (c.moveToFirst()) {
                db.execSQL("DELETE FROM aluno WHERE ra='" + editRa.getText() + "'");
                showMessage("Successo", "Registro Excluído!");
            } else {
                showMessage("Error", "RA Inválido!");
            }
            clearText();
        }

        if (view == btnModify) {
            if (editRa.getText().toString().trim().length() == 0) {
                showMessage("Erro", "Informe o RA!");
                return;
            }
            Cursor c = db.rawQuery("SELECT * FROM aluno WHERE ra='" + editRa.getText() + "'", null);
            if (c.moveToFirst()) {
                db.execSQL("UPDATE aluno SET nome='" + editNome.getText() + "',curso='" + editCurso.getText()
                        + "' WHERE ra='" + editRa.getText() + "'");
                showMessage("Successo", "Aluno Alterado com Sucesso!");
            } else {
                showMessage("Erro", "RA Inválido!");
            }
            clearText();
        }

        if (view == btnView) {
            if (editRa.getText().toString().trim().length() == 0) {
                showMessage("Erro", "Informe o RA!");
                return;
            }
            Cursor c = db.rawQuery("SELECT * FROM aluno WHERE ra='" + editRa.getText() + "'", null);
            if (c.moveToFirst()) {
                editNome.setText(c.getString(1));
                editCurso.setText(c.getString(2));
            } else {
                showMessage("Erro", "RA Inválido!");
                clearText();
            }
        }

        if (view == btnViewAll) {
            Cursor c = db.rawQuery("SELECT * FROM aluno", null);
            if (c.getCount() == 0) {
                showMessage("Erro", "Nenhum Registro Encontrado!");
                return;
            }
            StringBuffer buffer = new StringBuffer();
            while (c.moveToNext()) {
                buffer.append("RA: " + c.getString(0) + "\n");
                buffer.append("Nome: " + c.getString(1) + "\n");
                buffer.append("Curso: " + c.getString(2) + "\n\n");
            }
            showMessage("Detalhes:", buffer.toString());
        }
        if (view == btnShowInfo) {
            showMessage("Exemplo com SQLite", "By EdsonMSouza");
        }
    }

    public void showMessage(String title, String message) {
        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clearText() {
        editRa.setText("");
        editNome.setText("");
        editCurso.setText("");
        editRa.requestFocus();
    }

}
