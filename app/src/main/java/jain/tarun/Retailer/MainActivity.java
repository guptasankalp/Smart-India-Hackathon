package jain.tarun.Retailer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;

import java.util.Map;

import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.ui.AIButton;
import jain.tarun.Retailer.fragments.FpiNearbyViewPagerFragmentActivity;

public class MainActivity extends Activity implements View.OnClickListener{
    ImageButton drawer_menu,notify,search;
    DrawerLayout drawerLayout;
    TextView dashboard,home_option,profile_option,settings_option,changeLanguage_option,share_option,rate_option,logOut_option;
    FrameLayout framelay;
    View menu_view;
    String latitude="75.0";
    String longitude="25.0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final AIConfiguration config = new AIConfiguration("257cd8e17d844690ac5e271733f24c1c",
                AIConfiguration.SupportedLanguages.English,   //for voice and both for text
                AIConfiguration.RecognitionEngine.System);

        final EditText input=(EditText)findViewById(R.id.editText);
        ImageButton go=(ImageButton)findViewById(R.id.notification);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideSoftKeyboard(MainActivity.this);
                final AIDataService aiDataService = new AIDataService(config);

                final AIRequest aiRequest = new AIRequest();
                aiRequest.setQuery(input.getText().toString());

                new AsyncTask<AIRequest, Void, AIResponse>() {
                    @Override
                    protected AIResponse doInBackground(AIRequest... requests) {
                        final AIRequest request = requests[0];
                        try {
                            final AIResponse response = aiDataService.request(aiRequest);
                            return response;
                        } catch (AIServiceException e) {
                            Log.d("ApiAi", "onError from text");
                        }
                        return null;
                    }


                    @Override
                    protected void onPostExecute(AIResponse aiResponse) {
                        if (aiResponse != null) {
                            // process aiResponse here
                            //// TODO: 1/4/17 add textview
                            input.setText("Query:" + aiResponse.getResult().getResolvedQuery()+"\nResponse:"+aiResponse.getResult().getFulfillment().getSpeech());
//                            transaction = response.getText().toString();
                            if(input.getText().toString().split("\n")[1].equals("Response:you are being redirected to FPI having pickle"))
                            {  SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                SharedPreferences.Editor editor2 = settings.edit();
                                editor2.putString("product", "papad");
                                editor2.commit();

                                Intent intent = new Intent(MainActivity.this, FpiNearbyViewPagerFragmentActivity.class);
                                startActivity(intent);
                                //new RetrieveFeedTask().execute("http://www.yourdomain.com/serverside-script.php");

                            }
                            else if(input.getText().toString().split("\n")[1].equals("Response:you are being redirected to FPI having papad"))
                            {SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                SharedPreferences.Editor editor2 = settings.edit();
                                editor2.putString("product", "papad");
                                editor2.commit();
                                Intent intent = new Intent(MainActivity.this, FpiNearbyViewPagerFragmentActivity.class);
                                startActivity(intent);
                            }
                            else if(input.getText().toString().split("\n")[1].equals("Response:you are being redirected to FPI having paneer"))
                            {SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                SharedPreferences.Editor editor2 = settings.edit();
                                editor2.putString("product", "papad");
                                editor2.commit();
                                Intent intent = new Intent(MainActivity.this, FpiNearbyViewPagerFragmentActivity.class);
                                startActivity(intent);
                            }

                        }
                    }
                }.execute(aiRequest);
                input.setText("");
            }
        });




//Speech button
        AIButton aiButton = (AIButton) findViewById(R.id.micButton);


//for the speech recoggnition
        aiButton.initialize(config);

        aiButton.setResultsListener(new AIButton.AIButtonListener() {
            @Override
            public void onResult(AIResponse result) {
                Log.d("ApiAi", "onResult");
                String parameterString = "";
                if (result.getResult().getParameters() != null && !result.getResult().getParameters().isEmpty()) {
                    for (final Map.Entry<String, JsonElement> entry : result.getResult().getParameters().entrySet()) {
                        parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
                    }
                }
                            //// TODO: 1/4/17 textview
                input.setText("Query:" + result.getResult().getResolvedQuery()+"\nResponse:"+result.getResult().getFulfillment().getSpeech());

                if(input.getText().toString().split("\n")[1].equals("Response:you are being redirected to FPI having pickle"))
                {

                    Intent intent = new Intent(MainActivity.this, FpiNearbyViewPagerFragmentActivity.class);
                    startActivity(intent);
                    //new RetrieveFeedTask().execute("http://www.yourdomain.com/serverside-script.php");

                }
                else if(input.getText().toString().split("\n")[1].equals("Response:you are being redirected to FPI having papad"))
                {
                    Intent intent = new Intent(MainActivity.this, FpiNearbyViewPagerFragmentActivity.class);
                    startActivity(intent);
                }
                else if(input.getText().toString().split("\n")[1].equals("Response:you are being redirected to FPI having paneer"))
                {
                    Intent intent = new Intent(MainActivity.this, FpiNearbyViewPagerFragmentActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onError(AIError error) {
                Log.d("ApiAi", "onError");

                //// TODO: 1/4/17 textview
                input.setText(error.toString());
            }

            @Override
            public void onCancelled() {
                Log.d("ApiAi", "onCancelled");
            }
        });
//        latitude=getIntent().getExtras().getString("Latitude");
//        longitude=getIntent().getExtras().getString("Longitude");

        //System.out.println(latitude+","+longitude);
        initView();
    }

    private void initView() {
        drawer_menu=(ImageButton)findViewById(R.id.menu);
        drawer_menu.setOnClickListener(this);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);
        menu_view=(View) findViewById(R.id.Menu_view);

        notify=(ImageButton)findViewById(R.id.notification);
        framelay=(FrameLayout)findViewById(R.id.frame_lay);
        framelay.setOnClickListener(this);
        home_option=(TextView)findViewById(R.id.homeDrawer);
        /*profile_option=(TextView)findViewById(R.id.profileDrawer);
        settings_option=(TextView)findViewById(R.id.settingDrawer);
        changeLanguage_option=(TextView)findViewById(R.id.changeLanguage);*/
        share_option=(TextView)findViewById(R.id.shareappDrawer);
        rate_option=(TextView)findViewById(R.id.rateTheApp);
        logOut_option=(TextView)findViewById(R.id.logout);

    }

    public void UpdateDemand(View view){

}
    public void onClick(View v) {
        if(v==drawer_menu){
            if(!drawerLayout.isDrawerOpen(menu_view))
                drawerLayout.openDrawer(Gravity.LEFT);
            else
                drawerLayout.closeDrawer(menu_view);
        }

        else if(v==framelay)
        {
            if(drawerLayout.isDrawerOpen(menu_view))
                drawerLayout.closeDrawer(menu_view);
        }



    }
    public void FPInearby(View view){
        Intent intent=new Intent(getApplicationContext(), FpiNearbyViewPagerFragmentActivity.class);
        intent.putExtra("Latitude",String.valueOf(latitude));
        intent.putExtra("Longitude",String.valueOf(longitude));
        startActivity(intent);
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
    public void DashBoard(View view){
        Intent i=new Intent(getApplicationContext(), DashBoardFragmentActivity.class);
        startActivity(i);
    }
    public void Update_Demand(View view){
        Intent n=new Intent(getApplicationContext(),UpadteItemActivity.class);
        startActivity(n);
    }
    public void HOME(View view){
        Intent h=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(h);
        home_option.setSelected(true);
        /*profile_option.setSelected(false);
        settings_option.setSelected(false);
        changeLanguage_option.setSelected(false);*/
        share_option.setSelected(false);
        rate_option.setSelected(false);
        logOut_option.setSelected(false);
    }
   /* public void PROFILE(View view){
        home_option.setSelected(false);
        profile_option.setSelected(true);
        settings_option.setSelected(false);
        changeLanguage_option.setSelected(false);
        share_option.setSelected(false);
        rate_option.setSelected(false);
        logOut_option.setSelected(false);
    }
    public void SETTINGS(View view){
        home_option.setSelected(false);
        profile_option.setSelected(false);
        settings_option.setSelected(true);
        changeLanguage_option.setSelected(false);
        share_option.setSelected(false);
        rate_option.setSelected(false);
        logOut_option.setSelected(false);
    }
    public void CHANGELANGUAGE(View view){
        home_option.setSelected(false);
        profile_option.setSelected(false);
        settings_option.setSelected(false);
        changeLanguage_option.setSelected(true);
        share_option.setSelected(false);
        rate_option.setSelected(false);
        logOut_option.setSelected(false);
    }*/
    public void SHARE_THIS_APP(View view){
        home_option.setSelected(false);
       /* profile_option.setSelected(false);
        settings_option.setSelected(false);
        changeLanguage_option.setSelected(false);*/
        share_option.setSelected(true);
        rate_option.setSelected(false);
        logOut_option.setSelected(false);
    }
    public void RATE_THIS_APP(View view){
        home_option.setSelected(false);
        /*profile_option.setSelected(false);
        settings_option.setSelected(false);
        changeLanguage_option.setSelected(false);*/
        share_option.setSelected(false);
        rate_option.setSelected(true);
        logOut_option.setSelected(false);
    }
    public void LOGOUT(View view){
        home_option.setSelected(false);
/*        profile_option.setSelected(false);
        settings_option.setSelected(false);
        changeLanguage_option.setSelected(false);*/
        share_option.setSelected(false);
        rate_option.setSelected(false);
        logOut_option.setSelected(true);
    }
    public void Payment(View view){
        Intent j=new Intent(getApplicationContext(),PaymentActivity.class);
        startActivity(j);
    }
    public void PurchaseHistory(View view){
        Toast.makeText(getApplicationContext(), "purchase history section selected!",
                Toast.LENGTH_SHORT).show();
    }


}
