package com.homepainter.service;

import com.homepainter.mapper.GoodsMapper;
import com.homepainter.util.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataControlService {


    @Autowired
    GoodsMapper goodsMapper;

    public List<HashMap<String, Object>> dataService() {


        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        Random random = new Random();
        int[] user = RandomUtils.getRandomNumbers(random, 50, 1, 2000, false);
        //

        int[] click = new int[2001];
        int[] style = new int[2001];
        int[] like = new int[2001];
        int[] use = new int[2001];
        int[] consum = new int[2001];
        int[] view = new int[2001];

        double[] comment = new double[2001];
        for (int com = 0; com < comment.length; ++com)
            comment[com] = 0.5;


        Arrays.fill(click, 0);
        Arrays.fill(like, 0);
        Arrays.fill(use, 0);
        Arrays.fill(consum, 0);
        Arrays.fill(view, 0);


        int[] randomClick = RandomUtils.getRandomNumbers(random, 19,0, 50, true);
        int[] randomLike = RandomUtils.getRandomNumbers(random, 19,0, 1, true);
        int[] randomUse = RandomUtils.getRandomNumbers(random, 19,0, 1, true);
        int[] randomConsum = RandomUtils.getRandomNumbers(random, 19,0, 1, true);
        int[] randomView = RandomUtils.getRandomNumbers(random, 19,0, 10000, true);

        for (int i = 1, j = 0; i < user.length; ++i) {
            click[user[i]] = randomClick[j];
            comment[user[i]] = RandomUtils.generateRandomNumber();
            like[user[i]] = randomLike[j];
            consum[user[i]] = randomConsum[j];
            view[user[i]] = randomView[j];
            use[user[i]] = randomUse[j++];
        }

        //




        for (int i = 1; i < click.length; ++i){
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", i);
            map.put("click", click[i]);
            map.put("comment", comment[i]);
            map.put("collect", like[i]);
            map.put("use", use[i]);
            map.put("consum", consum[i]);
            map.put("view", view[i]);
            map.put("style", goodsMapper.getStyleById(i));
            list.add(map);
        }
        System.out.println(list);
        return list;
    }


    public List<HashMap<String, Object>> styleService(){
        List<HashMap<String, Object>> list = new ArrayList<>();

        Random random = new Random();






        for(int p = 1; p <= 10000; ++p) {
            HashMap<String, Object> map = new HashMap<>();
            int[] randomClick = RandomUtils.getRandomNumbers(random,19, 0, 1000, true);
            int[] randomCollect= RandomUtils.getRandomNumbers(random,19, 0, 1000, true);
            int[] randomConsume = RandomUtils.getRandomNumbers(random,19, 0, 1000, true);
            int[] randomView = RandomUtils.getRandomNumbers(random,19, 0, 10000, true);
            int[] randomSearchClick = RandomUtils.getRandomNumbers(random,19, 0, 1000, true);
            int[] randomUse = RandomUtils.getRandomNumbers(random,19, 0, 1000, true);
            int[] intComment = RandomUtils.getRandomNumbers(random,19, 0, 100000, true);
            double[] randomComment = new double[19];
            for(int i = 0; i < randomComment.length; ++i) randomComment[i] = intComment[i] / 100000.00;

            map.put("randomClick", Arrays.toString(randomClick));
            map.put("randomCollect", Arrays.toString(randomCollect));
            map.put("randomConsume", Arrays.toString(randomConsume));
            map.put("randomView", Arrays.toString(randomView));
            map.put("randomSearchClick", Arrays.toString(randomSearchClick));
            map.put("randomComment", Arrays.toString(randomComment));
            map.put("randomUse", Arrays.toString(randomUse));
            list.add(map);
        }
        return list;
    }

    public static void main(String[] args) {
        List<HashMap<String, Object>> list = new ArrayList<>();



        double[] randomComment = new double[19];
        for(int i = 1; i < randomComment.length; ++i) randomComment[i] = RandomUtils.generateRandomNumber();


        Random random = new Random();
        for(int p = 1; p <= 10000; ++p) {
            HashMap<String, Object> map = new HashMap<>();
            int[] randomClick = RandomUtils.getRandomNumbers(random,19, 0, 1000, true);
            int[] randomCollect= RandomUtils.getRandomNumbers(random,19, 0, 1000, true);
            int[] randomConsume = RandomUtils.getRandomNumbers(random,19, 0, 1000, true);
            int[] randomView = RandomUtils.getRandomNumbers(random,19, 0, 10000, true);
            int[] randomSearchClick = RandomUtils.getRandomNumbers(random,19, 0, 1000, true);
            int[] randomUse = RandomUtils.getRandomNumbers(random,19, 0, 1000, true);
            map.put("id", p);
            map.put("randomClick", Arrays.toString(randomClick));
            map.put("randomCollect", Arrays.toString(randomCollect));
            map.put("randomConsume", Arrays.toString(randomConsume));
            map.put("randomView", Arrays.toString(randomView));
            map.put("randomSearchClick", Arrays.toString(randomSearchClick));
            map.put("randomComment", Arrays.toString(randomComment));
            map.put("randomUse", Arrays.toString(randomUse));
            list.add(map);
        }
        System.out.println(list);
    }

}
