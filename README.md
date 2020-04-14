# fast-verification

![demo](https://img-blog.csdnimg.cn/20200414114451477.gif#pic_center)

## 依赖到项目
#### 项目根gradle中添加
~~~
maven { url 'https://www.jitpack.io' }
~~~
示例：
~~~
allprojects {
    repositories {
        ...
        maven { url 'https://www.jitpack.io' }
    }
}
~~~
#### 在需要使用的module层级的gradle中添加
~~~
implementation 'com.github.sariki-L:fast-verification:1.1'
~~~
示例：
~~~
dependencies {
    ...
    implementation 'com.github.sariki-L:fast-verification:1.1'
}
~~~
## 正式使用
### xml中添加VerificationLayout
~~~
<com.sariki.fastverification.VerificationLayout
        android:background="@android:color/transparent"
        android:id="@+id/verification"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
~~~
### 初始化
~~~
        verification = findViewById(R.id.verification);
        verification.init(this, VerificationType.LENGTH_LONG);
~~~
变量名  | 对应验证码长度
---- | ----- 
VerificationType.LENGTH_LONG|6位
VerificationType.LENGTH_SHORT|4位

### 验证码输入完成时的回调
~~~
verification.setEndListener(new VerificationTypeListener() {
            @Override
            public void onFinish(String result) {
                if (!result.equals(**需校对内容**)){
                    verification.setErrType();
                }else{
                    ...
                }
            }
        });
~~~
在setEndListener中设置监听，重写onFinish方法处理输入完成后的状态。
setErrType为调用默认错误处理。
## 设置参数
如果对默认验证码框背景不满意，可以在代码中对部分参数进行更改

***需写在init调用前***

#### 设置验证码框背景
~~~
verification.setDrawBackground();
~~~
#### 设置验证码文字大小
~~~
verification.setDrawSize();
~~~
#### 设置验证码文字颜色
~~~
verification.setDrawColor();
~~~
博客地址：[CSDN](https://blog.csdn.net/qq_26059493/article/details/105493425)
