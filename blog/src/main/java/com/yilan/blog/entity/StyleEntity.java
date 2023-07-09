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
@TableName("style")
public class StyleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 涓婚敭
	 */
	@TableId
	private Integer id;
	/**
	 * 涓婚?鍚嶇О
	 */
	private String name;
	/**
	 * 鑳屾櫙鍥剧墖鍦板潃
	 */
	private String background;
	/**
	 * 瀵艰埅鑹
	 */
	private String nav;

}
