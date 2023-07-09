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
@TableName("user_to_user")
public class UserToUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 涓婚敭
	 */
	@TableId
	private Integer id;
	/**
	 * 澶栭敭瀵瑰簲user琛
	 */
	private Integer fromUserId;
	/**
	 * 澶栭敭瀵瑰簲user琛
	 */
	private Integer toUserId;
	/**
	 * 鐢ㄦ埛涔嬮棿鐨勫叧绯荤姸鎬佸?锛?-濂藉弸锛?-鐢宠?涓?紝2-fromUser灞忚斀toUser
	 */
	private Integer status;
	/**
	 * 璁板綍鐨勫垱寤烘椂闂达紝榛樿?鐢辨暟鎹?簱鍒涘缓
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;

}
