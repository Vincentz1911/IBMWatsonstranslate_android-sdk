package com.ibm.watson.developer_cloud.android.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.ibm.watson.language_translator.v3.util.Language;

public class MainActivity extends AppCompatActivity {

    private EditText input;
    private Button translate;
    private TextView translatedText;
    private LanguageTranslator translationService;
    private String selectedTargetLanguage = Language.SPANISH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        translationService = initLanguageTranslatorService();

        RadioGroup targetLanguage = findViewById(R.id.target_language);
        input = findViewById(R.id.input);
        translate = findViewById(R.id.translate);
        translatedText = findViewById(R.id.translated_text);

        targetLanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.spanish:
                        selectedTargetLanguage = Language.SPANISH;
                        break;
                    case R.id.french:
                        selectedTargetLanguage = Language.FRENCH;
                        break;
                    case R.id.italian:
                        selectedTargetLanguage = Language.DANISH;
                        break;
                }
            }
        });

        translate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new TranslationTask().execute(input.getText().toString());
            }
        });
    }

    private void showTranslation(final String translation) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                translatedText.setText(translation);
            }
        });
    }

    private LanguageTranslator initLanguageTranslatorService() {
        Authenticator authenticator = new IamAuthenticator(
                "mYhzSNnKIjzjZBdsUSjqH7W5idEYY7IkcHyDaWS-jEPb");
        LanguageTranslator service = new LanguageTranslator("2018-05-01", authenticator);
        service.setServiceUrl("https://api.eu-de.language-translator.watson.cloud.ibm.com/instances/f2eb7b8b-2fc9-48d4-bd9a-1adf7139e778");
        return service;
    }

    private class TranslationTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            TranslateOptions translateOptions = new TranslateOptions.Builder()
                    .addText(params[0])
                    .source(Language.ENGLISH)
                    .target(selectedTargetLanguage)
                    .build();
            TranslationResult result
                    = translationService.translate(translateOptions).execute().getResult();
            String firstTranslation = result.getTranslations().get(0).getTranslation();
            showTranslation(firstTranslation);
            return "Did translate";
        }
    }
}
