package com.example.quartz.controller;

import com.example.quartz.domain.TUser;
import com.example.quartz.service.beanservice.Impl.UserServiceImpl4;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
public class RedisController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserServiceImpl4 userServiceImpl4;

    /**
     * 操作Redis字符串和散列类型数据
     * @return map
     */
    @RequestMapping("/stringAndHash")
    @ResponseBody
    public Map<String,Object> testStringAndHash(){
        log.info("进入testStringAndHash方法：");
//        Long startTime=System.currentTimeMillis();
//        int count=0;
//        while (true){
//            Long endTime=System.currentTimeMillis();
//            count++;
//            redisTemplate.opsForValue().set(count+"",count+"");
//            if(endTime-startTime>=1000){
//                break;
//            }
//        }
//        log.info("每秒插入数据次数："+ count);
        redisTemplate.opsForValue().set("key1","value1");
        //redisTemplate使用Jackson序列器，所以Redis保存时不是整数，不能运算
        redisTemplate.opsForValue().set("int_key","1");
        stringRedisTemplate.opsForValue().set("int","1");
        //使用运算
        stringRedisTemplate.opsForValue().increment("int",2);
        Map<String,String> hash=new HashMap<String,String>();
        hash.put("field1","value1");
        hash.put("field2","value2");
        //存入一个散列数据类型
        stringRedisTemplate.opsForHash().putAll("hash",hash);

        //新增一个字段
        stringRedisTemplate.opsForHash().put("hash","field3","value3");
        //绑定散列操作的key，这样可以连续对同一个散列数据类型进行操作
        BoundHashOperations hashOperations=stringRedisTemplate.boundHashOps("hash");
        hashOperations.delete("field1","field2");
        //新增一个字段
        hashOperations.put("field4","value5");
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("success",true);
        log.info("离开testStringAndHash方法");
        return map;
    }

    /**
     * 使用spring操作列表
     * @return map
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> testList(){
        //插入两个链表，注意它们在链表的顺序
        //链表从左到右的顺序为v10,v8,v6,v4,v2
        stringRedisTemplate.opsForList().leftPushAll("list1","v2","v4","v6","v8","v10");
        //链表从左到右为v1,v2,v3,v4,v5,v6
        stringRedisTemplate.opsForList().rightPushAll("list2","v1","v2","v3","v4","v5","v6");
        //绑定list2链表操作
        BoundListOperations<String, String> listOperations=stringRedisTemplate.boundListOps("list2");
        //从右边弹出一个成员
        Object result1=listOperations.rightPop();
        //获取定位元素，Redis从0开始计算，这里值为v2
        Object result2=listOperations.index(1);
        //从左边插入链表
        listOperations.leftPush("v0");
        //求链表长度
        Long size=listOperations.size();
        //求链表下标区间成员，整个链表下标范围为0到size-1，这里不取最后一个元素
        List<String> elements=listOperations.range(0,size-2);
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("success",true);
        return map;
    }

    /**
     * 使用Spring操作集合
     */
    @RequestMapping("/set")
    @ResponseBody
    public Map<String,Object> testSet(){
        //请注意：这里v1存在两次，因为集合不允许重复，所以只是插入5个成员到集合中
        stringRedisTemplate.opsForSet().add("set1","v1","v1","v2","v3","v4","v5");
        stringRedisTemplate.opsForSet().add("set2","v2","v4","v6","v8","v10");
        //绑定set1集合操作
        BoundSetOperations<String, String> setOperations=stringRedisTemplate.boundSetOps("set1");
        //增加两个元素
        setOperations.add("v6","v7");
        //删除两个元素
        setOperations.remove("v1","v7");
        //返回所有元素
        Set<String> set1=setOperations.members();
        //求成员数
        Long size=setOperations.size();
        //求交集
        Set<String> inter= setOperations.intersect ("set2");
        //求交集，并且用新集合 inter 保存 s
        setOperations.intersectAndStore (" set2", "inter") ;
        //求差集
        Set<String> diff = setOperations.diff ("set2");
        //求差集，并且用新集合 diff 保存
        setOperations.diffAndStore ("set2","diff");
        //求并集
        Set<String> union= setOperations.union ("set2");
        //求并集，并且用新集合 union 保存
        setOperations.unionAndStore("set2","union");
        Map<String, Object> map= new HashMap<String, Object>();
        map .put ("success", true);
        return map;
    }

    /**
     * 操作Redis有序集合
     */
    @RequestMapping("/zSet")
    @ResponseBody
    public Map<String,Object> testZset(){
        Set<ZSetOperations.TypedTuple<String>> typedTupleSet = new HashSet<>();
        for( int i=0; i<=9; i++){
            //分数
            double score=i*0.1;
            //创建一个TypedTuple对象，存入值和分数
            ZSetOperations.TypedTuple<String> typedTuple=new DefaultTypedTuple<String>("value"+i,score);
            typedTupleSet.add(typedTuple);
        }
        //往有序集合插入元素
        stringRedisTemplate.opsForZSet().add("zset1",typedTupleSet);
        //绑定zset1有序集合操作
        BoundZSetOperations<String,String> zSetOperations=stringRedisTemplate.boundZSetOps("zset1");
        //增加一个元素
        zSetOperations.add("value10",0.26);
        Set<String> setRange=zSetOperations.range(1,6);
        //按分数排序获取有序集合
        Set<String> setScore=zSetOperations.rangeByScore(0.2,0.6);
        //定义值范围
        RedisZSetCommands.Range range=new RedisZSetCommands.Range();
        range.gt("value3");
        range.lte("value8");
        //安置排序，请注意这个排序是按照字符串排序
        Set<String> setLex=zSetOperations.rangeByLex(range);
        //删除元素
        zSetOperations.remove("value9","value2");
        //求分数
        Double score=zSetOperations.score("value8");
        //在下标区间下，按分数排序，同时返回value和score
        Set<ZSetOperations.TypedTuple<String>> rangeSet=zSetOperations.rangeWithScores(1,6);
        //在分数区间下，按分数排序，同时返回value和score
        Set<ZSetOperations.TypedTuple<String>> scoreSet=zSetOperations.rangeByScoreWithScores(1,6);
        //按从大到小排序
        Set<String> reverseSet= zSetOperations.reverseRange(2,8);
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("success",true);
        return map;
    }

    /**
     * 通过Spring使用Redis的事务机制
     */
    public Map<String,Object> testMulti(){
        redisTemplate.opsForValue().set("key1","value1");
        List list=(List)redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations){
                //设置要监控key1
                operations.watch("key1");
                //开启事务，在exec命令执行前，全部都只是进入队列
                operations.multi();
                operations.opsForValue().set("key2","value2");
                Object value2=operations.opsForValue().get("key2");
                System.out.println("命令还在队列中，所以value2为null【"+value2+"】");
                //执行exec命令，将会先判断key1是否在被监控后又被修改，如果是则不执行
                return operations.exec();
            };
            });
        System.out.println(list);
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("success",true);
        return map;
    }

    @GetMapping("/getUser")
    public TUser getUser(String id){
        return userServiceImpl4.getUser(id);
    }
}
