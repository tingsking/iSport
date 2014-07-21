package com.netease.isport;

import java.net.URISyntaxException;
import org.apache.http.HttpResponse;

import com.netease.util.GetIntentInstance;
import com.netease.util.NetWorkUtil;
import com.netease.util.PostandGetConnectionUtil;
import com.netease.util.SharedPreferenceUtil;
import com.netease.util.ToastUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.ClickListener;

public class SettingActivity extends Activity {

	private ProgressDialog progDialog = null;
	private UITableView tableView;
	private ImageView title_bar_menu_btn=null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);
        
        tableView = (UITableView) findViewById(R.id.tableView);
        title_bar_menu_btn=(ImageView) findViewById(R.id.title_bar_menu_btn);
        title_bar_menu_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SettingActivity.this.finish();
			}
		});
        createList();
        
        Log.d("MainActivity", "total items: " + tableView.getCount());
        
        tableView.commit();
    }
    
    private void createList() {
    	CustomClickListener listener = new CustomClickListener();
    	tableView.setClickListener(listener);
    	tableView.addBasicItem(R.drawable.clock, "���ǰ����", "��ǰ30����");
    	tableView.addBasicItem(R.drawable.version, "����汾", "�汾̫�󣬳����ֻ��洢��Χ1.0");
    	tableView.addBasicItem(R.drawable.info, "����iSport", "iSport�Ľ���˵��");
    	tableView.addBasicItem(R.drawable.logout, "�˳���¼", "�˳���ǰ���˻�");
    }
    
    private void showProgressDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(this);
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(false);
		progDialog.setMessage("�����˳������˻�");
		progDialog.show();
	}

	/**
	 * ���ؽ��ȿ�
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}
	
    private class CustomClickListener implements ClickListener {

		@Override
		public void onClick(int index) {
			//Toast.makeText(SettingActivity.this, "item clicked: " + index, Toast.LENGTH_SHORT).show();
			switch(index) {
			case 3: //logout
				if( !NetWorkUtil.isNetworkConnected(SettingActivity.this.getApplicationContext()) ) {
					ToastUtil.show(getApplicationContext(), "������񲻿��ã���������״̬��");
					return;
				}
				HttpResponse httpResponse = null;
				try {
					showProgressDialog();
					httpResponse = PostandGetConnectionUtil.postConnect(PostandGetConnectionUtil.logoutUrl);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(PostandGetConnectionUtil.responseCode(httpResponse) == 200){
					String message = PostandGetConnectionUtil.GetResponseMessage(httpResponse);            
		            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
		            JsonRet o = new DecodeJson().jsonRet(message);
		            if(o.getRet().equals("ok")) {
		            	SharedPreferenceUtil.setLogin(false);
		            	ToastUtil.show(getApplicationContext(), "�˳���¼�ɹ���");
		            	setResult(RESULT_OK, GetIntentInstance.getIntent());
		            	SettingActivity.this.finish();
		            } else {
		            	SharedPreferenceUtil.setLogin(false);
		            	setResult(RESULT_OK, GetIntentInstance.getIntent());
		            	SettingActivity.this.finish();
		            	//ToastUtil.show(getApplicationContext(), "�˳���¼ʧ���˰�����������");
		            }
				} else {
					SharedPreferenceUtil.setLogin(false);
	            	setResult(RESULT_OK, GetIntentInstance.getIntent());
	            	SettingActivity.this.finish();
					ToastUtil.show(getApplicationContext(), "������������⣬��Ҳ��֪����ô��Ŷ��");
				}
				dissmissProgressDialog();
				break;
			}
		}
    	
    }
}