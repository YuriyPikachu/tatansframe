package net.tatans.coeus.network.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;


import java.util.List;

import net.tatans.coeus.network.R;
import net.tatans.coeus.network.imp.ITatansItemClick;
import net.tatans.coeus.network.utils.Person;
import net.tatans.coeus.network.utils.StringHelper;

/**
 * Created by SiLiPing on 2015/12/23.
 */
@SuppressLint("NewApi")
public class TatansListAdapter extends BaseAdapter  implements SectionIndexer {

    private List<Person> list = null;
    private Context ctx;
    private ITatansItemClick itemClick;

    final static class ViewHolder {
        private TextView tvLetter;
        private TextView tvTitle;
    }

    public TatansListAdapter(Context mContext, List<Person> list, ITatansItemClick itemClick) {
        this.ctx = mContext;
        this.list = list;
        this.itemClick = itemClick;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    public void updateListView(List<Person> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final Person mContent = list.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(ctx).inflate(R.layout.sort_item, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if(position == getPositionForSection(section)){
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getPinYinName());
        }else{
            viewHolder.tvLetter.setVisibility(View.GONE);
        }

        viewHolder.tvTitle.setText(this.list.get(position).getName());
        convertView.setOnClickListener(new ItemOnClickListener(position));
        convertView.setOnLongClickListener(new ItemOnClickListener(position));
        return convertView;
    }

    /**
     * 单击事件
     */
    private class ItemOnClickListener implements View.OnClickListener,View.OnLongClickListener{
        private int mPosition;
        public ItemOnClickListener(int position) {
            this.mPosition = position;
        }
        @Override
        public void onClick(View v) {
            itemClick.OnTatansItemClick(mPosition,list.get(mPosition).getName());
        }

        @Override
        public boolean onLongClick(View view) {
            itemClick.OnTatansItemLongClick(mPosition,list.get(mPosition).getName());
            return false;
        }
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        //汉字转换成拼音
        String sortString = StringHelper.getPinYinHeadChar(str.substring(0, 1));
        // 正则表达式，判断首字母是否是英文字母
        if(sortString.matches("[a-zA-Z]")){
            return sortString;
        }else{
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }
    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getPinYinName();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }
        return -1;
    }
    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    @Override
    public int getSectionForPosition(int position) {
        return list.get(position).getPinYinName().charAt(0);
    }
}
