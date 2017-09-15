# 这是一个实现回弹效果的scrollview


  网上大多数时候动画还原时的代码是这样的：
  
      TranslateAnimation ta = new TranslateAnimation(0, 0, mRoot.getTop(),normal.top);    
      ta.setDuration(200);    
      mContentView.startAnimation(ta);    
      // 设置回到正常的布局位置    
      mContentView.layout(normal.left, normal.top, normal.right, normal.bottom);   
    
  但其实这是有bug的：
  如果代码中的normal.top不等于0，也就是说，你可以给mContentView添加上一个参数andoird:marginTop=”100dp”，你会明显的发现动画会出现问题。
  ![image](https://github.com/872822645/danxuankuangDemo/blob/master/1.jpg)
  明显看到，在添加以后的示图中，当手指松开以后，mContentView视图会先往下运动一下，然后再向上运动。
  layout函数有四个参数，分别是上、下、左、右，四个点的坐标。很明显，这是用来定位view所有位置的。
  这里要注意的是，它移动的坐标是按谁的来呢？我们这里传进去的值的时候，是通过类似view.getLeft()来获取左顶点X坐标的。这个坐标就相对父控件的坐标系的。
  所以layout的坐标是以父控件的坐标系来计算的。 其实这也好理解，因为layout可以随意的根据用户传进去的坐标来改变大小和位置。如果使用自己的坐标系来改变
  大小和位置的话，会很难计算；使用别人做为参照物来改变自己就相对容易的多。所以谷歌那帮老头用的是别人的坐标系来改变自己的位置和大小的。所以，一般情况
  下，改变自己位置和大小的函数，所用到的坐标系，一般都是父控件的坐标系。
  所以我的回弹代码是：
  
      int top = mContentView.getTop();
      mContentView.layout(mRect.left, mRect.top, mRect.right, mRect.bottom);
      TranslateAnimation animation = new TranslateAnimation(0, 0, top - mRect.top, 0);
      animation.setDuration(200);
      mContentView.startAnimation(animation);
