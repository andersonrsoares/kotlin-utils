package br.com.andersonsoares.kotlinutils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.andersonsoares.utils.ExtensionActivityKt;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ExtensionActivityKt.showKeyboard(this);

       // ExtensionActivityKt.showKeyboard();
    }
}
