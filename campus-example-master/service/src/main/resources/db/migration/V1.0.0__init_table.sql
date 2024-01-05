
DROP TABLE IF EXISTS `comments`;

CREATE TABLE `comments` (
                            `coid` bigint(20) NOT NULL COMMENT '评论主键',
                            `parent_id` bigint(20) DEFAULT NULL COMMENT '上级id',
                            `uid` bigint(20) DEFAULT NULL COMMENT '用户id',
                            `cid` bigint(20) DEFAULT NULL COMMENT '文章id',
                            `co_content` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '评论内容',
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            `is_deleted` tinyint(3) DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
                            PRIMARY KEY (`coid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `comments` */

insert  into `comments`(`coid`,`parent_id`,`uid`,`cid`,`co_content`,`create_time`,`update_time`,`is_deleted`) values
(1499942588285812737,0,1,1499941535293140994,'hello','2022-03-05 10:59:15','2022-03-05 10:59:15',0),
(1499942634171498497,1499942588285812737,1,1499941535293140994,'world','2022-03-05 10:59:26','2022-03-05 10:59:26',0);

/*Table structure for table `contents` */

DROP TABLE IF EXISTS `contents`;

CREATE TABLE `contents` (
                            `cid` bigint(20) NOT NULL COMMENT '内容主键',
                            `uid` bigint(20) DEFAULT NULL COMMENT '用户id',
                            `mid` bigint(20) DEFAULT NULL COMMENT '分类id',
                            `content` varchar(700) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '发表的内容',
                            `status` tinyint(3) DEFAULT NULL COMMENT '状态，0审核，1正常,2下架',
                            `ctype` tinyint(3) DEFAULT NULL COMMENT '文章：0文字，1带图片,2视频',
                            `picture_num` tinyint(3) DEFAULT NULL COMMENT '图片的数量',
                            `type` tinyint(3) DEFAULT NULL COMMENT '类型：0：不匿名，1匿名',
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            `is_deleted` tinyint(3) DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
                            PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `contents` */

insert  into `contents`(`cid`,`uid`,`mid`,`content`,`status`,`ctype`,`picture_num`,`type`,`create_time`,`update_time`,`is_deleted`) values
(1499941535293140994,1,1448839042677460994,'欢迎使用校园信息墙项目\n开源地址：https://gitee.com/oddfar/campus\n欢迎大家 Star 和 Fork 支持~',1,0,0,0,'2022-03-05 10:55:04','2022-03-05 10:57:28',0);

/*Table structure for table `fabulous` */

DROP TABLE IF EXISTS `fabulous`;

CREATE TABLE `fabulous` (
                            `fid` bigint(20) NOT NULL COMMENT '点赞主键',
                            `uid` bigint(20) DEFAULT NULL COMMENT '用户id',
                            `cid` bigint(20) DEFAULT NULL COMMENT '文章id',
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`fid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `fabulous` */

/*Table structure for table `file_info` */

DROP TABLE IF EXISTS `file_info`;

CREATE TABLE `file_info` (
                             `file_id` bigint(20) NOT NULL COMMENT '文件主键id',
                             `location` tinyint(4) NOT NULL COMMENT '文件存储位置：1-本地，2-网络，3-阿里云',
                             `bucket` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文件仓库（文件夹）',
                             `origin_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文件名称（上传时候的文件全名）',
                             `suffix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文件后缀，例如.txt',
                             `size_kb` bigint(20) DEFAULT NULL COMMENT '文件大小kb为单位',
                             `object_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '存储到bucket中的名称，主键id+.后缀',
                             `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '存储路径',
                             `state` tinyint(4) DEFAULT '0' COMMENT '状态（0：正常，1禁止，2已移除）',
                             `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             `is_deleted` tinyint(3) DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
                             PRIMARY KEY (`file_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='文件信息';

/*Data for the table `file_info` */

/*Table structure for table `metas` */

DROP TABLE IF EXISTS `metas`;

CREATE TABLE `metas` (
                         `mid` bigint(20) NOT NULL COMMENT '分类主键',
                         `name` varchar(30) NOT NULL COMMENT '分类名',
                         `slug` varbinary(30) NOT NULL COMMENT '缩略名',
                         `description` varchar(300) DEFAULT NULL COMMENT '描述',
                         `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         `is_deleted` tinyint(3) DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
                         PRIMARY KEY (`mid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `metas` */

insert  into `metas`(`mid`,`name`,`slug`,`description`,`create_time`,`update_time`,`is_deleted`) values
(1448839042677460994,'表白','confes','表白分类描述','2021-10-15 10:32:00','2022-03-01 15:56:39',0),
(1449212758636646402,'吐槽','debunk','吐槽描述','2021-10-16 11:17:01','2021-10-16 11:24:48',0),
(1465662542308495361,'实讯','solid','实时最新消息','2021-11-30 20:42:35','2022-03-01 15:56:12',0),
(1465662740514525185,'寻物','anyOther','寻找物品','2021-11-30 20:43:22','2022-03-01 15:55:28',0),
(1465663121806118913,'拼车','carpool','拼车出行','2021-11-30 20:44:53','2022-03-01 15:56:31',0);

/*Table structure for table `pictures` */

DROP TABLE IF EXISTS `pictures`;

CREATE TABLE `pictures` (
                            `pid` bigint(20) NOT NULL COMMENT '图片主键',
                            `uid` bigint(20) DEFAULT NULL COMMENT '用户id',
                            `cid` bigint(20) DEFAULT NULL COMMENT '文章id',
                            `file_id` varchar(200) DEFAULT NULL COMMENT '文件id',
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            `is_deleted` tinyint(3) DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
                            PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `pictures` */

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
                        `role_id` bigint(20) NOT NULL,
                        `name` varchar(30) NOT NULL COMMENT '角色名称',
                        `key` varchar(50) DEFAULT NULL COMMENT '角色权限字符串',
                        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                        `is_deleted` tinyint(3) DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
                        PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `role` */

insert  into `role`(`role_id`,`name`,`key`,`create_time`,`update_time`,`is_deleted`) values
(1,'超级管理员','superAdmin','2022-02-09 11:34:18','2022-02-09 11:34:18',0),
(2,'管理员','admin','2022-02-09 11:34:39','2022-02-09 11:34:39',0),
(3,'普通用户','common','2022-02-11 19:00:21','2022-02-11 19:00:21',0);

/*Table structure for table `sys_config` */

DROP TABLE IF EXISTS `sys_config`;

CREATE TABLE `sys_config` (
                              `config_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '参数主键',
                              `config_name` varchar(100) DEFAULT '' COMMENT '参数名称',
                              `config_key` varchar(100) DEFAULT '' COMMENT '参数键名',
                              `config_value` varchar(500) DEFAULT '' COMMENT '参数键值',
                              `config_type` char(1) DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
                              `group_code` varchar(100) DEFAULT NULL COMMENT '所属分类的编码',
                              `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                              `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              `is_deleted` tinyint(3) DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
                              PRIMARY KEY (`config_id`)
) ENGINE=InnoDB AUTO_INCREMENT=206 DEFAULT CHARSET=utf8 COMMENT='参数配置表';

/*Data for the table `sys_config` */

insert  into `sys_config`(`config_id`,`config_name`,`config_key`,`config_value`,`config_type`,`group_code`,`remark`,`create_time`,`update_time`,`is_deleted`) values
(2,'Linux本地文件保存路径','sys.local.file.save.path.linux','/data/campusFile','Y','file_config',NULL,'2022-01-14 10:59:39','2022-01-16 14:11:53',0),
(3,'Windows本地文件保存路径','sys.local.file.save.path.windows','D:\\campusFile','Y','file_config',NULL,'2022-01-14 11:00:39','2022-01-16 14:11:53',0),
(4,'默认存储文件的bucket名称','sys.file.default.bucket','defaultBucket','Y','file_config',NULL,'2022-01-14 11:03:10','2022-01-16 14:11:54',0),
(5,'文件默认部署的环境地址','sys.server.deploy.host','http://localhost:8160','Y','file_config',NULL,'2022-01-16 14:11:45','2022-01-16 14:18:55',0),
(6,'文件访问是否用nginx映射','sys.file.visit.nginx','false','Y','file_config','true真，false假','2022-01-16 14:40:00','2022-01-16 14:40:20',0),
(7,'信息墙删除且对应文件也删除','sys.file.is.delete','false','Y','file_config','true墙和文件都删除，false文件转移到别的目录','2022-01-17 14:29:11','2022-01-18 15:02:14',0),
(8,'文件默认转移的bucket名称','sys.file.move.default.bucket','moveBucket','Y','file_config',NULL,'2022-01-17 14:31:05','2022-01-18 15:09:48',0),
(101,'阿里云邮件服务accessKeyId','sys.aliyun.mail.accessKeyId','','Y','mail_config',NULL,'2022-01-19 10:04:08','2022-01-19 10:49:30',0),
(102,'阿里云邮件服务accessKeySecret','sys.aliyun.mail.accessKeySecret','','Y','mail_config',NULL,'2022-01-19 10:07:28','2022-01-19 10:49:31',0),
(113,'smtp服务器地址','sys.email.smtp.host','smtp.qq.com','Y','mail_config',NULL,'2022-01-19 10:33:50','2022-01-24 11:28:13',0),
(114,'smtp服务端口','sys.email.smtp.port','465','Y','mail_config',NULL,'2022-01-19 10:35:29','2022-01-24 11:28:14',0),
(115,'邮箱的发送方邮箱','sys.email.send.account','3066693006@qq.com','Y','mail_config',NULL,'2022-01-19 10:38:17','2022-01-24 11:28:15',0),
(116,'邮箱的密码或者授权码','sys.email.password','11111111111','Y','mail_config',NULL,'2022-01-19 10:07:31','2022-01-19 12:02:57',0),
(117,'邮箱发送时的用户名','sys.email.name','致远','Y','mail_config',NULL,'2022-01-19 11:10:47','2022-01-24 11:28:19',0),
(202,'用户默认头像','sys.user.default.image','https://img0.baidu.com/it/u=1183896628,1403534286&fm=253&fmt=auto&app=138&f=PNG','Y','sys_config',NULL,'2022-02-08 11:35:31','2022-02-08 11:40:15',0),
(203,'用户匿名头像','sys.user.anonym.image','https://iknow-pic.cdn.bcebos.com/d788d43f8794a4c2d0b5cf5409f41bd5ad6e393e','Y','sys_config',NULL,'2022-02-08 11:36:36','2022-02-08 11:37:38',0),
(204,'用户前端默认部署的环境地址','sys.user.service.deploy.host','http://localhost:3000','Y','sys_config',NULL,'2022-02-23 22:45:20','2022-02-23 22:46:33',0),
(205,'管理前端默认部署的环境地址','sys.admin.server.deploy.host','http://localhost:9530','Y','sys_config',NULL,'2022-02-23 22:46:16','2022-02-23 22:46:39',0);

/*Table structure for table `sys_dict` */

DROP TABLE IF EXISTS `sys_dict`;

CREATE TABLE `sys_dict` (
                            `dict_id` bigint(20) NOT NULL COMMENT '字典id',
                            `dict_code` varchar(100) NOT NULL COMMENT '字典编码',
                            `dict_name` varchar(100) NOT NULL COMMENT '字典名称',
                            `dict_type_code` varchar(100) NOT NULL COMMENT '字典类型的编码',
                            `dict_sort` decimal(10,2) DEFAULT NULL COMMENT '排序，带小数',
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            `is_deleted` tinyint(3) DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
                            PRIMARY KEY (`dict_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `sys_dict` */

insert  into `sys_dict`(`dict_id`,`dict_code`,`dict_name`,`dict_type_code`,`dict_sort`,`create_time`,`update_time`,`is_deleted`) values
(1,'sys_config','系统配置','config_group',100.00,'2022-01-12 15:00:36','2022-01-22 14:07:01',0),
(2,'mail_config','邮件配置','config_group',98.00,'2022-01-12 15:01:07','2022-01-14 10:56:28',0),
(3,'file_config','文件配置','config_group',99.00,'2022-01-14 10:56:19','2022-01-14 10:56:26',0);

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
                            `uid` bigint(20) NOT NULL COMMENT '用户ID',
                            `user_account` varchar(20) NOT NULL COMMENT '用户账号',
                            `user_name` varchar(30) NOT NULL COMMENT '用户昵称',
                            `user_email` varchar(50) NOT NULL COMMENT '用户邮箱',
                            `user_type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '用户类型（0为用户,1管理员，2系统管理员）',
                            `avatar` bigint(20) DEFAULT NULL COMMENT '用户头像的fileID',
                            `password` varchar(100) NOT NULL COMMENT '密码',
                            `status` tinyint(3) DEFAULT '1' COMMENT '状态（0：锁定 1：正常）',
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            `is_deleted` tinyint(3) DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
                            PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `sys_user` */

insert  into `sys_user`(`uid`,`user_account`,`user_name`,`user_email`,`user_type`,`avatar`,`password`,`status`,`create_time`,`update_time`,`is_deleted`) values
(1,'zhiyuan','致远','3066693006@qq.com',2,NULL,'$2a$10$SkCpAQd5Rg.Rq6q9cOn.ueUHIbSq3xFFyrJYlB5OvfKIThRvK8.Ju',1,'2022-02-09 13:53:54','2022-02-23 22:33:57',0),
(100,'test','test','zhiyuan@qq.com',0,NULL,'$2a$10$Qetvb.vByFgSajixHmv4FOW3aUETHZK/O.AkQLomI19eQtMWLHoGi',1,'2022-02-18 20:39:17','2022-03-05 10:38:04',0),
(200,'admin','admin','1234567@qq.com',1,NULL,'$2a$10$/8qSPK/g6KaH3yNl4b9NbOjni6EFSHj01nZSGFwEOyXB6rgI/ljh.',1,'2022-03-05 10:40:49','2022-03-05 10:41:31',0);

/*Table structure for table `user_role` */

DROP TABLE IF EXISTS `user_role`;

CREATE TABLE `user_role` (
                             `user_id` bigint(20) NOT NULL COMMENT '用户主键',
                             `role_id` bigint(20) NOT NULL COMMENT '权限主键',
                             PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `user_role` */

insert  into `user_role`(`user_id`,`role_id`) values
(1,1),
(100,3),
(200,2);

