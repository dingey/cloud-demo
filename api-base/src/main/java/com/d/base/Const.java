package com.d.base;

@SuppressWarnings("unused")
public class Const {
    //配置名称
    public static final String DEV = "dev";//开发
    public static final String PRE = "pre";//预发布
    public static final String PROD = "prod";//生产
    public static final String PROFILE_DEV = "dev";//开发
    public static final String PROFILE_PRE = "pre";//预发布
    public static final String PROFILE_PROD = "prod";//生产

    public static final long ORDER_EXPIRE_TIME = 1800;//订单支付限时：秒

    //缓存队列名称
    public static final String ORDER_QUEUE = "order.queue";//下单队列
    public static final String ORDER_CONFIRM_QUEUE = "order.confirm.queue";//下单成功队列
    public static final String ORDER_CANCEL_QUEUE = "order.cancel.queue";//订单取消队列
    public static final String ORDER_PAID_QUEUE = "order.paid.queue";//订单支付成功队列

    //订阅频道
    public static final String TOPIC_ORDER_SUBMIT = "topic.order.submit";//订单提交
    public static final String TOPIC_ORDER_CREATE = "topic.order.create";//订单创建
    public static final String TOPIC_ORDER_CHECK = "topic.order.check";//订单校验，扣库存
    public static final String TOPIC_ORDER_CHECK_FAIL = "topic.order.check.fail";//订单校验，扣库存
    public static final String TOPIC_ORDER_CHECK_SUCCESS = "topic.order.check.fail";//订单校验，扣库存
    public static final String TOPIC_ORDER_CANCEL = "topic.order.cancel";//订单取消
    public static final String TOPIC_ORDER_CLOSE = "topic.order.close";//订单关闭
    public static final String TOPIC_ORDER_FAIL = "topic.order.fail";//下单失败，库存不足等需回退
    public static final String TOPIC_ORDER_PAY = "topic.order.pay";//订单支付成功

    //缓存key
    public static final String CACHE_KEY_STOCK = "cache.key.stock";//库存
    public static final String CACHE_KEY_CALL_COUNT = "cache.key.call.count";//调用次数
    public static final String CACHE_KEY_ORDER = "cache.key.order";//订单状态
    public static final String CACHE_KEY_ORDER_EXPIRE = "cache.key.order.expire";//订单状态
    public static final String CACHE_KEY_AUTH_PATH = "cache.key.auth.path";//订单状态

    //CRON表达式
    public static final String CRON_EVERY_SECONDS = "0/1 * * * * ?";//每秒
    public static final String CRON_EVERY_MINUTES = "0 0/1 * * * ?";//每分钟
    public static final String CRON_EVERY_FIVE_MINUTES = "0 0/5 * * * ?";//每5分钟
    public static final String CRON_HALF_HOUR = "0 */30 * * * ?";//每30分钟
    public static final String CRON_EVERY_HOUR = "0 0 0/1 * * ?";//每小时
    public static final String CRON_EVERY_DAY = "0 0 0 * * ?";//每天凌晨0点
    public static final String CRON_EVERY_WEEK = "0 0 0 ? * MON";//每周凌晨0点
    public static final String CRON_EVERY_MONGTH = "0 0 0 1 * ?";//每月1号凌晨0点

    //lock_key
    public static final String LOCK_KEY_JOB = "lock.key.job"; //定时任务
}
