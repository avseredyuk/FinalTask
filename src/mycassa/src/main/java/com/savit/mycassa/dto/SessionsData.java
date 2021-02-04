package com.savit.mycassa.dto;

import java.util.List;

import com.savit.mycassa.entity.session.Session;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SessionsData {

	private List<Session> sessionsData;
	
}
