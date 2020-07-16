package com.idcard.huashi;

import java.text.SimpleDateFormat;
import java.util.List;

import com.huashi.lua.db.utlis.IDCardDao;
import com.huashi.serialport.sdk.IDCardInfo;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DbFragment extends Fragment {

	private ListView lv;
	private TextView tv_info;
	private List<IDCardInfo> ics;
	private IDCardDao dao;
	SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");// 设置日期格式
	private String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/wltlib";// 授权目录

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_db, container, false);
		lv = (ListView) view.findViewById(R.id.lv);
		initData();
		return view;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		dao = new IDCardDao(getActivity());
		ics = dao.getAllInfos();
		lv.setAdapter(new MyAdapter());
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				IDCardInfo ic = ics.get(position);
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				View v = View.inflate(getActivity(), R.layout.dialog_idcard, null);
				tv_info = (TextView) v.findViewById(R.id.tv_info);
				byte[] fp = new byte[1024];
				fp = ic.getFpDate();
				String m_FristPFInfo = "";
				String m_SecondPFInfo = "";

				if (fp[4] == 1) {
					m_FristPFInfo = String.format("指位：%s。指纹质量：%d \n", GetFPcode(fp[5]), fp[6]);
				} else {

					m_FristPFInfo = "身份证无指纹 \n";
				}
				if (fp[512 + 4] == 1) {
					m_SecondPFInfo = String.format("指位：%s。指纹质量：%d \n", GetFPcode(fp[512 + 5]),
							fp[512 + 6]);

				} else {
					m_SecondPFInfo = "身份证无指纹 \n";
				}
				tv_info.setText("姓名：" + ic.getPeopleName() + "\n" + "性别：" + ic.getSex() + "\n" + "民族：" + ic.getPeople()
						+ "\n" + "出生日期：" + df.format(ic.getBirthDay()) + "\n" + "地址：" + ic.getAddr() + "\n" + "最新地址："
						+ ic.getNewAddr() + "\n" + "身份证号：" + ic.getIDCard() + "\n" + "签证机关：" + ic.getDepartment() + "\n"
						+ "有效期：" + ic.getStrartDate() + "-" + ic.getEndDate() + "\n" + "指纹：" + m_FristPFInfo + "\t"
						+ "\t" + "\t" + "\t" + "\t" + m_SecondPFInfo);
				builder.setView(v);
				builder.create().show();
			}
		});
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return ics.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v;
			ViewHolder hodler;
			if (convertView == null) {
				v = View.inflate(getActivity(), R.layout.item2, null);
				hodler = new ViewHolder();
				hodler.tv_name = (TextView) v.findViewById(R.id.tv_name);
				hodler.tv_sex = (TextView) v.findViewById(R.id.tv_sex);
				hodler.tv_idNo = (TextView) v.findViewById(R.id.tv_idNo);
				convertView = v;
				convertView.setTag(hodler);
			} else {
				v = convertView;
				hodler = (ViewHolder) convertView.getTag();
			}
			IDCardInfo ic = ics.get(position);
			if (ic == null) {
				return v;
			}
			hodler.tv_name.setText(ic.getPeopleName());
			hodler.tv_sex.setText(ic.getSex());
			hodler.tv_idNo.setText(ic.getIDCard());
			return v;
		}
	}

	class ViewHolder {
		private TextView tv_name, tv_sex, tv_idNo, tv_finger;
	}

	/**
	 * 指纹 指位代码
	 *
	 * @param FPcode
	 * @return
	 */
	String GetFPcode(int FPcode) {
		switch (FPcode) {
			case 11:
				return "右手拇指";
			case 12:
				return "右手食指";
			case 13:
				return "右手中指";
			case 14:
				return "右手环指";
			case 15:
				return "右手小指";
			case 16:
				return "左手拇指";
			case 17:
				return "左手食指";
			case 18:
				return "左手中指";
			case 19:
				return "左手环指";
			case 20:
				return "左手小指";
			case 97:
				return "右手不确定指位";
			case 98:
				return "左手不确定指位";
			case 99:
				return "其他不确定指位";
			default:
				return "未知";
		}
	}

}
