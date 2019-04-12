###导出 sms-center 的数据库结构
CREATE DATABASE IF NOT EXISTS `log-center` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `log-center`;
SET FOREIGN_KEY_CHECKS=0;
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COMMENT '用户名',
  `module` varchar(3000)  COMMENT '模块名',
  `params` text COMMENT '方法参数',
  `remark` text COMMENT '备注',
  `flag` tinyint(1) NOT NULL,
  `createTime` datetime NOT NULL,
  PRIMARY KEY (`id`) 
) ENGINE=archive DEFAULT CHARSET=utf8mb4;