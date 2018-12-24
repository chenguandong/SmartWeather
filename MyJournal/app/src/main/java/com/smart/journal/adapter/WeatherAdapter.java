package com.smart.journal.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smart.journal.R;
import com.smart.journal.bean.TodayWeatherBean;
import com.smart.journal.tools.DateTools;

import java.util.List;

/**
 * @author guandongchen
 * @date 2018/1/6
 */

public class WeatherAdapter extends BaseQuickAdapter<TodayWeatherBean.ForecastsBean.CastsBean,BaseViewHolder>{

    public WeatherAdapter(int layoutResId, @Nullable List<TodayWeatherBean.ForecastsBean.CastsBean> data) {
        super(layoutResId, data);
    }

    public WeatherAdapter(@Nullable List<TodayWeatherBean.ForecastsBean.CastsBean> data) {
        super(data);
    }

    public WeatherAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, TodayWeatherBean.ForecastsBean.CastsBean item) {

        TextView textView = helper.getView(R.id.textView);
        TextView dataTextView = helper.getView(R.id.dataTextView);
        TextView tempTextView = helper.getView(R.id.tempTextView);

        textView.setText(
                item.getDayweather()
        );
        dataTextView.setText("星期"+ DateTools.coverNumToWeekChina(item.getWeek())+"\r\n"
                +item.getDate()+"\r\n");
        tempTextView.setText(item.getNighttemp()+" - "+item.getDaytemp()+"℃");
    }
}