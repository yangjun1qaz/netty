package com.honey.netty.prize;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang.math.RandomUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Description: 抽奖工具类
 * @author: xiake
 * @Date: 2020/1/5 13:23
 * @ModifiedDate：2020/1/5 13:23
 * @Copyright: miaoxaike.com
 */
public class PrizeMathRandom {



    public static int getPrizeIndex(List<Prize> prizes) {
        DecimalFormat df = new DecimalFormat("######0.00");
        int random = -1;
        try{
            //计算总权重
            double sumWeight = 0;
            for(Prize p : prizes){
                sumWeight += p.getPrize_weight();
            }

            //产生随机数
            double randomNumber;
            randomNumber = Math.random();

            //根据随机数在所有奖品分布的区域并确定所抽奖品
            double d1 = 0;
            double d2 = 0;
            for(int i=0;i<prizes.size();i++){
                d2 += Double.parseDouble(String.valueOf(prizes.get(i).getPrize_weight()))/sumWeight;
                if(i==0){
                    d1 = 0;
                }else{
                    d1 +=Double.parseDouble(String.valueOf(prizes.get(i-1).getPrize_weight()))/sumWeight;
                }
                if(randomNumber >= d1 && randomNumber <= d2){
                    random = i;
                    break;
                }
            }
        }catch(Exception e){
            System.out.println("生成抽奖随机数出错，出错原因：" +e.getMessage());
        }
        return random;
    }





    public static void main(String[] args) {
        //初始化奖品信息
        List<Prize> prizeList=new ArrayList<>();
        prizeList.add(new Prize(1,"一等奖",1,10));
        prizeList.add(new Prize(2,"二等奖",4,2));
        prizeList.add(new Prize(3,"三等奖",5,3));
        int[] result=new int[4];
        System.out.println("抽奖开始");
        for (int i = 0; i < 10000; i++)// 打印100个测试概率的准确性
        {
            int selected=getPrizeIndex(prizeList);
            System.out.println("第"+i+"次抽中的奖品为："+prizeList.get(selected).getPrize_name());
            result[selected]++;
            System.out.println("--------------------------------");
        }
        System.out.println("抽奖结束");
        System.out.println("每种奖品抽到的数量为：");
        System.out.println("一等奖："+result[0]);
        System.out.println("二等奖："+result[1]);
        System.out.println("三等奖："+result[2]);
        System.out.println("四等奖："+result[3]);
    }
}