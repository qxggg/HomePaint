package com.homepainter.service;

import com.homepainter.mapper.GoodsMapper;
import com.homepainter.util.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class DataControlService {


    @Autowired
    GoodsMapper goodsMapper;

    public List<HashMap<String, Object>> dataService() {


        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        int[] user = RandomUtils.getRandomNumbers(50, 1, 2000, false);
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

        int[] randomClick = RandomUtils.getRandomNumbers(50, 0, 50, true);
        int[] randomLike = RandomUtils.getRandomNumbers(50, 0, 1, true);
        int[] randomUse = RandomUtils.getRandomNumbers(50, 0, 1, true);
        int[] randomConsum = RandomUtils.getRandomNumbers(50, 0, 1, true);
        int[] randomView = RandomUtils.getRandomNumbers(50, 0, 10000, true);

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


}
