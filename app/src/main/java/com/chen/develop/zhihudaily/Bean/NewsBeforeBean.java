package com.chen.develop.zhihudaily.Bean;

import java.util.List;

/**
 * Created by chen.zhiwei on 2016/6/14 0014.
 */
public class NewsBeforeBean {


    /**
     * date : 20160613
     * stories : [{"images":["http://pic1.zhimg.com/97066df1ae58f6e9ce6afb3dd346bf90.jpg"],"type":0,"id":8413330,"ga_prefix":"061322","title":"小事 · 从清华北大退学"},{"images":["http://pic1.zhimg.com/56c7d934cefc80d03afd71b921bcf0ac.jpg"],"type":0,"id":8397232,"ga_prefix":"061321","title":"他们的相遇不是久别重逢，而是终成眷属"},{"images":["http://pic4.zhimg.com/9c503e85d35366d729777c67c1c83e77.jpg"],"type":0,"id":8232472,"ga_prefix":"061320","title":"最让你震惊的一个杀人案件是什么？"},{"title":"想在人迹罕至的地方体验大自然的神奇，跟我来吧","ga_prefix":"061319","images":["http://pic4.zhimg.com/5dd0042c146653e6471df4b638f1fc0b.jpg"],"multipic":true,"type":0,"id":8437168},{"images":["http://pic1.zhimg.com/c733f7e44ecb34a0052f70637e49f228.jpg"],"type":0,"id":8437800,"ga_prefix":"061318","title":"职人介绍所第 20 期：他们工作中的很多时候，是在记录别人的痛苦"},{"images":["http://pic1.zhimg.com/26411d56a47142d0ded6217d76bc7f64.jpg"],"type":0,"id":8418067,"ga_prefix":"061318","title":"要说到地域歧视，英国内部也是\u2026\u2026"},{"images":["http://pic3.zhimg.com/133317648d2179d54f17d6e9ddc30aee.jpg"],"type":0,"id":8437326,"ga_prefix":"061317","title":"如果你小时候，也曾为这「不可能」的问题磨秃无数支笔"},{"images":["http://pic2.zhimg.com/11c9b3555496280bd4bf61a0646f03f9.jpg"],"type":0,"id":8436189,"ga_prefix":"061316","title":"空战、三角恋、虚拟偶像，共同造就了这个系列的抖腿神曲"},{"images":["http://pic2.zhimg.com/74d51b180d01e76c71471cccde4c2915.jpg"],"type":0,"id":8436683,"ga_prefix":"061315","title":"看大片特效爽不爽？为了让你爽，砸了这些钱"},{"images":["http://pic1.zhimg.com/98973cc3fdb453b259614e435f7f5314.jpg"],"type":0,"id":8436721,"ga_prefix":"061314","title":"别让这观天巨眼成为下一个「浙江温州江南皮革厂」"},{"images":["http://pic3.zhimg.com/a2777d5285ce92714d4f1640d249550e.jpg"],"type":0,"id":8435630,"ga_prefix":"061313","title":"「买到烂尾楼，楼盘停工，我该怎么办？」"},{"images":["http://pic3.zhimg.com/b890703ae196421c2d65282cbdcd17c2.jpg"],"type":0,"id":8373000,"ga_prefix":"061312","title":"大误 · 我不知道这算不算一夜情"},{"images":["http://pic2.zhimg.com/0ced135928f7f6ab3867b2dd6b9b6c4d.jpg"],"type":0,"id":8434866,"ga_prefix":"061310","title":"「听说你是设计师，能不能帮我个忙？至于钱嘛\u2026\u2026」"},{"images":["http://pic4.zhimg.com/a3a0da1fb5ea676de0e82a7ca844661f.jpg"],"type":0,"id":8430787,"ga_prefix":"061309","title":"打官司不爱请律师，不光是钱的事"},{"images":["http://pic1.zhimg.com/38aafc586fe6ad4774fb8126449c9088.jpg"],"type":0,"id":8415928,"ga_prefix":"061308","title":"铁路旁的那些陈年断路断桥有些什么故事、来历？"},{"images":["http://pic4.zhimg.com/90781cddc5c93b223897b54245686b67.jpg"],"type":0,"id":8415502,"ga_prefix":"061307","title":"似乎女生爱看美女，而男生却不那么爱看帅哥"},{"images":["http://pic1.zhimg.com/e37b4a44b8bca279ee8fb657adf1471c.jpg"],"type":0,"id":8434858,"ga_prefix":"061307","title":"警察执法时到底公不公平？这个经济学家让数据说话"},{"images":["http://pic4.zhimg.com/c32c784d50f657e7a28a93c031aaa7fb.jpg"],"type":0,"id":8425314,"ga_prefix":"061307","title":"选个好专业 · 嗯\u2026\u2026现在还能不能学医？"},{"images":["http://pic3.zhimg.com/47f2cfd2802f4cd76fdf3a825a47a806.jpg"],"type":0,"id":8434813,"ga_prefix":"061307","title":"读读日报 24 小时热门 TOP 5 · 邹市明的真正敌人"},{"images":["http://pic4.zhimg.com/af48f847e8d5b3e076c54f93f256fc8f.jpg"],"type":0,"id":8429368,"ga_prefix":"061306","title":"瞎扯 · 如何正确地吐槽"}]
     */

    private String date;
    /**
     * images : ["http://pic1.zhimg.com/97066df1ae58f6e9ce6afb3dd346bf90.jpg"]
     * type : 0
     * id : 8413330
     * ga_prefix : 061322
     * title : 小事 · 从清华北大退学
     */

    private List<StoriesBean> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }


}
