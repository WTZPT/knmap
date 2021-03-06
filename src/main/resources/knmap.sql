DROP TABLE IF EXISTS `spider_info`;
CREATE TABLE `spider_info` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '用于管理系统分类的名称',
  `display_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '用于文章展示时的名称',
  `db_name` varchar(255) NOT NULL COMMENT '数据库名',
  `thread` int(11) DEFAULT '1' COMMENT '抓取线程数',
  `retry` int(11) DEFAULT '2' COMMENT '失败的网页重试次数',
  `sleep` int(11) DEFAULT '0' COMMENT '抓取每个网页睡眠时间',
  `max_page_gather` int(11) DEFAULT '100' COMMENT '最大抓取网页数量,0代表不限制',
  `timeout` int(11) DEFAULT '5000' COMMENT 'HTTP链接超时时间',
  `start_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '起始页面',
  `list_page_url_reg` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '下一页链接的Xpath',
  `article_url_reg` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '正文链接的正则表达式，用于发现正文地址',
  `article_url_xpath` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '列表的XPATH',
  `article_s_xpath` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'spo_s的Xpath',
  `article_s_reg` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'spo_s的正则表达式',
  `article_o_xpath` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'spo_o的Xpath',
  `article_o_reg` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'spo_o的正则表达式',
  `article_p_xpath` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'spo_p的正则表达式',
  `article_p_reg` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'spo_p的Xpath',
  `dynamic_site` int(1) DEFAULT '0' COMMENT '是否是动态加载网站,如果是则使用selenium下载器',
    `charset` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8;