package team.ag.knmap.gather.article.scheduler;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.DuplicateRemovedScheduler;
import us.codecraft.webmagic.scheduler.MonitorableScheduler;
import us.codecraft.webmagic.scheduler.component.DuplicateRemover;

/**
 * @author panzejia
 * queue 用来存储待爬URL
 * set 用来去重
 */

public class RedisScheduler extends DuplicateRemovedScheduler implements MonitorableScheduler, DuplicateRemover {
    protected JedisPool pool;
    //s 为 spider 的标示，如果key太长redis无法识别

    private static final String QUEUE_PREFIX = "s_queue_";
    private static final String SET_PREFIX = "s_set_";
    private static final String ITEM_PREFIX = "s_item_";

    public RedisScheduler(String host) {
        this(new JedisPool(new JedisPoolConfig(), host));
    }

    public RedisScheduler(JedisPool pool) {
        this.pool = pool;
        this.setDuplicateRemover(this);
    }
    @Override
    public void resetDuplicateCheck(Task task) {
        Jedis jedis = this.pool.getResource();

        try {
            jedis.del(SET_PREFIX+"*");
        } finally {
            this.pool.returnResource(jedis);
        }

    }
    @Override
    public boolean isDuplicate(Request request, Task task) {
        Jedis jedis = this.pool.getResource();

        boolean var4;
        try {
            var4 = jedis.sadd(SET_PREFIX+request.getUrl(), new String[]{request.getUrl()}) == 0L;
        } finally {
            this.pool.returnResource(jedis);
        }

        return var4;
    }


    @Override
    protected void pushWhenNoDuplicate(Request request, Task task) {
        Jedis jedis = this.pool.getResource();

        try {
            jedis.rpush(this.getQueueKey(task), new String[]{request.getUrl()});
            if (request.getExtras() != null) {
                String field = DigestUtils.shaHex(request.getUrl());
                String value = JSON.toJSONString(request);
                jedis.hset(ITEM_PREFIX + task.getUUID(), field, value);
            }
        } finally {
            this.pool.returnResource(jedis);
        }
    }
    @Override
    public synchronized Request poll(Task task) {
        Jedis jedis = this.pool.getResource();

        Request var8;
        try {
            String url = jedis.lpop(this.getQueueKey(task));
            String key;
            if (url == null) {
                key = null;
                return null;
            }

            key = ITEM_PREFIX + task.getUUID();
            String field = DigestUtils.shaHex(url);
            byte[] bytes = jedis.hget(key.getBytes(), field.getBytes());
            Request request;
            if (bytes != null) {
                request = (Request)JSON.parseObject(new String(bytes), Request.class);
                var8 = request;
                return var8;
            }

            request = new Request(url);
            var8 = request;
        } finally {
            this.pool.returnResource(jedis);
        }

        return var8;
    }


    protected String getQueueKey(Task task) {
        return QUEUE_PREFIX + task.getUUID();
    }

    @Override
    public int getLeftRequestsCount(Task task) {
        Jedis jedis = this.pool.getResource();

        int var4;
        try {
            Long size = jedis.llen(this.getQueueKey(task));
            var4 = size.intValue();
        } finally {
            this.pool.returnResource(jedis);
        }

        return var4;
    }
    @Override
    public int getTotalRequestsCount(Task task) {
        Jedis jedis = this.pool.getResource();

        int var4;
        try {
            Long size = jedis.scard(SET_PREFIX+"*");
            var4 = size.intValue();
        } finally {
            this.pool.returnResource(jedis);
        }

        return var4;
    }

    public String getPreUrl(Task task){
        Jedis jedis = this.pool.getResource();
        return jedis.lpop(this.getQueueKey(task));
    }

    public boolean delSetKey(String url){
        Jedis jedis = this.pool.getResource();
        if(jedis.del(SET_PREFIX+url)==0){
            return false;
        }
        return true;
    }
}
