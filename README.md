最开始使用AndroidStudio的时候，各种不适应，各种怀恋Eclipse，写了几千行代码勉强熟悉了AndroidStudio后，感觉AndroidStudio不要太棒了。
学习Retrofit也是这样，遇到麻烦就想去用以前用过的框架，熟悉了过后我现在连吃个汤圆都喜欢串着吃，囧....

#像OkHttp一样使用
当然实际的项目开发中，不可能给我们时间慢慢去适应、去学习，要考虑技术风险和学习成本、团队共用。所以我才觉得Retrofit这一点好贴心，基于OkHttp实现，也就是说我当我遇到我不知道该怎么实现的功能的时候，可以无缝切换到OkHttp上面实现需求。
看看下面的代码，同样申明一个请求`call`，同样的异步` call.enqueue`，同样的回调`Callback`，甚至连`  mDatas = new Gson().fromJson(res, new TypeToken<List<SearchImage>>`Gson解析都是从前的味道。但是我们已经开始在使用Retrofit了，不知不觉，潜移默化中已经前进了一小步。
```
    private void initBtnBasic() {
        mBtnBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit build = new Retrofit.Builder().baseUrl("http://zhuangbi.info/").build();
                Call<ResponseBody> call = build.create(SearchApi.class).basicSearch("牛逼");
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String res = response.body().string();
                            mDatas = new Gson().fromJson(res, new TypeToken<List<SearchImage>>() {
                            }.getType());
                            for (SearchImage searchImage : mDatas) {
                                Log.d(TAG, "onResponse: " + searchImage.toString());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(TAG, "onFailure: ", t);
                    }
                });
            }
        });
    }
```

```
public interface SearchApi {

    @GET("search")
    Call<ResponseBody> basicSearch(@Query("q") String qTitle);
}
```
当然，100%的雷同也不好嘛，来了一趟总得学点东西，一下接触陌生知识太多，跨步前进容易扯到蛋不可取，原地踏步也不爽，小步慢行。
这里我们构建请求URL的事后使用了一个接口`SearchApi`，
Retrofit
```
Retrofit.Builder().baseUrl("http://zhuangbi.info/").build().create(SearchApi.class).basicSearch("牛逼");
```
OkHttp

```
new Request.Builder()
                .url("http://zhuangbi.info/search?q=" + getString("牛逼"))
                .build();
```
聪明的我们一看就知道

```
 @GET("search")
    Call<ResponseBody> basicSearch(@Query("q") String qTitle);
```
这里的` @GET("search")`表示使用get请求，同时search是get命令路径的一部分
##Path
当然，你也可以把他写到参数里面，Like This

```
   @GET("{path}")
    Call<ResponseBody> basicSearch(@Path("path") String path, @Query("q") String qTitle);
```
这样用就行了：

```
Retrofit.Builder().baseUrl("http://zhuangbi.info/").build().create(SearchApi.class).basicSearch("search","牛逼");
```
##Query & QueryMap

```
@Query("q") String qTitle
```
Query是查询参数，就相当于`http://zhuangbi.info/search?q=牛逼`的
   `?q=牛逼`      部分

也许你会想如果要查询很多参数，岂不是也要配置很多函数参数里面，然后让函数变得巨长
当然不用`@QueryMap `为你排忧解难

```
    @GET("search")
    Call<ResponseBody> basicSearch(@QueryMap Map<String, String> map);
```
就这么简单，轻松进入到Retrofit的地盘

#组装转换器---Gson工具升级

```
    private void initTrans() {
        final Retrofit build = new Retrofit.Builder()
                .baseUrl("http://zhuangbi.info/")
                .addConverterFactory(GsonConverterFactory.create())//添加转换器
                .build();

        mBtnTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<List<SearchImage>> call = build.create(SearchApi.class).transSearch("牛逼");
                call.enqueue(new Callback<List<SearchImage>>() {
                    @Override
                    public void onResponse(Call<List<SearchImage>> call, Response<List<SearchImage>> response) {
                        mDatas.clear();
                        //使用转换器，内部已经帮我们实现转换，省去了new Gson().form(jsonStr,new TypeToken<>())的过程
                        mDatas = response.body();
                        for (SearchImage searchImage : mDatas) {
                            Log.d(TAG, "onResponse: " + searchImage.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<SearchImage>> call, Throwable t) {
                        Log.e(TAG, "onFailure: ", t);
                    }
                });
            }
        });
    }
```
所谓转换器，其实再刚才的代码基础上也没多大改动嘛，不过是再构造的时候加了一句`  .addConverterFactory(GsonConverterFactory.create())//添加转换器`
Callback里面的json转换不用再使用

```
			    Gson gson = new Gson();
                mDatas = gson.fromJson(resStr, new TypeToken<List<SearchImage>>() {
                }.getType());
```
直接使用` mDatas = response.body();`就可以获取，当然请求的函数泛型也要改写

```
    @GET("search")
    Call<List<SearchImage>> transSearch(@Query("q") String qTitle);
```
省去了在回调函数new Gson的干扰，代码变得跟简洁。
当然，这里使用的是Gson，如果你需要JackSon，FastJson选择合适的解析器传递进去就行了。
如果，你需要自定义解析规则，也是没问题的，这需要自己去实现，但是这不是我们今天要说的重点，放到后面再写一个这种例子。
总之---使用了解析器，让我们的代码可读性变得更好，代码也更简洁。

#组装适配器---打通去往RxJava的道路

```
 private void initAdapt() {

        final Retrofit build = new Retrofit.Builder()
                .baseUrl("http://zhuangbi.info/")
                .addConverterFactory(GsonConverterFactory.create())//添加转换器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//添加适配器 连接RxJava
                .build();
        //使用RxBinding处理点击事件 RxBinding基于RxJava实现
        RxView.clicks(mBtnAdapter)
                .debounce(500, TimeUnit.MILLISECONDS) //排除500ms内重复点击事件
                .observeOn(AndroidSchedulers.mainThread())//切换到主线程执行响应内容,相当于在回调方法中执行runOnUiThread方法
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        //不需要Call了，直接使用Observable，顺滑的切换到RxJava领域
                        Observable<List<SearchImage>> observable = build.create(SearchApi.class)
                                .search("牛逼")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                        //这里主要是为了演示，代码简洁，实际中不要这样，
                        // 还要考虑取消一个请求，返回错误值处理，业务需求等等
                        observable.subscribe(new Action1<List<SearchImage>>() {
                            @Override
                            public void call(List<SearchImage> searchImages) {
                                for (SearchImage searchImage : searchImages) {
                                    Log.d(TAG, "call: " + searchImage.toString());
                                }
                            }
                        });
                    }
                });
    }
```
和解析器一样，适配器也就是加了一句`  .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//添加适配器 连接`
代码虽然简洁，作用巨大，注意现在我们已经不用Call来做请求了，我们使用`Observable`，这直接就可以利用RxJava操作。

 1. 线程切换，是这种感觉

```
 .subscribeOn(Schedulers.io())
 .observeOn(AndroidSchedulers.mainThread());
```
2.以前的回调`Callback`变成了监听者处理事件` observable.subscribe`，有更多

```
Action1
Subscription
```
方式让我们选择

3.链式调用，整个逻辑清晰明了
但是注意一点：
对API调用了 observeOn(MainThread) 之后，线程会跑在主线程上，包括 onComplete 也是， unsubscribe 也在主线程，然后如果这时候调用 `call.cancel` 会导致 NetworkOnMainThreadException ，所以一定要在 retrofit 的API调用`.subscribeOn(io).observeOn(MainThread)` 之后加一句 `unsubscribeOn(io)`

完整的就是

```
Api
.subscribeOn(io)
.observeOn(MainThread)
.unsubscribeOn(io)
```
哦，这里取消一个命令和call的方式不同，用的是`unsubscribeOn(io)`，Call对应的方法是`cancel()`


这里还加入了一点小元素，注意我们使用的是

```
 RxView.clicks(mBtnAdapter)
                .debounce(500, TimeUnit.MILLISECONDS) //排除500ms内重复点击事件
                .observeOn(AndroidSchedulers.mainThread())//切换到主线程执行响应内容,相当于在回调方法中执行runOnUiThread方法
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
```
取代

```
  mBtnTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
```
这是使用了RxBinding这个库，基于RxJava实现，看看用着这种方式实现方式对比传统方式在上述代码表达出来的，线程切换、防止重复点击的实现优势

#组装拦截器---调试神器
我写了，快一周也没有找到他的，请求方式，请求头在哪儿打印，一直好慌。知道再OkHttp的Github上看到`HttpLoggingInterceptor`这个库。
使用方式：导入

```
    compile 'com.squareup.okhttp3:logging-interceptor:3.3.1'
```

```
   private void initLog() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS);
        //自定义拦截方式
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.v("Retrofit", message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(logger).build();
        final Retrofit build = new Retrofit.Builder()
                .baseUrl("http://zhuangbi.info/")
                .addConverterFactory(GsonConverterFactory.create())//添加转换器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//添加适配器 连接RxJava
                .client(httpClient)
                .build();
        RxView.clicks(mBtnLog)
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                               @Override
                               public void call(Void aVoid) {
                                   //不需要Call了，直接使用Observable，顺滑的切换到RxJava领域
                                   Observable<List<SearchImage>> observable = build.create(SearchApi.class)
                                           .search("牛逼")
                                           .subscribeOn(Schedulers.io())
                                           .observeOn(AndroidSchedulers.mainThread());
                                   //这里主要是为了演示，代码简洁，实际中不要这样，
                                   // 还要考虑取消一个请求，返回错误值处理，业务需求等等
                                   observable.subscribe(new Action1<List<SearchImage>>() {
                                       @Override
                                       public void call(List<SearchImage> searchImages) {
                                           for (SearchImage searchImage : searchImages) {
                                               Log.d(TAG, "call: " + searchImage.toString());
                                           }
                                       }
                                   });
                               }
                           }
                );
    }
```
有4个请求层次，一般BASIC和HEADER用得比较多
```
  public enum Level {
    /** No logs. */
    NONE,
    /**
     * Logs request and response lines.
     *
     * <p>Example:
     * <pre>{@code
     * --> POST /greeting http/1.1 (3-byte body)
     *
     * <-- 200 OK (22ms, 6-byte body)
     * }</pre>
     */
    BASIC,
    /**
     * Logs request and response lines and their respective headers.
     *
     * <p>Example:
     * <pre>{@code
     * --> POST /greeting http/1.1
     * Host: example.com
     * Content-Type: plain/text
     * Content-Length: 3
     * --> END POST
     *
     * <-- 200 OK (22ms)
     * Content-Type: plain/text
     * Content-Length: 6
     * <-- END HTTP
     * }</pre>
     */
    HEADERS,
    /**
     * Logs request and response lines and their respective headers and bodies (if present).
     *
     * <p>Example:
     * <pre>{@code
     * --> POST /greeting http/1.1
     * Host: example.com
     * Content-Type: plain/text
     * Content-Length: 3
     *
     * Hi?
     * --> END GET
     *
     * <-- 200 OK (22ms)
     * Content-Type: plain/text
     * Content-Length: 6
     *
     * Hello!
     * <-- END HTTP
     * }</pre>
     */
    BODY
  }
```
还可以通过自定义`   HttpLoggingInterceptor logger = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() `个性化打造自己的网络数据请求打印。
![这里写图片描述](http://img.blog.csdn.net/20160629040515528)
#组装监听器---监听Response下行数据
把拦截器，进一步升华，比如下载数据的时候，拦截下Response，读取里面的信息，然后再传输给下一层，不就可以实现监听下行数据么。
先实现一个监听器

```
public interface ProcessListener {
    /**
     * @param current     已完成字节数
     * @param total       总字节数
     * @param isCompleted 是否已经完成
     */
    void onProcess(long current, long total, boolean isCompleted);
}
```
实现一个具备拦截能力的Response类

```
public class ProcessResponseBody extends ResponseBody {
    private ProcessListener mProcessListener;
    private ResponseBody mResponseBody;
    private BufferedSource mBufferedSource;

    public ProcessResponseBody(ResponseBody responseBody, ProcessListener processListener) {
        mProcessListener = processListener;
        mResponseBody = responseBody;
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return mBufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long nowSize = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long readSize = super.read(sink, byteCount);
                nowSize += (readSize != -1 ? readSize : 0);
                mProcessListener.onProcess(nowSize, mResponseBody.contentLength(), readSize == -1);
                return readSize;
            }
        };
    }
}
```
调用

```
        final OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Response oldResponse = chain.proceed(chain.request());
                        return oldResponse.newBuilder()
                                .body(new ProcessResponseBody(oldResponse.body(), new ProcessListener() {
                                    @Override
                                    public void onProcess(long current, long total, boolean isCompleted) {
                                        Log.d(TAG, "onProcess: " + current + "/" + total + "---" + isCompleted);
                                    }
                                }))
                                .build();
                    }
                }).build();
```
查看Log
![这里写图片描述](http://img.blog.csdn.net/20160629040950438)
#组装监听器2---获取Request上传进度
上传和下行，同理，实现一个具备拦截能力的Request即可
![这里写图片描述](http://img.blog.csdn.net/20160629115031958)
太快了，模拟器里面没有大一点的图片，本来是有进度条显示，看下日志吧
![这里写图片描述](http://img.blog.csdn.net/20160629041826295)


实现代码：

```
public class ProcessRequestBody extends RequestBody {
    private ProcessListener mProcessListener;
    private RequestBody mRequestBody;

    private BufferedSink mBufferedSink;

    public ProcessRequestBody(RequestBody requestBody, ProcessListener processListener) {
        mProcessListener = processListener;
        mRequestBody = requestBody;
    }

    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (mBufferedSink == null) {
            mBufferedSink = Okio.buffer(sink(sink));
        }
        mRequestBody.writeTo(mBufferedSink);
        mBufferedSink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            long writenBytes = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                writenBytes += byteCount;
                mProcessListener.onProcess(writenBytes, mRequestBody.contentLength(), writenBytes == mRequestBody.contentLength());
            }
        };
    }
}

```
调用

```
 final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {

                        RequestBody requestBody = new ProcessRequestBody(chain.request().body(), new ProcessListener() {
                            @Override
                            public void onProcess(final long current, final long total, final boolean isCompleted) {

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mDialog.show();
                                        mDialog.setProgress((int) (current * 100 / total));
                                        if (isCompleted) {
                                            mDialog.dismiss();
                                        }

                                    }
                                });
                                Log.d(TAG, "onProcess: " + current + "/" + total + isCompleted);
                            }
                        });

                        Request newRequest = chain.request().newBuilder().post(requestBody).build();
                        return chain.proceed(newRequest);

                    }
                })
                .build();
```
没有进度条终究是不爽的，用真机选个大图再来一发
![这里写图片描述](http://img.blog.csdn.net/20160629115316338)
源码下载地址：[https://github.com/zhouruikevin/RxJavaSamples-master](https://github.com/zhouruikevin/RxJavaSamples-master)