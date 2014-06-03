package com.hb.activity.component;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.hb.R;
import com.hb.activity.ProfileActivity;
import com.hb.model.ProfileModel;

public class NameCardListTableLayout extends TableLayout {

	private ArrayList<ProfileModel> entries = null;
	private Context mContext = null;

	public NameCardListTableLayout(Context context, ArrayList<ProfileModel> e) {
		super(context);
		this.mContext = context;
		this.entries = e;

		LayoutInflater.from(context).inflate(R.layout.layout_name_card_table, this, true);

		this.init();

	}

	private void init() {
		int nCol = 3;
		int nRow = entries.size() / 3;

		if (nRow * nCol != entries.size()) {
			nRow++;
		}

		TableLayout tlNameCardList = (TableLayout) this.findViewById(R.id.tl_name_card_list);

		int count = 0;
		for (int i = 0; i < nRow; i++) {
			TableRow row = new TableRow(this.mContext);
			row.setPadding(0, 0, 0, 10);

			for (int j = 0; j < nCol; j++) {
				if (count >= this.entries.size())
					continue;

				NameCardBriefRelativeLayout namecard = new NameCardBriefRelativeLayout(this.getContext(), this.entries.get(count));
				namecard.setPadding(0, 0, 0, 0);

				count++;

				row.addView(namecard);
			}
			tlNameCardList.addView(row);
		}
	}

}
