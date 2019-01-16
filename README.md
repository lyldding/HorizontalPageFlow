

HorizontalPageFlow提供RecyclerView的瀑布流LayoutManager，每一页显示瀑布流并支持翻页。 

实现：
1、每页最大行列数（默认填充最大宽高）；
2、支持上一页、下一页；

CSDN：https://blog.csdn.net/lylddingHFFW/article/details/86482840
GitHub：https://github.com/lyldding/HorizontalPageFlow

<div align="center">
<img src="https://img-blog.csdnimg.cn/20190114193857755.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2x5bGRkaW5nSEZGVw==,size_16,color_FFFFFF,t_70"  width ="40%" />
</div>

图中三种layoutmanager：
上：HorizontalPageFlowLayoutManager();
中：HorizontalPageFlowLayoutManager(0，3);
下：HorizontalPageFlowLayoutManager(4，4)

部分代码：

```
mRecyclerViewTop = findViewById(R.id.top);   
PagingScrollHelper pagingScrollHelper1 = new PagingScrollHelper();       
pagingScrollHelper1.setUpRecycleView(mRecyclerViewTop);       
mRecyclerViewTop.setLayoutManager(new HorizontalPageFlowLayoutManager());     
mRecyclerViewTop.setAdapter(testAdapter);

mRecyclerViewMid = findViewById(R.id.mid);      
PagingScrollHelper pagingScrollHelper2 = new PagingScrollHelper();       
pagingScrollHelper2.setUpRecycleView(mRecyclerViewMid);       
mRecyclerViewMid.setLayoutManager(new HorizontalPageFlowLayoutManager(0, 3));   
mRecyclerViewMid.setAdapter(testAdapter);
 
mRecyclerViewBottom = findViewById(R.id.bottom);    
PagingScrollHelper pagingScrollHelper3 = new PagingScrollHelper();       
pagingScrollHelper3.setUpRecycleView(mRecyclerViewBottom);     
final HorizontalPageFlowLayoutManager layoutManager = new HorizontalPageFlowLayoutManager(4,4);    
mRecyclerViewBottom.setLayoutManager(layoutManager);       
mRecyclerViewBottom.setAdapter(testAdapter);
```

```
allprojects {
  repositories {
   ...
   maven { url 'https://www.jitpack.io' }
  }
 }
  
   dependencies {
         implementation 'com.github.lyldding:HorizontalPageFlow:1.0.1'
 }
```


