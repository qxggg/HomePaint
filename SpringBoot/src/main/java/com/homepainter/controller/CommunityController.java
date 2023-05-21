package com.homepainter.controller;



import com.homepainter.pojo.EvaluateImage;
import com.homepainter.pojo.Tieba;
import com.homepainter.pojo.TiebaEvaluate;

import com.homepainter.service.CommunityService;

import com.homepainter.service.TranslateService;
import com.homepainter.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/community")
public class CommunityController {

    @Autowired
    CommunityService communityService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    TranslateService translateService;

    @Autowired
    JdbcTemplate jdbcTemplate;



    
    @GetMapping("/list")
    public Map<String, Object> getCommunityList(){
        Map<String, Object> map = new HashMap();
        List<Tieba> tiebas = communityService.getTiebaList();
        for (Tieba tieba : tiebas)
            tieba.getUser().setPassword(null);
        map.put("code", 0);
        map.put("msg", "社区信息查询成功！");
        map.put("data", tiebas);
        return map;
    }

    @PostMapping("/post")
    public Map<String, Object> insertTieba(@RequestBody Map<String, Object> data, @RequestHeader String token){
        String tel =(String) redisUtil.get(token);
        int userId = Integer.parseInt(tel.substring(5));
        Map<String, Object> map = new HashMap<>();
        Date date = new Date();
        String content = (String) data.get("content");
        List<Integer> goodsId = (List<Integer>) data.get("goodsId");
        List<String> image = (List<String>) data.get("image");
        String title = (String) data.get("title");
        String query = "select tiebaId from tieba order by tiebaId limit 1 desc";
        Map<String, Object> q = jdbcTemplate.queryForMap(query);
        int tiebaId = (int) q.get("tiebaId") + 1;
        String sql1 = "insert into tieba values(?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql1, userId, content, tiebaId, 0, date, 0, title);

        String sql2 = "insert into tiebagoods values(?, ?)";
        for (Integer goodsid : goodsId)
            jdbcTemplate.update(sql2, goodsid, tiebaId, goodsid);


        map.put("code", 0);
        return map;
    }
    @PostMapping("/detail")
    public Map<String, Object> getDetail(@RequestBody Map<String, Object> data){
        Map<String, Object> map = new HashMap();
        map.put("code", 0);
        map.put("msg", "帖子详细信息查询成功！");
        map.put("data", communityService.getTiebaById((int) data.get("forum_id")));
        map.put("evaluates", communityService.getTiebaEvaluate((int) data.get("forum_id")));
        return map;
    }

    @PostMapping("/publish")
    public Map<String, Object> givePrice(@RequestBody Map<String, Object> data){
        Map<String, Object> map = new HashMap();
        map.put("code", 1);
        if (communityService.givePrice((int) data.get("forum_id")) == 0) map.put("msg", "点赞失败，网络异常");
        else{
            map.put("code", 0);
            map.put("msg", "点赞成功！");
        }
        return map;
    }

    @PostMapping("/evaluate")
    public Map<String, Object> makeEvaluate(@RequestBody Map<String, Object> data, @RequestHeader String token){
        Map<String, Object> map = new HashMap();
        int forum_id = (int) data.get("forum_id");
        List<String> image = (ArrayList) data.get("image");
        List<EvaluateImage> l = new ArrayList<>();
        for (String s : image)
            l.add(new EvaluateImage(forum_id, s));
        String tel =(String) redisUtil.get(token);
        String telephone = tel.substring(5);
        int id = Integer.parseInt(telephone);
        if (communityService.makeEvaluate(new TiebaEvaluate((String) data.get("detail"), forum_id, l, id))){
            map.put("code", 0);
            map.put("msg", "插入评论成功");
        }
        else {
            map.put("code", 1);
            map.put("msg", "插入评论失败");
        }
        return map;
    }


    @GetMapping("get")
    public Map<String, Object> get(){
        String a = "[{\"name\":\"推荐\",\"data\":[{\"title\":\"210m平方珠海尚东领御雅居原创新中式风格案例\",\"image\":[\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/1.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/2.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/3.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/4.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/5.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/6.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/7.jpg\"],\"favorite\":323,\"avatar\":\"https://img1.baidu.com/it/u=1403245892,3051757811&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500\",\"writename\":\"无与同空间设计\",\"content\":\"专业的客厅设计包括功能划分、区块划设装修风格、色彩搭配、灯光布局、家具配套、软装渲染等一系列设计布局。\\n新中式继承了中国传统家居理念的精华，将其中的经典元素符号提炼，通过设计手法来改变原有形式状态，空间布局借鉴传统中式空间设计美学，并且非常注重空间层次、布局、对称等，风格讲究纲常，讲究对称，以阴阳平衡概念调和室内生态，中国五千年文化汉代、唐代的风格都可以用现代手法来演经新中式风格。\\n#室内设计 #家居家装日堂 #珠海装修 #现代轻奢 #装修灵感库 #豪宅#珠海装修 #珠海室内设计#入户 #玄关 #鞋柜 \"},{\"title\":\"磐石远程设计 | 日式暖木风，暖暖的家\",\"image\":[\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/1/1.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/1/2.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/1/3.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/1/4.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/1/5.jpg\"],\"favorite\":6171,\"avatar\":\"https://img2.baidu.com/it/u=4261212628,2246376874&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500\",\"writename\":\"更新装饰设计工程\",\"content\":\"温暖且治愈的原木风，三房调整成可以临时居住的小四房。\\n满足屋主居住需求的同时，保证了每个房间的通风采光。\\n同时规划了足够多的收纳空间，满足屋主未来10年的居住需求。\"},{\"title\":\"喜欢中式?不妨看看我家?\",\"image\":[\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/2/1.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/2/2.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/2/3.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/2/4.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/2/5.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/2/6.jpg\"],\"favorite\":40,\"avatar\":\"https://img2.baidu.com/it/u=4186913488,2418932402&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500\",\"writename\":\"今和设计装修中心\",\"content\":\"中式风格是以宫廷建筑为代表的，中国古典建筑的室内装饰设计艺术风格，透露出优雅、端庄\\n风格元素\\n中式风格以中国传统文化内涵为设计元素，在室内布置、线性、色调以及家具、陈列的造型\\n等方面，吸取传统装饰。中式风格的家具对称、简约、朴素富有文化内涵，体现主人较高审美与社会地位\\n空间层次\\n中式风格设计非常讲究层次感，在需要隔绝视线的地方，通常会设置中式的屏风或窗根等通过这种空间隔断\\n线条感\\n中式家具装修风格的设计多采用简洁、硬朗的直线条，不仅迎合了中式家具追求内敛、质朴的设计风格，还反映出现代人追求简单生活的居住要求，使中式风格更加实用，更富有现代感\"},{\"title\":\"极简新中式 || 年轻人爱不释手的选择\",\"image\":[\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/3/1.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/3/2.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/3/3.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/3/4.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/3/5.jpg\"],\"favorite\":1383,\"avatar\":\"https://img1.baidu.com/it/u=2691907958,269123817&fm=253&fmt=auto&app=138&f=JPEG?w=532&h=500\",\"writename\":\"紫苹果国际设计\",\"content\":\"国潮风兴起，越来越多的年轻人喜欢上了古典文化，原本以为是中老年人的喜好，现在却成为了年轻人的心头好。\\n器在整个新中式客厅空间种，一眼就会被浓浓的新中式复古风格所吸引~\\n可以感受到浓郁的传统文化气息，但是不像以前传统中式家具给人带来沉闷和老引日的感觉，而且透露出一股新中式独有的朝气和符合现代年轻人喜爱的展现~\"},{\"title\":\"原木风#体验生活之美，感受惬意时光\",\"image\":[\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/4/1.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/4/2.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/4/3.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/4/4.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/4/5.jpg\"],\"favorite\":3501,\"avatar\":\"https://img1.baidu.com/it/u=4188500822,3869319195&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500\",\"writename\":\"优住空间\",\"content\":\"#原木风 今日分享原木风Q?原木温润，给人一种岁月沉淀的隽永之感，只是看着就会让人相信幸福，相信总有一天，漫漫长夜里有人陪你说话，晚灯不灭有人等你回家。岁月静好不外如是。\"},{\"title\":\"温润自然现代原木风 治愈系恰到好处温柔\",\"image\":[\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/5/1.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/5/2.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/5/3.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/5/4.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/5/5.jpg\"],\"favorite\":301,\"avatar\":\"https://pic3.zhimg.com/v2-220ccec1c2cc68973ef4ae8fcfe65822_b.jpg\",\"writename\":\"南希\",\"content\":\"温暖自然 随遇而安的舒适时光原木风\\n简洁胜于繁复，极简即为丰富自然的材质打造治愈空间\\n温暖静谧的浅色空间，木质材质的运用，以本真的姿态，给予全屋空间自然而又富有质感的底色\\n每一处都是对生活的热爱，暖色调的高级温暖治愈的理想住宅，喜欢的话记得收藏一下台\\n·全屋空间主体奶油大地色的质感，玄关拱形门的仪式感，恰当的绿植点缀，自然的光线引入空间，太美了\\n木色、白色的空间中，和谐百搭，充满了极简主义的调性，显高级\"},{\"title\":\"8月盛夏欢迎参观清新原木风小家\",\"image\":[\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/6/1.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/6/2.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/6/3.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/6/4.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/6/5.jpg\"],\"favorite\":701,\"avatar\":\"https://pic2.zhimg.com/v2-f2ae6e4ae726a987e4f9f997c47f1731_b.jpg\",\"writename\":\"尖尖的小日子\",\"content\":\"每次把家里收拾得井有条都格外有成就感室外的燥热和蝉鸣影响不到室内的凉爽宁静\\n当阳光撒进客厅\\n那种通透感让人心旷神怡\\n来一杯满冰的气泡水是对夏天的尊重\\n找一部治愈电影就可以打发一下午时间\\n坐在奶呼呼的地毯上撸猫太过惬意\\n复古橙色沙发越看越喜欢\\n累的时候往上面躺不要太舒服\"},{\"title\":\"空间设计|原木风，自然治愈之美\",\"image\":[\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/7/1.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/7/2.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/7/3.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/7/4.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/7/5.jpg\"],\"favorite\":601,\"avatar\":\"https://pic2.zhimg.com/v2-f2ae6e4ae726a987e4f9f997c47f1731_b.jpg\",\"writename\":\"一行一物\",\"content\":\"原木风作为清新自然的代表，一直深受人们的喜欢。自然舒缓、温柔治愈的感受，让这种风格更加老少皆宜。\\n项目介绍设计风格:原木风项目面积: 90m2材质:木作、涂料、护墙板、陶瓷\\n设计介绍\\n如未曾污染的天空湛蓝，如未经世事的孩童的眼睛清澈，如一切事物的最初，其最纯净的状态。\"}]},{\"name\":\"家具\",\"data\":[]},{\"name\":\"客厅\",\"data\":[{\"title\":\"210m平方珠海尚东领御雅居原创新中式风格案例\",\"image\":[\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/8/1.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/8/2.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/8/3.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/8/4.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/8/5.jpg\"],\"favorite\":234,\"avatar\":\"https://img1.baidu.com/it/u=1403245892,3051757811&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500\",\"writename\":\"无与同空间设计\",\"content\":\"专业的客厅设计包括功能划分、区块划设装修风格、色彩搭配、灯光布局、家具配套、软装渲染等一系列设计布局。\\n新中式继承了中国传统家居理念的精华，将其中的经典元素符号提炼，通过设计手法来改变原有形式状态，空间布局借鉴传统中式空间设计美学，并且非常注重空间层次、布局、对称等，风格讲究纲常，讲究对称，以阴阳平衡概念调和室内生态，中国五千年文化汉代、唐代的风格都可以用现代手法来演经新中式风格。\\n#室内设计 #家居家装日堂 #珠海装修 #现代轻奢 #装修灵感库 #豪宅#珠海装修 #珠海室内设计#入户 #玄关 #鞋柜 \"},{\"title\":\"买不起大house，110平的小家也不错啊\",\"image\":[\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/9/1.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/9/2.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/9/3.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/9/4.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/9/5.jpg\"],\"favorite\":633,\"avatar\":\"https://pic1.zhimg.com/v2-3012d58037316b80d8b9719ef09e8bac_b.jpg\",\"writename\":\"温柔\",\"content\":\"简简单单的小家却无比舒适自在~#装修 #客厅装修灵感库 #装修案例 #装修设计 #软装 #软装搭配 #家居美学 #家居\"},{\"title\":\"关于高级灰的装修风格 你想看的都在这里\",\"image\":[\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/10/1.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/10/2.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/10/3.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/10/4.jpg\"],\"favorite\":63,\"avatar\":\"https://pic3.zhimg.com/v2-54e372dc8e21fd20de599ce8bd36d0b6_b.jpg\",\"writename\":\"尚层品味·设计\",\"content\":\"高级灰风格逐渐成了家居装修的潮流，看似简洁却暗藏了有魅力的高级感。\\n这种风格在不少年轻人裙中受到追捧，趋势逐步上升。这种高级灰在家装中的运用怎么样更高级，来看看这组高级灰的家装风格吧\\n将高级灰用到墙面上o\\n奠定整体风格的基调 空间上显得简洁大气客厅大厅选用原木色地板 添加一些柔和·沙发、床、窗帘等软装上选择深浅不易的色调提升房间的质感\\n艺术品的摆放 在无形中传达出静谧感\"},{\"title\":\"装修灵感咖色系装修简直就是流量密码\",\"image\":[\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/11/1.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/11/2.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/11/3.jpg\"],\"favorite\":561,\"avatar\":\"https://pic4.zhimg.com/v2-05c926a44f1fa716d538fb4a90bbd537_b.jpg\",\"writename\":\"宝盖头家居科技\",\"content\":\"坐标上海，现代风不只有黑白灰，设计师出的套奶咖色的方案效果非常好\\n屋主喜欢暖色系的家，对餐厅的设计有着自己的想法，跟设计师沟通后，做出的方案很是满意\"}]},{\"name\":\"卧室\",\"data\":[{\"title\":\"|装修设计|现代风格设计卧室分享\",\"image\":[\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/12/1.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/12/2.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/12/3.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/12/4.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/12/5.jpg\"],\"favorite\":61,\"avatar\":\"https://pic3.zhimg.com/v2-8c92c8d14f48363229cec253d3ac3b16_b.jpg\",\"writename\":\"深圳设计师徐成\",\"content\":\"自古到今卧室的功能从未有太多的变化 它直是睡眠和休息以及存放衣物的主要地方  是居室中最个人化的空间中\\n每一间精心布置的卧室就是一首动人优美的旋律 值得回味·\\n然而卧室的设计并非一定由多姿多彩的色调和层出不穷的造型来营造气氛 大方简洁以及清逸淡雅而又极富现代感的简约主义已经越来越受到人们的欣赏和喜爱了个\"},{\"title\":\"老公说，这无主灯卧室简直好看到离谱!!!\",\"image\":[\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/13/1.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/13/2.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/13/3.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/13/4.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/13/5.jpg\"],\"favorite\":961,\"avatar\":\"https://pic4.zhimg.com/v2-05c926a44f1fa716d538fb4a90bbd537_b.jpg\",\"writename\":\"宝盖头家居科技\",\"content\":\"这卧室简直好看到离大谱!!谁看了不会迷糊呢?\\n令设计简约，灯光也是真的舒适，就是理想中的设计感和灯光效果。\\n干净的地板配上舒适的灯光，让家变得好看的同时有了温度。\\n@灯具参数\\n格栅灯10W4500K\\n防眩射灯9W4500K\\n线性灯带5W/M4500K\\n背景墙柔性灯带 5W/M 3500K\\n天花灯槽明装线性灯5W/M 4500K\\n灯带均可做智能RGB颜色\"},{\"title\":\"我悟了!极简风卧室就该这样装\",\"image\":[\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/14/1.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/14/2.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/14/3.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/14/4.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/14/5.jpg\"],\"favorite\":1151,\"avatar\":\"https://pic2.zhimg.com/v2-cf19ba74d938bab190afa35215716e71_b.jpg\",\"writename\":\"我是臭臭吖\",\"content\":\"20平极简卧室就该这样装图\\n整体硬装十软装是深灰+咖色+浅灰\\n奶咖色中象床一真的高级拉满~\\n大气的外表不占底地盘的床，很难不心动\\n拥有细腻柔软的靠感，C2男生女生都会爱上\\n再加上床头柜和一些小摆件，高级两个字呼之欲任2\"},{\"title\":\"普通人穷装的奶油卧室，居然有一点点高级!\",\"image\":[\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/15/1.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/15/2.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/15/3.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/15/4.jpg\",\"https://image-1304455659.cos.ap-nanjing.myqcloud.com/HomePaint/15/5.jpg\"],\"favorite\":561,\"avatar\":\"https://pic3.zhimg.com/v2-048bc424ae74fe4f4bbe904962d6bf86_b.jpg\",\"writename\":\"咪露\",\"content\":\"早晨看着还普普通通的卧室，当傍晚夕阳照进卧室的那一刻，瞬间温柔了好多。\\n卧室16平左右，1.8m的泡芙床，奶油色系的梳妆台、床头柜、窗帘和地毯，墙面钱滩银珠通刷，奶白色的大衣柜，真没想到，简简单单的软装布置出来，还有一点点高级感!最爱在落地窗前发呆，看看夕阳，岁月静好。\"}]},{\"name\":\"沙发\",\"data\":[]}]";
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "查询成功");
        map.put("data", a);
        return map;
    }

}
