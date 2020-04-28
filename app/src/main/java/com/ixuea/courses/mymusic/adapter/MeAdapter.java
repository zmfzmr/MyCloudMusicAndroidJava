package com.ixuea.courses.mymusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.ixuea.courses.mymusic.R;
import com.ixuea.courses.mymusic.domain.Sheet;
import com.ixuea.courses.mymusic.domain.ui.MeGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 我的界面适配器
 * 该适配器也可以封装
 */
public class MeAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private LayoutInflater inflater;//布局填充器(布局创建器)
    /**
     * 列表数据(里面的是: MeGroup)
     */
    private List<MeGroup> datum = new ArrayList<>();

    /**
     * 构造方法
     *
     * @param context
     */
    public MeAdapter(Context context) {
        this.context = context;
        //布局填充器(布局创建器)
        inflater = LayoutInflater.from(context);
    }

    /**
     * 返回组数量
     *
     * @return
     */
    @Override
    public int getGroupCount() {
        return datum.size();
    }

    /**
     * 返回子类表数量
     *
     * @param groupPosition
     * @return
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        //return 里面某个组 的大小
        return datum.get(groupPosition).getDatum().size();
    }

    /**
     * 获取组的对象
     *
     * @param groupPosition
     * @return
     */
    @Override
    public Object getGroup(int groupPosition) {
        return datum.get(groupPosition);
    }

    /**
     * 获取子元素的对象(获取Datum：里面的group,在获取group(里面列表)的某个数据)
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        //datum->获取组数据(datum.get(groupPosition).getDatum())->再获取组数据的某个数据get(childPosition)
        return datum.get(groupPosition).getDatum().get(childPosition);
    }

    /**
     * 组id （这里我们用不到）
     * 就是组的索引
     *
     * @param groupPosition
     * @return
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * 子元素id（当然我们这里也用不到，这类的返回值可以随便写），不实现也没有什么影响
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * childPosition;
    }

    /**
     * 是否有稳定的Id
     * 具体有什么用这里不讲解
     * 详解课程中才会讲解
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 获取组View
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        //这是ListView这类控件固定写法
        //只有这样写
        //才能用到它的View复用功能
        //RecyclerView在ListView后面才推出的
        //所以RecyclerView的架构更好一点(RecyclerView那边不用像ListView这样就可以直接复用ViewHolder，所以性能好一点)

        //组ViewHolder
        GroupViewHolder viewHolder;
        /*
            convertView:这个view是列表滚动到外面的那个item布局(view):就是在列表控件外面的那个item布局 view
            如果这个convertView没有值就创建，有值的话，就不用创建，直接复用convertView就行
         */
        if (convertView == null) {
            //如果没有复用的View

            //就从View加载一个控件
            //这里其实就是LayoutInflater对象调用inflate 加载布局返回该布局的view对象
            //这里加载的是item_title_me(因为本方法内是获取组view（加载一个标题的布局就行了）)
            convertView = inflater.inflate(R.layout.item_title_me, parent, false);

            //创建一个ViewHolder
            viewHolder = new GroupViewHolder(convertView);//这里记得传递convertView进去

            //将ViewHolder保存到tag
            convertView.setTag(viewHolder);

        } else {
            //有复用的view

            //从tag取出ViewHolder(convertView获取tag转换成ViewHolder)
            //之所以要取出tag(转换成ViewHolder),因为我们要这个ViewHolder去绑定显示数据
            viewHolder = (GroupViewHolder) convertView.getTag();
        }

        //绑定数据(这里是每个ViewHolder绑定一组数据(根据组索引获取这组数据))
        viewHolder.bindData(getGroupData(groupPosition), isExpanded);

        //返回view
        return convertView;
    }

    /**
     * 返回子View
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //子ViewHolder
        ChildViewHolder viewHolder;

        if (convertView == null) {
            //如果没有复用的View

            //就从View加载一个控件
            //这里其实就是LayoutInflater对象调用inflate 加载布局返回该布局的view对象
            //这里加载的是item_title_me(因为本方法内是获取组view（加载一个标题的布局就行了）)
            convertView = inflater.inflate(R.layout.item_sheet_me, parent, false);

            //创建一个ViewHolder
            viewHolder = new ChildViewHolder(convertView);//这里记得传递convertView进去

            //将ViewHolder保存到tag
            convertView.setTag(viewHolder);

        } else {
            //有复用的view

            //从tag取出ViewHolder(convertView获取tag转换成ViewHolder)
            viewHolder = (ChildViewHolder) convertView.getTag();
        }

        //绑定数据(这里是每个ViewHolder绑定一组数据(根据组索引获取这组数据))
        //这里getChildData：传入组索引和子元素索引
        viewHolder.bindData(getChildData(groupPosition, childPosition));

        //返回view
        return convertView;
    }

    /**
     * 当前字元素是否可以选中（点击）
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * 获取组数据
     * 其实这个方法跟（本类回调的getGroup方法差不多，只不过getGroup方法返回的是Object,用不惯，需要经常转换数据
     * 所以自己定义了一个）
     *
     * @param groupPosition
     * @return
     */
    private MeGroup getGroupData(int groupPosition) {
        return datum.get(groupPosition);
    }

    /**
     * 获取子对象
     *
     * @param groupPosition 组索引
     * @param childPosition 子元素索引
     * @return
     */
    private Sheet getChildData(int groupPosition, int childPosition) {
        //List<MeGroup> datum
        //获取组(MeGroup)里面列表，然后在获取子元素
        return datum.get(groupPosition).getDatum().get(childPosition);
    }

    /**
     * 设置数据
     *
     * @param datum
     */
    public void setDatum(ArrayList<MeGroup> datum) {
        //清除原来的数据
        this.datum.clear();

        //添加数据
        this.datum.addAll(datum);

        //通知控件数据改变了(对于ListView这种控件来，没有通知某一位置(某一个item)改变了),
        // 只能统一用这个notifyDataSetChanged(全部item刷新一次)
        notifyDataSetChanged();
    }

    /**
     * 组ViewHolder
     * 就是一个普通类
     * 保存了控件的引用
     * 避免每次找控件产生的性能消耗
     * 这里就不需要继承任何类，ListView这样的控件没有这个要求(而RecyclerView 的ViewHolder有这个要求)
     */
    class GroupViewHolder {
        /**
         * 构造方法
         */
        public GroupViewHolder(View view) {
            //初始化View注解框架
            ButterKnife.bind(this, view);
        }

        /**
         * 绑定数据
         *
         * @param data
         * @param isExpanded
         */
        public void bindData(MeGroup data, boolean isExpanded) {

        }
    }

    /**
     * 子ViewHolder
     * 注意：bindData(Sheet sheet) 传入的是歌单对象Sheet
     */
    class ChildViewHolder {
        public ChildViewHolder(View view) {
            //初始化View注解框架
            ButterKnife.bind(this, view);
        }

        /**
         * 绑定数据
         *
         * @param sheet 歌单对象
         */
        public void bindData(Sheet sheet) {

        }
    }
}
