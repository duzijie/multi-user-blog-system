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
@TableName("letter")
public class LetterEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 涓婚敭
	 */
	@TableId
	private Integer id;
	/**
	 * 澶栭敭瀵瑰簲user琛?紝涓??涓?叧绯
	 */
	private Integer fromUserId;
	/**
	 * 澶栭敭瀵瑰簲user琛?紝涓??涓?叧绯
	 */
	private Integer toUserId;
	/**
	 * 鐣欒█鍒涘缓鏃堕棿锛岀敱鏁版嵁搴撳垱寤
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 鐣欒█鐘舵?锛?-鐣欒█涓嶅睍绀猴紙琚?妇鎶ユ垨鑷??鍒犻櫎绛夊師鍥狅級 1-鍙戝竷鎴愬姛锛
	 */
	private Integer status;
	/**
	 * 鐣欒█鍐呭?锛屾渶灏忛暱搴︿负1锛屾渶澶ч暱搴︿负250
	 */
	private String content;

}
