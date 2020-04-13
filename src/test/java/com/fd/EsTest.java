package com.fd;


import com.fd.entity.Emp;
import com.fd.entity.Video;
import com.fd.repository.EmpRepository;
import com.fd.repository.VideoRepository;
import com.fd.service.VideoService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsTest {
    @Resource
    VideoRepository videoRepository;

    @Resource
    EmpRepository empRepository;

    @Resource
    VideoService videoService;


    @Resource
    ElasticsearchTemplate elasticsearchTemplate;


    @Test
    public void test1(){
        Video video = new Video("3", "天空啊", "有花有天空", "1.mp4", "1.jpg", new Date(), "1", "2", "1",0,1);
       videoRepository.save(video);

    }

    @Test
    public void queryLight(){
        String content="花花";

        //处理高亮的操作
        HighlightBuilder.Field field = new HighlightBuilder.Field("*");
        field.preTags("<span style='color:red'>"); //前缀
        field.postTags("</span>"); //后缀

        //查询的条件
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withIndices("yingxue")  //指定索引名
                .withTypes("video")   //指定索引类型
                .withQuery(QueryBuilders.queryStringQuery(content).field("title").field("brief"))  //搜索的条件
                .withHighlightFields(field)  //处理高亮
                //.withFields("title","brief","cover")  //查询返回指定字段
                .build();

        //高亮查询
        AggregatedPage<Video> videoList = elasticsearchTemplate.queryForPage(nativeSearchQuery, Video.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
//参数searchResponse查询返回响应数据
                ArrayList<Video> videos = new ArrayList<>();

                //获取查询结果（原始数据）
                SearchHit[] hits = searchResponse.getHits().getHits();

                for (SearchHit hit : hits) {
                    //System.out.println("====="+hit.getSourceAsMap()); //原始数据
                    //System.out.println("-------"+hit.getHighlightFields()); //高亮数据
                    //原始数据
                    Map<String, Object> map = hit.getSourceAsMap();
                    //处理普通数据
                    String id = map.get("id")!=null? map.get("id").toString() : null;
                    String title = map.get("title")!=null? map.get("title").toString() : null;
                    String brief = map.get("brief")!=null? map.get("brief").toString() : null;
                    String path = map.get("path")!=null? map.get("path").toString() : null;
                    String cover = map.get("cover")!=null? map.get("cover").toString() : null;
                    String categoryId = map.get("categoryId")!=null? map.get("categoryId").toString() : null;
                    String groupId = map.get("groupId")!=null? map.get("groupId").toString() : null;
                    String userId = map.get("userId")!=null? map.get("userId").toString() : null;

                    //处理日期操作
                    Date date = null;

                    if(map.get("publishDate")!=null){
                        String publishDateStr = map.get("publishDate").toString();

                        //处理日期转换
                        Long aLong = Long.valueOf(publishDateStr);
                        date = new Date(aLong);
                    }

                    //封装video对象
                    Video video = new Video(id, title, brief, path, cover, date, categoryId, groupId, userId);

                    //处理高亮数据
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();

                    if(title!=null){
                        if (highlightFields.get("title") != null) {
                            String titles = highlightFields.get("title").fragments()[0].toString();
                            video.setTitle(titles);
                        }
                    }

                    if(brief!=null){
                        if (highlightFields.get("brief") != null) {
                            String briefs = highlightFields.get("brief").fragments()[0].toString();
                            video.setBrief(briefs);
                        }
                    }
                    //将对象放入集合
                    videos.add(video);

                }
                //强转 返回
                return new AggregatedPageImpl<T>((List<T>) videos);
            }
        });

        videoList.forEach(video -> System.out.println(video));

    }


    @Test
    public void querySearchVideos(){

        String content="橘子";

        //处理高亮的操作
        HighlightBuilder.Field field = new HighlightBuilder.Field("*");
        field.preTags("<span style='color:red'>"); //前缀
        field.postTags("</span>"); //后缀

        //查询的条件
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withIndices("yingxue")  //指定索引名
                .withTypes("video")   //指定索引类型
                .withQuery(QueryBuilders.queryStringQuery(content).field("title").field("brief"))  //搜索的条件
                .withHighlightFields(field)  //处理高亮
                //.withFields("title","brief","cover")  //查询返回指定字段
                .build();

        //高亮查询
        AggregatedPage<Video> videoList = elasticsearchTemplate.queryForPage(nativeSearchQuery, Video.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {

                ArrayList<Video> videos = new ArrayList<>();


                //获取查询结果
                SearchHit[] hits = searchResponse.getHits().getHits();

                for (SearchHit hit : hits) {

                    //System.out.println("====="+hit.getSourceAsMap()); //原始数据
                    //System.out.println("-------"+hit.getHighlightFields()); //高亮数据

                    //原始数据
                    Map<String, Object> map = hit.getSourceAsMap();

                    //处理普通数据

                    String id = map.get("id")!=null?map.containsKey("id") ? map.get("id").toString() : null:null;
                    String title = map.get("title")!=null?map.containsKey("title") ? map.get("title").toString() : null:null;
                    String brief = map.get("brief")!=null?map.containsKey("brief") ? map.get("brief").toString() : null:null;
                    String path = map.get("path")!=null?map.containsKey("path") ? map.get("path").toString() : null:null;
                    String cover = map.get("cover")!=null?map.containsKey("cover") ? map.get("cover").toString() : null:null;
                    String categoryId = map.get("categoryId")!=null?map.containsKey("categoryId") ? map.get("categoryId").toString() : null:null;
                    String groupId = map.get("groupId")!=null?map.containsKey("groupId") ? map.get("groupId").toString() : null:null;
                    String userId = map.get("userId")!=null?map.containsKey("userId") ? map.get("userId").toString() : null:null;

                    //处理日期操作
                    Date date = null;

                    if(map.get("publishDate")!=null){
                        if (map.containsKey("publishDate")) {

                            String publishDateStr = map.get("publishDate").toString();

                            //处理日期转换
                            Long aLong = Long.valueOf(publishDateStr);
                            date = new Date(aLong);
                        }
                    }

                    //封装video对象
                    Video video = new Video(id, title, brief, path, cover, date, categoryId, groupId, userId);

                    //处理高亮数据
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();

                    if(title!=null){
                        if (highlightFields.get("title") != null) {
                            String titles = highlightFields.get("title").fragments()[0].toString();
                            video.setTitle(titles);
                        }
                    }

                    if(brief!=null){
                        if (highlightFields.get("brief") != null) {
                            String briefs = highlightFields.get("brief").fragments()[0].toString();
                            video.setBrief(briefs);
                        }

                    }

                    //将对象放入集合
                    videos.add(video);

                }
                //强转 返回
                return new AggregatedPageImpl<T>((List<T>) videos);
            }
        });

        videoList.forEach(video -> System.out.println(video));

    }



    @Test
    public void queryByContent(){
        String content="花海";
        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withIndices("yingxue")
                .withTypes("video")
                .withQuery(QueryBuilders.queryStringQuery(content).field("title").field("brief"))  //或者满足一个条件
                .build();


        List<Video> videos = elasticsearchTemplate.queryForList(build, Video.class);
        videos.forEach(video -> System.out.println(video));


    }

    @Test
    public void test2(){
        Map<String, Object> map = videoService.queryAllVideo(1, 100);
        List<Video> videos = (List<Video>) map.get("rows");
        for (Video video : videos) {
            videoRepository.save(video);
        }
    }

    @Test
    public void queryByID(){
        Optional<Emp> byId = empRepository.findById("1");

        System.out.println(byId);

    }

    @Test
    public void queryBySort(){
        Iterable<Emp> age = empRepository.findAll(Sort.by(Sort.Order.desc("age")));
        age.forEach(emp -> System.out.println(emp));

    }

    @Test
    public void queryByPage(){
        Page<Emp> all = empRepository.findAll(PageRequest.of(0, 2));
        all.forEach(emp -> System.out.println(emp));

    }

}
