package com.kazemieh.www.note;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    Context context;
    List<DataModel> dataModels;
    DataBaseOpenHelper dataBaseOpenHelper;

    Adapter(Context context, List<DataModel> dataModels, DataBaseOpenHelper dataBaseOpenHelper) {
        this.context = context;
        this.dataModels = dataModels;
        this.dataBaseOpenHelper = dataBaseOpenHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String colortext="<font color='red' >"+MainActivity.searchtext+"</font>";
      //                         "<font colot='red'> w </font>"

        Spanned texttitle= Html.fromHtml(dataModels.get(position).getTitle().replace(MainActivity.searchtext,colortext));
        holder.title.setText(texttitle);

        Spanned textdes=Html.fromHtml(dataModels.get(position).getDes().replace(MainActivity.searchtext,colortext));
        holder.des.setText(textdes);

        if (dataModels.get(position).getFav() == 0) {
            holder.star.setImageResource(R.drawable.ic_round_star_border_24);
        } else {
            holder.star.setImageResource(R.drawable.ic_round_star_24);
        }

    }

    @Override
    public int getItemCount() {
        return dataModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, des;
        ImageView star;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_Adapter_title);
            des = itemView.findViewById(R.id.tv_Adapter_des);
            star = itemView.findViewById(R.id.iv_Adapter_star);

            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataModel dataModel = dataModels.get(getAdapterPosition());
                    int i = dataModel.getFav();
                    if (i == 0) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    if (i == 0) {
                        star.setImageResource(R.drawable.ic_round_star_border_24);
                    } else {
                        star.setImageResource(R.drawable.ic_round_star_24);
                    }
                    dataModel.setFav(i);
                    dataModels.set(getAdapterPosition(), dataModel);

                    dataBaseOpenHelper.getWritableDatabase().execSQL(" update " + dataBaseOpenHelper.tablename + " set " + dataBaseOpenHelper.favnote + " = "
                            + i + " where " + dataBaseOpenHelper.idnote + " = " + dataModel.getId());

              /*      ContentValues contentValues=new ContentValues();
                    contentValues.put(dataBaseOpenHelper.favnote,i);
                    dataBaseOpenHelper.getWritableDatabase().update(dataBaseOpenHelper.tablename,contentValues, dataBaseOpenHelper.idnote + " = " + dataModel.getId(),null);*/

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataModel dataModel = dataModels.get(getAdapterPosition());
                    Intent intent = new Intent(context, InsertActivity.class);
                    intent.putExtra("id", dataModel.getId());
                    intent.putExtra("key", "up");
                    context.startActivity(intent);
                }
            });

        }
    }
}
