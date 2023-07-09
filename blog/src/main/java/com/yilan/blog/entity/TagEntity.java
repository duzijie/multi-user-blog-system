package com.yilan.blog.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author GroupOne
 * @email ${email}
 * @date 2020-09-11 11:08:28
 */
@Data
@TableName("tag")
public class TagEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 涓婚敭
	 */
	@TableId
	private Integer id;
	/**
	 * 鏍囩?鍚嶇О
	 */
	private String name;
	/**
	 * 澶栭敭瀵瑰簲blog琛?澶氬?涓
	 */
	private Integer blogId;

}
