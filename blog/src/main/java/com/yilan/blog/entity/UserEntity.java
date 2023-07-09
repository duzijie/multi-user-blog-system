package com.yilan.blog.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 
 * 
 * @author GroupOne
 * @email ${email}
 * @date 2020-09-11 11:08:28
 */
@Data
@TableName("user")
public class UserEntity implements Serializable, UserDetails {
	private static final long serialVersionUID = 1L;

	/**
	 * 涓婚敭
	 */
	@TableId
	private Integer id;
	/**
	 * 濮撳悕锛屾渶灏忛暱搴︿负2锛屾渶澶ч暱搴︿负20
	 */
	private String name;
	/**
	 * 閭??锛屾渶澶ч暱搴︿负50锛屼笖unique
	 */
	private String email;
	/**
	 * 鐢ㄦ埛璐﹀彿锛屾渶灏忛暱搴︿负3锛屾渶澶ч暱搴︿负20锛屼笖unique
	 */
	private String username;
	/**
	 * 瀵嗙爜锛屾渶澶ч暱搴︿负100
	 */
	private String password;
	/**
	 * 澶村儚鍥剧墖鍦板潃锛屾渶澶ч暱搴︿负200
	 */
	private String avatar;
	/**
	 * 鐢佃瘽鍙风爜
	 */
	private String phone;
	/**
	 * 鎬у埆 0-濂?1-鐢
	 */
	private Integer sex;
	/**
	 * 
	 */
	private Date birthday;
	/**
	 * 鍦板潃
	 */
	private String address;
	/**
	 * 璐﹀彿閿佸畾锛?-姝ｅ父 1-閿佸畾
	 */
	private Integer islock;
	/**
	 * 璐︽埛鍒涘缓鏃堕棿锛岀敱鏁版嵁搴撻粯璁ゅ垱寤
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 涓?汉绠?粙
	 */
	private String introduction;
	/**
	 * 涓??绛惧悕
	 */
	private String motto;
	/**
	 * 澶栭敭瀵瑰簲style琛?紝澶氬?涓?叧绯伙紝涓?釜style瀵瑰簲澶氫釜user
	 */
	private Integer styleId;
	/**
	 * 鐢ㄦ埛瑙掕壊锛孯OLE_USER-鏅??鐢ㄦ埛 ROLE_ADMIN-绠＄悊鍛?ROLE_SUPER-瓒呯骇绠＄悊鍛
	 */
	private String roles;

	// 验证码
	private String verification;

	//数据库中不存在该字段
	@TableField(exist = false)
	private List<GrantedAuthority> authorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return ( this.getIslock() == 0 ? true : false );
	}
}
