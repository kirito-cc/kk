package com.sxt.sys.vo;

import com.sxt.sys.domain.Role;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
public class RoleVo extends Role{
private static final long serialVersionUID = 1L;

	
	private Integer page=1;
	private Integer limit=10;
}
