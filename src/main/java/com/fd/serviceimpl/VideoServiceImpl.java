package com.fd.serviceimpl;

import com.fd.annotation.AddLog;
import com.fd.dao.VideoMapper;
import com.fd.entity.Category;
import com.fd.entity.Video;
import com.fd.entity.VideoExample;
import com.fd.repository.VideoRepository;
import com.fd.service.VideoService;
import com.fd.util.AliyunOssUtil;
import com.fd.vo.CateVo;
import com.fd.vo.VideoVo;
import org.apache.ibatis.session.RowBounds;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Transactional
@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    VideoMapper videoMapper;

    @Resource
    VideoRepository videoRepository;

    @Resource
    ElasticsearchTemplate elasticsearchTemplate;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Map<String, Object> queryAllVideo(Integer page, Integer rows) {
        HashMap<String , Object> map = new HashMap<>();
        //相当于是一个条件，没有条件对所有数据进行分页
        // 创建 Example对象 负责条件查询
        Example example = new Example(Video.class);

        //分页查询： 参数： 起始页,获取几条数据
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
        //总条数
        Video video = new Video();
        Integer records=videoMapper.selectCount(video);
        //总页数
        Integer total= records%rows==0? records/rows:records/rows+1;

        //查询
        List<Video> videos = videoMapper.selectByExampleAndRowBounds(example, rowBounds);
        map.put("page",page);
        map.put("total",total);
        map.put("rows",videos);
        map.put("records",records);
        return map;
    }


    @AddLog(value = "添加视频")
    @Override
    public String addVideo(Video video) {
        String id= UUID.randomUUID().toString().replace("-","");
        video.setId(id);
        video.setPublishDate(new Date());
        video.setCategoryId("1");
        videoMapper.insertSelective(video);


        return id;
    }


    @AddLog(value = "删除视频")
    @Override
    public HashMap<String , Object> deleteVideo(Video video) {
        HashMap<String, Object> map = new HashMap<>();
        try {

            //设置条件
            VideoExample example = new VideoExample();
            example.createCriteria().andIdEqualTo(video.getId());
            //根据id查询video信息
            Video myVideo = videoMapper.selectOneByExample(example);
            System.out.println(myVideo);
            //1.删除数据
            videoMapper.deleteByExample(example);

            //2.删除视频
            //字符串拆分
            // https:/  /yingxue-fd.oss-cn-beijing.aliyuncs.com  video  1585733454660-花海.mp4
            // https:/  /yingx-186.oss-cn-beijing.aliyuncs.com   photo  1585733454660-花海.jpg
            /*
            *
            https:

            yingxue-fd.oss-cn-beijing.aliyuncs.com
            video
            1585733454660-花海.mp4
            分成五部分
            * */

            String[] pathSplit = myVideo.getPath().split("/");
            for (String s : pathSplit) {
                System.out.println(s);
            }

            String[] coverSplit = myVideo.getCover().split("/");
            String videoName = pathSplit[pathSplit.length - 2] + "/" + pathSplit[pathSplit.length - 1];
            String coverName = coverSplit[coverSplit.length - 2] + "/" + coverSplit[coverSplit.length - 1];

            System.out.println("videoName"+videoName);
            System.out.println("coverName"+coverName);
            /*
             * 存储空间名
             * 视频名称
             * */
            AliyunOssUtil.delete("yingxue-fd", videoName);
            //3.删除封面
            AliyunOssUtil.delete("yingxue-fd", coverName);

            map.put("status", "200");
            map.put("message", "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "400");
            map.put("message", "删除失败");
        }

            return map;
    }



    @AddLog(value = "修改视频")
    @Override
    public void updateVideo(Video video) {
        System.out.println(video);

        VideoExample example = new VideoExample();
        example.createCriteria().andIdEqualTo(video.getId());

        videoMapper.updateByExampleSelective(video,example);

    }


    @AddLog(value = "上传视频")
    @Override
    public void uploadVideo(MultipartFile path, String id, HttpServletRequest request) {
        //截取视频第一帧 做封面
        //将封面上传到阿里云

        //获取文件名
        String filename = path.getOriginalFilename();
        String newName=new Date().getTime()+"-"+filename;

        //上传到阿里云
        /*
        * 1,上传路径
        * 2.上传视频名
        * */
        AliyunOssUtil.uploadFileBytes("yingxue-fd",path,"video/"+newName);

        //修改视频信息的条件
        VideoExample example = new VideoExample();
        example.createCriteria().andIdEqualTo(id);

        String netFileName="https://yingxue-fd.oss-cn-beijing.aliyuncs.com/video/"+newName;


        //频接本地存放路径    D:\动画.jpg
        // 1585661687777-好看.mp4
        String[] names = newName.split("\\.");
        String interceptName=names[0];
        String coverName=interceptName+".jpg";  //频接封面名字

        //截取视频第一帧作封面 并上传到阿里云
        AliyunOssUtil.videoCoverIntercept("yingxue-fd","video/"+newName,"photo/"+coverName);


        //修改视频信息
        Video video = new Video();
        video.setPath(netFileName);
        video.setCover("https://yingxue-fd.oss-cn-beijing.aliyuncs.com/photo/"+coverName);
        //调用修改
        videoMapper.updateByExampleSelective(video,example);

        //设置id
        video.setId(id);
        Video videos = videoMapper.selectOne(video);

        //向es中构建索引
        videoRepository.save(videos);

    }

    @Override
    public List<VideoVo> queryVideoByDescTime() {

        List<VideoVo> videoVos = videoMapper.queryByReleaseTime();
        for (VideoVo videoVo : videoVos) {
            videoVo.setLikeCount(12);
        }

        return videoVos;
    }

    @Override
    public List<VideoVo> queryByLikeVideoName(String content) {

        List<VideoVo> videoVos = videoMapper.queryByLikeVideoName(content);
        for (VideoVo videoVo : videoVos) {
            videoVo.setLikeCount(12);
        }

        return videoVos;

    }

    @Override
    public List<VideoVo> queryByVideoDetail(String videoId, String cateId, String userId) {
        List<VideoVo> videoVos = videoMapper.queryByVideoDetail(videoId, cateId, userId);
        return videoVos;
    }

    @Override
    public List<CateVo> queryAllCategory() {
        List<CateVo> cateVos = videoMapper.queryAllCategory();
        return cateVos;
    }

    @Override
    public List<VideoVo> queryCateVideoList(String cateId) {
        List<VideoVo> videoVos = videoMapper.queryCateVideoList(cateId);
        return videoVos;
    }

    @Override
    public List<VideoVo> queryByUserMoving(String userId) {
        List<VideoVo> videoVos = videoMapper.queryByUserMoving(userId);
        return videoVos;
    }

    @Override
    public List<VideoVo> queryByUserVideo(String userId) {
        List<VideoVo> videoVos = videoMapper.queryByUserVideo(userId);
        return videoVos;
    }

    @Override
    public List<Video> querySearch(String content) {

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

        return videoList.getContent();

    }
}
