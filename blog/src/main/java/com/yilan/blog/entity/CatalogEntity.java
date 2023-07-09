package com.yilan.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 
 * 
 * @author GroupOne
 * @email ${email}
 * @date 2020-09-11 11:08:28
 */
@Data
@TableName("catalog")
public class CatalogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 涓婚敭锛岃?琛ㄩ渶瑕佹墜宸ュ綍鍏ユ暟鎹
	 */
	@TableId
	private Integer id;
	/**
	 * 鍒嗙被鍚嶇О锛屾渶灏忛暱搴︿负2锛屾渶澶ч暱搴︿负30
	 */
	private String name;
	/**
	 * 鐖跺垎绫伙紝鑻ョ?A琛屾暟鎹?殑parent_id鍊肩瓑浜庣?B琛屾暟鎹?殑id鍊硷紝璇存槑B琛屾暟鎹?槸A琛屾暟鎹?殑鐖跺垎绫
	 */
	private Integer parentId;
	/**
	 * 涓夌骇鍒嗙被鐨勫眰绾э紝鍙?彇3涓?暟瀛楋細锛?銆?銆?锛?1浠ｈ〃涓?骇锛堥《绾э級鍒嗙被锛?浠ｈ〃璇ユ暟鎹?槸涓?骇鍒嗙被鐨勫効瀛
	 */
	private Integer catLevel;

	@TableField(exist = false)
	private List<CatalogEntity> children;
}
