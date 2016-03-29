package com.qizhi.qilaiqiqu.adapter;

import java.util.List;

import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.model.RiderRecommendModel;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class RiderListAdapter extends RecyclerView.Adapter<RiderListAdapter.MasonryView> implements OnClickListener{
	 private List<RiderRecommendModel> products;
	    private Context context;


	    public RiderListAdapter(List<RiderRecommendModel> list,Context context) {
	        products=list;
	        this.context = context;
	    }

	    @Override
	    public MasonryView onCreateViewHolder(ViewGroup viewGroup, int i) {
	        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_riderlist, viewGroup, false);
	       
	        view.setOnClickListener(this);
	        return new MasonryView(view);
	    }

	    @Override
	    public void onBindViewHolder(MasonryView masonryView, int position) {
	    			
	    		SystemUtil.Imagexutils(products.get(position).getRiderImage().split(",")[0], masonryView.imageView, context);
	    		masonryView.titleTxt.setText(products.get(position).getUserName());
	    		masonryView.describeTxt.setText(products.get(position).getRiderMemo());
	    		
	    		masonryView.itemView.setTag(position);

	    }

	    @Override
	    public int getItemCount() {
	        return products.size();
	    }

	    public static class MasonryView extends  RecyclerView.ViewHolder{

	        ImageView imageView;
	        TextView titleTxt;
	        TextView describeTxt;

	       public MasonryView(View itemView){
	           super(itemView);
	           imageView= (ImageView) itemView.findViewById(R.id.masonry_item_img );
	           titleTxt= (TextView) itemView.findViewById(R.id.masonry_item_title);
	           describeTxt = (TextView) itemView.findViewById(R.id.masonry_item_describe);
	       }

	    }
	    
	    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
	    public static interface OnRecyclerViewItemClickListener {
	        void onItemClick(View view , Integer position);
	    }
	    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
	        this.mOnItemClickListener = listener;
	    }

		@Override
		public void onClick(View v) {
			if (mOnItemClickListener != null) {
	            //注意这里使用getTag方法获取数据
	            mOnItemClickListener.onItemClick(v,(Integer)v.getTag());
	        }
		}
	    
}
