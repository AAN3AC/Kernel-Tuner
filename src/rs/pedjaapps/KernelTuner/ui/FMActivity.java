/*
 * This file is part of the Kernel Tuner.
 *
 * Copyright Predrag Čokulov <predragcokulov@gmail.com>
 *
 * Kernel Tuner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kernel Tuner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Kernel Tuner. If not, see <http://www.gnu.org/licenses/>.
 */
package rs.pedjaapps.KernelTuner.ui;

<<<<<<< HEAD
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
=======
>>>>>>> ginger
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

<<<<<<< HEAD
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;


=======
>>>>>>> ginger
import rs.pedjaapps.KernelTuner.R;
import rs.pedjaapps.KernelTuner.entry.FMEntry;
import rs.pedjaapps.KernelTuner.helpers.FMAdapter;
import rs.pedjaapps.KernelTuner.tools.Tools;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

public class FMActivity extends SherlockActivity
{
	List<FMEntry> e;
	String path;
	FMAdapter fAdapter;
	GridView fListView;
	Context c;
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		c = this;
        super.onCreate(savedInstanceState);
       
<<<<<<< HEAD
        setContentView(R.layout.fm);
=======
>>>>>>> ginger
		fListView = (GridView) findViewById(R.id.list);
		
		path = Environment.getExternalStorageDirectory().toString();

        fListView.setDrawingCacheEnabled(true);
		fAdapter = new FMAdapter(c, R.layout.fm_row);

		fListView.setAdapter(fAdapter);

		for (FMEntry entry : ls(new File(path)))
		{
			fAdapter.add(entry);
		}

<<<<<<< HEAD
		getSupportActionBar().setTitle(path);
=======
		setTitle(path);
>>>>>>> ginger
		fListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){



				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
										long arg3)
				{
					path = e.get(pos).getPath();
					fAdapter.clear();
					for (FMEntry entry : ls(new File(path)))
					{
						if(entry!=null){
						fAdapter.add(entry);
						}
					}
					fAdapter.notifyDataSetChanged();
<<<<<<< HEAD
					getSupportActionBar().setTitle(path);
=======
					setTitle(path);
>>>>>>> ginger
				}

			});

		((Button)findViewById(R.id.select)).setOnClickListener(new Listener(0));
		((Button)findViewById(R.id.cancel)).setOnClickListener(new Listener(1));
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
				Intent i = new Intent();
	        	i.putExtra("path", path);
	        	setResult(RESULT_OK, i);
	        	finish();
				return;
			case 1:
				finish();
				return;
			}
			
		}
    	
    }
	private List<FMEntry> ls(File path){
		e = new ArrayList<FMEntry>();
		
		File[] files = path.listFiles();
		for(File f : files){
			if(f.isDirectory()){
		      	e.add(new FMEntry(f.getName(),
		            	Tools.msToDate(f.lastModified()),
		            	Tools.byteToHumanReadableSize(f.length()),1,f.getAbsolutePath().toString()));
			}
		}
		Collections.sort(e, new SortByName());
		Collections.sort(e, new SortFolderFirst());

		return e;
	}
	
	static class SortFolderFirst implements Comparator<FMEntry>
	{
		@Override
		public int compare(FMEntry p1, FMEntry p2)
		{
	        if (p1.getFolder() < p2.getFolder()) return 1;
	        if (p1.getFolder() > p2.getFolder()) return -1;
	        return 0;
	    }   

	}

	static class SortByName implements Comparator<FMEntry>
	{
		@Override
		public int compare(FMEntry s1, FMEntry s2)
		{
		    String sub1 = s1.getName();
		    String sub2 = s2.getName();
		    return sub1.compareToIgnoreCase(sub2);
		} 

	}
	
	
	
	@Override
	public void onBackPressed() {
		if(path.equals(Environment.getExternalStorageDirectory().toString())){
		finish();
		}
		else{
			path = path.substring(0, path.lastIndexOf("/"));
			
			fAdapter.clear();
			for (FMEntry entry : ls(new File(path)))
			{
				fAdapter.add(entry);
			}
			fAdapter.notifyDataSetChanged();
<<<<<<< HEAD
			getSupportActionBar().setTitle(path);
=======
			setTitle(path);
>>>>>>> ginger
		}
	}
	
}
