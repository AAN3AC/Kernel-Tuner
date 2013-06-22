/*
 * Copyright 2012 two forty four a.m. LLC <http://www.twofortyfouram.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * <http://www.apache.org/licenses/LICENSE-2.0>
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package rs.pedjaapps.KernelTuner.ui;

import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.KernelTuner.Constants;
import rs.pedjaapps.KernelTuner.helpers.DatabaseHandler;
import rs.pedjaapps.KernelTuner.entry.Profile;
import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.bundle.BundleScrubber;
import rs.pedjaapps.KernelTuner.bundle.PluginBundleManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.twofortyfouram.locale.BreadCrumber;

/**
 * This is the "Edit" activity for a Locale Plug-in.
 */
public final class EditActivity extends Activity
{

    /**
     * Help URL, used for the {@link R.id#twofortyfouram_locale_menu_help} menu item.
     */
    
    private static final String HELP_URL = "http://kerneltuner.pedjaapps.in.rs/faq"; //$NON-NLS-1$

    private String profile;
    /**
     * Flag boolean that can only be set to true via the "Don't Save"
     * {@link R.id#twofortyfouram_locale_menu_dontsave} menu item in
     * {@link #onMenuItemSelected(int, MenuItem)}.
     * <p>
     * If true, then this {@code Activity} should return {@link Activity#RESULT_CANCELED} in {@link #finish()}.
     * <p>
     * If false, then this {@code Activity} should generally return {@link Activity#RESULT_OK} with extras
     * {@link com.twofortyfouram.locale.Intent#EXTRA_BUNDLE} and
     * {@link com.twofortyfouram.locale.Intent#EXTRA_STRING_BLURB}.
     * <p>
     * There is no need to save/restore this field's state when the {@code Activity} is paused.
     */
    private boolean mIsCancelled = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /*
         * A hack to prevent a private serializable classloader attack
         */
        BundleScrubber.scrub(getIntent());
        BundleScrubber.scrub(getIntent().getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE));

        setContentView(R.layout.locale_plugin);
        setTitle(BreadCrumber.generateBreadcrumb(getApplicationContext(), getIntent(), getString(R.string.plugin_name)));
        

		 DatabaseHandler db = new DatabaseHandler(this);
		 List<String> profileList = new ArrayList<String>();
		 List<Profile> profiles = db.getAllProfiles();
		 for(Profile p : profiles){
			 profileList.add(p.getName());
		 }
		 Spinner profSpinner = (Spinner)findViewById(R.id.bg);
		ArrayAdapter<String> profileAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, profileList);
		
		profileAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		profSpinner.setAdapter(profileAdapter);


		profSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					profile = parent.getItemAtPosition(pos).toString();
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{

				}
			});
		((Button)findViewById(R.id.ok)).setOnClickListener(new Listener(0));
		((Button)findViewById(R.id.cancel)).setOnClickListener(new Listener(0));
		((Button)findViewById(R.id.help)).setOnClickListener(new Listener(0));
		
    }

    class Listener implements OnClickListener{

    	int code;
    	
    	public Listener(int code){
    		this.code = code;
    	}
    	
		@Override
		public void onClick(View v) {
			switch(code){
			case 0:
				finish();
				return;
			case 1:
				mIsCancelled = true;
	            finish();
				return;
			case 2:
				try
	            {
	                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(HELP_URL)));
	            }
	            catch (final Exception e)
	            {
	                if (Constants.IS_LOGGABLE)
	                {
	                    Log.e(Constants.LOG_TAG, "Couldn't start Activity", e);
	                }
	            }
				return;
			}
			
		}
    	
    }
    
    

    @Override
    public void finish()
    {
        if (mIsCancelled)
        {
            setResult(RESULT_CANCELED);
        }
        else
        {
            //final String message = ((EditText) findViewById(android.R.id.text1)).getText().toString();

            /*
             * If the message is of 0 length, then there isn't a setting to save.
             */
            if (0 == profile.length())
            {
                setResult(RESULT_CANCELED);
            }
            else
            {
                /*
                 * This is the result Intent to Locale
                 */
                final Intent resultIntent = new Intent();

                /*
                 * This extra is the data to ourselves: either for the Activity or the BroadcastReceiver. Note
                 * that anything placed in this Bundle must be available to Locale's class loader. So storing
                 * String, int, and other standard objects will work just fine. However Parcelable objects
                 * must also be Serializable. And Serializable objects must be standard Java objects (e.g. a
                 * private subclass to this plug-in cannot be stored in the Bundle, as Locale's classloader
                 * will not recognize it).
                 */
                final Bundle resultBundle = new Bundle();
                resultBundle.putInt(PluginBundleManager.BUNDLE_EXTRA_INT_VERSION_CODE, Constants.getVersionCode(this));
                resultBundle.putString(PluginBundleManager.BUNDLE_EXTRA_STRING_MESSAGE, profile);

                resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, resultBundle);

                /*
                 * This is the blurb concisely describing what your setting's state is. This is simply used
                 * for display in the UI.
                 */
                if (profile.length() > getResources().getInteger(R.integer.twofortyfouram_locale_maximum_blurb_length))
                {
                    resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, profile.substring(0, getResources().getInteger(R.integer.twofortyfouram_locale_maximum_blurb_length)));
                }
                else
                {
                    resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, profile);
                }

                setResult(RESULT_OK, resultIntent);
            }
        }

        super.finish();
    }


    
}
