package no.aml.android.blinky.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import no.aml.android.blinky.R;

/**
 * Created by zhenhua.he on 2018/1/16.
 */

public class WifiAdapter extends BaseAdapter {
    LayoutInflater inflater;
    List<ScanResult> list;
    public WifiAdapter(Context context, List<ScanResult> list) {

        // TODO Auto-generated constructor stub
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {

        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {

        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // TODO Auto-generated method stub
        View view;

        view = inflater.inflate(R.layout.device_item, null);

        ScanResult scanResult = list.get(position);

        TextView textView = view.findViewById(R.id.device_name);

        textView.setText(scanResult.SSID);

        TextView signalStrenth =  view.findViewById(R.id.device_address);

        signalStrenth.setText("Rssi : " + String.valueOf(Math.abs(scanResult.level)));

        ImageView icoImgview = view.findViewById(R.id.it_icon);

        icoImgview.setImageResource(R.drawable.ic_wifi);

        ImageView imageView =  view.findViewById(R.id.rssi);

        if (Math.abs(scanResult.level) > 100) {
            imageView.setImageResource(R.drawable.ic_rssi_0_bar);
        }

        else if (Math.abs(scanResult.level) > 70) {
            imageView.setImageResource(R.drawable.ic_rssi_1_bar);
        }

        else if (Math.abs(scanResult.level) > 50) {
            imageView.setImageResource(R.drawable.ic_rssi_2_bars);
        }

        else {
            imageView.setImageResource(R.drawable.ic_rssi_3_bars);
        }

        return view;
    }

}
