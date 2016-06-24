package com.chen.develop.zhihudaily.Bean;

import java.util.List;

/**
 * Created by chen.zhiwei on 2016/6/14 0014.
 */
public class NewsLatestBean  {

    /**
     * date : 20160614
     * stories : [{"images":["http://pic2.zhimg.com/7855fb6ef2a6a2aefdb51996148e3d15.jpg"],"type":0,"id":8438387,"ga_prefix":"061410","title":"看到症状就想对号入座：我是不是有抑郁症？"},{"images":["http://pic1.zhimg.com/40ec3b8bbc92bbc127d283245ae93724.jpg"],"type":0,"id":8436182,"ga_prefix":"061409","title":"又一年的雨季来了，朋友们来相约一起看海"},{"images":["http://pic2.zhimg.com/38ab08468411e3f37d92dbc25893890d.jpg"],"type":0,"id":8431277,"ga_prefix":"061408","title":"一个撩妹技能，深情看着她，「我的部分神经元只为你一人而发放」"},{"images":["http://pic1.zhimg.com/e7bf09cfaf47e21f2c20dc937602fd0c.jpg"],"type":0,"id":7923550,"ga_prefix":"061407","title":"微软以 262 亿美元收购 LinkedIn，这两家公司都会得到什么？"},{"images":["http://pic3.zhimg.com/3db93eae1b8de43de94a918e82575e2e.jpg"],"type":0,"id":8437077,"ga_prefix":"061407","title":"历史上有哪些东西一直「不重要」，后来突然发现它的用途，被广泛使用呢？"},{"images":["http://pic2.zhimg.com/a82fc0013ea9f151fc49414b3b49af49.jpg"],"type":0,"id":8436251,"ga_prefix":"061407","title":"选个好专业 · 如果你喜欢看星星，那还是算了"},{"images":["http://pic4.zhimg.com/c8fd7af0c9f4ff01c6239d266f7fa4a3.jpg"],"type":0,"id":8439149,"ga_prefix":"061407","title":"读读日报 24 小时热门 TOP 5 · 在美国买枪"},{"images":["http://pic2.zhimg.com/ee43bdacdf2d44b70a3fd9eb49820821.jpg"],"type":0,"id":8436205,"ga_prefix":"061406","title":"瞎扯 · 如何正确地吐槽"}]
     * top_stories : [{"image":"http://pic1.zhimg.com/2859133b610677bc3804a71e49d1f140.jpg","type":0,"id":8439149,"ga_prefix":"061407","title":"读读日报 24 小时热门 TOP 5 · 在美国买枪"},{"image":"http://pic4.zhimg.com/dd7c2100ba34002db7691c1775cd3447.jpg","type":0,"id":8436182,"ga_prefix":"061409","title":"又一年的雨季来了，朋友们来相约一起看海"},{"image":"http://pic3.zhimg.com/97bc4a5ebb1f95ee1907195f52adbcf6.jpg","type":0,"id":7923550,"ga_prefix":"061407","title":"微软以 262 亿美元收购 LinkedIn，这两家公司都会得到什么？"},{"image":"http://pic3.zhimg.com/1d1350fe6f106c54955868f1ef448bf6.jpg","type":0,"id":8436251,"ga_prefix":"061407","title":"选个好专业 · 如果你喜欢看星星，那还是算了"},{"image":"http://pic4.zhimg.com/c42dc1813105d925c44f8e7904c2d227.jpg","type":0,"id":8232472,"ga_prefix":"061320","title":"最让你震惊的一个杀人案件是什么？"}]
     */

    private String date;
    /**
     * images : ["http://pic2.zhimg.com/7855fb6ef2a6a2aefdb51996148e3d15.jpg"]
     * type : 0
     * id : 8438387
     * ga_prefix : 061410
     * title : 看到症状就想对号入座：我是不是有抑郁症？
     */

    private  static List<StoriesBean> stories;
    /**
     * image : http://pic1.zhimg.com/2859133b610677bc3804a71e49d1f140.jpg
     * type : 0
     * id : 8439149
     * ga_prefix : 061407
     * title : 读读日报 24 小时热门 TOP 5 · 在美国买枪
     */

    private List<TopStoriesBean> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public List<TopStoriesBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStoriesBean> top_stories) {
        this.top_stories = top_stories;
    }


    public static class TopStoriesBean {
        private String image;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
