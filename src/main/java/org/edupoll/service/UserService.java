package org.edupoll.service;

import java.util.Optional;

import org.edupoll.model.dto.LoginRequestData;
import org.edupoll.model.entity.User;
import org.edupoll.model.entity.UserDetail;
import org.edupoll.repository.UserDetailRepository;
import org.edupoll.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	UserDetailRepository userDetailRepository;

	@Transactional
	public boolean deleteSpecificUser(String userId) {
		// userId 에 해당하는 데이터를 userRepository 에서 지우고
		if (!userRepository.findById(userId).isPresent()) {
			return false;
		}
		Integer userDetailIdx = userRepository.findById(userId).get().getUserDetailIdx();

		userRepository.deleteById(userId);
		// 쓰고 있던 user_detail 이 있었으면 이것도 지우는 작업
		if (userDetailIdx != null)
			userDetailRepository.deleteById(userDetailIdx);
		return true;
	}

	// 회원상세정보를 수정/변경 처리할 서비스 메서드
	public boolean modifySpecificUserDetail(String userId, UserDetail detail) {
		// 1. 특정 유저가 존재하는지 확인
		if (userRepository.findById(userId).isEmpty())
			return false;
		// 2. UserDetail 저장하고
		User foundUser = userRepository.findById(userId).get();
		if (foundUser.getUserDetailIdx() != null)
			detail.setIdx(foundUser.getUserDetailIdx());

		UserDetail saved = userDetailRepository.save(detail);
		// 3. 특정 유저의 detail_idx 에 방금 저장하며 부여받은 id 값을 설정해서 update
		foundUser.setUserDetailIdx(saved.getIdx());
		userRepository.save(foundUser);

		return true;
	}

	// 회원 가입을 처리할 서비스 메서드
	public boolean createNewUser(User user) {
		if (userRepository.findById(user.getId()).isEmpty()) {
			// user.setJoinDate(new Date());
			userRepository.save(user);
			return true;
		}
		return false;
	}

	// 로그인 처리를 하기 위해 사용할 서비스 메서드
	public boolean isValidUser(LoginRequestData data) { // id, pass
		Optional<User> optional = userRepository.findById(data.getLoginId());
		if (optional.isPresent()) {
			String savedPass = optional.get().getPass();
			return savedPass.equals(data.getLoginPass());
		}
		return false;
	}

	public UserDetail findSpecificSavedDetail(String logonId) {
		// logonId로 유저 정보 찾아서 그 유저의 detail_idx 찾아서
		Integer detailIdx = userRepository.findById(logonId).get().getUserDetailIdx();
		if (detailIdx == null)
			return null;

		// 그걸 유저 상세 찾아서 리턴
		return userDetailRepository.findById(detailIdx).orElse(null);
	}

}
