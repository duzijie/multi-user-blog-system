package com.yilan.blog.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("notice")
public class NoticeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 涓婚敭
	 */
	@TableId
	private Integer id;
	/**
	 * 鏍囬?
	 */
	private String title;
	/**
	 * 鍐呭?
	 */
	private String content;
	/**
	 * 鍏?憡鍒涘缓鏃堕棿锛岀敱鏁版嵁搴撻粯璁ゅ垱寤
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 鍙戝竷鐘舵?锛?-涓嶅睍绀猴紙涓嶇?浠?箞鍘熷洜锛?1-灞曠ず
	 */
	private Integer status;

}
