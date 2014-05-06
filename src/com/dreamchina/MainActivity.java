package com.dreamchina;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.umeng.update.UmengUpdateAgent;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

	private TabHost tabHost;
	private String tabFirstSpec = "首页";
	private String tabSecondSpec = "发现";
	private String tabThirdSpec = "我的";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tabHost = getTabHost();
		initTab();
		UmengUpdateAgent.update(this);
	}

	private void initTab() {
		addTab(Home.class, tabFirstSpec);
		addTab(Explore.class, tabSecondSpec);
		addTab(Me.class, tabThirdSpec);
	}

	private void addTab(Class<?> tabClass, String specName) {
		TabSpec tabSpec = tabHost.newTabSpec(specName)
				.setContent(new Intent(this, tabClass))
				.setIndicator(specName, null);
		tabHost.addTab(tabSpec);
	}

}
