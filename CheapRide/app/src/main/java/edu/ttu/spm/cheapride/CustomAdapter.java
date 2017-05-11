package edu.ttu.spm.cheapride;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.ttu.spm.cheapride.model.HistoryRecordEntity;

/**
 * Created by Administrator on 2017/5/5.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private Context context;
    private List<HistoryRecordEntity> historyRecordEntityList;

    public CustomAdapter(Context context, List<HistoryRecordEntity> historyRecordEntityList){
        this.context = context;
        this.historyRecordEntityList = historyRecordEntityList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.date.setPaintFlags(holder.date.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.date.setText(historyRecordEntityList.get(position).getDate());
        holder.date.setTextSize(25);
        holder.date.setTextColor(Color.parseColor("#333333"));

        if (historyRecordEntityList.get(position).getProvider().equals("lyft")) {
            holder.provider.setText(Html.fromHtml("<font color='black'>" + "PROVIDER ;  " + "</font>" + "<font color='#ff00bf'>" + "LYFT" + "</font>", 1));
            holder.card.setCardBackgroundColor(Color.parseColor("#8cadf2"));
        } else{
            holder.provider.setText(Html.fromHtml("<font color='black'>" + "PROVIDER ;  " + "</font>" + "<font color='black'>" + "UBER" + "</font>", 1));
            holder.card.setCardBackgroundColor(Color.parseColor("#dcdcdc"));
        }
        holder.provider.setTextSize(15);
        holder.pickup.setText(Html.fromHtml("<font color='black'>"+ "PICKUP ;  " + "</font>" + "<font color='black'>"+ historyRecordEntityList.get(position).getPick() + "</font>",1));
        holder.pickup.setTextSize(15);
        holder.destination.setText(Html.fromHtml("<font color='black'>"+ "DESTINATION ;  " + "</font>" + "<font color='black'>"+ historyRecordEntityList.get(position).getDestination(),1));
        holder.destination.setTextSize(15);
        holder.fee.setText(Html.fromHtml("<font color='black'>"+ "FEE ;  $"  + "</font>" + "<font color='black'>"+ historyRecordEntityList.get(position).getFee(),1));
        holder.fee.setTextSize(15);
    }

    @Override
    public int getItemCount() {
        return historyRecordEntityList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView date;
        public  TextView provider;
        public TextView pickup;
        public TextView destination;
        public TextView fee;
        public CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            this.date = (TextView) itemView.findViewById(R.id.date_card);
            this.provider = (TextView) itemView.findViewById(R.id.provider_card);
            this.pickup = (TextView) itemView.findViewById(R.id.pickup_card);
            this.destination = (TextView) itemView.findViewById(R.id.destination_card);
            this.fee = (TextView) itemView.findViewById(R.id.fee_card);
            this.card = (CardView) itemView.findViewById(R.id.card_view);
        }
    }
}
