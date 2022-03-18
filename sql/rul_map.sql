

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for rul_map
-- ----------------------------
DROP TABLE IF EXISTS `rul_map`;
CREATE TABLE `rul_map`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增编号',
  `rul_key` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '返回地址专用key',
  `re_rul` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务地址',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `rul_key`(`rul_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
