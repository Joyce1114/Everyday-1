package compsci290.edu.duke.myeveryday.util;

import android.os.AsyncTask;
import android.util.Log;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsResult;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.SentimentOptions;

import java.util.List;

/**
 * Created by Divya on 4/24/17.
 *
 * NaturalLanguageTask is an AsyncTask that is used to make an API call to IBM Watson's Natural Language Understanding API
 */

public class NaturalLanguageTask extends AsyncTask<String,Void,Double> {

    private NaturalLanguageUnderstanding mService;
    private String mInput;
    private Double mSentimentScore = 0.0;


    public NaturalLanguageTask(String input) {mInput = input;}

    protected void onPreExecute() {

    }

    protected Double doInBackground(String... input) {
        mService = new NaturalLanguageUnderstanding(
                NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
                "df206c3c-914d-49cd-8812-e6ab2bb487c6",
                "Q3UHY0dR3dsv"
        );

        EntitiesOptions entitiesOptions = new EntitiesOptions.Builder()
                .emotion(true)
                .sentiment(true)
                .limit(2)
                .build();

        KeywordsOptions keywordsOptions = new KeywordsOptions.Builder()
                .emotion(true)
                .sentiment(true)
                .limit(2)
                .build();


        Features features = new Features.Builder()
                .entities(entitiesOptions)
                .keywords(keywordsOptions)
                .build();

        AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                .text(mInput)
                .features(features)
                .build();

        AnalysisResults response = mService
                .analyze(parameters)
                .execute();

        SentimentOptions sentiment = new SentimentOptions.Builder()
                .build();

        List<KeywordsResult> keywordResults = response.getKeywords();
        if(keywordResults.size() > 0) {
            mSentimentScore = keywordResults.get(0).getSentiment().getScore();
        }


        return mSentimentScore;

    }

    protected void onPostExecute(String response) {
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
        Log.i("INFO", response);
    }



}
